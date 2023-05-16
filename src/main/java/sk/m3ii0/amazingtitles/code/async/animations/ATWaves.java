package sk.m3ii0.amazingtitles.code.async.animations;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;
import sk.m3ii0.amazingtitles.code.AmazingTitles;
import sk.m3ii0.amazingtitles.code.async.AmazingTitle;
import sk.m3ii0.amazingtitles.code.colors.ColorTranslator;
import sk.m3ii0.amazingtitles.code.colors.ColorUtils;

import java.awt.*;
import java.util.HashSet;
import java.util.Set;

public class ATWaves implements AmazingTitle {
	
	private BukkitTask task;
	private Set<Player> viewers = new HashSet<>();
	private final String subTitle;
	private final int speed;
	private final int duration;
	
	private int frameCounter = 0;
	private int tickCounter = 0;
	private int durationCounter = 0;
	
	public ATWaves(String title, String color1, String color2) {
		this(title, "", color1, color2, 1, 10);
	}
	
	public ATWaves(String title, String color1, String color2, String subTitle) {
		this(title, subTitle, color1, color2, 1, 10);
	}
	
	public ATWaves(String title, String subTitle, String color1, String color2, int duration) {
		this(title, subTitle, color1, color2, 1, duration);
	}
	
	public ATWaves(String title, Color color1, Color color2) {
		this(title, "", ColorUtils.hexFromColor(color1), ColorUtils.hexFromColor(color2), 1, 10);
	}
	
	public ATWaves(String title, Color color1, Color color2, String subTitle) {
		this(title, subTitle, ColorUtils.hexFromColor(color1), ColorUtils.hexFromColor(color2), 1, 10);
	}
	
	public ATWaves(String title, String subTitle, Color color1, Color color2, int duration) {
		this(title, subTitle, ColorUtils.hexFromColor(color1), ColorUtils.hexFromColor(color2), 1, duration);
	}
	
	public ATWaves(String title, String subTitle, Color color1, Color color2, int speed, int duration) {
		this(title, subTitle, ColorUtils.hexFromColor(color1), ColorUtils.hexFromColor(color2), speed, duration);
	}
	
	public ATWaves(String title, String subtitle, String color1, String color2, int speed, int duration) {
		int length = title.length();
		/*
		 * Red (Blue ->) Green
		 * */
		for (int i = 0; i <= length; i++) {
			String in = title.substring(0, i);
			String out = title.substring(i);
			String format = "<" + color1 + ">&l" + in + "</" + color2 + "><" + color2 + ">&l" + out + "</" + color1 + ">";
			frames.add(ColorTranslator.parse(format));
		}
		/*
		 * Green (Red ->) Blue
		 * */
		for (int i = 0; i <= length; i++) {
			String in = title.substring(0, i);
			String out = title.substring(i);
			String format = "<" + color1 + ">&l" + in + "</" + color1 + "><" + color1 + ">&l" + out + "</" + color2 + ">";
			frames.add(ColorTranslator.parse(format));
		}
		/*
		 * Blue (Green ->) Red
		 * */
		for (int i = 0; i <= length; i++) {
			String in = title.substring(0, i);
			String out = title.substring(i);
			String format = "<" + color2 + ">&l" + in + "</" + color1 + "><" + color1 + ">&l" + out + "</" + color1 + ">";
			frames.add(ColorTranslator.parse(format));
		}
		this.subTitle = ColorTranslator.parse(subtitle);
		this.speed = speed;
		this.duration = duration;
	}
	
	@Override
	public void streamToAll() {
		for (Player p : Bukkit.getOnlinePlayers()) {
			viewers.add(p);
			AmazingTitles.getTitleManager().setTitleFor(p, this);
		}
		runTask();
	}
	
	@Override
	public void sendTo(Player... players) {
		for (Player p : players) {
			viewers.add(p);
			AmazingTitles.getTitleManager().setTitleFor(p, this);
		}
		runTask();
	}
	
	@Override
	public void removeFor(Player... player) {
		for (Player p : player) {
			if (viewers.remove(p)) {
				p.resetTitle();
				AmazingTitles.getTitleManager().unsetTitleFor(p);
			}
		}
	}
	
	private void runTask() {
		if (task == null) {
			task = Bukkit.getScheduler().runTaskTimerAsynchronously(AmazingTitles.getInstance(), () -> {
				if (duration == durationCounter) {
					delete();
					return;
				}
				++tickCounter;
				if (tickCounter%speed==0) {
					++frameCounter;
				}
				if (frameCounter >= frames.size()) frameCounter = 0;
				String frame = frames.get(frameCounter);
				for (Player p : viewers) {
					p.sendTitle(frame, subTitle, 0, 5, 0);
				}
				if (tickCounter == 20) {
					tickCounter = 0;
					++durationCounter;
				}
			}, 0, 1);
		}
	}
	
	@Override
	public void delete() {
		if (task != null) {
			task.cancel();
			task = null;
		}
		removeFor(viewers.toArray(new Player[0]));
	}
	
}
