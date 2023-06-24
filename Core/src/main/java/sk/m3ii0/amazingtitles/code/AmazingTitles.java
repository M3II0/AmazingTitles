package sk.m3ii0.amazingtitles.code;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import sk.m3ii0.amazingtitles.basicpack.BasicPack;
import sk.m3ii0.amazingtitles.code.announcement.UpdateChecker;
import sk.m3ii0.amazingtitles.api.objects.AmazingCreator;
import sk.m3ii0.amazingtitles.code.colors.ColorTranslator;
import sk.m3ii0.amazingtitles.code.commands.PluginCommand;
import sk.m3ii0.amazingtitles.code.configuration.XYaml;
import sk.m3ii0.amazingtitles.code.notifications.BarNotification;
import sk.m3ii0.amazingtitles.code.notifications.DynamicBar;
import sk.m3ii0.amazingtitles.code.notifications.NotificationListener;
import sk.m3ii0.amazingtitles.code.spi.NmsBuilder;
import sk.m3ii0.amazingtitles.code.spi.NmsProvider;
import sk.m3ii0.amazingtitles.code.stats.Metrics;

import java.io.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.text.DecimalFormat;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;

public class AmazingTitles extends JavaPlugin {
	
	/*
	*
	* Values
	*
	* */
	
	private static Plugin instance;
	private static TitleManager titleManager;
	private static NmsProvider provider;
	private static Metrics metrics;
	private static final String version = "2.1";
	private static Map<UUID, DynamicBar> bars = new HashMap<>();
	private static Map<String, AmazingCreator> customComponents = new HashMap<>();
	private static File extensions;
	
	/*
	*
	* Configuration
	*
	* */
	
	private static XYaml options;
	
	/*
	*
	* Bukkit - API
	*
	* */
	
	@Override
	public void onLoad() {
		instance = this;
	}
	
	@Override
	public void onEnable() {
		long mills = -System.nanoTime();
		options = XYaml.fromPlugin(this, "options.yml", new File(getDataFolder(), "options.yml"));
		extensions = new File(getDataFolder(), "Extensions");
		if (!extensions.exists()) extensions.mkdirs();
		titleManager = new TitleManager();
		metrics = new Metrics(this, 18588);
		getCommand("amazingtitles").setExecutor(new PluginCommand());
		getCommand("amazingtitles").setTabCompleter(new PluginCommand());
		provider = getFromVersion(getVersion());
		if (provider == null) {
			Bukkit.getConsoleSender().sendMessage("§c[Error] AmazingTitles - You're using unsupported version...");
			Bukkit.getConsoleSender().sendMessage("§c[Error] AmazingTitles - Version must be 1.16 or higher");
			Bukkit.getConsoleSender().sendMessage("§c[Error] AmazingTitles - If you are using correct version, contact plugin author!");
			Bukkit.getConsoleSender().sendMessage("§c[Error] AmazingTitles - Disabling plugin...");
			Bukkit.getPluginManager().disablePlugin(this);
		}
		new UpdateChecker(this, "AmazingTitles", "https://www.spigotmc.org/resources/109916/", "amazingtitles.admin", version, 109916);
		Bukkit.getPluginManager().registerEvents(new NotificationListener(), this);
		Bukkit.getScheduler().runTaskTimerAsynchronously(this, () -> {
			long sysTime = System.currentTimeMillis();
			for (DynamicBar bar : bars.values()) {
				bar.update(sysTime);
			}
		}, 0, 1);
		BasicPack.loadDefaultAnimations();
		loadExtensions();
		mills += System.nanoTime();
		String format = new DecimalFormat("#.###").format(mills/1e+6);
		Bukkit.getConsoleSender().sendMessage(
				getEnableMessage(format)
		);
	}
	
	@Override
	public void onDisable() {
		HandlerList.unregisterAll(this);
		metrics.shutdown();
	}
	
	/*
	*
	* Private API
	*
	* */
	
