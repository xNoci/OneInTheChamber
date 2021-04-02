package me.noci.oitc.listener.gamephase;

import me.noci.oitc.gameutils.Game;
import me.noci.oitc.listener.OITCListener;
import me.noci.oitc.state.StateManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class GameSpectatorDamageListener extends OITCListener {

    public GameSpectatorDamageListener(JavaPlugin plugin, StateManager stateManager, Game game) {
        super(plugin, stateManager, game);
    }

    @EventHandler
    public void handleEntityDamage(EntityDamageEvent event) {
        if (!isState(StateManager.GAME_STATE)) return;
        if (!(event.getEntity() instanceof Player)) return;
        Player player = (Player) event.getEntity();
        if (!game.getSpectatorSet().contains(player.getUniqueId())) return;
        event.setCancelled(true);
    }

}
