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

    @DataProvider(name = "PetData")
    public Iterator<Object[]> petDp() {
        Collection<Object[]> dp = new ArrayList<>();
        //                   id,    category: (id, name), name, photUrls,               tags:(id, tagName),                 status
        dp.add(new String[] {"767", "231", "Dog", "Goldie", "http://myurl.com", "567", "why are you reading this?", "sold"});
        dp.add(new String[] {"999", "111", "Cat", "MiauMiau", "http://myurl.com", "267", "keep reading, it's cool", "available"});
        dp.add(new String[] {"1", "333", "Dog", "GoodDogMaadCity", "http://myurl.com", "222", "genericTag", "pending"});
        dp.add(new String[] {"217", "231", "Fish", "Nemo", "http://myurl.com", "567", "anotherGenericTag", "pending"});
        dp.add(new String[] {"888", "231", "Cat", "CoolCat", "http://myurl.com", "567", "just a cool cat", "sold"});
        return dp.iterator();
    }

    @DataProvider(name = "FindPetById")
    public Iterator<Object[]> findPetDp() {
        Collection<Object[]> dp = new ArrayList<>();
        dp.add(new String[] {"222", "200", "Beagle_Eagle"});
        dp.add(new String[] {"767", "404", ""});
        //dp.add(new String[] {"322", "200", "Rubye"});
        dp.add(new String[] {"10", "200", "doggie"});
        return dp.iterator();
    }
    @DataProvider(name= "FindPetByStatus")
    public Iterator<Object[]> findPetStatusDp() {
        Collection<Object[]> dp = new ArrayList<>();
        dp.add(new String[] {"available"});
        dp.add(new String[] {"pending"});
        dp.add(new String[] {"sold"});
        return dp.iterator();
    }

    @DataProvider(name = "DeletePet")
    public Iterator<Object[]> deletePetDp() {
        Collection<Object[]> dp = new ArrayList<>();
        dp.add(new String[] {"200", "404"});
        dp.add(new String[] {"732", "200"});

        return dp.iterator();
    }

    /************************************ GET ************************************/

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

    @Test(dataProvider = "FindPetByStatus")
    public void findPetByStatus(String status) {
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

    /************************************ POST ************************************/

    @Test(dataProvider = "PetData")
    public void createPet(String id, String cId, String cNane, String name, String photo, String tId, String tName, String status) {
        Category cat = new Category(Integer.parseInt(cId), cNane);
        Tag tag = new Tag(Integer.parseInt(tId), tName);
        ArrayList<Tag> tags = new ArrayList<>();
        tags.add(tag);
        ArrayList<String> photoUrls = new ArrayList<>();
        photoUrls.add(photo);
        Pet pet = new Pet(Integer.parseInt(id), cat, name, photoUrls, tags, status);

        Gson gson = new Gson();
        String jsonOutput = gson.toJson(pet);
        //System.out.println(jsonOutput);

        // fara asta iti da cod eroare 415
        httpRequest.header("Content-Type", "application/json");

        httpRequest.body(jsonOutput);
        Response response = httpRequest.request(Method.POST, "/pet");

        Assert.assertEquals(200, response.getStatusCode());

    }

    @Test
    public void createPetOtherVersion() {
        JSONObject requestParams = new JSONObject();
        requestParams.put("id", "233");
        requestParams.put("name", "doggie");
        httpRequest.header("Content-Type", "application/json");
        httpRequest.body(requestParams.toString());
        Response response = httpRequest.request(Method.POST, "/pet");
    }

    /************************************ PUT ************************************/

    @Test
    public void updatePet() {
        String id = "732";

        Response response = httpRequest.request(Method.GET, "/pet/" + id);
        Assert.assertEquals(response.getStatusCode(), 200);

        // vezi daca nu e o metoda mai scurta
        Category cat = new Category(231, "myName");
        Tag tag = new Tag(567, "tagName");
        ArrayList<Tag> tags = new ArrayList<>();
        tags.add(tag);
        ArrayList<String> photoUrls = new ArrayList<>();
        photoUrls.add("http://myurl.com");
        Pet pet = new Pet(732, cat, "MiauMiauForever", photoUrls, tags, "pending");

        Gson gson = new Gson();
        String jsonOutput = gson.toJson(pet);

        httpRequest.header("Content-Type", "application/json");

        httpRequest.body(jsonOutput);
        response = httpRequest.request(Method.PUT, "/pet");

        Assert.assertEquals(200, response.getStatusCode());

    }

    /************************************ DELETE ************************************/

    @Test(dataProvider = "DeletePet")
    public void deletePet(String id, String status) {
        Response response = httpRequest.request(Method.DELETE, "/pet/" + id);
        Assert.assertEquals(Integer.parseInt(status), response.getStatusCode());
    }
}
