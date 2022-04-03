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
import org.sbolstandard.core3.vocabulary.DataModel;

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

	
	public List<URI> getTypes() {
		return RDFUtil.getPropertiesAsURIs(this.resource, DataModel.type);
	}
	
	public void setTypes(List<URI> types) {
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
}