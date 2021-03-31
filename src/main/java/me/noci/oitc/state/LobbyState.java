package me.noci.oitc.state;

import me.noci.noclib.api.NocAPI;
import me.noci.noclib.api.scoreboard.Scoreboard;
import me.noci.noclib.api.user.User;
import me.noci.oitc.OITC;
import org.bukkit.Sound;

public class LobbyState extends State {

    private int remainingTime;
    private boolean starting;
    private boolean forceStarted;

    @Override
    public void start() {
        this.remainingTime = game.getTimeToStart();
        this.starting = false;
        this.forceStarted = false;
    }

    @Override
    public void stop() {
        this.remainingTime = game.getTimeToStart();
        this.starting = false;
        this.forceStarted = false;
        NocAPI.getOnlineUsers().forEach(User::resetLevelValue);
    }

    @Override
    public void update() {
        if (starting) {
            if (remainingTime <= 0) {
                changeState(StateManager.GAME_STATE);
                return;
            }

            NocAPI.getOnlineUsers().forEach(user -> {
                if (remainingTime % 10 == 0 || remainingTime == 15 || remainingTime <= 5) {
                    user.setLevelValue(remainingTime, game.getTimeToStart(), Sound.NOTE_BASS, 1, 1);
                    sendRemainingTime(user, remainingTime);
                } else {
                    user.setLevelValue(remainingTime, game.getTimeToStart());
                }
            });

            remainingTime--;
        } else {
            NocAPI.getOnlineUsers().forEach(user -> user.setLevelValue(game.getTimeToStart(), game.getTimeToStart()));
        }
    }

    @Override
    public void updatePlayerScoreboard(Scoreboard scoreboard, User user) {
        scoreboard.updateTitle("     §9OITC     ");
        scoreboard.updateLine(0, "");
        scoreboard.updateLine(1, " §7Map");
        scoreboard.updateLine(2, String.format("  §8» §c%s", mapName()));
        scoreboard.updateLine(3, "");
    }

    public void checkTimer() {
        boolean enoughPlayer = game.getPlayerSet().size() >= game.getPlayersNeeded();

        if (enoughPlayer && !starting) {
            starting = true;
            this.remainingTime = game.getTimeToStart();
        }

        if (!enoughPlayer && starting) {
            starting = false;
            forceStarted = false;
            this.remainingTime = game.getTimeToStart();
        }
    }

    public boolean forceStart() {
        if (forceStarted) return false;
        if (remainingTime <= game.getForceTime()) return false;
        forceStarted = true;
        starting = true;
        remainingTime = game.getForceTime();
        return true;
    }

    private void sendRemainingTime(User user, int time) {
        if (time != 1) {
            user.sendMessage("%sDas Spiel startet in §c%s §7Sekunden.", OITC.PREFIX, time);
        } else {
            user.sendMessage("%sDas Spiel startet in §c%s §7Sekunde.", OITC.PREFIX, time);
        }
    }

    private String mapName() {
        return game.getCurrentMap() == null ? "Unknown" : game.getCurrentMap().getMapName();
    }

}
