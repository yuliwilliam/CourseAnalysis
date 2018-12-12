import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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

    public void insertTimeStamp(TimeStamp timeStamp) throws SQLException {
        mySQLdb.runUpdate("insert into timeStamps (code, term, dataTime, currentEnrolment, courseSize" +
                ",section, location, lectureTime, instructor) values (" + timeStamp.generateQueryValues() + ")");
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
            Department department = new Department(name);
            for (Object course : courseUnderDepartment) {
                JSONObject temp = (JSONObject) course;
                department.addCourse(new Course(temp.getString("code"), temp.getString("courseName"), temp.getString("credits"), temp.getString("campus"), temp.getString("department"), temp.getString("term"), temp.getString("division"), temp.getString("url")));
            }
            courses.add(department);
        }
        return courses;
    }

    public List<Department> getCurrentYearCourses() throws SQLException {

        DateFormat yearFormat = new SimpleDateFormat("yyyy");
        DateFormat monthFormat = new SimpleDateFormat("mm");
        Calendar cal = Calendar.getInstance();
        String year = yearFormat.format(cal.getTime());
        String month = monthFormat.format(cal.getTime());

        String term1;
        String term2;
        String term3;

        if (month.equals("01") || month.equals("02") || month.equals("03") || month.equals("04") || month.equals("05") || month.equals("06")) {
            term1 = (Integer.parseInt(year) - 1) + " Fall";
            term2 = year + " Winter";
            term3 = (Integer.parseInt(year) - 1) + " Fall +";


        } else {
            term1 = year + " Fall";
            term2 = (Integer.parseInt(year) + 1) + " Winter";
            term3 = year + " Fall +";

        }

        List<Department> courses = new ArrayList<>();
        //arrange courses by department
        JSONArray departmentNames = mySQLdb.runQuery("select distinct department from courses");
        for (Object departmentName : departmentNames) {
            String name = ((JSONObject) departmentName).getString("department").replaceAll("'", "''");
            JSONArray courseUnderDepartment = mySQLdb.runQuery(String.format("select * from courses where department='%s' and (term='%s' or term='%s' or term='%s')", name, term1, term2, term3));
            Department department = new Department(name);
            for (Object course : courseUnderDepartment) {
                JSONObject temp = (JSONObject) course;
                department.addCourse(new Course(temp.getString("code"), temp.getString("courseName"), temp.getString("credits"), temp.getString("campus"), temp.getString("department"), temp.getString("term"), temp.getString("division"), temp.getString("url")));
            }
            courses.add(department);
        }
        return courses;

    }

    public List<Department> getCurrentSemesterCourses() throws SQLException {

        DateFormat yearFormat = new SimpleDateFormat("yyyy");
        DateFormat monthFormat = new SimpleDateFormat("mm");
        Calendar cal = Calendar.getInstance();
        String year = yearFormat.format(cal.getTime());
        String month = monthFormat.format(cal.getTime());

        String term1;
        String term2;

        if (month.equals("01") || month.equals("02") || month.equals("03") || month.equals("04") || month.equals("05") || month.equals("06")) {
            term1 = year + " Winter";
            term2 = (Integer.parseInt(year) - 1) + " Fall +";


        } else {
            term1 = year + " Fall";
            term2 = year + " Fall +";

        }

        List<Department> courses = new ArrayList<>();
        //arrange courses by department
        JSONArray departmentNames = mySQLdb.runQuery("select distinct department from courses");
        for (Object departmentName : departmentNames) {
            String name = ((JSONObject) departmentName).getString("department").replaceAll("'", "''");
            JSONArray courseUnderDepartment = mySQLdb.runQuery(String.format("select * from courses where department='%s' and (term='%s' or term='%s')", name, term1, term2));
            Department department = new Department(name);
            for (Object course : courseUnderDepartment) {
                JSONObject temp = (JSONObject) course;
                department.addCourse(new Course(temp.getString("code"), temp.getString("courseName"), temp.getString("credits"), temp.getString("campus"), temp.getString("department"), temp.getString("term"), temp.getString("division"), temp.getString("url")));
            }
            courses.add(department);
        }
        return courses;


    }

}

