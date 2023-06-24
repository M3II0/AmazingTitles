package sk.m3ii0.amazingtitles.animations;

import sk.m3ii0.amazingtitles.api.AmazingTitlesAPI;
import sk.m3ii0.amazingtitles.api.objects.AmazingTitleExtension;

import java.util.ArrayList;
import java.util.Collections;

public class Main implements AmazingTitleExtension {

	@Override
	public void load() {
		AmazingTitlesAPI.getApi().createAndRegister("TEST", false, false, (type, input, args) -> {
			return new ArrayList<>(Collections.singleton(input));
		});
	}

}