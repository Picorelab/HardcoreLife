package hardcorelife.chryscorelab.commands;

import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import hardcorelife.chryscorelab.Touchy;
import hardcorelife.chryscorelab.helpers.PlayerLife;

public class Lives implements CommandExecutor {

    private static Touchy touchy = Touchy.get();
    private static Server server = touchy.getServer();

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label,
            @NotNull String[] args) {

        if (touchy.globalLivesEnabled()) {
            sender.sendMessage("The server has " + PlayerLife.getServerLives() + " live(s) left.");
        } else {
            if (args.length > 0) {
                Player player = server.getPlayer(args[0]);

                if (player instanceof Player) {
                    sender.sendMessage(player.getName() + " has " + PlayerLife.getLives(player) + " live(s) left.");
                } else {
                    sender.sendMessage("ERROR: Unknown player '" + args[0] + "'");
                    return false;
                }

            } else if (sender instanceof Player) {
                Player player = (Player) sender;
                sender.sendMessage("You have " + PlayerLife.getLives(player) + " live(s) left.");
            } else {
                return false;
            }
        }
        return true;
    }
}
