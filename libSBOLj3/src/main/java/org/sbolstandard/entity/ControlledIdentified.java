package org.sbolstandard.entity;

import java.net.URI;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.sbolstandard.util.RDFUtil;
import org.sbolstandard.util.SBOLGraphException;
import org.sbolstandard.vocabulary.DataModel;

public abstract class ControlledIdentified extends Identified{
	
	protected  ControlledIdentified(Model model,URI uri) throws SBOLGraphException
	{
		super(model, uri);
		RDFUtil.addType(resource, DataModel.Identified.uri);
	}
	
	protected  ControlledIdentified(Resource resource) throws SBOLGraphException
	{
		super(resource);
	}
	
	
	@Override
	public URI getResourceType() {
		return DataModel.Identified.uri;
	}
	
}