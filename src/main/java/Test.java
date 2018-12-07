import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.SQLException;

public class Test {


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

            DataCollector dataCollector = new DataCollector(sqlManager.getCourses());
            dataCollector.collectData();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
