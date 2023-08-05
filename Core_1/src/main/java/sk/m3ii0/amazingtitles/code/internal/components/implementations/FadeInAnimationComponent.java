package sk.m3ii0.amazingtitles.code.internal.components.implementations;

import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;
import sk.m3ii0.amazingtitles.code.api.enums.DisplayType;
import sk.m3ii0.amazingtitles.code.internal.Booter;
import sk.m3ii0.amazingtitles.code.internal.components.AnimationComponent;

import java.util.*;

public class FadeInAnimationComponent implements AnimationComponent {
	
	/*
	*
	* Values
	*
	* */
	
	private BukkitTask task;
	private Runnable runnable;
	private BossBar bossBar;
	private int loopedSeconds = 0;
	private int loopedFrames = 0;
	private int framesCounter = 0;
	private final List<Player> receivers = new ArrayList<>();
	private final Plugin plugin;
	private final List<String> frames;
	private final String mainText;
	private final String subText;
	private final int duration;
	private final int fps;
	private final DisplayType displayType;
	private final BarColor componentColor;
	
	/*
	*
	* Constructor
	*
	* */
	
	public FadeInAnimationComponent(Plugin plugin, List<String> frames, String mainText, String subText, int fps, int duration, DisplayType displayType, BarColor componentColor) {
		this.plugin = plugin;
		this.frames = frames;
		this.mainText = mainText;
		this.subText = subText;
		this.fps = fps;
		this.duration = duration;
		this.displayType = displayType;
		this.componentColor = componentColor;
	}
	
	/*
	*
	* Values management
	*
	* */
	
	@Override
	public List<String> frames() {
		return frames;
	}
	
	@Override
	public String mainText() {
		return mainText;
	}
	
	@Override
	public Optional<String> subText() {
		return Optional.of(subText);
	}
	
	@Override
	public int duration() {
		return duration;
	}
	
	@Override
	public int fps() {
		return fps;
	}
	
	@Override
	public DisplayType display() {
		return displayType;
	}
	
	@Override
	public Optional<BarColor> componentColor() {
		return AnimationComponent.super.componentColor();
	}
	
	/*
	*
	* Player management
	*
	* */
	
	@Override
	public void addReceivers(Player... players) {
		receivers.addAll(Arrays.asList(players));
	}
	
	@Override
	public void addReceivers(Collection<Player> players) {
		receivers.addAll(players);
	}
	
	@Override
	public void removeReceivers(Player... players) {
		receivers.removeAll(Arrays.asList(players));
	}
	
	@Override
	public void removeReceivers(Collection<Player> players) {
		receivers.removeAll(players);
	}
	
	/*
	*
	* Animation management
	*
	* */
	
	
	
	@Override
	public String callCurrentFrame() {
		return frames.get(0);
	}
	
	@Override
	public void prepare() {
		if (displayType == DisplayType.BOSS_BAR) {
			bossBar = Bukkit.createBossBar("", componentColor, BarStyle.SOLID);
			for (Player p : receivers) {
				bossBar.addPlayer(p);
			}
			bossBar.setVisible(false);
		}
		if (displayType == DisplayType.TITLE) {
			this.runnable = () -> {
				if (next()) {
					String frame = frames.get(framesCounter);
					Object[] packets = Booter.getNmsProvider().createTitlePacket(frame, subText, 0, 20, 0);
					for (Player p : receivers) {
						if (p == null) continue;
						Booter.getNmsProvider().sendTitles(p, packets);
					}
				} else {
					end();
				}
			};
		}
		if (displayType == DisplayType.SUBTITLE) {
			this.runnable = () -> {
				if (next()) {
					String frame = frames.get(framesCounter);
					Object[] packets = Booter.getNmsProvider().createTitlePacket(subText, frame, 0, 20, 0);
					for (Player p : receivers) {
						if (p == null) continue;
						Booter.getNmsProvider().sendTitles(p, packets);
					}
				} else {
					end();
				}
			};
		}
		if (displayType == DisplayType.ACTION_BAR) {
			this.runnable = () -> {
				if (next()) {
					String frame = frames.get(framesCounter);
					Object packet = Booter.getNmsProvider().createActionbarPacket(frame);
					for (Player p : receivers) {
						if (p == null) continue;
						Booter.getNmsProvider().sendActionbar(p, packet);
					}
				} else {
					end();
				}
			};
		}
		if (displayType == DisplayType.BOSS_BAR) {
			this.runnable = () -> {
				if (next()) {
					String frame = frames.get(framesCounter);
					bossBar.setTitle(frame);
				} else {
					end();
				}
			};
		}
	}
	
	@Override
	public void run() {
		if (displayType == DisplayType.BOSS_BAR && bossBar != null) {
			bossBar.setVisible(true);
		}
		task = Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, this.runnable, 0, 20/fps);
	}
	
	@Override
	public void end() {
		if (task != null) {
			task.cancel();
		}
		if (bossBar != null) {
			bossBar.setVisible(false);
			bossBar.removeAll();
		}
		receivers.clear();
		frames.clear();
	}
	
	private boolean next() {
		++framesCounter;
		if (framesCounter >= frames.size()) {
			framesCounter = frames.size()-1;
		}
		if (loopedFrames >= framesPerSecond()) {
			if (loopedSeconds >= duration) {
				return false;
			}
			++loopedSeconds;
			loopedFrames = 0;
		}
		++loopedFrames;
		return true;
	}
	
	private int framesPerSecond() {
		return 20/fps;
	}
	
}
