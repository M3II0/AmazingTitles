package sk.m3ii0.amazingtitles.code.announcement;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Scanner;
import java.util.function.Consumer;

public class UpdateChecker implements Listener {
	
	private final JavaPlugin plugin;
	private final int resourceId;
	private final String currentVersion;
	private final String permission;
	private final String link;
	private final String pluginName;
	
	public UpdateChecker(JavaPlugin plugin, String pluginName, String link, String permission, String currentVersion, int resourceId) {
		this.plugin = plugin;
		this.resourceId = resourceId;
		this.currentVersion = currentVersion;
		this.permission = permission;
		this.link = link;
		this.pluginName = pluginName;
		Bukkit.getPluginManager().registerEvents(this, plugin);
	}
	
	private void getVersion(final Consumer<String> consumer) {
		Bukkit.getScheduler().runTaskAsynchronously(this.plugin, () -> {
			try (InputStream inputStream = new URL("https://api.spigotmc.org/legacy/update.php?resource=" + this.resourceId).openStream(); Scanner scanner = new Scanner(inputStream)) {
				if (scanner.hasNext()) {
					consumer.accept(scanner.next());
				}
			} catch (IOException ignore) {}
		});
	}
	
	@EventHandler
	public void playerJoin(PlayerJoinEvent e) {
		if (e.getPlayer().hasPermission(permission)) {
			getVersion((latest) -> {
				if (latest.equals(currentVersion)) {
					e.getPlayer().sendMessage("§a§l✓ §a" + pluginName + " §7» §fYou're using latest version!");
				} else {
					e.getPlayer().spigot().sendMessage(
					 new ComponentBuilder("§c§l✘ §c" + pluginName + " §7» §fYou're using outdated version! Click to visit SpigotMC and update plugin...")
					  .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT,  TextComponent.fromLegacyText("§fClick to open!")))
					  .event(new ClickEvent(ClickEvent.Action.OPEN_URL, link))
					  .create()
					);
				}
			});
		}
	}
	
}