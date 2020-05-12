package org.sbolstandard;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.sbolstandard.Location.LocationBuilder;
import org.sbolstandard.Location.LocationFactory;
import org.sbolstandard.util.RDFUtil;
import org.sbolstandard.vocabulary.DataModel;

public class SubComponent extends Feature{
	private URI roleIntegration=null;
	private URI isInstanceOf=null;
	private List<Location> locations=null;
	private List<Location> sourceLocations=null;
	

	protected  SubComponent(Model model,URI uri)
	{
		super(model, uri);
	}
	
	protected  SubComponent(Resource resource)
	{
		super(resource);
	}

	
	public URI getRoleIntegration() {
		if (roleIntegration==null)
		{
			roleIntegration=RDFUtil.getPropertyAsURI(this.resource, DataModel.SubComponent.roleIntegration);
		}
		return roleIntegration;
	}
	
	public void setRoleIntegration(URI roleIntegration) {
		this.roleIntegration = roleIntegration;
		RDFUtil.setProperty(this.resource, DataModel.SubComponent.roleIntegration, this.roleIntegration);
	}

	
	public URI getIsInstanceOf() {
		if (isInstanceOf==null)
		{
			isInstanceOf=RDFUtil.getPropertyAsURI(this.resource, DataModel.SubComponent.instanceOf);
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
			for (Resource res:resources)
			{
				Location location= LocationFactory.create(res);	
				locations.add(location);			
			}
				
		}
		return locations;
	}


	public Location createLocation(LocationBuilder builder ) {
		Location location=builder.build(this.resource.getModel());
		this.locations=addToList(this.locations, location, DataModel.SubComponent.location);
		return location;
	}
	
	
	public List<Location> getSourceLocations() throws SBOLGraphException {
		if (sourceLocations==null)
		{
			List<Resource> resources=RDFUtil.getResourcesWithProperty (resource, DataModel.SubComponent.sourceLocation);
			for (Resource res:resources)
			{
				Location location= LocationFactory.create(res);	
				sourceLocations.add(location);			
			}
				
		}
		return sourceLocations;
	}


	public Location createSourceLocation(LocationBuilder builder ) {
		Location sourceLocation=builder.build(this.resource.getModel());
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