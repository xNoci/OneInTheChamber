package me.noci.oitc.mapmanager.loader.file.config;

import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class MapFileConfig {

    private final ConfigObjectFactory configObjectFactory;
    private final File file;
    private final YamlConfiguration config;

    public MapFileConfig(File file) {
        this.configObjectFactory = new ConfigObjectFactory();
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

    public void setConfigEntry(String path, Object value) {
        ConfigObject<Object> configObject = configObjectFactory.getConfigObject(value.getClass());
        configObject.set(config, path, value);
    }

    public <T> T getConfigEntry(Class<?> type, String path, T def) {
        ConfigObject<T> configObject = configObjectFactory.getConfigObject(type);
        return configObject.get(config, path, def);
    }

    public void save() {
        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}