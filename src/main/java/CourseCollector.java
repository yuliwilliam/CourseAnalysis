import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class CourseCollector {

    private final static Logger logger = LogManager.getLogger(CourseCollector.class.getName());

    private final static int numOfThread = 5;

    private List<Department> courses;

    public CourseCollector() {
        courses = new ArrayList<>();
    }

    public void collectCourse() {
        WebDriver driver = SeleniumUtility.initWebDriver();
        driver.get("http://coursefinder.utoronto.ca/course-search/search/courseSearch?viewId=CourseSearch-FormView&methodToCall=start");
        SeleniumUtility.waitPresence(driver, "tagName", "body");


        WebElement table = driver.findElement(By.id("u208_boxLayout"));
        List<WebElement> departments = new ArrayList<>();

        for (WebElement d : table.findElements(By.className("uif-link"))) {
            if (!d.getText().equals("")) {
                departments.add(d);
            }
        }
        logger.info("collected all " + departments.size() + " departments of courses");
        List<Thread> threads = new ArrayList<>();

        for (int i = 0; i < departments.size(); i++) {
            final int index = i;
            Thread temp = new Thread(() -> {
                logger.info("collecting category " + (index + 1) + "/" + departments.size() + " - " + departments.get(index).getText() + " courses");
                WebDriver subDriver = SeleniumUtility.initWebDriver();
                subDriver.get(driver.getCurrentUrl());
                subDriver.findElement(By.id(departments.get(index).getAttribute("id"))).click();
                courses.add(collectCourseInCategory(subDriver));
                subDriver.quit();
                logger.info("collected category " + departments.get(index).getText() + " courses");
            });
            threads.add(temp);
        }

        ExecutorService pool = Executors.newFixedThreadPool(numOfThread);
        for (Thread thread : threads) {
            pool.execute(thread);
        }
        pool.shutdown();

        try {
            pool.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        int totalCourse = 0;
        for (Department d : courses) {
            totalCourse += d.getSize();
        }

        logger.info("collected all " + courses.size() + " departments " + totalCourse + " courses");
//        writeToTxt();
        driver.quit();
    }

    private Department collectCourseInCategory(WebDriver driver) {
        SeleniumUtility.waitVisible(driver, "id", "courseSearchResults_info");
        new Select(driver.findElement(By.name("courseSearchResults_length"))).selectByValue("100");
        SeleniumUtility.waitVisible(driver, "id", "courseSearchResults_info");
        Department department = new Department();
        while (!driver.findElement(By.id("courseSearchResults_next")).getAttribute("class").contains("disabled")) {

            List<WebElement> displayedCourse = driver.findElement(By.cssSelector("#courseSearchResults > tbody")).findElements(By.tagName("tr"));
            for (WebElement course : displayedCourse) {
                List<WebElement> fields = course.findElements(By.tagName("td"));
                department.addCourse(new Course(fields.get(1).getText(), fields.get(2).getText(), fields.get(3).getText(), fields.get(4).getText(), fields.get(5).getText(), fields.get(6).getText(), fields.get(7).getText()));
            }
            driver.findElement(By.id("courseSearchResults_next")).click();
            SeleniumUtility.waitVisible(driver, "id", "courseSearchResults_info");
        }
        //end case, courses on the last page
        for (WebElement course : driver.findElement(By.cssSelector("#courseSearchResults > tbody")).findElements(By.tagName("tr"))) {
            List<WebElement> fields = course.findElements(By.tagName("td"));
            department.addCourse(new Course(fields.get(1).getText(), fields.get(2).getText(), fields.get(3).getText(), fields.get(4).getText(), fields.get(5).getText(), fields.get(6).getText(), fields.get(7).getText()));
        }
        return department;
    }


    private void writeToTxt() {
        try {
            PrintWriter writer = new PrintWriter("courses.txt", "UTF-8");
            for (Department department : courses) {
                for (Course course : department.getCourses()) {
                    writer.println(course);
                }
            }
            writer.close();
        } catch (
                Exception e) {
            e.printStackTrace();
        }

    }

    public List<Department> getCourses() {
        return courses;
    }
}
