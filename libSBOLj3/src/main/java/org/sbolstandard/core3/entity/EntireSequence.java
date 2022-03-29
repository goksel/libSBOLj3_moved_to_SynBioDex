package org.sbolstandard.core3.entity;

import java.net.URI;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.sbolstandard.core3.util.SBOLGraphException;
import org.sbolstandard.core3.vocabulary.DataModel;

public class EntireSequence extends Location {

	

	protected EntireSequence(Model model, URI uri) throws SBOLGraphException {
		super(model, uri);
	}
	
	protected  EntireSequence(Resource resource) throws SBOLGraphException
	{
		super(resource);
	}
	
	
	public URI getResourceType()
	{
		return DataModel.EntireSequenceLocation.uri;
	}
	
	
}
