package hardcorelife.chryscorelab;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import hardcorelife.chryscorelab.commands.Lives;
import hardcorelife.chryscorelab.helpers.PlayerLife;
import hardcorelife.chryscorelab.listeners.PlayerDeath;
import hardcorelife.chryscorelab.listeners.PlayerJoinServer;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public final class Touchy extends JavaPlugin {

    private static Touchy instance;

    @Override
    public void onEnable() {
        instance = this;

        // Plugin startup logic
        saveDefaultConfig();
        getConfig().options().copyDefaults(true);
        GetLivesConfigFile();

        // TODO - Make this dynamic | This is really a need ?
        // World world = Bukkit.getWorld("world");

        Objects.requireNonNull(getCommand("lives")).setExecutor(new Lives());

        // assert world != null;

        PluginManager pm = getServer().getPluginManager();

        pm.registerEvents(new PlayerDeath(), this);
        pm.registerEvents(new PlayerJoinServer(), this);

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        PlayerLife.saveLivesData();
        instance = null;
    }

    public static Touchy get() {
        return instance;
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
}
