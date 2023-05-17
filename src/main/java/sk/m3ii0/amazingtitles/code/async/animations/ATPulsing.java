package sk.m3ii0.amazingtitles.code.async.animations;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;
import sk.m3ii0.amazingtitles.code.AmazingTitles;
import sk.m3ii0.amazingtitles.code.async.AmazingTitle;
import sk.m3ii0.amazingtitles.code.colors.ColorTranslator;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ATPulsing implements AmazingTitle {
	
	private BukkitTask task;
	private final Set<Player> viewers = new HashSet<>();
	private final String subTitle;
	private final int speed;
	private final int duration;
	private final List<String> frames = new ArrayList<>();
	
	private int frameCounter = 0;
	private int tickCounter = 0;
	private int durationCounter = 0;
	
	public ATPulsing(String title, String color1, String color2) {
		this(title, "", Color.decode(color1), Color.decode(color2), 1, 10);
	}
	
	public ATPulsing(String title, String color1, String color2, String subTitle) {
		this(title, subTitle, Color.decode(color1), Color.decode(color2), 1, 10);
	}
	
	public ATPulsing(String title, String subTitle, String color1, String color2, int duration) {
		this(title, subTitle, Color.decode(color1), Color.decode(color2), 1, duration);
	}
	
	public ATPulsing(String title, Color color1, Color color2) {
		this(title, "", color1, color2, 1, 10);
	}
	
	public ATPulsing(String title, Color color1, Color color2, String subTitle) {
		this(title, subTitle, color1, color2, 1, 10);
	}
	
	public ATPulsing(String title, String subTitle, Color color1, Color color2, int duration) {
		this(title, subTitle, color1, color2, 1, duration);
	}
	
	public ATPulsing(String title, String subTitle, String color1, String color2, int speed, int duration) {
		this(title, subTitle, Color.decode(color1), Color.decode(color2), speed, duration);
	}
	
	public ATPulsing(String title, String subtitle, Color color1, Color color2, int speed, int duration) {
		int r_max = Math.max(color1.getRed(), color2.getRed());
		int g_max = Math.max(color1.getGreen(), color2.getGreen());
		int b_max = Math.max(color1.getBlue(), color2.getBlue());
		int r_min = Math.min(color1.getRed(), color2.getRed());
		int g_min = Math.min(color1.getGreen(), color2.getGreen());
		int b_min = Math.min(color1.getBlue(), color2.getBlue());
		int r_total = r_max - r_min;
		int g_total = g_max - g_min;
		int b_total = b_max - b_min;
		int total_max = 0;
		if (r_total > total_max) total_max = r_total;
		if (g_total > total_max) total_max = g_total;
		if (b_total > total_max) total_max = b_total;
		int lastMinR = r_min;
		int lastMinG = g_min;
		int lastMinB = b_min;
		int lastMaxR = r_max;
		int lastMaxG = g_max;
		int lastMaxB = b_max;
		for (int i = 0; i <= total_max; i++) {
			int r = lastMinR;
			int g = lastMinG;
			int b = lastMinB;
			if (lastMinR < r_max) {
				r = (lastMinR = lastMinR+5);
			}
			if (lastMinG < g_max) {
				g = (lastMinG = lastMinG+5);
			}
			if (lastMinB < b_max) {
				b = (lastMinB = lastMinB+5);
			}
			if (r > r_max) r = r_max;
			if (g > g_max) g = g_max;
			if (b > b_max) b = b_max;
			Color c = new Color(r, g, b);
			String format = ChatColor.of(c) + title;
			frames.add(ColorTranslator.parse(format));
			if (r == r_max && g == g_max & b == b_max) {break;}
		}
		for (int i = 0; i <= total_max-1; i++) {
			int r = lastMaxR;
			int g = lastMaxG;
			int b = lastMaxB;
			if (lastMaxR > r_min) {
				r = (lastMaxR = lastMaxR-5);
			}
			if (lastMaxG > g_min) {
				g = (lastMaxG = lastMaxG-5);
			}
			if (lastMaxB > b_min) {
				b = (lastMaxB = lastMaxB-5);
			}
			if (r < r_min) r = r_min;
			if (g < g_min) g = g_min;
			if (b < b_min) b = b_min;
			Color c = new Color(r, g, b);
			String format = ChatColor.of(c) + title;
			frames.add(ColorTranslator.parse(format));
			if (r == r_min && g == g_min & b == b_min) {break;}
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
				if (frameCounter >= frames.size()) frameCounter = 0;
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
