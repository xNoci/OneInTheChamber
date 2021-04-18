package me.noci.oitc.commands;

import me.noci.noclib.api.NocAPI;
import me.noci.noclib.api.user.User;
import me.noci.noclib.command.Command;
import me.noci.noclib.command.CommandData;
import me.noci.oitc.BuildManager;
import me.noci.oitc.OITC;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

@CommandData(name = "build")
public class BuildCommand extends Command {

    public BuildCommand(JavaPlugin plugin) {
        super(plugin);
    }

    @Override
    public void onPlayerExecute(Player player, String command, String[] args) {
        User user = NocAPI.getUser(player);
        if (!user.hasPermission("build")) {
            user.sendMessage(OITC.NO_PERMISSION);
            return;
        }

        if (args.length > 1) {
            user.sendMessage("%s§cBenutze: /build [Name]", OITC.PREFIX);
            return;
        }

        if (args.length == 0) {
            if (BuildManager.toggleBuild(user.getUUID())) {
                user.getBase().setGameMode(GameMode.CREATIVE);
                user.sendTitle("§a§l✔", "§7Baumodus aktiviert.", 0, 20, 0);
            } else {
                user.getBase().setGameMode(GameMode.SURVIVAL);
                user.sendTitle("§c§l✘", "§7Baumodus deaktiviert.", 0, 20, 0);
            }
            return;
        }


        Player targetPlayer = Bukkit.getPlayer(args[0]);
        if (targetPlayer == null) {
            user.sendMessage("%s§cDieser Spieler wurde nicht gefunden.");
            return;
        }

        User targetUser = NocAPI.getUser(targetPlayer);

        if (BuildManager.toggleBuild(targetUser.getUUID())) {
            targetUser.getBase().setGameMode(GameMode.CREATIVE);
            targetUser.sendTitle("§a§l✔", "§7Baumodus aktiviert.", 0, 20, 0);
            user.sendMessage("%sDer Spieler §6%s §7kann §anun §7bauen.", OITC.PREFIX, targetUser.getName());
        } else {
            targetUser.getBase().setGameMode(GameMode.SURVIVAL);
            targetUser.sendTitle("§c§l✘", "§7Baumodus deaktiviert.", 0, 20, 0);
            user.sendMessage("%sDer Spieler §6%s §7kann nun §cnicht mehr §7bauen.", OITC.PREFIX, targetUser.getName());
        }
    }

    @Override
    public void onConsoleExecute(ConsoleCommandSender sender, String command, String[] args) {
        sender.sendMessage("§cThis command is only available for a player.");
    }
}
