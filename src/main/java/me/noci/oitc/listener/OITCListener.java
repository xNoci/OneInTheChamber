package me.noci.oitc.listener;

import me.noci.oitc.gameutils.Game;
import me.noci.oitc.state.State;
import me.noci.oitc.state.StateManager;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public abstract class OITCListener implements Listener {

    protected final JavaPlugin plugin;
    protected final StateManager stateManager;
    protected final Game game;

    public OITCListener(JavaPlugin plugin, StateManager stateManager, Game game) {
        this.plugin = plugin;
        this.stateManager = stateManager;
        this.game = game;
    }

    protected boolean isState(State state) {
        return stateManager.isState(state);
    }

    protected void updateScoreboard() {
        stateManager.updateScoreboard();
    }

}
