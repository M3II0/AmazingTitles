package sk.m3ii0.amazingtitles.code.internal;

import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import sk.m3ii0.amazingtitles.code.api.AmazingTitles;
import sk.m3ii0.amazingtitles.code.api.builders.AnimationBuilder;
import sk.m3ii0.amazingtitles.code.api.enums.DisplayType;
import sk.m3ii0.amazingtitles.code.internal.commands.PluginCommand;
import sk.m3ii0.amazingtitles.code.internal.components.ComponentArguments;
import sk.m3ii0.amazingtitles.code.internal.configuration.CustomConfiguration;
import sk.m3ii0.amazingtitles.code.internal.loaders.PluginLoader;
import sk.m3ii0.amazingtitles.code.internal.loaders.PluginMode;
import sk.m3ii0.amazingtitles.code.internal.smartbar.SmartBar;
import sk.m3ii0.amazingtitles.code.internal.smartbar.SmartBarManager;
import sk.m3ii0.amazingtitles.code.internal.spi.NmsBuilder;
import sk.m3ii0.amazingtitles.code.internal.spi.NmsProvider;
import sk.m3ii0.amazingtitles.code.internal.utils.ColorTranslator;

import java.text.DecimalFormat;
import java.util.List;

public class Booter extends JavaPlugin implements Listener {
	
	/*
	*
	* Values
	*
	* */
	
	private static CustomConfiguration customConfiguration;
	private static NmsProvider nmsProvider;
	private static PluginMode pluginMode;
	private static Plugin instance;
	private static SmartBarManager smartBarManager;
	private static PluginCommand pluginCommand;
	
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
				
				// Load default animations
				PluginLoader.loadDefaultAnimations();
				
				// Load smart bar manager
				smartBarManager = new SmartBarManager(this);
				
				// Register listeners
				Bukkit.getPluginManager().registerEvents(this, this);
				
				// Register command
				org.bukkit.command.PluginCommand command = getCommand("amazingtitles");
				pluginCommand = new PluginCommand(this);
				command.setExecutor(pluginCommand);
				command.setTabCompleter(pluginCommand);
				
				// Load static-bar
				String staticBarText = getCustomConfiguration().getShortcutSmartBar().getStaticBarText();
				String staticBarAnimation = getCustomConfiguration().getShortcutSmartBar().getStaticBarAnimation();
				String[] staticBarArguments = getCustomConfiguration().getShortcutSmartBar().getStaticBarArguments().toArray(new String[0]);
				AnimationBuilder animationBuilder = AmazingTitles.getCustomAnimation(staticBarAnimation);
				if (animationBuilder != null && getCustomConfiguration().getShortcutSmartBar().getStaticBarPermission()) {
					List<String> frames = animationBuilder.getFramesBuilder().buildFrames(ComponentArguments.create(staticBarText, "", BarColor.WHITE, 0, 0, DisplayType.ACTION_BAR), staticBarArguments);
					SmartBar.setStaticAnimationContent(frames);
				}
				
				// Run smart-bar task
				Bukkit.getScheduler().runTaskTimerAsynchronously(this, () -> {
					for (SmartBar bar : getSmartBarManager().getBars().values()) {
						if (bar != null) {
							bar.prepareAndTryToSend();
						}
					}
				}, 0, 1);
				
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
	
		// Unregister listeners
		HandlerList.unregisterAll((Plugin) this);
		
	}
	
	/*
	*
	* Listeners
	*
	* */
	
	@EventHandler
	public void join(PlayerJoinEvent e) {
		boolean notifications = getCustomConfiguration().getShortcutSmartBar().getNotificationsPermission();
		boolean staticBar = getCustomConfiguration().getShortcutSmartBar().getStaticBarPermission();
		boolean staticBarNotifications = getCustomConfiguration().getShortcutSmartBar().getStaticBarNotificationsPermission();
		Player player = e.getPlayer();
		SmartBar bar = new SmartBar(player, notifications, staticBar, staticBarNotifications);
		getSmartBarManager().insertBar(player, bar);
	}
	
	@EventHandler
	public void quit(PlayerQuitEvent e) {
		Player player = e.getPlayer();
		getSmartBarManager().removeBar(player);
	}
	
	/*
	*
	* API
	*
	* */
	
	public static Plugin getInstance() {
		return instance;
	}
	
	public static PluginCommand getPluginCommand() {
		return pluginCommand;
	}
	
	public static SmartBarManager getSmartBarManager() {
		return smartBarManager;
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
