package com.weavesocks.tests;


import com.github.javafaker.Faker;
import com.weavesocks.api.ProjectConfig;
import com.weavesocks.api.conditions.Conditions;
import com.weavesocks.api.payloads.UserPayload;
import com.weavesocks.api.services.UserApiService;
import io.restassured.RestAssured;
import org.aeonbits.owner.ConfigFactory;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.Locale;

import static org.hamcrest.Matchers.emptyString;
import static org.hamcrest.core.IsNot.not;

public class UsersTest {

    UserApiService userApiService = new UserApiService();
    private Faker faker;

    @BeforeClass
    public void setUp() {
        ProjectConfig projectConfig = ConfigFactory.create(ProjectConfig.class);
        faker = new Faker(new Locale(projectConfig.locale()));
        RestAssured.baseURI = projectConfig.baseUrl();
    }

    @Test
    public void testCanRegisterNewUser() {
        UserPayload user = new UserPayload()
                .username(faker.name().username())
                .password(faker.internet().password())
                .email(faker.internet().emailAddress());

        userApiService.registerUser(user)
                .shouldHave(Conditions.statusCode(200))
                .shouldHave(Conditions.bodyField("id", not(emptyString())));
    }
}
