package ApiTesting;

import ApiTesting.models.Order;
import com.google.gson.Gson;
import io.restassured.http.Method;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.ResourceBundle;

public class StoreTest extends BaseTest {

    @DataProvider(name = "PostOrder")
    public Iterator<Object[]> orderDp() {
        Collection<Object[]> dp = new ArrayList<>();
        dp.add(new String[] {"767", "767", "1", "2024-11-25T09:42:47.782+0000", "placed", "true"});
        dp.add(new String[] {"111", "1", "2", "2024-11-25T09:42:47.782+0000", "placed", "false"});
        dp.add(new String[] {"991", "888", "0", "2024-11-25T09:42:47.782+0000", "placed", "true"});
        return dp.iterator();
    }

    @DataProvider(name = "GetOrder")
    public Iterator<Object[]> orderIdDp() {
        Collection<Object[]> dp = new ArrayList<>();
        dp.add(new String[] {"767", "200"});
        dp.add(new String[] {"111", "200"});
        dp.add(new String[] {"23232323", "404"});
        return dp.iterator();
    }

    /************************************ GET ************************************/
    @Test(priority = 0)
    public void getInventory() {
        Response response = httpRequest.request(Method.GET, "/store/inventory");

        JsonPath jsonPath = response.jsonPath();
        //Aici trebuie facut mai bine
        Assert.assertEquals(response.getStatusCode(), 200);
        Assert.assertEquals(Integer.parseInt(jsonPath.getString("xyz")), 4);
        Assert.assertNotNull(jsonPath.getString("available"));
    }

    @Test(dataProvider = "GetOrder", priority = 2)
    public void getOrderById(String id, String status) {
        Response response = httpRequest.request(Method.GET, "/store/order/" + id);
        JsonPath jsonPathEvaluator = response.jsonPath();

        Assert.assertEquals(response.getStatusCode(), Integer.parseInt(status));

        if (response.getStatusCode() == 404) {
            Assert.assertEquals(jsonPathEvaluator.getString("message"), "Order not found");
        }
        else if (response.getStatusCode() == 200) {
            Assert.assertEquals(jsonPathEvaluator.getString("id"), id);
        }

    }
    /************************************ POST ************************************/
    @Test(dataProvider = "PostOrder", priority = 1)
    public void postOrder(String id, String petId, String quantity, String shipDate, String status, String complete) {
        //metoda functioneaza si cu id uri negative si cu campuri goale
        Order order = new Order(Integer.parseInt(id), Integer.parseInt(petId), Integer.parseInt(quantity), shipDate, status, Boolean.parseBoolean(complete));

        Gson gson = new Gson();
        String jsonOutput = gson.toJson(order);

        httpRequest.header("Content-Type", "application/json");
        httpRequest.body(jsonOutput);
        Response response = httpRequest.request(Method.POST, "/store/order");

        Assert.assertEquals(response.getStatusCode(), 200);

        JsonPath jsonPathEvaluator = response.jsonPath();

        Assert.assertEquals(Integer.parseInt(jsonPathEvaluator.getString("id")), Integer.parseInt(id));
        Assert.assertEquals(jsonPathEvaluator.getString("status"), status);

    }

    /************************************ DELETE ************************************/

    @Test(dataProvider = "GetOrder", priority = 3)
    public void deleteOrder(String id, String status) {
        Response response = httpRequest.request(Method.DELETE, "store/order/" + id);
        Assert.assertEquals(Integer.parseInt(status), response.getStatusCode());
    }
}
