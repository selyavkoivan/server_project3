package server.Consts;

import java.text.Format;
import java.text.SimpleDateFormat;

public class DateFormatter {

    public static Format DateTimeFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public static Format DateFormatter = new SimpleDateFormat("yyyy-MM-dd");
    public static String DateFormatterPattern = "yyyy-MM-dd";
}
