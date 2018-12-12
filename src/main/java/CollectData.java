import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.SQLException;
import java.util.List;

public class CollectData {

    private final static Logger logger = LogManager.getLogger(CollectData.class.getName());

    public static void main(String[] args) {
        try {

            SQLManager sqlManager = new SQLManager();

//            int i = 5000000;
//            String temp = "";
//            for (Department department : sqlManager.getCourses()) {
//                if (department.getSize()<=i){
//                    i =department.getSize();
//                    temp = department.getCourses().get(0).getDepartment();
//                }
//                for (Course c : department.getCourses()) {
//                    System.out.println(c);
//                }
//            }
//            System.out.println(i);
//            System.out.println(temp);

            //options: getCourses(), getCurrentYearCourses(), getCurrentSemesterCourses()
            List<Department> courses = sqlManager.getCourses();

            DataCollector dataCollector = new DataCollector(courses);
            dataCollector.collectData();
            List<TimeStamp> timeStamps = dataCollector.getTimeStamps();

            int i = 1;
            int size = timeStamps.size();
            for (TimeStamp timeStamp : timeStamps) {
                sqlManager.insertTimeStamp(timeStamp);
                logger.info("updated timestamp " + i + "/" + size + " - " + timeStamp.getCode());
                i++;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
