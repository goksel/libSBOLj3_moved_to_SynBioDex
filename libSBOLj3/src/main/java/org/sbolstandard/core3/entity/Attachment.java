package org.sbolstandard.core3.entity;

import java.net.URI;
import java.util.List;
import java.util.OptionalLong;
import org.apache.jena.rdf.model.Resource;
import org.sbolstandard.core3.util.Configuration;
import org.sbolstandard.core3.util.RDFUtil;
import org.sbolstandard.core3.util.SBOLGraphException;
import org.sbolstandard.core3.validation.IdentifiedValidator;
import org.sbolstandard.core3.validation.PropertyValidator;
import org.sbolstandard.core3.validation.ValidationMessage;
import org.sbolstandard.core3.vocabulary.DataModel;
import org.sbolstandard.core3.vocabulary.HashAlgorithm;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

public class Attachment extends TopLevel{
	/*private URI source=null;
	private URI format=null;
	private OptionalLong size=OptionalLong.empty();
	private String hash;
	private String hashAlgorithm;*/
	

	protected  Attachment(org.apache.jena.rdf.model.Model model,URI uri) throws SBOLGraphException
	{
		super(model, uri);
	}
	
	protected  Attachment(Resource resource) throws SBOLGraphException
	{
		super(resource);
	}

	@Override
	public List<ValidationMessage> getValidationMessages() throws SBOLGraphException
	{
		List<ValidationMessage> validationMessages=super.getValidationMessages();
		if (this.getHash()!=null && this.getHashAlgorithm()==null)
		{
			validationMessages= addToValidations(validationMessages,new ValidationMessage("{ATTACHMENT_HASHGORITHM_NOT_NULL_IF_HASH_IS_PROVIDED}", DataModel.Attachment.hashAlgorithm));      	
		}
		
		//ATTACHMENT_FORMAT_EDAM_URI
		validationMessages=assertValidAttachmentFormat(validationMessages);
		
		//ATTACHMENT_ALGORITHM_VALID_IF_NOT_NULL
		validationMessages= assertValidHashAlgorithm(validationMessages);
		
		return validationMessages;
	}	
	
    private List<ValidationMessage> assertValidAttachmentFormat(List<ValidationMessage> validationMessages) throws SBOLGraphException
    {
    	if (Configuration.getInstance().isValidateRecommendedRules()){
			URI format=this.getFormat();			
			if (format!=null && !Configuration.getInstance().getEdamFileFormatTerms().contains(format.toString())){
				ValidationMessage message = new ValidationMessage("{ATTACHMENT_FORMAT_EDAM_URI}", DataModel.Attachment.format, format);
				validationMessages=IdentifiedValidator.addToValidations(validationMessages, message);
			}
		}	
    	return validationMessages;
    }
    
    private List<ValidationMessage> assertValidHashAlgorithm(List<ValidationMessage> validationMessages) throws SBOLGraphException{
    	if (Configuration.getInstance().isValidateRecommendedRules()){
			String value=this.getHashAlgorithm();
			if (value!=null){
				HashAlgorithm alg=HashAlgorithm.get(value);
				if (alg==null){										
					ValidationMessage message = new ValidationMessage("{ATTACHMENT_ALGORITHM_VALID_IF_NOT_NULL}", DataModel.Attachment.hashAlgorithm, value);
					validationMessages=IdentifiedValidator.addToValidations(validationMessages, message);
				}
			}
		}	
    	return validationMessages;
    }
	
	//@NotNull(message = "Attachment.source cannot be null")
	@NotNull(message = "{ATTACHMENT_SOURCE_NOT_NULL}")
	public URI getSource() throws SBOLGraphException {
		/*if (source==null)
		{
			source=IdentityValidator.getValidator().getPropertyAsURI(this.resource, DataModel.Model.source);	
		}
		return source;*/
		return IdentifiedValidator.getValidator().getPropertyAsURI(this.resource, DataModel.Model.source);
	}

