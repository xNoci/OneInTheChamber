package me.noci.oitc.mapmanager;

import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.Setter;
import me.noci.noclib.utils.LocationUtils;
import me.noci.oitc.gameutils.Game;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.WorldCreator;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

public class Map {

    public static final String DEFAULT_MAP_NAME = "Unknown";

    @Getter @Setter private String mapName = DEFAULT_MAP_NAME;
    @Getter @Setter private String builderName = "Unknown";
    @Getter @Setter private String worldName = null;
    @Getter @Setter private Location spectatorSpawn = null;
    @Setter private List<String> rawPlayerSpawns = Lists.newArrayList();
    private final List<Location> playerSpawns = Lists.newArrayList();

    protected Map() {
        this(null);
    }

    protected Map(String mapName) {
        if (StringUtils.isNotBlank(mapName)) {
            this.mapName = mapName;
        }
    }

    public boolean isValid() {
        if (mapName.equals(DEFAULT_MAP_NAME)) return false;
        if (spectatorSpawn == null) return false;
        if (worldName == null) return false;
        return rawPlayerSpawns.size() >= Game.MIN_PLAYER_SPAWNS;
    }

    public void setupWorld(JavaPlugin plugin) {
        new BukkitRunnable() {
            @Override
            public void run() {
                Bukkit.createWorld(WorldCreator.name(worldName));
                loadSpawns();
            }
        }.runTask(plugin);
    }

    private void loadSpawns() {
        List<Location> locations = rawPlayerSpawns.stream().map(LocationUtils::locationFromStringSilently).collect(Collectors.toList());
        playerSpawns.clear();
        playerSpawns.addAll(locations);
    }

    public List<Location> getPlayerSpawns() {
        return Lists.newArrayList(this.playerSpawns);
    }

    public Iterator<Location> playerSpawnIterator() {
        return playerSpawns.iterator();
    }

    public List<String> getRawPlayerSpawns() {
        return Lists.newArrayList(this.rawPlayerSpawns);
    }

    public void addRawPlayerSpawn(Location location) {
        this.rawPlayerSpawns.add(LocationUtils.locationToStringSilently(location));
    }

}
