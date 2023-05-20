package sk.m3ii0.amazingtitles.code.async.animations;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;
import sk.m3ii0.amazingtitles.code.AmazingTitles;
import sk.m3ii0.amazingtitles.code.async.AmazingTitle;
import sk.m3ii0.amazingtitles.code.colors.ColorTranslator;
import sk.m3ii0.amazingtitles.code.colors.ColorUtils;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ATOpen implements AmazingTitle {
	
	private BukkitTask task;
	private final Set<Player> viewers = new HashSet<>();
	private final String subTitle;
	private final int speed;
	private final int duration;
	private final List<String> frames = new ArrayList<>();
	
	private int frameCounter = 0;
	private int tickCounter = 0;
	private int durationCounter = 0;
	
	public ATOpen(String title) {
		this(title, "", "#ffffff", 1, 10);
	}
	
	public ATOpen(String title, String color) {
		this(title, "", color, 1, 10);
	}
	
	public ATOpen(String title, String subTitle, String color) {
		this(title, subTitle, color, 1, 10);
	}
	
	public ATOpen(String title, String subTitle, String color,int duration) {
		this(title, subTitle, color, 1, duration);
	}
	
	public ATOpen(String title, Color color) {
		this(title, "", ColorUtils.hexFromColor(color), 1, 10);
	}
	
	public ATOpen(String title, String subTitle, Color color) {
		this(title, subTitle, ColorUtils.hexFromColor(color), 1, 10);
	}
	
	public ATOpen(String title, String subTitle, Color color,int duration) {
		this(title, subTitle, ColorUtils.hexFromColor(color), 1, duration);
	}
	
	public ATOpen(String title, String subtitle, Color color, int speed, int duration) {
		this(title, subtitle, ColorUtils.hexFromColor(color), speed, duration);
	}
	
	public ATOpen(String title, String subtitle, String color, int speed, int duration) {
		int max = title.length();
		int min = 0;
		int mid = max/2;
		int looped = 0;
		while (true) {
			boolean canBeDown = mid-looped > min;
			boolean canBeUp = mid+looped < max;
			String selected = title.substring((canBeDown)? mid-looped : min, (canBeUp)? mid+looped : max);
			String format = "|" + "&{" + color + "}" + selected + "|";
			frames.add(ColorTranslator.parse(format));
			++looped;
			if (!canBeUp && !canBeDown) break;
		}
		frames.add(ColorTranslator.parse(title));
		this.subTitle = ColorTranslator.parse(subtitle);
		this.speed = speed;
		this.duration = duration;
	}
	
	@Override
	public List<String> frames() {
		return frames;
	}
	
	@Override
	public int duration() {
		return duration;
	}
	
	@Override
	public int speed() {
		return speed;
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
				p.sendTitle("", "", 0, 0, 0);
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
				if (frameCounter >= frames.size()) frameCounter = frames.size()-1;
				String frame = frames.get(frameCounter);
				for (Player p : viewers) p .sendTitle(frame, subTitle, 0, 5, 0);
				if (tickCounter%20==0) {
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
