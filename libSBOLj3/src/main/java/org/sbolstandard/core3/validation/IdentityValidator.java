package org.sbolstandard.core3.validation;


import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.OptionalInt;
import java.util.Set;

import org.apache.jena.rdf.model.Resource;
import org.sbolstandard.core3.entity.Identified;
import org.sbolstandard.core3.util.Configuration;
import org.sbolstandard.core3.util.RDFUtil;
import org.sbolstandard.core3.util.SBOLGraphException;
import org.sbolstandard.core3.vocabulary.DataModel;
import org.sbolstandard.core3.vocabulary.MeasureDataModel;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

public class IdentityValidator {
	private static IdentityValidator idendityValidator = null;
	protected Validator validator;
	
	private IdentityValidator()
	{
		
	}
	
	public static IdentityValidator getValidator()
	{
		if (idendityValidator == null)
		{
			try
			{
				idendityValidator=new IdentityValidator();
				ValidatorFactory factory = Validation.byDefaultProvider()
		 	            .configure()
		 	            //.addValueExtractor(new ...ValueExtractor())
		 	            .buildValidatorFactory();
				idendityValidator.validator = factory.getValidator();	
			}
			catch (Exception exception)
			{
				throw new Error(new SBOLGraphException("Could not initialize the validator", exception));
			}
		}
		return idendityValidator;
	}
	
	public List<String> validate(Identified identified)
	{
	    Set<ConstraintViolation<Identified>> violations = validator.validate(identified);
	      List<String> messages=new ArrayList<String>();
	      for (ConstraintViolation<Identified> violation : violations) {
	    	    String propertyMessage=String.format("Property: %s",violation.getPropertyPath().toString());
	    	    String entityMessage =String.format("Entity Type: %s",violation.getRootBeanClass().getName());
	    	    String uriMessage="";
	    	    if (violation.getLeafBean()!=null)
	    	    {
	    	    	URI uri=((Identified) violation.getRootBean()).getUri();
	    	    	uriMessage =String.format("Entity: %s",uri.toString());
	    	    }
	    	    String message=String.format("%s, %s, %s, %s", violation.getMessage(), entityMessage, propertyMessage, uriMessage);
	    	    messages.add(message);
	    	}
	      return messages;
	}
	
	public URI getPropertyAsURI(Resource resource, URI property) throws SBOLGraphException
	{
		URI result=null;
		if (Configuration.getConfiguration().enforceOneToOneRelationships())
		{
			List<URI> uris=RDFUtil.getPropertiesAsURIs(resource, property);
			if (uris!=null && uris.size()>0)
			{
				if (uris.size()>1)
				{
					 String message=String.format("Multiple values are included for property %s. URI:%s", property.toString(), resource.getURI());
					 throw new SBOLGraphException(message);
				}
				else
				{
					result=uris.get(0);
				}
			}
		}	
		else
		{
			result=RDFUtil.getPropertyAsURI(resource, property);
		}
		return result;	
	}
	
	public String getPropertyAsString(Resource resource, URI property) throws SBOLGraphException
	{
		String result=null;
		if (Configuration.getConfiguration().enforceOneToOneRelationships())
		{
			List<String> uris=RDFUtil.getPropertiesAsStrings(resource, property);
			if (uris!=null && uris.size()>0)
			{
				if (uris.size()>1)
				{
					 String message=String.format("Multiple values are included for property %s. URI:%s", property.toString(), resource.getURI());
					 throw new SBOLGraphException(message);
				}
				else
				{
					result=uris.get(0);
				}
			}
		}	
		else
		{
			result=RDFUtil.getPropertyAsString(resource, property);
		}
		return result;
		
	}
	
	public OptionalInt getPropertyAsOptionalInt(Resource resource, URI property) throws SBOLGraphException
	{
		OptionalInt result=OptionalInt.empty();
		String value=IdentityValidator.getValidator().getPropertyAsString(resource, property);
		if (value!=null){
			result=OptionalInt.of(Integer.valueOf(value));
		}
		return result;
	}
	
	public void setPropertyAsOptionalInt(Resource resource, URI property, OptionalInt value)
	{
		String stringValue=null;
		if (value.isPresent())
		{
			stringValue= String.valueOf(value.getAsInt());
		}
		RDFUtil.setProperty(resource, property, stringValue);	
	}
	
	/*
	public String getRequiredPropertyAsString(Resource resource, URI property) throws SBOLGraphException
	{
		String value=getPropertyAsString(resource, property);
		if (value==null)
		{
			throw new SBOLGraphException("Cannot read the value. Property:" + property+ " Uri:+ " +  resource.getURI());
			
		}
		return value;
		
	}*/

}
