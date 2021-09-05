package hardcorelife.chryscorelab;

import io.papermc.paper.event.world.WorldGameRuleChangeEvent;
import org.bukkit.Bukkit;
import org.bukkit.GameRule;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import org.apache.commons.io.FileUtils;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import hardcorelife.chryscorelab.commands.Lives;
import hardcorelife.chryscorelab.commands.ResetServer;
import hardcorelife.chryscorelab.commands.SetLives;
import hardcorelife.chryscorelab.helpers.PlayerLife;
import hardcorelife.chryscorelab.listeners.PlayerDeath;
import hardcorelife.chryscorelab.listeners.PlayerJoinServer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.logging.Logger;

public final class Touchy extends JavaPlugin {

    private static Touchy instance;
    private static boolean reset_on_unload = false;

    @Override
    public void onEnable() {
        instance = this;

        // Plugin startup logic
        saveDefaultConfig();
        getConfig().options().copyDefaults(true);
        GetLivesConfigFile();
        setNaturalRegFalse();

        Objects.requireNonNull(getCommand("lives")).setExecutor(new Lives());
        Objects.requireNonNull(getCommand("setlives")).setExecutor(new SetLives());
        Objects.requireNonNull(getCommand("resetserver")).setExecutor(new ResetServer());

        PluginManager pm = getServer().getPluginManager();

        pm.registerEvents(new PlayerDeath(), this);
        pm.registerEvents(new PlayerJoinServer(), this);

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        PlayerLife.saveLivesData();
        if (reset_on_unload) {
            deleteWorldData();
        }
        instance = null;
    }

    public static Touchy get() {
        return instance;
    }

    public void resetServer() {
        // Deletes all worlds on the server and resets life counts
        Logger logger = getServer().getLogger();

        // Kick all online players
        TextComponent message = Component.text("The server is resetting. Please rejoin.");
        for (Player p : getServer().getOnlinePlayers()) {
            p.kick(message);
        }

        // Delete existing lives.yml
        GetLivesConfigFile().delete();
        // Clear playerlife cache
        PlayerLife.clearLivesData();

        logger.info("Restarting the server..");
        reset_on_unload = true;
        getServer().spigot().restart();
    }

    private void deleteWorldData() {
        Logger logger = getServer().getLogger();
        logger.info("Deleting all world data");

        for (World world : Touchy.get().getServer().getWorlds()) {
            String worldName = world.getName();
            logger.info("Deleting: " + world.getName());

            try {
                FileUtils.deleteDirectory(world.getWorldFolder());
            } catch (Exception e) {
                logger.severe("Failed to delete world: " + worldName);
                logger.severe(e.getMessage());
            }
        }
    }

    private FileConfiguration GetLivesConfig() {
        File LivesConfigFile = GetLivesConfigFile();
        FileConfiguration LivesConfig = new YamlConfiguration();
        try {
            LivesConfig.load(LivesConfigFile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
        return LivesConfig;
    }

    private void SetLivesConfig(FileConfiguration LivesConfig) {
        File LivesConfigFile = GetLivesConfigFile();
        try {
            LivesConfig.save(LivesConfigFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private File GetLivesConfigFile() {
        // Ensures lives.yml exists, and returns the file object
        File LivesConfigFile = new File(getDataFolder(), "lives.yml");
        if (!LivesConfigFile.exists()) {
            LivesConfigFile.getParentFile().mkdirs();
            saveResource("lives.yml", false);
        }
        return LivesConfigFile;
    }

    public int getDefaultLives() {
        // Returns the starting_lives value from config.yml
        FileConfiguration defaultLivesConfig = getConfig();
        return defaultLivesConfig.getInt("starting_lives");
    }

    public boolean globalLivesEnabled() {
        // Returns the global_lives value from config.yml
        FileConfiguration defaultLivesConfig = getConfig();
        return defaultLivesConfig.getBoolean("global_lives");
    }

    public int getPlayerLivesConfig(UUID uuid) {
        // Gets the number of lives a player has from config.yml
        // If no data is set, the default is returned
        FileConfiguration livesConfig = GetLivesConfig();
        return livesConfig.getInt(uuid + ".lives", getDefaultLives());
    }

    public void savePlayerLifeConfig(UUID uuid, int lives) {
        // Update the life value for a single player, in lives.yml
        FileConfiguration LivesConfig = GetLivesConfig();
        LivesConfig.set(uuid + ".lives", lives);
        SetLivesConfig(LivesConfig);
    }

    public void saveHashmapData(HashMap<UUID, Integer> lives) {
        // Invoked during plugin shutdown, saves in-memory config to disk
        FileConfiguration LivesConfig = GetLivesConfig();
        for (Map.Entry<UUID, Integer> entry : lives.entrySet()) {
            LivesConfig.set(entry.getKey() + ".lives", entry.getValue());
        }
        SetLivesConfig(LivesConfig);
    }

    private void setNaturalRegFalse() {
        if (!getConfig().getBoolean("naturalRegeneration", false)) {
            for (int i = 0; i < Touchy.get().getServer().getWorlds().size(); i++) {
                Touchy.get().getServer().getWorlds().get(i).setGameRule(GameRule.NATURAL_REGENERATION, false);
            }
            Bukkit.getConsoleSender()
                    .sendMessage("[HardcoreLife] The Gamerule NATURAL_REGENERATION have been disabled");
        }
    }
}
