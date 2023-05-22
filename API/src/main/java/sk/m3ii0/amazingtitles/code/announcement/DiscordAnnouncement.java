package sk.m3ii0.amazingtitles.code.announcement;

import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;

public class DiscordAnnouncement {
	
	private static BukkitTask task;
	
	public static void runWithMessage(Plugin plugin, String link, String message, String permission) {
		task = Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, () -> {
			BaseComponent[] component = TextComponent.fromLegacyText(message);
			for (BaseComponent var : component) {
				var.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, TextComponent.fromLegacyText("§7Click to open!")));
				var.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, link));
			}
			for (Player p : Bukkit.getOnlinePlayers()) {
				if (p.hasPermission(permission)) p.spigot().sendMessage(component);
			}
		}, 0, 72000);
	}
	
	public static void close() {
		if (task != null) {
			if (!task.isCancelled()) task.cancel();
			task = null;
		}
	}
	
}
