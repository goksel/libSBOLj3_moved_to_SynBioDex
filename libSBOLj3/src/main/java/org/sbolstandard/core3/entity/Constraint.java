package org.sbolstandard.core3.entity;

import java.net.URI;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.sbolstandard.core3.util.RDFUtil;
import org.sbolstandard.core3.util.SBOLGraphException;
import org.sbolstandard.core3.validation.IdentityValidator;
import org.sbolstandard.core3.validation.PropertyValidator;
import org.sbolstandard.core3.vocabulary.DataModel;

import jakarta.validation.constraints.NotNull;

public class Constraint extends Identified{
	/*private URI restriction=null;
	private URI subject=null;
	private URI object=null;*/

	protected  Constraint(Model model,URI uri) throws SBOLGraphException
	{
		super(model, uri);
	}
	
	protected  Constraint(Resource resource) throws SBOLGraphException
	{
		super(resource);
	}
	
	@NotNull(message = "{CONSTRAINT_RESTRICTION_NOT_NULL}")
	public URI getRestriction() throws SBOLGraphException {
		return IdentityValidator.getValidator().getPropertyAsURI(this.resource, DataModel.Constraint.restriction);	
	}

	public void setRestriction(@NotNull(message = "{CONSTRAINT_RESTRICTION_NOT_NULL}") URI restriction) throws SBOLGraphException{
		PropertyValidator.getValidator().validate(this, "setRestriction", new Object[] {restriction}, URI.class);
		RDFUtil.setProperty(resource, DataModel.Constraint.restriction, restriction);
	}
	
	@NotNull(message = "{CONSTRAINT_SUBJECT_NOT_NULL}")	
	public URI getSubject() throws SBOLGraphException{
		return IdentityValidator.getValidator().getPropertyAsURI(this.resource, DataModel.Constraint.subject);	
	}

	public void setSubject(@NotNull(message = "{CONSTRAINT_SUBJECT_NOT_NULL}") URI subject) throws SBOLGraphException{
		PropertyValidator.getValidator().validate(this, "setSubject", new Object[] {subject}, URI.class);
		RDFUtil.setProperty(resource, DataModel.Constraint.subject, subject);
	}

	@NotNull(message = "{CONSTRAINT_OBJECT_NOT_NULL}")
	public URI getObject() throws SBOLGraphException {
		return IdentityValidator.getValidator().getPropertyAsURI(this.resource, DataModel.Constraint.object);	
	}

	public void setObject(@NotNull(message = "{CONSTRAINT_OBJECT_NOT_NULL}") URI object) throws SBOLGraphException {
		PropertyValidator.getValidator().validate(this, "setObject", new Object[] {object}, URI.class);
		RDFUtil.setProperty(resource, DataModel.Constraint.object, object);
	}

	@Override
	public URI getResourceType() {
		return DataModel.Constraint.uri;
	}
}