package tests;

import config.Config;
import io.restassured.response.Response;
import listeners.RetryAnalizer;
import model.CommentModel.CommentRequest;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static utils.Constants.*;

public class TagTests extends Config {

    SoftAssert softAssert;

    @BeforeMethod(alwaysRun = true)
    public void setup(){
        softAssert = new SoftAssert();
    }

    @Test(priority = 1)
    public void getList() {

        Map<String, Integer> map = new HashMap<>();
        map.put("page", 0);
        map.put("limit", 10);

        Response response = given()
                .queryParams(map)
                .when().get(GET_TAG_LIST);

        softAssert.assertEquals(response.getStatusCode(), 200, "Expected 200 but got: " + response.getStatusCode());
        softAssert.assertAll();
    }

}
