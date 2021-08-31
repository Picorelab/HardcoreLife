package hardcorelife.chryscorelab.helpers;

import org.bukkit.entity.Player;

import hardcorelife.chryscorelab.Touchy;

import java.util.HashMap;
import java.util.UUID;

public class PlayerLife {

    private static HashMap<UUID, Integer> lives = new HashMap<>();

    public static void initPlayer(Player player) {
        // Triggers the player data to be loaded from config.yml
        getLives(player);
    }

    public static int getLives(Player player) {
        // Get the current number of lives for a player
        // Will pull player data from the config, if not already loaded
        if (!lives.containsKey(getUUID(player))) {
            setLives(player, getConfigLives(player));
        }
        return lives.get(getUUID(player));
    }

    private static int getConfigLives(Player player) {
        // Pulls the current number of lives for a player from the config
        return Touchy.get().getPlayerLivesConfig(getUUID(player));
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

    public static void forceSetLives(Player player, int lifeCount) {
        // Force set life value
        setLives(player, lifeCount);
    }

    private static void setLives(Player player, int lifeCount) {
        // Updates the player's life count, then saves configs
        if (lifeCount < 0) {
            lifeCount = 0;
        }
        lives.put(getUUID(player), lifeCount);
        Touchy.get().savePlayerLifeConfig(getUUID(player), lifeCount);
    }

    public static void saveLivesData() {
        // Saves lives object to disk
        Touchy.get().saveHashmapData(lives);
    }

    public static HashMap<UUID, Integer> getLivesMap() {
        return lives;
    }

    private static UUID getUUID(Player player) {
        // Wrapper to handle when global lives are enabled
        if (Touchy.get().globalLivesEnabled())
            return new UUID(0, 0);
        else
            return player.getUniqueId();
    }

}
