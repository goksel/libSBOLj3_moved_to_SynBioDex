package org.sbolstandard.core3.entity.provenance;

import java.net.URI;
import java.util.List;
import java.util.Set;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.sbolstandard.core3.entity.ControlledIdentified;
import org.sbolstandard.core3.util.Configuration;
import org.sbolstandard.core3.util.RDFUtil;
import org.sbolstandard.core3.util.SBOLGraphException;
import org.sbolstandard.core3.validation.IdentifiedValidator;
import org.sbolstandard.core3.validation.PropertyValidator;
import org.sbolstandard.core3.validation.ValidationMessage;
import org.sbolstandard.core3.vocabulary.ActivityType;
import org.sbolstandard.core3.vocabulary.ProvenanceDataModel;

import jakarta.validation.constraints.NotNull;

public class Usage extends ControlledIdentified{
	/*private URI entity=null;
	private List<URI> roles=null;*/
		
	protected  Usage(Model model,URI uri) throws SBOLGraphException
	{
		super(model, uri);
	}
	
	protected  Usage(Resource resource) throws SBOLGraphException
	{
		super(resource);
	}
	
	@NotNull(message = "{USAGE_ENTITY_NOT_NULL}")
	public URI getEntity() throws SBOLGraphException {
		return IdentifiedValidator.getValidator().getPropertyAsURI(this.resource, ProvenanceDataModel.Usage.entity);
	}
	
	public void setEntity(@NotNull(message = "{USAGE_ENTITY_NOT_NULL}") URI entity) throws SBOLGraphException {
		PropertyValidator.getValidator().validate(this, "setEntity", new Object[] {entity}, URI.class);
		RDFUtil.setProperty(resource, ProvenanceDataModel.Usage.entity, entity);
	}
	
	public List<URI> getRoles() {
		return RDFUtil.getPropertiesAsURIs(this.resource, ProvenanceDataModel.Usage.role);
	}
	
	public void setRoles(List<URI> roles) {
		RDFUtil.setProperty(resource, ProvenanceDataModel.Usage.role, roles);
	}
	
	public void addRole(URI role) {
		RDFUtil.addProperty(resource, ProvenanceDataModel.Usage.role, role);
	}
	
	@Override
	public URI getResourceType() {
		return ProvenanceDataModel.Usage.uri;
	}			
	
}