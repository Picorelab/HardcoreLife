package hardcorelife.dafray.commands;

import hardcorelife.dafray.listeners.PlayerLife;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class HowManyLives implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label,
            @NotNull String[] args) {

        if (sender instanceof Player) {
            Player player = (Player) sender;

            if (PlayerLife.getConfigLives(player) >= 2) {
                player.sendMessage("You have " + PlayerLife.getLives(player) + " lives");
            } else if (PlayerLife.getConfigLives(player) == 1) {
                player.sendMessage("You have " + PlayerLife.getLives(player) + " life left");
            } else if (PlayerLife.getConfigLives(player) == 0) {
                player.sendMessage("Ooops but you don't have lives");
            }

        }
        return false;
    }
}
