package org.sbolstandard.entity.provenance;

import java.net.URI;
import java.util.List;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.sbolstandard.entity.Identified;
import org.sbolstandard.util.RDFUtil;
import org.sbolstandard.util.SBOLGraphException;
import org.sbolstandard.vocabulary.ProvenanceDataModel;

public class Usage extends Identified{
	private URI entity=null;
	private List<URI> roles=null;
		
	
	protected  Usage(Model model,URI uri) throws SBOLGraphException
	{
		super(model, uri);
	}
	
	protected  Usage(Resource resource)
	{
		super(resource);
	}
	
	public URI getEntity() {
		if (entity==null)
		{
			entity=RDFUtil.getPropertyAsURI(this.resource, ProvenanceDataModel.Usage.entity);
		}
		return entity;
	}
	
	public void setEntity(URI entity) {
		this.entity = entity;
		RDFUtil.setProperty(resource, ProvenanceDataModel.Usage.entity, entity);
	}
	
	public List<URI> getRoles() {
		if (roles==null)
		{
			roles=RDFUtil.getPropertiesAsURIs(this.resource, ProvenanceDataModel.Usage.role);
		}
		return roles;
	}
	
	public void setRoles(List<URI> roles) {
		this.roles = roles;
		RDFUtil.setProperty(resource, ProvenanceDataModel.Usage.role, roles);
	}
	
	
	@Override
	public URI getResourceType() {
		return ProvenanceDataModel.Usage.uri;
	}
	
}