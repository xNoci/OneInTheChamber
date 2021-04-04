package me.noci.oitc.listener.gamephase;

import me.noci.oitc.gameutils.Game;
import me.noci.oitc.listener.OITCListener;
import me.noci.oitc.state.StateManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class GameSpectatorInteractListener extends OITCListener {

    public GameSpectatorInteractListener(JavaPlugin plugin, StateManager stateManager, Game game) {
        super(plugin, stateManager, game);
    }

    @EventHandler
    public void handleSpectatorInteract(PlayerInteractEvent event) {
        if (!isState(StateManager.GAME_STATE)) return;
        if (!game.getSpectatorSet().contains(event.getPlayer().getUniqueId())) return;
        event.setCancelled(true);
    }

    @EventHandler
    public void handleSpectatorInteractAtEntity(PlayerInteractAtEntityEvent event) {
        if (!isState(StateManager.GAME_STATE)) return;
        if (!game.getSpectatorSet().contains(event.getPlayer().getUniqueId())) return;
        event.setCancelled(true);
    }

}
