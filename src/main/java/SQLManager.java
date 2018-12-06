import org.json.JSONArray;

import java.sql.SQLException;

public class SQLManager {

    private MySQLdb mySQLdb;
    private final String USERNAME = "superuser";
    private final String PASSWORD = "pass";
    private final String HOST = "jdbc:mysql://localhost:3306/course_data";

    public SQLManager() {
        this.mySQLdb = new MySQLdb(HOST, USERNAME, PASSWORD);
    }

    public void insertCourse(Course course) throws SQLException {
        mySQLdb.runUpdate("insert into courses (code, courseName, credits, campus, department" +
                ", term, division) values (" + course.generateQueryValues() + ")");
    }

    public boolean courseExist(Course course) throws SQLException {
        JSONArray results = mySQLdb.runQuery("select code from courses where code='" + course.getCode().replaceAll("'", "''") + "'");
        return !results.isEmpty();
    }


}
