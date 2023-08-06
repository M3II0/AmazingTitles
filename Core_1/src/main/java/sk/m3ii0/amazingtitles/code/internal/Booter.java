package sk.m3ii0.amazingtitles.code.internal;

import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import sk.m3ii0.amazingtitles.code.internal.configuration.CustomConfiguration;
import sk.m3ii0.amazingtitles.code.internal.loaders.PluginLoader;
import sk.m3ii0.amazingtitles.code.internal.loaders.PluginMode;
import sk.m3ii0.amazingtitles.code.internal.spi.NmsBuilder;
import sk.m3ii0.amazingtitles.code.internal.spi.NmsProvider;
import sk.m3ii0.amazingtitles.code.internal.utils.ColorTranslator;

import java.text.DecimalFormat;

public class Booter extends JavaPlugin {
	
	/*
	*
	* Values
	*
	* */
	
	private static CustomConfiguration customConfiguration;
	private static NmsProvider nmsProvider;
	private static PluginMode pluginMode;
	private static Plugin instance;
	
	/*
	*
	* Bukkit API
	*
	* */
	
	@Override
	public void onLoad() {
		
		// Load plugin
		instance = this;
		
		// Load custom configuration
		customConfiguration = new CustomConfiguration(this);
		
	}
	
	@Override
	public void onEnable() {
		
		// Load plugin & record took ms
		String took = getTookMs(() -> {
			
			try {
				
				// Try to load NmsProvider
				NmsBuilder builder = PluginLoader.loadBuilder(getClassLoader());
				if (builder == null) {
					pluginMode = PluginMode.UNSUPPORTED_VERSION;
					return;
				}
				nmsProvider = builder.build();
				
				// Look for 1.16+ methods
				if (!ColorTranslator.isHexSupport()) {
					pluginMode = PluginMode.WITHOUT_RGB;
				}
				
				
				
			} catch (Exception e) {
				e.printStackTrace();
				pluginMode = PluginMode.UNEXPECTED_ERROR;
			}
			
		});
		
		// Handle plugin mode
		if (pluginMode == null) {
			pluginMode = PluginMode.UNEXPECTED_ERROR;
		}
		
		// Send report about enabling
		sendEnableReport(took, pluginMode);
	
	}
	
	@Override
	public void onDisable() {
	
	}
	
	/*
	*
	* API
	*
	* */
	
	public static Plugin getInstance() {
		return instance;
	}
	
	public static CustomConfiguration getCustomConfiguration() {
		return customConfiguration;
	}
	
	public static NmsProvider getNmsProvider() {
		return nmsProvider;
	}
	
	public static PluginMode getPluginMode() {
		return pluginMode;
	}
	
	/*
	*
	* Class functions
	*
	* */
	
	private void sendEnableReport(String took, PluginMode mode) {
	
	}
	
	private String getTookMs(Runnable action) {
		long nanos = -System.nanoTime();
		action.run();
		nanos += System.nanoTime();
		return new DecimalFormat("#.###").format(nanos/1e+6);
	}
	
}
