package sk.m3ii0.amazingtitles.code.internal.utils;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;

public class TimesUtility {

    // $td{days(date;1,1,2024)->(date;3,1,2024)} # Days to date
    // $td{hours(date;1,1,2024)->(date;3,1,2024)} # Hours to date
    // $td{minutes(date;1,1,2024)->(date;3,1,2024)} # Minutes to date
    // $td{seconds(date;1,1,2024)->(date;3,1,2024)} # Seconds to date

    // $td{hours(time;16:40)->(time;17:50)} # Hours to date
    // $td{minutes(time;16:40)->(time;17:50)} # Minutes to date
    // $td{seconds(time;16:40)->(time;17:50)} # Seconds to date

    public static Optional<String> getResult(String input) {
        if (input == null) {
            return Optional.empty();
        }
        input = input.replace("$td{", "").replace("}", "");
        if (input.startsWith("days")) {
            input = input.replace("days(", "").replace(")", "");
            String[] variables = input.split("->");
            if (checkVariables(variables)) {
                LocalDateTime var1 = read(variables[0]);
                LocalDateTime var2 = read(variables[1]);
                Duration duration = getDuration(var1, var2);
                return Optional.of(getNumber(TimeType.DAY, duration) + "");
            }
        }
        if (input.startsWith("hours")) {
            input = input.replace("hours(", "").replace(")", "");
            String[] variables = input.split("->");
            if (checkVariables(variables)) {
                LocalDateTime var1 = read(variables[0]);
                LocalDateTime var2 = read(variables[1]);
                Duration duration = getDuration(var1, var2);
                return Optional.of(getNumber(TimeType.HOUR, duration) + "");
            }
        }
        if (input.startsWith("minutes")) {
            input = input.replace("minutes(", "").replace(")", "");
            String[] variables = input.split("->");
            if (checkVariables(variables)) {
                LocalDateTime var1 = read(variables[0]);
                LocalDateTime var2 = read(variables[1]);
                Duration duration = getDuration(var1, var2);
                return Optional.of(getNumber(TimeType.MINUTE, duration) + "");
            }
        }
        if (input.startsWith("seconds")) {
            input = input.replace("seconds(", "").replace(")", "");
            String[] variables = input.split("->");
            if (checkVariables(variables)) {
                LocalDateTime var1 = read(variables[0]);
                LocalDateTime var2 = read(variables[1]);
                Duration duration = getDuration(var1, var2);
                return Optional.of(getNumber(TimeType.SECOND, duration) + "");
            }
        }
        return Optional.empty();
    }

    public static Duration getDuration(LocalDateTime from, LocalDateTime to) {
        return Duration.between(from, to);
    }

    public static int getNumber(TimeType timeType, Duration duration) {
        if (duration == null || timeType == null) {
            return 0;
        }
        if (timeType == TimeType.DAY) {
            return (int) duration.toDays();
        }
        if (timeType == TimeType.HOUR) {
            return (int) duration.toHours();
        }
        if (timeType == TimeType.MINUTE) {
            return (int) duration.toMinutes();
        }
        if (timeType == TimeType.SECOND) {
            return (int) (duration.toMinutes()*60);
        }
        return 0;
    }

    public enum TimeType {
        DAY(),
        HOUR(),
        MINUTE(),
        SECOND()
    }

    private static boolean checkVariables(String[] variables) {
        if (variables == null || variables.length != 2) return false;
        if (!variables[0].equals(variables[1])) return false;
        return (variables[0].equals("days") || variables[0].equals("hours") || variables[0].equals("minutes") || variables[0].equals("seconds"));
    }

    private static boolean checkTimeVariables(String[] variables) {
        if (variables == null || variables.length != 2) return false;
        int var1 = Integer.parseInt(variables[0]);
        int var2 = Integer.parseInt(variables[1]);
        return var1 <= 23 && var2 <= 59;
    }

    private static boolean checkDateVariables(String[] variables) {
        if (variables == null || variables.length != 3) return false;
        int var1 = Integer.parseInt(variables[0]);
        int var2 = Integer.parseInt(variables[1]);
        int var3 = Integer.parseInt(variables[2]);
        return (var1 < 32 && var2 < 13 && var3 > 0);
    }

    private static LocalDateTime read(String input) {
        LocalDateTime localDateTime = LocalDateTime.now();
        if (input.startsWith("date;")) {
            input = input.replace("date;", "");
            String[] dates = input.split(",");
            if (!checkDateVariables(dates)) {
                return localDateTime;
            }
            return LocalDateTime.of(Integer.parseInt(dates[0]), Integer.parseInt(dates[1]), Integer.parseInt(dates[2]), localDateTime.getHour(), localDateTime.getMinute());
        }
        if (input.startsWith("time;")) {
            input = input.replace("time;", "");
            String[] times = input.split(":");
            if (!checkTimeVariables(times)) {
                return localDateTime;
            }
            return LocalDateTime.of(localDateTime.getYear(), localDateTime.getMonth(), localDateTime.getDayOfMonth(), Integer.parseInt(times[0]), Integer.parseInt(times[1]));
        }
        return localDateTime;
    }

}
