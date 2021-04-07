package me.noci.oitc.mapmanager;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.common.io.Files;
import me.noci.oitc.utils.FileUtils;
import org.bukkit.Location;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.*;
import java.util.function.BiConsumer;

public class MapManager {

    private static final String MAP_FILE_TYPE = "ocmap";

    private final JavaPlugin plugin;
    private final File rootFolder;
    private final File mapWorldLocations;
    private final Set<Map> loadedMaps = Sets.newHashSet();

    public static Map createNewMap() {
        return new Map();
    }

    public static Map loadMap(File mapFile) {
        MapFileLoader mapFileLoader = new MapFileLoader(mapFile);
        String worldName = mapFileLoader.getString(MapFilePath.WORLD_NAME);
        String name = mapFileLoader.getString(MapFilePath.MAP_NAME);
        String builder = mapFileLoader.getString(MapFilePath.MAP_BUILDER, "Unknown");
        Location spectatorSpawn = mapFileLoader.getLocation(MapFilePath.SPECTATOR_SPAWN);
        List<String> rawPlayerSpawns = mapFileLoader.getStringList(MapFilePath.PLAYER_SPAWNS);

        Map map = new Map(name);
        map.setWorldName(worldName);
        map.setBuilderName(builder);
        map.setSpectatorSpawn(spectatorSpawn);
        map.setRawPlayerSpawns(rawPlayerSpawns);
        return map;
    }

    private static List<File> getMapFiles(File rootFolder) {
        List<File> mapFiles = Lists.newArrayList();
        File[] content = rootFolder.listFiles();
        if (content == null) return mapFiles;
        for (File file : content) {
            if (Files.getFileExtension(file.getName()).equals(MAP_FILE_TYPE)) {
                mapFiles.add(file);
            }
        }
        return mapFiles;
    }

    public MapManager(JavaPlugin plugin) {
        this.plugin = plugin;
        this.rootFolder = new File(plugin.getDataFolder(), "/maps");
        if (!this.rootFolder.exists()) {
            this.rootFolder.mkdirs();
        }
        this.mapWorldLocations = new File(plugin.getConfig().getString("worldFolderPath", plugin.getServer().getWorldContainer().getPath()));
        if (!this.mapWorldLocations.exists()) {
            this.mapWorldLocations.mkdirs();
        }
        loadMaps();
    }

    private void loadMaps() {
        List<File> maps = getMapFiles(rootFolder);

        for (File mapFile : maps) {
            Map map = loadMap(mapFile);
            if (!map.isValid()) return;
            loadedMaps.add(map);
        }
    }

    public void saveMap(Map map, BiConsumer<Boolean, String> savedSuccessful) {
        new BukkitRunnable() {
            @Override
            public void run() {
                File file = new File(rootFolder, map.getMapName().toLowerCase() + "." + MAP_FILE_TYPE);
                try {
                    if (file.exists() || !file.createNewFile()) {
                        savedSuccessful.accept(false, String.format("Eine Map mit dem Namen %s existiert bereits.", map.getMapName()));
                        return;
                    }
                    MapFileLoader mapFileLoader = new MapFileLoader(file);
                    mapFileLoader.set(MapFilePath.WORLD_NAME, map.getWorldName());
                    mapFileLoader.set(MapFilePath.MAP_NAME, map.getMapName());
                    mapFileLoader.set(MapFilePath.MAP_BUILDER, map.getBuilderName());
                    mapFileLoader.setLocation(MapFilePath.SPECTATOR_SPAWN, map.getSpectatorSpawn());
                    mapFileLoader.set(MapFilePath.PLAYER_SPAWNS, map.getRawPlayerSpawns());

                    loadedMaps.add(map);

                    mapFileLoader.save();
                    savedSuccessful.accept(true, "");

                } catch (IOException e) {
                    savedSuccessful.accept(false, String.format("Ein fehler beim erstellen der Datei für die Map '%s' ist aufgetreten.", map.getMapName()));
                    e.printStackTrace();
                }
            }
        }.runTaskAsynchronously(plugin);
    }

    public Map getRandomMap() {
        List<Map> maps = Lists.newArrayList(loadedMaps);
        if (maps.size() == 0) return null;
        Collections.shuffle(maps);
        return maps.get(0);
    }

    public Iterator<String> getLoadedMapNames() {
        return loadedMaps.stream().map(Map::getMapName).iterator();
    }

    public Optional<Map> getMap(String mapName) {
        return loadedMaps.stream().filter(map -> map.getMapName().equalsIgnoreCase(mapName)).findFirst();
    }

    public void copyWorld(String worldName) {
        Path src = new File(mapWorldLocations, worldName).toPath();
        Path dest = new File(plugin.getServer().getWorldContainer(), worldName).toPath();

        FileUtils.copyDir(src, dest);
    }
}
