package org.sbolstandard.entity;

import java.net.URI;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.sbolstandard.util.RDFUtil;
import org.sbolstandard.vocabulary.Encoding;

public class Sequence extends TopLevel {
	private String elements;
	private Encoding encoding;

	protected  Sequence(Model model,URI uri)
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
			elements=RDFUtil.getPropertyAsString(this.resource, URI.create("http://sbols.org/v3#elements"));
		}
		return elements;
	}
	
	public void setElements(String elements) {
		this.elements = elements;
		RDFUtil.setProperty(this.resource, URI.create("http://sbols.org/v3#elements"), this.elements);
	}
	
	public Encoding getEncoding() {
		if (encoding==null)
		{
			String encodingValue=RDFUtil.getPropertyAsString(this.resource, URI.create("http://sbols.org/v3#elements"));
			if (encodingValue!=null)
			{
				encoding=Encoding.valueOf(encodingValue); 
			}
		}
		return encoding;
	}
	
	public void setEncoding(Encoding encoding) {
		this.encoding = encoding;
		RDFUtil.setProperty(this.resource, URI.create("http://sbols.org/v3#encoding"), this.encoding.getUrl());
	}

	public URI getResourceType()
	{
		return URI.create("http://sbols.org/v3#Sequence");
	}
	
}