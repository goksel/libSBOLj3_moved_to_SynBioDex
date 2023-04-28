package org.sbolstandard.core3.entity;

import java.net.URI;
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
import org.sbolstandard.core3.vocabulary.DataModel;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;

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
		
		validationMessages=assertDoNotHaveOverlappingRegions(validationMessages, "{LOCALSUBCOMPONENT_LOCATIONS_REGIONS_NOT_OVERLAPPING}");
		
		if (Configuration.getInstance().isValidateRecommendedRules()) {
			if(types != null) {
				
				// LOCALSUBCOMPONENT_TYPE_FROM_TABLE2
				/* Removed this best practise rule
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
				*/
				
				//LOCALSUBCOMPONENT_TYPE_AT_MOST_ONE_TOPOLOGY_TYPE
				validationMessages=IdentifiedValidator.assertAtMostOneTopologyType(types, validationMessages, "{LOCALSUBCOMPONENT_TYPE_AT_MOST_ONE_TOPOLOGY_TYPE}");
				
				//LOCALSUBCOMPONENT_TYPE_ONLY_DNA_OR_RNA_INCLUDE_STRAND_OR_TOPOLOGY
				validationMessages=IdentifiedValidator.assertOnlyDNAOrRNAComponentsIncludeStrandOrTopology(types, validationMessages, "{LOCALSUBCOMPONENT_TYPE_ONLY_DNA_OR_RNA_INCLUDE_STRAND_OR_TOPOLOGY}");
				
				//LOCALCOMPONENT_TYPE_ONLY_DNA_OR_RNA_INCLUDE_SO_FEATURE_ROLE
				validationMessages=IdentifiedValidator.assertOnlyDNAOrRNAIdentifiedsIncludeSOFeatureRole(types, Configuration.getInstance().getSoSequenceFeatures(), this.getRoles(), validationMessages, "{LOCALCOMPONENT_TYPE_ONLY_DNA_OR_RNA_INCLUDE_SO_FEATURE_ROLE}", getResourceType(),this); 
				
				//LOCALCOMPONENT_TYPE_IF_DNA_OR_RNA_SHOULD_INCLUDE_ONE_SO_FEATURE_ROLE
				validationMessages=IdentifiedValidator.assertIfDNAOrRNAThenIdentifiedShouldIncludeOneSOFeatureRole(types, getRoles(), validationMessages, "{LOCALCOMPONENT_TYPE_IF_DNA_OR_RNA_SHOULD_INCLUDE_ONE_SO_FEATURE_ROLE}", this);

				/*boolean checkDNAOrRNA = false;
				for(URI typeURI: types) {
					TopologyType topologyType = TopologyType.get(typeURI);
					if (topologyType!=null)
					{
						checkDNAOrRNA=true;
						break;
					}
					StrandType strandType = StrandType.get(typeURI);
					if (strandType!=null)
					{
						checkDNAOrRNA=true;
						break;
					}
				}
				
				if (checkDNAOrRNA)
				{
					if (!types.contains(ComponentType.DNA.getUri()) && !types.contains(ComponentType.RNA.getUri()) ){
						validationMessages= addToValidations(validationMessages,new ValidationMessage("{LOCALSUBCOMPONENT_TYPE_ONLY_DNA_OR_RNA_INCLUDE_STRAND_OR_TOPOLOGY}", DataModel.type, types));      			
					}
				}*/
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
	
	public void addType(URI type) {
		RDFUtil.addProperty(resource, DataModel.type, type);
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