package org.sbolstandard.core3.entity;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.rdf.model.StmtIterator;
import org.sbolstandard.core3.api.SBOLAPI;
import org.sbolstandard.core3.entity.Location.LocationBuilder;
import org.sbolstandard.core3.entity.Location.LocationFactory;
import org.sbolstandard.core3.util.RDFUtil;
import org.sbolstandard.core3.util.SBOLGraphException;
import org.sbolstandard.core3.validation.IdentifiedValidator;
import org.sbolstandard.core3.validation.ValidationMessage;
import org.sbolstandard.core3.vocabulary.DataModel;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public class SequenceFeature extends FeatureWithLocation{
	/*private List<Location> locations=null;*/

	protected  SequenceFeature(Model model,URI uri) throws SBOLGraphException
	{
		super(model, uri);
	}
	
	protected  SequenceFeature(Resource resource) throws SBOLGraphException
	{
		super(resource);
	}

	/*private void addToLocationList(List<Location> listA, List<? extends Location> listB)
	{
		if (listB!=null && listB.size()>0)
		{
			listA.addAll(listB);
		}
	}*/
	
	@Override
	@NotEmpty(message = "{SEQUENCEFEATURE_LOCATIONS_NOT_EMPTY}")
	public List<Location> getLocations() throws SBOLGraphException {
		return super.getLocations();
	}

	/*public Location createLocation(LocationBuilder builder) throws SBOLGraphException
	{
		URI locationUri=SBOLAPI.createLocalUri(this,builder.getLocationTypeUri(),getLocations(),builder.getLocationClass());
		Location location=builder.build(this.resource.getModel(),locationUri);
		addToList(location, DataModel.SubComponent.location);
		return location;
	}*/
	
	/*private Location createLocation2(Location locationData) throws SBOLGraphException
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
	}*/
	
	@Override
	public URI getResourceType() {
		return DataModel.SequenceFeature.uri;
	}
	
	@Override
	public URI getDefaultLocationProperty() {
		return DataModel.SequenceFeature.location;
	}
	
	@Override
	public List<URI> getAdditionalLocationProperties() {
		return null;
	}
	
	
	@Override
	public List<Identified> getChildren() throws SBOLGraphException {
		List<Identified> identifieds=super.getChildren();
		identifieds=addToList(identifieds, this.getLocations());
		return identifieds;
	}
	
	@Override
	public List<ValidationMessage> getValidationMessages() throws SBOLGraphException {
		List<ValidationMessage> validationMessages = super.getValidationMessages();
		validationMessages=assertDoNotHaveOverlappingRegions(validationMessages, "{SEQUENCEFEATURE_LOCATIONS_REGIONS_NOT_OVERLAPPING}");
		return validationMessages;
	}
}