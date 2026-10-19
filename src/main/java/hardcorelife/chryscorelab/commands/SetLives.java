package hardcorelife.chryscorelab.commands;

import org.bukkit.GameMode;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.plugin.PluginManager;
import org.jetbrains.annotations.NotNull;

import hardcorelife.chryscorelab.Touchy;
import hardcorelife.chryscorelab.helpers.Lang;
import hardcorelife.chryscorelab.helpers.PlayerLife;

public class SetLives implements CommandExecutor {

    private static Touchy touchy = Touchy.get();
    private static Server server = touchy.getServer();

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label,
            @NotNull String[] args) {

        Player player;

        // Usage: /setlives [player] <value>
        // Must be a number greater than 0
        // Player only needed when global_lives is disabled

        if (args.length == 2) {
            // Set another player's life count
            if (touchy.globalLivesEnabled()) {
                return updateLives(args[1]);
            } else {
                player = server.getPlayer(args[0]);
                if (!(player instanceof Player)) {
                    sender.sendMessage(Lang.get(sender, "error.unknown_player", "player", args[0]));
                    return false;
                } else {
                    boolean success = updateLives(player, args[1]);
                    if (success) {
                        sender.sendMessage(
                                Lang.get(sender, "setlives.changed_player", "player", args[0], "lives", args[1]));
                    }
                    return success;
                }
            }
        } else if (args.length == 1) {
            if (touchy.globalLivesEnabled()) {
                // Set the server's life count
                return updateLives(args[0]);
            } else if (sender instanceof Player) {
                // Set the sender's life count
                player = (Player) sender;
                return updateLives(player, args[0]);
            } else {
                sender.sendMessage(Lang.get(sender, "error.player_and_value_required"));
                return false;
            }
        } else {
            return false;
        }
    }

    private boolean isPositiveInt(String val) {
        try {
            if (Integer.parseInt(val) < 1) {
                throw new java.lang.NumberFormatException();
            }
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

    private boolean updateLives(Player player, String lifeArg) {
        if (!isPositiveInt(lifeArg)) {
            return false;
        }
        int lifeCount = Integer.parseInt(lifeArg);
        int oldCount = PlayerLife.getLives(player);
        PlayerLife.forceSetLives(player, lifeCount);
        player.sendMessage(Lang.get(player, "setlives.changed_self", "lives", lifeCount));
        if (oldCount == 0) {
            revivePlayer(player);
        }
        return true;
    }

    private boolean updateLives(String lifeArg) {
        if (!isPositiveInt(lifeArg)) {
            return false;
        }
        int lifeCount = Integer.parseInt(lifeArg);
        int oldCount = PlayerLife.getServerLives();
        PlayerLife.forceSetServerLives(lifeCount);
        Lang.broadcast("setlives.changed_server", "lives", lifeCount);
        if (oldCount == 0) {
            reviveServer();
        }
        return true;
    }

    private void revivePlayer(Player player) {
        // Revive the player without altering life count
        PlayerLife.revivePlayer(player);
    }

    private void reviveServer() {
        if (touchy.globalLivesEnabled()) {
            // Handle server life refresh
            server.setDefaultGameMode(GameMode.SURVIVAL);

            // Revive all players
            for (Player p : server.getOnlinePlayers()) {
                PlayerLife.revivePlayer(p);
            }

            // Restrict resetserver permission
            PluginManager pm = server.getPluginManager();
            Permission resetserver = pm.getPermission("hardcorelife.resetserver");
            resetserver.setDefault(PermissionDefault.FALSE);
        }
    }

}
