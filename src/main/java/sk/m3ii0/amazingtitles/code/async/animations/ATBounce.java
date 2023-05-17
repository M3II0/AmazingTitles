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

public class ATBounce implements AmazingTitle {

    private BukkitTask task;
    private final Set<Player> viewers = new HashSet<>();
    private final String subTitle;
    private final int speed;
    private final int duration;
    private final List<String> frames = new ArrayList<>();

    private int frameCounter = 0;
    private int tickCounter = 0;
    private int durationCounter = 0;

    public ATBounce(String title, String color1, String color2) {
        this(title, "", color1, color2, 1, 10);
    }

    public ATBounce(String title, String color1, String color2, String subTitle) {
        this(title, subTitle, color1, color2, 1, 10);
    }

    public ATBounce(String title, String subTitle, String color1, String color2, int duration) {
        this(title, subTitle, color1, color2, 1, duration);
    }

    public ATBounce(String title, Color color1, Color color2) {
        this(title, "", ColorUtils.hexFromColor(color1), ColorUtils.hexFromColor(color2), 1, 10);
    }

    public ATBounce(String title, Color color1, Color color2, String subTitle) {
        this(title, subTitle, ColorUtils.hexFromColor(color1), ColorUtils.hexFromColor(color2), 1, 10);
    }

    public ATBounce(String title, String subTitle, Color color1, Color color2, int duration) {
        this(title, subTitle, ColorUtils.hexFromColor(color1), ColorUtils.hexFromColor(color2), 1, duration);
    }

    public ATBounce(String title, String subTitle, Color color1, Color color2, int speed, int duration) {
        this(title, subTitle, ColorUtils.hexFromColor(color1), ColorUtils.hexFromColor(color2), speed, duration);
    }

    public ATBounce(String title, String subtitle, String color1, String color2, int speed, int duration) {
        int length = title.length();
        for (int i = 0; i <= length; i++) {
            String to = title.substring(0, i);
            String from = title.substring(i);
            if (to.length() == 1 || from.length() == 1) continue;
            String formatted = "<" + color1 + ">&l" + to + "</" + color2 + ">" + "<" + color2 + ">&l" + from + "</" + color1 + ">";
            frames.add(ColorTranslator.parse(formatted));
        }
        int revertSize = frames.size();
        for (int i = revertSize-1; i > -1; i--) {
            String reversed = frames.get(i);
            frames.add(reversed);
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
