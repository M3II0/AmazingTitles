package sk.m3ii0.amazingtitles.code.internal.commands.commandreaders.subs;

import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import sk.m3ii0.amazingtitles.code.internal.commands.commandreaders.CommandHandler;
import sk.m3ii0.amazingtitles.code.internal.commands.commandreaders.HandlerType;
import sk.m3ii0.amazingtitles.code.internal.commands.commandreaders.InternalHandlerType;
import sk.m3ii0.amazingtitles.code.internal.commands.commandreaders.readers.ArgsHelper;
import sk.m3ii0.amazingtitles.code.internal.interactivemessages.InteractiveMessageHelper;
import sk.m3ii0.amazingtitles.code.internal.utils.CommandUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CHMessages implements CommandHandler {
	
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
				builder.append(args[i]);
			}
			BaseComponent[] message = InteractiveMessageHelper.getMessageFromRaw(builder.toString());
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
