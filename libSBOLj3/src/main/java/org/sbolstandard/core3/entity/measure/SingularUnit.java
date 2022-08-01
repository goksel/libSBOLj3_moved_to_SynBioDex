package org.sbolstandard.core3.entity.measure;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.sbolstandard.core3.util.RDFUtil;
import org.sbolstandard.core3.util.SBOLGraphException;
import org.sbolstandard.core3.util.SBOLUtil;
import org.sbolstandard.core3.validation.IdentifiedValidator;
import org.sbolstandard.core3.validation.ValidationMessage;
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
	
	public void setFactor(Optional<Float>factor) throws SBOLGraphException {
		IdentifiedValidator.getValidator().setPropertyAsOptional(this.resource, MeasureDataModel.SingularUnit.factor, factor);
	}
	
	public Optional<Float> getFactor() throws SBOLGraphException {
		return IdentifiedValidator.getValidator().getPropertyAsOptionalFloat(this.resource, MeasureDataModel.SingularUnit.factor);
	}
	
	public Unit getUnit() throws SBOLGraphException {
		//return IdentifiedValidator.getValidator().getPropertyAsURI(this.resource, MeasureDataModel.SingularUnit.unit);	
		return contsructIdentified(MeasureDataModel.SingularUnit.unit, Unit.getSubClassTypes());
	}

	public void setUnit(Unit unit) {
		RDFUtil.setProperty(resource, MeasureDataModel.SingularUnit.unit, SBOLUtil.toURI(unit));
	}
	
	@Override
	public URI getResourceType() {
		return MeasureDataModel.SingularUnit.uri;
	}
	
	@Override
	public List<ValidationMessage> getValidationMessages() throws SBOLGraphException
	{
		List<ValidationMessage> validationMessages=super.getValidationMessages();
		validationMessages= IdentifiedValidator.assertEquals(this, MeasureDataModel.SingularUnit.unit, this.resource, getUnit(), validationMessages);
		return validationMessages;
	}
	
}