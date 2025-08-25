package temp;

import javax.swing.text.DateFormatter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class EmailGenerator {
    public String generateEmail() {
        LocalDateTime date=LocalDateTime.now();
        DateTimeFormatter dateFormatter= DateTimeFormatter.ofPattern("YYYYMMDDHHmm");
        String formattedDate=dateFormatter.format(date);
        String mail="mahinfathima1116"+formattedDate+"@gmail.com";
        return mail;
    }
}
