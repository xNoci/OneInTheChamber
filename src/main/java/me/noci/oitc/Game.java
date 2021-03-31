package me.noci.oitc;

import com.google.common.collect.Sets;
import lombok.Getter;
import lombok.Setter;
import me.noci.oitc.mapmanager.Map;
import org.bukkit.entity.Player;

import java.util.Set;

public class Game {

    public static final int MIN_PLAYER_SPAWNS = 16;

    @Getter private final Set<Player> playerSet = Sets.newHashSet();
    @Getter private final Set<Player> spectatorSet = Sets.newHashSet();
    @Getter private final int maxPlayers;
    @Getter private final int playersNeeded;
    @Getter private final int timeToStart;
    @Getter @Setter private int forceTime = 10;
    @Getter @Setter private Map currentMap;

    public Game(int maxPlayers, int timeToStart) {
        this.maxPlayers = maxPlayers;
        this.playersNeeded = Math.max((int) (maxPlayers * 0.3), 3);
        this.timeToStart = timeToStart;
    }

}
