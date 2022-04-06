package org.sbolstandard.core3.entity.measure;

import java.net.URI;
import java.util.Optional;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.sbolstandard.core3.util.SBOLGraphException;
import org.sbolstandard.core3.validation.IdentityValidator;
import org.sbolstandard.core3.vocabulary.MeasureDataModel;

public abstract class Prefix extends Unit{
	
	//private float factor=Float.NaN;
	protected  Prefix(Model model,URI uri) throws SBOLGraphException
	{
		super(model, uri);
	}
	
	protected  Prefix(Resource resource) throws SBOLGraphException
	{
		super(resource);
	}
	
	public void setFactor(Optional<Float> factor) {
		IdentityValidator.getValidator().setPropertyAsOptionalFloat(this.resource, MeasureDataModel.Prefix.factor, factor);		
	}
	
	public Optional<Float> getFactor() throws SBOLGraphException {
		return IdentityValidator.getValidator().getPropertyAsOptionalFloat(this.resource, MeasureDataModel.Prefix.factor);
	}

	@Override
	public URI getResourceType() {
		return MeasureDataModel.Prefix.uri;
	}	
}