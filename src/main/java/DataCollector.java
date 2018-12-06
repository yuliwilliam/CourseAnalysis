import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

public class DataCollector {
    private final static Logger logger = LogManager.getLogger(DataCollector.class.getName());
    private final String USERNAME = "";
    private final String PASSWORD = "";

    public void collectData() {

        WebDriver driver = SeleniumUtility.initWebDriver();
        driver.get("https://acorn.utoronto.ca/");
        SeleniumUtility.waitClickable(driver, "name", "_eventId_proceed");
        driver.findElement(By.id("username")).sendKeys(USERNAME);
        driver.findElement(By.id("password")).sendKeys(PASSWORD);
        driver.findElement(By.name("_eventId_proceed")).click();
        driver.get("https://acorn.utoronto.ca/sws/welcome.do?welcome.dispatch#/courses/1");
        SeleniumUtility.waitClickable(driver, "cssSelector", "#tabcontent-1 > div.currentCoursesBox > div:nth-child(4) > div > div:nth-child(2) > div:nth-child(1) > div");

        logger.info("logged in to account");

        WebElement searchingBar = driver.findElement(By.id("typeaheadInput"));
        searchingBar.sendKeys("MAT135H1");
        logger.info("searching for course");
        while (searchingBar.getAttribute("class").contains("loadingIcon") || searchingBar.getAttribute("class").contains("ng-empty")) {
//            System.out.println(searchingBar.getAttribute("class"));
        }
//        System.out.println(searchingBar.getAttribute("class"));

        while (driver.findElement(By.cssSelector("#typeahead-search > div.ut-typeahead-container")).getAttribute("search-view-more-message").contains("Scroll down to show more results... (0 Courses found)")) {
//            System.out.println(driver.findElement(By.cssSelector("#typeahead-search > div.ut-typeahead-container")).getAttribute("search-view-more-message"));
        }


        WebElement course = null;
        while (course == null) {
            List<WebElement> courses = driver.findElements(By.tagName("li"));
            while (courses.size() == 0) {
                courses = driver.findElements(By.tagName("li"));
                logger.warn("relocating course element");
            }

            for (WebElement element : courses) {
                String text = element.getText();
                if (text.contains("MAT135H1 F")) {
                    System.out.println(text);
                    course = element;
                }
            }
        }

        logger.info("course located");

        course.click();
        SeleniumUtility.waitPresence(driver, "className", "modal-body");

        logger.info("collecting data");
        String info = driver.findElement(By.className("modal-body")).getAttribute("innerText");
        logger.info("collected data");

        System.out.println(info);

        driver.findElement(By.tagName("body")).sendKeys(Keys.ESCAPE);

        driver.quit();


//        System.out.println(courses);

//driver.quit();
    }
}
