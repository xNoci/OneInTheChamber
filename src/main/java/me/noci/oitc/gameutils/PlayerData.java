package me.noci.oitc.gameutils;

import lombok.Getter;
import me.noci.noclib.api.NocAPI;
import me.noci.noclib.api.user.User;
import org.bukkit.entity.Player;

import java.util.UUID;

public class PlayerData {

    private final UUID uuid;
    @Getter private final String name;

    @Getter private int score = 0;

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

    public void changeScore(int change) {
        this.score += change;
    }

}
