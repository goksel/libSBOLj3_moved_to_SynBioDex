package org.sbolstandard.entity.measure;

import java.net.URI;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.sbolstandard.util.RDFUtil;
import org.sbolstandard.util.SBOLGraphException;
import org.sbolstandard.vocabulary.MeasureDataModel;

public class UnitExponentiation extends CompountUnit{
	
	private URI base;
	private int exponent=Integer.MIN_VALUE;
	
	
	protected  UnitExponentiation(Model model,URI uri) throws SBOLGraphException
	{
		super(model, uri);
	}
	
	protected  UnitExponentiation(Resource resource)
	{
		super(resource);
	}
	
	public URI getBase() {
		if (base==null)
		{
			base=RDFUtil.getPropertyAsURI(this.resource, MeasureDataModel.UnitExponentiation.base);	
		}
		return base;
	}

	public void setBase(URI base) {
		this.base = base;
		RDFUtil.setProperty(resource, MeasureDataModel.UnitExponentiation.base, base);
	}
	
	public int getExponent() {
		if (exponent==Integer.MIN_VALUE)
		{
			String value=RDFUtil.getPropertyAsString(this.resource, MeasureDataModel.UnitExponentiation.exponent);
			if (value!=null)
			{
				exponent=Integer.valueOf(value);
			}
		}
		return exponent;
	}

	public void setExponent(int exponent) {
		this.exponent = exponent;
		RDFUtil.setProperty(resource, MeasureDataModel.UnitExponentiation.exponent, String.valueOf(exponent));
	}
	
	
	@Override
	public URI getResourceType() {
		return MeasureDataModel.UnitExponentiation.uri;
	}
	
}