import org.json.JSONArray;

import java.sql.SQLException;

public class Test {
    public static void main(String[] args) {
        try {
            MySQLdb mySQLdb = new MySQLdb("jdbc:mysql://localhost:3306/course_data", "superuser", "pass");
            JSONArray results = mySQLdb.runQuery("select * from courses");
            for (Object result : results) {
                System.out.println(result);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
