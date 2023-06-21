package sk.m3ii0.amazingtitles.code.async;

import org.bukkit.entity.Player;
import sk.m3ii0.amazingtitles.code.commands.types.ActionType;

import java.util.ArrayList;
import java.util.List;

public interface AmazingComponent {
	
	default List<String> frames() {
		return new ArrayList<>();
	}
	default String text() {
		return "This text is not set!";
	}
	default int duration() {
		return 20;
	}
	default int speed() {
		return 1;
	}
	
	ActionType type();
	
	void streamToAll();
	void sendTo(Player... players);
	void removeFor(Player... player);
	void delete();
	
}
