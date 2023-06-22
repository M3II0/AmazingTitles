package sk.m3ii0.amazingtitles.api.objects;

import sk.m3ii0.amazingtitles.api.objects.types.ActionType;

import java.util.List;

public interface FramesBuilder {
	
	List<String> frameBuilder(ActionType type, String input, Object... args);
	
	
}
