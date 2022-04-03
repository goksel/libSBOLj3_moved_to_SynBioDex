package org.sbolstandard.core3.entity.measure;

import java.net.URI;
import java.util.List;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.sbolstandard.core3.entity.ControlledIdentified;
import org.sbolstandard.core3.util.RDFUtil;
import org.sbolstandard.core3.util.SBOLGraphException;
import org.sbolstandard.core3.validation.IdentityValidator;
import org.sbolstandard.core3.vocabulary.DataModel;
import org.sbolstandard.core3.vocabulary.MeasureDataModel;

public class Measure extends ControlledIdentified{
	
	/*private float value=Float.NaN;
	private List<URI> types;
	private URI unit;*/
	
	protected  Measure(Model model,URI uri) throws SBOLGraphException
	{
		super(model, uri);
	}
	
	protected  Measure(Resource resource) throws SBOLGraphException
	{
		super(resource);
	}
	
	public void setValue(float value) {
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
		float value=Float.NaN;
		String valueString=IdentityValidator.getValidator().getPropertyAsString(this.resource, MeasureDataModel.Measure.value);
		if (valueString!=null){
			try{
				value= Float.parseFloat(valueString);
			}
			catch (Exception e)
			{
				throw new SBOLGraphException("Cannot read the value. Property:" + MeasureDataModel.Measure.value + " Uri:+ " +  this.getUri(), e);
			}
		}
		return value;
	}
	
	
	public URI getUnit() throws SBOLGraphException {
		return IdentityValidator.getValidator().getPropertyAsURI(this.resource, MeasureDataModel.Measure.unit);	
	}

	public void setUnit(URI unit) {
		RDFUtil.setProperty(resource, MeasureDataModel.Measure.unit, unit);
	}

	public List<URI> getTypes() {
		return RDFUtil.getPropertiesAsURIs(this.resource,DataModel.type);
	}
	
	public void setTypes(List<URI> types) {
		RDFUtil.setProperty(resource, DataModel.type, types);
	}
	
	@Override
	public URI getResourceType() {
		return MeasureDataModel.Measure.uri;
	}
	
}