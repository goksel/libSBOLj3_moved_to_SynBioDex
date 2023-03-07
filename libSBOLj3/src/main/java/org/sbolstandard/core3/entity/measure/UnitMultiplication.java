package org.sbolstandard.core3.entity.measure;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.sbolstandard.core3.entity.SBOLDocument;
import org.sbolstandard.core3.util.RDFUtil;
import org.sbolstandard.core3.util.SBOLGraphException;
import org.sbolstandard.core3.util.SBOLUtil;
import org.sbolstandard.core3.validation.IdentifiedValidator;
import org.sbolstandard.core3.validation.PropertyValidator;
import org.sbolstandard.core3.validation.ValidationMessage;
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
	
	public static UnitMultiplication create(SBOLDocument sbolDocument, URI uri, URI namespace) throws SBOLGraphException {
		UnitMultiplication identified = new UnitMultiplication(sbolDocument.getRDFModel(), uri);
		identified.setNamespace(namespace);
		return identified;
	}
	
	@NotNull(message = "{UNITMULTIPLICATION_TERM1_NOT_NULL}")	
	public Unit getTerm1() throws SBOLGraphException {
		//return IdentifiedValidator.getValidator().getPropertyAsURI(this.resource, MeasureDataModel.UnitMultiplication.term1);	
		return contsructIdentified(MeasureDataModel.UnitMultiplication.term1, Unit.getSubClassTypes());	
	}

	public void setTerm1(@NotNull(message = "UNITMULTIPLICATION_TERM1_NOT_NULL}") Unit term1) throws SBOLGraphException {
		PropertyValidator.getValidator().validate(this, "setTerm1", new Object[] {term1}, Unit.class);
		RDFUtil.setProperty(resource, MeasureDataModel.UnitMultiplication.term1, SBOLUtil.toURI(term1));
	}
	
	@NotNull(message = "UNITMULTIPLICATION_TERM2_NOT_NULL}")	
	public Unit getTerm2() throws SBOLGraphException {
		//return IdentifiedValidator.getValidator().getPropertyAsURI(this.resource, MeasureDataModel.UnitMultiplication.term2);
		return contsructIdentified(MeasureDataModel.UnitMultiplication.term2, Unit.getSubClassTypes());	
	}

	public void setTerm2(@NotNull(message = "UNITMULTIPLICATION_TERM2_NOT_NULL}") Unit term2) throws SBOLGraphException {
		PropertyValidator.getValidator().validate(this, "setTerm2", new Object[] {term2}, Unit.class);
		RDFUtil.setProperty(resource, MeasureDataModel.UnitMultiplication.term2, SBOLUtil.toURI(term2));
	}
	
	@Override
	public URI getResourceType() {
		return MeasureDataModel.UnitMultiplication.uri;
	}
	
	@Override
	public List<ValidationMessage> getValidationMessages() throws SBOLGraphException
	{
		List<ValidationMessage> validationMessages=super.getValidationMessages();
		validationMessages= IdentifiedValidator.assertEquals(this, MeasureDataModel.UnitMultiplication.term1, this.resource, getTerm1(), validationMessages);
		validationMessages= IdentifiedValidator.assertEquals(this, MeasureDataModel.UnitMultiplication.term2, this.resource, getTerm2(), validationMessages);
		return validationMessages;
	}
			
}