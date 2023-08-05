package sk.m3ii0.amazingtitles.code.internal.loaders;

public enum PluginMode {
	
	UNEXPECTED_ERROR(true, "Unexpected Error", "Unexpected error happened! Please contact the plugin author..."),
	OLD_CONFIGURATION_ERROR(true, "Old Configuration Error", "You're using old configuration! Please remove AmazingTitles folder & restart server..."),
	UNSUPPORTED_VERSION(true, "Unsupported Version Error", "You're using unsupported version of the plugin, use 1.13 or higher..."),
	WITHOUT_RGB(false, "Without RGB", "You're using plugin without RGB support, update server to 1.16 or higher to use more colors..."),
	FULLY_LOADED(false, "Fully Loaded", "Plugin has been loaded successfully...");
	
	PluginMode(boolean error, String name, String report) {
		this.error = error;
		this.name = name;
		this.report = report;
	}
	
	private final boolean error;
	private final String name;
	private final String report;
	
	public boolean isError() {
		return error;
	}
	
	public String getName() {
		return name;
	}
	
	public String getReport() {
		return report;
	}
	
}
