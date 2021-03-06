package me.noci.oitc;

import me.noci.noclib.api.NocAPI;
import me.noci.oitc.commands.*;
import me.noci.oitc.gameutils.Game;
import me.noci.oitc.listener.AsyncPlayerChatListener;
import me.noci.oitc.listener.BuildListener;
import me.noci.oitc.listener.ProtectionListener;
import me.noci.oitc.listener.ServerListPingListener;
import me.noci.oitc.listener.endingphase.EndEntityDamageListener;
import me.noci.oitc.listener.endingphase.EndPlayerConnectionListener;
import me.noci.oitc.listener.endingphase.EndPlayerSendChatMessageListener;
import me.noci.oitc.listener.gamephase.*;
import me.noci.oitc.listener.lobbyphase.LobbyEntityDamageListener;
import me.noci.oitc.listener.lobbyphase.LobbyPlayerConnectionListener;
import me.noci.oitc.listener.mapconfigphase.MapArmorStandDamageListener;
import me.noci.oitc.listener.mapconfigphase.MapPlayerConnectionListener;
import me.noci.oitc.listener.mapconfigphase.MapPlayerSendChatMessageListener;
import me.noci.oitc.listener.mapconfigphase.MapPlayerToggleSneakListener;
import me.noci.oitc.mapmanager.MapManager;
import me.noci.oitc.state.StateManager;
import net.atophia.atophiaapi.language.LocaleHandler;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class OITC extends JavaPlugin {

    public static final String PREFIX = "§9OITC §8» §7";
    public static final String PREFIX_ACTIONBAR = "§9OITC §8| §7";
    public static final String NO_PERMISSION = "§cI'm sorry, but you do not have permission to perform this command. Please contact the server administrators if you believe that this is in error.";

    private MapManager mapManager;
    private Game game;
    private StateManager stateManager;

    @Override
    public void onEnable() {
        loadDefaultConfig();

        mapManager = new MapManager(this);
        game = Game.setupGame(this, mapManager);
        stateManager = new StateManager(this, game);

        game.setCurrentMap(mapManager.getRandomMap());

        stateManager.start(this);
        stateManager.changeState(StateManager.LOBBY_STATE);


        LocaleHandler.loadLocales("OITC");

        registerListeners();
        registerCommands();
    }

    @Override
    public void onDisable() {
        stateManager.stop();
        Bukkit.getOnlinePlayers().forEach(player -> player.kickPlayer(String.format("%s§cDer Server wurde geschlossen!", PREFIX)));
    }

    private void registerCommands() {
        NocAPI.registerCommand(new StartCommand(this, stateManager, game));
        NocAPI.registerCommand(new ForceStartCommand(this, stateManager));
        NocAPI.registerCommand(new MapSetupCommand(this, stateManager));
        NocAPI.registerCommand(new MapSetupEndCommand(this, stateManager, mapManager));
        NocAPI.registerCommand(new WorldCommand(this, stateManager));
        NocAPI.registerCommand(new SetLobbySpawnCommand(this, stateManager, game));
        NocAPI.registerCommand(new ForceMapCommand(this, stateManager, game, mapManager));
        NocAPI.registerCommand(new BuildCommand(this));
    }

    private void registerListeners() {
        registerListener(new ProtectionListener());
        registerListener(new AsyncPlayerChatListener());
        registerListener(new ServerListPingListener(this, stateManager, game));
        registerListener(new BuildListener());

        registerListener(new LobbyPlayerConnectionListener(this, stateManager, game));
        registerListener(new LobbyEntityDamageListener(this, stateManager, game));

        registerListener(new GamePlayerDamageByPlayerListener(this, stateManager, game));
        registerListener(new GamePlayerSendChatMessageListener(this, stateManager, game));
        registerListener(new GamePlayerDeathListener(this, stateManager, game));
        registerListener(new GamePlayerDamageListener(this, stateManager, game));
        registerListener(new GamePlayerConnectionListener(this, stateManager, game));
        registerListener(new GamePlayerMoveListener(this, stateManager, game));
        registerListener(new GamePlayerStreakChangeListener(this, stateManager, game));
        registerListener(new GameEntityShootBowListener(this, stateManager, game));
        registerListener(new GameEntityDamageListener(this, stateManager, game));
        registerListener(new GameSpectatorDamagedPlayerListener(this, stateManager, game));
        registerListener(new GameSpectatorDamageListener(this, stateManager, game));
        registerListener(new GameSpectatorInteractListener(this, stateManager, game));

        registerListener(new EndPlayerSendChatMessageListener(this, stateManager, game));
        registerListener(new EndPlayerConnectionListener(this, stateManager, game));
        registerListener(new EndEntityDamageListener(this, stateManager, game));

        registerListener(new MapPlayerConnectionListener(this, stateManager, game));
        registerListener(new MapPlayerSendChatMessageListener(this, stateManager, game));
        registerListener(new MapPlayerToggleSneakListener(this, stateManager, game));
        registerListener(new MapArmorStandDamageListener(this, stateManager, game));
    }

    private void registerListener(Listener listener) {
        PluginManager pluginManager = getServer().getPluginManager();
        pluginManager.registerEvents(listener, this);
    }

    private void loadDefaultConfig() {
        getDataFolder().mkdirs();
        getConfig().options().copyDefaults(true);
        saveDefaultConfig();
    }

}
