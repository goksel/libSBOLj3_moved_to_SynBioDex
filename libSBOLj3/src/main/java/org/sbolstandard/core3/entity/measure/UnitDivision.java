package org.sbolstandard.core3.entity.measure;

import java.net.URI;
import java.util.List;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.sbolstandard.core3.entity.SBOLDocument;
import org.sbolstandard.core3.util.RDFUtil;
import org.sbolstandard.core3.util.SBOLGraphException;
import org.sbolstandard.core3.util.SBOLUtil;
import org.sbolstandard.core3.validation.IdentifiedValidator;
import org.sbolstandard.core3.validation.PropertyValidator;
import org.sbolstandard.core3.validation.ValidationMessage;
import org.sbolstandard.core3.vocabulary.MeasureDataModel;

import jakarta.validation.constraints.NotNull;

public class UnitDivision extends CompoundUnit{
	/*private URI numerator;
	private URI denominator;*/
	
	protected  UnitDivision(Model model,URI uri) throws SBOLGraphException
	{
		super(model, uri);
	}
	
	protected  UnitDivision(Resource resource) throws SBOLGraphException
	{
		super(resource);
	}
	
	public static UnitDivision create(SBOLDocument sbolDocument, URI uri, URI namespace) throws SBOLGraphException {
		UnitDivision identified = new UnitDivision(sbolDocument.getRDFModel(), uri);
		identified.setNamespace(namespace);
		return identified;
	}
	
	@NotNull(message = "{UNITDIVISION_NUMERATOR_NOT_NULL}")	
	public Unit getNumerator() throws SBOLGraphException{
		//return IdentifiedValidator.getValidator().getPropertyAsURI(this.resource, MeasureDataModel.UnitDivision.numerator);	
		return contsructIdentified(MeasureDataModel.UnitDivision.numerator, Unit.getSubClassTypes());	
	}

	public void setNumerator(@NotNull(message = "{UNITDIVISION_NUMERATOR_NOT_NULL}") Unit numerator) throws SBOLGraphException {
		PropertyValidator.getValidator().validate(this, "setNumerator", new Object[] {numerator}, Unit.class);
		RDFUtil.setProperty(resource, MeasureDataModel.UnitDivision.numerator, SBOLUtil.toURI(numerator));
	}
	
	@NotNull(message = "{UNITDIVISION_DENOMINATOR__NOT_NULL}")	
	public Unit getDenominator() throws SBOLGraphException {
		//return IdentifiedValidator.getValidator().getPropertyAsURI(this.resource, MeasureDataModel.UnitDivision.denominator);	
		return contsructIdentified(MeasureDataModel.UnitDivision.denominator, Unit.getSubClassTypes());	
	}

	public void setDenominator(@NotNull(message = "{UNITDIVISION_DENOMINATOR__NOT_NULL}") Unit denominator) throws SBOLGraphException {
		PropertyValidator.getValidator().validate(this, "setDenominator", new Object[] {denominator}, Unit.class);
		RDFUtil.setProperty(resource, MeasureDataModel.UnitDivision.denominator, SBOLUtil.toURI(denominator));
	}
	
	@Override
	public URI getResourceType() {
		return MeasureDataModel.UnitDivision.uri;
	}	
	
	@Override
	public List<ValidationMessage> getValidationMessages() throws SBOLGraphException
	{
		List<ValidationMessage> validationMessages=super.getValidationMessages();
		validationMessages= IdentifiedValidator.assertEquals(this, MeasureDataModel.UnitDivision.denominator, this.resource, getDenominator(), validationMessages);
		validationMessages= IdentifiedValidator.assertEquals(this, MeasureDataModel.UnitDivision.numerator, this.resource, getNumerator(), validationMessages);
		return validationMessages;
	}
}