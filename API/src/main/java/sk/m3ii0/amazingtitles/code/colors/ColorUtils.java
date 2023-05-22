package sk.m3ii0.amazingtitles.code.colors;

import java.awt.*;

public class ColorUtils {
	
	public static String hexFromColor(Color color) {
		String hex = "#" + Integer.toHexString(color.getRGB());
		return hex.substring(0, 7);
	}
	
}
