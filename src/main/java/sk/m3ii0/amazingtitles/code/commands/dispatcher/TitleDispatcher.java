package sk.m3ii0.amazingtitles.code.commands.dispatcher;

import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import sk.m3ii0.amazingtitles.code.async.AmazingTitle;
import sk.m3ii0.amazingtitles.code.async.animations.*;
import sk.m3ii0.amazingtitles.code.colors.ColorTranslator;
import sk.m3ii0.amazingtitles.code.commands.types.AnimationTypes;

import java.awt.*;
import java.util.List;

public class TitleDispatcher {

    public static void asyncDispatch(CommandSender s, AnimationTypes type, List<Player> receivers, String[] args) {
        try {
            AmazingTitle title = buildTitle(type, args);
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

    private static AmazingTitle buildTitle(AnimationTypes type, String[] args) {
        if (type == AnimationTypes.RAINBOW) {
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
            return new ATRainbow(title, subtitle, speed, duration);
        }
        if (type == AnimationTypes.WAVES) {
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
            return new ATWaves(title, subtitle, color1, color2, speed, duration);
        }
        if (type == AnimationTypes.BOUNCE) {
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
            return new ATBounce(title, subtitle, color1, color2, speed, duration);
        }
        if (type == AnimationTypes.WRITER_STAY) {
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
            return new ATWriterStay(title, subtitle, writer, speed, duration);
        }
        if (type == AnimationTypes.PULSING) {
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
            return new ATPulsing(title, subtitle, color1, color2, speed, duration);
        }
        if (type == AnimationTypes.COMING_FROM_RIGHT) {
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
            return new ATFromRight(title, subtitle, speed, duration);
        }
        if (type == AnimationTypes.COMING_FROM_LEFT) {
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
            return new ATFromLeft(title, subtitle, speed, duration);
        }
        if (type == AnimationTypes.COMING_FROM_SIDES) {
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
            return new ATFromSides(title, subtitle, speed, duration);
        }
        if (type == AnimationTypes.OPEN) {
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
            return new ATOpen(title, subtitle, color1, speed, duration);
        }
        if (type == AnimationTypes.FLASHING) {
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
            return new ATFlashing(title, subtitle, speed, duration);
        }
        if (type == AnimationTypes.SPACE_SPLIT) {
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
            return new ATWordSplit(title, subtitle, speed, duration);
        }
        if (type == AnimationTypes.SPLIT) {
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
            return new ATSplit(title, subtitle, speed, duration);
        }
        return null;
    }

    private static void sendError(CommandSender s) {
        s.sendMessage("§cAT §7-> §4Error with dispatching... (Check your format)");
    }

    private static void sendSuccess(CommandSender sender, AnimationTypes type, List<Player> receivers, AmazingTitle title) {
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
