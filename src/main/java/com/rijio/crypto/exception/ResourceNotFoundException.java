package com.rijio.crypto.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException{
	private String resourceName;
	private String fieldName;
	private String fieldValue;

	public ResourceNotFoundException(String resourceName, String fieldName, String currentUserName) {
		super(String.format("%s not found with %s: '%s'", resourceName, fieldName, currentUserName));
		this.resourceName = resourceName;
		this.fieldName = fieldName;
		this.fieldValue = currentUserName;
	}
	
	public String getFieldName() {
		return fieldName;
	}
	
	public String getFieldValue() {
		return fieldValue;
	}
	
	public String getResourceName() {
		return resourceName;
	}
}