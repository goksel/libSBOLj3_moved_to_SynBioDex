package org.sbolstandard.entity.provenance;

import java.net.URI;
import java.util.List;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.sbolstandard.entity.Identified;
import org.sbolstandard.util.RDFUtil;
import org.sbolstandard.util.SBOLGraphException;
import org.sbolstandard.vocabulary.ProvenanceDataModel;

public class Association extends Identified{
	
	private List<URI> roles=null;
	private URI plan=null;
	private URI agent=null;
	
	
	protected  Association(Model model,URI uri) throws SBOLGraphException
	{
		super(model, uri);
	}
	
	protected  Association(Resource resource)
	{
		super(resource);
	}
	
	public List<URI> getRoles() {
		if (roles==null)
		{
			roles=RDFUtil.getPropertiesAsURIs(this.resource, ProvenanceDataModel.Association.role);
		}
		return roles;
	}
	
	public void setRoles(List<URI> roles) {
		this.roles = roles;
		RDFUtil.setProperty(resource, ProvenanceDataModel.Association.role, roles);
	}
	
	public URI getPlan() {
		if (plan==null)
		{
			plan=RDFUtil.getPropertyAsURI(this.resource, ProvenanceDataModel.Association.plan);
		}
		return plan;
	}
	
	public void setPlan(URI plan) {
		this.plan = plan;
		RDFUtil.setProperty(resource, ProvenanceDataModel.Association.plan, plan);
	}
	
	public URI getAgent() {
		if (agent==null)
		{
			agent=RDFUtil.getPropertyAsURI(this.resource, ProvenanceDataModel.Association.agent);
		}
		return agent;
	}
	
	public void setAgent(URI agent) {
		this.agent = agent;
		RDFUtil.setProperty(resource, ProvenanceDataModel.Association.agent, agent);
	}
	
	
	@Override
	public URI getResourceType() {
		return ProvenanceDataModel.Association.uri;
	}
	
}