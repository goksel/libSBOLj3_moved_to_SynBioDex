package org.sbolstandard.core3.entity.measure;

import java.net.URI;
import java.util.Optional;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.sbolstandard.core3.util.RDFUtil;
import org.sbolstandard.core3.util.SBOLGraphException;
import org.sbolstandard.core3.validation.IdentityValidator;
import org.sbolstandard.core3.validation.PropertyValidator;
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
	public URI getPrefix() throws SBOLGraphException {
		return IdentityValidator.getValidator().getPropertyAsURI(this.resource, MeasureDataModel.PrefixedUnit.prefix);	
	}

	public void setPrefix(@NotNull(message = "{PREFIXEDUNIT_PREFIX_NOT_NULL}") URI prefix) throws SBOLGraphException {
		PropertyValidator.getValidator().validate(this, "setPrefix", new Object[] {prefix}, URI.class);
		RDFUtil.setProperty(resource, MeasureDataModel.PrefixedUnit.prefix, prefix);
	}
	
	@NotNull(message = "{PREFIXEDUNIT_UNIT_NOT_NULL}")	
	public URI getUnit() throws SBOLGraphException{
		return IdentityValidator.getValidator().getPropertyAsURI(this.resource, MeasureDataModel.PrefixedUnit.unit);
	}

	public void setUnit(@NotNull(message = "{PREFIXEDUNIT_UNIT_NOT_NULL}") URI unit) throws SBOLGraphException {
		PropertyValidator.getValidator().validate(this, "setUnit", new Object[] {unit}, URI.class);
		RDFUtil.setProperty(resource, MeasureDataModel.PrefixedUnit.unit, unit);
	}
	
	@Override
	public URI getResourceType() {
		return MeasureDataModel.PrefixedUnit.uri;
	}
}