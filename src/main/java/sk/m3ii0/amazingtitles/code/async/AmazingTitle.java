package sk.m3ii0.amazingtitles.code.async;

import org.bukkit.entity.Player;
import sk.m3ii0.amazingtitles.code.colors.ColorTranslator;

import java.util.ArrayList;
import java.util.List;

public interface AmazingTitle {
	
	void streamToAll();
	void sendTo(Player... players);
	void removeFor(Player... player);
	void delete();
	
}
