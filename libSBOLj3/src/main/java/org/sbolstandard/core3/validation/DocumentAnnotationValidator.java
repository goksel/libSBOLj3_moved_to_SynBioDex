package org.sbolstandard.core3.validation;

import java.net.URI;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.sbolstandard.core3.entity.Identified;
import org.sbolstandard.core3.entity.SBOLDocument;
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

public class DocumentAnnotationValidator implements ConstraintValidator<ValidDocument, SBOLDocument> {

	@Override
    public void initialize(ValidDocument constraintAnnotation) {
    }

    @Override
    public boolean isValid(SBOLDocument document, ConstraintValidatorContext context) {
        boolean valid=true;
    	if (document!=null)
        {
    		List<ValidationMessage> messages=null;
    		try
    		{
    			messages=document.getValidationMessages();
    			if (messages!=null)
    	    	{
    				valid=false;
    	    		context.disableDefaultConstraintViolation();
    	    		for (ValidationMessage message: messages){
    	    				  //context.buildConstraintViolationWithTemplate(message.getMessage())
    	                      //.addPropertyNode(message.getProperty()).addConstraintViolation();
    	    			String messageString=message.getMessage();
    	    			if (message.getInvalidValue()!=null && !(message.getInvalidValue() instanceof Identified)){
    	    				String separator="," + System.lineSeparator()  + "\t";
    	    				messageString = String.format("%s%sValue: %s",messageString, separator, message.getInvalidValue().toString());
    	    			}
    	    				  //context.buildConstraintViolationWithTemplate(messageString)
    	                      //.addPropertyNode(message.getProperty()).addConstraintViolation();
    	    			ConstraintViolationBuilder violationBuilder= context.buildConstraintViolationWithTemplate(messageString);
    	    			
    	    			addViolationPath(violationBuilder, message);	
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


