package hardcorelife.chryscorelab.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import hardcorelife.chryscorelab.helpers.PlayerLife;

public class SetLives implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label,
            @NotNull String[] args) {

        Player player;
        String numArg;
        int newCount;

        // Usage: \setlives [player] <value>
        // Must be a number greater than 0

        // TODO - Handle when a player's life count hits 0, then is increased

        if (args.length == 2) {
            // Set another player's life count
            player = Bukkit.getPlayer(args[0]);
            if (!(player instanceof Player)) {
                sender.sendMessage("ERROR: Unknown player '" + args[0] + "'");
                return false;
            }
            numArg = args[1];
        } else if (args.length == 1) {
            // Set the sender's life count
            if (sender instanceof Player) {
                player = (Player) sender;
                numArg = args[0];
            } else {
                sender.sendMessage("ERROR: Player and value must be specified.");
                return false;
            }
        } else {
            return false;
        }

        try {
            newCount = Integer.parseInt(numArg);
            if (newCount < 1)
                throw new java.lang.NumberFormatException();
        } catch (NumberFormatException e) {
            sender.sendMessage("Invalid lives value: " + numArg);
            return false;
        }

        PlayerLife.forceSetLives(player, newCount);
        sender.sendMessage("Set " + player.getName() + "'s life count to: " + numArg);
        player.sendMessage(sender.getName() + " has changed your life count to: " + numArg);

        return true;
    }

}
