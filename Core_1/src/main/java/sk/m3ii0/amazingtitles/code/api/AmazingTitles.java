package sk.m3ii0.amazingtitles.code.api;

import org.bukkit.entity.Player;
import sk.m3ii0.amazingtitles.code.api.builders.AnimationBuilder;
import sk.m3ii0.amazingtitles.code.internal.Booter;
import sk.m3ii0.amazingtitles.code.internal.smartbar.SmartBar;
import sk.m3ii0.amazingtitles.code.internal.smartbar.SmartNotification;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

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
	
	/*
	*
	* Smart bar
	*
	* */
	
	public SmartBar getSmartBar(Player player) {
		return Booter.getSmartBarManager().getBar(player);
	}
	
	public void sendNotification(String customId, SmartNotification notification, Player... players) {
		for (Player var : players) {
			SmartBar bar = getSmartBar(var);
			if (bar != null) {
				bar.setNotification(customId, notification);
			}
		}
	}
	
	public void sendNotification(SmartNotification notification, Player... players) {
		sendNotification(new SmartNotification[]{notification}, players);
	}
	
	public void sendNotification(SmartNotification[] notifications, Player... players) {
		for (Player var : players) {
			SmartBar bar = getSmartBar(var);
			if (bar != null) {
				for (SmartNotification notification : notifications) {
					bar.setNotification(UUID.randomUUID().toString(), notification);
				}
			}
		}
	}
	
	public void hideSmartBar(Player player) {
		SmartBar bar = getSmartBar(player);
		if (bar == null) {
			return;
		}
		bar.setHide(true);
	}
	
	public void showSmartBar(Player player) {
		SmartBar bar = getSmartBar(player);
		if (bar == null) {
			return;
		}
		bar.setHide(false);
	}
	
}
