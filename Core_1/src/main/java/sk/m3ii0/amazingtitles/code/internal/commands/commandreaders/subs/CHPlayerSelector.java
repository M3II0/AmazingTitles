package sk.m3ii0.amazingtitles.code.internal.commands.commandreaders.subs;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;
import sk.m3ii0.amazingtitles.code.internal.commands.commandreaders.CommandHandler;
import sk.m3ii0.amazingtitles.code.internal.utils.CommandUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CHPlayerSelector implements CommandHandler, Listener {
	
	private final List<String> names = new ArrayList<>();
	
	@Override
	public boolean readAndExecute(CommandSender s, String[] args) {
		return false;
	}
	
	@Override
	public List<String> readAndReturn(CommandSender s, String[] args) {
		List<String> selection = new ArrayList<>();
		if (args.length > 0) {
			String argument = args[0];
			selection.add("-p:");
			selection.addAll(names);
			if (argument.startsWith("-p:")) {
				selection.clear();
				selection.add("-p:<permission>");
			} else if (argument.endsWith(",")) {
				selection.clear();
				Set<String> alreadyContained = new HashSet<>(List.of(argument.split(",")));
				for (String var : names) {
					if (alreadyContained.contains(var)) continue;
					selection.add(argument + var);
				}
			} else if (!argument.isEmpty()) {
				selection = CommandUtils.copyAllStartingWith(selection, argument);
			}
		}
		return selection;
	}
	
	public CHPlayerSelector(Plugin plugin) {
		Bukkit.getPluginManager().registerEvents(this, plugin);
	}
	
	@EventHandler
	public void join(PlayerJoinEvent e) {
		names.add(e.getPlayer().getName());
	}
	
	@EventHandler
	public void quit(PlayerQuitEvent e) {
		names.remove(e.getPlayer().getName());
	}
	
}
