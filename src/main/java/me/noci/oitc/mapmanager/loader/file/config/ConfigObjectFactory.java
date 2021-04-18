package me.noci.oitc.mapmanager.loader.file.config;

import com.google.common.collect.Maps;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.HashMap;

public class ConfigObjectFactory {

    private final HashMap<Class<?>, ConfigObject> configObjectMap = Maps.newHashMap();

    public ConfigObjectFactory() {
        configObjectMap.put(Location.class, new LocationConfigObject());
        configObjectMap.put(new ArrayList<String>().getClass(), new ArrayListConfigObject());
    }

    public ConfigObject getConfigObject(Class<?> type) {
        return configObjectMap.getOrDefault(type, new DefaultConfigObject());
    }

}