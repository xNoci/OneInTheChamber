package me.noci.oitc.listener.gamephase;

import me.noci.noclib.api.NocAPI;
import me.noci.noclib.api.user.User;
import me.noci.oitc.OITC;
import me.noci.oitc.gameutils.Game;
import me.noci.oitc.listener.OITCListener;
import me.noci.oitc.state.GameState;
import me.noci.oitc.state.StateManager;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffectType;

public class GamePlayerConnectionListener extends OITCListener {

    public GamePlayerConnectionListener(JavaPlugin plugin, StateManager stateManager, Game game) {
        super(plugin, stateManager, game);
    }

    @EventHandler
    public void handlePlayerJoin(PlayerJoinEvent event) {
        if (!isState(StateManager.GAME_STATE)) return;
        User user = NocAPI.getUser(event.getPlayer());

        event.setJoinMessage(null);
        game.getSpectatorSet().add(user.getUUID());

        user.clearInventoryAndArmor();
        user.removePotionEffects();
        user.getBase().spigot().setCollidesWithEntities(false);
        user.getBase().setHealth(user.getBase().getMaxHealth());
        user.getBase().setFoodLevel(20);
        user.getBase().setGameMode(GameMode.ADVENTURE);
        user.getBase().setAllowFlight(true);
        user.getBase().addPotionEffect(PotionEffectType.INVISIBILITY.createEffect(Integer.MAX_VALUE, 1), true);

        Bukkit.getOnlinePlayers().forEach(player -> {
            if (!game.getSpectatorSet().contains(player.getUniqueId())) {
                player.hidePlayer(user.getBase());
            } else {
                player.showPlayer(user.getBase());
            }
        });

        updateTabList();
    }

    @EventHandler
    public void handlePlayerQuit(PlayerQuitEvent event) {
        if (!isState(StateManager.GAME_STATE)) return;
        User user = NocAPI.getUser(event.getPlayer());
        GameState state = (GameState) stateManager.getCurrentState();

        if (game.getPlayerSet().contains(user.getUUID())) {
            event.setQuitMessage(String.format("%sDer Spieler §c%s §7hat das Spiel verlassen.", OITC.PREFIX, user.getName()));
            game.getPlayerSet().remove(user.getUUID());
            game.removePlayerData(user.getUUID());
            state.checkEnding();
        } else {
            game.getSpectatorSet().remove(user.getUUID());
            user.removePotionEffects();
            user.getBase().setAllowFlight(false);
            user.getBase().spigot().setCollidesWithEntities(true);
        }

        updateTabList();
    }


}
