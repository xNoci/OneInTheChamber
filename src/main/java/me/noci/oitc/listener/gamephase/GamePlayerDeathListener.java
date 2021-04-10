package me.noci.oitc.listener.gamephase;

import me.noci.noclib.api.NocAPI;
import me.noci.noclib.api.user.User;
import me.noci.noclib.utils.items.AdvancedItemStack;
import me.noci.oitc.OITC;
import me.noci.oitc.gameutils.Game;
import me.noci.oitc.gameutils.PlayerData;
import me.noci.oitc.listener.OITCListener;
import me.noci.oitc.mapmanager.Map;
import me.noci.oitc.state.StateManager;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
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
            User uKiller = NocAPI.getUser(killer);
            uKiller.playSound(Sound.NOTE_PLING, 1, 1);
            killer.getInventory().addItem(new AdvancedItemStack(Material.ARROW).addItemFlags());
            killer.setHealth(killer.getMaxHealth());

            PlayerData killerData = game.getPlayerData(killer);
            killerData.changeScore(1);
            killerData.changeStreak(1, null);

            killer.sendMessage(String.format("%sDu hast §c%s §7getötet.", OITC.PREFIX, player.getName()));
            player.sendMessage(String.format("%sDu wurdest von §c%s §7getötet.", OITC.PREFIX, killer.getName()));
        } else {
            player.sendMessage(String.format("%sDu bist gestorben.", OITC.PREFIX));
        }

        PlayerData playerData = game.getPlayerData(player);
        playerData.resetStreak(killer);

        updateScoreboard();

        new BukkitRunnable() {
            @Override
            public void run() {
                player.spigot().respawn();
                Game.giveItems(player);
                game.spawnPlayer(player);
            }
        }.runTaskLater(plugin, 2);
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
