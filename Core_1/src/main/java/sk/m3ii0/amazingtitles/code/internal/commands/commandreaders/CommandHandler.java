package sk.m3ii0.amazingtitles.code.internal.commands.commandreaders;

import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.command.CommandSender;

import java.util.List;

public interface CommandHandler {
	
	boolean readAndExecute(CommandSender s, String[] args);
	
	List<String> readAndReturn(CommandSender s, String[] args);
	
	String permission();
	
	HandlerType handlerType();
	
	BaseComponent[] helpMessage();
	
}
