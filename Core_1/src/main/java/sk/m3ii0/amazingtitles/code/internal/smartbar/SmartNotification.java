package sk.m3ii0.amazingtitles.code.internal.smartbar;

import sk.m3ii0.amazingtitles.code.internal.utils.ColorTranslator;

import java.util.ArrayList;
import java.util.List;

public class SmartNotification {
	
	/*
	*
	* Values
	*
	* */
	
	private final double time;
	private long out;
	private final String symbol;
	private final String text;
	private final List<String> frames;
	private int lastFrame = 0;
	
	private long start = -1L;
	
	/*
	*
	* Constructor
	*
	* */
	
	public SmartNotification(double time, String symbol, String text) {
		this.time = time+2;
		this.symbol = ColorTranslator.colorize(symbol);
		this.text = ColorTranslator.colorize(text);
		this.frames = new ArrayList<>();
		final StringBuilder builder = new StringBuilder();
		frames.add("");
		for (String var : ColorTranslator.charactersWithColors(this.text)) {
			builder.append(var);
			this.frames.add(builder.toString());
		}
	}
	
	/*
	*
	* API
	*
	* */
	
	public void start(long systemTime) {
		this.start = systemTime;
		this.out = (long) (systemTime+(time*1000L)-1000L);
	}
	
	public void extend(double seconds) {
		this.out += seconds;
	}
	
	public double getTime() {
		return time;
	}
	
	public void quickRemove() {
		this.out = 0L;
	}
	
	public String getCurrentFrame(long systemTime, boolean latest) {
		final StringBuilder frame = new StringBuilder(symbol);
		if (!latest) {
			if (!(lastFrame <= 0)) {
				--lastFrame;
				frame.append(" ").append(frames.get(lastFrame));
			}
		} else {
			if (systemTime >= out) {
				if (lastFrame > 0) {
					--lastFrame;
					frame.append(" ").append(frames.get(lastFrame));
				} else {
					return "";
				}
			} else {
				if (lastFrame+1 < frames.size()) {
					++lastFrame;
					frame.append(" ").append(frames.get(lastFrame));
				} else {
					frame.append(" ").append(this.text);
				}
			}
		}
		return frame.toString();
	}
	
	public boolean isStarted() {
		return start != -1L;
	}
	
	public boolean isEnding(long systemTime) {
		return systemTime >= out;
	}
	
	public boolean isOver(long systemTime) {
		return systemTime >= out && lastFrame < 1;
	}
	
	
}
