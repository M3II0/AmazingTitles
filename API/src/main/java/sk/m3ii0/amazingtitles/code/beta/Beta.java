package sk.m3ii0.amazingtitles.code.beta;

import java.util.ArrayList;
import java.util.List;

public class Beta {

    public static void main(String[] args) {
        String text = "Formatter Color";
        List<String> frames = new ArrayList<>();
    
        for (int i = 0; i < text.length(); i++) {
            StringBuilder builder = new StringBuilder();
            int counter = 0;
            for (char var : text.toCharArray()) {
                if (counter == i) builder.append(" | ");
                else builder.append(var);
                ++counter;
            }
            frames.add(builder.toString());
        }
    
        for (int i = frames.size()-1; i > -1; i--) {
            frames.add(frames.get(i));
        }
    }

}
