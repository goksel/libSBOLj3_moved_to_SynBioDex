package org.sbolstandard.core3.entity;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.sbolstandard.core3.api.SBOLAPI;
import org.sbolstandard.core3.entity.Location.LocationBuilder;
import org.sbolstandard.core3.entity.Location.LocationFactory;
import org.sbolstandard.core3.util.RDFUtil;
import org.sbolstandard.core3.util.SBOLGraphException;
import org.sbolstandard.core3.util.SBOLUtil;
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
		
		validationMessages= IdentifiedValidator.assertEquals(this, DataModel.SubComponent.instanceOf, this.resource, this.getInstanceOf(), validationMessages);
		validationMessages= IdentifiedValidator.assertExists(this, DataModel.SubComponent.sourceLocation, this.resource, getSourceLocations(), validationMessages);

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
	public Component getInstanceOf() throws SBOLGraphException {
		//return IdentifiedValidator.getValidator().getPropertyAsURI(this.resource, DataModel.SubComponent.instanceOf);
		return contsructIdentified(DataModel.SubComponent.instanceOf, Component.class, DataModel.Component.uri);
	}

	public void setInstanceOf(@NotNull(message = "{SUBCOMPONENT_ISINSTANCEOF_NOT_NULL}") Component isInstanceOf) throws SBOLGraphException {
		PropertyValidator.getValidator().validate(this, "setInstanceOf", new Object[] {isInstanceOf}, Component.class);
		RDFUtil.setProperty(this.resource, DataModel.SubComponent.instanceOf, SBOLUtil.toURI(isInstanceOf));	
	}
	
	
	/*private <T extends Location> List<T> merge2(List<T> list1, List<T> list2) throws SBOLGraphException
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
	}*/
		
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
		return createCut(at, sequence,  DataModel.SubComponent.sourceLocation);
	}
	
	public Range createSourceRange(URI uri, int start, int end, Sequence sequence) throws SBOLGraphException {
		return createRange(uri, start, end, sequence, DataModel.SubComponent.sourceLocation);
	}
	
	public Range createSourceRange(String displayId, int start, int end, Sequence sequence) throws SBOLGraphException {
		return createRange(displayId, start, end, sequence, DataModel.SubComponent.sourceLocation);
	}
	
	public Range createSourceRange(int start, int end, Sequence sequence) throws SBOLGraphException {
		return createRange(start, end, sequence, DataModel.SubComponent.sourceLocation);
	}
	
	public EntireSequence createSourceEntireSequence(URI uri, Sequence sequence) throws SBOLGraphException {
		return createEntireSequence(uri, sequence, DataModel.SubComponent.sourceLocation);
	}
	
	public EntireSequence createSourceEntireSequence(String displayId,Sequence sequence) throws SBOLGraphException {
		return createEntireSequence(displayId, sequence, DataModel.SubComponent.sourceLocation);	
	}
	
	public EntireSequence createSourceEntireSequence(Sequence sequence) throws SBOLGraphException {
		return createEntireSequence(sequence, DataModel.SubComponent.sourceLocation);
	}
	
	@Override
	public URI getResourceType() {
		return DataModel.SubComponent.uri;
	}
	
	@Override
	public URI getDefaultLocationProperty() {
		return DataModel.SubComponent.location;
	}
	
	@Override
	public List<URI> getAdditionalLocationProperties() {
		return new ArrayList<URI>(Arrays.asList(DataModel.SubComponent.sourceLocation));
	}
	
	@Override
	public List<Identified> getChildren() throws SBOLGraphException {
		List<Identified> identifieds=super.getChildren();
		identifieds=addToList(identifieds, this.getLocations());
		identifieds=addToList(identifieds, this.getSourceLocations());
		
		return identifieds;
	}
	
}