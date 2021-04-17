package me.noci.oitc.mapmanager;

import com.google.common.collect.Lists;
import me.noci.noclib.utils.LocationUtils;
import me.noci.oitc.gameutils.Game;
import me.noci.oitc.mapmanager.settings.MapData;
import me.noci.oitc.mapmanager.settings.MapSetting;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.WorldCreator;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

public class Map {

    public static final String DEFAULT_MAP_NAME = "Unknown";
    public static final String DEFAULT_BUILDER_NAME = DEFAULT_MAP_NAME;

    private final MapSetting setting = new MapSetting();
    private final List<Location> playerSpawns = Lists.newArrayList();

    public Map() {
        set(MapData.MAP_NAME, DEFAULT_MAP_NAME);
        set(MapData.MAP_BUILDER, DEFAULT_BUILDER_NAME);
        set(MapData.PLAYER_SPAWNS, Lists.newArrayList());
    }

    public <T> List<T> getList(MapData data, Class<T> type) {
        return (List<T>) get(data, List.class);
    }

    public <T> T get(MapData data, Class<T> type) {
        return setting.get(data, type);
    }

    public <T> void set(MapData data, T value) {
        setting.set(data, value);
    }

    public boolean isValid() {
        if (get(MapData.MAP_NAME, String.class).equals(DEFAULT_MAP_NAME)) return false;
        if (get(MapData.SPECTATOR_SPAWN, Location.class) == null) return false;
        if (get(MapData.WORLD_NAME, String.class) == null) return false;
        return get(MapData.PLAYER_SPAWNS, ArrayList.class).size() >= Game.MIN_PLAYER_SPAWNS;
    }

    public void setupWorld(JavaPlugin plugin) {
        new BukkitRunnable() {
            @Override
            public void run() {
                Bukkit.createWorld(WorldCreator.name(get(MapData.WORLD_NAME, String.class)));
                loadSpawns();
            }
        }.runTask(plugin);
    }

    private void loadSpawns() {
        List<Location> locations = getList(MapData.PLAYER_SPAWNS, String.class).stream().map(LocationUtils::locationFromStringSilently).collect(Collectors.toList());
        playerSpawns.clear();
        playerSpawns.addAll(locations);
    }

    public List<Location> getPlayerSpawns() {
        return Lists.newArrayList(this.playerSpawns);
    }

    public Iterator<Location> playerSpawnIterator() {
        return playerSpawns.iterator();
    }

    public void addRawPlayerSpawn(Location location) {
        getList(MapData.PLAYER_SPAWNS, String.class).add(LocationUtils.locationToStringSilently(location));
    }

}
