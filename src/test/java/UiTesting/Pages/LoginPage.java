package UiTesting.Pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.Arrays;
import java.util.List;

public class LoginPage extends BasePage {

    private static final String USERNAME = "user-name";
    private static final String PASSWORD = "password";
    private static final String LOGIN_BUTTON = "login-button";
    private static final String LOGIN_CREDENTIALS = "login_credentials";
    private static final String LOGIN_PASSWORD = "password";

    public void login(int nr) {
        WebElement usernameField = driver.findElement(By.id(USERNAME));
        WebElement passwordField = driver.findElement(By.id((PASSWORD)));
        WebElement loginButton = driver.findElement(By.id(LOGIN_BUTTON));
        WebElement credentials = driver.findElement(By.id(LOGIN_CREDENTIALS));
        WebElement password = driver.findElement(By.id(LOGIN_PASSWORD));

        String[] usernames = credentials.getText().split("\n");
        List<String> validUsernames = List.of(usernames).subList(1, usernames.length);
        String username = validUsernames.get(nr);

        /*String[] passworList = password.getText().split("\n");
        List<String> validPassword = List.of(passworList).subList(1, passworList.length);
        String passworSend = validPassword.get(0);*/

        usernameField.clear();
        usernameField.sendKeys(username);
        passwordField.clear();
        passwordField.sendKeys("secret_sauce");

        loginButton.click();
    }

    public LoginPage(WebDriver driver) {
        super(driver);
    }

}
