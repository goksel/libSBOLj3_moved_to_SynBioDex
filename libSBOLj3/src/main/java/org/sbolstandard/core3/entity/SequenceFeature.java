package org.sbolstandard.core3.entity;

import java.net.URI;
import java.util.ArrayList;
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

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

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

	private void addToList(List<Location> listA, List<? extends Location> listB)
	{
		if (listB!=null && listB.size()>0)
		{
			listA.addAll(listB);
		}
	}
	
	@NotEmpty(message = "{SEQUENCEFEATURE_LOCATIONS_NOT_EMPTY}")
	public List<Location> getLocations() throws SBOLGraphException {
		List<Location> locations=new ArrayList<Location>();
		addToList(locations,getCutLocations());
		addToList(locations,this.getRangeLocations());
		addToList(locations,getEntireSequenceLocations());
		return locations;		
	}
	
	@Valid
	public List<Cut> getCutLocations() throws SBOLGraphException {
		return addToList(DataModel.SubComponent.location, Cut.class, DataModel.Cut.uri);
	}
	
	@Valid
	public List<Range> getRangeLocations() throws SBOLGraphException {
		return addToList(DataModel.SubComponent.location, Range.class, DataModel.Range.uri);
	}
	
	@Valid
	public List<EntireSequence> getEntireSequenceLocations() throws SBOLGraphException {
		return addToList(DataModel.SubComponent.location, EntireSequence.class, DataModel.EntireSequenceLocation.uri);
	}

	public Location createLocation(LocationBuilder builder) throws SBOLGraphException
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