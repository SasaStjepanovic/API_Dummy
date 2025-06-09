package tests;

import config.Config;
import io.restassured.response.Response;
import model.UserModel.UserRequest;
//import model.UserModel.UserResponse;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import static utils.Constants.*;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;

public class UsersTests extends Config {

    String userId;

    SoftAssert softAssert;

    @BeforeMethod(alwaysRun = true)
    public void setup(){
        softAssert = new SoftAssert();
    }

    @Test(priority = 1)
    public void getUsers() {

        Map<String, Integer> map = new HashMap<>();
        map.put("page", 0);
        map.put("limit", 10);

        Response response = given()
                .queryParams(map)
                .when().get(GET_ALL_USERS);

        this.userId = response.jsonPath().get("data[1].id");

        String actualFirstName = response.jsonPath().get("data[0].firstName");
        softAssert.assertEquals(response.getStatusCode(), 200, "Expected 200 but got: " + response.getStatusCode());
        softAssert.assertEquals(actualFirstName, "Roberto");
        softAssert.assertAll();
    }

    @Test(priority = 2)
    public void getUserById() {

        Response response = given()
                .pathParam("id", this.userId)
                .when().get(GET_USER_BY_ID);

        Assert.assertEquals(response.getStatusCode(), 200, "Expected 200 but got: " + response.getStatusCode());
    }

    @Test(priority = 3)
    public void getUserByWrongId() {

        String ID = "87987897";
        Response response = given()
                .pathParam("id", ID)
                .when().get(GET_USER_BY_ID);

        String message = response.jsonPath().get("error");

        softAssert.assertEquals(response.getStatusCode(), 400, "Expected 400 but got: " + response.getStatusCode());
        softAssert.assertEquals(message, "PARAMS_NOT_VALID", "Expected PARAMS_NOT_VALID but got: " + message);
        softAssert.assertAll();
    }

    @Test(priority = 4)
    public void createUser() {

        UserRequest userRequest = UserRequest.createUser();

        Response response = given()
                .body(userRequest)
                .when().post(CREATE_USER);
        String id = response.jsonPath().get("id");
        String date = response.jsonPath().get("updatedDate");
        System.out.println("ID is: " + id);
        System.out.println("Update date: " + date);

        Assert.assertEquals(response.getStatusCode(), 200, "Expected 200 but got: " + response.getStatusCode());
    }

    @Test(priority = 5)
    public void createUserEmptyFirstName() {

        UserRequest userRequest = UserRequest.createUser();

        Response response1 = given()
                .body(userRequest)
                .when().post(CREATE_USER);

        String emptyFirstName = "";

        UserRequest createUser = userRequest
                .withFirstName(emptyFirstName);

        Response response2 = given()
                .body(createUser)
                .when().post(CREATE_USER);

        Assert.assertEquals(response2.getStatusCode(), 400, "Expected 400 but got: " + response2.getStatusCode());
    }

    @Test(priority = 6)
    public void createUserShortFirstName() {

        UserRequest userRequest = UserRequest.createUser();

        Response response1 = given()
                .body(userRequest)
                .when().post(CREATE_USER);

        String shortFirstName = "w";

        UserRequest createUser = userRequest
                .withFirstName(shortFirstName);

        Response response2 = given()
                .body(createUser)
                .when().post(CREATE_USER);

        Assert.assertEquals(response2.getStatusCode(), 400, "Expected 400 but got: " + response2.getStatusCode());
    }

    @Test(priority = 7)
    public void createUserlongFirstName() {

        UserRequest userRequest = UserRequest.createUser();

        Response response1 = given()
                .body(userRequest)
                .when().post(CREATE_USER);

        String longFirstName = "1234567890123456789012345678901";

        UserRequest createUser = userRequest
                .withFirstName(longFirstName);

        Response response2 = given()
                .body(createUser)
                .when().post(CREATE_USER);

        Assert.assertEquals(response2.getStatusCode(), 400, "Expected 400 but got: " + response2.getStatusCode());
    }