	public void setSource(@NotNull (message="{ATTACHMENT_SOURCE_NOT_NULL}") URI source) throws SBOLGraphException {
		//this.source = source;
		/*try {
	
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		ExecutableValidator executableValidator = factory.getValidator().forExecutables();
		Object[] parameterValues = { source };
		Set<ConstraintViolation<Identified>> violations = executableValidator.validateParameters(
				  this,
				  Attachment.class.getMethod( "setSource", URI.class),
				  parameterValues
				);
			if (violations.size()>0)
			{
				throw new Error ("source is null - gmgm");
			}
		}
		catch (Exception e)
		{
			throw new Error(e);
		}*/
		PropertyValidator.getValidator().validate(this, "setSource", new Object[] {source}, URI.class);
		RDFUtil.setProperty(resource, DataModel.Model.source, source);
	}

	
	public URI getFormat() throws SBOLGraphException {
		/*if (format==null)
		{
			format=IdentityValidator.getValidator().getPropertyAsURI(this.resource, DataModel.Attachment.format);	
		}
		return format;*/
		
		return IdentifiedValidator.getValidator().getPropertyAsURI(this.resource, DataModel.Attachment.format);	
	}

	public void setFormat(URI format) {
		//this.format = format;
		RDFUtil.setProperty(resource, DataModel.Attachment.format, format);
	}
	
	@PositiveOrZero(message="{ATTACHMENT_SIZE_POSITIVE_OR_ZERO}")
	public OptionalLong getSize() throws SBOLGraphException{
		/*if (size.isEmpty())
		{
			String value=IdentityValidator.getValidator().getPropertyAsString(this.resource, DataModel.Attachment.size);
			if (value!=null)
			{
				size=OptionalLong.of(Long.parseLong(value));
			}
		}
		return size;*/
		OptionalLong size=OptionalLong.empty();
		String value=IdentifiedValidator.getValidator().getPropertyAsString(this.resource, DataModel.Attachment.size);
		if (value!=null)
		{
			size= OptionalLong.of(Long.parseLong(value));
		}
		return size;
	}

	public void setSize(@PositiveOrZero(message="{ATTACHMENT_SIZE_POSITIVE_OR_ZERO}") OptionalLong sizeValue) throws SBOLGraphException {
		//this.size = sizeValue;
		String stringValue=null;
		if (sizeValue.isPresent())
		{
			stringValue= String.valueOf(sizeValue.getAsLong());
		}
		PropertyValidator.getValidator().validate(this, "setSize", new Object[] {sizeValue}, OptionalLong.class);
		RDFUtil.setProperty(resource, DataModel.Attachment.size, stringValue);
	}

	public String getHash() throws SBOLGraphException {
		/*if (hash==null)
		{
			hash=IdentityValidator.getValidator().getPropertyAsString(this.resource, DataModel.Attachment.hash);	
		}
		return hash;*/
		return IdentifiedValidator.getValidator().getPropertyAsString(this.resource, DataModel.Attachment.hash);	
	}

	public void setHash(String hash) {
		//this.hash = hash;
		RDFUtil.setProperty(resource, DataModel.Attachment.hash, hash);
	}

	public String getHashAlgorithm() throws SBOLGraphException {
		/*if (hashAlgorithm==null)
		{
			hashAlgorithm=IdentityValidator.getValidator().getPropertyAsString(this.resource, DataModel.Attachment.hashAlgorithm);	
		}
		return hashAlgorithm;*/
		return IdentifiedValidator.getValidator().getPropertyAsString(this.resource, DataModel.Attachment.hashAlgorithm);	
		/*HashAlgorithm alg=null;
		
		String value=IdentifiedValidator.getValidator().getPropertyAsString(this.resource, DataModel.Attachment.hashAlgorithm);	

		if (value!=null){			
			alg=toHashAlgorithm(value); 
			if (Configuration.getInstance().isValidateRecommendedRules()) {
				PropertyValidator.getValidator().validateReturnValue(this, "toHashAlgorithm", alg, String.class);
			}
		}
		return alg;	*/	
	}
	
	/*@NotNull(message = "{ATTACHMENT_ALGORITHM_VALID_IF_NOT_NULL}")   
	public HashAlgorithm toHashAlgorithm (String value)
	{
		return HashAlgorithm.get(value); 
	}*/
	

	public void setHashAlgorithm(HashAlgorithm hashAlgorithm) {
		//this.hashAlgorithm = hashAlgorithm;
		String value=null;
		if (hashAlgorithm!=null)
		{
			value=hashAlgorithm.getValue();
		}
		RDFUtil.setProperty(resource, DataModel.Attachment.hashAlgorithm, value);
	}
	
	@Override
	public URI getResourceType() {
		return DataModel.Attachment.uri;
	}
		   
}