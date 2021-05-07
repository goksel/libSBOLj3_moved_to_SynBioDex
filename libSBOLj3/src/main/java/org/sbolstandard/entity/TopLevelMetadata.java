package org.sbolstandard.entity;

import java.net.URI;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.sbolstandard.util.RDFUtil;
import org.sbolstandard.util.SBOLGraphException;
import org.sbolstandard.util.SBOLUtil;
import org.sbolstandard.vocabulary.DataModel;

public class TopLevelMetadata extends TopLevel{
	
	protected  TopLevelMetadata(Model model,URI uri) throws SBOLGraphException
	{
		super(model, uri);
	}
	
	protected  TopLevelMetadata(Resource resource) throws SBOLGraphException
	{
		super(resource);
	}
	
	
	@Override
	public URI getResourceType() {
		return DataModel.TopLevel.uri;
	}
	
}