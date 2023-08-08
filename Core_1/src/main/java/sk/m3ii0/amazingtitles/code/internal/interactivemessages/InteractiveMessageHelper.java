package sk.m3ii0.amazingtitles.code.internal.interactivemessages;

import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import sk.m3ii0.amazingtitles.code.internal.utils.ColorTranslator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InteractiveMessageHelper {
	
	private static final Pattern match = Pattern.compile("<(.*?)>(.*?)</>");
	
	public static BaseComponent[] getMessageFromRaw(String text) {
		List<BaseComponent> components = new ArrayList<>();
		StringBuilder builder = new StringBuilder();
		for (char character : text.toCharArray()) {
			builder.append(character);
			String actual = builder.toString();
			Matcher matcher = match.matcher(actual);
			if (matcher.find()) {
				String[] before = text.split(matcher.group(1));
				String last = before[0].replaceAll("<$", "");
				if (!last.isEmpty()) {
					components.addAll(Arrays.asList(TextComponent.fromLegacyText(ColorTranslator.colorize(last))));
					text = text.replace(actual, "");
					System.out.println(actual);
				}
				String arguments = matcher.group(1);
				String between = matcher.group(2);
				ClickEvent clickEvent = null;
				HoverEvent hoverEvent = null;
				for (String arg : arguments.split(",")) {
					if (arg.split("=").length != 2) continue;
					String key = arg.split("=")[0];
					String val = arg.split("=")[1];
					if (key.equalsIgnoreCase("HOVER")) {
						hoverEvent = new HoverEvent(HoverEvent.Action.SHOW_TEXT, TextComponent.fromLegacyText(ColorTranslator.colorize(val)));
					}
					if (key.equalsIgnoreCase("OPEN_URL")) {
						clickEvent = new ClickEvent(ClickEvent.Action.OPEN_URL, val);
					}
					if (key.equalsIgnoreCase("SUGGEST_COMMAND")) {
						clickEvent = new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, val);
					}
					if (key.equalsIgnoreCase("RUN_COMMAND")) {
						clickEvent = new ClickEvent(ClickEvent.Action.RUN_COMMAND, val);
					}
				}
				for (BaseComponent var : TextComponent.fromLegacyText(ColorTranslator.colorize(between))) {
					if (clickEvent != null) var.setClickEvent(clickEvent);
					if (hoverEvent != null) var.setHoverEvent(hoverEvent);
					components.add(var);
				}
				builder = new StringBuilder();
			}
		}
		components.addAll(Arrays.asList(TextComponent.fromLegacyText(ColorTranslator.colorize(builder.toString()))));
		return components.toArray(new BaseComponent[0]);
	}
	
}
