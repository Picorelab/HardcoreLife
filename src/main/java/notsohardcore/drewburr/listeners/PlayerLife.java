package notsohardcore.drewburr.listeners;

import org.bukkit.entity.Player;

import notsohardcore.drewburr.Touchy;

import java.util.HashMap;
import java.util.UUID;

public class PlayerLife {
    private static Touchy touchy;

    private final Player player;

    public static HashMap<UUID, Integer> lives = new HashMap<>();

    public PlayerLife(Player player) {
        this.player = player;
    }

    public static int getLives(Player player) {
        if (!lives.containsKey(player.getUniqueId()))
            lives.put(player.getUniqueId(), getConfigLives(player));
        return lives.get(player.getUniqueId());
    }

    public static int getConfigLives(Player player) {
        return Touchy.get().getLivesConfig().getInt(player.getUniqueId() + ".lives");
    }

    public static void addLives(Player player, int i) {
        int finalLives = getLives(player) + i;
        lives.put(player.getUniqueId(), finalLives);
    }

    public static void removeLives(Player player, int i) {
        int finalLives = getLives(player) - i;
        lives.put(player.getUniqueId(), finalLives);

    }

    // Force set numbers to lives
    public void forceSetLives(int number) {
        UUID uuid = this.player.getUniqueId();
        lives.put(player.getUniqueId(), number);
        // TODO - perms
    }

}
