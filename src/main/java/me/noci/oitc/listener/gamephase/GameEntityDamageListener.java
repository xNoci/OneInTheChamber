package me.noci.oitc.listener.gamephase;

import me.noci.oitc.gameutils.Game;
import me.noci.oitc.listener.OITCListener;
import me.noci.oitc.state.GameState;
import me.noci.oitc.state.StateManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class GameEntityDamageListener extends OITCListener {

    public GameEntityDamageListener(JavaPlugin plugin, StateManager stateManager, Game game) {
        super(plugin, stateManager, game);
    }

    @EventHandler
    public void handleEntityDamage(EntityDamageEvent event) {
        if (!isState(StateManager.GAME_STATE)) return;
        GameState state = (GameState) stateManager.getCurrentState();
        if (!state.isProtectionTime()) return;
        event.setCancelled(true);
    }

}
