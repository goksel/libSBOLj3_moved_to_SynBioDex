package org.sbolstandard.core3.entity.provenance;

import java.net.URI;
import java.util.List;
import java.util.Set;

import org.apache.jena.datatypes.xsd.XSDDateTime;
import org.apache.jena.datatypes.xsd.impl.XSDDateType;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.sbolstandard.core3.api.SBOLAPI;
import org.sbolstandard.core3.entity.Component;
import org.sbolstandard.core3.entity.ControlledTopLevel;
import org.sbolstandard.core3.entity.Identified;
import org.sbolstandard.core3.entity.SBOLDocument;
import org.sbolstandard.core3.util.Configuration;
import org.sbolstandard.core3.util.RDFUtil;
import org.sbolstandard.core3.util.SBOLGraphException;
import org.sbolstandard.core3.util.SBOLUtil;
import org.sbolstandard.core3.validation.IdentifiedValidator;
import org.sbolstandard.core3.validation.ValidationMessage;
import org.sbolstandard.core3.vocabulary.ActivityType;
import org.sbolstandard.core3.vocabulary.DataModel;
import org.sbolstandard.core3.vocabulary.ProvenanceDataModel;

import jakarta.validation.Valid;

public class Activity extends ControlledTopLevel{
	/*private XSDDateTime startedAtTime=null;
	private XSDDateTime endedAtTime=null;
	private List<URI> types=null;
	private List<Usage> usages=null;
	private List<Association> associations=null;
	private List<URI> wasInformedBys=null;*/
	
	protected  Activity(Model model,URI uri) throws SBOLGraphException
	{
		super(model, uri);
	}
	
	protected  Activity(Resource resource) throws SBOLGraphException
	{
		super(resource);
	}
	
	public static Activity create(SBOLDocument doc, URI uri, URI namespace) throws SBOLGraphException
	{
		Activity activity = new Activity(doc.getRDFModel(), uri);
		activity.setNamespace(namespace);
		return activity;		
	}
		
	public XSDDateTime getStartedAtTime() throws SBOLGraphException {
		XSDDateTime startedAtTime=null;
		
		String startedAtTimeString=IdentifiedValidator.getValidator().getPropertyAsString(this.resource, ProvenanceDataModel.Activity.startedAtTime);
		if (startedAtTimeString!=null){
			try{
				startedAtTime= (XSDDateTime) XSDDateType.XSDdateTime.parse(startedAtTimeString);
			}
			catch (Exception e){
				throw new SBOLGraphException("Cannot read the datetime value. Property:" + ProvenanceDataModel.Activity.startedAtTime + " Uri:+ " +  this.getUri(), e);
			}
		}
		return startedAtTime;
	}
	
	public void setStartedAtTime(XSDDateTime dateTime) {
		if (dateTime!=null)
		{
			String timestring=dateTime.toString();
			RDFUtil.setProperty(resource, ProvenanceDataModel.Activity.startedAtTime, timestring);
		}
	}
	
	public XSDDateTime getEndedAtTime() throws SBOLGraphException {
		XSDDateTime endedAtTime=null;
		
		String timeString=IdentifiedValidator.getValidator().getPropertyAsString(this.resource, ProvenanceDataModel.Activity.endedAtTime);
		if (timeString!=null){
			try{
				endedAtTime= (XSDDateTime) XSDDateType.XSDdateTime.parse(timeString);
			}
			catch (Exception e){
				throw new SBOLGraphException("Cannot read the datetime value. Property:" + ProvenanceDataModel.Activity.endedAtTime + " Uri:+ " +  this.getUri(), e);
			}
		}
		return endedAtTime;
	}
	
	public void setEndedAtTime(XSDDateTime dateTime) {
		if (dateTime!=null) {
			String timestring=dateTime.toString();
			RDFUtil.setProperty(resource, ProvenanceDataModel.Activity.endedAtTime, timestring);
		}
	}

	public List<URI> getTypes() {
		return RDFUtil.getPropertiesAsURIs(this.resource, ProvenanceDataModel.Activity.type);
	}
	
	public void setTypes(List<URI> types) {
		RDFUtil.setProperty(resource, ProvenanceDataModel.Activity.type, types);
	}
	
	public void addType(URI type) {
		RDFUtil.addProperty(resource, ProvenanceDataModel.Activity.type, type);
	}
	
	public List<Activity> getWasInformedBys() throws SBOLGraphException {
		//return RDFUtil.getPropertiesAsURIs(this.resource, ProvenanceDataModel.Activity.wasInformedBy);
		return addToList(ProvenanceDataModel.Activity.wasInformedBy, Activity.class, ProvenanceDataModel.Activity.uri);	
	}
	
	public void setWasInformedBys(List<Activity> wasInformedBys) {
		RDFUtil.setProperty(resource, ProvenanceDataModel.Activity.wasInformedBy, SBOLUtil.getURIs(wasInformedBys));
	}
	
	@Valid
	public List<Usage> getUsages() throws SBOLGraphException {
		return addToList(ProvenanceDataModel.Activity.qualifiedUsage, Usage.class, ProvenanceDataModel.Usage.uri);
	}
	
	public Usage createUsage(URI uri, URI entity) throws SBOLGraphException
	{
		Usage usage= new Usage(this.resource.getModel(), uri);
		usage.setEntity(entity);
		addToList (usage, ProvenanceDataModel.Activity.qualifiedUsage);
		return usage;	
	}
	
	public Usage createUsage(URI entity) throws SBOLGraphException
	{
		URI childUri=SBOLAPI.createLocalUri(this, ProvenanceDataModel.Usage.uri, this.getUsages());
		return createUsage(childUri, entity);
	}
	
