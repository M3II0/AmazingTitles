package sk.m3ii0.amazingtitles.code.commands;

import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import sk.m3ii0.amazingtitles.code.AmazingTitles;
import sk.m3ii0.amazingtitles.code.colors.ColorTranslator;
import sk.m3ii0.amazingtitles.code.colors.ColorUtils;
import sk.m3ii0.amazingtitles.code.commands.dispatcher.TitleDispatcher;
import sk.m3ii0.amazingtitles.code.commands.types.ActionType;
import sk.m3ii0.amazingtitles.code.commands.types.AnimationTypes;
import sk.m3ii0.amazingtitles.code.notifications.BarNotification;
import sk.m3ii0.amazingtitles.code.stats.Metrics;

import java.util.Collections;
import java.util.List;

public class PluginCommand implements CommandExecutor, TabExecutor {
	
	
	@Override
	public List<String> onTabComplete(CommandSender s, Command cmd, String label, String[] args) {
		
		int current = args.length;
		if (current == 0) return Collections.emptyList();
		String using = args[current-1];
		
		/*
		 * Action Selection
		 * */
		if (current == 1) {
			return CommandUtils.copyPartialMatches(using, ActionType.toIterable());
		}
		
		/*
		* Player Selection
		* */
		if (current == 2) {
			return CommandUtils.copyPartialMatches(using, CommandUtils.buildPlayerParams(using));
		}
		
		/*
		* Message
		* */
		if (args[0].equalsIgnoreCase("MESSAGE"))  {
			return List.of("<Visit wiki how to build message>");
		}

		/*
		* Notification
		* */
		if (args[0].equalsIgnoreCase("NOTIFICATION")) {
			if (current == 3) {
				return List.of("<NotificationSymbol>");
			} else if (current == 4) {
				return List.of("<Number(Duration-InSeconds)>");
			} else return List.of("<NotificationMessage>");
		}
		
		/*
		* Animation Selection
		* */
		if (current == 3) {
			return CommandUtils.copyPartialMatches(using, AnimationTypes.toIterable());
		}
		
		/*
		* Animation arguments
		* */
		String animation_name = args[2];
		AnimationTypes type;
		try {
			type = AnimationTypes.valueOf(animation_name);
		} catch (IllegalArgumentException e) {
			return List.of("<Error-InvalidAnimationType>");
		}
		return CommandUtils.copyPartialMatches(using, type.getComplete(current-4));
	}
	
	@Override
	public boolean onCommand(CommandSender s, Command cmd, String label, String[] args) {

		if (args.length < 2) {
			sendHelpMessage(s);
			return true;
		}

		/*
		* 0-receivers 1-type 2-animation 3+ params
		* */

		List<Player> receivers = CommandUtils.playersFromOperator(args[1]);
		AnimationTypes animationTypes;
		ActionType actionType;
		try {
			actionType = ActionType.valueOf(args[0].toUpperCase());
			if (actionType == ActionType.MESSAGE) {
				String text = "";
				for (int i = 2; i < args.length; i++) {
					text += args[i] + " ";
				}
				text = text.replaceAll(" $", "");
				BaseComponent[] message = TitleDispatcher.getMessageFromRaw(text);
				for (Player p : receivers) {
					p.spigot().sendMessage(message);
				}
				AmazingTitles.getMetrics().addCustomChart(new Metrics.SingleLineChart("used_commands", () -> 1));
				return true;
			}
			if (actionType == ActionType.NOTIFICATION) {
				String symbol = ColorTranslator.parse(args[2]);
				int duration = Integer.parseInt(args[3]);
				String text = "";
				for (int i = 4; i < args.length; i++) {
					text += args[i] + " ";
				}
				text = text.replaceAll(" $", "");
				BarNotification notification = BarNotification.create(symbol, text, duration);
				AmazingTitles.setNotificationFor(notification, receivers);
				AmazingTitles.getMetrics().addCustomChart(new Metrics.SingleLineChart("used_commands", () -> 1));
				return true;
			}
			animationTypes = AnimationTypes.valueOf(args[2].toUpperCase());
		} catch (IllegalArgumentException | ArrayIndexOutOfBoundsException e) {
			sendHelpMessage(s);
			return true;
		}

		int animationArgs = args.length - 3;
		String[] animation = new String[animationArgs];
		int counter = 0;
		for (int i = 3; i < args.length; i++) {
			animation[counter] = args[i];
			++counter;
		}

		if (animationArgs < animationTypes.getMinimum()) {
			sendHelpMessage(s);
			return true;
		}

		/*
		* Builder
		* */
		TitleDispatcher.asyncDispatch(s, actionType, animationTypes, receivers, animation);
		AmazingTitles.getMetrics().addCustomChart(new Metrics.SingleLineChart("used_commands", () -> 1));
		return false;
	}

	private void sendHelpMessage(CommandSender s) {
		s.sendMessage("§cAT §7-> §4Invalid command format... (Follow instructions in tab completer)");
	}
	
}
