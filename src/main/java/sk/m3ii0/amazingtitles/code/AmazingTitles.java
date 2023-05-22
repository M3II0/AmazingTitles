package sk.m3ii0.amazingtitles.code;

import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import sk.m3ii0.amazingtitles.code.commands.PluginCommand;
import sk.m3ii0.amazingtitles.code.nms.NmsProvider;
import sk.m3ii0.amazingtitles.code.nms.R16.R1_16_R1;
import sk.m3ii0.amazingtitles.code.nms.R16.R1_16_R2;
import sk.m3ii0.amazingtitles.code.nms.R16.R1_16_R3;
import sk.m3ii0.amazingtitles.code.nms.R17.R_1_17_R1;
import sk.m3ii0.amazingtitles.code.nms.R18.R1_18_R1;
import sk.m3ii0.amazingtitles.code.nms.R18.R1_18_R2;
import sk.m3ii0.amazingtitles.code.nms.R19.R1_19_R1;
import sk.m3ii0.amazingtitles.code.nms.R19.R1_19_R2;
import sk.m3ii0.amazingtitles.code.nms.R19.R1_19_R3;

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
		switch (version) {
			
			case "v1_19_R3" -> {
				return new R1_19_R3();
			}
			case "v1_19_R2" -> {
				return new R1_19_R2();
			}
			case "v1_19_R1" -> {
				return new R1_19_R1();
			}
			
			case "v1_18_R2" -> {
				return new R1_18_R2();
			}
			case "v1_18_R1" -> {
				return new R1_18_R1();
			}
			
			case "v1_17_R1" -> {
				return new R_1_17_R1();
			}
			
			case "v1_16_R3" -> {
				return new R1_16_R3();
			}
			case "v1_16_R2" -> {
				return new R1_16_R2();
			}
			case "v1_16_R1" -> {
				return new R1_16_R1();
			}
		}
		return null;
	}
	
}