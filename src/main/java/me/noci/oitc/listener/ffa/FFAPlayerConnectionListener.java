package me.noci.oitc.listener.ffa;

import me.noci.noclib.api.NocAPI;
import me.noci.noclib.api.user.User;
import me.noci.oitc.gameutils.Game;
import me.noci.oitc.listener.OITCListener;
import me.noci.oitc.state.StateManager;
import org.bukkit.GameMode;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class FFAPlayerConnectionListener extends OITCListener {

    public FFAPlayerConnectionListener(JavaPlugin plugin, StateManager stateManager, Game game) {
        super(plugin, stateManager, game);
    }

    @EventHandler
    public void handlePlayerJoin(PlayerJoinEvent event) {
        if (!isState(StateManager.FFA_STATE)) return;
        User user = NocAPI.getUser(event.getPlayer());

        event.setJoinMessage(null);
        game.getPlayerSet().add(user.getUUID());

        user.clearInventoryAndArmor();
        user.removePotionEffects();
        user.getBase().spigot().setCollidesWithEntities(true);
        user.getBase().setHealth(user.getBase().getMaxHealth());
        user.getBase().setFoodLevel(20);
        user.getBase().setGameMode(GameMode.SURVIVAL);
        user.getBase().setAllowFlight(false);

        Game.giveItems(user.getBase());

        game.spawnPlayer(user.getBase());

        updateTabList();
    }

    @EventHandler
    public void handlePlayerQuit(PlayerQuitEvent event) {
        if (!isState(StateManager.FFA_STATE)) return;
        User user = NocAPI.getUser(event.getPlayer());

        event.setQuitMessage(null);
        game.getPlayerSet().remove(user.getUUID());
        game.removePlayerData(user.getUUID());

        updateTabList();
    }

}
