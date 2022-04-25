package org.sbolstandard.core3.validation;

import java.net.URI;
import java.util.List;

import org.sbolstandard.core3.entity.Identified;
import org.sbolstandard.core3.util.SBOLGraphException;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class IdentifiedAnnotationValidator implements ConstraintValidator<ValidIdentified, Identified> {

	@Override
    public void initialize(ValidIdentified constraintAnnotation) {
    }

    @Override
    public boolean isValid(Identified identified, ConstraintValidatorContext context) {
        boolean valid=true;
    	if (identified!=null)
        {
    		/*
        	List<URI> wasDerivedFroms=identified.getWasDerivedFrom();
        	if (wasDerivedFroms!=null && wasDerivedFroms.contains(identified.getUri()))
        	{
        		valid = false;
        		context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate( "{IDENTIFIED_REFERREDBY_WASDERIVEDFROM}" )
                         .addPropertyNode( "wasDerivedFrom" ).addConstraintViolation();
                  
        	}
        	String name="";
        	try
        	{
        		name= identified.getName();
        	}catch(SBOLGraphException e) {}
        	
        	if (name!=null && name.equals("invalidname"))
        	{
        		 context.buildConstraintViolationWithTemplate( "{RANGE_START_NOT_NULL}" )
                 .addPropertyNode( "name" ).addConstraintViolation();
        	}*/
    		List<ValidationMessage> messages=null;
    		try
    		{
    			 messages=identified.getValidationMessages();
    			 if (messages!=null)
    	    		{
    	    			valid=false;
    	    			context.disableDefaultConstraintViolation();
    	    			for (ValidationMessage message: messages)
    	    			{
    	    				  context.buildConstraintViolationWithTemplate(message.getMessage())
    	                      .addPropertyNode(message.getProperty()).addConstraintViolation();
    	    			}
    	    		}
    		}
    		catch (SBOLGraphException e)
    		{
    			valid=false;
    			context.disableDefaultConstraintViolation();
    			context.buildConstraintViolationWithTemplate(e.getMessage() + " Exception: " + e.toString())
                .addPropertyNode("error").addConstraintViolation();
    		}
    		
        }
    	return valid;
    }
}
