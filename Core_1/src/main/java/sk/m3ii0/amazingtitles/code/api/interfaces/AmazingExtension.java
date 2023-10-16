package sk.m3ii0.amazingtitles.code.api.interfaces;

import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import sk.m3ii0.amazingtitles.code.api.AmazingTitles;
import sk.m3ii0.amazingtitles.code.internal.Booter;

import java.util.List;

public interface AmazingExtension {
	
	String extension_name();
	
	void load();
	void unload();
	
	default void addListener(Listener listener) {
		AmazingTitles.registerExtensionListener(this, listener);
	}
	
	default Plugin getPluginInstance() {
		return Booter.getInstance();
	}
	
	default List<Listener> getListeners() {
		return AmazingTitles.getExtensionListeners(extension_name());
	}
	
	default void unregisterListeners() {
		AmazingTitles.unregisterExtensionListeners(extension_name());
	}
	
}
