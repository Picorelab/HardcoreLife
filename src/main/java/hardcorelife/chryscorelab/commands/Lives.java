package hardcorelife.chryscorelab.commands;

import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import hardcorelife.chryscorelab.Touchy;
import hardcorelife.chryscorelab.helpers.Lang;
import hardcorelife.chryscorelab.helpers.PlayerLife;

public class Lives implements CommandExecutor {

    private static Touchy touchy = Touchy.get();
    private static Server server = touchy.getServer();

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label,
            @NotNull String[] args) {

        if (touchy.globalLivesEnabled()) {
            sender.sendMessage(Lang.get(sender, "lives.server", "lives", PlayerLife.getServerLives()));
        } else {
            if (args.length > 0) {
                Player player = server.getPlayer(args[0]);

                if (player instanceof Player) {
                    sender.sendMessage(
                            Lang.get(sender, "lives.player", "player", player.getName(), "lives", PlayerLife.getLives(player)));
                } else {
                    sender.sendMessage(Lang.get(sender, "error.unknown_player", "player", args[0]));
                    return false;
                }

            } else if (sender instanceof Player) {
                Player player = (Player) sender;
                sender.sendMessage(Lang.get(sender, "lives.self", "lives", PlayerLife.getLives(player)));
            } else {
                return false;
            }
        }
        return true;
    }
}
