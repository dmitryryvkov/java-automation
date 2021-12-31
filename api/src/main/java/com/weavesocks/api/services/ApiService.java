package com.weavesocks.api.services;

import io.restassured.RestAssured;
import io.restassured.filter.Filter;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ApiService {

    protected RequestSpecification setUp(){
        return RestAssured
                .given().contentType(ContentType.JSON)
                .filters(getFilters());
    }

    private List<Filter> getFilters(){
        boolean enable = Boolean.parseBoolean(System.getProperty("logging", "true"));
        if(enable){
            return Arrays.asList(new RequestLoggingFilter(), new ResponseLoggingFilter());
        }
        else return Collections.emptyList();
    }
}
