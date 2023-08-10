package sk.m3ii0.amazingtitles.code.internal.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.plugin.Plugin;
import sk.m3ii0.amazingtitles.code.internal.commands.commandreaders.subs.*;
import sk.m3ii0.amazingtitles.code.internal.utils.ColorTranslator;
import sk.m3ii0.amazingtitles.code.internal.utils.Permissions;

import java.util.ArrayList;
import java.util.List;

public class PluginCommand implements CommandExecutor, TabExecutor {
	
	/*
	*
	* Values
	*
	* */
	
	private final CHAnimations animations;
	private final CHMessages messages;
	private final CHNotifications notifications;
	private final CHPlayerSelector playerSelector;
	private final CHPluginActions pluginActions;
	
	/*
	*
	* Constructor
	*
	* */
	
	public PluginCommand(Plugin plugin) {
		this.animations = new CHAnimations();
		this.messages = new CHMessages();
		this.notifications = new CHNotifications();
		this.playerSelector = new CHPlayerSelector(plugin);
		this.pluginActions = new CHPluginActions();
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
		return parseCommand(s, cmd, label, args);
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
		* 0 - Actions
		*
		* */
		if (args.length == 1) {
			if (s.hasPermission(Permissions.RELOAD_PERMISSION)) value.add("reloadPlugin");
			if (s.hasPermission(Permissions.NOTIFICATION_PERMISSION)) value.add("sendNotification");
			if (s.hasPermission(Permissions.ANIMATION_PERMISSION)) value.add("sendAnimation");
			if (s.hasPermission(Permissions.INTERACTIVE_MESSAGE_PERMISSION)) value.add("sendMessage");
		}
		
		/*
		*
		* 1 - PlayerSelection
		*
		* */
		if (args.length == 2) {
			String[] builtArgs = new String[1];
			builtArgs[0] = args[1];
			if (pluginActions.isPluginAction(s, builtArgs)) return pluginActions.readAndReturn(s, builtArgs);
			return playerSelector.readAndReturn(s, builtArgs);
		}
		
		/*
		*
		* 2 - CommandHandlers
		*
		* */
		if (args.length >= 3) {
			String firstArgument = args[0];
			String[] builtArgs = new String[args.length-2];
			System.arraycopy(args, 2, builtArgs, 0, builtArgs.length);
			if (pluginActions.isPluginAction(s, builtArgs)) return pluginActions.readAndReturn(s, builtArgs);
			if (firstArgument.equalsIgnoreCase("sendNotification")) return notifications.readAndReturn(s, builtArgs);
			if (firstArgument.equalsIgnoreCase("sendAnimation")) return animations.readAndReturn(s, builtArgs);
			if (firstArgument.equalsIgnoreCase("sendMessage")) return messages.readAndReturn(s, builtArgs);
		}
		
		return value;
	}
	
	private boolean parseCommand(CommandSender s, Command cmd, String label, String[] args) {
		if (args.length == 0) {
			return pluginActions.readAndExecute(s, args);
		}
		String arg1 = args[0];
		String[] builtArgs;
		if (args.length > 1) {
			builtArgs = new String[args.length-1];
			System.arraycopy(args, 2, builtArgs, 0, builtArgs.length);
		} else {
			builtArgs = new String[0];
		}
		if (pluginActions.isPluginAction(s, builtArgs)) return pluginActions.readAndExecute(s, builtArgs);
		if (arg1.equalsIgnoreCase("sendNotification")) return notifications.readAndExecute(s, builtArgs);
		if (arg1.equalsIgnoreCase("sendAnimation")) return animations.readAndExecute(s, builtArgs);
		if (arg1.equalsIgnoreCase("sendMessage")) return messages.readAndExecute(s, builtArgs);
		s.sendMessage(ColorTranslator.colorize("&cAmazingTitles &7| &fInvalid command structure!"));
		return true;
	}
	
}
