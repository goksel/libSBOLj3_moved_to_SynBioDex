package org.sbolstandard.core3.entity;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.sbolstandard.core3.api.SBOLAPI;
import org.sbolstandard.core3.entity.Location.LocationBuilder;
import org.sbolstandard.core3.entity.Location.LocationFactory;
import org.sbolstandard.core3.util.RDFUtil;
import org.sbolstandard.core3.util.SBOLGraphException;
import org.sbolstandard.core3.validation.IdentifiedValidator;
import org.sbolstandard.core3.validation.PropertyValidator;
import org.sbolstandard.core3.validation.ValidationMessage;
import org.sbolstandard.core3.vocabulary.ComponentType;
import org.sbolstandard.core3.vocabulary.DataModel;
import org.sbolstandard.core3.vocabulary.Encoding;
import org.sbolstandard.core3.vocabulary.RoleIntegration;

import jakarta.validation.ConstraintTarget;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public class SubComponent extends Feature{
	/*private URI roleIntegration=null;
	private URI isInstanceOf=null;
	private List<Location> locations=null;
	private List<Location> sourceLocations=null;*/
	

	protected  SubComponent(Model model,URI uri) throws SBOLGraphException
	{
		super(model, uri);
	}
	
	protected  SubComponent(Resource resource) throws SBOLGraphException
	{
		super(resource);
	}

	@Override
	public List<ValidationMessage> getValidationMessages() throws SBOLGraphException
	{
		List<ValidationMessage> validationMessages=super.getValidationMessages();
		List<URI> roles=this.getRoles();
		if (roles!=null && roles.size()>0)
		{
			RoleIntegration roleIntegration=this.getRoleIntegration();
			if (roleIntegration==null)
			{
				validationMessages= addToValidations(validationMessages,new ValidationMessage("{SUBCOMPONENT_ROLEINTEGRATION_NOT_NULL_IF_ROLES_EXIST}", DataModel.SubComponent.roleIntegration));      	
			}
		}
    	return validationMessages;
	}
	
	public RoleIntegration getRoleIntegration() throws SBOLGraphException {		
		RoleIntegration roleIntegration=null;
		URI value=IdentifiedValidator.getValidator().getPropertyAsURI(this.resource, DataModel.SubComponent.roleIntegration);
		//value=URI.create("http://test.org");
		if (value!=null){
			roleIntegration=toRoleIntegration(value);
			PropertyValidator.getValidator().validateReturnValue(this, "toRoleIntegration", roleIntegration, URI.class);
		}
		return roleIntegration;
	}
	
	@NotNull(message = "{SUBCOMPONENT_ROLEINTEGRATION_VALID_IF_NOT_NULL}")   
	public RoleIntegration toRoleIntegration(URI uri)
	{
		return RoleIntegration.get(uri); 
	}
	
	public void setRoleIntegration(RoleIntegration roleIntegration) {
		URI roleIntegrationURI=null;
		if (roleIntegration!=null)
		{
			roleIntegrationURI=roleIntegration.getUri();
		}
		RDFUtil.setProperty(this.resource, DataModel.SubComponent.roleIntegration, roleIntegrationURI);
	}
	

	@NotNull(message = "{SUBCOMPONENT_ISINSTANCEOF_NOT_NULL}")
	public URI getIsInstanceOf() throws SBOLGraphException {
		return IdentifiedValidator.getValidator().getPropertyAsURI(this.resource, DataModel.SubComponent.instanceOf);
	}

	public void setIsInstanceOf(@NotNull(message = "{SUBCOMPONENT_ISINSTANCEOF_NOT_NULL}") URI isInstanceOf) throws SBOLGraphException {
		PropertyValidator.getValidator().validate(this, "setIsInstanceOf", new Object[] {isInstanceOf}, URI.class);
		RDFUtil.setProperty(this.resource, DataModel.SubComponent.instanceOf, isInstanceOf);	
	}
	
	@Valid
	public List<Location> getLocations() throws SBOLGraphException {
		return getLocations(DataModel.SubComponent.location);
	}
	
	private List<Location> getLocations(URI property) throws SBOLGraphException
	{
		List<Location> locations=null;
		List<Resource> resources=RDFUtil.getResourcesWithProperty (resource, property);
		if (resources!=null){
			for (Resource res:resources){
				if (locations==null){
					locations=new ArrayList<Location>();
				}
				Location location= LocationFactory.create(res);	
				locations.add(location);			
			}
		}
		return locations;
	}

	public Location createLocation(LocationBuilder builder) throws SBOLGraphException
	{
		return createLocation(builder, DataModel.SubComponent.location);
	}
	
	private Location createLocation(LocationBuilder builder, URI property) throws SBOLGraphException
	{
		List<Location> allLocations=getAllLocations();
		URI locationUri=SBOLAPI.createLocalUri(this,builder.getLocationTypeUri(),allLocations,builder.getLocationClass());
		Location location=builder.build(this.resource.getModel(), locationUri);
		addToList(location, property);
		return location;
	}
	
	@Valid
	public List<Location> getSourceLocations() throws SBOLGraphException {
		return getLocations(DataModel.SubComponent.sourceLocation);
	}

	private List<Location> getAllLocations() throws SBOLGraphException
	{
		List<Location> allLocations=new ArrayList<Location>();
		List<Location> locations=getLocations();
		List<Location> sourceLocations=getSourceLocations();
		if (locations!=null)
		{
			allLocations.addAll(locations);
		}
		if (sourceLocations!=null)
		{
			allLocations.addAll(sourceLocations);
		}
		return allLocations;
	}

	public Location createSourceLocation(LocationBuilder builder ) throws SBOLGraphException
	{
		return createLocation(builder, DataModel.SubComponent.sourceLocation);
	}
	
	
	@Override
	public URI getResourceType() {
		return DataModel.SubComponent.uri;
	}
	
	@Override
	public List<Identified> getChildren() throws SBOLGraphException {
		List<Identified> identifieds=super.getChildren();
		identifieds=addToList(identifieds, this.getLocations());
		identifieds=addToList(identifieds, this.getSourceLocations());
		
		return identifieds;
	}
	
}