package org.sbolstandard.core3.entity;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.sbolstandard.core3.util.Configuration;
import org.sbolstandard.core3.util.RDFUtil;
import org.sbolstandard.core3.util.SBOLGraphException;
import org.sbolstandard.core3.util.SBOLUtil;
import org.sbolstandard.core3.validation.IdentifiedValidator;
import org.sbolstandard.core3.validation.PropertyValidator;
import org.sbolstandard.core3.validation.ValidationMessage;
import org.sbolstandard.core3.vocabulary.ComponentType;
import org.sbolstandard.core3.vocabulary.DataModel;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public class ExternallyDefined extends Feature{
	/*private List<URI> types=new ArrayList<URI>();
	private URI definition=null;*/

	protected  ExternallyDefined(Model model,URI uri) throws SBOLGraphException
	{
		super(model, uri);
	}
	
	protected  ExternallyDefined(Resource resource) throws SBOLGraphException
	{
		super(resource);
	}
	
	@Override
	public List<ValidationMessage> getValidationMessages() throws SBOLGraphException
	{
		List<ValidationMessage> validationMessages=super.getValidationMessages();
		List<URI> types=this.getTypes();
		if (SBOLUtil.includesMultipleRootComponentTypes(types))
		{
			validationMessages= addToValidations(validationMessages,new ValidationMessage("{EXTERNALLYDEFINED_TYPES_INCLUDE_ONE_ROOT_TYPE}", DataModel.type));      	
		}
		
		/*Removed this best practise rule
		 * if (Configuration.getInstance().isValidateRecommendedRules()){
			boolean foundType = false;
			if(types!=null) {
				for(URI type: types) {
					ComponentType recommendType = ComponentType.get(type);
					if(recommendType != null){
						foundType = true;
					}
				}
			}
			if(!foundType) {
				validationMessages = addToValidations(validationMessages,new ValidationMessage("{EXTERNALLYDEFINED_TYPE_IN_TABLE2}", DataModel.type));      	
			}
		}*/
		
		return validationMessages;
	}

	@Valid
	@NotEmpty(message = "{EXTERNALLYDEFINED_TYPES_NOT_EMPTY}")
	public List<URI> getTypes() {
		return RDFUtil.getPropertiesAsURIs(this.resource, DataModel.type);
	}
	
	public void setTypes(@NotEmpty(message = "{EXTERNALLYDEFINED_TYPES_NOT_EMPTY}") List<URI> types) throws SBOLGraphException {
		PropertyValidator.getValidator().validate(this, "setTypes", new Object[] {types}, List.class);
		RDFUtil.setProperty(resource, DataModel.type, types);
	}
	
	@NotNull(message = "{EXTERNALLYDEFINED_DEFINITION_NOT_NULL}")
	public URI getDefinition() throws SBOLGraphException {
		return IdentifiedValidator.getValidator().getPropertyAsURI(this.resource, DataModel.ExternalyDefined.definition);
	}

	public void setDefinition(@NotNull(message = "{EXTERNALLYDEFINED_DEFINITION_NOT_NULL}") URI definition) throws SBOLGraphException {
		PropertyValidator.getValidator().validate(this, "setDefinition", new Object[] {definition}, URI.class);
		RDFUtil.setProperty(this.resource, DataModel.ExternalyDefined.definition, definition);	
	}
	
	@Override
	public URI getResourceType() {
		return DataModel.ExternalyDefined.uri;
	}
	
}