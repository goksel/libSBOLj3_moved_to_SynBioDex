package org.sbolstandard.entity.provenance;

import java.net.URI;
import java.util.List;

import org.apache.jena.datatypes.xsd.XSDDateTime;
import org.apache.jena.datatypes.xsd.impl.XSDDateType;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.sbolstandard.api.SBOLAPI;
import org.sbolstandard.entity.Identified;
import org.sbolstandard.util.RDFUtil;
import org.sbolstandard.util.SBOLGraphException;
import org.sbolstandard.vocabulary.ProvenanceDataModel;

public class Activity extends Identified{
	private XSDDateTime startedAtTime=null;
	private XSDDateTime endedAtTime=null;
	private List<URI> types=null;
	private List<Usage> usages=null;
	private List<Association> associations=null;
	private List<URI> wasInformedBys=null;
	
	protected  Activity(Model model,URI uri) throws SBOLGraphException
	{
		super(model, uri);
	}
	
	protected  Activity(Resource resource)
	{
		super(resource);
	}
	
	public XSDDateTime getStartedAtTime() throws SBOLGraphException {
		if (startedAtTime==null)
		{
			String startedAtTimeString=RDFUtil.getPropertyAsString(this.resource, ProvenanceDataModel.Activity.startedAtTime);
			if (startedAtTimeString!=null)
			{
				try
				{
					startedAtTime= (XSDDateTime) XSDDateType.XSDdateTime.parse(startedAtTimeString);
				}
				catch (Exception e)
				{
					throw new SBOLGraphException("Cannot read the datetime value. Property:" + ProvenanceDataModel.Activity.startedAtTime + " Uri:+ " +  this.getUri(), e);
				}
			}
		}
		return startedAtTime;
	}
	
	public void setStartedAtTime(XSDDateTime dateTime) {
		if (dateTime!=null)
		{
			this.startedAtTime = dateTime;
			String timestring=dateTime.toString();
			RDFUtil.setProperty(resource, ProvenanceDataModel.Activity.startedAtTime, timestring);
		}
	}
	
	
	
	public XSDDateTime getEndedAtTime() throws SBOLGraphException {
		if (endedAtTime==null)
		{
			String timeString=RDFUtil.getPropertyAsString(this.resource, ProvenanceDataModel.Activity.startedAtTime);
			if (timeString!=null)
			{
				try
				{
					endedAtTime= (XSDDateTime) XSDDateType.XSDdateTime.parse(timeString);
				}
				catch (Exception e)
				{
					throw new SBOLGraphException("Cannot read the datetime value. Property:" + ProvenanceDataModel.Activity.endedAtTime + " Uri:+ " +  this.getUri(), e);
				}
			}
		}
		return endedAtTime;
	}
	
	public void setEndedAtTime(XSDDateTime dateTime) {
		if (dateTime!=null) {
			this.endedAtTime = dateTime;
			String timestring=dateTime.toString();
			RDFUtil.setProperty(resource, ProvenanceDataModel.Activity.endedAtTime, timestring);
		}
	}
	
	
	public List<URI> getTypes() {
		if (types==null)
		{
			types=RDFUtil.getPropertiesAsURIs(this.resource, ProvenanceDataModel.Activity.type);
		}
		return types;
	}
	
	public void setTypes(List<URI> types) {
		this.types = types;
		RDFUtil.setProperty(resource, ProvenanceDataModel.Activity.type, types);
	}
	
	public List<URI> getWasInformedBys() {
		if (wasInformedBys==null)
		{
			wasInformedBys=RDFUtil.getPropertiesAsURIs(this.resource, ProvenanceDataModel.Activity.wasInformedBy);
		}
		return wasInformedBys;
	}
	
	public void setWasInformedBys(List<URI> wasInformedBys) {
		this.wasInformedBys = wasInformedBys;
		RDFUtil.setProperty(resource, ProvenanceDataModel.Activity.wasInformedBy, wasInformedBys);
	}
	
	
	public List<Usage> getUsages() throws SBOLGraphException {
		this.usages=addToList(this.usages, ProvenanceDataModel.Activity.qualifiedUsage, Usage.class, ProvenanceDataModel.Usage.uri);
		return this.usages;
	}
	
	public Usage createUsage(URI uri, URI entity) throws SBOLGraphException
	{
		Usage usage= new Usage(this.resource.getModel(), uri);
		usage.setEntity(entity);
		this.usages=addToList (this.usages, usage, ProvenanceDataModel.Activity.qualifiedUsage);
		return usage;	
	}
	
	public Usage createUsage(URI entity) throws SBOLGraphException
	{
		URI childUri=SBOLAPI.createLocalUri(this, ProvenanceDataModel.Usage.uri, this.getUsages());
		return createUsage(childUri, entity);
	}
	
	public List<Association> getAssociations() throws SBOLGraphException {
		this.associations=addToList(this.associations, ProvenanceDataModel.Activity.qualifiedAssociation, Association.class, ProvenanceDataModel.Association.uri);
		return this.associations;
	}
	
	public Association createAssociation(URI uri, URI agent) throws SBOLGraphException
	{
		Association association= new Association(this.resource.getModel(), uri);
		association.setAgent(agent);
		this.associations=addToList (this.associations, association, ProvenanceDataModel.Activity.qualifiedAssociation);
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