	public static Map<String, AmazingCreator> getCustomComponents() {
		return Map.copyOf(customComponents);
	}
	public static void addCustomComponent(String name, AmazingCreator component) {
		customComponents.put(name, component);
	}
	public static AmazingCreator removeCustomComponent(String name) {
		return customComponents.remove(name);
	}
	public static Plugin getInstance() {
		return instance;
	}
	public static TitleManager getTitleManager() {
		return titleManager;
	}
	public static NmsProvider getProvider() {
		return provider;
	}
	public static XYaml getOptions() {
		return options;
	}
	public static void insertNewBar(Player player) {
		bars.put(player.getUniqueId(), DynamicBar.getBar(player));
	}
	public static void removeBar(Player player) {
		bars.remove(player.getUniqueId());
	}
	public static void setNotificationFor(BarNotification notification, List<Player> players) {
		for (Player p : players) {
			UUID uuid = p.getUniqueId();
			DynamicBar bar = bars.get(uuid);
			bar.notification("id" + (bar.getNotifications().size()+1), notification);
		}
	}
	public static String getPluginVersion() {
		return version;
	}
	public static Metrics getMetrics() {
		return metrics;
	}
	private String getVersion() {
		final String packageName = getServer().getClass().getPackage().getName();
		return packageName.substring(packageName.lastIndexOf('.') + 1);
	}
	private NmsProvider getFromVersion(String version) {
		for (NmsBuilder builder : ServiceLoader.load(NmsBuilder.class, getClassLoader())) {
			if (builder.checked(version)) {
				return builder.build();
			}
		}
		return null;
	}
	private String getEnableMessage(String millis) {
		return ColorTranslator.parse(
				"\n" +
						" &d> AmazingTitles - &7Created by M3II0&r\n" +
						"&r\n" +
						" &dEnabled:&r\n" +
						"  &f| NMS: " + getVersion() + "&r\n" +
						"  &f| Plugin Version: " + getPluginVersion() + "&r\n" +
						"  &f| Loaded Metrics.java&r\n" +
						"  &f| Loaded PacketProvider&r\n" +
						"  &f| Registered Notification bar listener&r\n" +
						"  &a| Plugin has been enabled!&r\n" +
						"&r\n" +
						" &d> Took " + millis + "ms!&r\n"
		);
	}
	private void loadExtensions() {
		File[] files = extensions.listFiles();
		File f = getServer().getWorldContainer();
		if (files == null) return;
		for (File extensionFile : files) {
			if (extensionFile.getName().endsWith(".jar")) {
				try (JarFile jarFile = new JarFile(extensionFile);) {
					ZipEntry document = jarFile.getEntry("extension.yml");
					try (InputStream stream = jarFile.getInputStream(document)) {
						Scanner s = new Scanner(stream).useDelimiter("\\A");
						String result = s.hasNext() ? s.next() : "";
						String main = result.replace("Class:", "").replace(" ", "");
						Enumeration<JarEntry> e = jarFile.entries();
						System.out.println(this.getClass().getProtectionDomain().getCodeSource().getLocation());
						URL[] urls = {f.toURI().toURL(), new URL("jar:file:" + extensionFile.getPath() + "!/")};
						try (URLClassLoader cl = new URLClassLoader(urls, getClassLoader())) {
							while (e.hasMoreElements()) {
								JarEntry je = e.nextElement();
								if(je.isDirectory() || !je.getName().endsWith(".class")){
									continue;
								}
								String className = je.getName().substring(0,je.getName().length()-6);
								className = className.replace('/', '.');
								Class<?> c = cl.loadClass(className);
								if (className.equals(main)) {
									Constructor<?> constructor = c.getConstructor();
									Object object = constructor.newInstance();
									Method method = c.getDeclaredMethod("load");
									method.setAccessible(true);
									method.invoke(object);
								}
							}
						}
					}
				} catch (Exception e) {
					System.out.println("§c[AT] - Error with loading extension " + extensionFile.getName() + "!");
				}
			}
		}
	}
	
}