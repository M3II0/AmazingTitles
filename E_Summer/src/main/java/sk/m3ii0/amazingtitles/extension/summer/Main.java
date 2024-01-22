package sk.m3ii0.amazingtitles.extension.summer;

import org.bukkit.boss.BarColor;
import sk.m3ii0.amazingtitles.code.api.builders.AnimationBuilder;
import sk.m3ii0.amazingtitles.code.api.enums.AnimationType;
import sk.m3ii0.amazingtitles.code.api.enums.DisplayType;
import sk.m3ii0.amazingtitles.code.api.interfaces.AmazingExtension;
import sk.m3ii0.amazingtitles.code.internal.components.ComponentArguments;
import sk.m3ii0.amazingtitles.code.internal.utils.ColorTranslator;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Main implements AmazingExtension {
	
	@Override
	public String extension_name() {
		return "Summer";
	}
	
	@Override
	public void unload() {
	
	}
	
	@Override
	public void load() {
		
		AnimationBuilder wrapped_sun = new AnimationBuilder(this, AnimationType.REPEATING, false);
		wrapped_sun.setComponentArguments(ComponentArguments.create("Text is null", "SubText is null", BarColor.WHITE, 10, 1, DisplayType.TITLE));
		wrapped_sun.setFramesBuilder((arguments, args) -> {
			List<String> frames = new ArrayList<>();
			String sun = " &e☀&r ";
			frames.add(ColorTranslator.colorize(arguments.getMainText()));
			frames.add(ColorTranslator.colorize(sun + arguments.getMainText() + sun));
			return frames;
		});
		wrapped_sun.register("EXTENSION_SUMMER_WRAPPED_SUN");
		
		AnimationBuilder wrapped_colored_sun = new AnimationBuilder(this, AnimationType.REPEATING, false, "Hex(Sun-Color)");
		wrapped_colored_sun.setComponentArguments(ComponentArguments.create("Text is null", "SubText is null", BarColor.WHITE, 10, 1, DisplayType.TITLE));
		wrapped_colored_sun.setFramesBuilder((arguments, args) -> {
			List<String> frames = new ArrayList<>();
			String input = arguments.getMainText();
			String sunColor = (String) args[0];
			String sun = " &{" + sunColor + "}☀&r ";
			frames.add(ColorTranslator.colorize(input));
			frames.add(ColorTranslator.colorize(sun + input + sun));
			return frames;
		});
		wrapped_colored_sun.register("EXTENSION_SUMMER_WRAPPED_COLORED_SUN");
		
		AnimationBuilder summer_gradient = new AnimationBuilder(this, AnimationType.REPEATING, false, "0/1(1=bold,0=normal)");
		summer_gradient.setComponentArguments(ComponentArguments.create("Text is null", "SubText is null", BarColor.WHITE, 10, 1, DisplayType.TITLE));
		summer_gradient.setFramesBuilder((arguments, args) -> {
			List<String> frames = new ArrayList<>();
			int code = Integer.parseInt((String) args[0]);
			String bold = "";
			if (code == 1) bold = "&l";
			frames.add(ColorTranslator.colorize("<#ffff12>" + bold + arguments.getMainText() + "</#CC6600>"));
			return frames;
		});
		summer_gradient.register("EXTENSION_SUMMER_GRADIENT");
		
		AnimationBuilder summer_waves = new AnimationBuilder(this, AnimationType.REPEATING, false);
		summer_waves.setComponentArguments(ComponentArguments.create("Text is null", "SubText is null", BarColor.WHITE, 10, 1, DisplayType.TITLE));
		summer_waves.setFramesBuilder((arguments, args) -> {
			List<String> frames = new ArrayList<>();
			String input = arguments.getMainText();
			String color1 = "#ffff12";
			String color2 = "#CC6600";
			String smoothed = "          " + input + "          ";
			int length = smoothed.length();
			int withGradient = input.length()*17;
			int start = 10*17;
			for (int i = 0; i <= length; i++) {
				String in = smoothed.substring(0, i);
				String out = smoothed.substring(i);
				String var = "<" + color1 + ">&l" + in + "</" + color2 + "><" + color2 + ">&l" + out + "</" + color1 + ">";
				frames.add(ColorTranslator.colorize(var).substring(start).substring(0, withGradient));
			}
			for (int i = 0; i <= length; i++) {
				String in = smoothed.substring(0, i);
				String out = smoothed.substring(i);
				String var = "<" + color2 + ">&l" + in + "</" + color1 + "><" + color1 + ">&l" + out + "</" + color2 + ">";
				frames.add(ColorTranslator.colorize(var).substring(start).substring(0, withGradient));
			}
			for (int i = 0; i <= length; i++) {
				String in = smoothed.substring(0, i);
				String out = smoothed.substring(i);
				String var = "<" + color1 + ">&l" + in + "</" + color2 + "><" + color2 + ">&l" + out + "</" + color1 + ">";
				frames.add(ColorTranslator.colorize(var).substring(start).substring(0, withGradient));
			}
			return frames;
		});
		summer_waves.register("EXTENSION_SUMMER_WAVES");
		
		AnimationBuilder summer_bounce = new AnimationBuilder(this, AnimationType.REPEATING, false);
		summer_bounce.setComponentArguments(ComponentArguments.create("Text is null", "SubText is null", BarColor.WHITE, 10, 1, DisplayType.TITLE));
		summer_bounce.setFramesBuilder((arguments, args) -> {
			List<String> frames = new ArrayList<>();
			String input = arguments.getMainText();
			String smoothed = "          " + input + "          ";
			String color1 = "#ffff12";
			String color2 = "#CC6600";
			int length = smoothed.length();
			int withGradient = input.length()*17;
			int start = 10*17;
			for (int i = 0; i <= length; i++) {
				String to = smoothed.substring(0, i);
				String from = smoothed.substring(i);
				if (to.length() == 1 || from.length() == 1) continue;
				String formatted = "<" + color1 + ">&l" + to + "</" + color2 + ">" + "<" + color2 + ">&l" + from + "</" + color1 + ">";
				frames.add(ColorTranslator.colorize(formatted).substring(start).substring(0, withGradient));
			}
			int revertSize = frames.size();
			for (int i = revertSize-1; i > -1; i--) {
				String reversed = frames.get(i);
				frames.add(reversed);
			}
			return frames;
		});
		summer_bounce.register("EXTENSION_SUMMER_BOUNCE");
		
		AnimationBuilder summer_pulsing = new AnimationBuilder(this, AnimationType.REPEATING, false);
		summer_pulsing.setComponentArguments(ComponentArguments.create("Text is null", "SubText is null", BarColor.WHITE, 10, 1, DisplayType.TITLE));
		summer_pulsing.setFramesBuilder((arguments, args) -> {
			List<String> frames = new ArrayList<>();
			String input = arguments.getMainText();
			Color from = Color.decode("#ffff12");
			Color to = Color.decode("#CC6600");
			int r_max = Math.max(from.getRed(), to.getRed());
			int g_max = Math.max(from.getGreen(), to.getGreen());
			int b_max = Math.max(from.getBlue(), to.getBlue());
			int r_min = Math.min(from.getRed(), to.getRed());
			int g_min = Math.min(from.getGreen(), to.getGreen());
			int b_min = Math.min(from.getBlue(), to.getBlue());
			int r_total = r_max - r_min;
			int g_total = g_max - g_min;
			int b_total = b_max - b_min;
			int total_max = 0;
			if (r_total > total_max) total_max = r_total;
			if (g_total > total_max) total_max = g_total;
			if (b_total > total_max) total_max = b_total;
			int lastMinR = r_min;
			int lastMinG = g_min;
			int lastMinB = b_min;
			int lastMaxR = r_max;
			int lastMaxG = g_max;
			int lastMaxB = b_max;
			for (int i = 0; i <= total_max; i++) {
				int r = lastMinR;
				int g = lastMinG;
				int b = lastMinB;
				if (lastMinR < r_max) {
					r = (lastMinR = lastMinR+5);
				}
				if (lastMinG < g_max) {
					g = (lastMinG = lastMinG+5);
				}
				if (lastMinB < b_max) {
					b = (lastMinB = lastMinB+5);
				}
				if (r > r_max) r = r_max;
				if (g > g_max) g = g_max;
				if (b > b_max) b = b_max;
				Color c = new Color(r, g, b);
				String format = ColorTranslator.fromColor(c) + input;
				frames.add(ColorTranslator.colorize(format));
				if (r == r_max && g == g_max & b == b_max) {break;}
			}
			for (int i = 0; i <= total_max-1; i++) {
				int r = lastMaxR;
				int g = lastMaxG;
				int b = lastMaxB;
				if (lastMaxR > r_min) {
					r = (lastMaxR = lastMaxR-5);
				}
				if (lastMaxG > g_min) {
					g = (lastMaxG = lastMaxG-5);
				}
				if (lastMaxB > b_min) {
					b = (lastMaxB = lastMaxB-5);
				}
				if (r < r_min) r = r_min;
				if (g < g_min) g = g_min;
				if (b < b_min) b = b_min;
				Color c = new Color(r, g, b);
				String format = ColorTranslator.fromColor(c) + input;
				frames.add(ColorTranslator.colorize(format));
				if (r == r_min && g == g_min & b == b_min) {break;}
			}
			return frames;
		});
		summer_pulsing.register("EXTENSION_SUMMER_PULSING");
		
	}
	
	
	
}