package org.sbolstandard.entity;

import java.net.URI;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.sbolstandard.util.RDFUtil;
import org.sbolstandard.util.SBOLGraphException;
import org.sbolstandard.vocabulary.DataModel;
import org.sbolstandard.vocabulary.Encoding;

public class Sequence extends TopLevel {
	private String elements;
	private Encoding encoding;

	protected  Sequence(Model model,URI uri) throws SBOLGraphException
	{
		super(model, uri);
	}
	
	protected  Sequence(Resource resource)
	{
		super(resource);
	}

	public String getElements() {
		if (elements==null)
		{
			elements=RDFUtil.getPropertyAsString(this.resource, DataModel.Sequence.elements);
		}
		return elements;
	}
	
	public void setElements(String elements) {
		this.elements = elements;
		RDFUtil.setProperty(this.resource, DataModel.Sequence.elements, this.elements);
	}
	
	public Encoding getEncoding() {
		if (encoding==null)
		{
			URI encodingValue=RDFUtil.getPropertyAsURI(this.resource, DataModel.Sequence.encoding);
			if (encodingValue!=null)
			{
				encoding=Encoding.get(encodingValue); 
			}
		}
		return encoding;
	}
	
	public void setEncoding(Encoding encoding) {
		this.encoding = encoding;
		RDFUtil.setProperty(this.resource, DataModel.Sequence.encoding, this.encoding.getUri());
	}

	public URI getResourceType()
	{
		return DataModel.Sequence.uri;
	}
	
}