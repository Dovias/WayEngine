package lt.wayout.minecraft.plugin.wayengine.util;

import org.bukkit.plugin.java.JavaPlugin;

import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.Map;

public class LocaleStorage {
	private Map<String, String> locale;
	private Map<String, String> defaultLocale;
	private JavaPlugin plugin;
	private Path path;


	public LocaleStorage(Path path, Map<String, String> defaultLocale, JavaPlugin plugin) {
		this.setPath(path);
		this.setPlugin(plugin);
		this.setDefaultLocale(defaultLocale);
		this.setLocale(LocaleUtils.loadLocale(path));
		if (getLocale() == null) {
			this.setLocale(defaultLocale);
		}
	}

	public void registerDefaultValue(String key, String value) {
		this.getDefaultLocale().put(key, value);
		this.setLocale(LocaleUtils.insertDefaults(this.getLocale(), this.getDefaultLocale()));
	}

	public Map<String, String> getLocale() {
		return locale;
	}

	public void setLocale(Map<String, String> locale) {
		if (locale == null) {
			this.locale = new LinkedHashMap<>(this.getDefaultLocale());
			return;
		}
		this.locale = LocaleUtils.insertDefaults(locale, this.getDefaultLocale());
	}

	public Map<String, String> getDefaultLocale() {
		return defaultLocale;
	}

	public void setDefaultLocale(Map<String, String> defaultLocale) {
		this.defaultLocale = defaultLocale;
	}

	public JavaPlugin getPlugin() {
		return plugin;
	}

	public void setPlugin(JavaPlugin plugin) {
		this.plugin = plugin;
	}

	public Path getPath() {
		return path;
	}

	public void setPath(Path path) {
		this.path = path;
	}
}
