package org.sbolstandard.core3.entity;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.sbolstandard.core3.entity.Location.LocationBuilder;
import org.sbolstandard.core3.entity.Location.LocationFactory;
import org.sbolstandard.core3.util.RDFUtil;
import org.sbolstandard.core3.util.SBOLGraphException;
import org.sbolstandard.core3.util.SBOLUtil;
import org.sbolstandard.core3.validation.PropertyValidator;
import org.sbolstandard.core3.validation.ValidationMessage;
import org.sbolstandard.core3.vocabulary.ComponentType;
import org.sbolstandard.core3.vocabulary.DataModel;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public class LocalSubComponent extends Feature{
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
	
	public List<Location> getLocations() throws SBOLGraphException {		
		List<Location> locations=null;
		{
			List<Resource> resources=RDFUtil.getResourcesWithProperty (resource, DataModel.SubComponent.location);
			for (Resource res:resources)
			{
				if (locations==null)
				{
					locations=new ArrayList<Location>();
				}
				Location location= LocationFactory.create(res);	
				locations.add(location);			
			}
				
		}
		return locations;
	}

	public Location createLocation(LocationBuilder builder ) throws SBOLGraphException {
		Location location=builder.build(this.resource.getModel(),this.getUri());
		addToList(location, DataModel.SubComponent.location);
		return location;
	}
	
	@Override
	public URI getResourceType() {
		return DataModel.LocalSubComponent.uri;
	}
	
	@Override
	public List<Identified> getChildren() throws SBOLGraphException {
		List<Identified> identifieds=super.getChildren();
		identifieds=addToList(identifieds, this.getLocations());
		return identifieds;
	}
}