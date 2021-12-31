package com.weavesocks.api.services;

import com.weavesocks.api.assertions.AssertableResponse;
import com.weavesocks.api.payloads.UserPayload;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UserApiService extends ApiService {

    public AssertableResponse registerUser(UserPayload user) {

        return new AssertableResponse(setUp()
                .body(user)
                .when()
                .post("register"));
    }

}
