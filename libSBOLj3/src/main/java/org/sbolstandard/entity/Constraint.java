package org.sbolstandard.entity;

import java.net.URI;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.sbolstandard.util.RDFUtil;
import org.sbolstandard.util.SBOLGraphException;
import org.sbolstandard.vocabulary.DataModel;

public class Constraint extends Identified{
	private URI restriction=null;
	private URI subject=null;
	private URI object=null;

	protected  Constraint(Model model,URI uri) throws SBOLGraphException
	{
		super(model, uri);
	}
	
	protected  Constraint(Resource resource)
	{
		super(resource);
	}
	
	public URI getRestriction() throws SBOLGraphException {
		if (restriction==null)
		{
			restriction=RDFUtil.getPropertyAsURI(this.resource, DataModel.Constraint.restriction);	
		}
		return restriction;
	}

	public void setRestriction(URI restriction) {
		this.restriction = restriction;
		RDFUtil.setProperty(resource, DataModel.Constraint.restriction, restriction);
	}

	
	public URI getSubject() {
		if (subject==null)
		{
			subject=RDFUtil.getPropertyAsURI(this.resource, DataModel.Constraint.subject);	
		}
		return subject;
	}

	public void setSubject(URI subject) {
		this.subject = subject;
		RDFUtil.setProperty(resource, DataModel.Constraint.subject, subject);
	}

	public URI getObject() {
		if (object==null)
		{
			object=RDFUtil.getPropertyAsURI(this.resource, DataModel.Constraint.object);	
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