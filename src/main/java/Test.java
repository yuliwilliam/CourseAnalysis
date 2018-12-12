import java.sql.SQLException;
import java.util.List;

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

            AcornDataCollector acornDataCollector = new AcornDataCollector(sqlManager.getCourses());
            List<TimeStamp> timeStamps = acornDataCollector.collectData();

            for (TimeStamp t:timeStamps){
                System.out.println(t);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
