package sk.m3ii0.amazingtitles.code;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import sk.m3ii0.amazingtitles.code.commands.PluginCommand;
import sk.m3ii0.amazingtitles.code.testing.TestListener;

public class AmazingTitles extends JavaPlugin {
	
	/*
	*
	* Values
	*
	* */
	
	private static Plugin instance;
	private static TitleManager titleManager;
	
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
		Bukkit.getPluginManager().registerEvents(new TestListener(), this);
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
	
}