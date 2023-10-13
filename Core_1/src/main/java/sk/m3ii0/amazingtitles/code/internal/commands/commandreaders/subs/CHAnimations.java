package sk.m3ii0.amazingtitles.code.internal.commands.commandreaders.subs;

import net.md_5.bungee.api.chat.*;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import sk.m3ii0.amazingtitles.code.api.AmazingTitles;
import sk.m3ii0.amazingtitles.code.api.builders.AnimationBuilder;
import sk.m3ii0.amazingtitles.code.internal.commands.commandreaders.CommandHandler;
import sk.m3ii0.amazingtitles.code.internal.commands.commandreaders.HandlerType;
import sk.m3ii0.amazingtitles.code.internal.commands.commandreaders.InternalHandlerType;
import sk.m3ii0.amazingtitles.code.internal.commands.commandreaders.readers.ArgsHelper;
import sk.m3ii0.amazingtitles.code.internal.components.AnimationComponent;
import sk.m3ii0.amazingtitles.code.internal.components.ComponentArguments;
import sk.m3ii0.amazingtitles.code.internal.utils.ColorTranslator;
import sk.m3ii0.amazingtitles.code.internal.utils.CommandUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CHAnimations implements CommandHandler {
	
	@Override
	public BaseComponent[] helpMessage() {
		BaseComponent[] message = new ComponentBuilder("").append("\n§a§lAnimations module help:\n").create();
		BaseComponent[] clickable = TextComponent.fromLegacyText(" §7> /at sendAnimation <Players> <arguments/@> <Animation> [AnimationArguments] <Text%subtitle%subText>\n");
		for (BaseComponent var : clickable) {
			var.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/at sendAnimation "));
			var.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, TextComponent.fromLegacyText("§fClick to suggest command")));
		}
		BaseComponent[] finalMessage = new BaseComponent[message.length + clickable.length];
		System.arraycopy(message, 0, finalMessage, 0, message.length);
		System.arraycopy(clickable, 0, finalMessage, message.length, clickable.length);
		return finalMessage;
	}
	
	@Override
	public String permission() {
		return "at.animations";
	}
	
	@Override
	public HandlerType handlerType() {
		return new InternalHandlerType();
	}
	
	@Override
	public boolean readAndExecute(CommandSender s, String[] args) {
		try {
			List<Player> players = ArgsHelper.readPlayers(args[0]);
			ComponentArguments arguments = ArgsHelper.readArguments(args[1]);
			AnimationBuilder builder = AmazingTitles.getCustomAnimation(args[2]);
			String[] totalArguments = new String[builder.getTotalArguments()];
			if (builder.getTotalArguments() >= 0)
				System.arraycopy(args, 3, totalArguments, 0, builder.getTotalArguments());
			StringBuilder text = new StringBuilder();
			for (int i = 3+builder.getTotalArguments(); i < args.length; i++) {
				text.append(args[i]).append(' ');
			}
			String total = text.toString().replaceAll(" $", "");
			String mainText = total.split("%subtitle%")[0];
			String subtitle = "";
			if (text.toString().split("%subtitle%").length > 1) {
				subtitle = ColorTranslator.colorize(total.split("%subtitle%")[1]);
			}
			AnimationComponent component = builder.createComponent(
			 ComponentArguments.create(mainText, subtitle, arguments.getComponentColor(), arguments.getDuration(), arguments.getFps(), arguments.getDisplayType())
			 , totalArguments);
			component.addReceivers(players);
			component.prepare();
			component.run();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	@Override
	public List<String> readAndReturn(CommandSender s, String[] args) {
		if (args.length == 1) {
			return CommandUtils.copyAllStartingWith(ArgsHelper.preparePlayers(args[0]), args[0]);
		}
		if (args.length == 2) {
			return CommandUtils.copyAllStartingWith(ArgsHelper.prepareArguments(args[1]), args[1]);
		}
		if (args.length == 3) {
			return CommandUtils.copyAllStartingWith(new ArrayList<>(AmazingTitles.getAnimationNames()), args[2]);
		}
		if (args.length > 3) {
			AnimationBuilder animation = AmazingTitles.getCustomAnimation(args[2]);
			if (animation == null) {
				return Collections.singletonList("Invalid animation!");
			}
			return animation.getArgumentAt(args.length-4);
		}
		return null;
	}
	
}
