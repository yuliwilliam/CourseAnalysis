import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.awt.*;

class SeleniumUtility {


    static WebDriver initWebDriver() {

        boolean test = false;

        StringBuilder location = new StringBuilder(System.getProperty("user.dir") + "/drivers/");
        String system = System.getProperty("os.name");
        if (test) {
            if (system.contains("Mac OS X")) {
                location.append("OSX/chromedriver");
            } else if (system.contains("Windows")) {
                location.append("Windows/chromedriver.exe");
            } else {
                location.append("Linux/chromedriver");
            }
            System.setProperty("webdriver.chrome.driver", location.toString());
            return new ChromeDriver();
        } else {
            if (system.contains("Mac OS X")) {
                location.append("OSX/phantomjs");
            } else if (system.contains("Windows")) {
                location.append("Windows/phantomjs.exe");
            } else {
                location.append("Linux/phantomjs");
            }
            System.setProperty("phantomjs.binary.path", location.toString());
            return new PhantomJSDriver();
        }
    }

    static void waitPresence(WebDriver driver, String flag, String info) {
        try {
            WebDriverWait wait = new WebDriverWait(driver, 10);
            if (flag.equals("id")) {
                WebElement element = wait.until(ExpectedConditions.presenceOfElementLocated(By.id(info)));
            }
            if (flag.equals("className")) {
                WebElement element = wait.until(ExpectedConditions.presenceOfElementLocated(By.className(info)));
            }
            if (flag.equals("tagName")) {
                WebElement element = wait.until(ExpectedConditions.presenceOfElementLocated(By.tagName(info)));

            }
        } catch (Exception ex) {

        }

    }

    static void waitVisible(WebDriver driver, String flag, String info) {
        try {
            WebDriverWait wait = new WebDriverWait(driver, 10);
            if (flag.equals("id")) {
                WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(info)));
            }
            if (flag.equals("className")) {
                WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(By.className(info)));
            }
            if (flag.equals("tagName")) {
                WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(By.tagName(info)));

            }
        } catch (Exception ex) {

        }

    }

    static void waitClickable(WebDriver driver, String flag, String info) {
        try {
            WebDriverWait wait = new WebDriverWait(driver, 10);
            if (flag.equals("id")) {
                WebElement element = wait.until(ExpectedConditions.elementToBeClickable(By.id(info)));
            }
            if (flag.equals("name")) {
                WebElement element = wait.until(ExpectedConditions.elementToBeClickable(By.name(info)));
            }
            if (flag.equals("xpath")) {
                WebElement element = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(info)));
            }
            if (flag.equals("cssSelector")) {
                WebElement element = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector(info)));

            }
        } catch (Exception ex) {
        }
    }

    static void bruteClick(WebDriver driver, String flag, String info) {
        boolean success = false;
        while (!success) {
            try {
                if (flag.equals("id")) {
                    driver.findElement(By.id(info)).click();
                }
                if (flag.equals("name")) {
                    driver.findElement(By.name(info)).click();
                }
                if (flag.equals("xpath")) {
                    driver.findElement(By.xpath(info)).click();
                }
                if (flag.equals("cssSelector")) {
                    driver.findElement(By.cssSelector(info)).click();
                }
                success = true;
            } catch (Exception e) {
            }
        }
    }

    static void setDimension(WebDriver driver, int numOfThread) {


        java.awt.Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        double width = screenSize.getWidth();
        double height = screenSize.getHeight();
        int numVer = (int) Math.sqrt(numOfThread);
        int numHor = (int) Math.round(numOfThread * 1.0 / numVer);

        int browserWidth = ((int) width / numHor);
        int browserHeight = ((int) height / numVer);

        driver.manage().window().setSize(new org.openqa.selenium.Dimension(browserWidth, browserHeight));
        long threadId = Thread.currentThread().getId();
        int pos = (int) threadId % numOfThread + 1;
        int row = 1;
        while (true) {
            if (browserWidth * pos <= (int) width * row) {
                driver.manage().window().setPosition(new org.openqa.selenium.Point(browserWidth * (pos - 1), browserHeight * (row - 1)));
                break;
            }
            pos = pos - ((int) width / browserWidth);
            row++;
        }
    }


}



