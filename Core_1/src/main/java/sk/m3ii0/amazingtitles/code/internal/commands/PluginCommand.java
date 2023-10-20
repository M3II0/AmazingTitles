package sk.m3ii0.amazingtitles.code.internal.commands;

import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.plugin.Plugin;
import sk.m3ii0.amazingtitles.code.internal.commands.commandreaders.CommandHandler;
import sk.m3ii0.amazingtitles.code.internal.commands.commandreaders.subs.CHAnimations;
import sk.m3ii0.amazingtitles.code.internal.commands.commandreaders.subs.CHMessages;
import sk.m3ii0.amazingtitles.code.internal.commands.commandreaders.subs.CHNotifications;
import sk.m3ii0.amazingtitles.code.internal.commands.commandreaders.subs.CHPluginActions;
import sk.m3ii0.amazingtitles.code.internal.utils.CommandUtils;
import sk.m3ii0.amazingtitles.code.internal.utils.MessageUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PluginCommand implements CommandExecutor, TabExecutor {
	
	/*
	*
	* Values
	*
	* */
	
	private static final Map<String, CommandHandler> handlers = new HashMap<>();
	
	/*
	*
	* Constructor
	*
	* */
	
	public PluginCommand(Plugin plugin) {
		handlers.put("sendAnimation", new CHAnimations());
		handlers.put("sendNotification", new CHNotifications());
		handlers.put("sendMessage", new CHMessages());
		handlers.put("pluginActions", new CHPluginActions());
	}
	
	/*
	*
	* API
	*
	* */
	
	@Override
	public List<String> onTabComplete(CommandSender s, Command cmd, String label, String[] args) {
		return parseComplete(s, cmd, label, args);
	}
	
	@Override
	public boolean onCommand(CommandSender s, Command cmd, String label, String[] args) {
		return parseCommand(s, args);
	}
	
	/*
	*
	* Builders
	*
	* */
	
	private List<String> parseComplete(CommandSender s, Command cmd, String label, String[] args) {
		
		// Values
		List<String> value = new ArrayList<>();
		
		/*
		*
		* 0 - Handler list
		*
		* */
		if (args.length == 1) {
			String begin = args[0];
			for (Map.Entry<String, CommandHandler> entry : handlers.entrySet()) {
				CommandHandler handler = entry.getValue();
				String argument = entry.getKey();
				if (s.hasPermission(handler.permission())) {
					value.add(argument);
				}
			}
			return CommandUtils.copyAllStartingWith(value, begin);
		}
		
		/*
		*
		* 1 - Handler return
		*
		* */
		if (args.length > 1) {
			String handlerName = args[0];
			CommandHandler handler = handlers.get(handlerName);
			if (handler != null) {
				String[] handlerArguments = new String[args.length-1];
				System.arraycopy(args, 1, handlerArguments, 0, handlerArguments.length);
				return handler.readAndReturn(s, handlerArguments);
			} else {
				value.add("Invalid argument (Use wiki for help)");
				return value;
			}
		}
		
		return value;
	}
	
	public boolean parseCommand(CommandSender s, String[] args) {
		if (args.length == 0) {
			s.spigot().sendMessage(MessageUtils.getPluginHelp());
			return true;
		}
		String handlerName = args[0];
		CommandHandler handler = handlers.get(handlerName);
		if (handler != null) {
			String[] handlerArguments = new String[args.length-1];
			System.arraycopy(args, 1, handlerArguments, 0, handlerArguments.length);
			boolean result = handler.readAndExecute(s, handlerArguments);
			if (!result) {
				s.spigot().sendMessage(handler.helpMessage());
			}
			return result;
		}
		s.spigot().sendMessage(MessageUtils.getPluginHelp());
		return true;
	}
	
	public static Map<String, CommandHandler> getHandlers() {
		return handlers;
	}
	
	public static void addHandler(String argument, CommandHandler handler) {
		handlers.put(argument, handler);
	}
	
	public static void removeHandler(String argument) {
		handlers.remove(argument);
	}
	
}
