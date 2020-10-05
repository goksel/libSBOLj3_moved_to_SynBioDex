package org.sbolstandard.entity.measure;

import java.net.URI;
import java.util.List;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.sbolstandard.entity.Identified;
import org.sbolstandard.util.RDFUtil;
import org.sbolstandard.util.SBOLGraphException;
import org.sbolstandard.vocabulary.DataModel;
import org.sbolstandard.vocabulary.MeasureDataModel;

public class Measure extends Identified{
	
	private float value=Float.NaN;
	private List<URI> types;
	private URI unit;
	
	protected  Measure(Model model,URI uri) throws SBOLGraphException
	{
		super(model, uri);
	}
	
	protected  Measure(Resource resource)
	{
		super(resource);
	}
	
	public void setValue(float value) {
		this.value = value;
		String valueString=String.valueOf(value);
		RDFUtil.setProperty(resource, MeasureDataModel.Measure.value, valueString);	
	}
	
	/*public XSDFloat getFactor() throws SBOLGraphException {
		if (this.value==null)
		{
			String valueString=RDFUtil.getPropertyAsString(this.resource, MeasureDataModel.Measure.value);
			if (valueString!=null)
			{
				try
				{
					value= (XSDFloat) XSDDatatype.XSDfloat.parse(valueString);
				}
				catch (Exception e)
				{
					throw new SBOLGraphException("Cannot read the value. Property:" + MeasureDataModel.Measure.value + " Uri:+ " +  this.getUri(), e);
				}
			}
		}
		return value;
	}
	*/
	
	public float getValue() throws SBOLGraphException {
		if (Float.isNaN(this.value))
		{
			String valueString=RDFUtil.getPropertyAsString(this.resource, MeasureDataModel.Measure.value);
			if (valueString!=null)
			{
				try
				{
					value= Float.parseFloat(valueString);
				}
				catch (Exception e)
				{
					throw new SBOLGraphException("Cannot read the value. Property:" + MeasureDataModel.Measure.value + " Uri:+ " +  this.getUri(), e);
				}
			}
		}
		return value;
	}
	
	
	public URI getUnit() {
		if (unit==null)
		{
			unit=RDFUtil.getPropertyAsURI(this.resource, MeasureDataModel.Measure.unit);	
		}
		return unit;
	}

	public void setUnit(URI unit) {
		this.unit = unit;
		RDFUtil.setProperty(resource, MeasureDataModel.Measure.unit, unit);
	}

	public List<URI> getTypes() {
		if (types==null)
		{
			types=RDFUtil.getPropertiesAsURIs(this.resource,DataModel.type);
		}
		return types;
	}
	
	public void setTypes(List<URI> types) {
		this.types = types;
		RDFUtil.setProperty(resource, DataModel.type, types);
	}
	
	
	@Override
	public URI getResourceType() {
		return MeasureDataModel.Measure.uri;
	}
	
}