package sk.m3ii0.amazingtitles.code.internal.components.implementations;

import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;
import sk.m3ii0.amazingtitles.code.api.AmazingTitles;
import sk.m3ii0.amazingtitles.code.api.enums.DisplayType;
import sk.m3ii0.amazingtitles.code.internal.Booter;
import sk.m3ii0.amazingtitles.code.internal.components.AnimationComponent;

import java.util.*;

public class LightAnimationComponent implements AnimationComponent {
	
	/*
	*
	* Values
	*
	* */
	
	private BukkitTask task;
	private Runnable runnable;
	private BossBar bossBar;
	private int loopedSeconds = 0;
	private final List<Player> receivers = new ArrayList<>();
	private final Plugin plugin;
	private final String frame;
	private final String mainText;
	private final String subText;
	private final int duration;
	private final DisplayType displayType;
	private final BarColor componentColor;
	
	/*
	*
	* Constructor
	*
	* */
	
	public LightAnimationComponent(Plugin plugin, String frame, String mainText, String subText, int duration, DisplayType displayType, BarColor componentColor) {
		this.plugin = plugin;
		this.frame = frame;
		this.mainText = mainText;
		this.subText = subText;
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
		return Collections.singletonList(frame);
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
		return 20;
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
		return frame;
	}
	
	@Override
	public void prepare() {
		for (Player p : receivers) {
			AmazingTitles.removeAnimation(p);
			AmazingTitles.insertAnimation(p, this);
		}
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
		task = Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, this.runnable, 0, 20);
	}
	
	@Override
	public void end() {
		for (Player p : receivers) {
			AmazingTitles.removeAnimationFromCache(p.getUniqueId());
		}
		if (task != null) {
			task.cancel();
		}
		if (bossBar != null) {
			bossBar.setVisible(false);
			bossBar.removeAll();
		}
		receivers.clear();
	}
	
	private boolean next() {
		if (loopedSeconds >= duration) {
			return false;
		}
		++loopedSeconds;
		return true;
	}
	
}
