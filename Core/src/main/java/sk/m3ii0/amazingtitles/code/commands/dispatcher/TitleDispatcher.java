package sk.m3ii0.amazingtitles.code.commands.dispatcher;

import net.md_5.bungee.api.chat.*;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import sk.m3ii0.amazingtitles.api.objects.AmazingComponent;
import sk.m3ii0.amazingtitles.api.objects.AmazingCreator;
import sk.m3ii0.amazingtitles.api.objects.types.ActionType;
import sk.m3ii0.amazingtitles.code.AmazingTitles;
import sk.m3ii0.amazingtitles.code.colors.ColorTranslator;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TitleDispatcher {
    
    private static final Pattern match = Pattern.compile("<(.*?)>(.*?)</>");
    private static final DecimalFormat format = new DecimalFormat("#.###");
    
    public static void asyncDispatch(CommandSender s, ActionType action, String type, List<Player> receivers, String[] args) {
        try {
            long nanos = -System.nanoTime();
            AmazingComponent title = buildTitle(receivers.toArray(new Player[0]), action, type, args);
            if (title == null) {
                sendError(s);
                return;
            }
            nanos += System.nanoTime();
            String took = format.format(nanos/1e+6);
            sendSuccess(s, took, type, receivers, title);
        } catch (Exception e) {
            e.printStackTrace();
            sendError(s);
        }
    }

    private static AmazingComponent buildTitle(Player[] receivers, ActionType action, String animation, String[] args) {
        try {
            int rawSpeed = Integer.parseInt(args[0]);
            int speed = Math.max(rawSpeed, 1);
            int duration = Integer.parseInt(args[1]);
            AmazingCreator creator = AmazingTitles.getCustomComponents().get(animation);
            if (creator == null) return null;
            int minimum = creator.getMinimum();
            Object[] objects = new Object[minimum];
            System.arraycopy(args, 2, objects, 0, minimum);
            StringBuilder text = new StringBuilder();
            for (int i = minimum+2; i < args.length; i++) {
                text.append(" ").append(args[i]);
            }
            if (text.length() > 0) text = new StringBuilder(text.substring(1));
            String[] split = text.toString().split("\\\\n\\\\");
            String input = split[0];
            String subText = "";
            if (split.length > 1) {
                subText = ColorTranslator.parse(split[1]);
            }
            return creator.dispatch(receivers, action, speed, duration, input, subText, objects);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
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
    
    private static void sendSuccess(CommandSender sender, String took, String type, List<Player> receivers, AmazingComponent title) {
        if (!AmazingTitles.getOptions().getBoolean("DispatchingMessage.Enabled")) return;
        int frames = title.frames().size();
        int duration = title.duration();
        int players = receivers.size();
        ComponentBuilder builder = new ComponentBuilder("");
        List<String> message = AmazingTitles.getOptions().getStringList("DispatchingMessage.Message");
        int last = message.size()-1;
        int now = 0;
        for (String line : message) {
            builder.append(ColorTranslator.parse(parsePlaceholders(line, frames, duration, players, type, took)));
            if (last != now) builder.append("\n");
            ++now;
        }
        sender.spigot().sendMessage(builder.create());
    }
    
    private static String parsePlaceholders(String text, int frames, int duration, int players, String type, String took) {
        return text.replace("%receivers%", players + "").replace("%animation%", type).replace("%frames%", frames + "")
         .replace("%duration%", duration + "").replace("%creation%", took);
    }
    
}