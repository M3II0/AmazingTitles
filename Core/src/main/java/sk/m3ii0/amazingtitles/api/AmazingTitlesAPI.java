package sk.m3ii0.amazingtitles.api;

import sk.m3ii0.amazingtitles.api.objects.AmazingCreator;
import sk.m3ii0.amazingtitles.api.objects.FramesBuilder;
import sk.m3ii0.amazingtitles.code.AmazingTitles;

public class AmazingTitlesAPI {
	
	private final static AmazingTitlesAPI api = new AmazingTitlesAPI();
	
	protected AmazingTitlesAPI() {}
	
	public static AmazingTitlesAPI getApi() {
		return api;
	}
	
	public void createAndRegister(String name, boolean repeat, boolean infinite, FramesBuilder framesBuilder, String... arguments) {
		AmazingTitles.tryToSetPathAnimation(name);
		boolean enabled = AmazingTitles.getOptions().getBoolean("ExtensionsManager." + name);
		if (!enabled) return;
		AmazingCreator creator = new AmazingCreator(repeat, infinite, framesBuilder, arguments);
		AmazingTitles.addCustomComponent(name, creator);
	}
	
}