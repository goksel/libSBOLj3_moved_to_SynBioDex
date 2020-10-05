package org.sbolstandard.entity;

import java.net.URI;
import java.util.List;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.rdf.model.StmtIterator;
import org.sbolstandard.api.SBOLAPI;
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
		/*this.locations=addToList(this.locations, DataModel.SubComponent.location, Location.class, DataModel.Location.uri);
		return locations;*/
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
		URI locationUri=SBOLAPI.createLocalUri(this,builder.getLocationTypeUri(),getLocations(),builder.getLocationClass());
		Location location=builder.build(this.resource.getModel(),locationUri);
		this.locations=addToList(this.locations, location, DataModel.SubComponent.location);
		return location;
	}
	
	private Location createLocation2(Location locationData) throws SBOLGraphException
	{
		URI uri=SBOLAPI.append(this.getUri(), locationData.getDisplayId());
		RangeLocation location=new RangeLocation(this.resource.getModel(), uri);
		
		StmtIterator it=location.resource.listProperties();
		while (it.hasNext())
		{
			Statement stmt=it.next();
			location.resource.addProperty(stmt.getPredicate(), stmt.getObject());
		}
		
		this.locations=addToList(this.locations, location, DataModel.SubComponent.location);
		return location;
	}
	
	@Override
	public URI getResourceType() {
		return DataModel.SequenceFeature.uri;
	}
}