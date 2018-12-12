import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class AcornDataCollector {
    private final static Logger logger = LogManager.getLogger(AcornDataCollector.class.getName());
    private final String USERNAME = "";
    private final String PASSWORD = "";

    private final static int numOfThread = 5;

    private List<Department> courses;

    public AcornDataCollector(List<Department> courses) {
        this.courses = courses;
    }

    public List<TimeStamp> collectData() {

        List<TimeStamp> timeStamps = new ArrayList<>();

        List<Thread> threads = new ArrayList<>();
        for (int i = 0; i < courses.size(); i++) {

            if (courses.get(i).getCourses().get(0).getDepartment().equals("Chemistry")) {
                final int index = i;
                //init a individual driver for each thread
                Thread temp = new Thread(() -> {
                    logger.info("collecting category " + (index + 1) + "/" + courses.size() + " - " + courses.get(index).getCourses().get(0).getDepartment() + " courses");
                    timeStamps.addAll(collectCoursesUnderDepartment(courses.get(index)));
                    logger.info("collected category " + courses.get(index).getCourses().get(0).getDepartment() + " courses");
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

        return timeStamps;

    }

    public List<TimeStamp> collectCoursesUnderDepartment(Department department) {

        List<TimeStamp> timeStamps = new ArrayList<>();

        WebDriver driver = SeleniumUtility.initWebDriver();
        driver.get("https://acorn.utoronto.ca/");
        SeleniumUtility.waitClickable(driver, "name", "_eventId_proceed");
        driver.findElement(By.id("username")).sendKeys(USERNAME);
        driver.findElement(By.id("password")).sendKeys(PASSWORD);
        driver.findElement(By.name("_eventId_proceed")).click();
        driver.get("https://acorn.utoronto.ca/sws/welcome.do?welcome.dispatch#/courses/1");
        SeleniumUtility.waitClickable(driver, "cssSelector", "#tabcontent-1 > div.currentCoursesBox > div:nth-child(4) > div > div:nth-child(2) > div:nth-child(1) > div");
        SeleniumUtility.waitClickable(driver, "id", "typeaheadInput");

        logger.info("logged in to account");


        for (Course course : department.getCourses()) {
            driver.findElement(By.id("typeaheadInput")).clear();
            driver.findElement(By.id("typeaheadInput")).sendKeys(course.getCode());
            driver.findElement(By.id("typeaheadInput")).click();
            logger.info("searching for course " + course.getCode());

            waitForDropListAppear(driver);

            List<WebElement> courses = driver.findElements(By.tagName("li"));
            while (courses.size() == 0) {
                courses = driver.findElements(By.tagName("li"));
                logger.warn("cannot find " + course.getCode() + ", relocating course elements");
            }

            for (WebElement dropListItem : courses) {
                String dropListText = dropListItem.getText();
                if (dropListText.contains(course.getCode())) {
                    logger.info("course " + course.getCode() + " located");
                    dropListItem.click();
                    SeleniumUtility.waitPresence(driver, "className", "modal-body");
                    logger.info("collecting data");
                    List<WebElement> lectures = driver.findElement(By.className("modal-body")).findElements(By.className("primaryActivity"));
                    List<WebElement> tutorials = driver.findElement(By.className("modal-body")).findElements(By.className("secondaryActivities"));


                    for (WebElement element : lectures) {
                        if (!element.getAttribute("class").contains("ng-hide")) {
                            timeStamps.add(parseTimeStamp(course.getCode(), dropListText.substring(dropListText.indexOf(" ") + 1, dropListText.indexOf(" ") + 1 + 1), element));
                        }
                    }
                    for (WebElement element : tutorials) {
                        timeStamps.add(parseTimeStamp(course.getCode(), dropListText.substring(dropListText.indexOf(" ") + 1, dropListText.indexOf(" ") + 1 + 1), element));
                    }

                    logger.info("collected data");
                    driver.findElement(By.tagName("body")).sendKeys(Keys.ESCAPE);
                    waitForDropListAppear(driver);
                    driver.findElement(By.id("typeaheadInput")).click();

                }
            }
            driver.findElement(By.tagName("body")).sendKeys(Keys.ESCAPE);

        }
        driver.quit();
        return timeStamps;
    }

    private TimeStamp parseTimeStamp(String code, String term, WebElement element) {

        String section = element.findElement(By.className("activity")).getAttribute("innerText");
        String lectureTime = element.findElement(By.className("time")).getAttribute("innerText");
        String location = element.findElement(By.className("location")).getAttribute("innerText");
        String instructor = element.findElement(By.className("instructor")).getAttribute("innerText");
        String spaceAvailability = element.findElement(By.className("spaceAvailability")).getAttribute("innerText");

        String courseSize;
        String availableSpot;

        String[] lst = spaceAvailability.split(System.getProperty("line.separator"));
        String temp = "";
        for (int i = 0; i < lst.length; i++) {
            if (lst[i].contains("of") && lst[i].contains("available")) {
                temp = lst[i];
            }
        }

        if (!temp.equals("")) {
            //remove leading spaces
            int i = 0;
            while (Character.toString(temp.charAt(0)).equals(" ")) {
                i++;
                temp = temp.substring(i);
            }
        }

        if (spaceAvailability.contains("of") && spaceAvailability.contains("available")) {
            System.out.println("----------------" + temp);
            courseSize = temp.substring(temp.indexOf("of") + 3, temp.indexOf("available") - 1);
            availableSpot = temp.substring(0, temp.indexOf(" "));
        } else {
            courseSize = spaceAvailability;
            availableSpot = spaceAvailability;
        }
        //TODO: if called  section full or like that, no avaibility

        return new TimeStamp(code, term, availableSpot, courseSize, section, location, lectureTime, instructor);

    }


    private void waitForDropListAppear(WebDriver driver) {
        WebElement searchingBar = driver.findElement(By.id("typeaheadInput"));
        while (searchingBar.getAttribute("class").contains("loadingIcon") || searchingBar.getAttribute("class").contains("ng-empty")) {
        }
        while (driver.findElement(By.cssSelector("#typeahead-search > div.ut-typeahead-container")).getAttribute("search-view-more-message").contains("Scroll down to show more results... (0 Courses found)")) {
        }

        while (searchingBar.getAttribute("aria-expanded").contains("false")) {
            searchingBar.click();
        }

        while (driver.findElement(By.cssSelector("#typeahead-search > div.ut-typeahead-container > form > div > div > div")).getAttribute("style").contains("display: none;")) {
        }

//        try {
//            TimeUnit.SECONDS.sleep(1);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
    }
}
