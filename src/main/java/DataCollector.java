import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class DataCollector {

    private final static Logger logger = LogManager.getLogger(DataCollector.class.getName());

    private final static int numOfThread = 5;

    private List<Department> courses;
    private List<TimeStamp> timeStamps;

    public DataCollector(List<Department> courses) {
        this.courses = courses;
        this.timeStamps = new ArrayList<>();
    }

    public void collectData() {

        List<Thread> threads = new ArrayList<>();

        int index = 1;

        for (Department department : courses) {
            if (department.getDepartmentName().contains("ASDN")) {
                Thread temp = new Thread(() -> {

                    logger.info("collecting department " + index + "/" + courses.size() + " - " + department.getDepartmentName() + " courses");
//                timeStamps.add(collectCoursesFromDepartment(subDriver));
                    collectDataOfCourse(department);
                    logger.info("collected department " + department.getDepartmentName() + " courses");

                });
                threads.add(temp);
            }
        }


        //thread pool, controlling number of threads running at the same time
        ExecutorService pool = Executors.newFixedThreadPool(numOfThread);
        for (Thread thread : threads) {
            pool.execute(thread);
        }
        pool.shutdown();

        //wait till all threads finished, then all courses collected
        try {
            pool.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //wait till all threads finished, then all courses collected
        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

    public void collectDataOfCourse(Department department) {

        WebDriver driver = SeleniumUtility.initWebDriver();

        for (Course course : department.getCourses()) {
            driver.get(course.getUrl());
            SeleniumUtility.waitPresence(driver, "id", "correctPage");

            List<WebElement> sections = driver.findElement(By.className("dataTables_wrapper")).findElements(By.tagName("tr"));

            for (WebElement section : sections) {

                //filter out not useful section
                if (section.getAttribute("class").equalsIgnoreCase("odd")||section.getAttribute("class").equalsIgnoreCase("even")) {

                    List<WebElement> fields = section.findElements(By.tagName("td"));


                    String sectionName = fields.get(0).getText();
                    String lectureTime = fields.get(1).getText();
                    String instructor = fields.get(2).getText();
                    String location = fields.get(3).getText();
                    String courseSize = fields.get(4).getText();
                    String currentEnrolment = fields.get(5).getText();
                    String optionToWaitlist = fields.get(6).getText();
                    String deliveryMode = fields.get(7).getText();

                    timeStamps.add(new TimeStamp(course.getCode(),course.getTerm(), currentEnrolment, courseSize, sectionName, location, lectureTime, instructor));
                }

            }

        }
        driver.quit();

    }

    public List<TimeStamp> getTimeStamps() {
        return timeStamps;
    }
}
