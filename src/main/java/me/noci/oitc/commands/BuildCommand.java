package me.noci.oitc.commands;

import me.noci.noclib.api.NocAPI;
import me.noci.noclib.api.user.User;
import me.noci.noclib.command.Command;
import me.noci.noclib.command.CommandData;
import me.noci.oitc.BuildManager;
import me.noci.oitc.OITC;
import net.atophia.atophiaapi.language.LanguageAPI;
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
            LanguageAPI.send(user.getBase(), "command.build.usage");
            return;
        }

        if (args.length == 0) {
            if (BuildManager.toggleBuild(user.getUUID())) {
                user.getBase().setGameMode(GameMode.CREATIVE);
                sendTitle(user, true);
            } else {
                user.getBase().setGameMode(GameMode.SURVIVAL);
                sendTitle(user, false);
            }
            return;
        }


        Player targetPlayer = Bukkit.getPlayer(args[0]);
        if (targetPlayer == null) {
            LanguageAPI.send(user.getBase(), "command.user_not_found");
            return;
        }

        User targetUser = NocAPI.getUser(targetPlayer);

        if (BuildManager.toggleBuild(targetUser.getUUID())) {
            targetUser.getBase().setGameMode(GameMode.CREATIVE);
            sendTitle(targetUser, true);
            LanguageAPI.send(user.getBase(), "command.build.change_other.activated", targetUser.getName());
        } else {
            targetUser.getBase().setGameMode(GameMode.SURVIVAL);
            sendTitle(targetUser, false);
            LanguageAPI.send(user.getBase(), "command.build.change_other.deactivated", targetUser.getName());
        }
    }

    @Override
    public void onConsoleExecute(ConsoleCommandSender sender, String command, String[] args) {
        sender.sendMessage(LanguageAPI.getFormatted("command.console.only_available_for_player"));
    }

    private static void sendTitle(User user, boolean activated) {
        if (activated) {
            user.sendTitle(LanguageAPI.getFormatted(user.getUUID(), "command.build.title.activated.title"), LanguageAPI.getFormatted(user.getUUID(), "command.build.title.activated.subtitle"), 0, 20, 0);
        } else {
            user.sendTitle(LanguageAPI.getFormatted(user.getUUID(), "command.build.title.deactivated.title"), LanguageAPI.getFormatted(user.getUUID(), "command.build.title.deactivated.subtitle"), 0, 20, 0);
        }
    }
}
