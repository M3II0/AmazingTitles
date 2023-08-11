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
				return handler.readAndReturn(s, args);
			} else {
				value.add("Invalid argument (Use wiki for help)");
				return value;
			}
		}
		
		return value;
	}
	
	public boolean parseCommand(CommandSender s, String[] args) {
		if (args.length == 0) {
			s.spigot().sendMessage(getHelpMessage());
			return true;
		}
		String handlerName = args[0];
		CommandHandler handler = handlers.get(handlerName);
		if (handler != null) {
			return handler.readAndExecute(s, args);
		}
		s.spigot().sendMessage(getHelpMessage());
		return true;
	}
	
	public static BaseComponent[] getHelpMessage() {
		ComponentBuilder builder = new ComponentBuilder("");
		builder.append("\n");
		builder.append("\n§5§lAmazingTitles §7✦ §fHelp message");
		builder.append("\n");
		builder.append("\n  §dAvailable commands");
		for (Map.Entry<String, CommandHandler> entry : handlers.entrySet()) {
			CommandHandler handler = entry.getValue();
			String argument = entry.getKey();
			builder.append(MessageUtils.quickCreate("\n   §7✎ §7/at §f" + argument, "§aClick to suggest\n§fType: §7" + handler.handlerType().id() + "\n§fPermission: §7" + handler.permission(), ClickEvent.Action.SUGGEST_COMMAND, "/at " + argument));
		}
		builder.append("\n");
		BaseComponent[] wiki = MessageUtils.quickCreate("§dWiki", "§fClick to open", ClickEvent.Action.OPEN_URL, "https://m3ii0.gitbook.io/amazingtitles/");
		BaseComponent[] discord = MessageUtils.quickCreate("§9Discord", "§fClick to open", ClickEvent.Action.OPEN_URL, "https://discord.com/invite/Ms7uAAAtcT");
		BaseComponent[] spigot = MessageUtils.quickCreate("§6Spigot", "§fClick to open", ClickEvent.Action.OPEN_URL, "https://www.spigotmc.org/resources/109916/");
		builder.append("\n  ").append(wiki).append("§7 | ").append(discord).append("§7 | ").append(spigot);
		builder.append("\n");
		return builder.create();
	}
	
	public static void addHandler(String argument, CommandHandler handler) {
		handlers.put(argument, handler);
	}
	
	public static void removeHandler(String argument) {
		handlers.remove(argument);
	}
	
}
