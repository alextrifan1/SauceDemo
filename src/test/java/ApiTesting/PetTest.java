package ApiTesting;

import ApiTesting.models.Category;
import ApiTesting.models.Pet;
import ApiTesting.models.Tag;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import io.restassured.http.Method;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.json.JSONArray;
import org.json.JSONObject;
import org.openqa.selenium.devtools.v127.fetch.model.AuthChallengeResponse;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

public class PetTest extends BaseTest {

    @DataProvider(name = "FindPetById")
    public Iterator<Object[]> findPetDp() {
        Collection<Object[]> dp = new ArrayList<>();
        dp.add(new String[] {"222", "200", "Beagle_Eagle"});
        dp.add(new String[] {"767", "404", ""});
        //dp.add(new String[] {"322", "200", "Rubye"});
        dp.add(new String[] {"10", "200", "doggie"});
        return dp.iterator();
    }

    @Test(dataProvider = "FindPetById")
    public void findPetById(String petId, String responseCode, String name) {
        Response response = httpRequest.request(Method.GET, "/pet/" + petId);
        Assert.assertEquals(response.getStatusCode(), Integer.parseInt(responseCode));

        JsonPath jsonPathEvaluator = response.jsonPath();

        if (response.getStatusCode() == 404) {
            Assert.assertEquals(jsonPathEvaluator.getString("message"), "Pet not found");
        }
        else if (response.getStatusCode() == 200) {
            Assert.assertEquals(jsonPathEvaluator.getString("name"), name);
        }
    }

    @Test
    public void findPetByStatus() {
        String status = "pending";
        Response response = httpRequest.request(Method.GET, "/pet/findByStatus?status=" + status);

        Assert.assertEquals(200, response.getStatusCode());

        // pt JSON ai nevoi mereu de un string, foloseste asString, nu toString !!
        String responseBody = response.getBody().asString();
        JSONArray pets = new JSONArray(responseBody);

        for (int i = 0; i < pets.length(); i++) {
            JSONObject pet = pets.getJSONObject(i);
            String petStatus = pet.getString("status");
            Assert.assertEquals(status, petStatus);
        }

    }


    @Test
    public void createPet() {
        Category cat = new Category(231, "myName");
        Tag tag = new Tag(567, "tagName");
        ArrayList<Tag> tags = new ArrayList<>();
        tags.add(tag);
        ArrayList<String> photoUrls = new ArrayList<>();
        photoUrls.add("http://myurl.com");
        Pet pet = new Pet(732, cat, "MiauMiau", photoUrls, tags, "available");

        Gson gson = new Gson();
        String jsonOutput = gson.toJson(pet);
        //System.out.println(jsonOutput);

        // fara asta iti da cod eroare 415
        httpRequest.header("Content-Type", "application/json");

       httpRequest.body(jsonOutput);
       Response response = httpRequest.request(Method.POST, "/pet");

        Assert.assertEquals(200, response.getStatusCode());

    }

    //bun pentru cazuri simple
    @Test
    public void createPetOtherVersion() {
        JSONObject requestParams = new JSONObject();
        requestParams.put("id", "233");
        requestParams.put("name", "doggie");
        httpRequest.header("Content-Type", "application/json");
        httpRequest.body(requestParams.toString());
        Response response = httpRequest.request(Method.POST, "/pet");
    }
}
