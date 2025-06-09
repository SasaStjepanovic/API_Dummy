package tests;

import config.Config;
import io.restassured.response.Response;
import model.PostModel.PostRequest;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static utils.Constants.*;

public class PostTests extends Config {

    String postID;
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
                .when().get(GET_LIST_POSTS);

        this.postID = response.jsonPath().get("data[0].owner.id");
        System.out.println("id: " + postID);
        softAssert.assertEquals(response.getStatusCode(), 200, "Expected 200 but got: " + response.getStatusCode());
        softAssert.assertAll();
    }

    @Test(priority = 2)
    public void getListByUser() {

        Response response = given()
                .when().get(GET_LIST_POSTS);

        String userId = response.jsonPath().get("data[0].owner.id");
        System.out.println("User ID: " + userId);

        Response response1 = given()
                .pathParam("id", userId)
                .when().get(GET_POSTS_BY_USER);

        String tags = response1.jsonPath().get("data[1].tags[1]");
        System.out.println("Tags: " + tags);
        softAssert.assertEquals(tags, "two");
        softAssert.assertEquals(response1.getStatusCode(), 200, "Expected 200 but got: " + response1.getStatusCode());
        softAssert.assertAll();
    }

    @Test(priority = 3)
    public void getListByTag() {

        Response response1 = given()
                .pathParam("id", "canine")
                .when().get(GET_POSTS_BY_TAG);

        String tags = response1.jsonPath().get("data[2].tags[1]");
        System.out.println("Tags: " + tags);
        softAssert.assertEquals(tags, "canine");
        softAssert.assertEquals(response1.getStatusCode(), 200, "Expected 200 but got: " + response1.getStatusCode());
        softAssert.assertAll();
    }
    @Test(priority = 4)
    public void getListByPostId() {

        Response response = given()
                .when().get(GET_LIST_POSTS);

        String postId = response.jsonPath().get("data[3].id");
        System.out.println("Post ID: " + postId);

        Response response1 = given()
                .pathParam("id", postId)
                .when().get(GET_POSTS_BY_ID);

        softAssert.assertEquals(response1.getStatusCode(), 200, "Expected 200 but got: " + response1.getStatusCode());
        softAssert.assertAll();
    }

    @Test(priority = 5)
    public void createPost() {

        PostRequest postRequest = PostRequest.createPost();

        Response response = given()
                .body(postRequest)
                .when().post(CREATE_POST);

        softAssert.assertEquals(response.getStatusCode(), 200, "Expected 200 but got: " + response.getStatusCode());
        softAssert.assertAll();
    }

    @Test(priority = 6)
    public void createPostNotExistingOwner() {

        PostRequest postRequest = PostRequest.createPost();

        Response response1 = given()
                .body(postRequest)
                .when().post(CREATE_POST);

        String updateOwner = "287sgs89789wsde2";

        PostRequest createNotExistOwner = postRequest
                .withOwner(updateOwner);

        Response response2 = given()
                .body(createNotExistOwner)
                .when().post(CREATE_POST);

        String errorMessage = response2.jsonPath().get("error");
        softAssert.assertEquals(response2.getStatusCode(), 400, "Expected 400 but got: " + response2.getStatusCode());
        softAssert.assertEquals(errorMessage, "BODY_NOT_VALID");
        softAssert.assertAll();
    }

    @Test(priority = 7)
    public void updatePost() {

        PostRequest postRequest = PostRequest.createPost();

        Response response1 = given()
                .body(postRequest)
                .when().post(CREATE_POST);

        String postId = response1.jsonPath().get("id");
        System.out.println("Post ID: " + postId);

        PostRequest updatePost = postRequest
                .withLikes(230)
                .withText("Updated text");

        Response response2 = given()
                .body(updatePost)
                .pathParam("id", postId)
                .when().put(UPDATE_POST);

        int likes = response2.jsonPath().get("likes");
        System.out.println("Number of likes: " + likes);

        softAssert.assertEquals(response2.getStatusCode(), 200, "Expected 200 but got: " + response2.getStatusCode());
        softAssert.assertEquals(likes, 230);
        softAssert.assertAll();
    }

    @Test(priority = 8)
    public void updatePostOwnerNegative() {

        PostRequest postRequest = PostRequest.createPost();

        Response response1 = given()
                .body(postRequest)
                .when().post(CREATE_POST);

        String postId = response1.jsonPath().get("id");
        System.out.println("Post ID: " + postId);

        softAssert.assertAll();
    }

    @Test(priority = 9)
    public void deletePost() {

        PostRequest postRequest = PostRequest.createPost();

        Response response1 = given()
                .body(postRequest)
                .when().post(CREATE_POST);

        String postId = response1.jsonPath().get("id");
        System.out.println("Post ID: " + postId);


        Response response2 = given()
                .pathParam("id", postId)
                .when().delete(DELETE_POST);

        String idFromResponse = response2.jsonPath().get("id");

        softAssert.assertEquals(response2.getStatusCode(), 200, "Expected 200 but got: " + response2.getStatusCode());
        softAssert.assertEquals(idFromResponse, postId);
        softAssert.assertAll();
    }

}
