package org.sbolstandard.core3.entity;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.sbolstandard.core3.api.SBOLAPI;
import org.sbolstandard.core3.entity.Location.LocationBuilder;
import org.sbolstandard.core3.entity.Location.LocationFactory;
import org.sbolstandard.core3.util.RDFUtil;
import org.sbolstandard.core3.util.SBOLGraphException;
import org.sbolstandard.core3.validation.IdentifiedValidator;
import org.sbolstandard.core3.validation.PropertyValidator;
import org.sbolstandard.core3.validation.ValidationMessage;
import org.sbolstandard.core3.vocabulary.ComponentType;
import org.sbolstandard.core3.vocabulary.DataModel;
import org.sbolstandard.core3.vocabulary.Encoding;
import org.sbolstandard.core3.vocabulary.RoleIntegration;

import jakarta.validation.ConstraintTarget;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public class SubComponent extends FeatureWithLocation{
	/*private URI roleIntegration=null;
	private URI isInstanceOf=null;
	private List<Location> locations=null;
	private List<Location> sourceLocations=null;*/
	

	protected  SubComponent(Model model,URI uri) throws SBOLGraphException
	{
		super(model, uri);
	}
	
	protected  SubComponent(Resource resource) throws SBOLGraphException
	{
		super(resource);
	}

	@Override
	public List<ValidationMessage> getValidationMessages() throws SBOLGraphException
	{
		List<ValidationMessage> validationMessages=super.getValidationMessages();
		List<URI> roles=this.getRoles();
		if (roles!=null && roles.size()>0)
		{
			RoleIntegration roleIntegration=this.getRoleIntegration();
			if (roleIntegration==null)
			{
				validationMessages= addToValidations(validationMessages,new ValidationMessage("{SUBCOMPONENT_ROLEINTEGRATION_NOT_NULL_IF_ROLES_EXIST}", DataModel.SubComponent.roleIntegration));      	
			}
		}
    	return validationMessages;
	}
	
	public RoleIntegration getRoleIntegration() throws SBOLGraphException {		
		RoleIntegration roleIntegration=null;
		URI value=IdentifiedValidator.getValidator().getPropertyAsURI(this.resource, DataModel.SubComponent.roleIntegration);
		//value=URI.create("http://test.org");
		if (value!=null){
			roleIntegration=toRoleIntegration(value);
			PropertyValidator.getValidator().validateReturnValue(this, "toRoleIntegration", roleIntegration, URI.class);
		}
		return roleIntegration;
	}
	
	@NotNull(message = "{SUBCOMPONENT_ROLEINTEGRATION_VALID_IF_NOT_NULL}")   
	public RoleIntegration toRoleIntegration(URI uri)
	{
		return RoleIntegration.get(uri); 
	}
	
	public void setRoleIntegration(RoleIntegration roleIntegration) {
		URI roleIntegrationURI=null;
		if (roleIntegration!=null)
		{
			roleIntegrationURI=roleIntegration.getUri();
		}
		RDFUtil.setProperty(this.resource, DataModel.SubComponent.roleIntegration, roleIntegrationURI);
	}
	

	@NotNull(message = "{SUBCOMPONENT_ISINSTANCEOF_NOT_NULL}")
	public URI getIsInstanceOf() throws SBOLGraphException {
		return IdentifiedValidator.getValidator().getPropertyAsURI(this.resource, DataModel.SubComponent.instanceOf);
	}

	public void setIsInstanceOf(@NotNull(message = "{SUBCOMPONENT_ISINSTANCEOF_NOT_NULL}") URI isInstanceOf) throws SBOLGraphException {
		PropertyValidator.getValidator().validate(this, "setIsInstanceOf", new Object[] {isInstanceOf}, URI.class);
		RDFUtil.setProperty(this.resource, DataModel.SubComponent.instanceOf, isInstanceOf);	
	}
	
	@Valid
	public List<Location> getLocations() throws SBOLGraphException {		
		return getLocations(DataModel.SubComponent.location);
	}
	
	public List<Cut> getCuts() throws SBOLGraphException {
		return getCuts(DataModel.SubComponent.location);
	}
	
	public List<Range> getRanges() throws SBOLGraphException {
		return getRanges(DataModel.SubComponent.location);
	}

	public List<EntireSequence> getEntireSequences() throws SBOLGraphException {
		return getEntireSequences(DataModel.SubComponent.location);
	}

	public Cut createCut(URI uri,  int at, Sequence sequence) throws SBOLGraphException {
		return createCut(uri, at, sequence, DataModel.SubComponent.location);
	}
	
	public Cut createCut(String displayId, int at, Sequence sequence) throws SBOLGraphException {
		return createCut(displayId, at, sequence, DataModel.SubComponent.location);
	}
	
	public Cut createCut(int at, Sequence sequence) throws SBOLGraphException {
		return createCut(at, sequence,  DataModel.SubComponent.location, merge(getCuts(), getSourceCuts()));
	}
	
	public Range createRange(URI uri, int start, int end, Sequence sequence) throws SBOLGraphException {
		return createRange(uri, start, end, sequence, DataModel.SubComponent.location);
	}
	
	public Range createRange(String displayId, int start, int end, Sequence sequence) throws SBOLGraphException {
		return createRange(displayId, start, end, sequence, DataModel.SubComponent.location);
	}
	
	public Range createRange(int start, int end, Sequence sequence) throws SBOLGraphException {
		return createRange(start, end, sequence, DataModel.SubComponent.location, merge(getRanges(), getSourceRanges()));
	}
	
	public EntireSequence createEntireSequence(URI uri, int start, int end, Sequence sequence) throws SBOLGraphException {
		return createEntireSequence(uri, start, end, sequence, DataModel.SubComponent.location);
	}
	
	public EntireSequence createEntireSequence(String displayId, int start, int end, Sequence sequence) throws SBOLGraphException {
		return createEntireSequence(displayId, start, end, sequence, DataModel.SubComponent.location);	
	}
	
	public EntireSequence createEntireSequence(int start, int end, Sequence sequence) throws SBOLGraphException {
		return createEntireSequence(start, end, sequence, DataModel.SubComponent.location, merge(getEntireSequences(), getSourceEntireSequences()));
	}

	private <T extends Location> List<T> merge(List<T> list1, List<T> list2) throws SBOLGraphException
	{
		List<T> allLocations=new ArrayList<T>();
		if (list1!=null)
		{
			allLocations.addAll(list1);
		}
		if (list2!=null)
		{
			allLocations.addAll(list2);
		}
		return allLocations;
	}
		
	@Valid
	public List<Location> getSourceLocations() throws SBOLGraphException {
		return getLocations(DataModel.SubComponent.sourceLocation);
	}
	
	public List<Cut> getSourceCuts() throws SBOLGraphException {
		return getCuts(DataModel.SubComponent.sourceLocation);
	}
	
	public List<Range> getSourceRanges() throws SBOLGraphException {
		return getRanges(DataModel.SubComponent.sourceLocation);
	}

	public List<EntireSequence> getSourceEntireSequences() throws SBOLGraphException {
		return getEntireSequences(DataModel.SubComponent.sourceLocation);
	}

	public Cut createSourceCut(URI uri,  int at, Sequence sequence) throws SBOLGraphException {
		return createCut(uri, at, sequence, DataModel.SubComponent.sourceLocation);
	}
	
	public Cut createSourceCut(String displayId, int at, Sequence sequence) throws SBOLGraphException {
		return createCut(displayId, at, sequence, DataModel.SubComponent.sourceLocation);
	}
	
	public Cut createSourceCut(int at, Sequence sequence) throws SBOLGraphException {
		return createCut(at, sequence,  DataModel.SubComponent.sourceLocation, merge(getCuts(), getSourceCuts()));
	}
	
	public Range createSourceRange(URI uri, int start, int end, Sequence sequence) throws SBOLGraphException {
		return createRange(uri, start, end, sequence, DataModel.SubComponent.sourceLocation);
	}
	
	public Range createSourceRange(String displayId, int start, int end, Sequence sequence) throws SBOLGraphException {
		return createRange(displayId, start, end, sequence, DataModel.SubComponent.sourceLocation);
	}
	
	public Range createSourceRange(int start, int end, Sequence sequence) throws SBOLGraphException {
		return createRange(start, end, sequence, DataModel.SubComponent.sourceLocation, merge(getRanges(), getSourceRanges()));
	}
	
	public EntireSequence createSourceEntireSequence(URI uri, int start, int end, Sequence sequence) throws SBOLGraphException {
		return createEntireSequence(uri, start, end, sequence, DataModel.SubComponent.sourceLocation);
	}
	
	public EntireSequence createSourceEntireSequence(String displayId, int start, int end, Sequence sequence) throws SBOLGraphException {
		return createEntireSequence(displayId, start, end, sequence, DataModel.SubComponent.sourceLocation);	
	}
	
	public EntireSequence createSourceEntireSequence(int start, int end, Sequence sequence) throws SBOLGraphException {
		return createEntireSequence(start, end, sequence, DataModel.SubComponent.sourceLocation, merge(getEntireSequences(), getSourceEntireSequences()) );
	}

	
	@Override
	public URI getResourceType() {
		return DataModel.SubComponent.uri;
	}
	
	@Override
	public List<Identified> getChildren() throws SBOLGraphException {
		List<Identified> identifieds=super.getChildren();
		identifieds=addToList(identifieds, this.getLocations());
		identifieds=addToList(identifieds, this.getSourceLocations());
		
		return identifieds;
	}
	
}