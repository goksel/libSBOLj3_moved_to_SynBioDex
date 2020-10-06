package org.sbolstandard.entity;

import java.net.URI;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.sbolstandard.util.SBOLGraphException;
import org.sbolstandard.vocabulary.DataModel;

public class EntireSequenceLocation extends Location {

	

	protected EntireSequenceLocation(Model model, URI uri) throws SBOLGraphException {
		super(model, uri);
	}
	
	protected  EntireSequenceLocation(Resource resource)
	{
		super(resource);
	}
	
	
	public URI getResourceType()
	{
		return DataModel.EntireSequenceLocation.uri;
	}
	
	
}
