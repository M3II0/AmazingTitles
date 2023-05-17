package sk.m3ii0.amazingtitles.code.async.animations;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;
import sk.m3ii0.amazingtitles.code.AmazingTitles;
import sk.m3ii0.amazingtitles.code.async.AmazingTitle;
import sk.m3ii0.amazingtitles.code.colors.ColorTranslator;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ATFromRight implements AmazingTitle {
	
	private BukkitTask task;
	private final Set<Player> viewers = new HashSet<>();
	private final String subTitle;
	private final int speed;
	private final int duration;
	private final List<String> frames = new ArrayList<>();
	
	private int frameCounter = 0;
	private int tickCounter = 0;
	private int durationCounter = 0;
	
	public ATFromRight(String title) {
		this(title, "", 1, 10);
	}
	
	public ATFromRight(String title, String subTitle) {
		this(title, subTitle, 1, 10);
	}
	
	public ATFromRight(String title, String subTitle, int duration) {
		this(title, subTitle, 1, duration);
	}
	
	public ATFromRight(String title, String subtitle, int speed, int duration) {
		int lastSpaces = 180;
		String spaces = "";
		for (int i = lastSpaces; i >=0; i--) {
			spaces += " ";
		}
		for (int i = 30; i > -1; i--) {
			int newSpaces = lastSpaces-(i*5);
			String formattedSpaces = spaces.substring(newSpaces);
			frames.add(ColorTranslator.parse(formattedSpaces + title));
		}
		frames.add(ColorTranslator.parse(title));
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
