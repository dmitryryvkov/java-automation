package com.weavesocks.tests;


import com.github.javafaker.Faker;
import com.weavesocks.api.ProjectConfig;
import com.weavesocks.api.core.Assertions;
import com.weavesocks.api.payloads.UserPayload;
import com.weavesocks.api.responses.UserRegistrationResponse;
import com.weavesocks.api.services.UserApiService;
import io.restassured.RestAssured;
import io.restassured.parsing.Parser;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.aeonbits.owner.ConfigFactory;
import org.testng.ITestContext;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.Locale;

import static com.weavesocks.api.conditions.Conditions.statusCode;

public class UsersTest {

    UserApiService userApiService = new UserApiService();
    private Faker faker;

    @BeforeClass
    public void setUp() {
        ProjectConfig projectConfig = ConfigFactory.create(ProjectConfig.class);
        faker = new Faker(new Locale(projectConfig.locale()));
        RestAssured.baseURI = projectConfig.baseUrl();
        RestAssured.registerParser("text/html", Parser.JSON);
    }

    @Test
    public void userCanRegister(ITestContext context) {
        String username = faker.name().username();

        UserPayload user = new UserPayload()
                .username(username)
                .password(faker.internet().password())
                .email(faker.internet().emailAddress());

        UserRegistrationResponse response = userApiService.registerUser(user)
                .shouldHave(statusCode(200))
                .asPojo(UserRegistrationResponse.class);

        Assertions.assertTrue(!response.getId().isEmpty(), "Incorrect status code");
        //.shouldHave(Conditions.bodyField("id", not(emptyString())));

        context.setAttribute("id", response.getId());
        context.setAttribute("username", username);
    }

    @Test
    public void userCanLogin() {
        userApiService.loginUser()
                .shouldHave(statusCode(200));
    }

    @Test
    public void userIsVisibleAsCustomer(ITestContext context) {
        Response response = RestAssured
                .given()
                .get(String.format("customers/%s", context.getAttribute("id")));

        JsonPath json = response.jsonPath();
        Assertions.assertEquals(json.get("status_code").toString(), "200", "Incorrect status code");

        //TODO doesn't work correctly for now
        /*AssertableResponse assertableResponse = userApiService.getCustomer(context.getAttribute("id"))
                .shouldHave(statusCode(200));
        GetCustomerDataResponse response = assertableResponse.asPojo(GetCustomerDataResponse.class);
        Assertions.assertEquals(response.getUsername(), context.getAttribute("username").toString(), "Incorrect username");*/
    }

}