    @Test(priority = 8)
    public void createUserEmptyLastName() {

        UserRequest userRequest = UserRequest.createUser();

        Response response1 = given()
                .body(userRequest)
                .when().post(CREATE_USER);

        String emptyLastName = "";

        UserRequest createUser = userRequest
                .withLastName(emptyLastName);

        Response response2 = given()
                .body(createUser)
                .when().post(CREATE_USER);

        Assert.assertEquals(response2.getStatusCode(), 400, "Expected 400 but got: " + response2.getStatusCode());
    }

    @Test(priority = 9)
    public void createUserShortLastName() {

        UserRequest userRequest = UserRequest.createUser();

        Response response1 = given()
                .body(userRequest)
                .when().post(CREATE_USER);

        String shortLastName = "q";

        UserRequest createUser = userRequest
                .withLastName(shortLastName);

        Response response2 = given()
                .body(createUser)
                .when().post(CREATE_USER);

        Assert.assertEquals(response2.getStatusCode(), 400, "Expected 400 but got: " + response2.getStatusCode());
    }

    @Test(priority = 10)
    public void createUserLongLastName() {

        UserRequest userRequest = UserRequest.createUser();

        Response response1 = given()
                .body(userRequest)
                .when().post(CREATE_USER);

        String longLastName = "1234567890123456789012345678901";

        UserRequest createUser = userRequest
                .withLastName(longLastName);

        Response response2 = given()
                .body(createUser)
                .when().post(CREATE_USER);

        Assert.assertEquals(response2.getStatusCode(), 400, "Expected 400 but got: " + response2.getStatusCode());
    }

    @Test(priority = 11)
    public void updateUser() {

        UserRequest userRequest = UserRequest.createUser();

        Response response = given()
                .body(userRequest)
                .when().post(CREATE_USER);

        String updatedFirstName = "updatedFirstName";
        String updatedEmail = "updatedEmail";
        String updatedCity = "updatedCity";

        UserRequest updateUser = userRequest
                .withFirstName(updatedFirstName)
                .withEmail(updatedEmail)
                .withLocation(userRequest.getLocation().withCity(updatedCity));

        String userId = response.jsonPath().get("id");

        Response updatedUserResponse = given()
                .body(updateUser)
                .pathParam("id", userId)
                .when().put(UPDATE_USER);

        String firstName = updatedUserResponse.jsonPath().get("firstName");
        boolean isFirstNameUpdated = firstName.equals(updatedFirstName);

        softAssert.assertTrue(isFirstNameUpdated, "First name not updated!");
        softAssert.assertEquals(response.getStatusCode(), 200, "Expected 200 but got: " + response.getStatusCode());
//        softAssert.assertEquals(updatedUserResponse.getLocation().getCity(), updatedCity, "City not updated!");
        softAssert.assertAll();
    }

    @Test(priority = 12)
    public void deleteUser() {

        UserRequest userRequest = UserRequest.createUser();

        Response response1 = given()
                .body(userRequest)
                .when().post(CREATE_USER);

        String id = response1.jsonPath().get("id");
        System.out.println("ID is: " + id);

        Response response2 = given()
                .pathParam("id", id)
                .when().delete(DELETE_USER);

        String idResponse = response2.jsonPath().get("id");

        softAssert.assertEquals(response2.getStatusCode(), 200, "Expected 200 but got: " + response2.getStatusCode());
        softAssert.assertEquals(idResponse, id, "Expected: " + idResponse);
        softAssert.assertAll();
    }

    @Test(priority = 13)
    public void deleteUserNotExisting() {

        UserRequest userRequest = UserRequest.createUser();

        Response response1 = given()
                .body(userRequest)
                .when().post(CREATE_USER);

        String id = response1.jsonPath().get("id");
        System.out.println("ID is: " + id);

        Response response2 = given()
                .pathParam("id", id)
                .when().delete(DELETE_USER);

        Assert.assertEquals(response2.getStatusCode(), 200, "Expected 200 but got: " + response2.getStatusCode());
        Response response3 = given()
                .pathParam("id", id)
                .when().delete(DELETE_USER);

        String errorMessage = response3.jsonPath().get("error");
        softAssert.assertEquals(errorMessage, "RESOURCE_NOT_FOUND");
        softAssert.assertEquals(response3.getStatusCode(), 404, "Expected 404 but got: " + response3.getStatusCode());



    }

}
