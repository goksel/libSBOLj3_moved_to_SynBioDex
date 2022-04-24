package org.sbolstandard.core3.validation;

public class ValidationMessage {
	private String message;
	private String property;

	public ValidationMessage(String message, String property)
	{
		this.message=message;
		this.property=property;
	}
	
	public String getMessage() {
		return message;
	}
	
	public String getProperty() {
		return property;
	}

}
