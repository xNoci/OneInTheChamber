package me.noci.oitc.gameutils;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import me.noci.noclib.utils.LocationUtils;
import me.noci.oitc.OITC;
import me.noci.oitc.mapmanager.Map;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class Game {

    public static final int MIN_PLAYER_SPAWNS = 16;

    private final HashMap<UUID, PlayerData> playerDataMap = Maps.newHashMap();
    private final JavaPlugin plugin;

    @Getter @Setter private Map currentMap;
    @Getter private final Set<UUID> playerSet = Sets.newHashSet();
    @Getter private final Set<UUID> spectatorSet = Sets.newHashSet();
    @Getter private final int maxPlayers;
    @Getter private final int playersNeeded;
    @Getter @Setter(AccessLevel.PRIVATE) private int timeToStart;
    @Getter @Setter(AccessLevel.PRIVATE) private long gameDuration ;
    @Getter @Setter(AccessLevel.PRIVATE) private int forceTime;
    @Setter private Location lobbySpawn;

    public static Game setupGame(JavaPlugin plugin) {
        FileConfiguration config = plugin.getConfig();
        Game game = new Game(plugin, config.getInt("playersToStart", 12));
        game.setTimeToStart(config.getInt("timeToStart", 60));
        game.setGameDuration(TimeUnit.MINUTES.toSeconds(config.getInt("gameDuration", 5)));
        game.setForceTime(config.getInt("forceTime", 10));

        Location lobbySpawn = LocationUtils.locationFromStringSilently(config.getString("lobbySpawn", ""));
        if(lobbySpawn != null) {
            game.setLobbySpawn(lobbySpawn);
        } else {
            Bukkit.getConsoleSender().sendMessage(String.format("%s§cEs wurde noch kein Lobby-Spawn gesetzt.", OITC.PREFIX));
        }
        return game;
    }

    private Game(JavaPlugin plugin, int maxPlayers) {
        this.plugin = plugin;
        this.maxPlayers = maxPlayers;
        this.playersNeeded = Math.max((int) (maxPlayers * 0.3), 3);
    }

    public void removePlayerData(UUID uuid) {
        playerDataMap.remove(uuid);
    }

    public PlayerData getPlayerData(UUID uuid) {
        if (playerDataMap.containsKey(uuid)) return playerDataMap.get(uuid);
        PlayerData playerData = new PlayerData(uuid);
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
}
