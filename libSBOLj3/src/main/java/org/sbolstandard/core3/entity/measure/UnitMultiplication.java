package org.sbolstandard.core3.entity.measure;

import java.net.URI;
import java.util.Optional;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.sbolstandard.core3.util.RDFUtil;
import org.sbolstandard.core3.util.SBOLGraphException;
import org.sbolstandard.core3.validation.IdentifiedValidator;
import org.sbolstandard.core3.validation.PropertyValidator;
import org.sbolstandard.core3.vocabulary.MeasureDataModel;

import jakarta.validation.constraints.NotNull;

public class UnitMultiplication extends CompoundUnit{
	/*private URI term1;
	private URI term2;*/
	protected  UnitMultiplication(Model model,URI uri) throws SBOLGraphException
	{
		super(model, uri);
	}
	
	protected  UnitMultiplication(Resource resource) throws SBOLGraphException
	{
		super(resource);
	}
	
	@NotNull(message = "{UNITMULTIPLICATION_TERM1_NOT_NULL}")	
	public URI getTerm1() throws SBOLGraphException {
		return IdentifiedValidator.getValidator().getPropertyAsURI(this.resource, MeasureDataModel.UnitMultiplication.term1);	
	}

	public void setTerm1(@NotNull(message = "UNITMULTIPLICATION_TERM1_NOT_NULL}") URI term1) throws SBOLGraphException {
		PropertyValidator.getValidator().validate(this, "setTerm1", new Object[] {term1}, URI.class);
		RDFUtil.setProperty(resource, MeasureDataModel.UnitMultiplication.term1, term1);
	}
	
	@NotNull(message = "UNITMULTIPLICATION_TERM2_NOT_NULL}")	
	public URI getTerm2() throws SBOLGraphException {
		return IdentifiedValidator.getValidator().getPropertyAsURI(this.resource, MeasureDataModel.UnitMultiplication.term2);	
	}

	public void setTerm2(@NotNull(message = "UNITMULTIPLICATION_TERM2_NOT_NULL}") URI term2) throws SBOLGraphException {
		PropertyValidator.getValidator().validate(this, "setTerm2", new Object[] {term2}, URI.class);
		RDFUtil.setProperty(resource, MeasureDataModel.UnitMultiplication.term2, term2);
	}
	
	@Override
	public URI getResourceType() {
		return MeasureDataModel.UnitMultiplication.uri;
	}
}