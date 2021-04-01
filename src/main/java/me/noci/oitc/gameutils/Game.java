package me.noci.oitc.gameutils;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import lombok.Getter;
import lombok.Setter;
import me.noci.oitc.mapmanager.Map;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class Game {

    public static final int MIN_PLAYER_SPAWNS = 16;

    private final HashMap<UUID, PlayerData> playerDataMap = Maps.newHashMap();
    @Getter @Setter private Map currentMap;
    @Getter private final Set<UUID> playerSet = Sets.newHashSet();
    @Getter private final Set<UUID> spectatorSet = Sets.newHashSet();
    @Getter private final int maxPlayers;
    @Getter private final int playersNeeded;
    @Getter private final int timeToStart;
    @Getter private long gameDuration = TimeUnit.MINUTES.toSeconds(15);
    @Getter private int forceTime = 10;

    public Game(int maxPlayers, int timeToStart) {
        this.maxPlayers = maxPlayers;
        this.playersNeeded = Math.max((int) (maxPlayers * 0.3), 3);
        this.timeToStart = timeToStart;
    }

    public PlayerData getPlayerData(UUID uuid) {
        if (playerDataMap.containsKey(uuid)) return playerDataMap.get(uuid);
        PlayerData playerData = new PlayerData(uuid);
        playerDataMap.put(uuid, playerData);
        return playerData;
    }

    public List<PlayerData> getPlayerDataSorted() {
        return playerDataMap.values()
                .stream()
                .sorted(Comparator.comparingInt(PlayerData::getScore).reversed())
                .collect(Collectors.toList());
    }

}
