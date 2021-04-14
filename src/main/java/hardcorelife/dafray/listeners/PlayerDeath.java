package hardcorelife.dafray.listeners;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class PlayerDeath implements Listener {




    @EventHandler
    public void onDeath(PlayerDeathEvent event){

        Player player = event.getEntity();
        PlayerLife playerLife = new PlayerLife(player);
        Location deathLocation = player.getLocation();


        PlayerLife.removeLives(player, 1);

        if(PlayerLife.getLives(player) >= 2){
            //TODO - You have "numbers" left;
        }
        else if(PlayerLife.getLives(player) == 1){
            // TODO - You have 1 left ! Be aware !
            player.sendMessage("You have 1 life left ! Be aware !");
        }
        else if(PlayerLife.getLives(player) == 0){
            // TODO - Oh no ! You have 0 left !
            player.sendMessage("Oh no ! You don't have life anymore ! ");
            Bukkit.broadcastMessage(player.getName() + " Died, he have 0 lives left !");
            // TODO - Fireworks
            // TODO - Spectator
            player.setGameMode(GameMode.SPECTATOR);
            player.spigot().respawn();
            player.teleport(deathLocation);
            player.setFlySpeed(0);
            player.setWalkSpeed(0);


            Bukkit.getConsoleSender().sendMessage(player.getName() + " is dead");
        }


    }

}
