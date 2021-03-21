package org.sbolstandard.entity;

import java.net.URI;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.sbolstandard.util.RDFUtil;
import org.sbolstandard.util.SBOLGraphException;
import org.sbolstandard.vocabulary.DataModel;

public abstract class ControlledTopLevel extends TopLevel{
	
	protected  ControlledTopLevel(Model model,URI uri) throws SBOLGraphException
	{
		super(model, uri);
		RDFUtil.addType(resource, DataModel.TopLevel.uri);
	}
	
	protected  ControlledTopLevel(Resource resource) throws SBOLGraphException
	{
		super(resource);
	}
	
	
	@Override
	public URI getResourceType() {
		return DataModel.TopLevel.uri;
	}
	
}