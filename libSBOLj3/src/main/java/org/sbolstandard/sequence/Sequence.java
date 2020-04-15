package org.sbolstandard.sequence;

import java.net.URI;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.sbolstandard.TopLevel;
import org.sbolstandard.util.RDFHandler;
import org.sbolstandard.vocabulary.Encoding;

public class Sequence extends TopLevel {
	public Sequence(URI uri)
	{
		super(uri);
	}
	
	public String getElements() {
		return elements;
	}
	public void setElements(String elements) {
		this.elements = elements;
	}
	public Encoding getEncoding() {
		return encoding;
	}
	public void setEncoding(Encoding encoding) {
		this.encoding = encoding;
	}
	private String elements;
	private Encoding encoding;

	public Resource addEntitySpecificProperties(Model model, Resource resource)
	{
		RDFHandler.addProperty(model, resource, URI.create("http://sbols.org/v3#elements"), this.elements);
		RDFHandler.addProperty(model, resource, URI.create("http://sbols.org/v3#encoding"), this.encoding.getUrl());
		return resource;
		
	}
	
	public URI getResourceType()
	{
		return URI.create("http://sbols.org/v3#Sequence");
	}
	
}
