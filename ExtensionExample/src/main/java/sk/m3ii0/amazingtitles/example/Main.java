package sk.m3ii0.amazingtitles.example;

import sk.m3ii0.amazingtitles.api.AmazingTitlesAPI;
import sk.m3ii0.amazingtitles.api.objects.AmazingTitleExtension;

import java.util.ArrayList;
import java.util.Collections;

public class Main implements AmazingTitleExtension {
	
	@Override
	public void load() {
		
		// Do your stuff
		
		// How to register new Animation?
		// Infinite - Text input
		// Repeat - Should animation begin again or keep on last frame
		AmazingTitlesAPI.getApi().createAndRegister("TEST", false, true, (type, input, args) -> {
			return new ArrayList<>(Collections.singleton("Frames"));
		 }, "ArgumentHelp....");
		
	}
	
}