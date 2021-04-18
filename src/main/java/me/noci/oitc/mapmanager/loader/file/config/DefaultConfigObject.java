package me.noci.oitc.mapmanager.loader.file.config;

import org.bukkit.configuration.file.YamlConfiguration;

public class DefaultConfigObject implements ConfigObject<Object> {

    @Override
    public void set(YamlConfiguration config, String path, Object value) {
        config.set(path, value);
    }

    @Override
    public Object get(YamlConfiguration config, String path, Object def) {
        return config.get(path, def);
    }

}