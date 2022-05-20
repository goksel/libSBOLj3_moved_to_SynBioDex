package org.sbolstandard.core3.entity;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.sbolstandard.core3.api.SBOLAPI;
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

public abstract class FeatureWithLocation extends Feature{
	/*private List<URI> types=new ArrayList<URI>();
	private List<Location> locations=null;*/

	protected  FeatureWithLocation(Model model,URI uri) throws SBOLGraphException
	{
		super(model, uri);
	}
	
	protected  FeatureWithLocation(Resource resource) throws SBOLGraphException
	{
		super(resource);
	}

	
	public List<Location> getLocations(URI property) throws SBOLGraphException {		
		/*List<Location> locations=null;
		{
			List<Resource> resources=RDFUtil.getResourcesWithProperty (resource, DataModel.SubComponent.location);
			if (resources!=null)
			{
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
		}
		return locations;*/
		return addToList(property, Location.getSubClassTypes());
	}
	
	protected List<Cut> getCuts(URI property) throws SBOLGraphException {
		return addToList(property, Cut.class, DataModel.Cut.uri);
	}
	
	protected List<Range> getRanges(URI property) throws SBOLGraphException {
		return addToList(property, Range.class, DataModel.Range.uri);
	}

	protected List<EntireSequence> getEntireSequences(URI property) throws SBOLGraphException {
		return addToList(property, EntireSequence.class, DataModel.EntireSequenceLocation.uri);
	}


	protected Cut createCut(URI uri,  int at, Sequence sequence, URI property) throws SBOLGraphException {
		Cut cut= new Cut(this.resource.getModel(), uri);
		cut.setSequence(sequence);
		cut.setAt(Optional.of(at));
		addToList(cut, property);
		return cut;
	}
	
	protected Cut createCut(String displayId, int at, Sequence sequence, URI property) throws SBOLGraphException {
		return createCut(SBOLAPI.append(this.getUri(), displayId), at, sequence, property);
	}
	
	protected Cut createCut(int at, Sequence sequence, URI property, List<Cut> locations) throws SBOLGraphException {
		String displayId=SBOLAPI.createLocalName(DataModel.Cut.uri, locations);	
		return createCut(displayId, at, sequence, property);
	}
	
	protected Range createRange(URI uri, int start, int end, Sequence sequence, URI property) throws SBOLGraphException {
		Range range= new Range(this.resource.getModel(), uri);
		range.setSequence(sequence);
		range.setStart(Optional.of(start));
		range.setEnd(Optional.of(end));
		addToList(range, property);
		return range;
	}
	
	protected Range createRange(String displayId, int start, int end, Sequence sequence, URI property) throws SBOLGraphException {
		return createRange(SBOLAPI.append(this.getUri(), displayId), start, end,  sequence, property);
	}
	
	protected Range createRange(int start, int end, Sequence sequence, URI property, List<Range> locations) throws SBOLGraphException {
		String displayId=SBOLAPI.createLocalName(DataModel.Range.uri, locations);	
		return createRange(displayId, start, end,  sequence, property);
	}
	
	protected EntireSequence createEntireSequence(URI uri, int start, int end, Sequence sequence, URI property) throws SBOLGraphException {
		EntireSequence entireSequence= new EntireSequence(this.resource.getModel(), uri);
		entireSequence.setSequence(sequence);
		addToList(entireSequence, property);
		return entireSequence;
	}
	
	protected EntireSequence createEntireSequence(String displayId, int start, int end, Sequence sequence, URI property) throws SBOLGraphException {
		return createEntireSequence(SBOLAPI.append(this.getUri(), displayId), start, end,  sequence, property);
	}
	
	protected EntireSequence createEntireSequence(int start, int end, Sequence sequence, URI property,List<EntireSequence> locations) throws SBOLGraphException {
		String displayId=SBOLAPI.createLocalName(DataModel.EntireSequenceLocation.uri, locations);	
		return createEntireSequence(displayId, start, end,  sequence, property);
	}
	
	/*public Location createCutLocation(int at, Sequence sequence) throws SBOLGraphException {
		Location location=builder.build(this.resource.getModel(),this.getUri());
		addToList(location, DataModel.LocalSubComponent.location);
		return location;
	}*/
	
	@Override
	public URI getResourceType() {
		return DataModel.Feature.uri;
	}
	
}