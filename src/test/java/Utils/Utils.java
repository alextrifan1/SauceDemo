package Utils;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.FileInputStream;
import java.io.IOException;
import java.time.Duration;
import java.util.Properties;
import java.util.Random;
import java.util.Set;

public class Utils {
    private static final String configFile = "config.properties";

    public static String getConfigProperty(String property) {
        Properties configProp = new Properties();
        String configItem="";

        try {
            configProp.load(new FileInputStream(configFile));
            configItem = configProp.getProperty(property).toLowerCase();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return configItem;
    }

    public static WebDriver getDriver() {
        WebDriver driver;
        String browser = getConfigProperty("browser");

        Proxy proxy = new Proxy();
        proxy.setHttpProxy("127.0.0.1:6969");

        switch(browser) {
            case "chrome": {
                ChromeOptions options = new ChromeOptions();
                if(Boolean.parseBoolean(getConfigProperty("headless"))) {
                    options.addArguments("--headless");

                }
                //options.setCapability("proxy", proxy);
                driver = new ChromeDriver(options);
                if(Boolean.parseBoolean(getConfigProperty("maximized"))) {
                    driver.manage().window().maximize();
                }
                break;
            }
            case "firefox": {
                FirefoxOptions options = new FirefoxOptions();
                FirefoxProfile profile = new FirefoxProfile();
                if(Boolean.parseBoolean(getConfigProperty("headless"))) {
                    options.addArguments("-headless");
                }

                profile.setPreference("browser.download.dir", ".");

                options.setProfile(profile);
                driver = new FirefoxDriver(options);

                if(Boolean.parseBoolean(getConfigProperty("maximized"))) {
                    driver.manage().window().maximize();
                }
                break;
            }
            case "edge": {
                EdgeOptions options = new EdgeOptions();
                if(Boolean.parseBoolean(getConfigProperty("maximized"))) {
                    options.addArguments("--start-maximized");
                }
                if(Boolean.parseBoolean(getConfigProperty("headless"))) {
                    options.addArguments("--headless");

                }
                driver = new EdgeDriver(options);
                break;
            }
            default: {
                driver = new ChromeDriver();
            }
        }
        //driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
        return driver;
    }

    public static WebElement waitForElement(WebDriver driver, long seconds, By locator) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(seconds));
        return wait.until(ExpectedConditions.presenceOfElementLocated(locator));
    }

    public static WebElement waitForElementClickable(WebDriver driver, long seconds, By locator) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(seconds));
        return wait.until(ExpectedConditions.elementToBeClickable(locator));
    }

    public static void scrollDown(WebDriver driver) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("window.scrollBy(0,document.body.scrollHeight);", "");
    }

    public static void scrollToElement(WebDriver driver, WebElement element) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].scrollIntoView(true);", element);
    }

    public static void printCookies(WebDriver driver)  {
        Set<Cookie> cookies = driver.manage().getCookies();
        for(Cookie c : cookies) {
            System.out.println(c.toJson());
        }
    }

    public static int generateRandomNumber(int maxNumber) {
        Random rand = new Random();
        return rand.nextInt(maxNumber) ;
    }



}
