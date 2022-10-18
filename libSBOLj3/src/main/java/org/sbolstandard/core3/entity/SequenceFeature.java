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
	
	private List<ValidationMessage> assertDoNotHaveOverlappingRegions(List<ValidationMessage>  validationMessages, String message) throws SBOLGraphException
	{
		List<Location> locations = getLocations(); 
		if (locations != null) {
			for (int i = 0; i < locations.size() - 1; i++) {
				Location location = locations.get(i);
				if (location instanceof Range) {
					Range range1 = (Range) location;
					for (int j = i + 1; j < locations.size(); j++) {	
						Location location2 = locations.get(j);
						if (location2 instanceof Range) {
							Range range2 = (Range) location2;
							validationMessages=assertDoNotOverlap(validationMessages, range1, range2, message);
						} 
						else {		
							continue;
						}
					}
					
				} 
				else {		
					continue;
				}
			}
		}
		return  validationMessages;
	}
	
	private List<ValidationMessage> assertDoNotOverlap(List<ValidationMessage>  validationMessages, Range range1, Range range2, String message) throws SBOLGraphException
	{
		if (!range1.getUri().equals(range2.getUri())){
			ValidationMessage validationMessage=null;
			
			int start1=range1.getStart().get();
			int end1=range1.getEnd().get();
			int start2=range2.getStart().get();
			int end2=range2.getEnd().get();
			//10:7-8
			if (existsBetween(end2, start1, end1)) //range2 overlaps with range 1start
			{
				validationMessage=new ValidationMessage(message, DataModel.SequenceFeature.location, range2, end2);
				validationMessage.childPath(DataModel.Range.end);
			}
			else if (existsBetween(end1, start2, end2))
			{
				validationMessage=new ValidationMessage(message, DataModel.SequenceFeature.location, range1, end1);
				validationMessage.childPath(DataModel.Range.end);
			}
			//6:7-8
			else if (existsBetween(start2, start1, end1)) //range2 overlaps with range1 end		
			{
				validationMessage=new ValidationMessage(message, DataModel.SequenceFeature.location, range2, start2);
				validationMessage.childPath(DataModel.Range.start);
			}
			else if (existsBetween(start1, start2, end2)) 	
			{
				validationMessage=new ValidationMessage(message, DataModel.SequenceFeature.location, range1, start1);
				validationMessage.childPath(DataModel.Range.start);
			}
			
			if (validationMessage!=null)
			{
				validationMessages = IdentifiedValidator.addToValidations(validationMessages, validationMessage);
			}
		}
		return validationMessages;	
	}
	
	private boolean existsBetween (int location, int start, int end)
	{
		return (location>=start && location<=end);
	}
	
}