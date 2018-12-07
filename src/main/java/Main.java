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
            int valid = 0;
            for (Department department : courses) {
                for (Course course : department.getCourses()) {
                    if (!sqlManager.courseExist(course)) {
                        logger.info("updating data for course " + course.getCode() + " " + i + "/" + totalCourse);
                        sqlManager.insertCourse(course);
                        valid++;
                    } else {
                        logger.info("course " + course.getCode() + " exists " + i + "/" + totalCourse);
                    }
                    i++;
                }
            }
            logger.info("finished updating databases, " + valid + " valid new record");

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
