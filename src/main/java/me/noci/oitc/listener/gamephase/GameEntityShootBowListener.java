package me.noci.oitc.listener.gamephase;

import me.noci.oitc.gameutils.Game;
import me.noci.oitc.listener.OITCListener;
import me.noci.oitc.state.GameState;
import me.noci.oitc.state.StateManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class GameEntityShootBowListener extends OITCListener {

    public GameEntityShootBowListener(JavaPlugin plugin, StateManager stateManager, Game game) {
        super(plugin, stateManager, game);
    }

    @EventHandler
    public void handlePlayerInteract(EntityShootBowEvent event) {
        if (!isState(StateManager.GAME_STATE)) return;
        GameState state = (GameState) stateManager.getCurrentState();
        if (!state.isProtectionTime()) return;
        event.setCancelled(true);
        if(event.getEntity() instanceof Player) {
            ((Player) event.getEntity()).updateInventory();
        }
    }

}
