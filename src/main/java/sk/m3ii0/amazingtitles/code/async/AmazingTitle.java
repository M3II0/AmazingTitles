package sk.m3ii0.amazingtitles.code.async;

import org.bukkit.entity.Player;

import java.util.List;

public interface AmazingTitle {
	
	void streamToAll();
	void sendTo(Player... players);
	void removeFor(Player... player);
	void delete();
	
	int duration();
	int speed();
	List<String> frames();
	
}
