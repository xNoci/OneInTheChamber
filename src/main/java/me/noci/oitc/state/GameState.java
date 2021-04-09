package me.noci.oitc.state;

import com.google.common.collect.Lists;
import de.dytanic.cloudnet.bridge.CloudServer;
import lombok.Getter;
import me.noci.noclib.api.NocAPI;
import me.noci.noclib.api.scoreboard.Scoreboard;
import me.noci.noclib.api.user.User;
import me.noci.noclib.packtes.utils.WrappedEnumScoreboardTeamAction;
import me.noci.noclib.packtes.utils.WrappedScoreboardTeam;
import me.noci.noclib.packtes.wrapper.server.WrappedServerScoreboardTeam;
import me.noci.noclib.utils.items.AdvancedItemStack;
import me.noci.oitc.gameutils.OITCRank;
import me.noci.oitc.gameutils.PlayerData;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Iterator;
import java.util.List;
import java.util.UUID;

public class GameState extends State {

    @Getter private boolean protectionTime;
    private int remainingProtectionTime;
    private long timeRemaining;

    @Override
    protected void start() {
        try {
            CloudServer.getInstance().changeToIngame();
        } catch (NoClassDefFoundError | Exception e) {
        }

        protectionTime = true;
        timeRemaining = game.getGameDuration();
        remainingProtectionTime = game.getProtectionTime();

        Iterator<Location> mapSpawns = game.getCurrentMap().playerSpawnIterator();
        Location fallbackSpawn = game.getCurrentMap().getPlayerSpawns().get(0).clone();

        for (UUID uuid : game.getPlayerSet()) {
            User user = NocAPI.getUser(uuid);
            game.getPlayerData(user.getBase()); //CREATE PLAYER DATA

            new BukkitRunnable() {
                @Override
                public void run() {
                    user.clearInventoryAndArmor();
                    user.removePotionEffects();
                    user.getBase().spigot().setCollidesWithEntities(true);
                    user.getBase().setAllowFlight(false);
                    user.getBase().setFoodLevel(20);
                    user.getBase().setHealth(user.getBase().getMaxHealth());
                    user.getBase().setGameMode(GameMode.SURVIVAL);
                    user.getBase().getInventory().addItem(new AdvancedItemStack(Material.WOOD_SWORD).addItemFlags().setUnbreakable(true));
                    user.getBase().getInventory().addItem(new AdvancedItemStack(Material.BOW).addItemFlags().setUnbreakable(true));
                    user.getBase().getInventory().addItem(new AdvancedItemStack(Material.ARROW).addItemFlags());

                    Location playerSpawn = fallbackSpawn;
                    if (mapSpawns.hasNext()) {
                        playerSpawn = mapSpawns.next();
                    }
                    user.getBase().teleport(playerSpawn);
                }
            }.runTask(plugin);
        }
    }

    @Override
    protected void stop() {
        List<PlayerData> playerDataList = game.getPlayerDataSorted();
        if (playerDataList.size() > 0) {
            PlayerData playerData = playerDataList.get(0);
            game.setWinner(playerData.getName());
        }
    }

    @Override
    protected void update() {
        if (protectionTime) {

            switch (remainingProtectionTime) {
                case 3:
                    NocAPI.getOnlineUsers().forEach(user -> {
                        user.sendTitle("§8| §cStartet §8|", String.format("§7... in §4%s", remainingProtectionTime), 0, 20, 0);
                        user.playSound(Sound.CLICK, 2, 2);
                    });
                    break;
                case 2:
                    NocAPI.getOnlineUsers().forEach(user -> {
                        user.sendTitle("§8| §cStartet §8|", String.format("§7... in §e%s", remainingProtectionTime), 0, 20, 0);
                        user.playSound(Sound.CLICK, 2, 2);
                    });

                    break;
                case 1:
                    NocAPI.getOnlineUsers().forEach(user -> {
                        user.sendTitle("§8| §cStartet §8|", String.format("§7... in §a%s", remainingProtectionTime), 0, 20, 0);
                        user.playSound(Sound.CLICK, 2, 2);
                    });
                    break;
            }

            if (remainingProtectionTime <= 0) {
                protectionTime = false;
                NocAPI.getOnlineUsers().forEach(user -> {
                    user.sendTitle("§8| §cGO §8|", "", 0, 20, 0);
                    user.playSound(Sound.CLICK, 2, 2);
                });
                return;
            }
            remainingProtectionTime--;
        } else {
            if (checkEnding()) {
                return;
            }

            timeRemaining--;
        }
    }

    @Override
    protected void updateTabList(User user) {
        for (User player : NocAPI.getOnlineUsers()) {
            OITCRank rank = OITCRank.getRank(player.getBase(), game, true);
            WrappedScoreboardTeam team = new WrappedScoreboardTeam(rank.getTeamName(player.getBase()));
            team.getEntries().add(player.getName());
            team.setPrefix(rank.getPrefix());

            if (game.getSpectatorSet().contains(player.getUUID())) {
                team.setCanSeeFriendlyInvisibles(true);
            }

            user.sendPacket(new WrappedServerScoreboardTeam(team, WrappedEnumScoreboardTeamAction.REMOVE_TEAM));
            user.sendPacket(new WrappedServerScoreboardTeam(team, WrappedEnumScoreboardTeamAction.CREATE_TEAM));
        }
    }

    @Override
    protected void updatePlayerScoreboard(Scoreboard scoreboard, User user) {
        List<String> lines = Lists.newArrayList();
        lines.add("");
        lines.add(" §7Zeit");
        lines.add(String.format("  §8» §c%s", formatTime()));
        lines.add("");

        List<PlayerData> sortedPlayerData = game.getPlayerDataSorted();
        for (PlayerData playerData : sortedPlayerData) {
            User other = playerData.getUser();
            OITCRank rank = OITCRank.getRank(other.getBase(), game, false);
            String content = String.format("%s%s §7(%s)", rank.getRankColor(), other.getName(), playerData.getScore());
            if (content.length() > 30) content = content.substring(0, 30);

            lines.add(content);
        }

        lines.add("");
        scoreboard.updateLines(lines);
    }

    public boolean checkEnding() {
        boolean timeOver = timeRemaining <= 0;
        boolean notEnoughPlayer = game.getPlayerSet().size() <= 1;

        boolean end = timeOver || notEnoughPlayer;
        if (!end) return false;
        changeState(StateManager.ENDING_STATE);
        return true;
    }

    private String formatTime() {
        long time = timeRemaining;
        int minutes = (int) (timeRemaining / 60);
        int seconds = (int) (time % 60);

        if (minutes <= 0) return String.format("%s Sekunden", seconds);

        return String.format("%s:%s", minutes < 10 ? "0" + minutes : minutes, seconds < 10 ? "0" + seconds : seconds);
    }

}
