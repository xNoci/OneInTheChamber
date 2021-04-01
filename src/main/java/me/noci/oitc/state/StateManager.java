package me.noci.oitc.state;

import lombok.Getter;
import me.noci.noclib.api.NocAPI;
import me.noci.noclib.api.scoreboard.Scoreboard;
import me.noci.oitc.gameutils.Game;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class StateManager {

    public static final State LOBBY_STATE = new LobbyState();
    public static final State GAME_STATE = new GameState();
    public static final State ENDING_STATE = new EndingState();
    public static final State MAP_CONFIG_STATE = new MapConfigState();

    private final JavaPlugin plugin;
    private final Game game;
    @Getter private State currentState;
    private BukkitRunnable bukkitRunnable;

    public StateManager(JavaPlugin plugin, Game game) {
        this.plugin = plugin;
        this.game = game;

        initialiseState(LOBBY_STATE);
        initialiseState(GAME_STATE);
        initialiseState(ENDING_STATE);
        initialiseState(MAP_CONFIG_STATE);
    }

    private void initialiseState(State state) {
        state.setStateManager(this);
        state.setPlugin(this.plugin);
        state.setGame(this.game);
    }

    public void start(JavaPlugin plugin) {
        bukkitRunnable = new BukkitRunnable() {
            @Override
            public void run() {
                if (currentState == null) return;
                currentState.update();
                updateScoreboard();
            }
        };
        bukkitRunnable.runTaskTimerAsynchronously(plugin, 0, 20);
    }

    public void updateScoreboard() {
        NocAPI.getOnlineUsers().forEach(user -> {
            Scoreboard scoreboard = NocAPI.getScoreboard(user.getBase());
            scoreboard.removeLines();
            currentState.updatePlayerScoreboard(scoreboard, user);
        });
    }

    public void stop() {
        bukkitRunnable.cancel();
        currentState.stop();
        currentState = null;
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
