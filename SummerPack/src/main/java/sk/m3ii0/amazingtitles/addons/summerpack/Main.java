package sk.m3ii0.amazingtitles.addons.summerpack;

import sk.m3ii0.amazingtitles.api.AmazingTitlesAPI;
import sk.m3ii0.amazingtitles.api.objects.AmazingTitleExtension;
import sk.m3ii0.amazingtitles.code.colors.ColorTranslator;

import java.util.ArrayList;
import java.util.List;

public class Main implements AmazingTitleExtension {

	@Override
	public void load() {
		
		AmazingTitlesAPI.getApi().createAndRegister("PACK_SUMMER_BLINKING_SUNS", true, true, (type, input, args) -> {
			List<String> frames = new ArrayList<>();
			input = ColorTranslator.BeforeType.replaceColors(input);
			input = "§o" + input;
			String smoothed = "          " + input + "          ";
			String color = (String) args[0];
			
			return frames;
		});
		
	}

}