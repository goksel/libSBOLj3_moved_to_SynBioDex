package org.sbolstandard.core3.entity;

import java.net.URI;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.sbolstandard.core3.util.RDFUtil;
import org.sbolstandard.core3.util.SBOLGraphException;
import org.sbolstandard.core3.validation.IdentifiedValidator;
import org.sbolstandard.core3.validation.PropertyValidator;
import org.sbolstandard.core3.vocabulary.DataModel;

import jakarta.validation.constraints.NotNull;

public class ComponentReference extends Feature{
	/*private URI feature=null;
	private URI inChildOf=null;*/
	
	protected  ComponentReference(Model model,URI uri) throws SBOLGraphException
	{
		super(model, uri);
	}
	
	protected  ComponentReference(Resource resource) throws SBOLGraphException
	{
		super(resource);
	}

	@NotNull(message = "{COMBINATORIALREFERENCE_REFERSTO_NOT_NULL}")
	public URI getRefersTo() throws SBOLGraphException {
		return IdentifiedValidator.getValidator().getPropertyAsURI(this.resource, DataModel.ComponentReference.refersTo);
	}
	
	public void setRefersTo(@NotNull(message = "{COMBINATORIALREFERENCE_REFERSTO_NOT_NULL}") URI feature) throws SBOLGraphException {
		PropertyValidator.getValidator().validate(this, "setRefersTo", new Object[] {feature}, URI.class);
		RDFUtil.setProperty(this.resource, DataModel.ComponentReference.refersTo, feature);
	}
	
	@NotNull(message = "{COMBINATORIALREFERENCE_INCHILDOF_NOT_NULL}")
	public URI getInChildOf() throws SBOLGraphException{
		return IdentifiedValidator.getValidator().getPropertyAsURI(this.resource, DataModel.ComponentReference.inChildOf);
	}

	public void setInChildOf(@NotNull(message = "{COMBINATORIALREFERENCE_INCHILDOF_NOT_NULL}") URI inChildOf) throws SBOLGraphException{
		PropertyValidator.getValidator().validate(this, "setInChildOf", new Object[] {inChildOf}, URI.class);
		RDFUtil.setProperty(this.resource, DataModel.ComponentReference.inChildOf, inChildOf);	
	}
	
	@Override
	public URI getResourceType() {
		return DataModel.ComponentReference.uri;
	}
}