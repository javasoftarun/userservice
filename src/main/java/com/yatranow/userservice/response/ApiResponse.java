package com.yatranow.userservice.response;

import com.yatranow.userservice.entity.User;

public class ApiResponse {
	
	private String responseMessage;
	private User responseData;
	private int responseCode;
	
	public ApiResponse(String responseMessage, User responseData, int responseCode) {
		this.responseMessage = responseMessage;
		this.responseData = responseData;
		this.responseCode = responseCode;
	}

	public String getResponseMessage() {
		return responseMessage;
	}

	public void setResponseMessage(String responseMessage) {
		this.responseMessage = responseMessage;
	}

	public User getResponseData() {
		return responseData;
	}

	public void setResponseData(User responseData) {
		this.responseData = responseData;
	}

	public int getResponseCode() {
		return responseCode;
	}

	public void setResponseCode(int responseCode) {
		this.responseCode = responseCode;
	}
	
}
