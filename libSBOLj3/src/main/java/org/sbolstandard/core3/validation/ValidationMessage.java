package org.sbolstandard.core3.validation;

import java.net.URI;

import org.sbolstandard.core3.entity.Identified;
import org.sbolstandard.core3.util.SBOLGraphException;

public class ValidationMessage {
	private String message;
	private URI property;
	private Object invalidValue;
	private Identified childEntity;
	private ValidationMessage childMessage;

	public ValidationMessage(String message, URI property)
	{
		this.message=message;
		this.property=property;
	}
	
	public ValidationMessage(String message, URI property, Object invalidValue)
	{
		this.message=message;
		this.property=property;
		this.invalidValue=invalidValue;
	}
	
	
	public ValidationMessage(String message, URI property, Identified invalidChildEntity, Object invalidValue)
	{
		this.message=message;
		this.property=property;
		this.childEntity=invalidChildEntity;
		this.invalidValue=invalidValue;
	}
	
	public String getMessage() {
		return message;
	}
	/*public void setMessage(String message) {
		this.message=message;
	}*/
	
	public URI getProperty() {
		return property;
	}
	public Object getInvalidValue() {
		return this.invalidValue;
	}
	
	public void setInvalidValue(Object invalidValue) {
		this.invalidValue=invalidValue;
	}
	
	public ValidationMessage getChildMessage() {
		return this.childMessage;
	}

	public Identified getChildEntity() {
		return this.childEntity;
	}
	public Identified setChildEntity(Identified childEntity) {
		return this.childEntity= childEntity;
	}

	
	public ValidationMessage childPath(URI property, Identified entity) throws SBOLGraphException
	{
		if (this.childEntity!=null)
		{
			this.childMessage=new ValidationMessage(null, property, null);
			this.childMessage.childEntity=entity;
			this.childMessage.property=property;
			return childMessage;
		}
		else
		{
			throw new SBOLGraphException("Additional paths can be added if the invalid child entity is provided.");
		}
	}
	
	public ValidationMessage childPath(URI property) throws SBOLGraphException
	{
		if (this.childEntity!=null)
		{
			this.childMessage=new ValidationMessage(null, property, null);
			this.childMessage.childEntity=null;
			this.childMessage.property=property;
			return childMessage;
		}
		else
		{
			throw new SBOLGraphException("Additional paths can be added if the invalid child entity is provided.");
		}
	}
	


}
