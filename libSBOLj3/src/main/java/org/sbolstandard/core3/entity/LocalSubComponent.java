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

public class LocalSubComponent extends FeatureWithLocation{
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

	@Override
	public List<ValidationMessage> getValidationMessages() throws SBOLGraphException
	{
		List<ValidationMessage> validationMessages=super.getValidationMessages();
		List<URI> types=this.getTypes();
		if (SBOLUtil.includesMultipleRootComponentTypes(types))
		{
			validationMessages= addToValidations(validationMessages,new ValidationMessage("{LOCALSUBCOMPONENT_TYPES_INCLUDE_ONE_ROOT_TYPE}", DataModel.type));      	
		}
		return validationMessages;
	}
	
	@Valid
	@NotEmpty(message = "{LOCALSUBCOMPONENT_TYPES_NOT_EMPTY}")
	public List<URI> getTypes() {
		return RDFUtil.getPropertiesAsURIs(this.resource, DataModel.type);
	}
	
	public void setTypes(@NotEmpty(message = "{LOCALSUBCOMPONENT_TYPES_NOT_EMPTY}") List<URI> types) throws SBOLGraphException {
		PropertyValidator.getValidator().validate(this, "setTypes", new Object[] {types}, List.class);
		RDFUtil.setProperty(resource, DataModel.type, types);
	}
	
	@Valid
	public List<Location> getLocations() throws SBOLGraphException {		
		return getLocations(DataModel.LocalSubComponent.location);
	}
	
	public List<Cut> getCuts() throws SBOLGraphException {
		return getCuts(DataModel.LocalSubComponent.location);
	}
	
	public List<Range> getRanges() throws SBOLGraphException {
		return getRanges(DataModel.LocalSubComponent.location);
	}

	public List<EntireSequence> getEntireSequences() throws SBOLGraphException {
		return getEntireSequences(DataModel.LocalSubComponent.location);
	}

	public Cut createCut(URI uri,  int at, Sequence sequence) throws SBOLGraphException {
		return createCut(uri, at, sequence, DataModel.LocalSubComponent.location);
	}
	
	public Cut createCut(String displayId, int at, Sequence sequence) throws SBOLGraphException {
		return createCut(displayId, at, sequence, DataModel.LocalSubComponent.location);
	}
	
	public Cut createCut(int at, Sequence sequence) throws SBOLGraphException {
		return createCut(at, sequence,  DataModel.LocalSubComponent.location,getCuts());
	}
	
	public Range createRange(URI uri, int start, int end, Sequence sequence) throws SBOLGraphException {
		return createRange(uri, start, end, sequence, DataModel.LocalSubComponent.location);
	}
	
	public Range createRange(String displayId, int start, int end, Sequence sequence) throws SBOLGraphException {
		return createRange(displayId, start, end, sequence, DataModel.LocalSubComponent.location);
	}
	
	public Range createRange(int start, int end, Sequence sequence) throws SBOLGraphException {
		return createRange(start, end, sequence, DataModel.LocalSubComponent.location, getRanges());
	}
	
	public EntireSequence createEntireSequence(URI uri, int start, int end, Sequence sequence) throws SBOLGraphException {
		return createEntireSequence(uri, start, end, sequence, DataModel.LocalSubComponent.location);
	}
	
	public EntireSequence createEntireSequence(String displayId, int start, int end, Sequence sequence) throws SBOLGraphException {
		return createEntireSequence(displayId, start, end, sequence, DataModel.LocalSubComponent.location);	
	}
	
	public EntireSequence createEntireSequence(int start, int end, Sequence sequence) throws SBOLGraphException {
		return createEntireSequence(start, end, sequence, DataModel.LocalSubComponent.location, getEntireSequences());
	}

	@Override
	public URI getResourceType() {
		return DataModel.LocalSubComponent.uri;
	}
	
	@Override
	public List<Identified> getChildren() throws SBOLGraphException {
		List<Identified> identifieds=super.getChildren();
		identifieds=addToList(identifieds, this.getLocations());
		return identifieds;
	}
}