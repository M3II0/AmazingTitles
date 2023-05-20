package sk.m3ii0.amazingtitles.code.async.animations;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;
import sk.m3ii0.amazingtitles.code.AmazingTitles;
import sk.m3ii0.amazingtitles.code.async.AmazingTitle;
import sk.m3ii0.amazingtitles.code.colors.ColorTranslator;

import java.util.*;

public class ATWordSplit implements AmazingTitle {
	
	private BukkitTask task;
	private final Set<Player> viewers = new HashSet<>();
	private final String subTitle;
	private final int speed;
	private final int duration;
	private final List<String> frames = new ArrayList<>();
	
	private int frameCounter = 0;
	private int tickCounter = 0;
	private int durationCounter = 0;
	
	public ATWordSplit(String title) {
		this(title, "", 1, 10);
	}
	
	public ATWordSplit(String title, String subTitle) {
		this(title, subTitle, 1, 10);
	}
	
	public ATWordSplit(String title, String subTitle, int duration) {
		this(title, subTitle, 1, duration);
	}
	
	public ATWordSplit(String title, String subtitle, int speed, int duration) {
		title = ColorTranslator.parse(title);
		frames.addAll(Arrays.asList(title.split(" ")));
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
