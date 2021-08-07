package hardcorelife.dafray.commands;

import hardcorelife.dafray.Touchy;
import hardcorelife.dafray.listeners.PlayerLife;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class BuyLives implements CommandExecutor {

    public Touchy touchy;

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label,
            @NotNull String[] args) {

        if (sender instanceof Player) {

            Player player = (Player) sender;

            if (Touchy.get().getEconomy().getBalance(player) >= Touchy.get().getConfig().getDouble("lifePrice")) {
                Touchy.get().getEconomy().withdrawPlayer(player, Touchy.get().getConfig().getDouble("lifePrice"));
                PlayerLife.addLives(player, 1);

                if (player.getGameMode() == GameMode.SPECTATOR) {
                    Location spawn = new Location(player.getWorld(), Touchy.get().getConfig().getDouble("spawn.x"),
                            Touchy.get().getConfig().getDouble("spawn.y"),
                            Touchy.get().getConfig().getDouble("spawn.z"));
                    player.teleport(spawn);
                    player.setGameMode(GameMode.SURVIVAL);
                    player.setFlySpeed(0.1f);
                    player.setWalkSpeed(0.2f);

                }

                player.sendMessage("You have buy 1 life !");
            } else {
                player.sendMessage("Oops, and Error when buying the life");
            }

        }

        return false;
    }
}
