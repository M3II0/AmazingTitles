package sk.m3ii0.amazingtitles.code.internal.commands.commandreaders.subs;

import net.md_5.bungee.api.chat.*;
import org.bukkit.command.CommandSender;
import sk.m3ii0.amazingtitles.code.api.AmazingTitles;
import sk.m3ii0.amazingtitles.code.api.interfaces.AmazingExtension;
import sk.m3ii0.amazingtitles.code.internal.Booter;
import sk.m3ii0.amazingtitles.code.internal.commands.commandreaders.CommandHandler;
import sk.m3ii0.amazingtitles.code.internal.commands.commandreaders.HandlerType;
import sk.m3ii0.amazingtitles.code.internal.commands.commandreaders.InternalHandlerType;
import sk.m3ii0.amazingtitles.code.internal.loaders.PluginLoader;
import sk.m3ii0.amazingtitles.code.internal.utils.ColorTranslator;
import sk.m3ii0.amazingtitles.code.internal.utils.CommandUtils;
import sk.m3ii0.amazingtitles.code.internal.utils.MessageUtils;
import sk.m3ii0.amazingtitles.code.internal.utils.TextComponentBuilder;

import java.io.File;
import java.util.*;

public class CHPluginActions implements CommandHandler {
	
	private static final File extensionsFolder = new File(Booter.getInstance().getDataFolder(), "Extensions");
	private static final Map<String, Integer> firstArgs = new HashMap<String, Integer>() {{
		put("extensionList", -1);
		put("loadExtension", 0);
		put("reloadExtension", 3);
		put("unloadExtension", 3);
		put("reloadExtensions", -1);
		put("unloadExtensions", -1);
		put("loadExtensions", -1);
		put("reload", -1);
	}};
	
	@Override
	public BaseComponent[] helpMessage() {
		if (ColorTranslator.isHexSupport()) {
			TextComponentBuilder builder = new TextComponentBuilder();
			builder.appendLegacy("\n<#a217ff>AmazingTitles ✎ </#ff7ae9> &fPlugin Actions\n");
			builder.appendLegacy(" &7> <#dedede>/at pluginActions</#c7c7c7>\n", "&{#ffa6fc}Click to suggest command", ClickEvent.Action.SUGGEST_COMMAND, "/at pluginActions ");
			builder.appendLegacy("§f");
			return builder.createMessage();
		}
		TextComponentBuilder builder = new TextComponentBuilder();
		builder.appendLegacy("\n&5AmazingTitles ✎ &fPlugin Actions\n");
		builder.appendLegacy(" &7> &7/at pluginActions\n", "&{#ffa6fc}Click to suggest command", ClickEvent.Action.SUGGEST_COMMAND, "/at pluginActions ");
		builder.appendLegacy("§f");
		return builder.createMessage();
	}
	
	@Override
	public String permission() {
		return "at.plugin";
	}
	
	@Override
	public HandlerType handlerType() {
		return new InternalHandlerType();
	}
	
