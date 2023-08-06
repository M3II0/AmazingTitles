package sk.m3ii0.amazingtitles.code.api;

import sk.m3ii0.amazingtitles.code.api.builders.AnimationBuilder;

import java.util.HashMap;
import java.util.Map;

public class AmazingTitles {
	
	/*
	*
	* Values
	*
	* */
	
	private static final Map<String, AnimationBuilder> animations = new HashMap<>();

	/*
	*
	* Animations
	*
	* */
	
	public static void registerCustomAnimation(String name, AnimationBuilder animationBuilder) {
		if (name == null || animationBuilder == null) return;
		name = name.replace(" ", "_").toUpperCase();
		animations.put(name, animationBuilder);
	}
	
	public static AnimationBuilder getCustomAnimation(String name) {
		return animations.get(name);
	}
	
	public static boolean isCustomAnimationExists(String name) {
		return getCustomAnimation(name) != null;
	}
	
	public static boolean isCustomAnimationEnabled(String name) {
		return animations.containsKey(name);
	}
	
}
