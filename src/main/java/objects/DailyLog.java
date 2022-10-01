package objects;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;
@Data
@AllArgsConstructor

public class DailyLog {

    private String person;
    private Date date;
    private int duration;
}
