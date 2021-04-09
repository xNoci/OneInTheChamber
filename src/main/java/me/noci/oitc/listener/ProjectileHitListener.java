package me.noci.oitc.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;

public class ProjectileHitListener implements Listener {

    @EventHandler
    public void handleProjectileHit(ProjectileHitEvent event) {
        event.getEntity().remove();
    }

}
