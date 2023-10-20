package sk.m3ii0.amazingtitles.code.api;

import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import sk.m3ii0.amazingtitles.code.api.builders.AnimationBuilder;
import sk.m3ii0.amazingtitles.code.api.interfaces.AmazingExtension;
import sk.m3ii0.amazingtitles.code.internal.Booter;
import sk.m3ii0.amazingtitles.code.internal.commands.PluginCommand;
import sk.m3ii0.amazingtitles.code.internal.commands.commandreaders.CommandHandler;
import sk.m3ii0.amazingtitles.code.internal.components.AnimationComponent;
import sk.m3ii0.amazingtitles.code.internal.components.ComponentArguments;
import sk.m3ii0.amazingtitles.code.internal.interactivemessages.InteractiveMessageHelper;
import sk.m3ii0.amazingtitles.code.internal.smartbar.SmartBar;
import sk.m3ii0.amazingtitles.code.internal.smartbar.SmartNotification;

import java.util.*;

public class AmazingTitles {
	
	/*
	*
	* Values
	*
	* */
	
	private static final Map<String, AnimationBuilder> animations = new HashMap<>();
	private static final Map<UUID, AnimationComponent> components = new HashMap<>();
	private static final Map<String, AmazingExtension> extensions = new HashMap<>();
	private static final Map<String, List<Listener>> extensionsListeners = new HashMap<>();
	private static final Set<String> loadedExtensions = new HashSet<>();
	
	/*
	*
	* Internal
	*
	* */
	
	public static void reloadPlugin(CommandSender sender) {
		Booter.getBooter().reload(sender);
	}
	
	public static void reloadPlugin() {
		Booter.getBooter().reload(null);
	}
	
	/*
	*
	* Extensions
	*
	* */
	
	public static Set<String> getLoadedExtensionFileNames() {
		return loadedExtensions;
	}
	
	public static void loadExtension(AmazingExtension extension) {
		extensions.put(extension.extension_name(), extension);
		extension.load();
		loadedExtensions.add(extension.getAsFile().getName());
	}
	
	public static void unloadExtension(String extensionName) {
		AmazingExtension extension = extensions.get(extensionName);
		if (extension == null) return;
		loadedExtensions.remove(extension.getAsFile().getName());
		extension.unload();
		unregisterExtensionListeners(extensionName);
	}
	
	public static void registerExtensionListener(AmazingExtension extension, Listener listener) {
		List<Listener> listeners = extensionsListeners.getOrDefault(extension.extension_name(), new ArrayList<>());
		Bukkit.getPluginManager().registerEvents(listener, Booter.getInstance());
		listeners.add(listener);
		extensionsListeners.put(extension.extension_name(), listeners);
	}
	
	public static List<Listener> getExtensionListeners(String extension) {
		return extensionsListeners.getOrDefault(extension, new ArrayList<>());
	}
	
	public static void unregisterExtensionListeners(String extension) {
		List<Listener> listeners = extensionsListeners.get(extension);
		if (listeners == null) return;
		for (Listener var : listeners) {
			HandlerList.unregisterAll(var);
		}
	}
	
	public static void unloadAllExtensions() {
		for (AmazingExtension extension : extensions.values()) {
			unloadExtension(extension.extension_name());
		}
		extensions.clear();
		extensionsListeners.clear();
	}
	
	/*
	*
	* System
	*
	* */
	
	public static void executeAmazingTitlesCommand(String command) {
		executeAmazingTitlesCommand(command, false);
	}
	
	public static void executeAmazingTitlesCommand(String command, boolean handlers) {
		if (command.startsWith("/") && command.length() > 1) {
			command = command.substring(1);
		}
		if (handlers) {
			Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
		} else {
			String[] args = command.split(" ");
			String[] builtArgs;
			if (args.length > 0) {
				builtArgs = new String[args.length-1];
			} else {
				builtArgs = new String[0];
			}
			System.arraycopy(args, 1, builtArgs, 0, builtArgs.length);
			Booter.getPluginCommand().parseCommand(Bukkit.getConsoleSender(), builtArgs);
		}
	}
	
	/*
	*
	* Command handlers
	*
	* */
	
	public static void registerCommandHandler(String argument, CommandHandler commandHandler) {
		PluginCommand.addHandler(argument, commandHandler);
	}
	
	public static void unregisterCommandHandler(String argument) {
		PluginCommand.removeHandler(argument);
	}
	
	/*
	*
	* Animations
	*
	* */
	
	public static void registerCustomAnimation(String name, AnimationBuilder animationBuilder) {
		if (name == null || animationBuilder == null) {
			System.out.println("[AT] Name or AnimationBuilder is null!");
			return;
		}
		name = name.replace(" ", "_").toUpperCase();
		animations.put(name, animationBuilder);
	}
	
	public static void unregisterCustomAnimation(String name) {
		animations.remove(name);
	}
	
	public static AnimationBuilder getCustomAnimation(String name) {
		return animations.get(name);
	}
	
