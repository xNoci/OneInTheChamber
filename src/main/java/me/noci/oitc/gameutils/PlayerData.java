package me.noci.oitc.gameutils;

import lombok.Getter;
import me.noci.noclib.api.NocAPI;
import me.noci.noclib.api.user.User;

import java.util.UUID;

public class PlayerData {

    private final UUID uuid;
    @Getter private int score = 0;

    protected PlayerData(UUID uuid) {
        this.uuid = uuid;
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
