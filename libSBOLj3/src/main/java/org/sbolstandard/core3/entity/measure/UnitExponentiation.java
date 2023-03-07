package org.sbolstandard.core3.entity.measure;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.sbolstandard.core3.entity.SBOLDocument;
import org.sbolstandard.core3.util.RDFUtil;
import org.sbolstandard.core3.util.SBOLGraphException;
import org.sbolstandard.core3.util.SBOLUtil;
import org.sbolstandard.core3.validation.IdentifiedValidator;
import org.sbolstandard.core3.validation.PropertyValidator;
import org.sbolstandard.core3.validation.ValidationMessage;
import org.sbolstandard.core3.vocabulary.DataModel;
import org.sbolstandard.core3.vocabulary.MeasureDataModel;

import jakarta.validation.constraints.NotNull;

public class UnitExponentiation extends CompoundUnit{
	
	/*private URI base;
	private int exponent=Integer.MIN_VALUE;*/
	
	
	protected  UnitExponentiation(Model model,URI uri) throws SBOLGraphException
	{
		super(model, uri);
	}
	
	protected  UnitExponentiation(Resource resource) throws SBOLGraphException
	{
		super(resource);
	}
	
	public static UnitExponentiation create(SBOLDocument sbolDocument, URI uri, URI namespace) throws SBOLGraphException {
		UnitExponentiation identified = new UnitExponentiation(sbolDocument.getRDFModel(), uri);
		identified.setNamespace(namespace);
		return identified;
	}
	
	@NotNull(message = "{UNITEXPONENTIATION_BASE_NOT_NULL}")	
	public Unit getBase() throws SBOLGraphException{
		//return IdentifiedValidator.getValidator().getPropertyAsURI(this.resource, MeasureDataModel.UnitExponentiation.base);
		return contsructIdentified(MeasureDataModel.UnitExponentiation.base, Unit.getSubClassTypes());	
	}

	public void setBase(@NotNull(message = "{UNITEXPONENTIATION_BASE_NOT_NULL}") Unit base) throws SBOLGraphException {
		PropertyValidator.getValidator().validate(this, "setBase", new Object[] {base}, Unit.class);
		RDFUtil.setProperty(resource, MeasureDataModel.UnitExponentiation.base, SBOLUtil.toURI(base));
	}
	
	@NotNull(message = "{UNITEXPONENTIATION_EXPONENT_NOT_EMPTY}")	
	public Optional<@NotNull(message = "{UNITEXPONENTIATION_EXPONENT_NOT_EMPTY}") Integer> getExponent() throws SBOLGraphException {
		return IdentifiedValidator.getValidator().getPropertyAsOptionalInteger(this.resource, MeasureDataModel.UnitExponentiation.exponent);
	}

	public void setExponent(@NotNull(message = "{UNITEXPONENTIATION_EXPONENT_NOT_EMPTY}") Optional<@NotNull(message = "{UNITEXPONENTIATION_EXPONENT_NOT_EMPTY}") Integer> exponent) throws SBOLGraphException {
		PropertyValidator.getValidator().validate(this, "setExponent", new Object[] {exponent}, Optional.class);
		IdentifiedValidator.getValidator().setPropertyAsOptional(this.resource, MeasureDataModel.UnitExponentiation.exponent, exponent);
	}
		
	@Override
	public URI getResourceType() {
		return MeasureDataModel.UnitExponentiation.uri;
	}
	
	@Override
	public List<ValidationMessage> getValidationMessages() throws SBOLGraphException
	{
		List<ValidationMessage> validationMessages=super.getValidationMessages();
		validationMessages= IdentifiedValidator.assertEquals(this, MeasureDataModel.UnitExponentiation.base, this.resource, getBase(), validationMessages);
		return validationMessages;
	}
	
}