package sk.m3ii0.amazingtitles.extension.textbounce;


import sk.m3ii0.amazingtitles.api.AmazingTitlesAPI;
import sk.m3ii0.amazingtitles.api.objects.AmazingTitleExtension;
import sk.m3ii0.amazingtitles.code.colors.ColorTranslator;

import java.util.ArrayList;
import java.util.List;

public class Main implements AmazingTitleExtension {
	
	public static void main(String[] args) {
	
	}
	
	@Override
	public void load() {

		AmazingTitlesAPI.getApi().createAndRegister("EXTENSION_BOUNCE_TEXT", true, true, true, (type, input, args) -> {
			List<String> frames = new ArrayList<>();
			List<String> builtSpaces = new ArrayList<>();
			int spaces = Integer.parseInt((String) args[0]);
			input = ColorTranslator.colorize(input);
			StringBuilder builder = new StringBuilder();
			for (int i = 0; i <= spaces; i++) {
				builder.append(" ");
				builtSpaces.add(builder.toString());
			}
			for (int i = 0; i < builtSpaces.size(); i++) {
				String start = builtSpaces.get(i);
				String end = "§r" + builtSpaces.get((builtSpaces.size()-1)-i);
				frames.add(start + input + end);
			}
			for (int i = builtSpaces.size()-1; i > -1; i--) {
				frames.add(frames.get(i));
			}
			return frames;
		}, "(Number<Spaces in bounce>)");
		
		AmazingTitlesAPI.getApi().createAndRegister("EXTENSION_BOUNCE_FLYING_AROUND_RIGHT", true, true, true, (type, input, args) -> {
			List<String> frames = new ArrayList<>();
			List<String> builtSpaces = new ArrayList<>();
			input = ColorTranslator.colorize(input);
			StringBuilder builder = new StringBuilder();
			for (int i = 0; i <= 90; i++) {
				builder.append("  ");
				builtSpaces.add(builder.toString());
			}
			frames.add(input);
			for (String var : builtSpaces) {
				frames.add(var + input);
			}
			for (int i = builtSpaces.size()-1; i > -1; i--) {
				frames.add(input + builtSpaces.get(i));
			}
			return frames;
		});
		
		AmazingTitlesAPI.getApi().createAndRegister("EXTENSION_BOUNCE_FLYING_AROUND_LEFT", true, true, true, (type, input, args) -> {
			List<String> frames = new ArrayList<>();
			List<String> builtSpaces = new ArrayList<>();
			input = ColorTranslator.colorize(input);
			StringBuilder builder = new StringBuilder();
			for (int i = 0; i <= 90; i++) {
				builder.append("  ");
				builtSpaces.add(builder.toString());
			}
			frames.add(input);
			for (String var : builtSpaces) {
				frames.add(input + var);
			}
			for (int i = builtSpaces.size()-1; i > -1; i--) {
				frames.add(builtSpaces.get(i) + input);
			}
			return frames;
		});

	}
	
}