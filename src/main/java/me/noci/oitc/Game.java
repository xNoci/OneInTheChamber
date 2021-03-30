package me.noci.oitc;

import com.google.common.collect.Sets;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;

import java.util.Set;

public class Game {

    @Getter private final Set<Player> playerSet = Sets.newHashSet();
    @Getter private final Set<Player> spectatorSet = Sets.newHashSet();
    @Getter private final int maxPlayers;
    @Getter private final int playersNeeded;
    @Getter private final int timeToStart;
    @Getter @Setter private int forceTime = 10;

    public Game(int maxPlayers, int timeToStart) {
        this.maxPlayers = maxPlayers;
        this.playersNeeded = Math.max((int) (maxPlayers * 0.3), 3);
        this.timeToStart = timeToStart;
    }

}
