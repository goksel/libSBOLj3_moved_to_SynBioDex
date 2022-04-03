package org.sbolstandard.core3.entity.measure;

import java.net.URI;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.sbolstandard.core3.util.RDFUtil;
import org.sbolstandard.core3.util.SBOLGraphException;
import org.sbolstandard.core3.validation.IdentityValidator;
import org.sbolstandard.core3.vocabulary.MeasureDataModel;

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
	
	public URI getBase() throws SBOLGraphException{
		return IdentityValidator.getValidator().getPropertyAsURI(this.resource, MeasureDataModel.UnitExponentiation.base);
	}

	public void setBase(URI base) {
		RDFUtil.setProperty(resource, MeasureDataModel.UnitExponentiation.base, base);
	}
	
	public int getExponent() throws SBOLGraphException {
		int exponent=Integer.MIN_VALUE;

		String value=IdentityValidator.getValidator().getPropertyAsString(this.resource, MeasureDataModel.UnitExponentiation.exponent);
		if (value!=null)
		{
			exponent=Integer.valueOf(value);
		}
		
		return exponent;
	}

	public void setExponent(int exponent) {
		RDFUtil.setProperty(resource, MeasureDataModel.UnitExponentiation.exponent, String.valueOf(exponent));
	}
	
	
	@Override
	public URI getResourceType() {
		return MeasureDataModel.UnitExponentiation.uri;
	}
	
}