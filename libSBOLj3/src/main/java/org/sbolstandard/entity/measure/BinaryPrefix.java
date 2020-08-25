package org.sbolstandard.entity.measure;

import java.net.URI;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.sbolstandard.util.SBOLGraphException;
import org.sbolstandard.vocabulary.MeasureDataModel;

public class BinaryPrefix extends Prefix{
	
	protected  BinaryPrefix(Model model,URI uri) throws SBOLGraphException
	{
		super(model, uri);
	}
	
	protected  BinaryPrefix(Resource resource)
	{
		super(resource);
	}
	
	
	@Override
	public URI getResourceType() {
		return MeasureDataModel.BinaryPrefix.uri;
	}
	
}