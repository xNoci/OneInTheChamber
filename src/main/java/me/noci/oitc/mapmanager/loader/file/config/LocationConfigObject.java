package me.noci.oitc.mapmanager.loader.file.config;

import me.noci.noclib.utils.LocationUtils;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;

public class LocationConfigObject implements ConfigObject<Location> {

    @Override
    public void set(YamlConfiguration config, String path, Location value) {
        String location = LocationUtils.locationToStringSilently(value);
        config.set(path, location);
    }

    @Override
    public Location get(YamlConfiguration config, String path, Location def) {
        String rawLocation = config.getString(path, null);
        if (rawLocation == null) return def;
        Location location = LocationUtils.locationFromStringSilently(rawLocation);
        return location != null ? location : def;
    }

}