package sk.m3ii0.amazingtitles.code.internal.commands.commandreaders.subs;

import org.bukkit.command.CommandSender;
import sk.m3ii0.amazingtitles.code.internal.commands.commandreaders.CommandHandler;

import java.util.List;

public class CHPluginActions implements CommandHandler {
	
	@Override
	public boolean readAndExecute(CommandSender s, String[] args) {
		return false;
	}
	
	@Override
	public List<String> readAndReturn(CommandSender s, String[] args) {
		return null;
	}
	
	public boolean isPluginAction(CommandSender s, String[] args) {
		return true;
	}
	
}
