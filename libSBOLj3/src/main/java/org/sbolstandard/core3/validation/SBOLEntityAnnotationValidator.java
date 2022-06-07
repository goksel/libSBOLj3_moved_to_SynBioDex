package org.sbolstandard.core3.validation;

import java.net.URI;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.sbolstandard.core3.entity.Identified;
import org.sbolstandard.core3.util.SBOLGraphException;
import org.sbolstandard.core3.util.SBOLUtil;
import org.sbolstandard.core3.util.URINameSpace;

import com.google.protobuf.Message;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.ConstraintValidatorContext.ConstraintViolationBuilder;
import jakarta.validation.ConstraintValidatorContext.ConstraintViolationBuilder.LeafNodeBuilderCustomizableContext;
import jakarta.validation.ConstraintValidatorContext.ConstraintViolationBuilder.LeafNodeBuilderDefinedContext;
import jakarta.validation.ConstraintValidatorContext.ConstraintViolationBuilder.NodeBuilderCustomizableContext;

public class SBOLEntityAnnotationValidator implements ConstraintValidator<ValidSBOLEntity, ValidatableSBOLEntity> {

	@Override
    public void initialize(ValidSBOLEntity constraintAnnotation) {
    }

    @Override
    public boolean isValid(ValidatableSBOLEntity identified, ConstraintValidatorContext context) {
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
    	    		for (ValidationMessage message: messages){
    	    				  //context.buildConstraintViolationWithTemplate(message.getMessage())
    	                      //.addPropertyNode(message.getProperty()).addConstraintViolation();
    	    			String messageString=message.getMessage();
    	    			String separator="," + System.lineSeparator()  + "\t";
	    				if (message.getInvalidValue()!=null && !(message.getInvalidValue() instanceof ValidatableSBOLEntity)){
    	    				messageString = String.format("%s%sValue: %s",messageString, separator, message.getInvalidValue().toString());
    	    			}
	    				else if (message.getInvalidValue()!=null && message.getInvalidValue() instanceof Identified){
    	    				Identified invalidIdentified= (Identified ) message.getInvalidValue() ;
    	    				messageString = String.format("%s%sChild Entity URI: %s",messageString, separator, invalidIdentified.getUri().toString());
    	    				messageString = String.format("%s%sChild Entity Type: %s",messageString, separator, invalidIdentified.getClass());  	
    	    			}
        	    			
    	    				  //context.buildConstraintViolationWithTemplate(messageString)
    	                      //.addPropertyNode(message.getProperty()).addConstraintViolation();
    	    			ConstraintViolationBuilder violationBuilder= context.buildConstraintViolationWithTemplate(messageString);
    	    			
    	    			addViolationPath(violationBuilder, message);
    	    			
    	    			//combinatorialDerivations[0].addresses[home].country.name
    	    			/*context.buildConstraintViolationWithTemplate( "this detail is wrong" )
    	                .addPropertyNode( "addresses" )
    	                .addPropertyNode( "country" )
    	                    .inContainer( Map.class, 1 )
    	                    .inIterable().atKey( "home" )
    	                .addPropertyNode( "name" )
    	                .addConstraintViolation();*/

    	    			/*
    	    			NodeBuilderCustomizableContext nbcc= violationBuilder.addPropertyNode(SBOLUtil.toQualifiedString(message.getProperty()));
    	    			
    	    			if (message.getChildEntity()!=null){
	    	    			Identified childEntity=message.getChildEntity();
	    	    			String key=childEntity.getUri().toString();
	    	    			Class<?> childClass=childEntity.getClass();
	    	    			LeafNodeBuilderDefinedContext entityContext=nbcc.addBeanNode().inContainer(childClass, 1).inIterable().atKey(key);
	    	    			if (message.getChildMessage()!=null)
	    	    			{
	    	    				NodeBuilderCustomizableContext childContext=violationBuilder.addPropertyNode(SBOLUtil.toQualifiedString(message.getChildMessage().getProperty()));
	    	    				childContext.addConstraintViolation();
	    	    			}
	    	    			else
	    	    			{
	    	    				entityContext.addConstraintViolation(); 
	    	    			}
	    	    	    }
    	    			else
    	    			{
    	    			
    	    				nbcc.addConstraintViolation();  
    	    			}*/
    	    			
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
    
    private boolean addViolationPath(ConstraintViolationBuilder violationBuilder, ValidationMessage message)
    {
    	boolean added=false;
    	if (message!=null)
    	{
	    	NodeBuilderCustomizableContext nbcc= violationBuilder.addPropertyNode(SBOLUtil.toQualifiedString(message.getProperty()));
			
			if (message.getChildEntity()!=null){
				Identified childEntity=message.getChildEntity();
				String key=childEntity.getUri().toString();
				Class<?> childClass=childEntity.getClass();
				LeafNodeBuilderDefinedContext entityContext=nbcc.addBeanNode().inContainer(childClass, 1).inIterable().atKey(key);
				//LeafNodeBuilderDefinedContext entityContext=nbcc.addBeanNode().inIterable().atKey(key);
				
				added=addViolationPath(violationBuilder, message.getChildMessage());
				if (!added)
				{
					entityContext.addConstraintViolation(); 
					added=true;
				}
		    }
			if (!added)
			{		
				nbcc.addConstraintViolation();  
				added=true;
			}
    	}
    	return added;
    }
    
}


