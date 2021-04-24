package me.noci.oitc.listener.lobbyphase;

import me.noci.noclib.api.NocAPI;
import me.noci.noclib.api.user.User;
import me.noci.oitc.gameutils.Game;
import me.noci.oitc.listener.OITCListener;
import me.noci.oitc.state.LobbyState;
import me.noci.oitc.state.StateManager;
import net.atophia.atophiaapi.language.LanguageAPI;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class LobbyPlayerConnectionListener extends OITCListener {

    public LobbyPlayerConnectionListener(JavaPlugin plugin, StateManager stateManager, Game game) {
        super(plugin, stateManager, game);
    }

    @EventHandler
    public void handlePlayerJoin(PlayerJoinEvent event) {
        if (!isState(StateManager.LOBBY_STATE)) return;
        event.setJoinMessage(null);
        User user = NocAPI.getUser(event.getPlayer());
        LobbyState lobbyState = (LobbyState) stateManager.getCurrentState();

        user.clearInventoryAndArmor();
        user.removePotionEffects();
        user.getBase().setFoodLevel(20);
        user.getBase().setHealth(user.getBase().getMaxHealth());
        user.getBase().setGameMode(GameMode.SURVIVAL);


        Bukkit.getOnlinePlayers().forEach(player -> {
            LanguageAPI.send(player,  "lobby.player.join", user.getName());
        });

        game.getPlayerSet().add(user.getUUID());
        lobbyState.checkTimer();

        updateTabList();

        game.teleportToLobby(user.getBase());
    }

    @EventHandler
    public void handlePlayerQuit(PlayerQuitEvent event) {
        if (!isState(StateManager.LOBBY_STATE)) return;
        event.setQuitMessage(null);
        User user = NocAPI.getUser(event.getPlayer());
        LobbyState lobbyState = (LobbyState) stateManager.getCurrentState();

        Bukkit.getOnlinePlayers().forEach(player -> {
            LanguageAPI.send(player,  "lobby.player.quit", user.getName());
        });
        game.getPlayerSet().remove(user.getUUID());
        lobbyState.checkTimer();

        updateTabList();
    }

}
