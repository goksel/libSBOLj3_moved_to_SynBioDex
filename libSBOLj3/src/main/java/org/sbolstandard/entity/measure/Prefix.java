package org.sbolstandard.entity.measure;

import java.net.URI;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.sbolstandard.util.RDFUtil;
import org.sbolstandard.util.SBOLGraphException;
import org.sbolstandard.vocabulary.MeasureDataModel;

public abstract class Prefix extends Unit{
	
	private float factor=Float.NaN;
	
	protected  Prefix(Model model,URI uri) throws SBOLGraphException
	{
		super(model, uri);
	}
	
	protected  Prefix(Resource resource)
	{
		super(resource);
	}
	
	
	
	public void setFactor(float factor) {
		this.factor = factor;
		String factorString=String.valueOf(factor);
		RDFUtil.setProperty(resource, MeasureDataModel.Prefix.factor, factorString);
		//RDFUtil.setProperty(resource, MeasureDataModel.Prefix.factor, factor);
		
	}
	
	public float getFactor() throws SBOLGraphException {
		if (Float.isNaN(this.factor))
		{
			String factorString=RDFUtil.getPropertyAsString(this.resource, MeasureDataModel.Prefix.factor);
			if (factorString!=null)
			{
				try
				{
					factor= Float.parseFloat(factorString);
					//(XSDFloat) XSDDatatype.XSDfloat.parse(factorString);
				}
				catch (Exception e)
				{
					throw new SBOLGraphException("Cannot read the factor value. Property:" + MeasureDataModel.Prefix.factor + " Uri:+ " +  this.getUri(), e);
				}
			}
		}
		return factor;
	}

	@Override
	public URI getResourceType() {
		return MeasureDataModel.Prefix.uri;
	}
	
}