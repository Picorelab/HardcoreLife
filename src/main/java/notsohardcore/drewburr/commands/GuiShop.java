package notsohardcore.drewburr.commands;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import notsohardcore.drewburr.Touchy;

import java.util.Collections;

public class GuiShop implements CommandExecutor {

    private Touchy touchy;

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label,
            @NotNull String[] args) {

        if (sender instanceof Player) {
            Player player = (Player) sender;

            ItemStack Life1 = new ItemStack(Material.FLINT);
            ItemMeta Life1Meta = Life1.getItemMeta();

            Life1Meta.setDisplayName("Buy 1 life");
            Life1Meta.setLore(Collections.singletonList("100 $"));
            Life1.setItemMeta(Life1Meta);

            Inventory inventory = Bukkit.createInventory(null, 27, "§7 Shop Hardlife");

            inventory.setItem(11, Life1);

            player.openInventory(inventory);

        }
        return false;
    }
}
