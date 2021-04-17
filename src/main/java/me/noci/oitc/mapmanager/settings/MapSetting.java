package me.noci.oitc.mapmanager.settings;

import com.google.common.collect.Maps;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;

public class MapSetting {

    private final HashMap<MapData, Value> DATA = Maps.newHashMap();

    public <T> void set(MapData data, T value) {
        Value<T> cache = (Value<T>) getCache(data, value.getClass());
        if (cache.getValue() != null && !cache.getValue().getClass().equals(value.getClass()))
            throw new UnsupportedOperationException(String.format("Used wrong type for %s. Used type '%s' needed type '%s'", data.name(), value.getClass(), cache.getValue().getClass()));
        cache.setValue(value);
    }

    public <T> T get(MapData data, Class<T> type) {
        Value<T> cache = getCache(data, type);
        return cache.getValue();
    }

    private <T> Value<T> getCache(MapData data, Class<T> type) {
        if (DATA.get(data) != null) return DATA.get(data);
        Value<T> value = new Value<>();
        DATA.put(data, value);
        return value;
    }

    private static class Value<T> {

        @Getter @Setter private T value = null;

    }

}
