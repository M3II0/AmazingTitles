package sk.m3ii0.amazingtitles.code.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import sk.m3ii0.amazingtitles.code.commands.types.AnimationTypes;

import java.util.Collections;
import java.util.List;

public class PluginCommand implements CommandExecutor, TabExecutor {
	
	
	@Override
	public List<String> onTabComplete(CommandSender s, Command cmd, String label, String[] args) {
		
		int current = args.length;
		if (current == 0) return Collections.emptyList();
		String using = args[current-1];
		
		/*
		* Player Selection
		* */
		if (current == 1) {
			return CommandUtils.copyPartialMatches(using, CommandUtils.buildPlayerParams(using));
		}
		
		/*
		* Animation Selection
		* */
		if (current == 2) {
			return CommandUtils.copyPartialMatches(using, AnimationTypes.toIterable());
		}
		
		/*
		* Animation arguments
		* */
		String animation_name = args[1];
		AnimationTypes type;
		try {
			type = AnimationTypes.valueOf(animation_name);
		} catch (IllegalArgumentException e) {
			return List.of("<Error-InvalidAnimationType>");
		}
		return CommandUtils.copyPartialMatches(using, type.getComplete(current-3));
	}
	
	@Override
	public boolean onCommand(CommandSender s, Command cmd, String label, String[] args) {
		
		
		
		return false;
	}
	
}
