import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.SQLException;

public class Test {


    public static void main(String[] args) {
        try {

            Course course = new Course("CSC165H1", "", "", "", "", "", "");

            SQLManager sqlManager = new SQLManager();

//            if (!sqlManager.courseExist(course)) {
//                System.out.println("updating data for course " + course.getCode() );
////                sqlManager.insertCourse(course);
//            }
//            else {
//                System.out.println(("course exists"));
//            }

            for (Department department : sqlManager.getCourses()) {
                for (Course c : department.getCourses()) {
                    System.out.println(c);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
