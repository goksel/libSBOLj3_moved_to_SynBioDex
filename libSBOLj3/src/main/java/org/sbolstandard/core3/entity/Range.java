package org.sbolstandard.core3.entity;

import java.net.URI;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.sbolstandard.core3.util.RDFUtil;
import org.sbolstandard.core3.util.SBOLGraphException;
import org.sbolstandard.core3.validation.IdentityValidator;
import org.sbolstandard.core3.vocabulary.DataModel;

public class Range extends Location {

	private int start=Integer.MIN_VALUE;
	private int end=Integer.MIN_VALUE;

	protected Range(Model model, URI uri) throws SBOLGraphException
	{
		super(model, uri);
	}
	
	protected  Range(Resource resource) throws SBOLGraphException
	{
		super(resource);
	}
	
	/*public RangeLocation(String displayId)
	{
		super(displayId);
	}*/
	
	public int getStart() throws SBOLGraphException{
		if (start==Integer.MIN_VALUE)
		{
			String value=IdentityValidator.getValidator().getPropertyAsString(this.resource, DataModel.Range.start);
			if (value!=null)
			{
				start=Integer.valueOf(value);
			}
		}
		return start;
	}
	
	public void setStart(int start) {
		this.start = start;
		RDFUtil.setProperty(this.resource, DataModel.Range.start, String.valueOf(this.start));
	}
	
	public int getEnd() throws SBOLGraphException {
		if (end==Integer.MIN_VALUE)
		{
			String value=IdentityValidator.getValidator().getPropertyAsString(this.resource, DataModel.Range.end);
			if (value!=null)
			{
				end=Integer.valueOf(value);
			}
		}
		return end;
	}
	
	public void setEnd(int end) {
		this.end = end;
		RDFUtil.setProperty(this.resource, DataModel.Range.end, String.valueOf(this.end));
	}	
	
	public URI getResourceType()
	{
		return DataModel.Range.uri;
	}
	
}
