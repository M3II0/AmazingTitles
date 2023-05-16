package sk.m3ii0.amazingtitles.code.commands;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class CommandUtils {
	
	public static List<String> buildPlayerParams(String current) {
		List<String> remainingPlayers = new ArrayList<>();
		Bukkit.getOnlinePlayers().forEach(p -> remainingPlayers.add(p.getName()));
		if (current.isEmpty()) {
			remainingPlayers.add("all");
			return remainingPlayers;
		}
		if (!current.endsWith(",")) {
			remainingPlayers.remove("all");
			return remainingPlayers;
		} else {
			String[] names = current.split(",");
			for (String var : names) {
				remainingPlayers.remove(var);
			}
			List<String> newOne = new ArrayList<>();
			for (String p : remainingPlayers) {
				newOne.add(current + p);
			}
			return newOne;
		}
	}
	
	public static List<String> copyPartialMatches(String prefix, Iterable<String> originals) {
		List<String> matches = new ArrayList<>();
		for (String completion : originals)
			if (completion != null && (completion.regionMatches(true, 0, prefix, 0, prefix.length()) || completion.toLowerCase().contains(prefix.toLowerCase())))
				matches.add(completion);
		return matches;
	}
	
	public static List<Player> playersFromOperator(String operator) {
		List<Player> result = new ArrayList<>();
		if (operator.equalsIgnoreCase("all")) return new ArrayList<>(Bukkit.getOnlinePlayers());
		String[] rawPlayers = operator.split(",");
		for (String raw : rawPlayers) {
			Player p = Bukkit.getPlayer(raw);
			if (p != null) result.add(p);
		}
		return result;
	}
	
}
