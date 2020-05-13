package org.sbolstandard.entity;

import java.net.URI;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;

public abstract class TopLevel extends Identified {

	protected TopLevel(Model model, URI uri)
	{
		super(model, uri);
	}
	protected TopLevel() 
	{
		
	}
	
	protected TopLevel (Resource resource)
	{
		super(resource);
	}
	
}
