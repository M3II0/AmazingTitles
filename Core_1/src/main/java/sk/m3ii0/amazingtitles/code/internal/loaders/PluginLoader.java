package sk.m3ii0.amazingtitles.code.internal.loaders;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.plugin.Plugin;
import sk.m3ii0.amazingtitles.code.api.AmazingTitles;
import sk.m3ii0.amazingtitles.code.api.builders.AnimationBuilder;
import sk.m3ii0.amazingtitles.code.api.enums.AnimationType;
import sk.m3ii0.amazingtitles.code.api.enums.DisplayType;
import sk.m3ii0.amazingtitles.code.api.interfaces.AmazingExtension;
import sk.m3ii0.amazingtitles.code.internal.components.ComponentArguments;
import sk.m3ii0.amazingtitles.code.internal.spi.NmsBuilder;
import sk.m3ii0.amazingtitles.code.internal.utils.ColorTranslator;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;

public class PluginLoader {
	
	public static NmsBuilder loadBuilder(ClassLoader classLoader, boolean old) {
		String nmsVersion;
		if (old) {
			nmsVersion = getVersion();
		} else {
			nmsVersion = getNewVersion();
		}
		for (NmsBuilder builder : ServiceLoader.load(NmsBuilder.class, classLoader)) {
			if (builder.checked(nmsVersion)) {
				return builder;
			}
		}
		return null;
	}
	
	public static void loadExtensions(Plugin owner) {
		ClassLoader parent = owner.getClass().getClassLoader();
		File location = AmazingTitles.EXTENSIONS_FOLDER;
		location.mkdirs();
		File[] extensions = location.listFiles();
		if (extensions == null) return;
		for (File var : extensions) {
			if (!var.getName().endsWith(".jar")) continue;
			AmazingExtension extension = getExtension(parent, var);
			if (extension != null) {
				AmazingTitles.loadExtension(extension);
			}
		}
	}
	
