package org.sbolstandard.core3.entity;

import java.net.URI;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.sbolstandard.core3.util.RDFUtil;
import org.sbolstandard.core3.util.SBOLGraphException;
import org.sbolstandard.core3.validation.IdentityValidator;
import org.sbolstandard.core3.vocabulary.DataModel;

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

	public URI getFeature() throws SBOLGraphException {
		return IdentityValidator.getValidator().getPropertyAsURI(this.resource, DataModel.ComponentReference.feature);
	}
	
	public void setFeature(URI feature) {
		RDFUtil.setProperty(this.resource, DataModel.ComponentReference.feature, feature);
	}
	
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