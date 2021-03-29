package me.noci.oitc.listener.lobbyphase;

import me.noci.noclib.api.NocAPI;
import me.noci.noclib.api.user.User;
import me.noci.oitc.Game;
import me.noci.oitc.OITC;
import me.noci.oitc.state.LobbyState;
import me.noci.oitc.state.StateManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class LobbyPlayerConnectionListener implements Listener {

    private final StateManager stateManager;
    private final Game game;

    public LobbyPlayerConnectionListener(StateManager stateManager, Game game) {
        this.stateManager = stateManager;
        this.game = game;
    }

    @EventHandler
    public void handlePlayerJoin(PlayerJoinEvent event) {
        if (!stateManager.isState(StateManager.LOBBY_STATE)) return;
        User user = NocAPI.getUser(event.getPlayer());
        LobbyState lobbyState = (LobbyState) stateManager.getCurrentState();

        event.setJoinMessage(String.format("%sDer Spieler §c%s §7hat das Spiel betreten.", OITC.PREFIX, user.getName()));
        game.getPlayerSet().add(event.getPlayer());
        lobbyState.checkTimer();
    }

    @EventHandler
    public void handlePlayerQuit(PlayerQuitEvent event) {
        if (!stateManager.isState(StateManager.LOBBY_STATE)) return;
        User user = NocAPI.getUser(event.getPlayer());
        LobbyState lobbyState = (LobbyState) stateManager.getCurrentState();

        event.setQuitMessage(String.format("%sDer Spieler §c%s §7hat das Spiel verlassen.", OITC.PREFIX, user.getName()));
        game.getPlayerSet().remove(event.getPlayer());
        lobbyState.checkTimer();
    }


}
