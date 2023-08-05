package sk.m3ii0.amazingtitles.code.internal.components;

import org.bukkit.boss.BarColor;
import sk.m3ii0.amazingtitles.code.api.enums.DisplayType;

public class ComponentArguments {
	
	/*
	*
	* Builder
	*
	* */
	
	/*
	* RawArguments
	* -> [args=value,...]
	* */
	
	public static ComponentArguments create(String rawArguments, ComponentArguments defaults) {
		String mainText = defaults.getMainText();
		String subText = defaults.getSubText();
		BarColor componentColor = defaults.getComponentColor();
		int duration = defaults.getDuration();
		int speed = defaults.getSpeed();
		DisplayType displayType = defaults.getDisplayType();
		if (rawArguments.length() > 3) {
			rawArguments = rawArguments.substring(1, rawArguments.length()-1);
			for (String data : rawArguments.split(",")) {
				// Read & write
			}
		}
		return new ComponentArguments(mainText, subText, componentColor, duration, speed, displayType);
	}
	
	public static ComponentArguments create(String mainText, String subText, BarColor componentColor, int duration, int speed, DisplayType displayType) {
		return new ComponentArguments(mainText, subText, componentColor, duration, speed, displayType);
	}
	
	/*
	*
	* Arguments
	*
	* */
	
	private final String mainText;
	private final String subText;
	private final BarColor componentColor;
	private final int duration;
	private final int speed;
	private final DisplayType displayType;
	
	/*
	*
	* Constructor
	*
	* */
	
	private ComponentArguments(String mainText, String subText, BarColor componentColor, int duration, int speed, DisplayType displayType) {
		this.mainText = mainText;
		this.subText = subText;
		this.componentColor = componentColor;
		this.duration = duration;
		this.speed = speed;
		this.displayType = displayType;
	}
	
	/*
	*
	* Getters
	*
	* */
	
	public String getMainText() {
		return mainText;
	}
	
	public String getSubText() {
		return subText;
	}
	
	public BarColor getComponentColor() {
		return componentColor;
	}
	
	public int getDuration() {
		return duration;
	}
	
	public int getSpeed() {
		return speed;
	}
	
	public DisplayType getDisplayType() {
		return displayType;
	}
	
}
