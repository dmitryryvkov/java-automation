package com.weavesocks.tests;


import com.github.javafaker.Faker;
import com.weavesocks.api.ProjectConfig;
import com.weavesocks.api.conditions.Conditions;
import com.weavesocks.api.core.Assertions;
import com.weavesocks.api.payloads.UserPayload;
import com.weavesocks.api.responses.UserRegistrationResponse;
import com.weavesocks.api.services.UserApiService;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.aeonbits.owner.ConfigFactory;
import org.testng.ITestContext;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class UsersTest {

    UserApiService userApiService = new UserApiService();
    private Faker faker;

    @BeforeClass
    public void setUp() {
        ProjectConfig projectConfig = ConfigFactory.create(ProjectConfig.class);
        faker = new Faker(new Locale(projectConfig.locale()));
        RestAssured.baseURI = projectConfig.baseUrl();
    }

    @Test(enabled=false)
    public void userCanRegister(ITestContext context) {
        String username = faker.name().username();

        UserPayload user = new UserPayload()
                .username(username)
                .password(faker.internet().password())
                .email(faker.internet().emailAddress());

        UserRegistrationResponse response = userApiService.registerUser(user)
                .shouldHave(Conditions.statusCode(200))
                .asPojo(UserRegistrationResponse.class);

        Assertions.assertTrue(!response.getId().isEmpty(), "Incorrect status code");
        //.shouldHave(Conditions.bodyField("id", not(emptyString())));

        context.setAttribute("id", response.getId());
        context.setAttribute("username", username);
    }

    @Test
    public void testCheckAbilityToGetAllUsers() {
        Response response = RestAssured
                .given().log().all()
                .get("api/users?page=2");
        response.then().assertThat().statusCode(200);

        JsonPath json = response.jsonPath();
        Assertions.assertEquals(json.get("total").toString(), "12", "Incorrect count of users");
    }

    @Test
    public void testCheckSingleUserInfo() {
        Response response = RestAssured
                .given().log().all()
                .get("api/users/1");

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
                .delete("api/users/1");

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
                .post("api/users");

        response.then().assertThat().statusCode(201);

        Assertions.assertTrue(!response.jsonPath().get("id").toString().isEmpty(), "Empty id parameter");
        Assertions.assertTrue(!response.jsonPath().get("createdAt").toString().isEmpty(),"Empty creation time");
    }

    @Test
    public void newUserCanBeRegisteredSuccessfully() {
        RequestSpecification request = RestAssured.given();
        Map<String, Object> map = new HashMap<>();
        map.put("email", "eve.holt@reqres.in");
        map.put("password", "pistol");

        Response response = request
                .body(map)
                .log().all()
                .post("api/register");

        response.then().assertThat().statusCode(200);

        Assertions.assertTrue(!response.jsonPath().get("id").toString().isEmpty(), "Empty id parameter");
        Assertions.assertTrue(!response.jsonPath().get("token").toString().isEmpty(),"Empty token");
    }

    @Test
    public void newUserCanNotBeRegisteredSuccessfullyWithEmptyPassword() {
        RequestSpecification request = RestAssured.given();
        Map<String, Object> map = new HashMap<>();
        map.put("email", "eve.holt@reqres.in");

        Response response = request
                .body(map)
                .log().all()
                .post("api/register");

        response.then().assertThat().statusCode(400);

        Assertions.assertTrue(!response.jsonPath().get("error").toString().isEmpty(), "Missing password");

    }

    @Test
    public void newUserCanNotBeRegisteredSuccessfullyWithEmptyEmail() {
        RequestSpecification request = RestAssured.given();
        Map<String, Object> map = new HashMap<>();
        map.put("password", "pistol");

        Response response = request
                .body(map)
                .log().all()
                .post("api/register");

        response.then().assertThat().statusCode(400);

        Assertions.assertTrue(!response.jsonPath().get("error").toString().isEmpty(), "Missing email");

    }

/*
    @Test
    public void userIsVisibleAsCustomer(ITestContext context) {
        Response response = RestAssured
                .given()
                .contentType(ContentType.MULTIPART).log().all()
                .get(String.format("customers/%s", context.getAttribute("id")));

        JsonPath json = response.jsonPath();
        Assertions.assertEquals(json.get("status_code").toString(), "200", "Incorrect status code");

        //TODO doesn't work correctly for now
        *//*AssertableResponse assertableResponse = userApiService.getCustomer(context.getAttribute("id"))
                .shouldHave(statusCode(200));
        GetCustomerDataResponse response = assertableResponse.asPojo(GetCustomerDataResponse.class);
        Assertions.assertEquals(response.getUsername(), context.getAttribute("username").toString(), "Incorrect username");*//*
    }

    @Test
    public void userCanGetAddresses(){

        RestAssured.defaultParser = Parser.fromContentType("hal+json");

        userApiService.getAddresses()
                .shouldHave(statusCode(200))
                .shouldHave(Conditions.bodyField("_embedded", not(emptyString())));
    }

    @Test
    public void userCanGetCards(){
        userApiService.getCards()
                .shouldHave(statusCode(200));
                //.shouldHave(Conditions.bodyField("_embedded", not(emptyString())));
    }*/


}
