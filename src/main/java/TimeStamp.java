import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class TimeStamp {

    private String code;
    private String term;
    private String dataTime;
    private String currentEnrolment;
    private String courseSize;
    private String section;
    private String location;
    private String lectureTime;
    private String instructor;

    public TimeStamp(String code, String term, String dataTime, String currentEnrolment, String courseSize, String section, String location, String lectureTime, String instructor) {
        this.code = code;
        this.term = term;
        this.dataTime = dataTime;
        this.currentEnrolment = currentEnrolment;
        this.courseSize = courseSize;
        this.section = section;
        this.location = location;
        this.lectureTime = lectureTime;
        this.instructor = instructor;
    }

    public TimeStamp(String code, String term, String currentEnrolment, String courseSize, String section, String location, String lectureTime, String instructor) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Calendar cal = Calendar.getInstance();
        this.code = code;
        this.term = term;
        this.dataTime = dateFormat.format(cal.getTime());
        this.currentEnrolment = currentEnrolment;
        this.courseSize = courseSize;
        this.section = section;
        this.location = location;
        this.lectureTime = lectureTime;
        this.instructor = instructor;
    }

    public String generateQueryValues() {

        String newLine = System.getProperty("line.separator");

        return String.format("'%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s'",
                getCode().replaceAll("'", "''"), getTerm().replaceAll("'", "''"), getDataTime().replaceAll("'", "''"), getCurrentEnrolment().replaceAll("'", "''"),
                getCourseSize().replaceAll("'", "''"), getSection().replaceAll("'", "''"), getLocation().replaceAll("'", "''"), getLectureTime().replaceAll("'", "''"), getInstructor().replaceAll("'", "''")).replaceAll(newLine, " ");
    }

    public String getCode() {
        return code;
    }

    public String getTerm() {
        return term;
    }

    public String getDataTime() {
        return dataTime;
    }

    public String getCurrentEnrolment() {
        return currentEnrolment;
    }

    public String getCourseSize() {
        return courseSize;
    }

    public String getSection() {
        return section;
    }

    public String getLocation() {
        return location;
    }

    public String getLectureTime() {
        return lectureTime;
    }

    public String getInstructor() {
        return instructor;
    }

    @Override
    public String toString() {
        return "TimeStamp{" +
                "code='" + code + '\'' +
                ", term='" + term + '\'' +
                ", dataTime='" + dataTime + '\'' +
                ", availableSpot='" + currentEnrolment + '\'' +
                ", courseSize='" + courseSize + '\'' +
                ", section='" + section + '\'' +
                ", location='" + location + '\'' +
                ", lectureTime='" + lectureTime + '\'' +
                ", instructor='" + instructor + '\'' +
                '}';
    }
}
