package sk.m3ii0.amazingtitles.code.internal.utils;

import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TextComponentBuilder {
	
	private final List<BaseComponent> baseComponents = new ArrayList<>();
	
	public TextComponentBuilder() {}
	
	public TextComponentBuilder appendLegacy(String legacy) {
		baseComponents.addAll(Arrays.asList(TextComponent.fromLegacyText(ColorTranslator.colorize(legacy))));
		return this;
	}
	
	public TextComponentBuilder appendLegacy(String legacy, String legacyHover) {
		HoverEvent hover = new HoverEvent(HoverEvent.Action.SHOW_TEXT, TextComponent.fromLegacyText(ColorTranslator.colorize(legacyHover)));
		for (BaseComponent var : TextComponent.fromLegacyText(ColorTranslator.colorize(legacy))) {
			var.setHoverEvent(hover);
			baseComponents.add(var);
		}
		return this;
	}
	
	public TextComponentBuilder appendLegacy(String legacy, ClickEvent.Action clickAction, String clickValue) {
		ClickEvent click = new ClickEvent(clickAction, clickValue);
		for (BaseComponent var : TextComponent.fromLegacyText(ColorTranslator.colorize(legacy))) {
			var.setClickEvent(click);
			baseComponents.add(var);
		}
		return this;
	}
	
	public TextComponentBuilder appendLegacy(String legacy, String legacyHover, ClickEvent.Action clickAction, String clickValue) {
		HoverEvent hover = new HoverEvent(HoverEvent.Action.SHOW_TEXT, TextComponent.fromLegacyText(ColorTranslator.colorize(legacyHover)));
		ClickEvent click = new ClickEvent(clickAction, clickValue);
		for (BaseComponent var : TextComponent.fromLegacyText(ColorTranslator.colorize(legacy))) {
			var.setHoverEvent(hover);
			var.setClickEvent(click);
			baseComponents.add(var);
		}
		return this;
	}
	
	public BaseComponent[] createMessage() {
		return baseComponents.toArray(new BaseComponent[0]);
	}
	
}
