package sk.m3ii0.amazingtitles.code.announcement;

import net.md_5.bungee.api.chat.*;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;

public class DiscordAnnouncement {
	
	private static BukkitTask task;
	private static BaseComponent[] message;
	
	public static void runWithMessage(Plugin plugin, String spigot, String discord, String pluginName) {
		message = new ComponentBuilder()
		 .appendLegacy("\n§c" + pluginName + " §f-> §7You're using testing version of this plugin!\n")
		 .appendLegacy("\n")
		 .append(new ComponentBuilder()
		  .appendLegacy(" §f> §6Click to open SpigotMC page\n")
		  .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, TextComponent.fromLegacyText("§6Click to open SpigotMC page")))
		  .event(new ClickEvent(ClickEvent.Action.OPEN_URL, spigot))
		  .create())
		 .append(new ComponentBuilder()
		  .appendLegacy(" §f> §bClick to remove this message\n")
		  .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, TextComponent.fromLegacyText("§bClick to remove")))
		  .event(new ClickEvent(ClickEvent.Action.OPEN_URL, discord))
		  .create())
		 .appendLegacy("\n§7(Removing message is free, condition is to join our Discord Server and download .jar from there)\n")
		 .create();
		task = Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, () -> {
			Bukkit.spigot().broadcast(message);
		}, 0, 24000);
	}
	
	public static void close() {
		if (task != null) {
			if (!task.isCancelled()) task.cancel();
			task = null;
		}
	}
	
}
