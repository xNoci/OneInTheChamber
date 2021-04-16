package me.noci.oitc.state;

import lombok.AccessLevel;
import lombok.Setter;
import me.noci.noclib.api.NocAPI;
import me.noci.noclib.api.user.User;
import me.noci.oitc.gameutils.Game;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

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

    protected abstract void start();

    protected abstract void stop();

    protected abstract void update();

    protected abstract void updateTabList(User user);

    protected abstract void updateScoreboardLines(List<String> lines, User user);

    protected void updateTabList() {
        NocAPI.getOnlineUsers().forEach(this::updateTabList);
    }

}
