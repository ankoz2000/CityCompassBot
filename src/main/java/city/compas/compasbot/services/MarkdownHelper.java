package city.compas.compasbot.services;

import java.util.Objects;

public class MarkdownHelper {
    public static String getTextAsLink(String text, String link) {
        if (Objects.isNull(link)) {
            return text;
        }
        return "[" + text + "]" + "(" + link + ")";
    }
}
