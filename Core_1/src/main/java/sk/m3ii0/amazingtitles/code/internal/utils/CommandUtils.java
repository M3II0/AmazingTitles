package sk.m3ii0.amazingtitles.code.internal.utils;

import java.util.ArrayList;
import java.util.List;

public class CommandUtils {
	
	public static List<String> copyAllStartingWith(List<String> selection, String starting) {
		List<String> result = new ArrayList<>();
		for (String var : selection) {
			int size = var.length();
			boolean equal = true;
			for (int i = 0; i < selection.size(); i++) {
				if (i >= size) {
					equal = false;
				}
				char one = var.charAt(i);
				char two = starting.charAt(i);
				if (one != two) {
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
