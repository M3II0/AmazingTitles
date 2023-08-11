package sk.m3ii0.amazingtitles.code.internal.utils;

import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

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
	
}
