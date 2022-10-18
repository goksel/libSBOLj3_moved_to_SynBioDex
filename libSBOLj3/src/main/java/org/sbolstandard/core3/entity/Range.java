package org.sbolstandard.core3.entity;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.sbolstandard.core3.util.RDFUtil;
import org.sbolstandard.core3.util.SBOLGraphException;
import org.sbolstandard.core3.validation.IdentifiedValidator;
import org.sbolstandard.core3.validation.PropertyValidator;
import org.sbolstandard.core3.validation.ValidationMessage;
import org.sbolstandard.core3.vocabulary.DataModel;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

public class Range extends Location {

	/*private int start=Integer.MIN_VALUE;
	private int end=Integer.MIN_VALUE;*/

	protected Range(Model model, URI uri) throws SBOLGraphException
	{
		super(model, uri);
	}
	
	protected  Range(Resource resource) throws SBOLGraphException
	{
		super(resource);
	}
	
	/*public RangeLocation(String displayId)
	{
		super(displayId);
	}*/
	
	@NotNull(message = "{RANGE_START_NOT_NULL}")
	public Optional<@NotNull(message = "{RANGE_START_NOT_NULL}") @Positive(message="{RANGE_START_POSITIVE_OR_ZERO}") Integer> getStart() throws SBOLGraphException{
		return IdentifiedValidator.getValidator().getPropertyAsOptionalInteger(this.resource, DataModel.Range.start);
	}
	
	public void setStart(@NotNull(message = "{RANGE_START_NOT_NULL}") Optional<@NotNull(message = "{RANGE_START_NOT_NULL}") @Positive(message="{RANGE_START_POSITIVE_OR_ZERO}") Integer> start) throws SBOLGraphException {
		PropertyValidator.getValidator().validate(this, "setStart", new Object[] {start}, Optional.class);
		IdentifiedValidator.getValidator().setPropertyAsOptional(this.resource, DataModel.Range.start, start);
	}
	
	@NotNull(message = "{RANGE_END_NOT_NULL}")
	public Optional<@NotNull(message = "{RANGE_END_NOT_NULL}") @Positive(message="{RANGE_END_POSITIVE_OR_ZERO}")Integer> getEnd() throws SBOLGraphException {
		return IdentifiedValidator.getValidator().getPropertyAsOptionalInteger(this.resource, DataModel.Range.end);
	}
	
	public void setEnd(@NotNull(message = "{RANGE_END_NOT_NULL}") Optional< @NotNull(message = "{RANGE_END_NOT_NULL}") @Positive(message="{RANGE_START_POSITIVE_OR_ZERO}") Integer> end) throws SBOLGraphException {
		PropertyValidator.getValidator().validate(this, "setEnd", new Object[] {end}, Optional.class);
		IdentifiedValidator.getValidator().setPropertyAsOptional(this.resource, DataModel.Range.end, end);
	}	
	
	public URI getResourceType()
	{
		return DataModel.Range.uri;
	}
	
	@Override
	public List<ValidationMessage> getValidationMessages() throws SBOLGraphException
	{
		List<ValidationMessage> validationMessages=super.getValidationMessages();
		Optional<Integer> start=getStart();
		Optional<Integer> end=getEnd();
		Sequence sequence=this.getSequence();
		
		//RANGE_START_VALID_FOR_SEQUENCE
		validationMessages= assertLocationNotBiggerThanSequence(validationMessages, sequence, start, "{RANGE_START_VALID_FOR_SEQUENCE}", DataModel.Range.start);
		
		//RANGE_END_VALID_FOR_SEQUENCE
		validationMessages= assertLocationNotBiggerThanSequence(validationMessages, sequence, end, "{RANGE_END_VALID_FOR_SEQUENCE}", DataModel.Range.end);
		
		//RANGE_END_BIGGER_THAN_START
		if (start!=null && end!=null && !start.isEmpty() && !end.isEmpty())
		{
			if (end.get()<start.get())
			{
				ValidationMessage validationMessage=new ValidationMessage("{RANGE_END_BIGGER_THAN_START}", DataModel.Range.end, end.get());
				validationMessages = IdentifiedValidator.addToValidations(validationMessages, validationMessage);
		
			}
		}
		return validationMessages;	
	}
	
}
