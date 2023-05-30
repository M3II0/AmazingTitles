package sk.m3ii0.amazingtitles.code.commands.dispatcher;

import net.md_5.bungee.api.chat.*;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import sk.m3ii0.amazingtitles.code.async.AmazingComponent;
import sk.m3ii0.amazingtitles.code.async.animations.*;
import sk.m3ii0.amazingtitles.code.async.animations.smooth.ATSmoothBounce;
import sk.m3ii0.amazingtitles.code.async.animations.smooth.ATSmoothRainbow;
import sk.m3ii0.amazingtitles.code.async.animations.smooth.ATSmoothWaves;
import sk.m3ii0.amazingtitles.code.colors.ColorTranslator;
import sk.m3ii0.amazingtitles.code.commands.types.ActionType;
import sk.m3ii0.amazingtitles.code.commands.types.AnimationTypes;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TitleDispatcher {
    
    private static final Pattern match = Pattern.compile("<(.*?)>(.*?)</>");
    
    public static void asyncDispatch(CommandSender s, ActionType action, AnimationTypes type, List<Player> receivers, String[] args) {
        try {
            AmazingComponent title = buildTitle(action, type, args);
            if (title == null) {
                sendError(s);
                return;
            }
            title.sendTo(receivers.toArray(new Player[0]));
            sendSuccess(s, type, receivers, title);
        } catch (Exception e) {
            e.printStackTrace();
            sendError(s);
        }
    }

    private static AmazingComponent buildTitle(ActionType action, AnimationTypes animation, String[] args) {
        if (animation == AnimationTypes.RAINBOW) {
            int rawSpeed = Integer.parseInt(args[0]);
            int speed = Math.max(rawSpeed, 1);
            int duration = Integer.parseInt(args[1]);
            String text = "";
            for (int i = 2; i < args.length; i++) {
                text += args[i] + " ";
            }
            text = text.replaceAll(" $", "");
            String title = text.split("\\\\n\\\\")[0];
            String subtitle = (text.split("\\\\n\\\\").length > 1)? text.split("\\\\n\\\\")[1] : "";
            ATRainbow rainbow = new ATRainbow(action, title);
            rainbow.setSpeed(speed);
            rainbow.setDuration(duration);
            rainbow.setSubText(subtitle);
            return rainbow;
        }
        if (animation == AnimationTypes.SMOOTH_RAINBOW) {
            int rawSpeed = Integer.parseInt(args[0]);
            int speed = Math.max(rawSpeed, 1);
            int duration = Integer.parseInt(args[1]);
            String text = "";
            for (int i = 2; i < args.length; i++) {
                text += args[i] + " ";
            }
            text = text.replaceAll(" $", "");
            String title = text.split("\\\\n\\\\")[0];
            String subtitle = (text.split("\\\\n\\\\").length > 1)? text.split("\\\\n\\\\")[1] : "";
            ATSmoothRainbow atSmoothRainbow = new ATSmoothRainbow(action, title);
            atSmoothRainbow.setSpeed(speed);
            atSmoothRainbow.setDuration(duration);
            atSmoothRainbow.setSubText(subtitle);
            return atSmoothRainbow;
        }
        if (animation == AnimationTypes.WAVES) {
            int rawSpeed = Integer.parseInt(args[0]);
            int speed = Math.max(rawSpeed, 1);
            int duration = Integer.parseInt(args[1]);
            String color1 = args[2];
            String color2 = args[3];
            String text = "";
            for (int i = 4; i < args.length; i++) {
                text += args[i] + " ";
            }
            text = text.replaceAll(" $", "");
            String title = text.split("\\\\n\\\\")[0];
            String subtitle = (text.split("\\\\n\\\\").length > 1)? text.split("\\\\n\\\\")[1] : "";
            ATWaves waves = new ATWaves(action, title, color1, color2);
            waves.setSpeed(speed);
            waves.setDuration(duration);
            waves.setSubText(subtitle);
            return waves;
        }
        if (animation == AnimationTypes.SMOOTH_WAVES) {
            int rawSpeed = Integer.parseInt(args[0]);
            int speed = Math.max(rawSpeed, 1);
            int duration = Integer.parseInt(args[1]);
            String color1 = args[2];
            String color2 = args[3];
            String text = "";
            for (int i = 4; i < args.length; i++) {
                text += args[i] + " ";
            }
            text = text.replaceAll(" $", "");
            String title = text.split("\\\\n\\\\")[0];
            String subtitle = (text.split("\\\\n\\\\").length > 1)? text.split("\\\\n\\\\")[1] : "";
            ATSmoothWaves smoothWaves = new ATSmoothWaves(action, title, color1, color2);
            smoothWaves.setSpeed(speed);
            smoothWaves.setDuration(duration);
            smoothWaves.setSubText(subtitle);
            return smoothWaves;
        }
        if (animation == AnimationTypes.BOUNCE) {
            int rawSpeed = Integer.parseInt(args[0]);
            int speed = Math.max(rawSpeed, 1);
            int duration = Integer.parseInt(args[1]);
            String color1 = args[2];
            String color2 = args[3];
            String text = "";
            for (int i = 4; i < args.length; i++) {
                text += args[i] + " ";
            }
            text = text.replaceAll(" $", "");
            String title = text.split("\\\\n\\\\")[0];
            String subtitle = (text.split("\\\\n\\\\").length > 1)? text.split("\\\\n\\\\")[1] : "";
            ATBounce bounce = new ATBounce(action, title, color1, color2);
            bounce.setSpeed(speed);
            bounce.setDuration(duration);
            bounce.setSubText(subtitle);
            return bounce;
        }
        if (animation == AnimationTypes.SMOOTH_BOUNCE) {
            int rawSpeed = Integer.parseInt(args[0]);
            int speed = Math.max(rawSpeed, 1);
            int duration = Integer.parseInt(args[1]);
            String color1 = args[2];
            String color2 = args[3];
            String text = "";
            for (int i = 4; i < args.length; i++) {
                text += args[i] + " ";
            }
            text = text.replaceAll(" $", "");
            String title = text.split("\\\\n\\\\")[0];
            String subtitle = (text.split("\\\\n\\\\").length > 1)? text.split("\\\\n\\\\")[1] : "";
            ATSmoothBounce smoothBounce = new ATSmoothBounce(action, title, color1, color2);
            smoothBounce.setSpeed(speed);
            smoothBounce.setDuration(duration);
            smoothBounce.setSubText(subtitle);
            return smoothBounce;
        }
        if (animation == AnimationTypes.WRITER_STAY) {
            int rawSpeed = Integer.parseInt(args[0]);
            int speed = Math.max(rawSpeed, 1);
            int duration = Integer.parseInt(args[1]);
            String writer = args[2];
            String text = "";
            for (int i = 3; i < args.length; i++) {
                text += args[i] + " ";
            }
            text = text.replaceAll(" $", "");
            String title = text.split("\\\\n\\\\")[0];
            String subtitle = (text.split("\\\\n\\\\").length > 1)? text.split("\\\\n\\\\")[1] : "";
            ATWriterStay writerStay = new ATWriterStay(action, title, writer);
            writerStay.setSpeed(speed);
            writerStay.setDuration(duration);
            writerStay.setSubText(subtitle);
            return writerStay;
        }
        if (animation == AnimationTypes.PULSING) {
            int rawSpeed = Integer.parseInt(args[0]);
            int speed = Math.max(rawSpeed, 1);
            int duration = Integer.parseInt(args[1]);
            Color color1 = java.awt.Color.decode(args[2]);
            Color color2 = java.awt.Color.decode(args[3]);
            String text = "";
            for (int i = 4; i < args.length; i++) {
                text += args[i] + " ";
            }
            text = text.replaceAll(" $", "");
            String title = text.split("\\\\n\\\\")[0];
            String subtitle = (text.split("\\\\n\\\\").length > 1)? text.split("\\\\n\\\\")[1] : "";
            ATPulsing pulsing = new ATPulsing(action, title, args[2], args[3]);
            pulsing.setSpeed(speed);
            pulsing.setDuration(duration);
            pulsing.setSubText(subtitle);
            return pulsing;
        }
        if (animation == AnimationTypes.COMING_FROM_RIGHT) {
            int rawSpeed = Integer.parseInt(args[0]);
            int speed = Math.max(rawSpeed, 1);
            int duration = Integer.parseInt(args[1]);
            String text = "";
            for (int i = 2; i < args.length; i++) {
                text += args[i] + " ";
            }
            text = text.replaceAll(" $", "");
            String title = text.split("\\\\n\\\\")[0];
            String subtitle = (text.split("\\\\n\\\\").length > 1)? text.split("\\\\n\\\\")[1] : "";
            ATFromRight fromRight = new ATFromRight(action, title);
            fromRight.setSpeed(speed);
            fromRight.setDuration(duration);
            fromRight.setSubText(subtitle);
            return fromRight;
        }
        if (animation == AnimationTypes.COMING_FROM_LEFT) {
            int rawSpeed = Integer.parseInt(args[0]);
            int speed = Math.max(rawSpeed, 1);
            int duration = Integer.parseInt(args[1]);
            String text = "";
            for (int i = 2; i < args.length; i++) {
                text += args[i] + " ";
            }
            text = text.replaceAll(" $", "");
            String title = text.split("\\\\n\\\\")[0];
            String subtitle = (text.split("\\\\n\\\\").length > 1)? text.split("\\\\n\\\\")[1] : "";
            ATFromLeft fromLeft = new ATFromLeft(action, title);
            fromLeft.setSpeed(speed);
            fromLeft.setDuration(duration);
            fromLeft.setSubText(subtitle);
            return fromLeft;
        }
        if (animation == AnimationTypes.COMING_FROM_SIDES) {
            int rawSpeed = Integer.parseInt(args[0]);
            int speed = Math.max(rawSpeed, 1);
            int duration = Integer.parseInt(args[1]);
            String text = "";
            for (int i = 2; i < args.length; i++) {
                text += args[i] + " ";
            }
            text = text.replaceAll(" $", "");
            String title = text.split("\\\\n\\\\")[0];
            String subtitle = (text.split("\\\\n\\\\").length > 1)? text.split("\\\\n\\\\")[1] : "";
            ATFromSides sides = new ATFromSides(action, title);
            sides.setSpeed(speed);
            sides.setDuration(duration);
            sides.setSubText(subtitle);
            return sides;
        }
        if (animation == AnimationTypes.OPEN) {
            int rawSpeed = Integer.parseInt(args[0]);
            int speed = Math.max(rawSpeed, 1);
            int duration = Integer.parseInt(args[1]);
            Color color1 = java.awt.Color.decode(args[2]);
            String text = "";
            for (int i = 3; i < args.length; i++) {
                text += args[i] + " ";
            }
            text = text.replaceAll(" $", "");
            String title = text.split("\\\\n\\\\")[0];
            String subtitle = (text.split("\\\\n\\\\").length > 1)? text.split("\\\\n\\\\")[1] : "";
            ATOpen open = new ATOpen(action, title, args[2]);
            open.setSpeed(speed);
            open.setDuration(duration);
            open.setSubText(subtitle);
            return open;
        }
        if (animation == AnimationTypes.FLASHING) {
            int rawSpeed = Integer.parseInt(args[0]);
            int speed = Math.max(rawSpeed, 1);
            int duration = Integer.parseInt(args[1]);
            String text = "";
            for (int i = 2; i < args.length; i++) {
                text += args[i] + " ";
            }
            text = text.replaceAll(" $", "");
            String title = text.split("\\\\n\\\\")[0];
            String subtitle = (text.split("\\\\n\\\\").length > 1)? text.split("\\\\n\\\\")[1] : "";
            ATFlashing flashing = new ATFlashing(action, title);
            flashing.setSpeed(speed);
            flashing.setDuration(duration);
            flashing.setSubText(subtitle);
            return flashing;
        }
        if (animation == AnimationTypes.SPACE_SPLIT) {
            int rawSpeed = Integer.parseInt(args[0]);
            int speed = Math.max(rawSpeed, 1);
            int duration = Integer.parseInt(args[1]);
            String text = "";
            for (int i = 2; i < args.length; i++) {
                text += args[i] + " ";
            }
            text = text.replaceAll(" $", "");
            String title = text.split("\\\\n\\\\")[0];
            String subtitle = (text.split("\\\\n\\\\").length > 1)? text.split("\\\\n\\\\")[1] : "";
            ATWordSplit wordSplit = new ATWordSplit(action, title);
            wordSplit.setSpeed(speed);
            wordSplit.setDuration(duration);
            wordSplit.setSubText(subtitle);
            return wordSplit;
        }
        if (animation == AnimationTypes.SPLIT) {
            int rawSpeed = Integer.parseInt(args[0]);
            int speed = Math.max(rawSpeed, 1);
            int duration = Integer.parseInt(args[1]);
            String text = "";
            for (int i = 2; i < args.length; i++) {
                text += args[i] + " ";
            }
            text = text.replaceAll(" $", "");
            String title = text.split("\\\\n\\\\")[0];
            String subtitle = (text.split("\\\\n\\\\").length > 1)? text.split("\\\\n\\\\")[1] : "";
            ATSplit split = new ATSplit(action, title);
            split.setSpeed(speed);
            split.setDuration(duration);
            split.setSubText(subtitle);
            return split;
        }
        return null;
    }

    private static void sendError(CommandSender s) {
        s.sendMessage("§cAT §7-> §4Error with dispatching... (Check your format)");
    }
    
    public static BaseComponent[] getMessageFromRaw(String text) {
        List<BaseComponent> components = new ArrayList<>();
        StringBuilder builder = new StringBuilder();
        for (char character : text.toCharArray()) {
            builder.append(character);
            String actual = builder.toString();
            Matcher matcher = match.matcher(actual);
            if (matcher.find()) {
                String[] before = text.split(matcher.group(1));
                String last = before[0].replaceAll("<$", "");
                if (!last.isEmpty()) {
                    components.addAll(Arrays.asList(TextComponent.fromLegacyText(ColorTranslator.parse(last))));
                    text = text.replace(actual, "");
                    System.out.println(actual);
                }
                String arguments = matcher.group(1);
                String between = matcher.group(2);
                ClickEvent clickEvent = null;
                HoverEvent hoverEvent = null;
                for (String arg : arguments.split(",")) {
                    if (arg.split("=").length != 2) continue;
                    String key = arg.split("=")[0];
                    String val = arg.split("=")[1];
                    if (key.equalsIgnoreCase("HOVER")) {
                        hoverEvent = new HoverEvent(HoverEvent.Action.SHOW_TEXT, TextComponent.fromLegacyText(ColorTranslator.parse(val)));
                    }
                    if (key.equalsIgnoreCase("COPY_TO_CLIPBOARD")) {
                        clickEvent = new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, val);
                    }
                    if (key.equalsIgnoreCase("OPEN_URL")) {
                        clickEvent = new ClickEvent(ClickEvent.Action.OPEN_URL, val);
                    }
                    if (key.equalsIgnoreCase("SUGGEST_COMMAND")) {
                        clickEvent = new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, val);
                    }
                    if (key.equalsIgnoreCase("RUN_COMMAND")) {
                        clickEvent = new ClickEvent(ClickEvent.Action.RUN_COMMAND, val);
                    }
                }
                for (BaseComponent var : TextComponent.fromLegacyText(ColorTranslator.parse(between))) {
                    if (clickEvent != null) var.setClickEvent(clickEvent);
                    if (hoverEvent != null) var.setHoverEvent(hoverEvent);
                    components.add(var);
                }
                builder = new StringBuilder();
            }
        }
        components.addAll(Arrays.asList(TextComponent.fromLegacyText(ColorTranslator.parse(builder.toString()))));
        return components.toArray(new BaseComponent[0]);
    }
    
    private static void sendSuccess(CommandSender sender, AnimationTypes type, List<Player> receivers, AmazingComponent title) {
        int frames = title.frames().size();
        int speed = 20/title.speed();
        int duration = title.duration();
        int players = receivers.size();
        String animation = type.name();
        BaseComponent[] message = new ComponentBuilder()
         .appendLegacy("\n")
         .appendLegacy(ColorTranslator.parse("&{#fff34d}AT &7-> &{#fff34d}Title &7dispatcher info:\n"))
         .appendLegacy("\n")
         .appendLegacy(ColorTranslator.parse("  &7Receivers: &{#ffb866}" + players + "\n"))
         .appendLegacy(ColorTranslator.parse("  &7Animation: &{#ffb866}" + animation + "\n"))
         .appendLegacy(ColorTranslator.parse("  &7Frames: &{#ffb866}" + frames + "\n"))
         .appendLegacy(ColorTranslator.parse("  &7Speed: &{#ffb866}" + speed + " &8(Frames per second)\n"))
         .appendLegacy(ColorTranslator.parse("  &7Duration: &{#ffb866}" + duration + " &8(In seconds)\n"))
         .appendLegacy("")
         .create();
        sender.spigot().sendMessage(message);
    }
    
}