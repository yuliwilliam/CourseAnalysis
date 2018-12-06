import java.util.ArrayList;
import java.util.List;

public class Division {

    private List<Course> courses;

    public Division() {
        this.courses = new ArrayList<>();
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
        return "Division{" +
                "courses=" + courses +
                '}';
    }
}
