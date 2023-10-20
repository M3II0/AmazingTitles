package sk.m3ii0.amazingtitles.code.internal.commands.commandreaders.subs;

import net.md_5.bungee.api.chat.*;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import sk.m3ii0.amazingtitles.code.internal.commands.commandreaders.CommandHandler;
import sk.m3ii0.amazingtitles.code.internal.commands.commandreaders.HandlerType;
import sk.m3ii0.amazingtitles.code.internal.commands.commandreaders.InternalHandlerType;
import sk.m3ii0.amazingtitles.code.internal.commands.commandreaders.readers.ArgsHelper;
import sk.m3ii0.amazingtitles.code.internal.interactivemessages.InteractiveMessageHelper;
import sk.m3ii0.amazingtitles.code.internal.utils.ColorTranslator;
import sk.m3ii0.amazingtitles.code.internal.utils.CommandUtils;
import sk.m3ii0.amazingtitles.code.internal.utils.TextComponentBuilder;

import java.util.Collections;
import java.util.List;

public class CHMessages implements CommandHandler {
	
	@Override
	public BaseComponent[] helpMessage() {
		if (ColorTranslator.isHexSupport()) {
			TextComponentBuilder builder = new TextComponentBuilder();
			builder.appendLegacy("\n<#a217ff>AmazingTitles ✎ </#ff7ae9> &fInteractive Message\n");
			builder.appendLegacy(" &7> <#dedede>/at sendMessage <Players> <Message></#c7c7c7>\n", "&{#ffa6fc}Click to suggest command", ClickEvent.Action.SUGGEST_COMMAND, "/at sendMessage ");
			builder.appendLegacy("§f");
			return builder.createMessage();
		}
		TextComponentBuilder builder = new TextComponentBuilder();
		builder.appendLegacy("\n&5AmazingTitles ✎ &fInteractive Message\n");
		builder.appendLegacy(" &7> &7/at sendMessage <Players> <Message>\n", "&{#ffa6fc}Click to suggest command", ClickEvent.Action.SUGGEST_COMMAND, "/at sendMessage ");
		builder.appendLegacy("§f");
		return builder.createMessage();
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
