package sk.m3ii0.amazingtitles.code.commands.types;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public enum AnimationTypes {
	
	SMOOTH_RAINBOW(
	 3,
	 true,
	 List.of("<Number-Speed(20=1s),Min:1>"),
	 List.of("<Number-Duration(InSeconds)>"),
	 List.of("<Text-AnimationText\\n\\AnimationSubText>")
	),
	
	STICK_BOUNCE(
	 3,
	 true,
	 List.of("<Number-Speed(20=1s),Min:1>"),
	 List.of("<Number-Duration(InSeconds)>"),
	 List.of("<Text-BouncerSymbol>"),
	 List.of("<Text-AnimationText\\n\\AnimationSubText>")
	),
	
	SMOOTH_WAVES(
	 5,
	 true,
	 List.of("<Number-Speed(20=1s),Min:1>"),
	 List.of("<Number-Duration(InSeconds)>"),
	 List.of("<HexCode-BaseColor(#ffffff)>"),
	 List.of("<HexCode-WaveColor(#ffffff)>"),
	 List.of("<Text-AnimationText\\n\\AnimationSubText>")
	),
	
	SMOOTH_BOUNCE(
	 5,
	 true,
	 List.of("<Number-Speed(20=1s),Min:1>"),
	 List.of("<Number-Duration(InSeconds)>"),
	 List.of("<HexCode-BaseColor(#ffffff)>"),
	 List.of("<HexCode-BounceColor(#ffffff)>"),
	 List.of("<Text-AnimationText\\n\\AnimationSubText>")
	),
	
	RAINBOW(
			3,
	 true,
	 List.of("<Number-Speed(20=1s),Min:1>"),
	 List.of("<Number-Duration(InSeconds)>"),
	 List.of("<Text-AnimationText\\n\\AnimationSubText>")
	),
	
	WAVES(
			5,
	 true,
	 List.of("<Number-Speed(20=1s),Min:1>"),
	 List.of("<Number-Duration(InSeconds)>"),
	 List.of("<HexCode-BaseColor(#ffffff)>"),
	 List.of("<HexCode-WaveColor(#ffffff)>"),
	 List.of("<Text-AnimationText\\n\\AnimationSubText>")
	),
	
	WRITER_STAY(
	 4,
	 true,
	 List.of("<Number-Speed(20=1s),Min:1>"),
	 List.of("<Number-Duration(InSeconds)>"),
	 List.of("<Text-WriterSymbol>"),
	 List.of("<Text-AnimationText\\n\\AnimationSubText>")
	),
	
	PULSING(
	 5,
	 true,
	 List.of("<Number-Speed(20=1s),Min:1>"),
	 List.of("<Number-Duration(InSeconds)>"),
	 List.of("<HexCode-BaseColor(#ffffff)>"),
	 List.of("<HexCode-PulseColor(#ffffff)>"),
	 List.of("<Text-AnimationText\\n\\AnimationSubText>")
	),
	
	COMING_FROM_RIGHT(
	 3,
	 true,
	 List.of("<Number-Speed(20=1s),Min:1>"),
	 List.of("<Number-Duration(InSeconds)>"),
	 List.of("<Text-AnimationText\\n\\AnimationSubText>")
	),
	
	COMING_FROM_LEFT(
	 3,
	 true,
	 List.of("<Number-Speed(20=1s),Min:1>"),
	 List.of("<Number-Duration(InSeconds)>"),
	 List.of("<Text-AnimationText\\n\\AnimationSubText>")
	),
	
	COMING_FROM_SIDES(
	 3,
	 true,
	 List.of("<Number-Speed(20=1s),Min:1>"),
	 List.of("<Number-Duration(InSeconds)>"),
	 List.of("<Text-AnimationText\\n\\AnimationSubText>")
	),
	
	FLASHING(
	 3,
	 true,
	 List.of("<Number-Speed(20=1s),Min:1>"),
	 List.of("<Number-Duration(InSeconds)>"),
	 List.of("<Text-AnimationText\\n\\AnimationSubText>")
	),
	
	SPACE_SPLIT(
	 3,
	 true,
	 List.of("<Number-Speed(20=1s),Min:1>"),
	 List.of("<Number-Duration(InSeconds)>"),
	 List.of("<Text-AnimationText(Space=NextFrame)\\n\\AnimationSubText>")
	),
	
	SPLIT(
	 3,
	 true,
	 List.of("<Number-Speed(20=1s),Min:1>"),
	 List.of("<Number-Duration(InSeconds)>"),
	 List.of("<Text-AnimationText(%frame%=NextFrame)\\n\\AnimationSubText>")
	),
	
	OPEN(
	 4,
	 true,
	 List.of("<Number-Speed(20=1s),Min:1>"),
	 List.of("<Number-Duration(InSeconds)>"),
	 List.of("<HexCode-BaseColor(#ffffff)>"),
	 List.of("<Text-AnimationText\\n\\AnimationSubText>")
	),

	BOUNCE(
	 5,
	 true,
	 List.of("<Number-Speed(20=1s),Min:1>"),
	 List.of("<Number-Duration(InSeconds)>"),
	 List.of("<HexCode-BaseColor(#ffffff)>"),
	 List.of("<HexCode-BounceColor(#ffffff)>"),
	 List.of("<Text-AnimationText\\n\\AnimationSubText>")
	);
	
	@SafeVarargs
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
