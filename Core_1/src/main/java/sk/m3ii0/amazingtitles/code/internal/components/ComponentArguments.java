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
	*
	*
	*
	* */
	
	public static ComponentArguments create(String mainText, String subText, BarColor componentColor, int duration, int fps, DisplayType displayType) {
		return new ComponentArguments(mainText, subText, componentColor, duration, fps, displayType);
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
	private final int fps;
	private final DisplayType displayType;
	
	/*
	*
	* Constructor
	*
	* */
	
	private ComponentArguments(String mainText, String subText, BarColor componentColor, int duration, int fps, DisplayType displayType) {
		this.mainText = mainText;
		this.subText = subText;
		this.componentColor = componentColor;
		this.duration = duration;
		this.fps = fps;
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
	
	public int getFps() {
		return fps;
	}
	
	public DisplayType getDisplayType() {
		return displayType;
	}
	
}
