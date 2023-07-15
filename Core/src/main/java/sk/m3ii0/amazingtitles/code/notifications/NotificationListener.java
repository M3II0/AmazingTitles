package sk.m3ii0.amazingtitles.code.notifications;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import sk.m3ii0.amazingtitles.code.AmazingTitles;

import java.util.List;

public class NotificationListener implements Listener {

    @EventHandler
    public void join(PlayerJoinEvent e) {
        AmazingTitles.insertNewBar(e.getPlayer());
        AmazingTitles.setNotificationFor(BarNotification.create("1", "One", 5), List.of(e.getPlayer()));
        AmazingTitles.setNotificationFor(BarNotification.create("2", "Two", 5), List.of(e.getPlayer()));
        AmazingTitles.setNotificationFor(BarNotification.create("3", "Three", 5), List.of(e.getPlayer()));
    }

    @EventHandler
    public void quit(PlayerQuitEvent e) {
        AmazingTitles.removeBar(e.getPlayer());
    }

}
