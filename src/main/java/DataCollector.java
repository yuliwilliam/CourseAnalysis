import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.lang.ref.PhantomReference;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class DataCollector {
    private final static Logger logger = LogManager.getLogger(DataCollector.class.getName());
    private final String USERNAME = "yulizhi";
    private final String PASSWORD = "YlZ19980511";

    private final static int numOfThread = 5;

    private List<Department> courses;

    public DataCollector(List<Department> courses) {
        this.courses = courses;
    }

    public void collectData() {

        List<Thread> threads = new ArrayList<>();
        for (int i = 0; i < courses.size(); i++) {

            if(courses.get(i).getCourses().get(0).getDepartment().equals("Mathematics")){
            final int index = i;
            //init a individual driver for each thread
            Thread temp = new Thread(() -> {
                logger.info("collecting category " + (index + 1) + "/" + courses.size() + " - " + courses.get(index).getCourses().get(0).getDepartment() + " courses");
                collectCoursesUnderDepartment(courses.get(index));
                logger.info("collected category " + courses.get(index).getCourses().get(0).getDepartment() + " courses");
            });
            threads.add(temp);

        }}


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

    public void collectCoursesUnderDepartment(Department department) {

        WebDriver driver = SeleniumUtility.initWebDriver();
        driver.get("https://acorn.utoronto.ca/");
        SeleniumUtility.waitClickable(driver, "name", "_eventId_proceed");
        driver.findElement(By.id("username")).sendKeys(USERNAME);
        driver.findElement(By.id("password")).sendKeys(PASSWORD);
        driver.findElement(By.name("_eventId_proceed")).click();
        driver.get("https://acorn.utoronto.ca/sws/welcome.do?welcome.dispatch#/courses/1");
        SeleniumUtility.waitClickable(driver, "cssSelector", "#tabcontent-1 > div.currentCoursesBox > div:nth-child(4) > div > div:nth-child(2) > div:nth-child(1) > div");

        logger.info("logged in to account");



        for (Course course : department.getCourses()) {
            driver.findElement(By.id("typeaheadInput")).clear();
            driver.findElement(By.id("typeaheadInput")).sendKeys("MAT135H1");
            driver.findElement(By.id("typeaheadInput")).click();
            logger.info("searching for course " + course.getCode());

            waitForDropListAppear(driver);


            WebElement targetCourse = null;
            while (targetCourse == null) {
                List<WebElement> courses = driver.findElements(By.tagName("li"));
                while (courses.size() == 0) {
                    courses = driver.findElements(By.tagName("li"));
                    logger.warn("cannot find " + course.getCode() + ", relocating course elements");
                }

                for (WebElement dropListItem : courses) {
                    String dropListText = dropListItem.getText();
                    if (dropListText.contains("MAT135H1")) {
                        targetCourse = dropListItem;
                        logger.info("course "+course.getCode()+" located");
                        targetCourse.click();
                        SeleniumUtility.waitPresence(driver, "className", "modal-body");
                        logger.info("collecting data");
                        String info = driver.findElement(By.className("modal-body")).getAttribute("innerText");
                        logger.info("collected data");
                        System.out.println(dropListText+" collected");
                        driver.findElement(By.tagName("body")).sendKeys(Keys.ESCAPE);
                        waitForDropListAppear(driver);
                        driver.findElement(By.id("typeaheadInput")).click();
//                        try {
//                            TimeUnit.SECONDS.sleep(3);
//                        } catch (InterruptedException e) {
//                            e.printStackTrace();
//                        }
                    }
                }
            }
            System.out.println("test");
            driver.findElement(By.tagName("body")).sendKeys(Keys.ESCAPE);
        }
        driver.quit();
    }


    private void waitForDropListAppear(WebDriver driver){
        WebElement searchingBar = driver.findElement(By.id("typeaheadInput"));
        while (searchingBar.getAttribute("class").contains("loadingIcon") || searchingBar.getAttribute("class").contains("ng-empty")) {
        }
        while (driver.findElement(By.cssSelector("#typeahead-search > div.ut-typeahead-container")).getAttribute("search-view-more-message").contains("Scroll down to show more results... (0 Courses found)")) {
        }
        while (searchingBar.getAttribute("aria-expanded").contains("false")){
        }
        while (driver.findElement(By.cssSelector("#typeahead-search > div.ut-typeahead-container > form > div > div > div")).getAttribute("style").contains("display: none;")){
        }

    }
}
