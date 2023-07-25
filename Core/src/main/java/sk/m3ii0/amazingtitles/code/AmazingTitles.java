package sk.m3ii0.amazingtitles.code;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import sk.m3ii0.amazingtitles.api.objects.AmazingCreator;
import sk.m3ii0.amazingtitles.api.objects.types.ActionType;
import sk.m3ii0.amazingtitles.basicpack.BasicPack;
import sk.m3ii0.amazingtitles.code.announcement.UpdateChecker;
import sk.m3ii0.amazingtitles.code.colors.ColorTranslator;
import sk.m3ii0.amazingtitles.code.commands.PluginCommand;
import sk.m3ii0.amazingtitles.code.notifications.BarNotification;
import sk.m3ii0.amazingtitles.code.notifications.DynamicBar;
import sk.m3ii0.amazingtitles.code.notifications.NotificationListener;
import sk.m3ii0.amazingtitles.code.spi.NmsBuilder;
import sk.m3ii0.amazingtitles.code.spi.NmsProvider;
import sk.m3ii0.amazingtitles.code.stats.Metrics;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.text.DecimalFormat;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;

public class AmazingTitles extends JavaPlugin implements Listener {
	
	/*
	*
	* Values
	*
	* */
	
	private static Plugin instance;
	private static TitleManager titleManager;
	private static NmsProvider provider;
	private static Metrics metrics;
	
	private static final String version = "3.8";
	private static final Map<UUID, DynamicBar> bars = new HashMap<>();
	private static final Map<String, AmazingCreator> customComponents = new HashMap<>();
	private static File extensions;
	
	private static String lineSeparator;
	
	private static boolean staticBar;
	private static boolean staticBarNotifications;
	private static boolean staticBarAnimations;
	private static int staticBarSpeed;
	private static int staticBarTickCounter;
	private static int staticBarFrame;
	private static List<String> staticBarFrames;
	private static AmazingCreator staticBarAnimation;
	private static Object[] staticBarArgs;
	
	/*
	*
	* Configuration
	*
	* */
	
	private static FileConfiguration options;
	private static File optionsFile;
	
	/*
	*
	* Bukkit - API
	*
	* */
	
	@Override
	public void onLoad() {
		instance = this;
		getDataFolder().mkdirs();
		saveResource("options.yml", false);
		optionsFile = new File(getDataFolder(), "options.yml");
		options = YamlConfiguration.loadConfiguration(optionsFile);
	}
	
	@Override
	public void onEnable() {
		long mills = -System.nanoTime();
		extensions = new File(getDataFolder(), "Extensions");
		if (!extensions.exists()) {
			boolean make = extensions.mkdirs();
		}
		titleManager = new TitleManager();
		metrics = new Metrics(this, 18588);
		getCommand("amazingtitles").setExecutor(new PluginCommand());
		getCommand("amazingtitles").setTabCompleter(new PluginCommand());
		Bukkit.getPluginManager().registerEvents(this, this);
		provider = getFromVersion(getVersion());
		if (provider == null) {
			Bukkit.getConsoleSender().sendMessage("§c[Error] AmazingTitles - You're using unsupported version...");
			Bukkit.getConsoleSender().sendMessage("§c[Error] AmazingTitles - Version must be 1.13 or higher");
			Bukkit.getConsoleSender().sendMessage("§c[Error] AmazingTitles - If you are using correct version, contact plugin author!");
			Bukkit.getConsoleSender().sendMessage("§c[Error] AmazingTitles - Disabling plugin...");
			Bukkit.getPluginManager().disablePlugin(this);
			return;
		}
		Bukkit.getPluginManager().registerEvents(new NotificationListener(), this);
		BasicPack.loadDefaultAnimations();
		loadExtensions();
		mills += System.nanoTime();
		String format = new DecimalFormat("#.###").format(mills/1e+6);
		Bukkit.getConsoleSender().sendMessage(
				getEnableMessage(format)
		);
		new UpdateChecker(this, "AmazingTitles", "https://www.spigotmc.org/resources/109916/", "amazingtitles.admin", version, 109916);
		staticBar = options.getBoolean("StaticBar.Enabled", false);
		staticBarAnimations = options.getBoolean("StaticBar.Animations", false);
		staticBarNotifications = options.getBoolean("StaticBar.Notifications", false);
		staticBarAnimation = customComponents.get(options.getString("StaticBar.Animation", "NONE"));
		staticBarSpeed = options.getInt("StaticBar.Speed", 1);
		List<String> args = options.getStringList("StaticBar.Animation_Arguments");
		Object[] s = new Object[args.size()];
		for (int i = 0; i < args.size(); i++) {
			s[i] = args.get(i);
		}
		staticBarArgs = s;
		staticBarFrames = staticBarAnimation.getFramesBuilder().frameBuilder(ActionType.ACTION_BAR, ColorTranslator.colorize(options.getString("StaticBar.Text", "Set your text!")), s);
		lineSeparator = options.getString("LineSeparator", "%subtitle%");
		Bukkit.getScheduler().runTaskTimerAsynchronously(this, () -> {
			long sysTime = System.currentTimeMillis();
			for (DynamicBar bar : bars.values()) {
				bar.update(sysTime);
			}
			tryToUpdateText();
		}, 20, 1);
	}
	
	@Override
	public void onDisable() {
		HandlerList.unregisterAll((Plugin) this);
		metrics.shutdown();
	}
	
	/*
	*
	* Events
	*
	* */
	
	@EventHandler
	public void quit(PlayerQuitEvent e) {
		getTitleManager().unsetTitleFor(e.getPlayer());
	}
	
	/*
	*
	* Private API
	*
	* */
	
	public static String getLineSeparator() {
		return lineSeparator;
	}
	public static boolean isStaticBar() {
		return staticBar;
	}
	public static boolean isStaticBarNotifications() {
		return staticBarNotifications;
	}
	public static boolean isStaticBarAnimations() {
		return staticBarAnimations;
	}
	public static int getStaticBarSpeed() {
		return staticBarSpeed;
	}
	public static AmazingCreator getStaticBarAnimation() {
		return staticBarAnimation;
	}
	public static Object[] getStaticBarArgs() {
		return staticBarArgs;
	}
	public static Map<UUID, DynamicBar> getBars() {
		return bars;
	}
	public static File getOptionsFile() {
		return optionsFile;
	}
	public static void tryToSetPathAnimation(String name) {
		if (!options.contains("ExtensionsManager." + name)) {
			options.set("ExtensionsManager." + name, true);
			try {
				options.save(optionsFile);
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
	}
	public static Map<String, AmazingCreator> getCustomComponents() {
		return customComponents;
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
	public static FileConfiguration getOptions() {
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
			if (bar == null) continue;
			bar.notification("id" + (bar.getNotifications().size()+1), notification);
		}
	}
	public static String getPluginVersion() {
		return version;
	}
	public static Metrics getMetrics() {
		return metrics;
	}
	public static String getStaticBarText() {
		return staticBarFrames.get(staticBarFrame);
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
		return ColorTranslator.colorize(
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
	private void tryToUpdateText() {
		++staticBarTickCounter;
		if (staticBarTickCounter == staticBarSpeed) {
			staticBarTickCounter = 0;
			++staticBarFrame;
			if (staticBarFrame == staticBarFrames.size()) {
				staticBarFrame = 0;
			}
		}
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