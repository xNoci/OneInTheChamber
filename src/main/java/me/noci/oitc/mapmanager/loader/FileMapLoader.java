package me.noci.oitc.mapmanager.loader;

import com.google.common.collect.Lists;
import com.google.common.io.Files;
import me.noci.noclib.utils.LocationUtils;
import me.noci.oitc.mapmanager.Map;
import me.noci.oitc.mapmanager.MapFilePath;
import me.noci.oitc.utils.FileUtils;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public class FileMapLoader implements MapLoader {

    private static final String MAP_FILE_TYPE = "ocmap";

    private final File mapRootFolder;
    private final File mapConfigFileFolder;

    public FileMapLoader(JavaPlugin plugin) {
        this.mapRootFolder = new File(plugin.getDataFolder().getAbsolutePath(), "/maps");
        if (!this.mapRootFolder.exists()) {
            this.mapRootFolder.mkdirs();
        }
        this.mapConfigFileFolder = new File(plugin.getConfig().getString("mapConfigFolder", plugin.getServer().getWorldContainer().getPath()));
        if (!this.mapConfigFileFolder.exists()) {
            this.mapConfigFileFolder.mkdirs();
        }
    }

    @Override
    public boolean saveMap(Map map) {
        try {
            String fileName = map.getMapName().toLowerCase() + "." + MAP_FILE_TYPE;
            File file = new File(mapRootFolder, fileName);

            if (file.exists() || !file.createNewFile()) {
                return false;
            }

            FileHandler fileHandler = new FileHandler(file);
            fileHandler.set(MapFilePath.WORLD_NAME, map.getWorldName());
            fileHandler.set(MapFilePath.MAP_NAME, map.getMapName());
            fileHandler.set(MapFilePath.MAP_BUILDER, map.getBuilderName());
            fileHandler.setLocation(MapFilePath.SPECTATOR_SPAWN, map.getSpectatorSpawn());
            fileHandler.set(MapFilePath.PLAYER_SPAWNS, map.getRawPlayerSpawns());
            fileHandler.save();

            Path configFileSrc = file.toPath();
            Path configFileDest = new File(mapConfigFileFolder, fileName).toPath();

            return FileUtils.moveFile(configFileSrc, configFileDest);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<Map> loadMaps() {
        List<Map> maps = Lists.newArrayList();

        for (File mapFile : getMapFiles()) {
            Map map = loadMap(mapFile);
            if (!map.isValid()) continue;
            maps.add(map);
        }
        return maps;
    }

    private Map loadMap(File mapFile) {

        FileHandler mapFileLoader = new FileHandler(mapFile);

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

    private List<File> getMapFiles() {
        List<File> mapFiles = Lists.newArrayList();
        File[] content = mapRootFolder.listFiles();
        if (content == null) return mapFiles;
        for (File file : content) {
            if (Files.getFileExtension(file.getName()).equals(MAP_FILE_TYPE)) {
                mapFiles.add(file);
            }
        }
        return mapFiles;
    }

    private static class FileHandler {

        private final File file;
        private final YamlConfiguration config;

        public FileHandler(File file) {
            this.file = file;
            if (!file.exists()) {
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            this.config = YamlConfiguration.loadConfiguration(file);
        }

        public String getString(MapFilePath path) {
            return getString(path, null);
        }

        public String getString(MapFilePath path, String def) {
            return getValue(String.class, path, def);
        }

        public Location getLocation(MapFilePath path) {
            return getLocation(path, null);
        }

        public Location getLocation(MapFilePath path, Location def) {
            String rawLocation = getString(path);
            if (rawLocation == null) return def;
            Location location = LocationUtils.locationFromStringSilently(rawLocation);
            return location == null ? def : location;
        }

        public List<String> getStringList(MapFilePath path) {
            return getStringList(path, Lists.newArrayList());
        }

        public List<String> getStringList(MapFilePath path, List<String> def) {
            return getList(String.class, path, def);
        }

        public void set(MapFilePath path, Object object) {
            setValue(path, object);
        }

        public void setLocation(MapFilePath path, Location location) {
            String value = LocationUtils.locationToString(location);
            set(path, value);
        }

        private <T> List<T> getList(Class<T> clazz, MapFilePath path, List<T> def) {
            return (List<T>) config.getList(path.getFilePath(), def);
        }

        private <T> T getValue(Class<T> clazz, MapFilePath path, T def) {
            return (T) config.get(path.getFilePath(), def);
        }

        private void setValue(MapFilePath path, Object list) {
            config.set(path.getFilePath(), list);
        }

        public void save() {
            try {
                config.save(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

}
