package com.weavesocks.api.responses;


import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class GetCustomerDataResponse {

    public String error;
    public Integer statusCode;
    public String statusText;

}