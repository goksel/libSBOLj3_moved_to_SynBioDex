package org.sbolstandard.core3.validation;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import org.apache.commons.lang3.StringUtils;
import org.sbolstandard.core3.entity.Identified;
import org.sbolstandard.core3.util.Configuration;
import org.sbolstandard.core3.util.Configuration.PropertyValidationType;
import org.sbolstandard.core3.util.SBOLGraphException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.ValidatorFactory;
import jakarta.validation.executable.ExecutableValidator;

public class PropertyValidator {
	private static PropertyValidator propertyValidator = null;
	protected ExecutableValidator validator;
	
	private PropertyValidator()
	{	
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
				propertyValidator.validator = factory.getValidator().forExecutables();	
			}
			catch (Exception exception)
			{
				throw new SBOLGraphException("Could not initialize the property validator", exception);
			}
		}
		return propertyValidator;
	}

	public void validate(Identified identified, String methodName, Object[] parameterValues, Class<?>... parameterTypes) throws SBOLGraphException
	{
		if (Configuration.getConfiguration().getPropertyValidationType()==PropertyValidationType.ValidateAfterSettingProperties)
		{
			Method method;
			try {
				method = identified.getClass().getMethod(methodName, parameterTypes);
			} 
			catch (NoSuchMethodException | SecurityException e) {
				throw new SBOLGraphException(e.getMessage(),e);
			}
			
			Set<ConstraintViolation<Identified>> violations = validator.validateParameters(identified, method,parameterValues);
			if (violations!=null && violations.size()>0){
				List<String> messages=new ArrayList<String>();
				for (ConstraintViolation<Identified> violation : violations) {
			    	List<String> fragments=new ArrayList<String>();
			    	fragments.add(violation.getMessage());
			    	fragments.add(String.format("Property: %s",violation.getPropertyPath().toString()));
			    	if (violation.getLeafBean()!=null && violation.getLeafBean() instanceof Identified ){
			    	    Identified identifiedLeaf= (Identified) violation.getLeafBean();
			    	    fragments.add(String.format("Entity URI: %s",identifiedLeaf.getUri().toString()));
			    	    fragments.add(String.format("Entity type: %s",identifiedLeaf.getClass()));    
			    	}
			    	/*if (violation.getRootBean()!=null && violation.getRootBean() instanceof Identified ){
			    	    Identified identifiedRoot= (Identified) violation.getRootBean();
			    	    fragments.add(String.format("Parent entity URI: %s",identifiedRoot.getUri().toString()));
			    	    fragments.add(String.format("Parent entity type: %s",identifiedRoot.getClass()));    
			    	}*/
			    	if (violation.getInvalidValue()!=null){
			    		fragments.add("Value:" + violation.getInvalidValue().toString());
			    	}
			    	String message=StringUtils.join(fragments, ",\r\n\t");
			    	messages.add(message);
				}
				
				String errorMessage= StringUtils.join(messages, ",\r\n\t");
				throw new SBOLGraphException(errorMessage);
			}
		}
	}
	
}
