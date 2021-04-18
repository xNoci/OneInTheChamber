package me.noci.oitc.mapmanager.loader.file.config;

import org.bukkit.configuration.file.YamlConfiguration;

public interface ConfigObject<T> {

    void set(YamlConfiguration config, String path, T value);

    T get(YamlConfiguration config, String path, T def);

}