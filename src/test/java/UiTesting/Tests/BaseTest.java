package UiTesting.Tests;

import Utils.Utils;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

public class BaseTest {
    protected static final String baseUrl = Utils.getConfigProperty("sauceDemoURL");
    protected WebDriver driver;

    @BeforeMethod
    public void initDriver() {
        driver = Utils.getDriver();
    }

    @AfterMethod
    public void closeBrowser() {
        driver.close();
    }
}
