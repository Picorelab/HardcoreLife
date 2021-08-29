package hardcorelife.chryscorelab.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import hardcorelife.chryscorelab.helpers.PlayerLife;

public class HowManyLives implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label,
            @NotNull String[] args) {

        if (sender instanceof Player) {
            Player player = (Player) sender;

            player.sendMessage("You have " + PlayerLife.getLives(player) + " live(s) left.");

        }
        return false;
    }
}
