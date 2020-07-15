package org.sbolstandard.entity;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.sbolstandard.util.RDFUtil;
import org.sbolstandard.util.SBOLGraphException;
import org.sbolstandard.vocabulary.DataModel;

public class Namespace extends Collection{
	
	protected  Namespace(Model model,URI uri) throws SBOLGraphException
	{
		super(model, uri);
	}
	
	protected  Namespace(Resource resource)
	{
		super(resource);
	}
	
	
	@Override
	public URI getResourceType() {
		return DataModel.Namespace.uri;
	}
	
}