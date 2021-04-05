package me.noci.oitc.mapmanager;

import com.google.common.collect.Lists;
import me.noci.noclib.utils.LocationUtils;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MapFileLoader {

    private final File file;
    private final YamlConfiguration config;

    public MapFileLoader(File file) {
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
