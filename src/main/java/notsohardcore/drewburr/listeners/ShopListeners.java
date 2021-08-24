package notsohardcore.drewburr.listeners;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;

import notsohardcore.drewburr.Touchy;

public class ShopListeners implements Listener {

    @EventHandler
    public void onClickShop(InventoryClickEvent event) {
        InventoryView inv = event.getView();
        Player player = (Player) event.getWhoClicked();
        ItemStack current = event.getCurrentItem();

        if (current == null) {
            return;
        }

        if (inv.getTitle().equalsIgnoreCase("§7 Shop Hardlife")) {
            event.setCancelled(true);
            if (current.getType() == Material.FLINT) {
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
        }

    }
}
