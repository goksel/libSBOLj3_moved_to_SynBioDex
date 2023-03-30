package org.sbolstandard.core3.entity.measure;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.sbolstandard.core3.entity.ControlledIdentified;
import org.sbolstandard.core3.util.Configuration;
import org.sbolstandard.core3.util.RDFUtil;
import org.sbolstandard.core3.util.SBOLGraphException;
import org.sbolstandard.core3.util.SBOLUtil;
import org.sbolstandard.core3.validation.IdentifiedValidator;
import org.sbolstandard.core3.validation.PropertyValidator;
import org.sbolstandard.core3.validation.ValidationMessage;
import org.sbolstandard.core3.vocabulary.DataModel;
import org.sbolstandard.core3.vocabulary.MeasureDataModel;
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
		IdentifiedValidator.getValidator().setPropertyAsOptional(this.resource, MeasureDataModel.Measure.value, value);
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
		Optional<Float> value= IdentifiedValidator.getValidator().getPropertyAsOptionalFloat(this.resource, MeasureDataModel.Measure.value);
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
	public Unit getUnit() throws SBOLGraphException {
		//return IdentifiedValidator.getValidator().getPropertyAsURI(this.resource, MeasureDataModel.Measure.unit);	
		return contsructIdentified(MeasureDataModel.Measure.unit, Unit.getSubClassTypes());
	}

	public void setUnit(@NotNull(message = "{MEASURE_UNIT_NOT_NULL}") Unit unit) throws SBOLGraphException {
		PropertyValidator.getValidator().validate(this, "setUnit", new Object[] {unit}, Unit.class);
		RDFUtil.setProperty(resource, MeasureDataModel.Measure.unit, SBOLUtil.toURI(unit));
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
	
	@Override
	public List<ValidationMessage> getValidationMessages() throws SBOLGraphException
	{
		List<ValidationMessage> validationMessages=super.getValidationMessages();
		validationMessages=assertSBOTypesIncluded(validationMessages);
		validationMessages= IdentifiedValidator.assertEquals(this, MeasureDataModel.Measure.unit, this.resource, getUnit(), validationMessages);
		return validationMessages;
	}
	
	public List<ValidationMessage> assertSBOTypesIncluded(List<ValidationMessage> validationMessages) throws SBOLGraphException
	{
		if (Configuration.getInstance().isValidateRecommendedRules()) {
			List<URI> types=this.getTypes();
			if (types!=null && types.size()>0){
				boolean valid=false;
				for (URI type:types) {
					if (Configuration.getInstance().getSboSystemDescriptionParameters().contains(type.toString())) {
						valid=true;
						break;
					}						
				}
				if (!valid){
					ValidationMessage message=new ValidationMessage("{MEASURE_TYPE_SBO}", MeasureDataModel.Measure.type, types);      
					validationMessages= IdentifiedValidator.addToValidations(validationMessages,message);											
				}
			}
		}
		return validationMessages;
	}
	

	
}