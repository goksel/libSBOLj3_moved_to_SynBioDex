package org.sbolstandard.core3.entity.provenance;

import java.net.URI;
import java.util.List;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.sbolstandard.core3.entity.ControlledIdentified;
import org.sbolstandard.core3.util.RDFUtil;
import org.sbolstandard.core3.util.SBOLGraphException;
import org.sbolstandard.core3.util.SBOLUtil;
import org.sbolstandard.core3.validation.IdentifiedValidator;
import org.sbolstandard.core3.validation.PropertyValidator;
import org.sbolstandard.core3.validation.ValidationMessage;
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
	
	public void addRole(URI role) {
		RDFUtil.addProperty(resource, ProvenanceDataModel.Association.role, role);
	}
	
	public Plan getPlan() throws SBOLGraphException {
		//return IdentifiedValidator.getValidator().getPropertyAsURI(this.resource, ProvenanceDataModel.Association.plan);
		return contsructIdentified(ProvenanceDataModel.Association.plan, Plan.class, ProvenanceDataModel.Plan.uri);
	}
	
	public void setPlan(Plan plan) {
		RDFUtil.setProperty(resource, ProvenanceDataModel.Association.plan, SBOLUtil.toURI(plan));
	}
	
	@NotNull(message = "{ASSOCIATION_AGENT_NOT_NULL}")
	public Agent getAgent() throws SBOLGraphException{
		//return IdentifiedValidator.getValidator().getPropertyAsURI(this.resource, ProvenanceDataModel.Association.agent);
		return contsructIdentified(ProvenanceDataModel.Association.agent, Agent.class, ProvenanceDataModel.Agent.uri);

	}
	
	public void setAgent(@NotNull(message = "{ASSOCIATION_AGENT_NOT_NULL}") Agent agent) throws SBOLGraphException{
		PropertyValidator.getValidator().validate(this, "setAgent", new Object[] {agent}, Agent.class);
		RDFUtil.setProperty(resource, ProvenanceDataModel.Association.agent, SBOLUtil.toURI(agent));
	}
	
	@Override
	public URI getResourceType() {
		return ProvenanceDataModel.Association.uri;
	}
	

	@Override
	public List<ValidationMessage> getValidationMessages() throws SBOLGraphException
	{
		List<ValidationMessage> validationMessages=super.getValidationMessages();
		validationMessages= IdentifiedValidator.assertEquals(this, ProvenanceDataModel.Association.agent, this.resource, getAgent(), validationMessages);
		validationMessages= IdentifiedValidator.assertEquals(this, ProvenanceDataModel.Association.plan, this.resource, getPlan(), validationMessages);
		return validationMessages;
	}
}