package me.noci.oitc.listener.gamephase;

import me.noci.oitc.OITC;
import me.noci.oitc.gameutils.Game;
import me.noci.oitc.listener.OITCListener;
import me.noci.oitc.mapmanager.Map;
import me.noci.oitc.state.StateManager;
import org.bukkit.Location;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;
import java.util.SplittableRandom;

public class GamePlayerDeathListener extends OITCListener {

    private static final SplittableRandom RANDOM = new SplittableRandom();

    public GamePlayerDeathListener(JavaPlugin plugin, StateManager stateManager, Game game) {
        super(plugin, stateManager, game);
    }

    @EventHandler
    public void handlePlayerDeath(PlayerDeathEvent event) {
        if (!isState(StateManager.GAME_STATE)) return;
        event.setKeepInventory(true);
        event.setKeepLevel(true);
        event.setDeathMessage(null);
        Player player = event.getEntity();
        Player killer = getKiller(player);

        if (killer != null) {
            game.getPlayerData(killer).changeScore(1);
            killer.sendMessage(String.format("%sDu hast §c%s §7getötet.", OITC.PREFIX, player.getName()));
            player.sendMessage(String.format("%sDu wurdest von §c%s §7getötet.", OITC.PREFIX, killer.getName()));
        } else {
            player.sendMessage(String.format("%sDu bist gestorben.", OITC.PREFIX));
        }

        updateScoreboard();
        new BukkitRunnable() {
            @Override
            public void run() {
                player.spigot().respawn();

                Map map = game.getCurrentMap();
                List<Location> spawns = map.getPlayerSpawns();
                Location location = spawns.get(RANDOM.nextInt(spawns.size()));
                player.teleport(location);
            }
        }.runTaskLater(plugin, 5);
    }

    private Player getKiller(Player player) {
        Player killer = null;
        if (player.getLastDamageCause() instanceof EntityDamageByEntityEvent) {
            EntityDamageByEntityEvent entityEvent = (EntityDamageByEntityEvent) player.getLastDamageCause();
            if (entityEvent.getDamager() instanceof Player) {
                killer = (Player) entityEvent.getDamager();
            }
            if (entityEvent.getDamager() instanceof Arrow) {
                Arrow arrow = (Arrow) entityEvent.getDamager();
                if (arrow.getShooter() instanceof Player) {
                    killer = (Player) arrow.getShooter();
                }
            }
        }

        if (killer != null && player.getUniqueId().equals(killer.getUniqueId())) {
            killer = null;
        }
        return killer;
    }

}
