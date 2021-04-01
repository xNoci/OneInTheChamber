package me.noci.oitc.listener.mapconfigphase;

import me.noci.oitc.gameutils.Game;
import me.noci.oitc.listener.OITCListener;
import me.noci.oitc.state.StateManager;
import org.bukkit.entity.ArmorStand;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class MapArmorStandDamageListener extends OITCListener {

    public MapArmorStandDamageListener(JavaPlugin plugin, StateManager stateManager, Game game) {
        super(plugin, stateManager, game);
    }

    @EventHandler
    public void handleArmorStandDamage(EntityDamageByEntityEvent event) {
        if (!isState(StateManager.MAP_CONFIG_STATE)) return;
        if (!(event.getEntity() instanceof ArmorStand)) return;
        event.setCancelled(true);
    }

}
