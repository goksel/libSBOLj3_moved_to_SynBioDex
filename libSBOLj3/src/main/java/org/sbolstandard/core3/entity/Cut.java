package org.sbolstandard.core3.entity;

import java.net.URI;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.sbolstandard.core3.util.RDFUtil;
import org.sbolstandard.core3.util.SBOLGraphException;
import org.sbolstandard.core3.validation.IdentityValidator;
import org.sbolstandard.core3.vocabulary.DataModel;

public class Cut extends Location {

	private int at=Integer.MIN_VALUE;
	

	protected Cut(Model model, URI uri) throws SBOLGraphException {
		super(model, uri);
	}
	
	protected  Cut(Resource resource) throws SBOLGraphException
	{
		super(resource);
	}
	
	

	
	
	public int getAt() throws SBOLGraphException {
		if (at==Integer.MIN_VALUE)
		{
			String value=IdentityValidator.getValidator().getPropertyAsString(this.resource, DataModel.Cut.at);
			if (value!=null)
			{
				at=Integer.valueOf(value);
			}
		}
		return at;
	}
	
	public void setAt(int at) {
		this.at = at;
		RDFUtil.setProperty(this.resource, DataModel.Cut.at, String.valueOf(this.at));
	}
	
	
	public URI getResourceType()
	{
		return DataModel.Cut.uri;
	}
	
	
}
