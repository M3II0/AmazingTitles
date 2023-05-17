package sk.m3ii0.amazingtitles.code.commands.types;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public enum AnimationTypes {
	
	RAINBOW(
			3,
	 true,
	 List.of("<Number-Speed(20=1s)>"),
	 List.of("<Number-Duration(InSeconds)>"),
	 List.of("<Text-AnimationText\\n\\AnimationSubText>")
	),
	
	WAVES(
			5,
	 true,
	 List.of("<Number-Speed(20=1s)>"),
	 List.of("<Number-Duration(InSeconds)>"),
	 List.of("<HexCode-BaseColor(#ffffff)>"),
	 List.of("<HexCode-WaveColor(#ffffff)>"),
	 List.of("<Text-AnimationText\\n\\AnimationSubText>")
	);
	
	AnimationTypes(int minimum, boolean infinite, List<String>... completer) {
		this.competitions = completer;
		this.infinite = infinite;
		this.minimum = minimum;
	}
	
	private final List<String>[] competitions;
	private final boolean infinite;
	private final int minimum;

	public int getMinimum() {
		return minimum;
	}

	public List<String>[] getCompetitions() {
		return competitions;
	}
	
	public static List<String> toIterable() {
		List<String> names = new ArrayList<>();
		for (AnimationTypes type : values()) {
			names.add(type.name());
		}
		return names;
	}
	
	public List<String> getComplete(int arg) {
		int length = competitions.length;
		if (arg < length) return competitions[arg];
		if (infinite) return Collections.emptyList();
		return List.of("<Argument out of bounds>");
	}
	
}