	@Override
	public boolean readAndExecute(CommandSender s, String[] args) {
		try {
			String action = args[0];
			if (action.equalsIgnoreCase("extensionList")) {
				Set<String> extensions = AmazingTitles.getLoadedExtensionNames();
				if (extensions.size() == 0) {
					TextComponentBuilder hex = new TextComponentBuilder().appendLegacy("<#a217ff>AmazingTitles ✎ </#ff7ae9> &fYou don't have any extensions!");
					TextComponentBuilder legacy = new TextComponentBuilder().appendLegacy("&5AmazingTitles ✎ &fYou don't have any extensions!");
					BaseComponent[] message = MessageUtils.getCorrect(hex, legacy);
					s.spigot().sendMessage(message);
					return true;
				}
				int counter = 0;
				TextComponentBuilder hex = new TextComponentBuilder().appendLegacy("<#a217ff>AmazingTitles ✎ </#ff7ae9> ");
				for (String name : extensions) {
					hex.appendLegacy("&{#ffa6fc}" + name);
					if (counter < extensions.size()-1) hex.appendLegacy("&f, ");
					++counter;
				}
				counter = 0;
				TextComponentBuilder legacy = new TextComponentBuilder().appendLegacy("&5AmazingTitles ✎ ");
				for (String name : extensions) {
					legacy.appendLegacy("&d" + name);
					if (counter < extensions.size()-1) legacy.appendLegacy("&f, ");
					++counter;
				}
				s.spigot().sendMessage(MessageUtils.getCorrect(hex, legacy));
				return true;
			}
			if (action.equalsIgnoreCase("loadExtension")) {
				String extension = args[1];
				File file = new File(AmazingTitles.EXTENSIONS_FOLDER, extension);
				if (file != null) {
					AmazingExtension loader = PluginLoader.getExtension(Booter.getBooter().getClass().getClassLoader(), file);
					if (loader != null) {
						String took = Booter.getTookMs(() -> AmazingTitles.loadExtension(loader));
						TextComponentBuilder hex = new TextComponentBuilder().appendLegacy("<#a217ff>AmazingTitles ✎ </#ff7ae9> &fLoaded plugin in &{#ffa6fc}" + took + "&fms!");
						TextComponentBuilder legacy = new TextComponentBuilder().appendLegacy("&5AmazingTitles ✎ &fLoaded plugin &d" + took + "&fms!");
						BaseComponent[] message = MessageUtils.getCorrect(hex, legacy);
						s.spigot().sendMessage(message);
						return true;
					}
				}
				TextComponentBuilder hex = new TextComponentBuilder().appendLegacy("<#a217ff>AmazingTitles ✎ </#ff7ae9> &fError with loading extension! (Check extension file or contact the author)");
				TextComponentBuilder legacy = new TextComponentBuilder().appendLegacy("&5AmazingTitles ✎ &fError with loading extension! (Check extension file or contact the author)");
				BaseComponent[] message = MessageUtils.getCorrect(hex, legacy);
				s.spigot().sendMessage(message);
				return true;
			}
			if (action.equalsIgnoreCase("reloadExtension")) {
				String extension = args[1];
				AmazingExtension amazingExtension = AmazingTitles.unloadExtension(extension);
				if (amazingExtension == null) {
					TextComponentBuilder hex = new TextComponentBuilder().appendLegacy("<#a217ff>AmazingTitles ✎ </#ff7ae9> &fError with reloading extension! (Check extension file or contact the author)");
					TextComponentBuilder legacy = new TextComponentBuilder().appendLegacy("&5AmazingTitles ✎ &fError with reloading extension! (Check extension file or contact the author)");
					BaseComponent[] message = MessageUtils.getCorrect(hex, legacy);
					s.spigot().sendMessage(message);
					return true;
				}
				File file = new File(AmazingTitles.EXTENSIONS_FOLDER, amazingExtension.getAsFile().getName());
				if (!file.exists()) {
					TextComponentBuilder hex = new TextComponentBuilder().appendLegacy("<#a217ff>AmazingTitles ✎ </#ff7ae9> &fError with reloading extension! (Check extension file or contact the author)");
					TextComponentBuilder legacy = new TextComponentBuilder().appendLegacy("&5AmazingTitles ✎ &fError with reloading extension! (Check extension file or contact the author)");
					BaseComponent[] message = MessageUtils.getCorrect(hex, legacy);
					s.spigot().sendMessage(message);
					return true;
				}
				AmazingExtension loader = PluginLoader.getExtension(Booter.getBooter().getClass().getClassLoader(), file);
				if (loader == null) {
					TextComponentBuilder hex = new TextComponentBuilder().appendLegacy("<#a217ff>AmazingTitles ✎ </#ff7ae9> &fError with reloading extension! (Check extension file or contact the author)");
					TextComponentBuilder legacy = new TextComponentBuilder().appendLegacy("&5AmazingTitles ✎ &fError with reloading extension! (Check extension file or contact the author)");
					BaseComponent[] message = MessageUtils.getCorrect(hex, legacy);
					s.spigot().sendMessage(message);
					return true;
				}
				String took = Booter.getTookMs(() -> AmazingTitles.loadExtension(loader));
				TextComponentBuilder hex = new TextComponentBuilder().appendLegacy("<#a217ff>AmazingTitles ✎ </#ff7ae9> &fReloaded extension in &{#ffa6fc}" + took + "&fms!");
				TextComponentBuilder legacy = new TextComponentBuilder().appendLegacy("&5AmazingTitles ✎ &fReloaded extension &d" + took + "&fms!");
				BaseComponent[] message = MessageUtils.getCorrect(hex, legacy);
				s.spigot().sendMessage(message);
				return true;
			}
			if (action.equalsIgnoreCase("unloadExtension")) {
				String extension = args[1];
				AmazingExtension amazingExtension = AmazingTitles.unloadExtension(extension);
				if (amazingExtension == null) {
					TextComponentBuilder hex = new TextComponentBuilder().appendLegacy("<#a217ff>AmazingTitles ✎ </#ff7ae9> &fError with unloading extension! (Check extension file or contact the author)");
					TextComponentBuilder legacy = new TextComponentBuilder().appendLegacy("&5AmazingTitles ✎ &fError with unloading extension! (Check extension file or contact the author)");
					BaseComponent[] message = MessageUtils.getCorrect(hex, legacy);
					s.spigot().sendMessage(message);
					return true;
				}
				TextComponentBuilder hex = new TextComponentBuilder().appendLegacy("<#a217ff>AmazingTitles ✎ </#ff7ae9> &fUnloaded extension in &{#ffa6fc}" + amazingExtension.extension_name() + "&f!");
				TextComponentBuilder legacy = new TextComponentBuilder().appendLegacy("&5AmazingTitles ✎ &fUnloaded extension &d" + amazingExtension.extension_name() + "&f!");
				BaseComponent[] message = MessageUtils.getCorrect(hex, legacy);
				s.spigot().sendMessage(message);
				return true;
			}
			if (action.equalsIgnoreCase("loadExtensions")) {
				String took = Booter.getTookMs(() -> PluginLoader.loadExtensions(Booter.getBooter()));
				TextComponentBuilder hex = new TextComponentBuilder().appendLegacy("<#a217ff>AmazingTitles ✎ </#ff7ae9> &fLoaded extensions in &{#ffa6fc}" + took + "&fms!");
				TextComponentBuilder legacy = new TextComponentBuilder().appendLegacy("&5AmazingTitles ✎ &fLoaded extensions &d" + took + "&fms!");
				BaseComponent[] message = MessageUtils.getCorrect(hex, legacy);
				s.spigot().sendMessage(message);
				return true;
			}
			if (action.equalsIgnoreCase("reloadExtensions")) {
				String took = Booter.getTookMs(() -> {
					AmazingTitles.unloadAllExtensions();
					PluginLoader.loadExtensions(Booter.getBooter());
				});
				TextComponentBuilder hex = new TextComponentBuilder().appendLegacy("<#a217ff>AmazingTitles ✎ </#ff7ae9> &fReloaded extensions in &{#ffa6fc}" + took + "&fms!");
				TextComponentBuilder legacy = new TextComponentBuilder().appendLegacy("&5AmazingTitles ✎ &fReloaded extensions &d" + took + "&fms!");
				BaseComponent[] message = MessageUtils.getCorrect(hex, legacy);
				s.spigot().sendMessage(message);
				return true;
			}
			if (action.equalsIgnoreCase("unloadExtensions")) {
				String took = Booter.getTookMs(() -> AmazingTitles.unloadAllExtensions());
				TextComponentBuilder hex = new TextComponentBuilder().appendLegacy("<#a217ff>AmazingTitles ✎ </#ff7ae9> &fUnloaded extensions in &{#ffa6fc}" + took + "&fms!");
				TextComponentBuilder legacy = new TextComponentBuilder().appendLegacy("&5AmazingTitles ✎ &fUnloaded extensions &d" + took + "&fms!");
				BaseComponent[] message = MessageUtils.getCorrect(hex, legacy);
				s.spigot().sendMessage(message);
				return true;
			}
			if (action.equalsIgnoreCase("reload")) {
				Booter.getBooter().reload(s);
				return true;
			}
		} catch (Exception ignore) {
			return false;
		}
		return false;
	}
	
	@Override
	public List<String> readAndReturn(CommandSender s, String[] args) {
		if (args.length == 1) {
			return CommandUtils.copyAllStartingWith(new ArrayList<>(firstArgs.keySet()), args[0]);
		}
		int data = firstArgs.getOrDefault(args[0], -1);
		return CommandUtils.copyAllStartingWith(getParticipleArgument(data), args[args.length-1]);
	}
	
	private List<String> getParticipleArgument(int data) {
		if (data == 3) return new ArrayList<>(AmazingTitles.getLoadedExtensionNames());
		if (data == -1) return  Collections.singletonList("There are no more characters | Wrong command");
		List<String> array = new ArrayList<>();
		File[] files = extensionsFolder.listFiles();
		if (files == null) return new ArrayList<>();
		for (File var : files) {
			array.add(var.getName());
		}
		if (data == 0) {
			List<String> list = new ArrayList<>();
			for (String var : array) {
				if (!AmazingTitles.getLoadedExtensionFileNames().contains(var)) {
					list.add(var);
				}
			}
			return list;
		}
		if (data == 1) {
			return new ArrayList<>(AmazingTitles.getLoadedExtensionFileNames());
		}
		return array;
	}
	
}
