package me.noci.oitc.state;

import lombok.Getter;
import me.noci.noclib.api.NocAPI;
import me.noci.oitc.Game;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class StateManager {

    public static final State LOBBY_STATE = new LobbyState();
    public static final State GAME_STATE = new GameState();
    public static final State ENDING_STATE = new EndingState();
    public static final State MAP_CONFIG_STATE = new MapConfigState();

    @Getter private State currentState;
    private BukkitRunnable bukkitRunnable;

    public StateManager(Game game) {
        LOBBY_STATE.setGame(game);
        LOBBY_STATE.setStateManager(this);

        GAME_STATE.setGame(game);
        GAME_STATE.setStateManager(this);

        ENDING_STATE.setGame(game);
        ENDING_STATE.setStateManager(this);
    }

    public void start(JavaPlugin plugin) {
        bukkitRunnable = new BukkitRunnable() {
            @Override
            public void run() {
                if (currentState == null) return;
                currentState.update();
                NocAPI.getOnlineUsers().forEach(user -> currentState.updatePlayerScoreboard(NocAPI.getScoreboard(user.getBase()), user));
            }
        };
        bukkitRunnable.runTaskTimerAsynchronously(plugin, 0, 20);
    }

    public void stop() {
        bukkitRunnable.cancel();
    }

    public void changeState(State state) {
        if (currentState == state)
            throw new IllegalStateException(String.format("Tried to change %s to %s.", currentState.getClass().getName(), state.getClass().getName()));
        if (currentState != null) {
            currentState.stop();
        }
        this.currentState = state;
        currentState.start();
    }

    public boolean isState(State state) {
        return currentState == state;
    }

}
