import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.SQLException;
import java.util.List;

public class Main {

    private final static Logger logger = LogManager.getLogger(Main.class.getName());

    public static void main(String[] args) {

        CourseCollector collector = new CourseCollector();
        collector.collectCourse();
        List<Department> courses = collector.getCourses();

        try {
            SQLManager sqlManager = new SQLManager();

            int totalCourse = 0;
            for (Department d : courses) {
                totalCourse += d.getSize();
            }
            int i = 1;
            for (Department department : courses) {
                for (Course course : department.getCourses()) {
                    if (!sqlManager.courseExist(course)) {
                        logger.info("updating data for course " + course.getCode() + " " + i + "/" + totalCourse);
                        i++;
                        sqlManager.insertCourse(course);
                    }
                    else {
                        logger.info("course exists");
                    }
                }
            }
            logger.info("finished updating databases");

            //return all query
//            JSONArray results = mySQLdb.runQuery("select * from courses");
//            for (Object result : results) {
//                System.out.println(result);
//            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

//        DataCollector dataCollector = new DataCollector();
//        dataCollector.collectData();

    }
}
