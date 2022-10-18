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
import jakarta.validation.constraints.PositiveOrZero;

public class Cut extends Location {
	//private int at=Integer.MIN_VALUE;

	protected Cut(Model model, URI uri) throws SBOLGraphException {
		super(model, uri);
	}
	
	protected  Cut(Resource resource) throws SBOLGraphException
	{
		super(resource);
	}
	
	@NotNull(message = "{CUT_AT_NOT_NULL}")
	public Optional<@NotNull(message = "{CUT_AT_NOT_NULL}") @PositiveOrZero(message="{CUT_AT_POSITIVE_OR_ZERO}") Integer> getAt() throws SBOLGraphException {
		return IdentifiedValidator.getValidator().getPropertyAsOptionalInteger(this.resource, DataModel.Cut.at);
	}
	
	public void setAt(Optional<@NotNull(message = "{CUT_AT_NOT_NULL}")  @PositiveOrZero(message="{CUT_AT_POSITIVE_OR_ZERO}")Integer> at) throws SBOLGraphException {
		PropertyValidator.getValidator().validate(this, "setAt", new Object[] {at}, Optional.class);
		IdentifiedValidator.getValidator().setPropertyAsOptional(this.resource, DataModel.Cut.at, at);
	}
	
	public URI getResourceType()
	{
		return DataModel.Cut.uri;
	}
	
	@Override
	public List<ValidationMessage> getValidationMessages() throws SBOLGraphException
	{
		List<ValidationMessage> validationMessages=super.getValidationMessages();
		Optional<Integer> at=getAt();
		Sequence sequence=this.getSequence();
		
		//CUT_AT_VALID_FOR_SEQUENCE
		validationMessages= assertLocationNotBiggerThanSequence(validationMessages, sequence, at, "{CUT_AT_VALID_FOR_SEQUENCE}", DataModel.Cut.at);
	
		return validationMessages;	
	}
	
}
