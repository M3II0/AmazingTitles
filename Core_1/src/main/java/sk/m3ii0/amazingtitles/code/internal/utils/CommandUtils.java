package sk.m3ii0.amazingtitles.code.internal.utils;

import java.util.ArrayList;
import java.util.List;

public class CommandUtils {
	
	public static List<String> copyAllStartingWith(List<String> selection, String starting) {
		List<String> result = new ArrayList<>();
		for (String var : selection) {
			int size = var.length();
			boolean equal = true;
			for (int i = 0; i < starting.length(); i++) {
				if (i >= size) {
					equal = false;
					break;
				}
				char one = var.charAt(i);
				char two = starting.charAt(i);
				if (Character.toLowerCase(one) != Character.toLowerCase(two)) {
					equal = false;
				}
			}
			if (equal) {
				result.add(var);
			}
		}
		return result;
	}
	
}
