package org.sbolstandard.core3.entity;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.sbolstandard.core3.api.SBOLAPI;
import org.sbolstandard.core3.entity.Location.LocationBuilder;
import org.sbolstandard.core3.entity.Location.LocationFactory;
import org.sbolstandard.core3.util.Configuration;
import org.sbolstandard.core3.util.RDFUtil;
import org.sbolstandard.core3.util.SBOLGraphException;
import org.sbolstandard.core3.util.SBOLUtil;
import org.sbolstandard.core3.validation.PropertyValidator;
import org.sbolstandard.core3.validation.ValidationMessage;
import org.sbolstandard.core3.vocabulary.ComponentType;
import org.sbolstandard.core3.vocabulary.ComponentType.TopologyType;
import org.sbolstandard.core3.vocabulary.DataModel;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public class LocalSubComponent extends FeatureWithLocation{
	/*private List<URI> types=new ArrayList<URI>();
	private List<Location> locations=null;*/

	protected  LocalSubComponent(Model model,URI uri) throws SBOLGraphException
	{
		super(model, uri);
	}
	
	protected  LocalSubComponent(Resource resource) throws SBOLGraphException
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
			validationMessages= addToValidations(validationMessages,new ValidationMessage("{LOCALSUBCOMPONENT_TYPES_INCLUDE_ONE_ROOT_TYPE}", DataModel.type));      	
		}
		
		if (Configuration.getInstance().isValidateRecommendedRules()) {
			if(types != null) {
				// LOCALSUBCOMPONENT_TYPE_FROM_TABLE2
				boolean found=false;
				for(URI typeURI: types) {
					ComponentType recommendType = ComponentType.get(typeURI);
					if (recommendType!=null)
					{
						found=true;
						break;
					}
				}
				if(!found){
					validationMessages= addToValidations(validationMessages,new ValidationMessage("{LOCALSUBCOMPONENT_TYPE_FROM_TABLE2}", DataModel.type, types));      		
				}
				
				int counter=0;
				if (types.contains(ComponentType.DNA.getUrl()) || types.contains(ComponentType.RNA.getUrl()) ){
					for(URI typeURI: types) {
						TopologyType topologyType = TopologyType.get(typeURI);
						if (topologyType!=null)
						{
							counter++;
						}
					}
					if(counter>1){
						validationMessages= addToValidations(validationMessages,new ValidationMessage("{LOCALSUBCOMPONENT_TYPE_AT_MOST_ONE_TOPOLOGY_TYPE}", DataModel.type, types));      		
					}
				}
			}
		}
		
		
		return validationMessages;
	}
	
	@Valid
	@NotEmpty(message = "{LOCALSUBCOMPONENT_TYPES_NOT_EMPTY}")
	public List<URI> getTypes() {
		return RDFUtil.getPropertiesAsURIs(this.resource, DataModel.type);
	}
	
	public void setTypes(@NotEmpty(message = "{LOCALSUBCOMPONENT_TYPES_NOT_EMPTY}") List<URI> types) throws SBOLGraphException {
		PropertyValidator.getValidator().validate(this, "setTypes", new Object[] {types}, List.class);
		RDFUtil.setProperty(resource, DataModel.type, types);
	}
	
	@Override
	public URI getResourceType() {
		return DataModel.LocalSubComponent.uri;
	}
	
	@Override
	public URI getDefaultLocationProperty() {
		return DataModel.LocalSubComponent.location;
	}
	
	@Override
	public List<URI> getAdditionalLocationProperties() {
		return null;
	}
	
	@Override
	public List<Identified> getChildren() throws SBOLGraphException {
		List<Identified> identifieds=super.getChildren();
		identifieds=addToList(identifieds, this.getLocations());
		return identifieds;
	}
}