package me.noci.oitc.state;

import me.noci.noclib.api.scoreboard.Scoreboard;
import me.noci.noclib.api.user.User;
import me.noci.oitc.gameutils.PlayerData;

import java.util.List;

public class GameState extends State {

    private long timeRemaining;

    @Override
    public void start() {
        timeRemaining = game.getGameDuration();
    }

    @Override
    public void stop() {

    }

    @Override
    public void update() {
        timeRemaining--;
    }

    @Override
    public void updatePlayerScoreboard(Scoreboard scoreboard, User user) {
        scoreboard.updateTitle("     §9OITC     ");
        scoreboard.updateLine(0, "");
        scoreboard.updateLine(1, " §7Zeit");
        scoreboard.updateLine(2, String.format("  §8» §c%s", formatTime()));
        scoreboard.updateLine(3, "");
    }

    private String formatTime() {
        long time = timeRemaining;
        int minutes = (int) (timeRemaining / 60);
        int seconds = (int) (time % 60);

        if (minutes <= 0) return String.format("%s Sekunden", seconds);

        return String.format("%s:%s", minutes < 10 ? "0" + minutes : minutes, seconds < 10 ? "0" + seconds : seconds);
    }

}
