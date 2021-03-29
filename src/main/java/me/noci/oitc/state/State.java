package me.noci.oitc.state;

import me.noci.noclib.api.scoreboard.Scoreboard;
import me.noci.noclib.api.user.User;
import me.noci.oitc.Game;

public abstract class State {

    private StateManager stateManager;
    protected Game game;

    public void setStateManager(StateManager stateManager) {
        this.stateManager = stateManager;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    protected void changeState(State state) {
        stateManager.changeState(state);
    }

    abstract void start();

    abstract void stop();

    abstract void update();

    abstract void updatePlayerScoreboard(Scoreboard scoreboard, User user);

}
