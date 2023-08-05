package sk.m3ii0.amazingtitles.code.internal.loaders;

import org.bukkit.Bukkit;
import sk.m3ii0.amazingtitles.code.internal.spi.NmsBuilder;

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
	
	private static String getVersion() {
		final String packageName = Bukkit.getServer().getClass().getPackage().getName();
		return packageName.substring(packageName.lastIndexOf('.') + 1);
	}
	
}
