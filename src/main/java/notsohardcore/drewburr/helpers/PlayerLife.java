package notsohardcore.drewburr.helpers;

import org.bukkit.entity.Player;

import notsohardcore.drewburr.Touchy;

import java.util.HashMap;
import java.util.UUID;

public class PlayerLife {
    private static Touchy touchy;

    private final Player player;

    private static HashMap<UUID, Integer> lives = new HashMap<>();

    public PlayerLife(Player player) {
        this.player = player;
    }

    public static void initPlayer(Player player) {
        // Triggers the player data to be loaded from config.yml
        getLives(player);
    }

    public static int getLives(Player player) {
        // Get the current number of lives for a player
        // Will pull player data from the config, if not already loaded
        if (!lives.containsKey(player.getUniqueId())) {
            setLives(player, getConfigLives(player));
        }
        return lives.get(player.getUniqueId());
    }

    private static int getConfigLives(Player player) {
        // Pulls the current number of lives for a player from the config
        return touchy.getPlayerLivesConfig(player.getUniqueId());
    }

    public static int addLife(Player player) {
        // Adds a life to a player
        int finalLives = getLives(player) + 1;
        setLives(player, finalLives);
        return finalLives;
    }

    public static int removeLife(Player player) {
        // Removes a life from a player
        int finalLives = getLives(player) - 1;
        setLives(player, finalLives);
        return finalLives;
    }

    public void forceSetLives(int lifeCount) {
        // Force set numbers to lives
        // TODO - Should be OP only
        setLives(player, lifeCount);
    }

    private static void setLives(Player player, int lifeCount) {
        // Updates the player's life count, then saves configs
        // TODO - Does not support global lives
        lives.put(player.getUniqueId(), lifeCount);
        touchy.savePlayerLifeConfig(player.getUniqueId(), lifeCount);
    }

    public static void saveLivesData() {
        // Saves lives object to disk
        touchy.saveHashmapData(lives);
    }

    public static HashMap<UUID, Integer> getLivesMap() {
        return lives;
    }

}
