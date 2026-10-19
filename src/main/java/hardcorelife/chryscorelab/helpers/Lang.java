package hardcorelife.chryscorelab.helpers;

import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class Lang {

    public static final String DEFAULT_LANGUAGE = "en_EN";
    // Language files shipped inside the jar. Copied to the plugin folder on startup
    private static final String[] BUNDLED_LANGUAGES = { "en_EN", "de_DE", "es_ES", "fr_FR", "ja_JP", "ko_KR",
            "pl_PL", "pt_BR", "ru_RU", "zh_CN" };

    // All files in plugins/Hardcorelife/lang/, keyed by lowercase name (e.g. "pt_br")
    private static Map<String, FileConfiguration> languages = new TreeMap<>();
    private static FileConfiguration serverMessages = new YamlConfiguration();
    private static String serverLanguage = DEFAULT_LANGUAGE.toLowerCase(Locale.ROOT);
    private static boolean useClientLanguage = false;

    public static void load(JavaPlugin plugin, String language, boolean clientLanguage) {
        // Loads every plugins/Hardcorelife/lang/<language>.yml
        // Missing keys fall back to the bundled copy of that language, then to en_EN
        Logger logger = plugin.getLogger();

        for (String bundled : BUNDLED_LANGUAGES) {
            if (!getLangFile(plugin, bundled).exists()) {
                plugin.saveResource(getLangPath(bundled), false);
            }
        }

        FileConfiguration defaults = loadBundled(plugin, DEFAULT_LANGUAGE);
        languages = new TreeMap<>();
        File[] files = new File(plugin.getDataFolder(), "lang").listFiles((dir, name) -> name.endsWith(".yml"));
        if (files != null) {
            for (File file : files) {
                String name = file.getName().substring(0, file.getName().length() - ".yml".length());
                languages.put(name.toLowerCase(Locale.ROOT), loadLanguage(plugin, name, file, defaults));
            }
        }

        if (language == null || language.isBlank()) {
            language = DEFAULT_LANGUAGE;
        }
        if (!languages.containsKey(language.toLowerCase(Locale.ROOT))) {
            logger.warning("Language file '" + getLangPath(language) + "' not found. Falling back to "
                    + DEFAULT_LANGUAGE);
            language = DEFAULT_LANGUAGE;
        }
        serverLanguage = language.toLowerCase(Locale.ROOT);
        serverMessages = languages.get(serverLanguage);
        useClientLanguage = clientLanguage;

        logger.info("Loaded languages: " + String.join(", ", languages.keySet()) + ". Server language: " + language
                + (useClientLanguage ? " (players see their client language when available)" : ""));
    }

    public static String get(String key, Object... placeholders) {
        // Returns the message for key in the server language, with {name} placeholders replaced
        // Usage: Lang.get("lives.player", "player", name, "lives", count)
        return format(serverMessages, key, placeholders);
    }

    public static String get(CommandSender sender, String key, Object... placeholders) {
        // Returns the message in the sender's client language, if enabled
        return format(messagesFor(sender), key, placeholders);
    }

    public static void broadcast(String key, Object... placeholders) {
        // Sends a message to every online player in their own language, and to the console
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.sendMessage(get(player, key, placeholders));
        }
        Bukkit.getConsoleSender().sendMessage(get(key, placeholders));
    }

    @SuppressWarnings("deprecation") // Player#locale() is not available on every supported version
    private static FileConfiguration messagesFor(CommandSender sender) {
        if (useClientLanguage && sender instanceof Player) {
            return forLocale(((Player) sender).getLocale());
        }
        return serverMessages;
    }

    static FileConfiguration forLocale(String locale) {
        // Matches a Minecraft client locale (e.g. "de_at") to a loaded language file
        if (locale == null || locale.isBlank()) {
            return serverMessages;
        }
        locale = locale.toLowerCase(Locale.ROOT);
        if (languages.containsKey(locale)) {
            return languages.get(locale);
        }

        // No exact match: try the same language from another region
        // Prefers the server language, then e.g. de_de, then any de_*
        String prefix = locale.split("_")[0] + "_";
        if (serverLanguage.startsWith(prefix)) {
            return serverMessages;
        }
        String sameRegion = prefix + locale.split("_")[0];
        if (languages.containsKey(sameRegion)) {
            return languages.get(sameRegion);
        }
        for (Map.Entry<String, FileConfiguration> entry : languages.entrySet()) {
            if (entry.getKey().startsWith(prefix)) {
                return entry.getValue();
            }
        }
        return serverMessages;
    }

    private static String format(FileConfiguration messages, String key, Object... placeholders) {
        // getString(key, def) would skip the defaults chain, so check for null instead
        String message = messages.getString(key);
        if (message == null) {
            return key;
        }
        for (int i = 0; i + 1 < placeholders.length; i += 2) {
            message = message.replace("{" + placeholders[i] + "}", String.valueOf(placeholders[i + 1]));
        }
        return message;
    }

    private static FileConfiguration loadLanguage(JavaPlugin plugin, String name, File file,
            FileConfiguration defaults) {
        FileConfiguration bundled = name.equals(DEFAULT_LANGUAGE) ? defaults : loadBundled(plugin, name);
        if (bundled == null) {
            // Custom language with no bundled copy
            bundled = defaults;
        } else if (bundled != defaults) {
            bundled.setDefaults(defaults);
        }

        FileConfiguration messages = YamlConfiguration.loadConfiguration(file);
        messages.setDefaults(bundled);
        return messages;
    }

    private static FileConfiguration loadBundled(JavaPlugin plugin, String language) {
        InputStream stream = plugin.getResource(getLangPath(language));
        if (stream == null) {
            return null;
        }
        return YamlConfiguration.loadConfiguration(new InputStreamReader(stream, StandardCharsets.UTF_8));
    }

    private static File getLangFile(JavaPlugin plugin, String language) {
        return new File(plugin.getDataFolder(), getLangPath(language));
    }

    private static String getLangPath(String language) {
        return "lang/" + language + ".yml";
    }
}
