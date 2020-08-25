package org.sbolstandard.entity.measure;

import java.net.URI;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.sbolstandard.util.SBOLGraphException;
import org.sbolstandard.vocabulary.MeasureDataModel;

public class SIPrefix extends Prefix{
	
	protected  SIPrefix(Model model,URI uri) throws SBOLGraphException
	{
		super(model, uri);
	}
	
	protected  SIPrefix(Resource resource)
	{
		super(resource);
	}
	
	
	@Override
	public URI getResourceType() {
		return MeasureDataModel.SIPrefix.uri;
	}
	
}