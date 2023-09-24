package sk.m3ii0.amazingtitles.code.internal.loaders;

import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import sk.m3ii0.amazingtitles.code.api.builders.AnimationBuilder;
import sk.m3ii0.amazingtitles.code.api.enums.AnimationType;
import sk.m3ii0.amazingtitles.code.api.enums.DisplayType;
import sk.m3ii0.amazingtitles.code.internal.components.ComponentArguments;
import sk.m3ii0.amazingtitles.code.internal.spi.NmsBuilder;
import sk.m3ii0.amazingtitles.code.internal.utils.ColorTranslator;

import java.util.ArrayList;
import java.util.List;
import java.util.ServiceLoader;

public class PluginLoader {
	
	public static NmsBuilder loadBuilder(ClassLoader classLoader) {
		String nmsVersion = getVersion();
		for (NmsBuilder builder : ServiceLoader.load(NmsBuilder.class, classLoader)) {
			if (builder.checked(nmsVersion)) {
				return builder;
			}
		}
		return null;
	}
	
	public static void loadDefaultAnimations() {
		
		AnimationBuilder rainbow = new AnimationBuilder(AnimationType.REPEATING, true);
		rainbow.setComponentArguments(ComponentArguments.create("Text is null", "SubText is null", BarColor.WHITE, 20, 20, DisplayType.TITLE));
		rainbow.setFramesBuilder((arguments, args) -> {
			List<String> frames = new ArrayList<>();
			String red = "#FF2424";
			String blue = "#002AFF";
			String green = "#00FF08";
			String smoothed = "          " + arguments.getMainText() + "          ";
			int length = smoothed.length();
			int withGradient = arguments.getMainText().length()*17;
			int start = 10*17;
			/*
			 * Red (Blue ->) Green
			 * */
			for (int i = 0; i <= length; i++) {
				String in = smoothed.substring(0, i);
				String out = smoothed.substring(i);
				String var = "<" + red + ">&l" + in + "</" + blue + "><" + blue + ">&l" + out + "</" + green + ">";
				frames.add(ColorTranslator.colorize(var).substring(start).substring(0, withGradient));
			}
			/*
			 * Green (Red ->) Blue
			 * */
			for (int i = 0; i <= length; i++) {
				String in = smoothed.substring(0, i);
				String out = smoothed.substring(i);
				String var = "<" + green + ">&l" + in + "</" + red + "><" + red + ">&l" + out + "</" + blue + ">";
				frames.add(ColorTranslator.colorize(var).substring(start).substring(0, withGradient));
			}
			/*
			 * Blue (Green ->) Red
			 * */
			for (int i = 0; i <= length; i++) {
				String in = smoothed.substring(0, i);
				String out = smoothed.substring(i);
				String var = "<" + blue + ">&l" + in + "</" + green + "><" + green + ">&l" + out + "</" + red + ">";
				frames.add(ColorTranslator.colorize(var).substring(start).substring(0, withGradient));
			}
			return frames;
		
		});
		rainbow.register("RAINBOW");
		
	}
	
	private static String getVersion() {
		final String packageName = Bukkit.getServer().getClass().getPackage().getName();
		return packageName.substring(packageName.lastIndexOf('.') + 1);
	}
	
}
