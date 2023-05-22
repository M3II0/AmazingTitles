package sk.m3ii0.amazingtitles.code.async.animations;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;
import sk.m3ii0.amazingtitles.code.AmazingTitles;
import sk.m3ii0.amazingtitles.code.async.AmazingComponent;
import sk.m3ii0.amazingtitles.code.colors.ColorTranslator;
import sk.m3ii0.amazingtitles.code.commands.types.ActionType;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ATPulsing implements AmazingComponent {
	
	/*
	 *
	 * Values
	 *
	 * */
	
	private BukkitTask task;
	private final Set<Player> viewers = new HashSet<>();
	private final List<String> frames = new ArrayList<>();
	private final ActionType type;
	
	private int frameCounter = 0;
	private int tickCounter = 0;
	private int durationCounter = 0;
	
	private Object[] lastPackets;
	private int lastFrame;
	
	private String subText = AmazingComponent.super.text();
	private int speed = AmazingComponent.super.speed();
	private int duration = AmazingComponent.super.duration();
	
	private final BossBar bar;
	
	/*
	 *
	 * Constructor
	 *
	 * */
	
	public ATPulsing(ActionType type, String title, String color1, String color2) {
		Color from = Color.decode(color1);
		Color to = Color.decode(color2);
		int r_max = Math.max(from.getRed(), to.getRed());
		int g_max = Math.max(from.getGreen(), to.getGreen());
		int b_max = Math.max(from.getBlue(), to.getBlue());
		int r_min = Math.min(from.getRed(), to.getRed());
		int g_min = Math.min(from.getGreen(), to.getGreen());
		int b_min = Math.min(from.getBlue(), to.getBlue());
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
		this.type = type;
		bar = Bukkit.createBossBar("", BarColor.WHITE, BarStyle.SOLID);
	}
	
	/*
	 *
	 * Setter
	 *
	 * */
	
	public void setSubText(String subText) {
		this.subText = ColorTranslator.parse(subText);
	}
	
	public void setSpeed(int speed) {
		this.speed = speed;
	}
	
	public void setDuration(int duration) {
		this.duration = duration;
	}
	
	/*
	*
	* API
	*
	* */
	
	@Override
	public ActionType type() {
		return type;
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
			if (type == ActionType.BOSS_BAR) bar.addPlayer(p);
			AmazingTitles.getTitleManager().setTitleFor(p, this);
		}
		runTask();
	}
	
	@Override
	public void sendTo(Player... players) {
		for (Player p : players) {
			viewers.add(p);
			if (type == ActionType.BOSS_BAR) bar.addPlayer(p);
			AmazingTitles.getTitleManager().setTitleFor(p, this);
		}
		runTask();
	}
	
	@Override
	public void removeFor(Player... player) {
		for (Player p : player) {
			if (viewers.remove(p)) {
				p.resetTitle();
				if (type == ActionType.BOSS_BAR) bar.removePlayer(p);
				AmazingTitles.getTitleManager().unsetTitleFor(p);
			}
		}
	}
	
	@Override
	public void delete() {
		if (task != null) {
			task.cancel();
			task = null;
		}
		if (bar != null) {
			bar.removeAll();
		}
		removeFor(viewers.toArray(new Player[0]));
	}
	
	/*
	*
	* Tasks
	*
	* */
	
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
				Object[] packets = new Object[0];
				if (frameCounter == lastFrame) {
					if (lastPackets == null) {
						if (type == ActionType.TITLE) lastPackets = AmazingTitles.getProvider().createTitlePacket(frame, subText);
						if (type == ActionType.SUBTITLE) lastPackets = AmazingTitles.getProvider().createTitlePacket(subText, frame);
						if (type == ActionType.ACTION_BAR) lastPackets = new Object[] {AmazingTitles.getProvider().createActionbarPacket(frame)};
					}
					packets = lastPackets;
				} else {
					if (type == ActionType.TITLE) packets = AmazingTitles.getProvider().createTitlePacket(frame, subText);
					if (type == ActionType.SUBTITLE) packets = AmazingTitles.getProvider().createTitlePacket(subText, frame);
					if (type == ActionType.ACTION_BAR) packets = new Object[] {AmazingTitles.getProvider().createActionbarPacket(frame)};
				}
				lastFrame = frameCounter;
				lastPackets = packets;
				for (Player p : viewers) {
					if (type == ActionType.TITLE || type == ActionType.SUBTITLE) AmazingTitles.getProvider().sendTitles(p, packets);
					if (type == ActionType.ACTION_BAR) AmazingTitles.getProvider().sendActionbar(p, packets[0]);
					if (type == ActionType.BOSS_BAR) {
						bar.setTitle(frame);
						bar.setProgress(((double) durationCounter/(double) duration));
					}
				}
				if (tickCounter%20==0) {
					++durationCounter;
				}
			}, 0, 1);
		}
	}
	
}
