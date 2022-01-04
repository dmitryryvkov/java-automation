package com.weavesocks.api.services;

import com.weavesocks.api.assertions.AssertableResponse;
import com.weavesocks.api.payloads.UserPayload;
import io.restassured.RestAssured;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UserApiService extends ApiService {

    public AssertableResponse registerUser(UserPayload user) {

        return new AssertableResponse(setUp()
                .body(user)
                .when()
                .post("register"));
    }

    public AssertableResponse loginUser(){

        return new AssertableResponse(setUp()
                .header("Authorization", "Basic Og==")
                .when()
                .get("login"));
    }

    public AssertableResponse getCustomer(Object userId){
        return new AssertableResponse(RestAssured
                .given()
                .filters(getFilters())
                .get(String.format("customers/%s", userId)));
    }
}
