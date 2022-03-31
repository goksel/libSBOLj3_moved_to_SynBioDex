package org.sbolstandard.core3.entity;

import java.net.URI;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.sbolstandard.core3.util.RDFUtil;
import org.sbolstandard.core3.util.SBOLGraphException;
import org.sbolstandard.core3.validation.IdentityValidator;
import org.sbolstandard.core3.vocabulary.DataModel;

public class ComponentReference extends Feature{
	private URI feature=null;
	private URI inChildOf=null;
	

	protected  ComponentReference(Model model,URI uri) throws SBOLGraphException
	{
		super(model, uri);
	}
	
	protected  ComponentReference(Resource resource) throws SBOLGraphException
	{
		super(resource);
	}

	
	public URI getFeature() throws SBOLGraphException {
		if (this.feature==null)
		{
			this.feature=IdentityValidator.getValidator().getPropertyAsURI(this.resource, DataModel.ComponentReference.feature);
		}
		return this.feature;
	}
	
	public void setFeature(URI feature) {
		this.feature = feature;
		RDFUtil.setProperty(this.resource, DataModel.ComponentReference.feature, this.feature);
	}
	
	public URI getInChildOf() throws SBOLGraphException{
		if (this.inChildOf==null)
		{
			this.inChildOf=IdentityValidator.getValidator().getPropertyAsURI(this.resource, DataModel.ComponentReference.inChildOf);
		}
		return this.inChildOf;
	}

	public void setInChildOf(URI inChildOf) {
		this.inChildOf = inChildOf;
		RDFUtil.setProperty(this.resource, DataModel.ComponentReference.inChildOf, this.inChildOf);	
	}
	

	@Override
	public URI getResourceType() {
		return DataModel.ComponentReference.uri;
	}
	
}