	public static boolean isCustomAnimationExists(String name) {
		return getCustomAnimation(name) != null;
	}
	
	public static boolean isCustomAnimationEnabled(String name) {
		return animations.containsKey(name);
	}
	
	public static Collection<AnimationBuilder> getAnimations() {
		return animations.values();
	}
	
	public static Set<String> getAnimationNames() {
		return animations.keySet();
	}
	
	public static void broadcastAnimation(String animation, ComponentArguments arguments, String[] args) {
		sendAnimation(animation, arguments, args, Bukkit.getOnlinePlayers().toArray(new Player[0]));
	}
	
	public static void sendAnimation(String animation, ComponentArguments arguments, String[] args, Collection<Player> players) {
		AnimationBuilder builder = animations.get(animation);
		if (builder != null) {
			AnimationComponent component = builder.createComponent(arguments, args);
			component.prepare();
			component.addReceivers(players);
			component.run();
		}
	}
	
	public static void sendAnimation(String animation, ComponentArguments arguments, String[] args, Player... players) {
		AnimationBuilder builder = animations.get(animation);
		if (builder != null) {
			AnimationComponent component = builder.createComponent(arguments, args);
			component.prepare();
			component.addReceivers(players);
			component.run();
		}
	}
	
	/*
	*
	* Components
	*
	* */
	
	public static void insertAnimation(Player player, AnimationComponent component) {
		components.put(player.getUniqueId(), component);
	}
	
	public static AnimationComponent getAnimationBy(Player player) {
		return getAnimationBy(player.getUniqueId());
	}
	
	public static AnimationComponent getAnimationBy(UUID uuid) {
		return components.get(uuid);
	}
	
	public static void removeAnimation(Player player) {
		AnimationComponent component = getAnimationBy(player);
		if (component == null) return;
		component.removeReceivers(player);
	}
	
	public static void removeAnimation(UUID uuid) {
		removeAnimation(Bukkit.getPlayer(uuid));
	}
	
	public static boolean hasAnimation(Player player) {
		return hasAnimation(player.getUniqueId());
	}
	
	public static boolean hasAnimation(UUID uuid) {
		return components.get(uuid) != null;
	}
	
	public static void removeAnimationFromCache(UUID uuid) {
		components.remove(uuid);
	}
	
	/*
	*
	* Smart bar
	*
	* */
	
	public static SmartBar getSmartBar(Player player) {
		return Booter.getSmartBarManager().getBar(player);
	}
	
	public static void broadcastNotification(SmartNotification notification) {
		sendNotification(UUID.randomUUID().toString(), notification);
	}
	
	public static void broadcastNotification(String id, SmartNotification notification) {
		sendNotification(id, notification, Bukkit.getOnlinePlayers().toArray(new Player[0]));
	}
	
	public static void sendNotification(String customId, SmartNotification notification, Collection<Player> players) {
		for (Player var : players) {
			SmartBar bar = getSmartBar(var);
			if (bar != null) {
				bar.setNotification(customId, notification);
			}
		}
	}
	
	public static void sendNotification(SmartNotification notification, Collection<Player> players) {
		sendNotification(UUID.randomUUID().toString(), notification, players);
	}
	
	public static void sendNotification(String customId, SmartNotification notification, Player... players) {
		for (Player var : players) {
			SmartBar bar = getSmartBar(var);
			if (bar != null) {
				bar.setNotification(customId, notification);
			}
		}
	}
	
	public static void sendNotification(SmartNotification notification, Player... players) {
		sendNotification(UUID.randomUUID().toString(), notification, players);
	}
	
	public static void hideSmartBar(Player player) {
		SmartBar bar = getSmartBar(player);
		if (bar == null) {
			return;
		}
		bar.setHide(true);
	}
	
	public static void showSmartBar(Player player) {
		SmartBar bar = getSmartBar(player);
		if (bar == null) {
			return;
		}
		bar.setHide(false);
	}
	
	/*
	*
	* Messages
	*
	* */
	
	public static BaseComponent[] getInteractiveMessageFromRaw(String rawMessage) {
		return InteractiveMessageHelper.getMessageFromRaw(rawMessage);
	}
	
	public static void sendInteractiveMessage(String rawMessage, Player... players) {
		BaseComponent[] message = getInteractiveMessageFromRaw(rawMessage);
		for (Player var : players) {
			var.spigot().sendMessage(message);
		}
	}
	
	public static void sendInteractiveMessage(String rawMessage, Collection<Player> players) {
		BaseComponent[] message = getInteractiveMessageFromRaw(rawMessage);
		for (Player var : players) {
			var.spigot().sendMessage(message);
		}
	}
	
	public static void broadcastInteractiveMessage(String rawMessage) {
		BaseComponent[] message = getInteractiveMessageFromRaw(rawMessage);
		Bukkit.spigot().broadcast(message);
	}
	
	/*
	*
	* System (Internal)
	*
	* */
	
	public static void clearCacheInternally() {
		animations.clear();
		for (AnimationComponent component : components.values()) {
			component.end();
		}
	}
	
}
