package org.sbolstandard.core3.validation;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import org.apache.commons.lang3.StringUtils;
import org.sbolstandard.core3.entity.Identified;
import org.sbolstandard.core3.util.Configuration;
import org.sbolstandard.core3.util.SBOLGraphException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.ValidatorFactory;
import jakarta.validation.executable.ExecutableValidator;

public class PropertyValidator {
	private static PropertyValidator propertyValidator = null;
	private ExecutableValidator validator;
	
	private PropertyValidator()
	{	
	}
	
	private void setExecutableValidator(ExecutableValidator executableValidator) throws SBOLGraphException	
	{
		if (executableValidator==null)
		{
			throw new SBOLGraphException("Unable to create an ExecutableValidator");
		}
			
		this.validator=executableValidator;
	}
	
	public static PropertyValidator getValidator() throws SBOLGraphException
	{
		if (propertyValidator == null)
		{
			try
			{
				propertyValidator=new PropertyValidator();
				ValidatorFactory factory = Validation.byDefaultProvider()
		 	            .configure()
		 	            .buildValidatorFactory();
				
				//propertyValidator.validator = factory.getValidator().forExecutables();	
				propertyValidator.setExecutableValidator(factory.getValidator().forExecutables());	
			}
			catch (Exception exception)
			{
				throw new SBOLGraphException("Could not initialize the property validator. " + exception.getMessage(), exception);
			}
		}
		return propertyValidator;
	}

	
	
	public void validateReturnValue(Identified identified, String methodName, Object returnValue, Class<?>... parameterTypes) throws SBOLGraphException
	{
		Method method;
		try {
			method = identified.getClass().getMethod(methodName, parameterTypes);
		} 
		catch (NoSuchMethodException | SecurityException e) {
			throw new SBOLGraphException(e.getMessage(),e);
		}
		
		Set<ConstraintViolation<Identified>> violations = this.validator.validateReturnValue(identified, method,returnValue);
		processViolations(violations);
	}
	
	public static String  getViolotionMessage(ConstraintViolation<?> violation)
	{
		List<String> fragments=new ArrayList<String>();
    	fragments.add(violation.getMessage());
    	fragments.add(String.format("Property: %s",violation.getPropertyPath().toString()));
    	if (violation.getLeafBean()!=null && violation.getLeafBean() instanceof Identified ){
    	    Identified identifiedLeaf= (Identified) violation.getLeafBean();
    	    fragments.add(String.format("Entity URI: %s",identifiedLeaf.getUri().toString()));
    	    fragments.add(String.format("Entity type: %s",identifiedLeaf.getClass()));    
    	}
    	if (violation.getInvalidValue()!=null && !(violation.getInvalidValue() instanceof Identified)){    	
    		fragments.add("Value:" + violation.getInvalidValue().toString());
    	}
    	String message=StringUtils.join(fragments, "," + System.lineSeparator()  + "\t");
    	return message;
	}
	
	public static List<String>  getViolotionMessages(Set<ConstraintViolation<Identified>> violations)
	{
		List<String> messages=null;
		if (violations!=null && violations.size()>0){
			messages=new ArrayList<String>();
			for (ConstraintViolation<Identified> violation : violations) {
		    	messages.add(getViolotionMessage(violation));
			}
		}
		return messages;
	}
	private void processViolations(Set<ConstraintViolation<Identified>> violations) throws SBOLGraphException
	{
		List<String> messages=PropertyValidator.getViolotionMessages(violations);
		if (messages!=null && messages.size()>0)
		{	
			String errorMessage=StringUtils.join(messages, "," + System.lineSeparator()  + "\t");	
			throw new SBOLGraphException(errorMessage);
		
		}
	}
	
	public void validate(Identified identified, String methodName, Object[] parameterValues, Class<?>... parameterTypes) throws SBOLGraphException
	{
		if (Configuration.getConfiguration().isValidateAfterSettingProperties())
		{
			Method method;
			try {
				method = identified.getClass().getMethod(methodName, parameterTypes);
			} 
			catch (NoSuchMethodException | SecurityException e) {
				throw new SBOLGraphException(e.getMessage(),e);
			}
			
			Set<ConstraintViolation<Identified>> violations = this.validator.validateParameters(identified, method,parameterValues);
			processViolations(violations);
		}
	}
	
}
