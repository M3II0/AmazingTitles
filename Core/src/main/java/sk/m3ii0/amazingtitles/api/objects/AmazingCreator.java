package sk.m3ii0.amazingtitles.api.objects;

import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;
import sk.m3ii0.amazingtitles.api.objects.types.ActionType;
import sk.m3ii0.amazingtitles.code.AmazingTitles;
import sk.m3ii0.amazingtitles.code.notifications.DynamicBar;
import sk.m3ii0.amazingtitles.code.utils.StringUtils;

import java.util.*;

public class AmazingCreator {
	
	private final List<String> arguments;
	private final FramesBuilder framesBuilder;
	private final boolean repeat;
	private final boolean infinite;
	private final boolean legacy;
	private final List<String>[] competitions;
	
	public AmazingCreator(boolean repeat, boolean infinite, boolean legacy, FramesBuilder framesBuilder, String... arguments) {
		this.arguments = new ArrayList<>(StringUtils.of(arguments));
		this.framesBuilder = framesBuilder;
		this.legacy = legacy;
		this.repeat = repeat;
		this.infinite = infinite;
		this.competitions = new List[arguments.length];
		for (int i = 0; i < arguments.length; i++) {
			this.competitions[i] = new ArrayList<>(Collections.singleton(arguments[i]));
		}
	}
	
	public List<String> getArguments() {
		return arguments;
	}
	
	public int getMinimum() {
		return arguments.size();
	}
	
	public List<String>[] getCompetitions() {
		return competitions;
	}
	
	public List<String> getComplete(int arg) {
		int length = competitions.length;
		if (arg < length) return competitions[arg];
		else if (infinite) return StringUtils.of("<Text\\n\\SubText>");
		return new ArrayList<>();
	}
	
	public boolean isLegacy() {
		return legacy;
	}
	
	public FramesBuilder getFramesBuilder() {
		return framesBuilder;
	}
	
	public boolean isInfinite() {
		return infinite;
	}
	
	public AmazingComponent dispatch(boolean silent, Player[] receivers, ActionType type, int speed, int duration, String input, String subText, Object... args) throws Exception {
		if (subText == null || subText.isEmpty()) subText = "";
		AmazingComponent component;
		try {
			String finalSubText = subText;
			component = new AmazingComponent() {
				
				private BukkitTask task;
				private final Set<Player> viewers = new HashSet<>();
				private final List<String> frames = framesBuilder.frameBuilder(type, input, args);
				
				private int frameCounter = 0;
				private int tickCounter = 0;
				private int durationCounter = 0;
				
				private Object[] lastPackets;
				private int lastFrame;
				
				private final String subTitle = finalSubText;
				
				private final BossBar bar = Bukkit.createBossBar("", BarColor.WHITE, BarStyle.SOLID);;
				
				@Override
				public boolean silent() {
					return silent;
				}
				
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
						if (type == ActionType.ACTION_BAR && AmazingTitles.isStaticBarAnimations()) {
							DynamicBar bar = AmazingTitles.getBars().get(p.getUniqueId());
							bar.disableBar();
						}
						if (type == ActionType.BOSS_BAR) bar.addPlayer(p);
						AmazingTitles.getTitleManager().setTitleFor(p, this);
					}
					runTask();
				}
				
				@Override
				public void sendTo(Player... players) {
					for (Player p : players) {
						viewers.add(p);
						if (type == ActionType.ACTION_BAR && AmazingTitles.isStaticBarAnimations()) {
							DynamicBar bar = AmazingTitles.getBars().get(p.getUniqueId());
							bar.disableBar();
						}
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
							if (type == ActionType.ACTION_BAR && AmazingTitles.isStaticBarAnimations()) {
								DynamicBar bar = AmazingTitles.getBars().get(p.getUniqueId());
								bar.enableBar();
							}
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
				
				private void runTask() {
					if (task == null) {
						task = Bukkit.getScheduler().runTaskTimerAsynchronously(AmazingTitles.getInstance(), () -> {
							if (duration != -999) {
								if (duration == durationCounter) {
									delete();
									return;
								}
							}
							++tickCounter;
							if (tickCounter%speed==0) {
								++frameCounter;
							}
							if (frameCounter >= frames.size() && repeat) frameCounter = 0;
							else if (frameCounter >= frames.size()) frameCounter = frames.size()-1;
							String frame = frames.get(frameCounter);
							Object[] packets = new Object[0];
							if (frameCounter == lastFrame) {
								if (lastPackets == null) {
									if (type == ActionType.TITLE) lastPackets = AmazingTitles.getProvider().createTitlePacket(frame, subTitle, 0, 5, 0);
									if (type == ActionType.SUBTITLE) lastPackets = AmazingTitles.getProvider().createTitlePacket(subTitle, frame, 0, 5, 0);
									if (type == ActionType.ACTION_BAR) lastPackets = new Object[] {AmazingTitles.getProvider().createActionbarPacket(frame)};
								}
								packets = lastPackets;
							} else {
								if (type == ActionType.TITLE) packets = AmazingTitles.getProvider().createTitlePacket(frame, subTitle, 0, 5, 0);
								if (type == ActionType.SUBTITLE) packets = AmazingTitles.getProvider().createTitlePacket(subTitle, frame, 0, 5, 0);
								if (type == ActionType.ACTION_BAR) packets = new Object[] {AmazingTitles.getProvider().createActionbarPacket(frame)};
							}
							lastFrame = frameCounter;
							lastPackets = packets;
							if (type == ActionType.BOSS_BAR) {
								bar.setTitle(frame);
								bar.setProgress(((double) durationCounter/(double) duration));
							} else {
								for (Player p : viewers) {
									if (type == ActionType.TITLE || type == ActionType.SUBTITLE)
										AmazingTitles.getProvider().sendTitles(p, packets);
									if (type == ActionType.ACTION_BAR) AmazingTitles.getProvider().sendActionbar(p, packets[0]);
								}
							}
							if (tickCounter%20==0) {
								++durationCounter;
							}
						}, 0, 1);
					}
				}
				
			};
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("Invalid input or unhandled exception in animation! Contact animation creator or check your arguments!");
		}
		
		component.sendTo(receivers);
		
		return component;
	}
	
}
