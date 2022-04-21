package org.sbolstandard.core3.entity.measure;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.sbolstandard.core3.entity.ControlledIdentified;
import org.sbolstandard.core3.util.RDFUtil;
import org.sbolstandard.core3.util.SBOLGraphException;
import org.sbolstandard.core3.validation.IdentityValidator;
import org.sbolstandard.core3.validation.PropertyValidator;
import org.sbolstandard.core3.vocabulary.DataModel;
import org.sbolstandard.core3.vocabulary.MeasureDataModel;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

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
	
	//The first NotNull: value is not null, the second NotNull: Not empty.
	public void setValue(@NotNull (message = "{MEASURE_VALUE_NOT_NULL}") Optional<@NotNull (message = "{MEASURE_VALUE_NOT_NULL}") Float> value) throws SBOLGraphException{
		//String valueString=String.valueOf(value);
		//RDFUtil.setProperty(resource, MeasureDataModel.Measure.value, valueString);	
		PropertyValidator.getValidator().validate(this, "setValue", new Object[] {value}, Optional.class);
		IdentityValidator.getValidator().setPropertyAsOptional(this.resource, MeasureDataModel.Measure.value, value);
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
	
	/*public float getValue() throws SBOLGraphException {
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
	}*/
	
	//@Valid
	//@NotNull(message = "Measure.value cannot be null")	
	@NotNull (message = "{MEASURE_VALUE_NOT_NULL}")
	public Optional<@NotNull (message = "{MEASURE_VALUE_NOT_NULL}") Float> getValue() throws SBOLGraphException {
		Optional<Float> value= IdentityValidator.getValidator().getPropertyAsOptionalFloat(this.resource, MeasureDataModel.Measure.value);
		return value;
	}
	
	/*
	private Optional<Integer> test;
	@NotNull(message = "Measure.test cannot be null")	
	public Optional<@NotNull Integer> getTest() throws SBOLGraphException {
		return test;
	}
	
	public void setTest(Optional<Integer> value)
	{
		this.test=value;
	}
	*/
	@NotNull(message = "{MEASURE_UNIT_NOT_NULL}")	
	public URI getUnit() throws SBOLGraphException {
		return IdentityValidator.getValidator().getPropertyAsURI(this.resource, MeasureDataModel.Measure.unit);	
	}

	public void setUnit(@NotNull(message = "{MEASURE_UNIT_NOT_NULL}") URI unit) throws SBOLGraphException {
		PropertyValidator.getValidator().validate(this, "setUnit", new Object[] {unit}, URI.class);
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