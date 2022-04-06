package org.sbolstandard.core3.entity.measure;

import java.net.URI;
import java.util.Optional;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.sbolstandard.core3.util.RDFUtil;
import org.sbolstandard.core3.util.SBOLGraphException;
import org.sbolstandard.core3.validation.IdentityValidator;
import org.sbolstandard.core3.vocabulary.MeasureDataModel;

public class SingularUnit extends Unit{
	
	//private float factor=Float.NaN;
	//private URI unit;
	
	protected  SingularUnit(Model model,URI uri) throws SBOLGraphException
	{
		super(model, uri);
	}
	
	protected  SingularUnit(Resource resource) throws SBOLGraphException
	{
		super(resource);
	}
	
	public void setFactor(Optional<Float>factor) {
		IdentityValidator.getValidator().setPropertyAsOptionalFloat(this.resource, MeasureDataModel.SingularUnit.factor, factor);
	}
	
	public Optional<Float> getFactor() throws SBOLGraphException {
		return IdentityValidator.getValidator().getPropertyAsOptionalFloat(this.resource, MeasureDataModel.SingularUnit.factor);
	}
	
	public URI getUnit() throws SBOLGraphException {
		return IdentityValidator.getValidator().getPropertyAsURI(this.resource, MeasureDataModel.SingularUnit.unit);	
	}

	public void setUnit(URI unit) {
		RDFUtil.setProperty(resource, MeasureDataModel.SingularUnit.unit, unit);
	}
	
	@Override
	public URI getResourceType() {
		return MeasureDataModel.SingularUnit.uri;
	}
}