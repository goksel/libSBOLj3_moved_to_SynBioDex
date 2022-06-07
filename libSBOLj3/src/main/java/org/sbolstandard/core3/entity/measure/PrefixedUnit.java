package org.sbolstandard.core3.entity.measure;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.sbolstandard.core3.util.RDFUtil;
import org.sbolstandard.core3.util.SBOLGraphException;
import org.sbolstandard.core3.util.SBOLUtil;
import org.sbolstandard.core3.validation.IdentifiedValidator;
import org.sbolstandard.core3.validation.PropertyValidator;
import org.sbolstandard.core3.validation.ValidationMessage;
import org.sbolstandard.core3.vocabulary.MeasureDataModel;

import jakarta.validation.constraints.NotNull;

public class PrefixedUnit extends Unit{
	
	/*private URI prefix;
	private URI unit;*/
	
	protected  PrefixedUnit(Model model,URI uri) throws SBOLGraphException
	{
		super(model, uri);
	}
	
	protected  PrefixedUnit(Resource resource) throws SBOLGraphException
	{
		super(resource);
	}
	
	@NotNull(message = "{PREFIXEDUNIT_PREFIX_NOT_NULL}")	
	public Prefix getPrefix() throws SBOLGraphException {
		//return IdentifiedValidator.getValidator().getPropertyAsURI(this.resource, MeasureDataModel.PrefixedUnit.prefix);	
		return contsructIdentified(MeasureDataModel.PrefixedUnit.prefix, Prefix.getSubClassTypes());	
	}

	public void setPrefix(@NotNull(message = "{PREFIXEDUNIT_PREFIX_NOT_NULL}") Prefix prefix) throws SBOLGraphException {
		PropertyValidator.getValidator().validate(this, "setPrefix", new Object[] {prefix}, Prefix.class);
		RDFUtil.setProperty(resource, MeasureDataModel.PrefixedUnit.prefix, SBOLUtil.toURI(prefix));
	}
	
	@NotNull(message = "{PREFIXEDUNIT_UNIT_NOT_NULL}")	
	public Unit getUnit() throws SBOLGraphException{
		//return IdentifiedValidator.getValidator().getPropertyAsURI(this.resource, MeasureDataModel.PrefixedUnit.unit);
		return contsructIdentified(MeasureDataModel.PrefixedUnit.unit, Unit.getSubClassTypes());	
	}

	public void setUnit(@NotNull(message = "{PREFIXEDUNIT_UNIT_NOT_NULL}") Unit unit) throws SBOLGraphException {
		PropertyValidator.getValidator().validate(this, "setUnit", new Object[] {unit}, Unit.class);
		RDFUtil.setProperty(resource, MeasureDataModel.PrefixedUnit.unit, SBOLUtil.toURI(unit));
	}
	
	@Override
	public URI getResourceType() {
		return MeasureDataModel.PrefixedUnit.uri;
	}
	
	@Override
	public List<ValidationMessage> getValidationMessages() throws SBOLGraphException
	{
		List<ValidationMessage> validationMessages=super.getValidationMessages();
		validationMessages= IdentifiedValidator.assertEquals(this, MeasureDataModel.PrefixedUnit.unit, this.resource, getUnit(), validationMessages);
		return validationMessages;
	}
}