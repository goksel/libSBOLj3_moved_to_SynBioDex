package org.sbolstandard.core3.entity;

import java.net.URI;
import java.util.List;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.rdf.model.StmtIterator;
import org.sbolstandard.core3.api.SBOLAPI;
import org.sbolstandard.core3.entity.Location.LocationBuilder;
import org.sbolstandard.core3.entity.Location.LocationFactory;
import org.sbolstandard.core3.util.RDFUtil;
import org.sbolstandard.core3.util.SBOLGraphException;
import org.sbolstandard.core3.vocabulary.DataModel;

public class SequenceFeature extends Feature{
	/*private List<Location> locations=null;*/

	protected  SequenceFeature(Model model,URI uri) throws SBOLGraphException
	{
		super(model, uri);
	}
	
	protected  SequenceFeature(Resource resource) throws SBOLGraphException
	{
		super(resource);
	}

	public List<Location> getLocations() throws SBOLGraphException {
		return addToList(DataModel.SubComponent.location, Location.class, DataModel.Location.uri);
	}

	public Location createLocation(LocationBuilder builder ) throws SBOLGraphException
	{
		URI locationUri=SBOLAPI.createLocalUri(this,builder.getLocationTypeUri(),getLocations(),builder.getLocationClass());
		Location location=builder.build(this.resource.getModel(),locationUri);
		addToList(location, DataModel.SubComponent.location);
		return location;
	}
	
	private Location createLocation2(Location locationData) throws SBOLGraphException
	{
		URI uri=SBOLAPI.append(this.getUri(), locationData.getDisplayId());
		Range location=new Range(this.resource.getModel(), uri);
		
		StmtIterator it=location.resource.listProperties();
		while (it.hasNext())
		{
			Statement stmt=it.next();
			location.resource.addProperty(stmt.getPredicate(), stmt.getObject());
		}
		
		addToList(location, DataModel.SubComponent.location);
		return location;
	}
	
	@Override
	public URI getResourceType() {
		return DataModel.SequenceFeature.uri;
	}
}