import java.util.Objects;

public class Course {

    private String code;
    private String courseName;
    private String credits;
    private String campus;
    private String department;
    private String term;
    private String division;

    public Course(String code, String courseName, String credits, String campus, String department, String term, String division) {
        this.code = code;
        this.courseName = courseName;
        this.credits = credits;
        this.campus = campus;
        this.department = department;
        this.term = term;
        this.division = division;
    }

    public String getCode() {
        return code;
    }

    public String getCourseName() {
        return courseName;
    }

    public String getCredits() {
        return credits;
    }

    public String getCampus() {
        return campus;
    }

    public String getDepartment() {
        return department;
    }

    public String getTerm() {
        return term;
    }

    public String getDivision() {
        return division;
    }

    public String generateQueryValues() {
        return String.format("'%s', '%s', '%s', '%s', '%s', '%s', '%s'",
                getCode().replaceAll("'", "''"), getCourseName().replaceAll("'", "''"), getCredits().replaceAll("'", "''"), getCampus().replaceAll("'", "''"),
                getDepartment().replaceAll("'", "''"), getTerm().replaceAll("'", "''"), getDivision().replaceAll("'", "''"));
    }

    /*
    course in different term consider same course
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Course course = (Course) o;
        return Objects.equals(code, course.code) &&
                Objects.equals(courseName, course.courseName) &&
                Objects.equals(credits, course.credits) &&
                Objects.equals(campus, course.campus) &&
                Objects.equals(department, course.department) &&
                Objects.equals(division, course.division);
    }


    @Override
    public String toString() {
        return "Course{" +
                "code='" + code + '\'' +
                ", courseName='" + courseName + '\'' +
                ", credits='" + credits + '\'' +
                ", campus='" + campus + '\'' +
                ", department='" + department + '\'' +
                ", term='" + term + '\'' +
                ", division='" + division + '\'' +
                '}';
    }
}
