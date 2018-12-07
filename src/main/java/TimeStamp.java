import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class TimeStamp {

    private String code;
    private String term;
    private String dataTime;
    private String availableSpot;
    private String courseSize;
    private String section;
    private String location;
    private String lectureTime;
    private String instructor;

    public TimeStamp(String code, String term, String dataTime, String availableSpot, String courseSize, String section, String location, String lectureTime, String instructor) {
        this.code = code;
        this.term = term;
        this.dataTime = dataTime;
        this.availableSpot = availableSpot;
        this.courseSize = courseSize;
        this.section = section;
        this.location = location;
        this.lectureTime = lectureTime;
        this.instructor = instructor;
    }

    public TimeStamp(String code, String term, String availableSpot, String courseSize, String section, String location, String lectureTime, String instructor) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Calendar cal = Calendar.getInstance();
        this.code = code;
        this.term = term;
        this.dataTime = dateFormat.format(cal.getTime());
        this.availableSpot = availableSpot;
        this.courseSize = courseSize;
        this.section = section;
        this.location = location;
        this.lectureTime = lectureTime;
        this.instructor = instructor;
    }

    @Override
    public String toString() {
        return "TimeStamp{" +
                "code='" + code + '\'' +
                ", term='" + term + '\'' +
                ", dataTime='" + dataTime + '\'' +
                ", availableSpot='" + availableSpot + '\'' +
                ", courseSize='" + courseSize + '\'' +
                ", section='" + section + '\'' +
                ", location='" + location + '\'' +
                ", lectureTime='" + lectureTime + '\'' +
                ", instructor='" + instructor + '\'' +
                '}';
    }
}
