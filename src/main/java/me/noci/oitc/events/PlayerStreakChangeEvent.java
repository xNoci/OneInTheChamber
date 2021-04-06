package me.noci.oitc.events;

import lombok.Getter;
import me.noci.noclib.events.core.CorePlayerEvent;
import org.bukkit.entity.Player;

import javax.annotation.Nullable;

public class PlayerStreakChangeEvent extends CorePlayerEvent {

    @Nullable @Getter private final Player killer;
    @Getter private final int oldStreak;
    @Getter private final int newStreak;

    public PlayerStreakChangeEvent(Player player, @Nullable Player killer, int oldStreak, int newStreak) {
        super(player);
        this.killer = killer;
        this.oldStreak = oldStreak;
        this.newStreak = newStreak;
    }
}
