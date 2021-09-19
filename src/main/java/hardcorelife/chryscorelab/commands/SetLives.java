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
import hardcorelife.chryscorelab.helpers.PlayerLife;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;

public class SetLives implements CommandExecutor {

    private static Touchy touchy = Touchy.get();
    private static Server server = touchy.getServer();

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label,
            @NotNull String[] args) {

        Player player;
        String numArg;
        int newCount;
        int oldCount;

        // Usage: /setlives [player] <value>
        // Must be a number greater than 0

        // TODO - Handle when a player's life count hits 0, then is increased

        if (args.length == 2) {
            // Set another player's life count
            player = server.getPlayer(args[0]);
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

        oldCount = PlayerLife.getLives(player);
        PlayerLife.forceSetLives(player, newCount);
        sender.sendMessage("Set " + player.getName() + "'s life count to: " + numArg);
        player.sendMessage(sender.getName() + " has changed your life count to: " + numArg);

        // Handle reviving a player/server
        if (oldCount == 0 && newCount > 0) {
            if (touchy.globalLivesEnabled()) {
                // Handle server life refresh
                server.setDefaultGameMode(GameMode.SURVIVAL);

                // Revive all players
                for (Player p : server.getOnlinePlayers()) {
                    revivePlayer(p);
                }

                // Restrict resetserver permission
                PluginManager pm = server.getPluginManager();
                Permission resetserver = pm.getPermission("hardcorelife.resetserver");
                resetserver.setDefault(PermissionDefault.FALSE);

                TextComponent reset_comp = Component
                        .text("The server's life count has been increased to: " + String.valueOf(newCount));
                server.broadcast(reset_comp);
            } else {
                // Revive the player without altering life count
                revivePlayer(player);
            }
        }

        return true;
    }

    public void revivePlayer(Player player) {
        // Force-respawn a player without altering their life count
        // Mainly used when reviving a player who experiences permadeath
        player.setGameMode(GameMode.SURVIVAL);

        PlayerLife.addLife(player);
        player.setHealth(0);

        // Re-enable movement
        player.setWalkSpeed(0.2f);
        player.setFlySpeed(0.1f);
    }

}
