package me.noci.oitc.state;

import me.noci.noclib.api.NocAPI;
import me.noci.noclib.api.scoreboard.Scoreboard;
import me.noci.noclib.api.user.User;
import me.noci.noclib.packtes.utils.WrappedEnumScoreboardTeamAction;
import me.noci.noclib.packtes.utils.WrappedScoreboardTeam;
import me.noci.noclib.packtes.wrapper.server.WrappedServerScoreboardTeam;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.scheduler.BukkitRunnable;

public class EndingState extends State {

    @Override
    protected void start() {
        NocAPI.getOnlineUsers().forEach(user -> {
            new BukkitRunnable() {
                @Override
                public void run() {
                    Bukkit.getOnlinePlayers().forEach(other -> other.showPlayer(user.getBase()));
                    user.clearInventoryAndArmor();
                    user.getBase().setFoodLevel(20);
                    user.getBase().setHealth(user.getBase().getMaxHealth());
                    user.getBase().setGameMode(GameMode.SURVIVAL);
                    user.removePotionEffects();
                    user.getBase().spigot().setCollidesWithEntities(true);
                    game.teleportToLobby(user.getBase());
                }
            }.runTask(plugin);
        });
    }

    @Override
    protected void stop() {

    }

    @Override
    protected void update() {

    }

    @Override
    protected void updateTabList(User user) {
        for (User player : NocAPI.getOnlineUsers()) {
            String teamName = player.getUUID().toString().replaceAll("-", "");
            if (teamName.length() > 16) teamName = teamName.substring(0, 16);

            WrappedScoreboardTeam team = new WrappedScoreboardTeam(teamName);
            team.setPrefix("§9User §8| §7");
            team.getEntries().add(player.getName());
            user.sendPacket(new WrappedServerScoreboardTeam(team, WrappedEnumScoreboardTeamAction.REMOVE_TEAM));
            user.sendPacket(new WrappedServerScoreboardTeam(team, WrappedEnumScoreboardTeamAction.CREATE_TEAM));
        }
    }

    @Override
    protected void updatePlayerScoreboard(Scoreboard scoreboard, User user) {
        scoreboard.updateTitle("     §9OITC     ");
        scoreboard.updateLine(0, "");
        scoreboard.updateLine(1, " §7Gewinner");
        scoreboard.updateLine(2, String.format("  §8» §c%s", game.getWinner()));
        scoreboard.updateLine(3, "");
    }

}
