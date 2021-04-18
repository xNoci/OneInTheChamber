package me.noci.oitc.listener;

import me.noci.oitc.BuildManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

public class BuildListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void handleBlockPlace(BlockPlaceEvent event) {
        if (!BuildManager.canBuild(event.getPlayer().getUniqueId())) return;
        event.setCancelled(false);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void handleBlockBreak(BlockBreakEvent event) {
        if (!BuildManager.canBuild(event.getPlayer().getUniqueId())) return;
        event.setCancelled(false);
    }

}
