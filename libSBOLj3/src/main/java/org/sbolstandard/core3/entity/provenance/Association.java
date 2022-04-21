package org.sbolstandard.core3.entity.provenance;

import java.net.URI;
import java.util.List;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.sbolstandard.core3.entity.ControlledIdentified;
import org.sbolstandard.core3.util.RDFUtil;
import org.sbolstandard.core3.util.SBOLGraphException;
import org.sbolstandard.core3.validation.IdentityValidator;
import org.sbolstandard.core3.validation.PropertyValidator;
import org.sbolstandard.core3.vocabulary.ProvenanceDataModel;

import jakarta.validation.constraints.NotNull;

public class Association extends ControlledIdentified{
	
	/*private List<URI> roles=null;
	private URI plan=null;
	private URI agent=null;*/
	protected  Association(Model model,URI uri) throws SBOLGraphException
	{
		super(model, uri);
	}
	
	protected  Association(Resource resource) throws SBOLGraphException
	{
		super(resource);
	}
	
	public List<URI> getRoles() {
		return RDFUtil.getPropertiesAsURIs(this.resource, ProvenanceDataModel.Association.role);
	}
	
	public void setRoles(List<URI> roles) {
		RDFUtil.setProperty(resource, ProvenanceDataModel.Association.role, roles);
	}
	
	public URI getPlan() throws SBOLGraphException {
		return IdentityValidator.getValidator().getPropertyAsURI(this.resource, ProvenanceDataModel.Association.plan);
	}
	
	public void setPlan(URI plan) {
		RDFUtil.setProperty(resource, ProvenanceDataModel.Association.plan, plan);
	}
	
	@NotNull(message = "{ASSOCIATION_AGENT_NOT_NULL}")
	public URI getAgent() throws SBOLGraphException{
		return IdentityValidator.getValidator().getPropertyAsURI(this.resource, ProvenanceDataModel.Association.agent);
	}
	
	public void setAgent(@NotNull(message = "{ASSOCIATION_AGENT_NOT_NULL}") URI agent) throws SBOLGraphException{
		PropertyValidator.getValidator().validate(this, "setAgent", new Object[] {agent}, URI.class);
		RDFUtil.setProperty(resource, ProvenanceDataModel.Association.agent, agent);
	}
	
	@Override
	public URI getResourceType() {
		return ProvenanceDataModel.Association.uri;
	}
}