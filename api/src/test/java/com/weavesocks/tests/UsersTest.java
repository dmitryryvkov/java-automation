package com.weavesocks.tests;


import com.weavesocks.api.ProjectConfig;
import com.weavesocks.api.services.ApiService;
import io.restassured.RestAssured;
import org.aeonbits.owner.ConfigFactory;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;

public class UsersTest extends ApiService {

    //private Faker faker;

    @BeforeClass
    public void setUp() {
        ProjectConfig projectConfig = ConfigFactory.create(ProjectConfig.class);
        //faker = new Faker(new Locale(projectConfig.locale()));
        RestAssured.baseURI = projectConfig.baseUrl();
    }

    @Test
    public void testCheckAbilityToGetAllUsers() {

        setUpRequest()
                .get("users?page=2")
                .then()
                .statusCode(SC_OK)
                .body(
                        "total", is(12)
                );
    }

    @Test
    public void testCheckSingleUserInfo() {
        setUpRequest()
                .get("users/1")
                .then()
                .statusCode(SC_OK)
                .body(
                        "data.id", is(1),
                        "data.email", is("george.bluth@reqres.in"),
                        "data.first_name", is("George"),
                        "data.last_name", is("Bluth")
                );
    }

    @Test
    public void userCanBeDeleted() {
        setUpRequest()
                .delete("users/1")
                .then()
                .statusCode(SC_NO_CONTENT);
    }

    @Test
    public void userCanBeCreated() {
        Map<String, Object> user = new HashMap<>();
        user.put("name", "morpheus");
        user.put("job", "leader");

        setUpRequest()
                .body(user)
                .post("users")
                .then()
                .statusCode(SC_CREATED)
                .body(
                        "name", is(user.get("name")),
                        "job", is(user.get("job")),
                        "id", notNullValue(),
                        "createdAt", notNullValue()
                );
    }

    @Test
    public void newUserCanBeRegisteredSuccessfully() {
        Map<String, Object> user = new HashMap<>();
        user.put("password", "pistol");
        user.put("email", "eve.holt@reqres.in");

        setUpRequest()
                .body(user)
                .post("register")
                .then()
                .statusCode(SC_OK)
                .body(
                        "id", is(4),
                        "token", notNullValue()
                );
    }

    @Test
    public void newUserCanNotBeRegisteredSuccessfullyWithEmptyPassword() {
        Map<String, Object> user = new HashMap<>();
        user.put("email", "eve.holt@reqres.in");

        setUpRequest()
                .body(user)
                .post("register")
                .then()
                .statusCode(SC_BAD_REQUEST)
                .body(
                        "error", is("Missing email or username")
                );
    }

    @Test
    public void newUserCanNotBeRegisteredSuccessfullyWithEmptyEmail() {
        Map<String, Object> user = new HashMap<>();
        user.put("password", "pistol");

        setUpRequest()
                .body(user)
                .post("register")
                .then()
                .statusCode(SC_BAD_REQUEST)
                .body(
                        "error", is("Missing email or username")
                );
    }

    @Test
    public void userCanLogin() {
        Map<String, Object> user = new HashMap<>();
        user.put("email", "eve.holt@reqres.in");
        user.put("password", "pistol");

        setUpRequest()
                .body(user)
                .post("login")
                .then()
                .statusCode(SC_OK)
                .body(
                        "token", notNullValue()
                );

    }

    @Test
    public void userCanNotLoginWithMissingEmail() {
        Map<String, Object> user = new HashMap<>();
        user.put("password", "pistol");

        setUpRequest()
                .body(user)
                .post("login")
                .then()
                .statusCode(SC_BAD_REQUEST)
                .body(
                        "error", is("Missing email or username")
                );
    }

    @Test
    public void userCanNotLoginWithMissingPassword() {
        Map<String, Object> user = new HashMap<>();
        user.put("password", "pistol");

        setUpRequest()
                .body(user)
                .post("login")
                .then()
                .statusCode(SC_BAD_REQUEST)
                .body(
                        "error", is("Missing email or username")
                );
    }

    @Test
    public void userDataCanUpdatedByPatchMethod() {
        Map<String, Object> user = new HashMap<>();
        user.put("name", "morpheus");
        user.put("job", "zion resident");

        setUpRequest()
                .body(user)
                .patch("users/2")
                .then()
                .statusCode(SC_OK)
                .body(
                        "name", is(user.get("name")),
                        "job", is(user.get("job")),
                        "updatedAt", notNullValue()
                );
    }

    @Test
    public void userDataCanUpdatedByPutMethod() {
        Map<String, Object> user = new HashMap<>();
        user.put("name", "Alex");
        user.put("job", "petuch");

        setUpRequest()
                .body(user)
                .put("users/2")
                .then()
                .statusCode(SC_OK)
                .body(
                        "name", is(user.get("name")),
                        "job", is(user.get("job")),
                        "updatedAt", notNullValue()
                );
    }
}
