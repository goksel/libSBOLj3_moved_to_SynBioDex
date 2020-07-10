package org.sbolstandard.entity;

import java.net.URI;
import java.util.List;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.sbolstandard.entity.Location.LocationBuilder;
import org.sbolstandard.entity.Location.LocationFactory;
import org.sbolstandard.util.RDFUtil;
import org.sbolstandard.util.SBOLGraphException;
import org.sbolstandard.vocabulary.DataModel;

public class SequenceFeature extends Feature{
	private List<Location> locations=null;

	protected  SequenceFeature(Model model,URI uri) throws SBOLGraphException
	{
		super(model, uri);
	}
	
	protected  SequenceFeature(Resource resource)
	{
		super(resource);
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

	public Location createLocation(LocationBuilder builder ) throws SBOLGraphException
	{
		Location location=builder.build(this.resource.getModel());
		this.locations=addToList(this.locations, location, DataModel.SubComponent.location);
		return location;
	}
	
	@Override
	public URI getResourceType() {
		return DataModel.SequenceFeature.uri;
	}
}