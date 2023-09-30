package sk.m3ii0.amazingtitles.code.internal.commands.commandreaders.subs;

import net.md_5.bungee.api.chat.*;
import org.bukkit.command.CommandSender;
import sk.m3ii0.amazingtitles.code.internal.commands.commandreaders.CommandHandler;
import sk.m3ii0.amazingtitles.code.internal.commands.commandreaders.HandlerType;
import sk.m3ii0.amazingtitles.code.internal.commands.commandreaders.InternalHandlerType;

import java.util.List;

public class CHPluginActions implements CommandHandler {
	
	@Override
	public BaseComponent[] helpMessage() {
		BaseComponent[] message = new ComponentBuilder("").append("\n§a§lPlugin module help:\n").create();
		BaseComponent[] clickable = TextComponent.fromLegacyText(" §7> /at pluginActions <Players> <Message>\n");
		for (BaseComponent var : clickable) {
			var.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/at pluginActions "));
			var.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, TextComponent.fromLegacyText("§fClick to suggest command")));
		}
		BaseComponent[] finalMessage = new BaseComponent[message.length + clickable.length];
		System.arraycopy(message, 0, finalMessage, 0, message.length);
		System.arraycopy(clickable, 0, finalMessage, message.length, clickable.length);
		return finalMessage;
	}
	
	@Override
	public String permission() {
		return "at.plugin";
	}
	
	@Override
	public HandlerType handlerType() {
		return new InternalHandlerType();
	}
	
	@Override
	public boolean readAndExecute(CommandSender s, String[] args) {
		return false;
	}
	
	@Override
	public List<String> readAndReturn(CommandSender s, String[] args) {
		return null;
	}
	
	public boolean isPluginAction(CommandSender s, String[] args) {
		return false;
	}
	
}
