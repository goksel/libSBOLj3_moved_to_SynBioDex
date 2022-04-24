package org.sbolstandard.core3.entity.measure;

import java.net.URI;
import java.util.Optional;
import java.util.OptionalInt;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.sbolstandard.core3.util.RDFUtil;
import org.sbolstandard.core3.util.SBOLGraphException;
import org.sbolstandard.core3.validation.IdentityValidator;
import org.sbolstandard.core3.validation.PropertyValidator;
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
	
	@NotNull(message = "{UNITEXPONENTIATION_BASE_NOT_NULL}")	
	public URI getBase() throws SBOLGraphException{
		return IdentityValidator.getValidator().getPropertyAsURI(this.resource, MeasureDataModel.UnitExponentiation.base);
	}

	public void setBase(@NotNull(message = "{UNITEXPONENTIATION_BASE_NOT_NULL}") URI base) throws SBOLGraphException {
		PropertyValidator.getValidator().validate(this, "setBase", new Object[] {base}, URI.class);
		RDFUtil.setProperty(resource, MeasureDataModel.UnitExponentiation.base, base);
	}
	
	@NotNull(message = "{UNITEXPONENTIATION_EXPONENT_NOT_EMPTY}")	
	public Optional<@NotNull(message = "{UNITEXPONENTIATION_EXPONENT_NOT_EMPTY}") Integer> getExponent() throws SBOLGraphException {
		return IdentityValidator.getValidator().getPropertyAsOptionalInteger(this.resource, MeasureDataModel.UnitExponentiation.exponent);
	}

	public void setExponent(@NotNull(message = "{UNITEXPONENTIATION_EXPONENT_NOT_EMPTY}") Optional<@NotNull(message = "{UNITEXPONENTIATION_EXPONENT_NOT_EMPTY}") Integer> exponent) throws SBOLGraphException {
		PropertyValidator.getValidator().validate(this, "setExponent", new Object[] {exponent}, Optional.class);
		IdentityValidator.getValidator().setPropertyAsOptional(this.resource, MeasureDataModel.UnitExponentiation.exponent, exponent);
	}
		
	@Override
	public URI getResourceType() {
		return MeasureDataModel.UnitExponentiation.uri;
	}
	
}