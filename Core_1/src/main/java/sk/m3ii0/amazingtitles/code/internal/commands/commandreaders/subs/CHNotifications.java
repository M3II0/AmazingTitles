package sk.m3ii0.amazingtitles.code.internal.commands.commandreaders.subs;

import net.md_5.bungee.api.chat.*;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import sk.m3ii0.amazingtitles.code.api.AmazingTitles;
import sk.m3ii0.amazingtitles.code.internal.commands.commandreaders.CommandHandler;
import sk.m3ii0.amazingtitles.code.internal.commands.commandreaders.HandlerType;
import sk.m3ii0.amazingtitles.code.internal.commands.commandreaders.InternalHandlerType;
import sk.m3ii0.amazingtitles.code.internal.commands.commandreaders.readers.ArgsHelper;
import sk.m3ii0.amazingtitles.code.internal.smartbar.SmartNotification;
import sk.m3ii0.amazingtitles.code.internal.utils.ColorTranslator;
import sk.m3ii0.amazingtitles.code.internal.utils.CommandUtils;

import java.util.Collections;
import java.util.List;

public class CHNotifications implements CommandHandler {
	
	@Override
	public BaseComponent[] helpMessage() {
		BaseComponent[] message = new ComponentBuilder("").append("\n§a§lNotifications module help:\n").create();
		BaseComponent[] clickable = TextComponent.fromLegacyText(" §7> /at sendNotification <Players> <Duration> <Symbol> <Message>\n");
		for (BaseComponent var : clickable) {
			var.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/at sendNotification "));
			var.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, TextComponent.fromLegacyText("§fClick to suggest command")));
		}
		BaseComponent[] finalMessage = new BaseComponent[message.length + clickable.length];
		System.arraycopy(message, 0, finalMessage, 0, message.length);
		System.arraycopy(clickable, 0, finalMessage, message.length, clickable.length);
		return finalMessage;
	}
	
	@Override
	public String permission() {
		return "at.notifications";
	}
	
	@Override
	public HandlerType handlerType() {
		return new InternalHandlerType();
	}
	
	@Override
	public boolean readAndExecute(CommandSender s, String[] args) {
		if (args.length >= 4) {
			List<Player> players = ArgsHelper.readPlayers(args[0]);
			try {
				int duration = Integer.parseInt(args[1]);
				String symbol = ColorTranslator.colorize(args[2]);
				final StringBuilder argsHelper = new StringBuilder();
				for (int i = 3; i < args.length; i++) {
					argsHelper.append(args[i]);
				}
				String message = ColorTranslator.colorize(argsHelper.toString());
				AmazingTitles.sendNotification(new SmartNotification(duration, symbol, message), players);
			} catch (Exception e) {
				return false;
			}
		}
		return false;
	}
	
	@Override
	public List<String> readAndReturn(CommandSender s, String[] args) {
		if (args.length == 1) {
			return CommandUtils.copyAllStartingWith(ArgsHelper.preparePlayers(args[0]), args[0]);
		}
		if (args.length == 2) {
			return Collections.singletonList("Duration (Seconds)");
		}
		if (args.length == 3) {
			return Collections.singletonList("Symbol");
		}
		if (args.length > 3) {
			return Collections.singletonList("Notification message");
		}
		return Collections.emptyList();
	}
	
}
