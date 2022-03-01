package lt.wayout.minecraft.plugin.wayengine.util;

import com.google.gson.reflect.TypeToken;
import me.clip.placeholderapi.PlaceholderAPI;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import java.nio.file.Path;
import java.util.Map;

public class LocaleUtils {
	private LocaleUtils() {
	}

	public static Component getParsedComponent(Object player, String key) {
		String text = "";
		if (player instanceof OfflinePlayer) {
			if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
				text = PlaceholderAPI.setPlaceholders((OfflinePlayer) player, text);
			}
		}
		return MiniMessage.miniMessage().deserialize(text);
	}

	@SuppressWarnings("unchecked")
	public static Map<String, String> loadLocale(Path path) {
		return (Map<String, String>) JsonUtils.loadObject(path, new TypeToken<Map<String, String>>() {
		}.getType());
	}

	public static boolean saveLocale(Path path, Map<String, String> locale) {
		return JsonUtils.saveObject(path, locale);
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
