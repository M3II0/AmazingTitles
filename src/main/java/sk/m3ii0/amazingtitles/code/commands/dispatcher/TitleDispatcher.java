package sk.m3ii0.amazingtitles.code.commands.dispatcher;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import sk.m3ii0.amazingtitles.code.async.AmazingTitle;
import sk.m3ii0.amazingtitles.code.async.animations.ATBounce;
import sk.m3ii0.amazingtitles.code.async.animations.ATRainbow;
import sk.m3ii0.amazingtitles.code.async.animations.ATWaves;
import sk.m3ii0.amazingtitles.code.commands.types.AnimationTypes;

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
        } catch (Exception e) {
            e.printStackTrace();
            sendError(s);
        }
    }

    private static AmazingTitle buildTitle(AnimationTypes type, String[] args) {
        if (type == AnimationTypes.RAINBOW) {
            int speed = Integer.parseInt(args[0]);
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
            int speed = Integer.parseInt(args[0]);
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
            int speed = Integer.parseInt(args[0]);
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
        return null;
    }

    private static void sendError(CommandSender s) {
        s.sendMessage("§cAT §7-> §4Error with dispatching... (Check your format)");
    }

}
