package sk.m3ii0.amazingtitles.code.notifications;

import sk.m3ii0.amazingtitles.code.colors.ColorTranslator;
import sk.m3ii0.amazingtitles.code.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class BarNotification {

    /*
     *
     * Builder
     *
     * */

    public static BarNotification create(String symbol, String text, int duration) {
        if (symbol == null || text == null) return null;
        return new BarNotification(symbol, text, duration, (text1) -> text1);
    }

    public static BarNotification from(BarNotification example, Placeholders placeholders) {
        return new BarNotification(example.getSymbol(), example.getRawText(), example.getRawDuration(), placeholders);
    }

    /*
     *
     * Class values
     *
     * */

    private final String symbol;
    private String text;
    private long duration;
    private int rawDuration;
    private List<String> animation;
    private int adder;
    private int frame;
    private final String rawText;
    private double remaining;
    private boolean hide;

    /*
     *
     * Constructor
     *
     * */

    private BarNotification(String symbol, String text, int duration, Placeholders placeholders) {
        this.symbol = ColorTranslator.parse(symbol);
        this.rawText = text + "";
        this.rawDuration = duration;
        this.text = ColorTranslator.parse(placeholders.setPlaceholders(symbol + " " + text));
        this.frame = 0;
        this.animation = new ArrayList<>();
        StringBuilder builder = new StringBuilder();
        List<String> chars = StringUtils.toColoredChars(this.text);
        for (String aChar : chars) {
            builder.append(aChar);
            animation.add(builder.toString());
        }
        this.adder = animation.size() / 20 + 1;
        this.duration = System.currentTimeMillis() + (((adder * 2L) + duration) * 1000L);
    }

    /*
     *
     * Getters
     *
     * */

    public int getRawDuration() {
        return rawDuration;
    }

    public String getSymbol() {
        return symbol;
    }

    public String getText() {
        return text;
    }

    public long getDuration() {
        return duration;
    }

    public int getFrame() {
        return frame;
    }

    public String getRawText() {
        return rawText;
    }

    public void setHide(boolean hide) {
        this.hide = hide;
    }

    /*
     *
     * Functions
     *
     * */

    public boolean isEnding() {
        if (rawDuration == -1) return false;
        return remaining < this.adder;
    }

    public boolean isOverridable() {
        if (rawDuration == -1) return false;
        return !(remaining > this.adder+rawDuration) && (remaining > this.adder);
    }

    public void updateBy(BarNotification notification) {
        this.adder = notification.animation.size()/20+1;
        this.duration = System.currentTimeMillis()+(notification.rawDuration*1000L);
        this.rawDuration = notification.rawDuration;
        this.animation = notification.animation;
        this.text = notification.text;
        this.remaining = this.adder-duration;
    }

    public boolean isValid(long time) {
        if (rawDuration == -1) return true;
        return duration > time && frame >= 0;
    }

    public String getCurrentFrame(long time) {
        if (rawDuration == -1) return text;
        remaining = (duration-time)/1000.0;
        if (hide) {
            if (frame > 0) {
                --frame;
                if (frame < 1 || frame >= animation.size()) return symbol;
                return animation.get(frame);
            } else return symbol;
        }
        if (remaining > this.adder+rawDuration) {
            if (frame < animation.size()) {
                String current = animation.get(frame);
                ++frame;
                return current;
            } else return text;
        }
        if (remaining < this.adder) {
            if (frame > -1) {
                --frame;
                if (frame < 0 || frame >= animation.size()) return "";
                return animation.get(frame);
            } else return "";
        }
        if (frame < animation.size()) {
            String current = animation.get(frame);
            ++frame;
            return current;
        } else return text;
    }

    /*
     *
     * Functional
     *
     * */

    public interface Placeholders {
        String setPlaceholders(String text);
    }

}
