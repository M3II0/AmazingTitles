package sk.m3ii0.amazingtitles.code.internal.utils;

import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import sk.m3ii0.amazingtitles.code.internal.Booter;
import sk.m3ii0.amazingtitles.code.internal.commands.PluginCommand;
import sk.m3ii0.amazingtitles.code.internal.commands.commandreaders.CommandHandler;

import java.util.Map;

public class MessageUtils {
	
	public static BaseComponent[] quickCreate(String text) {
		return quickCreate(text, null, null, null);
	}
	
	public static BaseComponent[] quickCreate(String text, String hover) {
		return quickCreate(text, hover, null, null);
	}
	
	public static BaseComponent[] quickCreate(String text, ClickEvent.Action action, String value) {
		return quickCreate(text, null, action, value);
	}
	
	public static BaseComponent[] quickCreate(String text, String hover, ClickEvent.Action action, String value) {
		BaseComponent[] components = TextComponent.fromLegacyText(text);
		HoverEvent hoverEvent = null;
		if (hover != null) {
			hoverEvent = new HoverEvent(HoverEvent.Action.SHOW_TEXT, TextComponent.fromLegacyText(hover));
		}
		ClickEvent clickEvent = null;
		if (action != null && value != null) {
			clickEvent = new ClickEvent(action, value);
		}
		for (BaseComponent var : components) {
			if (hoverEvent != null) {
				var.setHoverEvent(hoverEvent);
			}
			if (clickEvent != null) {
				var.setClickEvent(clickEvent);
			}
		}
		return components;
	}
	
	private static String getPrefix() {
		if (ColorTranslator.isHexSupport()) {
			return ColorTranslator.colorize("<#a217ff>AmazingTitles ✎ </#ff7ae9> &f");
		}
		return ColorTranslator.colorize("&5AmazingTitles ✎ &f");
	}
	
	public static BaseComponent[] getPluginHelp() {
		if (ColorTranslator.isHexSupport()) {
			TextComponentBuilder builder = new TextComponentBuilder();
			builder.appendLegacy("\n");
			builder.appendLegacy(getPrefix() + "Help message\n");
			builder.appendLegacy("\n");
			builder.appendLegacy(" &{#ffa6fc}Available command handlers:\n");
			for (Map.Entry<String, CommandHandler> entry : PluginCommand.getHandlers().entrySet()) {
				String argument = entry.getKey();
				CommandHandler handler = entry.getValue();
				String permission = handler.permission();
				String type = handler.handlerType().id();
				builder.appendLegacy("   &f> <#dedede>" + argument + "</#c7c7c7>", "&{#ffa6fc}Click to suggest command\n&fType: &7" + type + "\n&fPermission: &7" + permission, ClickEvent.Action.SUGGEST_COMMAND, "/at " + argument + " ");
				builder.appendLegacy("\n");
			}
			builder.appendLegacy("\n");
			builder.appendLegacy(" &{#ffa6fc}Available redirections:\n");
			builder.appendLegacy("   &fClick here to open &{#8348c7}Wiki\n", "&{#ffa6fc}Click to open", ClickEvent.Action.OPEN_URL, "https://m3ii0.gitbook.io/amazingtitles-recode/");
			builder.appendLegacy("   &fClick here to open &{#2e52c7}Support\n", "&{#ffa6fc}Click to open", ClickEvent.Action.OPEN_URL, "https://discord.com/invite/Ms7uAAAtcT");
			builder.appendLegacy("   &fClick here to open &{#FFAA00}Spigot\n", "&{#ffa6fc}Click to open", ClickEvent.Action.OPEN_URL, "https://www.spigotmc.org/resources/109916/");
			builder.appendLegacy("§f");
			return builder.createMessage();
		}
		TextComponentBuilder builder = new TextComponentBuilder();
		builder.appendLegacy("\n");
		builder.appendLegacy(getPrefix() + "Help message\n");
		builder.appendLegacy("\n");
		builder.appendLegacy(" &5Available command handlers:\n");
		for (Map.Entry<String, CommandHandler> entry : PluginCommand.getHandlers().entrySet()) {
			String argument = entry.getKey();
			CommandHandler handler = entry.getValue();
			String permission = handler.permission();
			String type = handler.handlerType().id();
			builder.appendLegacy("   &f> &7" + argument, "&dClick to suggest command\n&fType: &7" + type + "\n&fPermission: &7" + permission, ClickEvent.Action.SUGGEST_COMMAND, "/at " + argument + " ");
			builder.appendLegacy("\n");
		}
		builder.appendLegacy("\n");
		builder.appendLegacy(" &5Available redirections:\n");
		builder.appendLegacy("   &fClick here to open &dWiki\n", "&{#ffa6fc}Click to open", ClickEvent.Action.OPEN_URL, "https://m3ii0.gitbook.io/amazingtitles-recode/");
		builder.appendLegacy("   &fClick here to open &bSupport\n", "&{#ffa6fc}Click to open", ClickEvent.Action.OPEN_URL, "https://discord.com/invite/Ms7uAAAtcT");
		builder.appendLegacy("   &fClick here to open &6Spigot\n", "&{#ffa6fc}Click to open", ClickEvent.Action.OPEN_URL, "https://www.spigotmc.org/resources/109916/");
		builder.appendLegacy("&f");
		return builder.createMessage();
	}
	
	public static BaseComponent[] getCorrect(TextComponentBuilder hex, TextComponentBuilder legacy) {
		if (ColorTranslator.isHexSupport()) return hex.createMessage();
		return legacy.createMessage();
	}
	
}
