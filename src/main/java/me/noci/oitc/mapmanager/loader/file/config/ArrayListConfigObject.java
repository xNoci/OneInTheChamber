package me.noci.oitc.mapmanager.loader.file.config;

import com.google.common.collect.Lists;
import org.bukkit.configuration.file.YamlConfiguration;

import java.util.ArrayList;

public class ArrayListConfigObject implements ConfigObject<ArrayList<String>> {

    @Override
    public void set(YamlConfiguration config, String path, ArrayList<String> value) {
        config.set(path, value != null ? value : Lists.newArrayList());
    }

    @Override
    public ArrayList<String> get(YamlConfiguration config, String path, ArrayList<String> def) {
        return Lists.newArrayList(config.getStringList(path));
    }

}