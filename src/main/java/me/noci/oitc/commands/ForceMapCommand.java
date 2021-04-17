package me.noci.oitc.commands;

import me.noci.noclib.api.NocAPI;
import me.noci.noclib.api.user.User;
import me.noci.noclib.command.Command;
import me.noci.noclib.command.CommandData;
import me.noci.noclib.gui.GuiBuilder;
import me.noci.noclib.utils.items.AdvancedItemStack;
import me.noci.noclib.utils.items.NBTTagQuery;
import me.noci.noclib.utils.items.SkullUtils;
import me.noci.oitc.OITC;
import me.noci.oitc.gameutils.Game;
import me.noci.oitc.mapmanager.Map;
import me.noci.oitc.mapmanager.MapManager;
import me.noci.oitc.mapmanager.settings.MapData;
import me.noci.oitc.state.LobbyState;
import me.noci.oitc.state.StateManager;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Iterator;
import java.util.Optional;


@CommandData(name = "forcemap", aliases = {"fm"})
public class ForceMapCommand extends Command {

    private static final AdvancedItemStack MAP_ITEM = SkullUtils.getSkull("http://textures.minecraft.net/texture/b8140ad7da87de28491f48da190781b28ce05248fcc48176188fb6cecf5b5096");

    private final Game game;
    private final StateManager stateManager;
    private boolean mapForced = false;

    public ForceMapCommand(JavaPlugin plugin, StateManager stateManager, Game game, MapManager mapManager) {
        super(plugin);
        this.game = game;
        this.stateManager = stateManager;

        GuiBuilder builder = GuiBuilder.create("§c§lForcemap", 9 * 3);
        builder.onGuiCreate((inventory, player, objects) -> {
            Iterator<String> loadedMap = mapManager.getLoadedMapNames();
            while (loadedMap.hasNext()) {
                String name = loadedMap.next();
                AdvancedItemStack map = new AdvancedItemStack(MAP_ITEM.clone());
                map.setDisplayName(String.format("§8● §6%s", name));
                map.setNBTTag("MapName", name);
                map.addItemFlags();
                inventory.addItem(map);
            }
        });
        builder.onGuiClick(event -> {
            event.setCancelled(true);
            if (event.getClickedItem() == null || event.getClickedItem().getType() != Material.SKULL_ITEM) return;
            String mapName = NBTTagQuery.getNBTTagString(event.getClickedItem(), "MapName");
            if (mapName == null) return;
            User user = NocAPI.getUser(event.getPlayer());
            user.getBase().closeInventory();

            if (game.getCurrentMap().get(MapData.MAP_NAME, String.class).equalsIgnoreCase(mapName)) {
                user.playSound(Sound.ANVIL_BREAK, 1, 1);
                user.sendMessage("%s§cDiese Map ist bereist ausgewählt.", OITC.PREFIX);
                return;
            }

            Optional<Map> mapOptional = mapManager.getMap(mapName);
            if (!mapOptional.isPresent()) {
                user.playSound(Sound.ANVIL_BREAK, 1, 1);
                user.sendMessage("%s§cEin Fehler ist aufgetreten.", OITC.PREFIX);
                return;
            }
            Map map = mapOptional.get();
            game.setCurrentMap(map);
            user.sendMessage("%s§aDu hast die Map zu %s geändert.", OITC.PREFIX, map.get(MapData.MAP_NAME, String.class));
            user.playSound(Sound.LEVEL_UP, 1, 1);
            mapForced = true;
        });

        NocAPI.getGuiManager().registerGui("forcemap", builder);
    }

    @Override
    public void onPlayerExecute(Player player, String command, String[] args) {
        if (!player.hasPermission("forcemap")) {
            player.sendMessage(OITC.NO_PERMISSION);
            return;
        }

        if (!stateManager.isState(StateManager.LOBBY_STATE)) {
            player.sendMessage(String.format("%s§cDieser Command funktioniert nur während der Lobby-Phase.", OITC.PREFIX));
            return;
        }

        if (mapForced) {
            player.sendMessage(String.format("%sEs wurde bereits eine andere §cMap §7ausgewählt.", OITC.PREFIX));
            return;
        }

        LobbyState state = (LobbyState) stateManager.getCurrentState();
        if (state.getRemainingTime() <= 10 || game.isMapLoaded()) {
            player.sendMessage(String.format("%s§cDie Map kann nicht mehr geändert werden.", OITC.PREFIX));
            return;
        }
        NocAPI.getGuiManager().openGui("forcemap", player);
    }

    @Override
    public void onConsoleExecute(ConsoleCommandSender sender, String command, String[] args) {
        sender.sendMessage("§cThis command is only available for a player.");
    }
}
