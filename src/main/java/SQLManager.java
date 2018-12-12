import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SQLManager {

    private MySQLdb mySQLdb;

    //default connect to this database
    private final String USERNAME = "superuser";
    private final String PASSWORD = "pass";
    private final String HOST = "jdbc:mysql://localhost:3306/course_data";

    public SQLManager() {
        this.mySQLdb = new MySQLdb(HOST, USERNAME, PASSWORD);
    }

    public void insertCourse(Course course) throws SQLException {
        mySQLdb.runUpdate("insert into courses (code, courseName, credits, campus, department" +
                ",term, division, url) values (" + course.generateQueryValues() + ")");
    }

    public boolean courseExist(Course course) throws SQLException {
        JSONArray results = mySQLdb.runQuery("select code from courses where code='" + course.getCode().replaceAll("'", "''") + "' and term='" + course.getTerm().replaceAll("'", "''") + "' and url='" + course.getUrl().replaceAll("'", "''") + "'");
        return !results.isEmpty();
    }

    public List<Department> getCourses() throws SQLException {
        List<Department> courses = new ArrayList<>();
        //arrange courses by department
        JSONArray departmentNames = mySQLdb.runQuery("select distinct department from courses");
        for (Object departmentName : departmentNames) {
            String name = ((JSONObject) departmentName).getString("department").replaceAll("'", "''");
            JSONArray courseUnderDepartment = mySQLdb.runQuery("select * from courses where department='" + name + "'");
            Department department = new Department();
            for (Object course : courseUnderDepartment) {
                JSONObject temp = (JSONObject) course;
                department.addCourse(new Course(temp.getString("code"), temp.getString("courseName"), temp.getString("credits"), temp.getString("campus"), temp.getString("department"),temp.getString("term"), temp.getString("division"), temp.getString("url")));
            }
            courses.add(department);
        }
        return courses;
    }

}

