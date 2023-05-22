package sk.m3ii0.amazingtitles.code;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import sk.m3ii0.amazingtitles.code.announcement.DiscordAnnouncement;
import sk.m3ii0.amazingtitles.code.commands.PluginCommand;
import sk.m3ii0.amazingtitles.code.spi.NmsBuilder;
import sk.m3ii0.amazingtitles.code.spi.NmsProvider;

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
		DiscordAnnouncement.runWithMessage(this,
		 "https://m3ii0.gitbook.io/amazingtitles/plugin-support",
		 "§bAT §7-> §fJoin discord server and be able to report bug, or get notification about new plugin update! §8(Don't worry, just admin can see this message)",
		 "amazingtitles.admin"
		);
	}
	
	@Override
	public void onDisable() {
		DiscordAnnouncement.close();
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