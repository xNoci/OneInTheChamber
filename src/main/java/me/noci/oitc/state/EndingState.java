package me.noci.oitc.state;

import com.google.common.collect.Lists;
import de.dytanic.cloudnet.bridge.CloudServer;
import de.dytanic.cloudnet.lib.server.ServerState;
import me.noci.noclib.api.NocAPI;
import me.noci.noclib.api.scoreboard.Scoreboard;
import me.noci.noclib.api.user.User;
import me.noci.noclib.packtes.utils.WrappedEnumScoreboardTeamAction;
import me.noci.noclib.packtes.utils.WrappedScoreboardTeam;
import me.noci.noclib.packtes.wrapper.server.WrappedServerScoreboardTeam;
import me.noci.oitc.OITC;
import me.noci.oitc.gameutils.OITCRank;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

public class EndingState extends State {

    private static final int TIME = 15;
    private int remainingTime;

    @Override
    protected void start() {
        try {
            CloudServer.getInstance().setServerStateAndUpdate(ServerState.OFFLINE);
        } catch (NoClassDefFoundError | Exception e) {
        }

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

        this.remainingTime = TIME;
    }

    @Override
    protected void stop() {
        Bukkit.getServer().shutdown();
    }

    @Override
    protected void update() {
        NocAPI.getOnlineUsers().forEach(user -> {
            if (remainingTime == 15 || remainingTime == 10 || remainingTime <= 5) {
                user.setLevelValue(remainingTime, TIME, Sound.NOTE_BASS, 1, 1);
                sendRemainingTime(user, remainingTime);
            } else {
                user.setLevelValue(remainingTime, TIME);
            }
        });

        if (remainingTime <= 0) {
            stop();
            return;
        }
        remainingTime--;
    }

    @Override
    protected void updateTabList(User user) {
        for (User player : NocAPI.getOnlineUsers()) {
            OITCRank rank = OITCRank.getRank(player.getBase(), game, false);
            WrappedScoreboardTeam team = new WrappedScoreboardTeam(rank.getTeamName(player.getBase()));
            team.getEntries().add(player.getName());
            team.setPrefix(rank.getPrefix());

            user.sendPacket(new WrappedServerScoreboardTeam(team, WrappedEnumScoreboardTeamAction.REMOVE_TEAM));
            user.sendPacket(new WrappedServerScoreboardTeam(team, WrappedEnumScoreboardTeamAction.CREATE_TEAM));
        }
    }

    @Override
    protected void updatePlayerScoreboard(Scoreboard scoreboard, User user) {
        List<String> lines = Lists.newArrayList();
        lines.add("");
        lines.add(" §7Gewinner");
        lines.add(String.format("  §8» §c%s", game.getWinner()));
        lines.add("");
        scoreboard.updateLines(lines);
    }

    private void sendRemainingTime(User user, int time) {
        if (time != 1) {
            user.sendMessage("%sDer Server stoppt in §c%s §7Sekunden.", OITC.PREFIX, time);
        } else {
            user.sendMessage("%sDer Server stoppt in §c%s §7Sekunde.", OITC.PREFIX, time);
        }
    }

}