	public static AmazingExtension getExtension(ClassLoader parent, File file) {
		if (!file.getName().endsWith(".jar")) return null;
		try (JarFile jar = new JarFile(file)) {
			ZipEntry document = jar.getEntry("extension.yml");
			try (InputStream stream = jar.getInputStream(document)) {
				Scanner s = new Scanner(stream).useDelimiter("\\A");
				String result = s.hasNext() ? s.next() : "";
				String main = result.replace("Class:", "").replace(" ", "");
				Enumeration<JarEntry> e = jar.entries();
				URL[] urls = {new URL("jar:file:" + file.getPath() + "!/")};
				try (URLClassLoader cl = new URLClassLoader(urls, parent)) {
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
							AmazingExtension extension = (AmazingExtension) object;
							AmazingExtension finalExtension = new AmazingExtension() {
								@Override
								public String extension_name() {
									return extension.extension_name();
								}
								
								@Override
								public void load() {
									extension.load();
								}
								
								@Override
								public void unload() {
									extension.unload();
								}
								
								@Override
								public File getAsFile() {
									return file;
								}
							};
							return finalExtension;
						}
					}
				} catch (ClassNotFoundException | InvocationTargetException | NoSuchMethodException |
				         InstantiationException | IllegalAccessException ignore) {}
			}
		} catch (IOException ignore) {}
		return null;
	}
	
	public static void loadDefaultAnimations() {
		
		AnimationBuilder none = new AnimationBuilder(null, AnimationType.LIGHT, false);
		none.setComponentArguments(ComponentArguments.create("Text is null", "SubText is null", BarColor.WHITE, 20, 20, DisplayType.TITLE));
		none.setFramesBuilder((arguments, args) -> new LinkedList<String>() {{
			add(ColorTranslator.colorize(arguments.getMainText()));
		}});
		none.register("NONE");
		
		AnimationBuilder symbolWrap = new AnimationBuilder(null, AnimationType.REPEATING, false, "Symbol(Text)");
		symbolWrap.setComponentArguments(ComponentArguments.create("Text is null", "SubText is null", BarColor.WHITE, 20, 10, DisplayType.TITLE));
		symbolWrap.setFramesBuilder((arguments, args) -> {
			LinkedList<String> frames = new LinkedList<>();
			String symbol = " " + args[0] + "&r ";
			String input = arguments.getMainText();
			frames.add(ColorTranslator.colorize(input));
			frames.add(ColorTranslator.colorize(symbol + input + symbol));
			return frames;
		});
		symbolWrap.register("FLASHING_SYMBOL_WRAP");
		
		AnimationBuilder rainbow = new AnimationBuilder(null, AnimationType.REPEATING, true);
		rainbow.setComponentArguments(ComponentArguments.create("Text is null", "SubText is null", BarColor.WHITE, 20, 20, DisplayType.TITLE));
		rainbow.setFramesBuilder((arguments, args) -> {
			LinkedList<String> frames = new LinkedList<>();
			String red = "#FF2424";
			String blue = "#002AFF";
			String green = "#00FF08";
			String smoothed = "          " + arguments.getMainText() + "          ";
			int length = smoothed.length();
			int withGradient = arguments.getMainText().length()*17;
			int start = 10*17;
			/*
			 * Red (Blue ->) Green
			 * */
			for (int i = 0; i <= length; i++) {
				String in = smoothed.substring(0, i);
				String out = smoothed.substring(i);
				String var = "<" + red + ">&l" + in + "</" + blue + "><" + blue + ">&l" + out + "</" + green + ">";
				frames.add(ColorTranslator.colorize(var).substring(start).substring(0, withGradient));
			}
			/*
			 * Green (Red ->) Blue
			 * */
			for (int i = 0; i <= length; i++) {
				String in = smoothed.substring(0, i);
				String out = smoothed.substring(i);
				String var = "<" + green + ">&l" + in + "</" + red + "><" + red + ">&l" + out + "</" + blue + ">";
				frames.add(ColorTranslator.colorize(var).substring(start).substring(0, withGradient));
			}
			/*
			 * Blue (Green ->) Red
			 * */
			for (int i = 0; i <= length; i++) {
				String in = smoothed.substring(0, i);
				String out = smoothed.substring(i);
				String var = "<" + blue + ">&l" + in + "</" + green + "><" + green + ">&l" + out + "</" + red + ">";
				frames.add(ColorTranslator.colorize(var).substring(start).substring(0, withGradient));
			}
			return frames;
		
		});
		rainbow.register("RAINBOW");
		
		AnimationBuilder waves = new AnimationBuilder(null, AnimationType.REPEATING, true, "Color1(HEX/Legacy)", "Color2(HEX/Legacy)");
		waves.setComponentArguments(ComponentArguments.create("Text is null", "SubText is null", BarColor.WHITE, 10, 20, DisplayType.TITLE));
		waves.setFramesBuilder((arguments, args) -> {
			LinkedList<String> frames = new LinkedList<>();
			String input = arguments.getMainText();
			String color1 = (String) args[0];
			String color2 = (String) args[1];
			String smoothed = "          " + input + "          ";
			int length = smoothed.length();
			int withGradient = input.length()*17;
			int start = 10*17;
			/*
			 * Red (Blue ->) Green
			 * */
			for (int i = 0; i <= length; i++) {
				String in = smoothed.substring(0, i);
				String out = smoothed.substring(i);
				String var = "<" + color1 + ">&l" + in + "</" + color2 + "><" + color2 + ">&l" + out + "</" + color1 + ">";
				frames.add(ColorTranslator.colorize(var).substring(start).substring(0, withGradient));
			}
			/*
			 * Green (Red ->) Blue
			 * */
			for (int i = 0; i <= length; i++) {
				String in = smoothed.substring(0, i);
				String out = smoothed.substring(i);
				String var = "<" + color2 + ">&l" + in + "</" + color1 + "><" + color1 + ">&l" + out + "</" + color2 + ">";
				frames.add(ColorTranslator.colorize(var).substring(start).substring(0, withGradient));
			}
			/*
			 * Blue (Green ->) Red
			 * */
			for (int i = 0; i <= length; i++) {
				String in = smoothed.substring(0, i);
				String out = smoothed.substring(i);
				String var = "<" + color1 + ">&l" + in + "</" + color2 + "><" + color2 + ">&l" + out + "</" + color1 + ">";
				frames.add(ColorTranslator.colorize(var).substring(start).substring(0, withGradient));
			}
			return frames;
		});
		waves.register("WAVES");
		
		AnimationBuilder bounce = new AnimationBuilder(null, AnimationType.REPEATING, true, "Color1(Hex/Legacy)", "Color2(Hex/Legacy)");
		bounce.setComponentArguments(ComponentArguments.create("Text is null", "SubText is null", BarColor.WHITE, 10, 20, DisplayType.TITLE));
		bounce.setFramesBuilder((arguments, args) -> {
			LinkedList<String> frames = new LinkedList<>();
			String input = arguments.getMainText();
			String smoothed = "          " + input + "          ";
			String color1 = (String) args[0];
			String color2 = (String) args[1];
			int length = smoothed.length();
			int withGradient = input.length()*17;
			int start = 10*17;
			for (int i = 0; i <= length; i++) {
				String to = smoothed.substring(0, i);
				String from = smoothed.substring(i);
				if (to.length() == 1 || from.length() == 1) continue;
				String formatted = "<" + color1 + ">&l" + to + "</" + color2 + ">" + "<" + color2 + ">&l" + from + "</" + color1 + ">";
				frames.add(ColorTranslator.colorize(formatted).substring(start).substring(0, withGradient));
			}
			int revertSize = frames.size();
			for (int i = revertSize-1; i > -1; i--) {
				String reversed = frames.get(i);
				frames.add(reversed);
			}
			return frames;
		});
		bounce.register("BOUNCE");
		
		AnimationBuilder flashing = new AnimationBuilder(null, AnimationType.REPEATING, false);
		flashing.setComponentArguments(ComponentArguments.create("Text is null", "SubText is null", BarColor.WHITE, 10, 10, DisplayType.TITLE));
		flashing.setFramesBuilder((arguments, args) -> {
			LinkedList<String> frames = new LinkedList<>();
			frames.add(ColorTranslator.colorize(arguments.getMainText()));
			frames.add("");
			return frames;
		});
		flashing.register("FLASHING");
		
		AnimationBuilder split = new AnimationBuilder(null, AnimationType.REPEATING, false);
		split.setComponentArguments(ComponentArguments.create("Text is null", "SubText is null", BarColor.WHITE, 10, 10, DisplayType.TITLE));
		split.setFramesBuilder((arguments, args) -> {
			String input = ColorTranslator.colorize(arguments.getMainText());
			return new LinkedList<>(Arrays.asList(input.split("%frame%")));
		});
		split.register("SPLIT");
		
		AnimationBuilder words_split = new AnimationBuilder(null, AnimationType.REPEATING, false);
		words_split.setComponentArguments(ComponentArguments.create("Text is null", "SubText is null", BarColor.WHITE, 10, 10, DisplayType.TITLE));
		words_split.setFramesBuilder((arguments, args) -> {
			String input = ColorTranslator.colorize(arguments.getMainText());
			return new LinkedList<>(Arrays.asList(input.split(" ")));
		});
		words_split.register("WORDS_SPLIT");
		
		AnimationBuilder from_left = new AnimationBuilder(null, AnimationType.FADE_IN, false);
		from_left.setComponentArguments(ComponentArguments.create("Text is null", "SubText is null", BarColor.WHITE, 20, 20, DisplayType.TITLE));
		from_left.setFramesBuilder((arguments, args) -> {
			LinkedList<String> frames = new LinkedList<>();
			String input = arguments.getMainText();
			int lastSpaces = 180;
			StringBuilder spaces = new StringBuilder();
			for (int i = 0; i < lastSpaces; i++) {
				spaces.append(" ");
			}
			for (int i = 30; i > -1; i--) {
				int newSpaces = lastSpaces-(i*5);
				String formattedSpaces = spaces.substring(newSpaces);
				frames.add(ColorTranslator.colorize(input + formattedSpaces));
			}
			frames.add(ColorTranslator.colorize(input));
			return frames;
		});
		from_left.register("FROM_LEFT");
		
		AnimationBuilder from_right = new AnimationBuilder(null, AnimationType.FADE_IN, false);
		from_right.setComponentArguments(ComponentArguments.create("Text is null", "SubText is null", BarColor.WHITE, 20, 20, DisplayType.TITLE));
		from_right.setFramesBuilder((arguments, args) -> {
			LinkedList<String> frames = new LinkedList<>();
			String input = arguments.getMainText();
			int lastSpaces = 180;
			StringBuilder spaces = new StringBuilder();
			for (int i = 0; i < lastSpaces; i++) {
				spaces.append(" ");
			}
			for (int i = 30; i > -1; i--) {
				int newSpaces = lastSpaces-(i*5);
				String formattedSpaces = spaces.substring(newSpaces);
				frames.add(ColorTranslator.colorize(formattedSpaces + input));
			}
			frames.add(ColorTranslator.colorize(input));
			return frames;
		});
		from_right.register("FROM_RIGHT");
		
		AnimationBuilder fade_in = new AnimationBuilder(null, AnimationType.FADE_IN, false);
		fade_in.setComponentArguments(ComponentArguments.create("Text is null", "SubText is null", BarColor.WHITE, 20, 20, DisplayType.TITLE));
		fade_in.setFramesBuilder((arguments, args) -> {
			LinkedList<String> frames = new LinkedList<>();
			List<String> chars = ColorTranslator.charactersWithColors(ColorTranslator.colorize(arguments.getMainText()));
			StringBuilder frameBuilder = new StringBuilder();
			for (String var : chars) {
				frameBuilder.append(var);
				frames.add(frameBuilder.toString());
			}
			return frames;
		});
		fade_in.register("FADE_IN");
		
		AnimationBuilder fade_in_writer = new AnimationBuilder(null, AnimationType.FADE_IN, false, "Writer(Single Word)");
		fade_in_writer.setComponentArguments(ComponentArguments.create("Text is null", "SubText is null", BarColor.WHITE, 20, 20, DisplayType.TITLE));
		fade_in_writer.setFramesBuilder((arguments, args) -> {
			LinkedList<String> frames = new LinkedList<>();
			String symbol = ColorTranslator.colorize((String) args[0]);
			String colorizedInput = ColorTranslator.colorize(arguments.getMainText());
			List<String> chars = ColorTranslator.charactersWithColors(colorizedInput);
			StringBuilder frameBuilder = new StringBuilder();
			frames.add(symbol);
			for (String var : chars) {
				frameBuilder.append(var);
				frames.add(frameBuilder.toString() + symbol);
			}
			frames.add(colorizedInput);
			return frames;
		});
		fade_in_writer.register("FADE_IN_WRITER");
		
		AnimationBuilder from_both_sides = new AnimationBuilder(null, AnimationType.FADE_IN, false);
		from_both_sides.setComponentArguments(ComponentArguments.create("Text is null", "SubText is null", BarColor.WHITE, 20, 20, DisplayType.TITLE));
		from_both_sides.setFramesBuilder((arguments, args) -> {
			LinkedList<String> frames = new LinkedList<>();
			String input = arguments.getMainText();
			int lastSpaces = 180;
			StringBuilder spaces = new StringBuilder();
			for (int i = 0; i < lastSpaces; i++) {
				spaces.append(" ");
			}
			String pre = input.substring(0, input.length()/2);
			String aft = input.substring(input.length()/2);
			for (int i = 30; i > -1; i--) {
				int newSpaces = lastSpaces-(i*6);
				String formattedSpaces = spaces.substring(newSpaces);
				String format = pre + formattedSpaces + aft;
				frames.add(ColorTranslator.colorize(format));
			}
			return frames;
		});
		from_both_sides.register("FROM_BOTH_SIDES");
		
		AnimationBuilder pulsing = new AnimationBuilder(null, AnimationType.REPEATING, true, "Color1(Hex/Legacy)", "Color2(Hex/Legacy)");
		pulsing.setComponentArguments(ComponentArguments.create("Text is null", "SubText is null", BarColor.WHITE, 20, 20, DisplayType.TITLE));
		pulsing.setFramesBuilder((arguments, args) -> {
			LinkedList<String> frames = new LinkedList<>();
			String input = arguments.getMainText();
			String color1 = (String) args[0];
			String color2 = (String) args[1];
			Color from;
			Color to;
			if (color1.startsWith("#")) {
				from = Color.decode(color1);
			} else if (color1.startsWith("&")) {
				from = ColorTranslator.fromChatColor(ChatColor.getByChar(color1.charAt(1)));
			} else from = Color.WHITE;
			if (color2.startsWith("#")) {
				to = Color.decode(color2);
			} else if (color2.startsWith("&")) {
				to = ColorTranslator.fromChatColor(ChatColor.getByChar(color2.charAt(1)));
			} else to = Color.BLACK;
			int r_max = Math.max(from.getRed(), to.getRed());
			int g_max = Math.max(from.getGreen(), to.getGreen());
			int b_max = Math.max(from.getBlue(), to.getBlue());
			int r_min = Math.min(from.getRed(), to.getRed());
			int g_min = Math.min(from.getGreen(), to.getGreen());
			int b_min = Math.min(from.getBlue(), to.getBlue());
			int r_total = r_max - r_min;
			int g_total = g_max - g_min;
			int b_total = b_max - b_min;
			int total_max = 0;
			if (r_total > total_max) total_max = r_total;
			if (g_total > total_max) total_max = g_total;
			if (b_total > total_max) total_max = b_total;
			int lastMinR = r_min;
			int lastMinG = g_min;
			int lastMinB = b_min;
			int lastMaxR = r_max;
			int lastMaxG = g_max;
			int lastMaxB = b_max;
			for (int i = 0; i <= total_max; i++) {
				int r = lastMinR;
				int g = lastMinG;
				int b = lastMinB;
				if (lastMinR < r_max) {
					r = (lastMinR = lastMinR+5);
				}
				if (lastMinG < g_max) {
					g = (lastMinG = lastMinG+5);
				}
				if (lastMinB < b_max) {
					b = (lastMinB = lastMinB+5);
				}
				if (r > r_max) r = r_max;
				if (g > g_max) g = g_max;
				if (b > b_max) b = b_max;
				Color c = new Color(r, g, b);
				String format = ColorTranslator.fromColor(c) + input;
				frames.add(ColorTranslator.colorize(format));
				if (r == r_max && g == g_max & b == b_max) {break;}
			}
			for (int i = 0; i <= total_max-1; i++) {
				int r = lastMaxR;
				int g = lastMaxG;
				int b = lastMaxB;
				if (lastMaxR > r_min) {
					r = (lastMaxR = lastMaxR-5);
				}
				if (lastMaxG > g_min) {
					g = (lastMaxG = lastMaxG-5);
				}
				if (lastMaxB > b_min) {
					b = (lastMaxB = lastMaxB-5);
				}
				if (r < r_min) r = r_min;
				if (g < g_min) g = g_min;
				if (b < b_min) b = b_min;
				Color c = new Color(r, g, b);
				String format = ColorTranslator.fromColor(c) + input;
				frames.add(ColorTranslator.colorize(format));
				if (r == r_min && g == g_min & b == b_min) {break;}
			}
			return frames;
		});
		pulsing.register("PULSING");
		
	}

	public static String getVersion() {
		final String packageName = Bukkit.getServer().getClass().getPackage().getName();
		return packageName.substring(packageName.lastIndexOf('.') + 1);
	}

	public static String getNewVersion() {
		return Bukkit.getBukkitVersion();
	}
	
}
