package hardcorelife.chryscorelab.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import hardcorelife.chryscorelab.Touchy;

public class ResetServer implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label,
            @NotNull String[] args) {

        Boolean confirm = false;
        try {
            confirm = Boolean.parseBoolean(args[0]);
        } catch (Exception e) {
            return false;
        }

        if (confirm) {
            Touchy.get().resetServer();
        } else {
            return false;
        }

        return true;
    }
}
