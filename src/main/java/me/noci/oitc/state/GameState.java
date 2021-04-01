package me.noci.oitc.state;

import me.noci.noclib.api.NocAPI;
import me.noci.noclib.api.scoreboard.Scoreboard;
import me.noci.noclib.api.user.User;
import me.noci.noclib.utils.items.AdvancedItemStack;
import me.noci.oitc.gameutils.PlayerData;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;

import java.util.List;
import java.util.UUID;

public class GameState extends State {

    private long timeRemaining;

    @Override
    public void start() {
        timeRemaining = game.getGameDuration();
        for (UUID uuid : game.getPlayerSet()) {
            User user = NocAPI.getUser(uuid);
            game.getPlayerData(user.getUUID()); //CREATE PLAYER DATA

            user.clearInventoryAndArmor();
            user.getBase().setFoodLevel(20);
            user.getBase().setHealth(user.getBase().getMaxHealth());
            user.getBase().setGameMode(GameMode.SURVIVAL);
            user.getBase().getInventory().addItem(new AdvancedItemStack(Material.WOOD_SWORD).addItemFlags().setUnbreakable(true));
            user.getBase().getInventory().addItem(new AdvancedItemStack(Material.BOW).addItemFlags().setUnsafeEnchantment(Enchantment.ARROW_INFINITE, 1).setUnbreakable(true));
            user.getBase().getInventory().addItem(new AdvancedItemStack(Material.ARROW).addItemFlags());
        }
    }

    @Override
    public void stop() {

    }

    @Override
    public void update() {
        timeRemaining--;
    }

    @Override
    public void updatePlayerScoreboard(Scoreboard scoreboard, User user) {
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
