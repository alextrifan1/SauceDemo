package UiTesting.Tests;

import UiTesting.Pages.LoginPage;
import org.testng.annotations.Test;

public class LoginTest extends BaseTest {
    @Test
    public void testAllUsers() {
        int index;
        for (index = 0; index < 6; index++) {
            driver.get(baseUrl);
            LoginPage lp = new LoginPage(driver);
            lp.login(index);
        }
    }
}
