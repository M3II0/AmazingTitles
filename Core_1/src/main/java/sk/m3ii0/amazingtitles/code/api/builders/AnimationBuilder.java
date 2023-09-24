package sk.m3ii0.amazingtitles.code.api.builders;

import org.bukkit.boss.BarColor;
import sk.m3ii0.amazingtitles.code.api.AmazingTitles;
import sk.m3ii0.amazingtitles.code.api.enums.AnimationType;
import sk.m3ii0.amazingtitles.code.api.enums.DisplayType;
import sk.m3ii0.amazingtitles.code.api.interfaces.FramesBuilder;
import sk.m3ii0.amazingtitles.code.internal.Booter;
import sk.m3ii0.amazingtitles.code.internal.components.AnimationComponent;
import sk.m3ii0.amazingtitles.code.internal.components.ComponentArguments;
import sk.m3ii0.amazingtitles.code.internal.components.implementations.FadeInAnimationComponent;
import sk.m3ii0.amazingtitles.code.internal.components.implementations.LightAnimationComponent;
import sk.m3ii0.amazingtitles.code.internal.components.implementations.RepeatingAnimationComponent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class AnimationBuilder {
	
	/*
	*
	* Values
	*
	* */
	
	private static final List<String> textArgument = Collections.singletonList(
	 "(Animation Text - visit wiki for more)"
	);
	private ComponentArguments componentArguments = ComponentArguments.create("Default Text", "Default SubText", BarColor.WHITE, 1, 20, DisplayType.TITLE);
	private FramesBuilder framesBuilder = (componentArguments, args) -> {
		String mainText = componentArguments.getMainText();
		return new ArrayList<>(Collections.singletonList(mainText));
	};
	
	private String overrideMainText = null;
	private String overrideSubText = null;
	private BarColor overrideBarColor = null;
	private int overrideDuration = -99;
	private int overrideFps = -99;
	private DisplayType overrideDisplayType = null;
	
	private final AnimationType animationType;
	private final boolean requiresHex;
	private final List<String> arguments;
	
	/*
	*
	* Constructor
	*
	* */
	
	public AnimationBuilder(AnimationType animationType, boolean requiresHex, String... arguments) {
		this.animationType = animationType;
		this.requiresHex = requiresHex;
		this.arguments = Arrays.asList((arguments));
	}
	
	/*
	*
	* Getters
	*
	* */
	
	public FramesBuilder getFramesBuilder() {
		return framesBuilder;
	}
	
	public AnimationType getAnimationType() {
		return animationType;
	}
	
	public boolean isRequiresHex() {
		return requiresHex;
	}
	
	public List<String> getArguments() {
		return arguments;
	}
	
	public List<String> getArgumentAt(int position) {
		if (position >= arguments.size()) {
			return textArgument;
		}
		return Collections.singletonList((arguments.get(position)));
	}
	
	public int getTotalArguments() {
		return arguments.size();
	}
	
	/*
	*
	* Setters
	*
	* */
	
	public void setFramesBuilder(FramesBuilder framesBuilder) {
		this.framesBuilder = framesBuilder;
	}
	
	public void setComponentArguments(ComponentArguments componentArguments) {
		this.componentArguments = componentArguments;
	}
	
	/*
	*
	* Overrides
	*
	* */
	
	public void overrideMainText(String override) {
		this.overrideMainText = override;
	}
	
	public void overrideSubText(String override) {
		this.overrideSubText = override;
	}
	
	public void overrideBarColor(BarColor override) {
		this.overrideBarColor = override;
	}
	
	public void overrideDuration(int override) {
		this.overrideDuration = override;
	}
	
	public void overrideFps(int override) {
		this.overrideFps = override;
	}
	
	public void overrideDisplayType(DisplayType override) {
		this.overrideDisplayType = override;
	}
	
	/*
	*
	* Functions
	*
	* */
	
	public void register(String name) {
		AmazingTitles.registerCustomAnimation(name, this);
	}
	
	public AnimationComponent createComponent(ComponentArguments arguments, String[] args) {
		String mainText = (overrideMainText != null)? overrideMainText : (arguments.getMainText() != null)? arguments.getMainText() : componentArguments.getMainText();
		String subText = (overrideSubText != null)? overrideSubText : (arguments.getSubText() != null)? arguments.getSubText() : componentArguments.getSubText();
		BarColor barColor = (overrideBarColor != null)? overrideBarColor : (arguments.getComponentColor() != null)? arguments.getComponentColor() : componentArguments.getComponentColor();
		int duration = (overrideDuration != -99)? overrideDuration : (arguments.getDuration() > 0)? arguments.getDuration() : componentArguments.getDuration();
		int fps = (overrideFps != -99)? overrideFps : (arguments.getFps() > 0)? arguments.getFps() : componentArguments.getFps();
		DisplayType displayType = (overrideDisplayType != null)? overrideDisplayType : (arguments.getDisplayType() != null)? arguments.getDisplayType() : componentArguments.getDisplayType();
		ComponentArguments builtArguments = ComponentArguments.create(mainText, subText, barColor, duration, fps, displayType);
		List<String> frames = framesBuilder.buildFrames(builtArguments, args);
		if (animationType == AnimationType.REPEATING) {
			return new RepeatingAnimationComponent(Booter.getInstance(), frames, mainText, subText, fps, duration, displayType, barColor);
		}
		if (animationType == AnimationType.FADE_IN) {
			return new FadeInAnimationComponent(Booter.getInstance(), frames, mainText, subText, fps, duration, displayType, barColor);
		}
		return new LightAnimationComponent(Booter.getInstance(), frames, mainText, subText, duration, displayType, barColor);
	}
	
}
