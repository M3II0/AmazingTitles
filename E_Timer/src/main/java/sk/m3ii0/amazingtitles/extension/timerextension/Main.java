package sk.m3ii0.amazingtitles.extension.timerextension;

import sk.m3ii0.amazingtitles.api.AmazingTitlesAPI;
import sk.m3ii0.amazingtitles.api.objects.AmazingTitleExtension;
import sk.m3ii0.amazingtitles.code.colors.ColorTranslator;

import java.util.ArrayList;
import java.util.List;

public class Main implements AmazingTitleExtension {
	
	@Override
	public void load() {
		
		AmazingTitlesAPI.getApi().createAndRegister("EXTENSION_TIMER_DEFAULT", false, true, true, (type, input, args) -> {
			List<String> frames = new ArrayList<>();
			String duration = (String) args[0];
			int[] format = readFormat(duration);
			int counts = 0;
			if (format[3] > 0) counts = 4;
			if (format[2] > 0) counts = 3;
			if (format[1] > 0) counts = 2;
			if (format[0] > 0) counts = 1;
			if (counts == 0) {
				return new ArrayList<String>() {{
					add("0 is not an option");
				}};
			}
			int[] counters = new int[counts];
			for (int i = 0; i < counts; i++) {
				counters[i] = format[i];
			}
			while (true) {
				StringBuilder timer = new StringBuilder();
				for (int i = counts-1; i > -1; i--) {
					int number = counters[i];
					if (number > 0) {
						timer.append(number).append(":");
					}
				}
				if (timer.toString().isEmpty()) {
					break;
				}
				timer = new StringBuilder(timer.substring(0, timer.length() - 1));
				frames.add(ColorTranslator.colorize(input.replace("{timer}", timer.toString())));
				int seconds = counters[0];
				if (seconds <= 0) {
					if (counts > 1) {
						int minutes = counters[1];
						if (minutes <= 0) {
							if (counts > 2) {
								int hours = counters[2];
								if (hours <= 0) {
									if (counts > 3) {
										int days = counters[3];
										if (days <= 0) {
											break;
										} else {
											counters[3] = days-1;
											counters[2] = 23;
											counters[1] = 59;
											counters[0] = 59;
										}
									} else {
										break;
									}
								} else {
									counters[2] = hours-1;
									counters[1] = 59;
									counters[0] = 59;
								}
							} else {
								break;
							}
						} else {
							counters[1] = minutes-1;
							counters[0] = 59;
						}
					} else {
						break;
					}
				} else {
					counters[0] = seconds-1;
				}
			}
			return frames;
		}, "<Duration (Visit wiki for more)>");
	
	}
	
	/*
	* 10d,10h,10m,10s
	* */
	private int[] readFormat(String input) {
		int[] format = new int[4];
		for (String var : input.split(",")) {
			String copy = var.substring(0, var.length()-1);
			if (var.endsWith("d")) {
				format[3] = Integer.parseInt(copy);
			}
			if (var.endsWith("h")) {
				format[2] = Integer.parseInt(copy);
			}
			if (var.endsWith("m")) {
				format[1] = Integer.parseInt(copy);
			}
			if (var.endsWith("s")) {
				format[0] = Integer.parseInt(copy);
			}
		}
		return format;
	}
	
}