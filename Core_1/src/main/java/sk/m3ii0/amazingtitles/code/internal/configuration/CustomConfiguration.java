package sk.m3ii0.amazingtitles.code.internal.configuration;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;

public class CustomConfiguration {
	
	/*
	*
	* Values
	*
	* */
	
	private final FileConfiguration options;
	
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
		
	}
	
	/*
	*
	* OPTIONS.YML - Shortcuts
	*
	* */
	
	public class Options {
		
		public double getConfigVersion() {
			return options.getDouble("Config");
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
