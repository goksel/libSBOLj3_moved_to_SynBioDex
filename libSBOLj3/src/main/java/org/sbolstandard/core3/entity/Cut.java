package org.sbolstandard.core3.entity;

import java.net.URI;
import java.util.OptionalInt;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.sbolstandard.core3.util.RDFUtil;
import org.sbolstandard.core3.util.SBOLGraphException;
import org.sbolstandard.core3.validation.IdentityValidator;
import org.sbolstandard.core3.vocabulary.DataModel;

public class Cut extends Location {
	//private int at=Integer.MIN_VALUE;

	protected Cut(Model model, URI uri) throws SBOLGraphException {
		super(model, uri);
	}
	
	protected  Cut(Resource resource) throws SBOLGraphException
	{
		super(resource);
	}
	
	public OptionalInt getAt() throws SBOLGraphException {
		return IdentityValidator.getValidator().getPropertyAsOptionalInt(this.resource, DataModel.Cut.at);
	}
	
	public void setAt(OptionalInt at) {
		IdentityValidator.getValidator().setPropertyAsOptionalInt(this.resource, DataModel.Cut.at, at);
	}
	
	public URI getResourceType()
	{
		return DataModel.Cut.uri;
	}
}
