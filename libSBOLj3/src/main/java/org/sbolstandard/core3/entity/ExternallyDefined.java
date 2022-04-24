package org.sbolstandard.core3.entity;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.sbolstandard.core3.util.RDFUtil;
import org.sbolstandard.core3.util.SBOLGraphException;
import org.sbolstandard.core3.validation.IdentityValidator;
import org.sbolstandard.core3.validation.PropertyValidator;
import org.sbolstandard.core3.vocabulary.DataModel;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public class ExternallyDefined extends Feature{
	/*private List<URI> types=new ArrayList<URI>();
	private URI definition=null;*/

	protected  ExternallyDefined(Model model,URI uri) throws SBOLGraphException
	{
		super(model, uri);
	}
	
	protected  ExternallyDefined(Resource resource) throws SBOLGraphException
	{
		super(resource);
	}

	@Valid
	@NotEmpty(message = "{EXTERNALLYDEFINED_TYPES_NOT_EMPTY}")
	public List<URI> getTypes() {
		return RDFUtil.getPropertiesAsURIs(this.resource, DataModel.type);
	}
	
	public void setTypes(@NotEmpty(message = "{EXTERNALLYDEFINED_TYPES_NOT_EMPTY}") List<URI> types) throws SBOLGraphException {
		PropertyValidator.getValidator().validate(this, "setTypes", new Object[] {types}, List.class);
		RDFUtil.setProperty(resource, DataModel.type, types);
	}
	
	@NotNull(message = "{EXTERNALLYDEFINED_DEFINITION_NOT_NULL}")
	public URI getDefinition() throws SBOLGraphException {
		return IdentityValidator.getValidator().getPropertyAsURI(this.resource, DataModel.ExternalyDefined.definition);
	}

	public void setDefinition(@NotNull(message = "{EXTERNALLYDEFINED_DEFINITION_NOT_NULL}") URI definition) throws SBOLGraphException {
		PropertyValidator.getValidator().validate(this, "setDefinition", new Object[] {definition}, URI.class);
		RDFUtil.setProperty(this.resource, DataModel.ExternalyDefined.definition, definition);	
	}
	
	@Override
	public URI getResourceType() {
		return DataModel.ExternalyDefined.uri;
	}
	
}