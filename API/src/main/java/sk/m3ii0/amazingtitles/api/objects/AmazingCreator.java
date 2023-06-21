package sk.m3ii0.amazingtitles.api.objects;

import org.bukkit.entity.Player;
import sk.m3ii0.amazingtitles.code.async.AmazingComponent;
import sk.m3ii0.amazingtitles.code.commands.types.ActionType;

import java.util.*;

public class AmazingCreator {
	
	private final List<String> arguments;
	private final FramesBuilder framesBuilder;
	private final boolean repeat;
	private final List<String>[] competitions;
	
	public AmazingCreator(boolean repeat, FramesBuilder framesBuilder, String... arguments) {
		this.arguments = null;
		this.framesBuilder = null;
		this.repeat = false;
		this.competitions = null;
	}
	
	public List<String> getArguments() {
		return arguments;
	}
	
	public int getMinimum() {
		return arguments.size();
	}
	
	public List<String>[] getCompetitions() {
		return competitions;
	}
	
	public List<String> getComplete(int arg) {
		return new ArrayList<>();
	}
	
	public AmazingComponent dispatch(Player[] receivers, ActionType type, int speed, int duration, String input, Optional<String> subText, Object... args) {
		return null;
	}
	
}
