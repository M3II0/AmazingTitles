package sk.m3ii0.amazingtitles.code.async.animations.smooth;

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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ATSmoothBounce implements AmazingComponent {
    
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

    public ATSmoothBounce(ActionType type, String title, String color1, String color2) {
        String smoothed = "          " + title + "          ";
        int length = smoothed.length();
        int withGradient = title.length()*17;
        int start = 10*17;
        for (int i = 0; i <= length; i++) {
            String to = smoothed.substring(0, i);
            String from = smoothed.substring(i);
            if (to.length() == 1 || from.length() == 1) continue;
            String formatted = "<" + color1 + ">&l" + to + "</" + color2 + ">" + "<" + color2 + ">&l" + from + "</" + color1 + ">";
            frames.add(ColorTranslator.parse(formatted).substring(start).substring(0, withGradient));
        }
        int revertSize = frames.size();
        for (int i = revertSize-1; i > -1; i--) {
            String reversed = frames.get(i);
            frames.add(reversed);
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
    * Task
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
