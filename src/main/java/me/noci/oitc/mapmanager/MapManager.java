package me.noci.oitc.mapmanager;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import me.noci.oitc.mapmanager.loader.MapLoader;
import me.noci.oitc.mapmanager.loader.file.FileMapLoader;
import me.noci.oitc.mapmanager.settings.MapData;
import me.noci.oitc.utils.FileUtils;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.nio.file.Path;
import java.util.*;
import java.util.function.BiConsumer;

public class MapManager {

    private final JavaPlugin plugin;
    private final Set<Map> loadedMaps = Sets.newHashSet();
    private final File mapWorldFolder;
    private final MapLoader mapLoader;

    public static Map createNewMap() {
        return new Map();
    }

    public MapManager(JavaPlugin plugin) {
        this.plugin = plugin;
        this.mapLoader = new FileMapLoader(plugin);
        this.mapWorldFolder = new File(plugin.getConfig().getString("mapWorldFolder", plugin.getServer().getWorldContainer().getPath()));
        if (!this.mapWorldFolder.exists()) {
            this.mapWorldFolder.mkdirs();
        }

        loadMaps();
    }

    private boolean existsMap(String name) {
        return loadedMaps.stream().map(map -> map.get(MapData.MAP_NAME, String.class)).anyMatch(mapName -> mapName.equalsIgnoreCase(name));
    }

    private boolean copyWorldToFolder(Map toCopy) {
        Path worldSrc = new File(plugin.getServer().getWorldContainer(), toCopy.get(MapData.WORLD_NAME, String.class)).toPath();
        Path worldDest = new File(mapWorldFolder, toCopy.get(MapData.WORLD_NAME, String.class)).toPath();
        return FileUtils.copyDir(worldSrc, worldDest);
    }

    private void loadMaps() {
        loadedMaps.addAll(mapLoader.loadMaps());
    }

    public void saveMap(Map map, BiConsumer<Boolean, String> savedSuccessful) {
        new BukkitRunnable() {
            @Override
            public void run() {
                String mapName = map.get(MapData.MAP_NAME, String.class);

                if (existsMap(mapName)) {
                    savedSuccessful.accept(false, String.format("Eine Map mit dem Namen %s existiert bereits.", mapName));
                    return;
                }

                if (!mapLoader.saveMap(map)) {
                    savedSuccessful.accept(false, "Ein Fehler beim Speichern der Map ist aufgetreten.");
                    return;
                }

                if (!copyWorldToFolder(map)) {
                    savedSuccessful.accept(false, "Ein Fehler beim Kopieren der Welt ist aufgetreten.");
                    return;
                }

                loadedMaps.add(map);
                savedSuccessful.accept(true, "");
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
        return loadedMaps.stream().map(map -> map.get(MapData.MAP_NAME, String.class)).iterator();
    }

    public Optional<Map> getMap(String mapName) {
        return loadedMaps.stream().filter(map -> map.get(MapData.MAP_NAME, String.class).equalsIgnoreCase(mapName)).findFirst();
    }

    public void copyWorldToServer(String worldName) {
        Path src = new File(mapWorldFolder, worldName).toPath();
        Path dest = new File(plugin.getServer().getWorldContainer(), worldName).toPath();
        FileUtils.copyDir(src, dest);
    }

}
