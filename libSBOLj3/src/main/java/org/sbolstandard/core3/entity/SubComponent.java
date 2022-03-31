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
import org.sbolstandard.core3.validation.IdentityValidator;
import org.sbolstandard.core3.vocabulary.DataModel;

public class SubComponent extends Feature{
	private URI roleIntegration=null;
	private URI isInstanceOf=null;
	private List<Location> locations=null;
	private List<Location> sourceLocations=null;
	

	protected  SubComponent(Model model,URI uri) throws SBOLGraphException
	{
		super(model, uri);
	}
	
	protected  SubComponent(Resource resource) throws SBOLGraphException
	{
		super(resource);
	}

	
	public URI getRoleIntegration() throws SBOLGraphException {
		if (roleIntegration==null)
		{
			roleIntegration=IdentityValidator.getValidator().getPropertyAsURI(this.resource, DataModel.SubComponent.roleIntegration);
		}
		return roleIntegration;
	}
	
	public void setRoleIntegration(URI roleIntegration) {
		this.roleIntegration = roleIntegration;
		RDFUtil.setProperty(this.resource, DataModel.SubComponent.roleIntegration, this.roleIntegration);
	}

	
	public URI getIsInstanceOf() throws SBOLGraphException {
		if (isInstanceOf==null)
		{
			isInstanceOf=IdentityValidator.getValidator().getPropertyAsURI(this.resource, DataModel.SubComponent.instanceOf);
		}
		return isInstanceOf;
	}

	public void setIsInstanceOf(URI isInstanceOf) {
		this.isInstanceOf = isInstanceOf;
		RDFUtil.setProperty(this.resource, DataModel.SubComponent.instanceOf, this.isInstanceOf);	
	}
	

	public List<Location> getLocations() throws SBOLGraphException {
		if (locations==null)
		{
			List<Resource> resources=RDFUtil.getResourcesWithProperty (resource, DataModel.SubComponent.location);
			if (resources!=null)
			{
				for (Resource res:resources)
				{
					Location location= LocationFactory.create(res);	
					locations.add(location);			
				}
			}
		}
		return locations;
	}


	public Location createLocation(LocationBuilder builder ) throws SBOLGraphException
	{
		//URI locationUri=SBOLAPI.createLocalUri(this,DataModel.Location.uri,getLocations());
		List<Location> allLocations=new ArrayList<Location>();
		if (getLocations()!=null)
		{
			allLocations.addAll(getLocations());
		}
		if (sourceLocations!=null)
		{
			allLocations.addAll(getSourceLocations());
		}
		URI locationUri=SBOLAPI.createLocalUri(this,builder.getLocationTypeUri(),allLocations,builder.getLocationClass());
		Location location=builder.build(this.resource.getModel(), locationUri);
		this.locations=addToList(this.locations, location, DataModel.SubComponent.location);
		return location;
	}
	
	
	public List<Location> getSourceLocations() throws SBOLGraphException {
		if (sourceLocations==null)
		{
			List<Resource> resources=RDFUtil.getResourcesWithProperty (resource, DataModel.SubComponent.sourceLocation);
			if (resources!=null)
			{
				for (Resource res:resources)
				{
					Location location= LocationFactory.create(res);	
					sourceLocations.add(location);			
				}
			}
				
		}
		return sourceLocations;
	}


	public Location createSourceLocation(LocationBuilder builder ) throws SBOLGraphException
	{
		//URI locationUri=SBOLAPI.createLocalUri(this,DataModel.Location.uri,getSourceLocations());
		List<Location> allLocations=new ArrayList<Location>();
		if (getLocations()!=null)
		{
			allLocations.addAll(getLocations());
		}
		if (sourceLocations!=null)
		{
			allLocations.addAll(getSourceLocations());
		}
		URI locationUri=SBOLAPI.createLocalUri(this,builder.getLocationTypeUri(),allLocations,builder.getLocationClass());
		
		Location sourceLocation=builder.build(this.resource.getModel(),locationUri);
		RDFUtil.setProperty(resource, DataModel.SubComponent.sourceLocation, sourceLocation.getUri());
		
		if (sourceLocations==null)
		{	
			sourceLocations=new ArrayList<Location>();
			sourceLocations.add(sourceLocation);
		}
		return sourceLocation;
	}
	
	
	@Override
	public URI getResourceType() {
		return DataModel.SubComponent.uri;
	}
	
	
}