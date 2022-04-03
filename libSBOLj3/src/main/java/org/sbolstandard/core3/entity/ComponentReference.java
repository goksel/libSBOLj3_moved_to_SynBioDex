package org.sbolstandard.core3.entity;

import java.net.URI;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.sbolstandard.core3.util.RDFUtil;
import org.sbolstandard.core3.util.SBOLGraphException;
import org.sbolstandard.core3.validation.IdentityValidator;
import org.sbolstandard.core3.vocabulary.DataModel;

import jakarta.validation.constraints.NotNull;

public class ComponentReference extends Feature{
	/*private URI feature=null;
	private URI inChildOf=null;*/
	
	protected  ComponentReference(Model model,URI uri) throws SBOLGraphException
	{
		super(model, uri);
	}
	
	protected  ComponentReference(Resource resource) throws SBOLGraphException
	{
		super(resource);
	}

	@NotNull(message = "ComponentReference.refersTo cannot be null")
	public URI getRefersTo() throws SBOLGraphException {
		return IdentityValidator.getValidator().getPropertyAsURI(this.resource, DataModel.ComponentReference.refersTo);
	}
	
	public void setRefersTo(URI feature) {
		RDFUtil.setProperty(this.resource, DataModel.ComponentReference.refersTo, feature);
	}
	
	@NotNull(message = "ComponentReference.childOf cannot be null")
	public URI getInChildOf() throws SBOLGraphException{
		return IdentityValidator.getValidator().getPropertyAsURI(this.resource, DataModel.ComponentReference.inChildOf);
	}

	public void setInChildOf(URI inChildOf) {
		RDFUtil.setProperty(this.resource, DataModel.ComponentReference.inChildOf, inChildOf);	
	}
	
	@Override
	public URI getResourceType() {
		return DataModel.ComponentReference.uri;
	}
}