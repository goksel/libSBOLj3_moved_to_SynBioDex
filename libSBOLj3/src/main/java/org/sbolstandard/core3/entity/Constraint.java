package org.sbolstandard.core3.entity;

import java.net.URI;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.sbolstandard.core3.util.RDFUtil;
import org.sbolstandard.core3.util.SBOLGraphException;
import org.sbolstandard.core3.validation.IdentityValidator;
import org.sbolstandard.core3.vocabulary.DataModel;

public class Constraint extends Identified{
	private URI restriction=null;
	private URI subject=null;
	private URI object=null;

	protected  Constraint(Model model,URI uri) throws SBOLGraphException
	{
		super(model, uri);
	}
	
	protected  Constraint(Resource resource) throws SBOLGraphException
	{
		super(resource);
	}
	
	public URI getRestriction() throws SBOLGraphException {
		if (restriction==null)
		{
			restriction=IdentityValidator.getValidator().getPropertyAsURI(this.resource, DataModel.Constraint.restriction);	
		}
		return restriction;
	}

	public void setRestriction(URI restriction) {
		this.restriction = restriction;
		RDFUtil.setProperty(resource, DataModel.Constraint.restriction, restriction);
	}

	
	public URI getSubject() throws SBOLGraphException{
		if (subject==null)
		{
			subject=IdentityValidator.getValidator().getPropertyAsURI(this.resource, DataModel.Constraint.subject);	
		}
		return subject;
	}

	public void setSubject(URI subject) {
		this.subject = subject;
		RDFUtil.setProperty(resource, DataModel.Constraint.subject, subject);
	}

	public URI getObject() throws SBOLGraphException {
		if (object==null)
		{
			object=IdentityValidator.getValidator().getPropertyAsURI(this.resource, DataModel.Constraint.object);	
		}
		return object;
	}

	public void setObject(URI object) {
		this.object = object;
		RDFUtil.setProperty(resource, DataModel.Constraint.object, object);
	}


	@Override
	public URI getResourceType() {
		return DataModel.Constraint.uri;
	}
	
	
}