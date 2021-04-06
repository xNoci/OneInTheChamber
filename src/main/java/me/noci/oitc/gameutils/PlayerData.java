package me.noci.oitc.gameutils;

import lombok.Getter;
import me.noci.noclib.api.NocAPI;
import me.noci.noclib.api.user.User;
import me.noci.oitc.events.PlayerStreakChangeEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

public class PlayerData {

    private final UUID uuid;
    @Getter private final String name;
    @Getter private int score = 0;
    @Getter private int streak;

    protected PlayerData(Player player) {
        this.uuid = player.getUniqueId();
        this.name = player.getName();
    }

    public UUID getUUID() {
        return this.uuid;
    }

    public User getUser() {
        return NocAPI.getUser(uuid);
    }

    public void changeScore(int amount) {
        this.score += amount;
    }

    public void resetStreak(Player killer) {
        changeStreak(-streak, killer);
    }

    public void changeStreak(int amount, Player killer) {
        int oldStreak = this.streak;
        this.streak += amount;

        PlayerStreakChangeEvent streakChangeEvent = new PlayerStreakChangeEvent(getUser().getBase(), killer, oldStreak, this.streak);
        Bukkit.getPluginManager().callEvent(streakChangeEvent);
    }

}
