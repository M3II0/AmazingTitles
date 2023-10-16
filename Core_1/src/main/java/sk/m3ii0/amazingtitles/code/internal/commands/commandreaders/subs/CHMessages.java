package sk.m3ii0.amazingtitles.code.internal.commands.commandreaders.subs;

import net.md_5.bungee.api.chat.*;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import sk.m3ii0.amazingtitles.code.internal.commands.commandreaders.CommandHandler;
import sk.m3ii0.amazingtitles.code.internal.commands.commandreaders.HandlerType;
import sk.m3ii0.amazingtitles.code.internal.commands.commandreaders.InternalHandlerType;
import sk.m3ii0.amazingtitles.code.internal.commands.commandreaders.readers.ArgsHelper;
import sk.m3ii0.amazingtitles.code.internal.interactivemessages.InteractiveMessageHelper;
import sk.m3ii0.amazingtitles.code.internal.utils.CommandUtils;

import java.util.Collections;
import java.util.List;

public class CHMessages implements CommandHandler {
	
	@Override
	public BaseComponent[] helpMessage() {
		BaseComponent[] message = new ComponentBuilder("").append("\n§a§lMessages module help:\n").create();
		BaseComponent[] clickable = TextComponent.fromLegacyText(" §7> /at sendMessage <Players> <Message>\n");
		for (BaseComponent var : clickable) {
			var.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/at sendMessage "));
			var.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, TextComponent.fromLegacyText("§fClick to suggest command")));
		}
		BaseComponent[] finalMessage = new BaseComponent[message.length + clickable.length];
		System.arraycopy(message, 0, finalMessage, 0, message.length);
		System.arraycopy(clickable, 0, finalMessage, message.length, clickable.length);
		return finalMessage;
	}
	
	@Override
	public String permission() {
		return "at.messages";
	}
	
	@Override
	public HandlerType handlerType() {
		return new InternalHandlerType();
	}
	
	@Override
	public boolean readAndExecute(CommandSender s, String[] args) {
		if (args.length > 1) {
			List<Player> players = ArgsHelper.readPlayers(args[0]);
			final StringBuilder builder = new StringBuilder();
			for (int i = 1; i < args.length; i++) {
				builder.append(' ').append(args[i]);
			}
			String finalRaw;
			if (builder.length() > 0) {
				finalRaw = builder.substring(1);
			} else finalRaw = "";
			BaseComponent[] message = InteractiveMessageHelper.getMessageFromRaw(finalRaw);
			for (Player var : players) {
				var.spigot().sendMessage(message);
			}
			return true;
		}
		return false;
	}
	
	@Override
	public List<String> readAndReturn(CommandSender s, String[] args) {
		if (args.length == 1) {
			return CommandUtils.copyAllStartingWith(ArgsHelper.preparePlayers(args[0]), args[0]);
		}
		return Collections.singletonList("Your raw message (Visit wiki for more...)");
	}
	
}
