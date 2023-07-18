package sk.m3ii0.amazingtitles.code.notifications;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import sk.m3ii0.amazingtitles.code.AmazingTitles;

public class NotificationListener implements Listener {

    @EventHandler
    public void join(PlayerJoinEvent e) {
        AmazingTitles.insertNewBar(e.getPlayer());
    }

    @EventHandler
    public void quit(PlayerQuitEvent e) {
        AmazingTitles.removeBar(e.getPlayer());
    }

}
