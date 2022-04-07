package org.sbolstandard.core3.entity.measure;

import java.net.URI;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.sbolstandard.core3.util.RDFUtil;
import org.sbolstandard.core3.util.SBOLGraphException;
import org.sbolstandard.core3.validation.IdentityValidator;
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
	
	@NotNull(message = "UnitDivision.numerator cannot be null")	
	public URI getNumerator() throws SBOLGraphException{
		return IdentityValidator.getValidator().getPropertyAsURI(this.resource, MeasureDataModel.UnitDivision.numerator);	
	}

	public void setNumerator(URI numerator) {
		RDFUtil.setProperty(resource, MeasureDataModel.UnitDivision.numerator, numerator);
	}
	
	@NotNull(message = "UnitDivision.denominator cannot be null")	
	public URI getDenominator() throws SBOLGraphException {
		return IdentityValidator.getValidator().getPropertyAsURI(this.resource, MeasureDataModel.UnitDivision.denominator);	
	}

	public void setDenominator(URI denominator) {
		RDFUtil.setProperty(resource, MeasureDataModel.UnitDivision.denominator, denominator);
	}
	
	@Override
	public URI getResourceType() {
		return MeasureDataModel.UnitDivision.uri;
	}	
}