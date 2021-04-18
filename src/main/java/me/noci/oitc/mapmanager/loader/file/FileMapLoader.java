package me.noci.oitc.mapmanager.loader.file;

import com.google.common.collect.Lists;
import com.google.common.io.Files;
import me.noci.oitc.mapmanager.Map;
import me.noci.oitc.mapmanager.loader.MapLoader;
import me.noci.oitc.mapmanager.loader.file.config.MapFileConfig;
import me.noci.oitc.mapmanager.settings.MapData;
import me.noci.oitc.utils.FileUtils;
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
            String fileName = map.get(MapData.MAP_NAME, String.class).toLowerCase() + "." + MAP_FILE_TYPE;
            File file = new File(mapRootFolder, fileName);

            if (file.exists() || !file.createNewFile()) {
                return false;
            }

            MapFileConfig mapFileConfig = new MapFileConfig(file);
            for (MapData value : MapData.values()) {
                mapFileConfig.setConfigEntry(value.name(), map.get(value, value.getType()));
            }
            mapFileConfig.save();

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
        Map map = new Map();

        MapFileConfig mapFileConfig = new MapFileConfig(mapFile);
        for (MapData mapData : MapData.values()) {
            Object configValue = mapFileConfig.getConfigEntry(mapData.getType(), mapData.name(), null);
            map.set(mapData, configValue);
        }

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

}
