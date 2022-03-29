package org.sbolstandard.core3.entity.provenance;

import java.net.URI;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.sbolstandard.core3.entity.ControlledTopLevel;
import org.sbolstandard.core3.util.SBOLGraphException;
import org.sbolstandard.core3.vocabulary.ProvenanceDataModel;

public class Agent extends ControlledTopLevel{
	
	protected  Agent(Model model,URI uri) throws SBOLGraphException
	{
		super(model, uri);
	}
	
	protected  Agent(Resource resource) throws SBOLGraphException
	{
		super(resource);
	}
	
	
	@Override
	public URI getResourceType() {
		return ProvenanceDataModel.Agent.uri;
	}
	
}