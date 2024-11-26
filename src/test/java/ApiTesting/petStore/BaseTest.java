package ApiTesting.petStore;

import Utils.Utils;
import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;
import org.testng.annotations.BeforeMethod;

public class BaseTest {
    private static final String petStoreUrl = Utils.getConfigProperty("petStoreURL");
    protected RequestSpecification httpRequest;

    @BeforeMethod
    public void setup() {
        RestAssured.baseURI = petStoreUrl;
        httpRequest = RestAssured.given();
    }
}
