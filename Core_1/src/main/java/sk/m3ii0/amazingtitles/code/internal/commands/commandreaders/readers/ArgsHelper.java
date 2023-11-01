package sk.m3ii0.amazingtitles.code.internal.commands.commandreaders.readers;

import org.bukkit.Bukkit;
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
		if (argument.equalsIgnoreCase("all")) {
			result.addAll(Bukkit.getOnlinePlayers());
			return result;
		}
		for (String var : argument.split(",")) {
			if (var.startsWith("-p:")) {
				var = var.replaceFirst("-p:", "");
				for (Player player : players.values()) {
					if (player != null && player.hasPermission(var)) {
						result.add(player);
					}
				}
				continue;
			}
			if (var.startsWith("-w:")) {
				var = var.replaceFirst("-w:", "");
				for (Player player : players.values()) {
					if (player != null && player.getWorld().getName().equalsIgnoreCase(var)) {
						result.add(player);
					}
				}
				continue;
			}
			Player player = players.get(var);
			if (player != null) {
				result.add(player);
			}
		}
		return result;
	}
	
	public static List<String> preparePlayers(String argument) {
		List<String> results = new ArrayList<>();
		Set<String> contains = new HashSet<>(Arrays.asList(argument.split(",")));
		results.add("all");
		results.add("-p:your.permission");
		results.add("-w:worldName");
		for (Player var : Bukkit.getOnlinePlayers()) {
			String name = var.getName();
			if (contains.contains(name)) continue;
			results.add(var.getName());
		}
		if (argument.endsWith(",")) {
			for (String name : players.keySet()) {
				if (contains.contains(name)) continue;
				results.add(argument + name);
			}
		}
		return results;
	}
	
	public static List<String> prepareArguments(String argument) {
		List<String> list = new ArrayList<String>() {{
			add("cc:PINK");
			add("cc:BLUE");
			add("cc:RED");
			add("cc:GREEN");
			add("cc:YELLOW");
			add("cc:PURPLE");
			add("cc:WHITE");
			add("p:TITLE");
			add("p:SUBTITLE");
			add("p:BOSSBAR");
			add("p:ACTIONBAR");
			add("d:(DurationInSeconds)");
			add("fps:(1-20)");
		}};
		Set<String> contains = new HashSet<>(Arrays.asList(argument.split(",")));
		for (String var : contains) {
			if (var.startsWith("cc:")) list.removeIf(line -> line.startsWith("cc:"));
			if (var.startsWith("p:")) list.removeIf(line -> line.startsWith("p:"));
			if (var.startsWith("d:")) list.removeIf(line -> line.startsWith("d:"));
			if (var.startsWith("fps:")) list.removeIf(line -> line.startsWith("fps:"));
		}
		if (argument.endsWith(",")) {
			List<String> news = new ArrayList<>();
			for (String var : list) {
				news.add(argument + var);
			}
			return news;
		}
		return list;
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
