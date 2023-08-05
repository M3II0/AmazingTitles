package sk.m3ii0.amazingtitles.code.internal.components;

import org.bukkit.boss.BarColor;
import org.bukkit.entity.Player;
import sk.m3ii0.amazingtitles.code.api.enums.DisplayType;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface AnimationComponent {
	
	default List<String> frames() {
		return new ArrayList<>();
	}
	
	default String mainText() {
		return "This text is not set!";
	}
	
	default Optional<String> subText() {
		return Optional.empty();
	}
	
	default Optional<BarColor> componentColor() {
		return Optional.empty();
	}
	
	default int duration() {
		return 10;
	}
	
	default int fps() {
		return 1;
	}
	
	default DisplayType display() {
		return DisplayType.TITLE;
	}
	
	String callCurrentFrame();
	
	void addReceivers(Player... players);
	
	void addReceivers(Collection<Player> players);
	
	void removeReceivers(Player... players);
	
	void removeReceivers(Collection<Player> players);
	
	void prepare();
	
	void run();
	
	void end();
	
}
