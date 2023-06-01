package sk.m3ii0.amazingtitles.code;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import sk.m3ii0.amazingtitles.code.announcement.UpdateChecker;
import sk.m3ii0.amazingtitles.code.commands.PluginCommand;
import sk.m3ii0.amazingtitles.code.spi.NmsBuilder;
import sk.m3ii0.amazingtitles.code.spi.NmsProvider;
import sk.m3ii0.amazingtitles.code.stats.Metrics;

import java.util.ServiceLoader;

public class AmazingTitles extends JavaPlugin {
	
	/*
	*
	* Values
	*
	* */
	
	private static Plugin instance;
	private static TitleManager titleManager;
	private static NmsProvider provider;
	private static Metrics metrics;
	
	/*
	*
	* Bukkit - API
	*
	* */
	
	@Override
	public void onLoad() {
		instance = this;
	}
	
	@Override
	public void onEnable() {
		titleManager = new TitleManager();
		metrics = new Metrics(this, 18588);
		getCommand("amazingtitles").setExecutor(new PluginCommand());
		getCommand("amazingtitles").setTabCompleter(new PluginCommand());
		provider = getFromVersion(getVersion());
		if (provider == null) {
			Bukkit.getConsoleSender().sendMessage("§c[Error] AmazingTitles - You're using unsupported version...");
			Bukkit.getConsoleSender().sendMessage("§c[Error] AmazingTitles - Version must be 1.16 or higher");
			Bukkit.getConsoleSender().sendMessage("§c[Error] AmazingTitles - If you are using correct version, contact plugin author!");
			Bukkit.getConsoleSender().sendMessage("§c[Error] AmazingTitles - Disabling plugin...");
			Bukkit.getPluginManager().disablePlugin(this);
		}
		new UpdateChecker(this, "AmazingTitles", "https://www.spigotmc.org/resources/109916/", "amazingtitles.admin", "1.7", 109916);
	}
	
	@Override
	public void onDisable() {
		HandlerList.unregisterAll(this);
		metrics.shutdown();
	}
	
	/*
	*
	* AmazingTitles - API
	*
	* */
	
	public static Plugin getInstance() {
		return instance;
	}
	
	public static TitleManager getTitleManager() {
		return titleManager;
	}
	
	public static NmsProvider getProvider() {
		return provider;
	}
	
	/*
	*
	* Private API
	*
	* */
	
	public static Metrics getMetrics() {
		return metrics;
	}
	private String getVersion() {
		final String packageName = getServer().getClass().getPackage().getName();
		return packageName.substring(packageName.lastIndexOf('.') + 1);
	}
	private NmsProvider getFromVersion(String version) {
		for (NmsBuilder builder : ServiceLoader.load(NmsBuilder.class, getClassLoader())) {
			if (builder.checked(version)) {
				return builder.build();
			}
		}
		return null;
	}
	
}