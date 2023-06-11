package sk.m3ii0.amazingtitles.code.utils;

import java.util.ArrayList;
import java.util.List;

public class StringUtils {
	
	public static List<String> toColoredChars(String text) {
		List<String> result = new ArrayList<>();
		int counter = 0;
		String lastColorCode = "";
		boolean checkForCode = false;
		while (true) {
			if (counter < text.length()) {
				char var = text.charAt(counter);
				if (!checkForCode) {
					if (var == '§') {
						checkForCode = true;
						lastColorCode += var;
					} else {
						result.add(lastColorCode + var);
						lastColorCode = "";
					}
				} else {
					lastColorCode += var;
					checkForCode = false;
				}
				++counter;
			} else break;
		}
		return result;
	}
	
}
