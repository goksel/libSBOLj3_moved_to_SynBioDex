package org.sbolstandard.entity;

import java.net.URI;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.sbolstandard.util.RDFUtil;
import org.sbolstandard.util.SBOLGraphException;
import org.sbolstandard.vocabulary.DataModel;

public class Implementation extends TopLevel{
	private URI component=null;
	
	protected  Implementation(Model model,URI uri) throws SBOLGraphException
	{
		super(model, uri);
	}
	
	protected  Implementation(Resource resource)
	{
		super(resource);
	}
	
	public URI getComponent() throws SBOLGraphException {
		if (component==null)
		{
			component=RDFUtil.getPropertyAsURI(this.resource, DataModel.Implementation.built);	
		}
		return component;
	}

	public void setComponent(URI component) {
		this.component = component;
		RDFUtil.setProperty(resource, DataModel.Implementation.built, component);
	}	

	@Override
	public URI getResourceType() {
		return DataModel.Implementation.uri;
	}
	
}