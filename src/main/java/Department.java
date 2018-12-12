import java.util.ArrayList;
import java.util.List;

public class Department {

    private List<Course> courses;
    private String departmentName;

    public String getDepartmentName() {
        return departmentName;
    }

    public Department(String departmentName) {
        this.courses = new ArrayList<>();
        this.departmentName = departmentName;
    }

    public void addCourse(Course course) {
        if (!courseExist(course)) {
            courses.add(course);
        }
    }

    public List<Course> getCourses() {
        return courses;
    }

    public int getSize() {
        return courses.size();
    }

    private boolean courseExist(Course course) {
        return courses.contains(course);

    }

    @Override
    public String toString() {
        return "Department{" +
                "courses=" + courses +
                '}';
    }
}
