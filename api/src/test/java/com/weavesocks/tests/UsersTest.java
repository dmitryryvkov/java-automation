package com.weavesocks.tests;


import com.weavesocks.api.ProjectConfig;
import com.weavesocks.api.core.Assertions;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.aeonbits.owner.ConfigFactory;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

public class UsersTest {

    //private Faker faker;

    @BeforeClass
    public void setUp() {
        ProjectConfig projectConfig = ConfigFactory.create(ProjectConfig.class);
        //faker = new Faker(new Locale(projectConfig.locale()));
        RestAssured.baseURI = projectConfig.baseUrl();
    }

    @Test
    public void testCheckAbilityToGetAllUsers() {
        Response response = RestAssured
                .given().log().all()
                .get("users?page=2");
        response.then().assertThat().statusCode(200);

        Assertions.assertEquals(response.jsonPath().get("total").toString(), "12", "Incorrect count of users");
    }

    @Test
    public void testCheckSingleUserInfo() {
        Response response = RestAssured
                .given().log().all()
                .get("users/1");

        response.then().assertThat().statusCode(200);

        Assertions.assertEquals(response.jsonPath().get("data.id").toString(), "1", "Incorrect count of users");
        Assertions.assertEquals(response.jsonPath().get("data.email").toString(), "george.bluth@reqres.in", "Incorrect email");
        Assertions.assertEquals(response.jsonPath().get("data.first_name").toString(), "George", "Incorrect first name");
        Assertions.assertEquals(response.jsonPath().get("data.last_name").toString(), "Bluth", "Incorrect last name");
    }

    @Test
    public void userCanBeDeleted() {
        Response response = RestAssured.given()
                .log().all()
                .delete("users/1");

        response.then().assertThat().statusCode(204);
    }

    @Test
    public void userCanBeCreated() {
        RequestSpecification request = RestAssured.given();
        Map<String, Object> map = new HashMap<>();
        map.put("name", "morpheus");
        map.put("job", "leader");

        Response response = request
                .body(map)
                .log().all()
                .post("users");

        response.then().assertThat().statusCode(201);

        Assertions.assertTrue(!response.jsonPath().get("id").toString().isEmpty(), "Empty id parameter");
        Assertions.assertTrue(!response.jsonPath().get("createdAt").toString().isEmpty(), "Empty creation time");
    }

    @Test
    public void newUserCanBeRegisteredSuccessfully() {
        RequestSpecification request = RestAssured.given();
        Map<String, Object> map = new HashMap<>();
        map.put("password", "pistol");
        map.put("email", "eve.holt@reqres.in");

        Response response = request
                .body(map)
                .log().all()
                .post("register");

        response.then().assertThat().statusCode(200);

        Assertions.assertTrue(!response.jsonPath().get("id").toString().isEmpty(), "Empty id parameter");
        Assertions.assertTrue(!response.jsonPath().get("token").toString().isEmpty(), "Empty token");
    }

    @Test
    public void newUserCanNotBeRegisteredSuccessfullyWithEmptyPassword() {
        RequestSpecification request = RestAssured.given();
        Map<String, Object> map = new HashMap<>();
        map.put("email", "eve.holt@reqres.in");

        Response response = request
                .body(map)
                .log().all()
                .post("register");

        response.then().assertThat().statusCode(400);

        Assertions.assertEquals(response.jsonPath().get("error").toString(), "Missing email or username", "Incorrect error message");
    }

    @Test
    public void newUserCanNotBeRegisteredSuccessfullyWithEmptyEmail() {
        RequestSpecification request = RestAssured.given();
        Map<String, Object> map = new HashMap<>();
        map.put("password", "pistol");

        Response response = request
                .body(map)
                .log().all()
                .post("register");

        response.then().assertThat().statusCode(400);

        Assertions.assertEquals(response.jsonPath().get("error").toString(), "Missing email or username", "Incorrect error message");
    }

    @Test
    public void userCanLogin() {
        RequestSpecification request = RestAssured.given();
        Map<String, Object> map = new HashMap<>();
        map.put("email", "eve.holt@reqres.in");
        map.put("password", "pistol");

        Response response = request
                .body(map)
                .log().all()
                .post("login");

        response.then().assertThat().statusCode(200);

        Assertions.assertTrue(!response.jsonPath().get("token").toString().isEmpty(), "Empty token");
    }

    @Test
    public void userCanNotLoginWithMissingEmail() {
        RequestSpecification request = RestAssured.given();
        Map<String, Object> map = new HashMap<>();
        map.put("password", "pistol");

        Response response = request
                .body(map)
                .log().all()
                .post("login");

        response.then().assertThat().statusCode(400);

        Assertions.assertEquals(response.jsonPath().get("error").toString(), "Missing email or username", "Incorrect error message");
    }

    @Test
    public void userCanNotLoginWithMissingPassword() {
        RequestSpecification request = RestAssured.given();
        Map<String, Object> map = new HashMap<>();
        map.put("password", "pistol");

        Response response = request
                .body(map)
                .log().all()
                .post("login");

        response.then().assertThat().statusCode(400);

        Assertions.assertEquals(response.jsonPath().get("error").toString(), "Missing email or username", "Incorrect error message");
    }

    @Test
    public void userDataCanUpdatedByPatchMethod() {
        RequestSpecification request = RestAssured.given();
        Map<String, Object> map = new HashMap<>();
        map.put("name", "morpheus");
        map.put("job", "zion resident");

        Response response = request
                .body(map)
                .log().all()
                .patch("users/2");

        response.then().assertThat().statusCode(200);

        Assertions.assertTrue(!response.jsonPath().get("updatedAt").toString().isEmpty(), "Empty updated time");
    }

    @Test
    public void userDataCanUpdatedByPutMethod() {
        RequestSpecification request = RestAssured.given();
        Map<String, Object> map = new HashMap<>();
        map.put("name", "morpheus");
        map.put("job", "zion resident");

        Response response = request
                .body(map)
                .log().all()
                .put("users/2");

        response.then().assertThat().statusCode(200);

        Assertions.assertTrue(!response.jsonPath().get("updatedAt").toString().isEmpty(), "Empty updated time");
    }
}
