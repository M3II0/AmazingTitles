package sk.m3ii0.amazingtitles.basicpack;

import sk.m3ii0.amazingtitles.api.AmazingTitlesAPI;
import sk.m3ii0.amazingtitles.code.colors.ColorTranslator;

import java.util.ArrayList;
import java.util.List;

public class BasicPack {
	
	public static void loadDefaultAnimations() {
		
		AmazingTitlesAPI.getApi().createAndRegister("SMOOTH_RAINBOW", true, true, (type, input, args) -> {
			List<String> frames = new ArrayList<>();
			String red = "#FF2424";
			String blue = "#002AFF";
			String green = "#00FF08";
			String smoothed = "          " + input + "          ";
			int length = smoothed.length();
			int withGradient = input.length()*17;
			int start = 10*17;
			/*
			 * Red (Blue ->) Green
			 * */
			for (int i = 0; i <= length; i++) {
				String in = smoothed.substring(0, i);
				String out = smoothed.substring(i);
				String var = "<" + red + ">&l" + in + "</" + blue + "><" + blue + ">&l" + out + "</" + green + ">";
				frames.add(ColorTranslator.parse(var).substring(start).substring(0, withGradient));
			}
			/*
			 * Green (Red ->) Blue
			 * */
			for (int i = 0; i <= length; i++) {
				String in = smoothed.substring(0, i);
				String out = smoothed.substring(i);
				String var = "<" + green + ">&l" + in + "</" + red + "><" + red + ">&l" + out + "</" + blue + ">";
				frames.add(ColorTranslator.parse(var).substring(start).substring(0, withGradient));
			}
			/*
			 * Blue (Green ->) Red
			 * */
			for (int i = 0; i <= length; i++) {
				String in = smoothed.substring(0, i);
				String out = smoothed.substring(i);
				String var = "<" + blue + ">&l" + in + "</" + green + "><" + green + ">&l" + out + "</" + red + ">";
				frames.add(ColorTranslator.parse(var).substring(start).substring(0, withGradient));
			}
			return frames;
		});
		
	}
	
}
