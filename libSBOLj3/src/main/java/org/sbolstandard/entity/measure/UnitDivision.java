package org.sbolstandard.entity.measure;

import java.net.URI;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.sbolstandard.util.RDFUtil;
import org.sbolstandard.util.SBOLGraphException;
import org.sbolstandard.vocabulary.MeasureDataModel;

public class UnitDivision extends CompountUnit{
	
	private URI numerator;
	private URI denominator;
	
	
	protected  UnitDivision(Model model,URI uri) throws SBOLGraphException
	{
		super(model, uri);
	}
	
	protected  UnitDivision(Resource resource)
	{
		super(resource);
	}
	
	public URI getNumerator() {
		if (numerator==null)
		{
			numerator=RDFUtil.getPropertyAsURI(this.resource, MeasureDataModel.UnitDivision.numerator);	
		}
		return numerator;
	}

	public void setNumerator(URI numerator) {
		this.numerator = numerator;
		RDFUtil.setProperty(resource, MeasureDataModel.UnitDivision.numerator, numerator);
	}
	
	public URI getDenominator() {
		if (denominator==null)
		{
			denominator=RDFUtil.getPropertyAsURI(this.resource, MeasureDataModel.UnitDivision.denominator);	
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