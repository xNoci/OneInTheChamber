package me.noci.oitc;

import me.noci.noclib.api.NocAPI;
import me.noci.oitc.commands.MapSetupCommand;
import me.noci.oitc.commands.MapSetupEndCommand;
import me.noci.oitc.commands.StartCommand;
import me.noci.oitc.listener.ProtectionListener;
import me.noci.oitc.listener.lobbyphase.LobbyPlayerConnectionListener;
import me.noci.oitc.listener.mapconfigphase.MapArmorStandDamageListener;
import me.noci.oitc.listener.mapconfigphase.MapAsyncPlayerChatListener;
import me.noci.oitc.listener.mapconfigphase.MapPlayerToggleSneakListener;
import me.noci.oitc.mapmanager.MapManager;
import me.noci.oitc.state.StateManager;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class OITC extends JavaPlugin {

    public static final String PREFIX = "§9OITC §8» §7";
    public static final String PREFIX_ACTIONBAR = "§9OITC §8| §7";
    public static final String NO_PERMISSION = "§cI'm sorry, but you do not have permission to perform this command. Please contact the server administrators if you believe that this is in error.";

    private final StateManager stateManager;
    private final MapManager mapManager;
    private final Game game;

    public OITC() {
        getDataFolder().mkdirs();

        game = new Game(12, 60);
        stateManager = new StateManager(this, game);
        mapManager = new MapManager(this);

        game.setCurrentMap(mapManager.getRandomMap());
    }

    @Override
    public void onEnable() {
        stateManager.start(this);
        stateManager.changeState(StateManager.LOBBY_STATE);

        registerListener();
        registerCommands();
    }

    @Override
    public void onDisable() {
        stateManager.stop();
    }

    private void registerListener() {
        PluginManager pluginManager = getServer().getPluginManager();
        pluginManager.registerEvents(new ProtectionListener(), this);
        pluginManager.registerEvents(new LobbyPlayerConnectionListener(stateManager, game), this);
        pluginManager.registerEvents(new MapAsyncPlayerChatListener(stateManager), this);
        pluginManager.registerEvents(new MapPlayerToggleSneakListener(stateManager), this);
        pluginManager.registerEvents(new MapArmorStandDamageListener(stateManager), this);
    }

    private void registerCommands() {
        NocAPI.registerCommand(new StartCommand(this, stateManager));
        NocAPI.registerCommand(new MapSetupCommand(this, stateManager));
        NocAPI.registerCommand(new MapSetupEndCommand(this, stateManager, mapManager));
    }

}
