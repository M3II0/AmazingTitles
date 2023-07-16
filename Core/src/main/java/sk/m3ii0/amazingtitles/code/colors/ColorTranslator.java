package sk.m3ii0.amazingtitles.code.colors;

import net.md_5.bungee.api.ChatColor;
import sk.m3ii0.amazingtitles.code.AmazingTitles;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ColorTranslator {
	
	private static final Pattern gradient = Pattern.compile("<(#[A-Za-z0-9]{6})>(.*?)</(#[A-Za-z0-9]{6})>");
	private static final Pattern legacyGradient = Pattern.compile("<(&[A-Za-z0-9])>(.*?)</(&[A-Za-z0-9])>");
	private static final Pattern rgb = Pattern.compile("&\\{(#......)}");
	
	public static String parse(String text) {
		if (!AmazingTitles.legacy()) {
			Matcher g = gradient.matcher(text);
			Matcher l = legacyGradient.matcher(text);
			Matcher r = rgb.matcher(text);
			while (g.find()) {
				Color start = Color.decode(g.group(1));
				String between = g.group(2);
				Color end = Color.decode(g.group(3));
				BeforeType[] types = BeforeType.detect(between);
				between = BeforeType.replaceColors(between);
				text = text.replace(g.group(0), rgbGradient(between, start, end, types));
			}
			while (l.find()) {
				char first = l.group(1).charAt(1);
				String between = l.group(2);
				char second = l.group(3).charAt(1);
				ChatColor firstColor = ChatColor.getByChar(first);
				ChatColor secondColor = ChatColor.getByChar(second);
				BeforeType[] types = BeforeType.detect(between);
				between = BeforeType.replaceColors(between);
				if (firstColor == null) firstColor = ChatColor.WHITE;
				if (secondColor == null) secondColor = ChatColor.WHITE;
				text = text.replace(l.group(0), rgbGradient(between, AmazingTitles.getFromChatColor(firstColor), AmazingTitles.getFromChatColor(secondColor), types));
			}
			while (r.find()) {
				ChatColor color = AmazingTitles.getFromColor(Color.decode(r.group(1)));
				text = text.replace(r.group(0), color + "");
			}
		}
		return ChatColor.translateAlternateColorCodes('&', text);
	}
	
	private static String rgbGradient(String str, Color from, Color to, BeforeType[] types) {
		final StringBuilder builder = new StringBuilder();
		final double[] red = linear(from.getRed(), to.getRed(), str.length());
		final double[] green = linear(from.getGreen(), to.getGreen(), str.length());
		final double[] blue = linear(from.getBlue(), to.getBlue(), str.length());
		StringBuilder before = new StringBuilder();
		for (BeforeType var : types) {
			before.append("§").append(var.getCode());
		}
		if (str.length() == 1) {
			return AmazingTitles.getFromColor(to) + before.toString() + str;
		}
		for (int i = 0; i < str.length(); i++) {
			builder.append(AmazingTitles.getFromColor(new Color((int) Math.round(red[i]), (int) Math.round(green[i]), (int) Math.round(blue[i])))).append(before).append(str.charAt(i));
		}
		return builder.toString();
	}
	
	private static double[] linear(double from, double to, int max) {
		final double[] res = new double[max];
		for (int i = 0; i < max; i++) {
			res[i] = from + i * ((to - from) / (max - 1));
		}
		return res;
	}
	
	public enum BeforeType {
		MIXED("k"),
		BOLD("l"),
		CROSSED("m"),
		UNDERLINED("n"),
		CURSIVE("o");
		BeforeType(String code) {
			this.code = code;
		}
		private final String code;
		public String getCode() {
			return code;
		}
		public static BeforeType[] detect(String text) {
			List<BeforeType> value = new ArrayList<>();
			boolean hasMix = text.contains("&k");
			boolean hasBold = text.contains("&l");
			boolean hasCrossed = text.contains("&m");
			boolean hasUnder = text.contains("&n");
			boolean hasCursive = text.contains("&o");
			if (hasMix) {
				value.add(MIXED);
			}
			if (hasBold) {
				value.add(BOLD);
			}
			if (hasCrossed) {
				value.add(CROSSED);
			}
			if (hasUnder) {
				value.add(UNDERLINED);
			}
			if (hasCursive) {
				value.add(CURSIVE);
			}
			return value.toArray(new BeforeType[0]);
		}
		public static String replaceColors(String text) {
			return text.replace("&k", "").replace("&m", "").replace("&n", "").replace("&o", "").replace("&l", "");
		}
	}
	
}