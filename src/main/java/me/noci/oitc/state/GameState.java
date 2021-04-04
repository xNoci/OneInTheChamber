package me.noci.oitc.state;

import me.noci.noclib.api.NocAPI;
import me.noci.noclib.api.scoreboard.Scoreboard;
import me.noci.noclib.api.user.User;
import me.noci.noclib.packtes.utils.WrappedEnumScoreboardTeamAction;
import me.noci.noclib.packtes.utils.WrappedScoreboardTeam;
import me.noci.noclib.packtes.wrapper.server.WrappedServerScoreboardTeam;
import me.noci.noclib.utils.items.AdvancedItemStack;
import me.noci.oitc.gameutils.PlayerData;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Iterator;
import java.util.List;
import java.util.UUID;

public class GameState extends State {

    private long timeRemaining;

    @Override
    protected void start() {
        timeRemaining = game.getGameDuration();

        Iterator<Location> mapSpawns = game.getCurrentMap().getPlayerSpawns().iterator();
        Location fallbackSpawn = game.getCurrentMap().getPlayerSpawns().get(0).clone();

        for (UUID uuid : game.getPlayerSet()) {
            User user = NocAPI.getUser(uuid);
            game.getPlayerData(user.getUUID()); //CREATE PLAYER DATA

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
                    user.getBase().getInventory().addItem(new AdvancedItemStack(Material.BOW).addItemFlags().setUnsafeEnchantment(Enchantment.ARROW_INFINITE, 1).setUnbreakable(true));
                    user.getBase().getInventory().addItem(new AdvancedItemStack(Material.ARROW).addItemFlags());

                    Location playerSpawn = fallbackSpawn;
                    if(mapSpawns.hasNext()) {
                        playerSpawn = mapSpawns.next();
                    }
                    user.getBase().teleport(playerSpawn);
                }
            }.runTask(plugin);
        }
    }

    @Override
    protected void stop() {

    }

    @Override
    protected void update() {
        if (timeRemaining <= 0) {
            changeState(StateManager.ENDING_STATE);
            return;
        }

        timeRemaining--;
    }

    @Override
    protected void updateTabList(User user) {
        for (User player : NocAPI.getOnlineUsers()) {
            String teamName = player.getUUID().toString().replaceAll("-", "");
            if (teamName.length() > 16) teamName = teamName.substring(0, 16);
            WrappedScoreboardTeam team = new WrappedScoreboardTeam(teamName);
            team.getEntries().add(player.getName());
            team.setPrefix("§9User §8| §7");

            if (game.getSpectatorSet().contains(player.getUUID())) {
                team.setPrefix("§7");
                team.setCanSeeFriendlyInvisibles(true);
            }

            user.sendPacket(new WrappedServerScoreboardTeam(team, WrappedEnumScoreboardTeamAction.REMOVE_TEAM));
            user.sendPacket(new WrappedServerScoreboardTeam(team, WrappedEnumScoreboardTeamAction.CREATE_TEAM));
        }
    }

    @Override
    protected void updatePlayerScoreboard(Scoreboard scoreboard, User user) {
        scoreboard.updateTitle("     §9OITC     ");
        scoreboard.updateLine(0, "");
        scoreboard.updateLine(1, " §7Zeit");
        scoreboard.updateLine(2, String.format("  §8» §c%s", formatTime()));
        scoreboard.updateLine(3, "");

        int line = 4;
        List<PlayerData> sortedPlayerData = game.getPlayerDataSorted();

        for (PlayerData playerData : sortedPlayerData) {
            String content = String.format(" §c%s §7(%s) ", playerData.getUser().getName(), playerData.getScore());
            if (content.length() > 30) content = content.substring(0, 30);

            scoreboard.updateLine(line, content);
            line++;
        }

        scoreboard.updateLine(line, "");
    }

    private String formatTime() {
        long time = timeRemaining;
        int minutes = (int) (timeRemaining / 60);
        int seconds = (int) (time % 60);

        if (minutes <= 0) return String.format("%s Sekunden", seconds);

        return String.format("%s:%s", minutes < 10 ? "0" + minutes : minutes, seconds < 10 ? "0" + seconds : seconds);
    }

}
