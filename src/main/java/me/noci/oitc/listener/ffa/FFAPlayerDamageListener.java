package me.noci.oitc.listener.ffa;

import me.noci.oitc.gameutils.Game;
import me.noci.oitc.listener.OITCListener;
import me.noci.oitc.state.StateManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class FFAPlayerDamageListener extends OITCListener {

    public FFAPlayerDamageListener(JavaPlugin plugin, StateManager stateManager, Game game) {
        super(plugin, stateManager, game);
    }

    @EventHandler
    public void handlePlayerFallDamage(EntityDamageEvent event) {
        if (!isState(StateManager.FFA_STATE)) return;
        if (!(event.getEntity() instanceof Player)) return;
        if (event.getCause() != EntityDamageEvent.DamageCause.FALL
                && event.getCause() != EntityDamageEvent.DamageCause.DROWNING
                && event.getCause() != EntityDamageEvent.DamageCause.CONTACT
                && event.getCause() != EntityDamageEvent.DamageCause.LAVA
                && event.getCause() != EntityDamageEvent.DamageCause.FIRE_TICK
                && event.getCause() != EntityDamageEvent.DamageCause.FIRE) return;
        event.setCancelled(true);
    }

}
