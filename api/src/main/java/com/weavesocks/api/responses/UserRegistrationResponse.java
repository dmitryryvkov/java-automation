package com.weavesocks.api.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRegistrationResponse{

	@JsonProperty("id")
	private String id;

	@JsonProperty("statusCode")
	private String statusCode;

}
