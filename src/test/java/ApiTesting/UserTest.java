package ApiTesting;

import ApiTesting.models.User;
import com.google.gson.Gson;
import io.restassured.http.Method;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import netscape.javascript.JSObject;
import org.json.JSONPointer;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;


public class UserTest extends BaseTest {
    @DataProvider(name = "UserDp")
    public Iterator<Object[]> userDp() {
        Collection<Object[]> dp = new ArrayList<>();
        //                   id, username, firstName, lastName, email,          password,phone,     userstatus
        dp.add(new String[] {"17", "taar", "Alex", "Trifan", "alex@gmail.com", "abc123", "12345678", "0"});
        dp.add(new String[] {"767", "ds", "Dan", "Suciu", "ds99@gmail.com", "eeee231", "192145678", "1"});
        dp.add(new String[] {"99", "pufi", "Sergiu", "Oprea", "soprea22@gmail.com", "anaaremere1", "123933218", "1"});
        return dp.iterator();
    }

    @DataProvider(name = "GetUserDp")
    public Iterator<Object[]> getUserDp() {
        Collection<Object[]> dp = new ArrayList<>();
        dp.add(new String[] {"taar"});
        dp.add(new String[] {"ds"});
        dp.add(new String[] {"pufi"});
        dp.add(new String[] {"nuExista"});
        return dp.iterator();
    }

    @DataProvider(name = "UserLogin")
    public Iterator<Object[]> loginUserDp() {
        Collection<Object[]> dp = new ArrayList<>();
        dp.add(new String[] {"taar", "abc123", "200"});
        dp.add(new String[] {"pufi", "anaaremere1", "200"});
        dp.add(new String[] {"nuExista", "aaaa", "404"});
        return dp.iterator();
    }
    /************************************ GET ************************************/

    @Test(dataProvider = "GetUserDp")
    public void getUserByUsername(String username) {
        Response response = httpRequest.request(Method.GET, "/user/" + username);

        JsonPath jsonPathEvaluator = response.jsonPath();
        if (response.getStatusCode() == 200) {
            Assert.assertEquals(jsonPathEvaluator.getString("username"), username);
        } else if (response.getStatusCode() == 404) {
            Assert.assertEquals(jsonPathEvaluator.getString("message"), "User not found");
        }
    }

    //    @Test(dataProvider = "UserLogin")
//    public void getUserLogin(String username, String password, String status) {
//        Response response = httpRequest.request(Method.GET, "/user/" + "login?username=" + username+ "&password=" + password);
//
//        Assert.assertEquals(response.getStatusCode(), Integer.parseInt(status));
//
//        JsonPath jsonPathEval = response.jsonPath();
//
//        if (response.getStatusCode() == 400) {
//            Assert.assertEquals(jsonPathEval.getString());
//        } else if (response.getStatusCode() == 200) {
//
//        }
//    }


    /************************************ POST ************************************/
    @Test(dataProvider = "UserDp")
    public void putUser(String id, String username, String firstName, String lastName, String email, String password, String phone, String userstatus) {
        User user = new User(Integer.parseInt(id), username, firstName, lastName, email, password, phone, Integer.parseInt(userstatus));
        Gson gson = new Gson();
        httpRequest.header("Content-Type", "application/json");
        httpRequest.body(gson.toJson(user));
        Response response = httpRequest.request(Method.POST, "/user");

        Assert.assertEquals(200, response.getStatusCode());
    }

    /************************************ PUT ************************************/

    /************************************ DELETE ************************************/
}
