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

            int i = 5000000;
            String temp = "";
            for (Department department : sqlManager.getCourses()) {
                if (department.getSize()<=i){
                    i =department.getSize();
                    temp = department.getCourses().get(0).getDepartment();
                }
                for (Course c : department.getCourses()) {
                    System.out.println(c);
                }
            }
            System.out.println(i);
            System.out.println(temp);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
