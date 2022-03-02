package lt.wayout.minecraft.plugin.wayengine.util;

import com.google.gson.reflect.TypeToken;
import me.clip.placeholderapi.PlaceholderAPI;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.nio.file.Path;
import java.util.Map;

public class LocaleUtils {
	private LocaleUtils() {
	}

	public static void sendMessage(Audience audience, Enum<?> key, LocaleStorage localeStorage) {
		sendMessage(audience, key.name(), localeStorage);
	}

	public static void sendMessage(Audience audience, String key, LocaleStorage localeStorage) {
		Component component = LocaleUtils.getParsedComponent(audience, String.valueOf(key), localeStorage);
		audience.sendMessage(component);
	}

	/*
	 * This version of getParsedComponent uses MiniMessage, which is not yet on a release of Paper. Will uncomment once they update.
	public static Component getParsedComponent(Audience audience, String key, LocaleStorage localeStorage) {
		String text = LocaleUtils.getRawText(key, localeStorage);
		if (audience instanceof Player player) {
			if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
				text = PlaceholderAPI.setPlaceholders(player, text);
			}
		}
		return MiniMessage.miniMessage().deserialize(text);
	}
	*/

	// will deprecate once Paper updates its Adventure
	public static Component getParsedComponent(Audience audience, String key, LocaleStorage localeStorage) {
		String text = LocaleUtils.getRawText(key, localeStorage);
		if (audience instanceof Player player) {
			if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
				text = PlaceholderAPI.setPlaceholders(player, text);
			}
		}
		return Component.text(text.replace("&", "§"));
	}

	private static String getRawText(String key, LocaleStorage localeStorage) {
		return localeStorage.getLocale().getOrDefault(key, null);
	}

	public static Map<String, String> loadLocale(Path path) {
		return JsonUtils.loadObject(path, new TypeToken<Map<String, String>>() {
		}.getType());
	}

	public static boolean saveLocale(Path path, Map<String, String> locale) {
		return JsonUtils.saveObject(path, locale);
	}

	public static LocaleStorage getStorage(Path path, Map<String, String> defaultLocale, JavaPlugin plugin) {
		return new LocaleStorage(path, defaultLocale, plugin);
	}

	public static Map<String, String> insertDefaults(Map<String, String> map, Map<String, String> defaults) {
		defaults.forEach((K, V) -> {
			if (!map.containsKey(K)) {
				map.put(K, V);
			}
		});
		return map;
	}

	public static Map<String, String> removeRedundant(Map<String, String> map, Map<String, String> defaults) {
		map.forEach((K, V) -> {
			if (!defaults.containsKey(K)) {
				map.remove(K);
			}
		});
		return map;
	}
}
