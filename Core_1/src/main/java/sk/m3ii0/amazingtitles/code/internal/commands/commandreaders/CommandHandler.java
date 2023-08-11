package sk.m3ii0.amazingtitles.code.internal.commands.commandreaders;

import org.bukkit.command.CommandSender;

import java.util.List;

public interface CommandHandler {
	
	boolean readAndExecute(CommandSender s, String[] args);
	
	List<String> readAndReturn(CommandSender s, String[] args);
	
	String permission();
	
	HandlerType handlerType();
	
}
