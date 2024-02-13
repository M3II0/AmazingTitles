package sk.m3ii0.amazingtitles.code.Iintegrations;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Map;

public class StringUtils {

    public static void execute(List<String> input, Map<String, String> placeholders) {
        for (String var : input) {
            for (Map.Entry<String, String> entry : placeholders.entrySet()) {
                var = var.replace(entry.getKey(), entry.getValue());
            }
            if (var.startsWith("/") && var.length() > 1) var = var.substring(1);
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), var);
        }
    }

    public static boolean conditions(List<String> input, Player player) {
        for (String var : input) {
            String[] split = var.split("::");
            if (split.length < 2) continue;
            String key = var.split("::")[0];
            String value = var.split("::")[1];
            if (key.equalsIgnoreCase("permission")) {
                if (!player.hasPermission(value)) return false;
                else continue;
            }
            if (key.equalsIgnoreCase("world")) {
                if (!player.getLocation().getWorld().getName().equalsIgnoreCase(value)) return false;
                else continue;
            }

        }
        return true;
    }

    private static String parseLocation(Location location) {
        return "World:" + location.getWorld().getName() + ",X:" + location.getBlockX() + ",Y:" + location.getBlockY() + ",Z:" + location.getBlockZ();
    }

}
