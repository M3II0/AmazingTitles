package sk.m3ii0.amazingtitles.code.internal.commands.commandreaders.readers;

import org.bukkit.boss.BarColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import sk.m3ii0.amazingtitles.code.api.enums.DisplayType;
import sk.m3ii0.amazingtitles.code.internal.components.ComponentArguments;

import java.util.*;

public class ArgsHelper implements Listener {
	
	/*
	*
	* Values
	*
	* */
	
	private final static Map<String, Player> players = new HashMap<>();
	
	/*
	*
	* API
	*
	* */
	
	public static List<Player> readPlayers(String argument) {
		List<Player> result = new ArrayList<>();
		if (argument.startsWith("-p:")) {
			argument = argument.replaceFirst("-p:", "");
			for (Player player : players.values()) {
				if (player != null && player.hasPermission(argument)) {
					result.add(player);
				}
			}
			return result;
		}
		for (String playerName : argument.split(",")) {
			Player player = players.get(playerName);
			if (player != null) {
				result.add(player);
			}
		}
		return result;
	}
	
	public static List<String> preparePlayers(String argument) {
		List<String> results = new ArrayList<>();
		Set<String> contains = new HashSet<>(Arrays.asList(argument.split(",")));
		if (argument.endsWith(",")) {
			for (String name : players.keySet()) {
				if (contains.contains(name)) continue;
				results.add(argument + name);
			}
		}
		return results;
	}
	
	public static ComponentArguments readArguments(String argument) {
		BarColor componentColor = null;
		int duration = -99;
		int fps = -99;
		DisplayType type = null;
		for (String var : argument.split(",")) {
			if (var.startsWith("cc:")) {
				var = var.replaceFirst("cc:", "");
				var = var.toUpperCase();
				componentColor = generateBarColor(var);
				continue;
			}
			if (var.startsWith("p:")) {
				var = var.replaceFirst("p:", "");
				var = var.toUpperCase();
				type = generateDisplayType(var);
				continue;
			}
			if (var.startsWith("d:")) {
				var = var.replaceFirst("d:", "");
				duration = generateNumber(var);
				continue;
			}
			if (var.startsWith("fps:")) {
				var = var.replaceFirst("fps:", "");
				fps = generateNumber(var);
			}
		}
		return ComponentArguments.create(null, null, componentColor, duration, fps, type);
	}
	
	private static int generateNumber(String number) {
		try {
			return Integer.parseInt(number);
		} catch (Exception e) {
			return 1;
		}
	}
	
	private static BarColor generateBarColor(String color) {
		switch (color) {
			case "PINK": {
				return BarColor.PINK;
			}
			case "BLUE": {
				return BarColor.BLUE;
			}
			case "RED": {
				return BarColor.RED;
			}
			case "GREEN": {
				return BarColor.GREEN;
			}
			case "YELLOW": {
				return BarColor.YELLOW;
			}
			case "PURPLE": {
				return BarColor.PURPLE;
			}
			default: {
				return BarColor.WHITE;
			}
		}
	}
	
	private static DisplayType generateDisplayType(String type) {
		switch (type) {
			case "SUBTITLE": {
				return DisplayType.SUBTITLE;
			}
			case "BOSSBAR": {
				return DisplayType.BOSS_BAR;
			}
			case "ACTIONBAR": {
				return DisplayType.ACTION_BAR;
			}
			default: {
				return DisplayType.TITLE;
			}
		}
	}
	
	/*
	*
	* Listeners
	*
	* */
	
	@EventHandler
	public void join(PlayerJoinEvent e) {
		players.put(e.getPlayer().getName(), e.getPlayer());
	}
	
	@EventHandler
	public void quit(PlayerQuitEvent e) {
		players.remove(e.getPlayer().getName());
	}
	
}
