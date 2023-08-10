package sk.m3ii0.amazingtitles.code.api;

import org.bukkit.entity.Player;
import sk.m3ii0.amazingtitles.code.api.builders.AnimationBuilder;
import sk.m3ii0.amazingtitles.code.internal.Booter;
import sk.m3ii0.amazingtitles.code.internal.components.AnimationComponent;
import sk.m3ii0.amazingtitles.code.internal.components.ComponentArguments;
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

	/*
	*
	* Animations
	*
	* */
	
	public static void registerCustomAnimation(String name, AnimationBuilder animationBuilder) {
		if (name == null || animationBuilder == null) return;
		name = name.replace(" ", "_").toUpperCase();
		animations.put(name, animationBuilder);
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
	
	public static void sendAnimation(String animation, ComponentArguments arguments, Collection<Player> players) {
		AnimationBuilder builder = animations.get(animation);
		if (builder != null) {
			AnimationComponent component = builder.createComponent(arguments);
			component.prepare();
			component.addReceivers(players);
			component.run();
		}
	}
	
	public static void sendAnimation(String animation, ComponentArguments arguments, Player... players) {
		AnimationBuilder builder = animations.get(animation);
		if (builder != null) {
			AnimationComponent component = builder.createComponent(arguments);
			component.prepare();
			component.addReceivers(players);
			component.run();
		}
	}
	
	/*
	*
	* Smart bar
	*
	* */
	
	public static SmartBar getSmartBar(Player player) {
		return Booter.getSmartBarManager().getBar(player);
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
	
}
