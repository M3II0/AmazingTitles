package sk.m3ii0.amazingtitles.code.testing;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import sk.m3ii0.amazingtitles.code.async.AmazingTitle;
import sk.m3ii0.amazingtitles.code.async.animations.ATWaves;

import java.awt.*;

public class TestListener implements Listener {
	
	@EventHandler
	public void chat(AsyncPlayerChatEvent e) {
		if (e.getMessage().equals("amazingtitles::test")) {
			
			AmazingTitle title = new ATWaves("|||||||||||||||||||||||||||||||||||||||||||||", "&7SubTitle", "#303030", "#fff673", 1, 60);
			title.sendTo(e.getPlayer());
			
		}
	}
	
}
