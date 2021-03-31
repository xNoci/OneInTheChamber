package me.noci.oitc.listener;

import me.noci.oitc.state.State;
import me.noci.oitc.state.StateManager;
import org.bukkit.event.Listener;

public abstract class OITCListener implements Listener {

    protected final StateManager stateManager;

    protected OITCListener(StateManager stateManager) {
        this.stateManager = stateManager;
    }

    protected boolean isState(State state) {
        return stateManager.isState(state);
    }

}
