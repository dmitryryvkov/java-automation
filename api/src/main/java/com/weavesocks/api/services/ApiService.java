package com.weavesocks.api.services;

import com.weavesocks.api.ProjectConfig;
import io.restassured.RestAssured;
import io.restassured.filter.Filter;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.aeonbits.owner.ConfigFactory;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ApiService {
    ProjectConfig projectConfig = ConfigFactory.create(ProjectConfig.class);

    protected RequestSpecification setUpRequest(){
        return RestAssured
                .given().contentType(ContentType.JSON)
                .filters(getFilters());
    }

    List<Filter> getFilters(){
        if(projectConfig.logging()){
            return Arrays.asList(new RequestLoggingFilter(), new ResponseLoggingFilter());
        }
        else return Collections.emptyList();
    }
}
