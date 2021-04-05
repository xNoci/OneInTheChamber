package me.noci.oitc.listener.endingphase;

import me.noci.noclib.api.NocAPI;
import me.noci.noclib.api.user.User;
import me.noci.oitc.OITC;
import me.noci.oitc.gameutils.Game;
import me.noci.oitc.listener.OITCListener;
import me.noci.oitc.state.StateManager;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class EndPlayerConnectionListener extends OITCListener {

    public EndPlayerConnectionListener(JavaPlugin plugin, StateManager stateManager, Game game) {
        super(plugin, stateManager, game);
    }

    @EventHandler
    public void handlePlayerJoin(PlayerJoinEvent event) {
        if (!isState(StateManager.ENDING_STATE)) return;
        User user = NocAPI.getUser(event.getPlayer());
        event.setJoinMessage(null);

        user.clearInventoryAndArmor();
        user.removePotionEffects();
        user.getBase().setFoodLevel(20);
        user.getBase().setHealth(user.getBase().getMaxHealth());
        user.getBase().setGameMode(GameMode.SURVIVAL);
        user.getBase().spigot().setCollidesWithEntities(true);
        Bukkit.getOnlinePlayers().forEach(other -> {
            other.showPlayer(user.getBase());
            user.getBase().showPlayer(other);
        });

        updateTabList();
        game.teleportToLobby(user.getBase());
    }

    @EventHandler
    public void handlePlayerQuit(PlayerQuitEvent event) {
        if (!isState(StateManager.ENDING_STATE)) return;
        User user = NocAPI.getUser(event.getPlayer());

        event.setQuitMessage(String.format("%sDer Spieler §c%s §7hat das Spiel verlassen.", OITC.PREFIX, user.getName()));
        updateTabList();
    }


}