	@Valid
	public List<Association> getAssociations() throws SBOLGraphException {
		return addToList(ProvenanceDataModel.Activity.qualifiedAssociation, Association.class, ProvenanceDataModel.Association.uri);
	}
	
	public Association createAssociation(URI uri, Agent agent) throws SBOLGraphException
	{
		Association association= new Association(this.resource.getModel(), uri);
		association.setAgent(agent);
		addToList (association, ProvenanceDataModel.Activity.qualifiedAssociation);
		return association;	
	}
	
	public Association createAssociation(Agent agent) throws SBOLGraphException
	{
		URI childUri=SBOLAPI.createLocalUri(this, ProvenanceDataModel.Association.uri, this.getAssociations());
		return createAssociation(childUri, agent);	
	}
	
	@Override
	public URI getResourceType() {
		return ProvenanceDataModel.Activity.uri;
	}
	
	@Override
	public List<Identified> getChildren() throws SBOLGraphException {
		List<Identified> identifieds=super.getChildren();
		identifieds=addToList(identifieds, this.getUsages());
		identifieds=addToList(identifieds, this.getAssociations());
		return identifieds;
	}
	
	@Override
	public List<ValidationMessage> getValidationMessages() throws SBOLGraphException
	{
		List<ValidationMessage> validationMessages=super.getValidationMessages();
		
		validationMessages = assertCorrectDBTLTypesForActivityUsages(validationMessages);
		validationMessages = assertCorrectDBTLTypesForActivityAssociations(validationMessages);
		
		validationMessages= IdentifiedValidator.assertExists(this, ProvenanceDataModel.Activity.qualifiedUsage, this.resource, getUsages(), validationMessages);
		validationMessages= IdentifiedValidator.assertExists(this, ProvenanceDataModel.Activity.wasInformedBy, this.resource, getWasInformedBys(), validationMessages);
		validationMessages= IdentifiedValidator.assertExists(this, ProvenanceDataModel.Activity.qualifiedAssociation, this.resource, getAssociations(), validationMessages);
		return validationMessages;
	}
	
	private List<ValidationMessage> assertCorrectDBTLTypesForActivityUsages(List<ValidationMessage> validationMessages) throws SBOLGraphException
	{
		if (Configuration.getInstance().isValidateRecommendedRules()){
			List<URI> activityTypes=this.getTypes();
			
			Set<URI> matches = IdentifiedValidator.getMatchingSearchURIs(activityTypes,ActivityType.getURIs());
			if (matches!=null && matches.size()>0){//DBTL Activity
				List<Usage> usages=this.getUsages();
				if (usages!=null){
					for (Usage usage:usages){
						List<URI> roles=usage.getRoles();
						if (roles!=null){
							for (URI role:roles){
								boolean valid=isValidUsageRole(activityTypes, role);
								if (!valid){
									ValidationMessage message = new ValidationMessage("{ACTIVITY_DBTL_TYPES_VALID_FOR_USAGE_ROLES}", ProvenanceDataModel.Activity.qualifiedUsage, usage, role);
									validationMessages=IdentifiedValidator.addToValidations(validationMessages, message);
						
								}
							}
						}
					}
				}
			}
		}		
		return validationMessages;
	}
	
	private boolean isValidUsageRole(List<URI> activityTypes, URI role)
	{
		ActivityType activityType= ActivityType.get(role);
		boolean validRole=false;
		if (activityType!=null && activityTypes!=null && activityTypes.size()>0){								
			if (activityTypes.contains(activityType.getUri())){
				validRole=true;
			}
			else{				
				for (URI parentActivityTypeURI: activityTypes) {
					ActivityType parentActivityType=ActivityType.get(parentActivityTypeURI);
					if (parentActivityType!=null){
						ActivityType precedingType=ActivityType.getPreceding(parentActivityType);
						if (role.equals(precedingType.getUri())){
							validRole=true;
							break;
						}
					}
				}
			}
		}
		return validRole;
	}
	
	private List<ValidationMessage> assertCorrectDBTLTypesForActivityAssociations(List<ValidationMessage> validationMessages) throws SBOLGraphException
	{
		if (Configuration.getInstance().isValidateRecommendedRules()){
			List<URI> activityTypes=this.getTypes();
			
			Set<URI> matches = IdentifiedValidator.getMatchingSearchURIs(activityTypes,ActivityType.getURIs());
			if (matches!=null && matches.size()>0){//DBTL Activity
				List<Association> associations=this.getAssociations();
				if (associations!=null){
					for (Association association:associations){
						List<URI> roles=association.getRoles();
						if (roles!=null){
							for (URI role:roles){
								boolean valid=isValidAssocationRole(activityTypes, role);
								if (!valid){
									ValidationMessage message = new ValidationMessage("{ACTIVITY_DBTL_TYPES_VALID_FOR_ASSOCIATION_ROLES}", ProvenanceDataModel.Activity.qualifiedAssociation, association, role);
									validationMessages=IdentifiedValidator.addToValidations(validationMessages, message);
						
								}
							}
						}
					}
				}
			}
		}		
		return validationMessages;
	}
	
	private boolean isValidAssocationRole(List<URI> activityTypes, URI role)
	{
		ActivityType activityType= ActivityType.get(role);
		boolean validRole=false;
		if (activityType!=null && activityTypes!=null && activityTypes.size()>0){								
			if (activityTypes.contains(activityType.getUri())){
				validRole=true;
			}			
		}
		return validRole;
	}
	
	 
}