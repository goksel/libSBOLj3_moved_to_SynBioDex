package org.sbolstandard.core3.entity.measure;

import java.net.URI;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.sbolstandard.core3.util.RDFUtil;
import org.sbolstandard.core3.util.SBOLGraphException;
import org.sbolstandard.core3.validation.IdentifiedValidator;
import org.sbolstandard.core3.validation.PropertyValidator;
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
	
	@NotNull(message = "{UNITDIVISION_NUMERATOR_NOT_NULL}")	
	public URI getNumerator() throws SBOLGraphException{
		return IdentifiedValidator.getValidator().getPropertyAsURI(this.resource, MeasureDataModel.UnitDivision.numerator);	
	}

	public void setNumerator(@NotNull(message = "{UNITDIVISION_NUMERATOR_NOT_NULL}") URI numerator) throws SBOLGraphException {
		PropertyValidator.getValidator().validate(this, "setNumerator", new Object[] {numerator}, URI.class);
		RDFUtil.setProperty(resource, MeasureDataModel.UnitDivision.numerator, numerator);
	}
	
	@NotNull(message = "{UNITDIVISION_DENOMINATOR__NOT_NULL}")	
	public URI getDenominator() throws SBOLGraphException {
		return IdentifiedValidator.getValidator().getPropertyAsURI(this.resource, MeasureDataModel.UnitDivision.denominator);	
	}

	public void setDenominator(@NotNull(message = "{UNITDIVISION_DENOMINATOR__NOT_NULL}") URI denominator) throws SBOLGraphException {
		PropertyValidator.getValidator().validate(this, "setDenominator", new Object[] {denominator}, URI.class);
		RDFUtil.setProperty(resource, MeasureDataModel.UnitDivision.denominator, denominator);
	}
	
	@Override
	public URI getResourceType() {
		return MeasureDataModel.UnitDivision.uri;
	}	
}