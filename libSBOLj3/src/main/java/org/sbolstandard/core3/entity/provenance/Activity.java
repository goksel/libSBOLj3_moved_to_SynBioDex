package org.sbolstandard.core3.entity.provenance;

import java.net.URI;
import java.util.List;

import org.apache.jena.datatypes.xsd.XSDDateTime;
import org.apache.jena.datatypes.xsd.impl.XSDDateType;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.sbolstandard.core3.api.SBOLAPI;
import org.sbolstandard.core3.entity.ControlledTopLevel;
import org.sbolstandard.core3.util.RDFUtil;
import org.sbolstandard.core3.util.SBOLGraphException;
import org.sbolstandard.core3.validation.IdentityValidator;
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
	
	public XSDDateTime getStartedAtTime() throws SBOLGraphException {
		XSDDateTime startedAtTime=null;
		
		String startedAtTimeString=IdentityValidator.getValidator().getPropertyAsString(this.resource, ProvenanceDataModel.Activity.startedAtTime);
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
		
		String timeString=IdentityValidator.getValidator().getPropertyAsString(this.resource, ProvenanceDataModel.Activity.endedAtTime);
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
	
	public List<URI> getWasInformedBys() {
		return RDFUtil.getPropertiesAsURIs(this.resource, ProvenanceDataModel.Activity.wasInformedBy);
	}
	
	public void setWasInformedBys(List<URI> wasInformedBys) {
		RDFUtil.setProperty(resource, ProvenanceDataModel.Activity.wasInformedBy, wasInformedBys);
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
	
	public Association createAssociation(URI uri, URI agent) throws SBOLGraphException
	{
		Association association= new Association(this.resource.getModel(), uri);
		association.setAgent(agent);
		addToList (association, ProvenanceDataModel.Activity.qualifiedAssociation);
		return association;	
	}
	
	public Association createAssociation(URI agent) throws SBOLGraphException
	{
		URI childUri=SBOLAPI.createLocalUri(this, ProvenanceDataModel.Association.uri, this.getAssociations());
		return createAssociation(childUri, agent);	
	}
	
	@Override
	public URI getResourceType() {
		return ProvenanceDataModel.Activity.uri;
	}
}