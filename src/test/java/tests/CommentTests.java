package tests;

import config.Config;
import io.restassured.response.Response;
import listeners.RetryAnalizer;
import model.CommentModel.CommentRequest;
import model.PostModel.PostRequest;
import model.UserModel.UserRequest;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static utils.Constants.*;

public class CommentTests extends Config {

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
                .when().get(GET_LIST_COMMENTS);

//        this.postID = response.jsonPath().get("data[0].owner.id");
//        System.out.println("id: " + postID);
        softAssert.assertEquals(response.getStatusCode(), 200, "Expected 200 but got: " + response.getStatusCode());
        softAssert.assertAll();
    }

    @Test(priority = 2)
    public void getListByPostId() {

        Map<String, Integer> map = new HashMap<>();
        map.put("page", 0);
        map.put("limit", 10);

        Response response1 = given()
                .queryParams(map)
                .when().get(GET_LIST_COMMENTS);

        String postID = response1.jsonPath().get("data[0].post");
        System.out.println("Post id: " + postID);

        Response response2 = given()
                .pathParam("id", postID)
                .when().get(GET_COMMENTS_BY_POST);

        softAssert.assertEquals(response2.getStatusCode(), 200, "Expected 200 but got: " + response2.getStatusCode());
        softAssert.assertAll();
    }

    @Test(priority = 3)
    public void getListByWrongPostId() {

        Map<String, Integer> map = new HashMap<>();
        map.put("page", 0);
        map.put("limit", 10);

        Response response1 = given()
                .queryParams(map)
                .when().get(GET_LIST_COMMENTS);

        String postID = "djhjd686djhjkd890089";

        Response response2 = given()
                .pathParam("id", postID)
                .when().get(GET_COMMENTS_BY_POST);

        String errorMessage = response2.jsonPath().get("error");
        softAssert.assertEquals(errorMessage, "PARAMS_NOT_VALID");
        softAssert.assertEquals(response2.getStatusCode(), 400, "Expected 400 but got: " + response2.getStatusCode());
        softAssert.assertAll();
    }

    @Test(priority = 4)
    public void getListByUser() {

        Map<String, Integer> map = new HashMap<>();
        map.put("page", 0);
        map.put("limit", 10);

        Response response1 = given()
                .queryParams(map)
                .when().get(GET_LIST_COMMENTS);

        String userID = response1.jsonPath().get("data[0].owner.id");
        System.out.println("User id: " + userID);

        Response response2 = given()
                .pathParam("id", userID)
                .when().get(GET_COMMENTS_BY_USER);

        softAssert.assertEquals(response2.getStatusCode(), 200, "Expected 200 but got: " + response2.getStatusCode());
        softAssert.assertAll();
    }

    @Test(priority = 5)
    public void getListByWrongUser() {

        Map<String, Integer> map = new HashMap<>();
        map.put("page", 0);
        map.put("limit", 10);

        Response response1 = given()
                .queryParams(map)
                .when().get(GET_LIST_COMMENTS);

        String userID = "kjkjf908furio98";

        Response response2 = given()
                .pathParam("id", userID)
                .when().get(GET_COMMENTS_BY_USER);

        String errorMessage = response2.jsonPath().get("error");
        softAssert.assertEquals(errorMessage, "PARAMS_NOT_VALID");
        softAssert.assertEquals(response2.getStatusCode(), 400, "Expected 400 but got: " + response2.getStatusCode());
        softAssert.assertAll();
    }

    @Test(priority = 6)
    public void createComment() {

        CommentRequest commentRequest = CommentRequest.createComment();

        Response response2 = given()
                .body(commentRequest)
                .when().post(CREATE_COMMENT);

        softAssert.assertEquals(response2.getStatusCode(), 200, "Expected 200 but got: " + response2.getStatusCode());
        softAssert.assertAll();
    }

    @Test(priority = 7, retryAnalyzer = RetryAnalizer.class)
    public void createCommentLengthLessOf2() {

        CommentRequest commentRequest = CommentRequest.createComment();

        String message = "d";
        CommentRequest createMessageLessOf2 = commentRequest
                .withMessage(message);

        Response response2 = given()
                .body(createMessageLessOf2)
                .when().post(CREATE_COMMENT);

        softAssert.assertEquals(response2.getStatusCode(), 400, "Should reject comment shorter than 2 characters. Expected 400 but got: " + response2.getStatusCode());
        softAssert.assertAll();
        Assert.fail("TODO: It's allowed to enter comment shorter than 2 characters, but it should not");
    }

    @Test(priority = 8, retryAnalyzer = RetryAnalizer.class)
    public void createCommentLengthMoreThan500() {

        CommentRequest commentRequest = CommentRequest.createComment();

        String message = "12345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890" +
                "1234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890";
        CommentRequest createMessagemoreThan500 = commentRequest
                .withMessage(message);

        Response response2 = given()
                .body(createMessagemoreThan500)
                .when().post(CREATE_COMMENT);

        softAssert.assertEquals(response2.getStatusCode(), 400, "Should reject comment greater than 500 characters. Expected 400 but got: " + response2.getStatusCode());
        softAssert.assertAll();
        Assert.fail("TODO: It's allowed to enter comment greater than 500 characters, but it should not");
    }

    @Test(priority = 9)
    public void deleteComment() {

        CommentRequest commentRequest = CommentRequest.createComment();

        Response response1 = given()
                .body(commentRequest)
                .when().post(CREATE_COMMENT);

        String idComment = response1.jsonPath().get("id");
        System.out.println("Comment id: " + idComment);

        Response response2 = given()
                .pathParam("id", idComment)
                .when().delete(DELETE_COMMENT);

        String idCommentAfterDeleting = response1.jsonPath().get("id");
        System.out.println("Comment id: " + idCommentAfterDeleting);

        softAssert.assertEquals(response2.getStatusCode(), 200, "Expected 200 but got: " + response2.getStatusCode());
        softAssert.assertEquals(idComment, idCommentAfterDeleting, "ID's should be the same");
        softAssert.assertAll();
    }
}
