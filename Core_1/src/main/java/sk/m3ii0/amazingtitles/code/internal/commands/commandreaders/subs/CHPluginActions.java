package sk.m3ii0.amazingtitles.code.internal.commands.commandreaders.subs;

import net.md_5.bungee.api.chat.*;
import org.bukkit.command.CommandSender;
import sk.m3ii0.amazingtitles.code.api.AmazingTitles;
import sk.m3ii0.amazingtitles.code.internal.Booter;
import sk.m3ii0.amazingtitles.code.internal.commands.commandreaders.CommandHandler;
import sk.m3ii0.amazingtitles.code.internal.commands.commandreaders.HandlerType;
import sk.m3ii0.amazingtitles.code.internal.commands.commandreaders.InternalHandlerType;
import sk.m3ii0.amazingtitles.code.internal.utils.ColorTranslator;
import sk.m3ii0.amazingtitles.code.internal.utils.CommandUtils;
import sk.m3ii0.amazingtitles.code.internal.utils.TextComponentBuilder;

import java.io.File;
import java.util.*;

public class CHPluginActions implements CommandHandler {
	
	private static final File extensionsFolder = new File(Booter.getInstance().getDataFolder(), "Extensions");
	private static final Map<String, Integer> firstArgs = new HashMap<String, Integer>() {{
		put("loadExtension", 0);
		put("reloadExtension", 1);
		put("unloadExtension", 1);
		put("reload", -1);
		put("reloadExtensions", -1);
		put("unloadExtensions", -1);
		put("extensionList", 2);
		put("loadExtensions", -1);
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
		return false;
	}
	
	@Override
	public List<String> readAndReturn(CommandSender s, String[] args) {
		if (args.length == 1) {
			return CommandUtils.copyAllStartingWith(new ArrayList<>(firstArgs.keySet()), args[0]);
		}
		int data = firstArgs.getOrDefault(args[0], -1);
		return getParticipleArgument(data);
	}
	
	private List<String> getParticipleArgument(int data) {
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
