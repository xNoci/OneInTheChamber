package me.noci.oitc.gameutils;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import me.noci.noclib.utils.LocationUtils;
import me.noci.noclib.utils.items.AdvancedItemStack;
import me.noci.oitc.OITC;
import me.noci.oitc.mapmanager.Map;
import me.noci.oitc.mapmanager.MapManager;
import me.noci.oitc.mapmanager.MapData;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class Game {

    public static final int MIN_PLAYER_SPAWNS = 16;

    private final HashMap<UUID, PlayerData> playerDataMap = Maps.newHashMap();
    private final JavaPlugin plugin;
    private final MapManager mapManager;

    @Getter @Setter private Map currentMap;
    @Getter private final Set<UUID> playerSet = Sets.newHashSet();
    @Getter private final Set<UUID> spectatorSet = Sets.newHashSet();
    @Getter private final int maxPlayers;
    @Getter private final int playersNeeded;
    @Setter private Location lobbySpawn;
    @Getter private boolean mapLoaded = false;

    @Getter @Setter(AccessLevel.PRIVATE) private int ffaServerDuration;
    @Getter @Setter(AccessLevel.PRIVATE) private int gameDuration;
    @Getter @Setter(AccessLevel.PRIVATE) private int timeToStart;
    @Getter @Setter(AccessLevel.PRIVATE) private int forceTime;
    @Getter @Setter(AccessLevel.PRIVATE) private int protectionTime;

    public static Game setupGame(JavaPlugin plugin, MapManager mapManager) {
        FileConfiguration config = plugin.getConfig();
        Game game = new Game(plugin, mapManager, config.getInt("maxPlayers", 12));
        game.setFfaServerDuration(getConfigSeconds(config, TimeUnit.MINUTES, "ffaGameDuration", 60));
        game.setGameDuration(getConfigSeconds(config, TimeUnit.MINUTES, "gameDuration", 5));
        game.setTimeToStart(config.getInt("timeToStart", 60));
        game.setForceTime(config.getInt("forceTime", 10));
        game.setProtectionTime(config.getInt("protectionTime", 3));

        Location lobbySpawn = LocationUtils.locationFromStringSilently(config.getString("lobbySpawn", ""));
        if (lobbySpawn != null) {
            game.setLobbySpawn(lobbySpawn);
        } else {
            Bukkit.getConsoleSender().sendMessage(String.format("%s??cEs wurde noch kein Lobby-Spawn gesetzt.", OITC.PREFIX));
        }
        return game;
    }

    public static void giveItems(Player player) {
        player.getInventory().clear();
        player.getInventory().addItem(new AdvancedItemStack(Material.WOOD_SWORD).addItemFlags().setUnbreakable(true));
        player.getInventory().addItem(new AdvancedItemStack(Material.BOW).addItemFlags().setUnbreakable(true));
        player.getInventory().addItem(new AdvancedItemStack(Material.ARROW).addItemFlags());
        player.updateInventory();
    }

    private static int getConfigSeconds(FileConfiguration config, TimeUnit timeUnit, String configPath, int def) {
        int value = config.getInt(configPath, def);
        return ((Long) timeUnit.toSeconds(value)).intValue();
    }


    private Game(JavaPlugin plugin, MapManager mapManager, int maxPlayers) {
        this.plugin = plugin;
        this.mapManager = mapManager;
        this.maxPlayers = maxPlayers;
        this.playersNeeded = Math.max((int) (maxPlayers * 0.2), 2);
    }

    public void removePlayerData(UUID uuid) {
        playerDataMap.remove(uuid);
    }

    public PlayerData getPlayerData(Player player) {
        UUID uuid = player.getUniqueId();
        if (playerDataMap.containsKey(uuid)) return playerDataMap.get(uuid);
        PlayerData playerData = new PlayerData(player);
        playerDataMap.put(uuid, playerData);
        return playerData;
    }

    public List<PlayerData> getPlayerDataSorted() {
        return playerDataMap.values()
                .stream()
                .sorted(Comparator.comparingInt(PlayerData::getScore).reversed())
                .collect(Collectors.toList());
    }

    private Location getLobbySpawn() {
        return this.lobbySpawn == null ? Bukkit.getWorlds().get(0).getSpawnLocation() : this.lobbySpawn;
    }

    public void teleportToLobby(Player player) {
        new BukkitRunnable() {
            @Override
            public void run() {
                Location location = getLobbySpawn();
                player.teleport(location);
            }
        }.runTask(plugin);
    }

    public String getMapName() {
        return currentMap == null ? "Unknown" : currentMap.get(MapData.MAP_NAME, String.class);
    }

    public void setupWorld() {
        mapManager.copyWorldToServer(currentMap.get(MapData.WORLD_NAME, String.class));
        currentMap.setupWorld(plugin);
        this.mapLoaded = true;
    }

    private static final SplittableRandom RANDOM = new SplittableRandom();

    public void spawnPlayer(Player player) {
        Map map = currentMap;
        List<Location> spawns = map.getPlayerSpawns();
        Location location = spawns.get(RANDOM.nextInt(spawns.size()));
        player.teleport(location);
    }
}
