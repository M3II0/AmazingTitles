package sk.m3ii0.amazingtitles.code.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import sk.m3ii0.amazingtitles.code.commands.dispatcher.TitleDispatcher;
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

		if (args.length < 2) {
			sendHelpMessage(s);
			return true;
		}

		/*
		* 0-receivers 1-animation 2+ params
		* */

		List<Player> receivers = CommandUtils.playersFromOperator(args[0]);
		AnimationTypes type;
		try {
			type = AnimationTypes.valueOf(args[1].toUpperCase());
		} catch (IllegalArgumentException e) {
			sendHelpMessage(s);
			return true;
		}

		int animationArgs = args.length - 2;
		String[] animation = new String[animationArgs];
		int counter = 0;
		for (int i = 2; i < args.length; i++) {
			animation[counter] = args[i];
			++counter;
		}

		if (animationArgs < type.getMinimum()) {
			sendHelpMessage(s);
			return true;
		}

		/*
		* Builder
		* */
		TitleDispatcher.asyncDispatch(s, type, receivers, animation);
		
		return false;
	}

	private void sendHelpMessage(CommandSender s) {
		s.sendMessage("§cAT §7-> §4Invalid command format... (Follow instructions in tab completer)");
	}
	
}
