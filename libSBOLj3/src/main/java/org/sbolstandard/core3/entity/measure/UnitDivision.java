package org.sbolstandard.core3.entity.measure;

import java.net.URI;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.sbolstandard.core3.util.RDFUtil;
import org.sbolstandard.core3.util.SBOLGraphException;
import org.sbolstandard.core3.validation.IdentityValidator;
import org.sbolstandard.core3.vocabulary.MeasureDataModel;

public class UnitDivision extends CompoundUnit{
	
	private URI numerator;
	private URI denominator;
	
	
	protected  UnitDivision(Model model,URI uri) throws SBOLGraphException
	{
		super(model, uri);
	}
	
	protected  UnitDivision(Resource resource) throws SBOLGraphException
	{
		super(resource);
	}
	
	public URI getNumerator() throws SBOLGraphException{
		if (numerator==null)
		{
			numerator=IdentityValidator.getValidator().getPropertyAsURI(this.resource, MeasureDataModel.UnitDivision.numerator);	
		}
		return numerator;
	}

	public void setNumerator(URI numerator) {
		this.numerator = numerator;
		RDFUtil.setProperty(resource, MeasureDataModel.UnitDivision.numerator, numerator);
	}
	
	public URI getDenominator() throws SBOLGraphException {
		if (denominator==null)
		{
			denominator=IdentityValidator.getValidator().getPropertyAsURI(this.resource, MeasureDataModel.UnitDivision.denominator);	
		}
		return denominator;
	}

	public void setDenominator(URI denominator) {
		this.denominator = denominator;
		RDFUtil.setProperty(resource, MeasureDataModel.UnitDivision.denominator, denominator);
	}
	
	
	@Override
	public URI getResourceType() {
		return MeasureDataModel.UnitDivision.uri;
	}
	
}