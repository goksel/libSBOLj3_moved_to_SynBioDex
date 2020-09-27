package org.sbolstandard.entity;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.sbolstandard.entity.Location.LocationBuilder;
import org.sbolstandard.entity.Location.LocationFactory;
import org.sbolstandard.util.RDFUtil;
import org.sbolstandard.util.SBOLGraphException;
import org.sbolstandard.vocabulary.DataModel;

public class LocalSubComponent extends Feature{
	private List<URI> types=new ArrayList<URI>();
	private List<Location> locations=null;

	protected  LocalSubComponent(Model model,URI uri) throws SBOLGraphException
	{
		super(model, uri);
	}
	
	protected  LocalSubComponent(Resource resource)
	{
		super(resource);
	}

	
	public List<URI> getTypes() {
		if (types==null)
		{
			types=RDFUtil.getPropertiesAsURIs(this.resource, DataModel.type);
		}
		return types;
	}
	
	public void setTypes(List<URI> types) {
		this.types = types;
		RDFUtil.setProperty(resource, DataModel.type, types);
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

	public Location createLocation(LocationBuilder builder ) throws SBOLGraphException {
		Location location=builder.build(this.resource.getModel(),this.getUri());
		this.locations=addToList(this.locations, location, DataModel.SubComponent.location);
		return location;
	}
	
	@Override
	public URI getResourceType() {
		return DataModel.LocalSubComponent.uri;
	}
	
	
}