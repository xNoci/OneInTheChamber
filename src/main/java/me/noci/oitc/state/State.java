package me.noci.oitc.state;

import lombok.AccessLevel;
import lombok.Setter;
import me.noci.noclib.api.scoreboard.Scoreboard;
import me.noci.noclib.api.user.User;
import me.noci.oitc.gameutils.Game;
import org.bukkit.plugin.java.JavaPlugin;

public abstract class State {

    @Setter(AccessLevel.PROTECTED) private StateManager stateManager;
    @Setter(AccessLevel.PROTECTED) protected JavaPlugin plugin;
    @Setter(AccessLevel.PROTECTED) protected Game game;

    protected void changeState(State state) {
        stateManager.changeState(state);
    }

    protected void updateScoreboard() {
        stateManager.updateScoreboard();
    }

    abstract void start();

    abstract void stop();

    abstract void update();

    abstract void updatePlayerScoreboard(Scoreboard scoreboard, User user);

}
