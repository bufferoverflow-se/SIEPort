package se.bufferoverflow.sieport.sie4;

import java.nio.charset.Charset;
import java.time.format.DateTimeFormatter;

public interface Constants {
    Charset SIE4_CHARSET = Charset.forName("IBM-437");
    DateTimeFormatter SIE4_DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd");
}
