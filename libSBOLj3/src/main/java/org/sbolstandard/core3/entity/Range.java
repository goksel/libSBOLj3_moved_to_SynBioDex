package org.sbolstandard.core3.entity;

import java.net.URI;
import java.util.OptionalInt;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.sbolstandard.core3.util.RDFUtil;
import org.sbolstandard.core3.util.SBOLGraphException;
import org.sbolstandard.core3.validation.IdentityValidator;
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
	
	@NotNull(message = "Range.start cannot be empty")
	@Positive(message="Range.start must be bigger than zero")
	public OptionalInt getStart() throws SBOLGraphException{
		return IdentityValidator.getValidator().getPropertyAsOptionalInt(this.resource, DataModel.Range.start);
	}
	
	public void setStart(OptionalInt start) {
		IdentityValidator.getValidator().setPropertyAsOptionalInt(this.resource, DataModel.Range.start, start);
	}
	
	@NotNull(message = "Range.end cannot be empty")
	@Positive(message="Range.end must be bigger than zero")	
	public OptionalInt getEnd() throws SBOLGraphException {
		return IdentityValidator.getValidator().getPropertyAsOptionalInt(this.resource, DataModel.Range.end);
	}
	
	public void setEnd(OptionalInt end) {
		IdentityValidator.getValidator().setPropertyAsOptionalInt(this.resource, DataModel.Range.end, end);
	}	
	
	public URI getResourceType()
	{
		return DataModel.Range.uri;
	}
}
