package org.sbolstandard.sequence;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.sbolstandard.TopLevel;
import org.sbolstandard.util.RDFHandler;

public class Component extends TopLevel {
	
	private List<URI> types=new ArrayList<URI>();
	public List<URI> getRoles() {
		return roles;
	}
	public void setRoles(List<URI> roles) {
		this.roles = roles;
	}
	public List<URI> getSequences() {
		return sequences;
	}
	public void setSequences(List<URI> sequences) {
		this.sequences = sequences;
	}

	private List<URI> roles=new ArrayList<URI>();
	private List<URI> sequences=new ArrayList<URI>();
	
	
	public Component(URI uri, URI type)
	{
		super(uri);
		this.types.add(type);
	}
	public Component(URI uri, List<URI> types)
	{
		super(uri);
		this.types=types;
	}
	
	public void addrole(URI role)
	{
		roles.add(role);
	}
	
	public Resource addEntitySpecificProperties(Model model, Resource resource)
	{
		RDFHandler.addProperty(model, resource, URI.create("http://sbols.org/v3#type"), this.types);
		RDFHandler.addProperty(model, resource, URI.create("http://sbols.org/v3#role"), this.roles);
		RDFHandler.addProperty(model, resource, URI.create("http://sbols.org/v3#sequence"), this.sequences);
		return resource;
		
	}
	
	public URI getResourceType()
	{
		return URI.create("http://sbols.org/v3#Component");
	}
	
}
