package org.sbolstandard.entity;

import java.net.URI;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.sbolstandard.util.RDFUtil;
import org.sbolstandard.util.SBOLGraphException;
import org.sbolstandard.vocabulary.DataModel;

public class CutLocation extends Location {

	private int at=Integer.MIN_VALUE;
	

	protected CutLocation(Model model, URI uri) throws SBOLGraphException {
		super(model, uri);
	}
	
	protected  CutLocation(Resource resource)
	{
		super(resource);
	}
	
	

	
	
	public int getAt() {
		if (at==Integer.MIN_VALUE)
		{
			String value=RDFUtil.getPropertyAsString(this.resource, DataModel.Cut.at);
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
