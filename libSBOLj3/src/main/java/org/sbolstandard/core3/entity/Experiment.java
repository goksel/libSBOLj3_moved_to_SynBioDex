package org.sbolstandard.core3.entity;

import java.net.URI;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.sbolstandard.core3.util.SBOLGraphException;
import org.sbolstandard.core3.vocabulary.DataModel;

public class Experiment extends Collection{
	
	protected  Experiment(Model model,URI uri) throws SBOLGraphException
	{
		super(model, uri);
	}
	
	protected  Experiment(Resource resource) throws SBOLGraphException
	{
		super(resource);
	}
	
	@Override
	public URI getResourceType() {
		return DataModel.Experiment.uri;
	}
}