package org.sbolstandard.entity;

import java.lang.reflect.Constructor;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Resource;
import org.sbolstandard.util.NameSpace;
import org.sbolstandard.util.RDFUtil;
import org.sbolstandard.util.SBOLGraphException;

public class SBOLDocument {
	protected Model model = null;
	private URI baseURI=null;

	public Model getModel() {
		return model;
	}
	
	private void setNameSpacePrefixes()
	{
		this.model.setNsPrefix(NameSpace.SBOL.getPrefix(), NameSpace.SBOL.getUri().toString());
		this.model.setNsPrefix(NameSpace.SO.getPrefix(), NameSpace.SO.getUri().toString());
		this.model.setNsPrefix(NameSpace.SBO.getPrefix(), NameSpace.SBO.getUri().toString());
		this.model.setNsPrefix(NameSpace.GO.getPrefix(), NameSpace.GO.getUri().toString());
		this.model.setNsPrefix(NameSpace.CHEBI.getPrefix(), NameSpace.CHEBI.getUri().toString());
	}

	public SBOLDocument() {
		this.model = ModelFactory.createDefaultModel();
		setNameSpacePrefixes();
	}
	
	public SBOLDocument(URI base) {
		this();
		RDFUtil.setBaseURI(this.model, base);
	}
	
	public SBOLDocument(Model model) {
		this.model = model;
		setNameSpacePrefixes();
	}

	public URI getBaseURI()
	{
		String baseURI =this.model.getNsPrefixURI("");
		if (baseURI!=null)
		{
			return URI.create(baseURI);
		}
		else
		{
			return null;
		}
	}
	
	private List<Component> components;
	private List<Sequence> sequences;


	public List<Component> getComponents() throws SBOLGraphException {
		this.components=addToList(model, this.components, URI.create("http://sbols.org/v3#Component"),Component.class);
		return components;
	}
	
	public Component createComponent(URI uri, List<URI> types) throws SBOLGraphException {

		Component component = new Component(this.model, uri);
		component.setTypes(types);
		if (components == null) {
			components = new ArrayList<Component>();
		}
		components.add(component);
		return component;
	}
	
	public List<Sequence> getSequences() throws SBOLGraphException {
		this.sequences=addToList(model, this.sequences, URI.create("http://sbols.org/v3#Sequence"),Sequence.class);
		return sequences;
	}

	public Sequence createSequence(URI uri) throws SBOLGraphException {
		Sequence sequence = new Sequence(this.model, uri);
		if (sequences == null) {
			sequences = new ArrayList<Sequence>();
		}
		sequences.add(sequence);
		return sequence;
	}
	
	
	
	public <T extends Identified>Identified getIdentified(URI uri, Class<T> identified) throws SBOLGraphException
	{
		Resource res=this.model.getResource(uri.toString());
		try
		{
			Constructor<T> constructor = identified.getDeclaredConstructor( new Class[] {Resource.class});
			Identified entity= (Identified)constructor.newInstance(new Object[]{res});
			return entity;
		}
		catch (Exception ex)
		{
			throw new SBOLGraphException(ex.getMessage());
		}

	}
	
	protected <T extends Identified> Identified createIdentified(Resource res, Class<T> identified) throws SBOLGraphException
	{
		try
		{
			Constructor<T> constructor = identified.getDeclaredConstructor( new Class[] {Resource.class});
			Identified entity= (Identified)constructor.newInstance(new Object[]{res});
			return entity;
		}
		catch (Exception ex)
		{
			throw new SBOLGraphException(ex.getMessage());
		}
	}

	private <T extends Identified>  List<T> addToList(Model model, List<T> items, URI entityType, Class<T> identifiedClass) throws SBOLGraphException
	{
		List<Resource> resources=RDFUtil.getResourcesOfType(model, entityType);
		if (resources!=null && resources.size()>0)
		{
			items=new ArrayList<T>();
		}
		for (Resource resource:resources)
		{
			Identified identified=createIdentified(resource, identifiedClass) ;
			items.add((T)identified);
		}
		return items;
	}
}