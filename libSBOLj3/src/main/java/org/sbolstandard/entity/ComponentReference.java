package org.sbolstandard.entity;

import java.net.URI;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.sbolstandard.util.RDFUtil;
import org.sbolstandard.util.SBOLGraphException;
import org.sbolstandard.vocabulary.DataModel;

public class ComponentReference extends Feature{
	private URI feature=null;
	private URI inChildOf=null;
	

	protected  ComponentReference(Model model,URI uri) throws SBOLGraphException
	{
		super(model, uri);
	}
	
	protected  ComponentReference(Resource resource)
	{
		super(resource);
	}

	
	public URI getFeature() {
		if (this.feature==null)
		{
			this.feature=RDFUtil.getPropertyAsURI(this.resource, DataModel.ComponentReference.feature);
		}
		return this.feature;
	}
	
	public void setFeature(URI feature) {
		this.feature = feature;
		RDFUtil.setProperty(this.resource, DataModel.ComponentReference.feature, this.feature);
	}

	
	public URI getInChildOf() {
		if (this.inChildOf==null)
		{
			this.inChildOf=RDFUtil.getPropertyAsURI(this.resource, DataModel.ComponentReference.inChildOf);
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