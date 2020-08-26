package org.sbolstandard.entity.measure;

import java.net.URI;
import org.apache.jena.datatypes.xsd.XSDDatatype;
import org.apache.jena.datatypes.xsd.impl.XSDFloat;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.sbolstandard.util.RDFUtil;
import org.sbolstandard.util.SBOLGraphException;
import org.sbolstandard.vocabulary.MeasureDataModel;

public class SingularUnit extends Unit{
	
	private float factor=Float.NaN;
	private URI unit;
	
	protected  SingularUnit(Model model,URI uri) throws SBOLGraphException
	{
		super(model, uri);
	}
	
	protected  SingularUnit(Resource resource)
	{
		super(resource);
	}
	
	public void setFactor(float factor) {
		this.factor = factor;
		String factorString=String.valueOf(factor);
		//RDFUtil.setProperty(resource, MeasureDataModel.Prefix.factor, factorString);
		RDFUtil.setProperty(resource, MeasureDataModel.Prefix.factor, factor);
		
	}
	
	public float getFactor() throws SBOLGraphException {
		if (this.factor==Float.NaN)
		{
			String factorString=RDFUtil.getPropertyAsString(this.resource, MeasureDataModel.SingularUnit.factor);
			if (factorString!=null)
			{
				try
				{
					factor= Float.parseFloat(factorString);
					//(XSDFloat) XSDDatatype.XSDfloat.parse(factorString);
				}
				catch (Exception e)
				{
					throw new SBOLGraphException("Cannot read the factor value. Property:" + MeasureDataModel.SingularUnit.factor + " Uri:+ " +  this.getUri(), e);
				}
			}
		}
		return factor;
	}
	

	
	
	
	public URI getUnit() {
		if (unit==null)
		{
			unit=RDFUtil.getPropertyAsURI(this.resource, MeasureDataModel.PrefixedUnit.unit);	
		}
		return unit;
	}

	public void setUnit(URI unit) {
		this.unit = unit;
		RDFUtil.setProperty(resource, MeasureDataModel.PrefixedUnit.unit, unit);
	}

	
	@Override
	public URI getResourceType() {
		return MeasureDataModel.SingularUnit.uri;
	}
	
}