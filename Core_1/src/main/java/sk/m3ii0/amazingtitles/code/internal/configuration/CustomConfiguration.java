package sk.m3ii0.amazingtitles.code.internal.configuration;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.util.List;

public class CustomConfiguration {
	
	/*
	*
	* Values
	*
	* */
	
	private final FileConfiguration options;
	
	private final Options shortcutOptions;
	private final SmartBar shortcutSmartBar;
	
	/*
	*
	* Constructor
	*
	* */
	
	public CustomConfiguration(Plugin plugin) {
		
		// Create files
		File optionsFile = createOptionsFile(plugin);
		
		// Load configurations
		this.options = YamlConfiguration.loadConfiguration(optionsFile);
		
		// Load shortcuts
		this.shortcutOptions = new Options();
		this.shortcutSmartBar = new SmartBar();
		
	}
	
	/*
	*
	* OPTIONS.YML - Shortcuts
	*
	* */
	
	public Options getShortcutOptions() {
		return shortcutOptions;
	}
	
	public SmartBar getShortcutSmartBar() {
		return shortcutSmartBar;
	}
	
	public class Options {
		
		public double getConfigVersion() {
			return options.getDouble("Config");
		}
		
		public boolean getUpdateNotifier() {
			int result = options.getInt("UpdateNotifier", 1);
			return result != 0;
		}
		
	}
	
	public class SmartBar {
		
		public boolean getNotificationsPermission() {
			return options.getBoolean("SmartBar.Notifications");
		}
		
		public boolean getStaticBarPermission() {
			return options.getBoolean("SmartBar.StaticBar.Enabled");
		}
		
		public boolean getStaticBarNotificationsPermission() {
			return options.getBoolean("SmartBar.StaticBar.Notifications");
		}
		
		public String getStaticBarAnimation() {
			return options.getString("SmartBar.StaticBar.Animation");
		}
		
		public int getStaticBarFps() {
			return options.getInt("SmartBar.StaticBar.Fps");
		}
		
		public List<String> getStaticBarArguments() {
			return options.getStringList("SmartBar.StaticBar.Arguments");
		}
		
		public String getStaticBarText() {
			return options.getString("SmartBar.StaticBar.Text");
		}
		
	}
	
	/*
	*
	* Generators
	*
	* */
	
	private File createOptionsFile(Plugin plugin) {
		plugin.saveResource("options.yml", false);
		return new File(plugin.getDataFolder(), "options.yml");
	}
	
}
