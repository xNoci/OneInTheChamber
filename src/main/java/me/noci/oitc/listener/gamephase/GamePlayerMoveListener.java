package me.noci.oitc.listener.gamephase;

import me.noci.oitc.gameutils.Game;
import me.noci.oitc.listener.OITCListener;
import me.noci.oitc.state.GameState;
import me.noci.oitc.state.StateManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class GamePlayerMoveListener extends OITCListener {

    public GamePlayerMoveListener(JavaPlugin plugin, StateManager stateManager, Game game) {
        super(plugin, stateManager, game);
    }

    @EventHandler
    public void handlePlayerMove(PlayerMoveEvent event) {
        if (!isState(StateManager.GAME_STATE)) return;
        GameState state = (GameState) stateManager.getCurrentState();

        if (state.isProtectionTime() && (event.getFrom().getX() != event.getTo().getX() || event.getFrom().getZ() != event.getTo().getZ())) {
            event.getTo().setX(event.getFrom().getX());
            event.getTo().setZ(event.getFrom().getZ());
        }
    }

}
