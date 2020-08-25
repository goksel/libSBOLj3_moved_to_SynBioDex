package org.sbolstandard.entity.measure;

import java.net.URI;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.sbolstandard.util.SBOLGraphException;
import org.sbolstandard.vocabulary.MeasureDataModel;

public abstract class CompountUnit extends Unit{
	
	
	protected  CompountUnit(Model model,URI uri) throws SBOLGraphException
	{
		super(model, uri);
	}
	
	protected  CompountUnit(Resource resource)
	{
		super(resource);
	}
	
	@Override
	public URI getResourceType() {
		return MeasureDataModel.CompoundUnit.uri;
	}
	
}