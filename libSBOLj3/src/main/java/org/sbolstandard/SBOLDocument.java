package org.sbolstandard;

import java.lang.reflect.Constructor;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Resource;
import org.sbolstandard.util.RDFUtil;
import org.sbolstandard.vocabulary.ComponentType;
import org.sbolstandard.vocabulary.Role;

public class SBOLDocument {
	protected Model model = null;
	private URI baseURI=null;

	public Model getModel() {
		return model;
	}
	
	private void setNameSpacePrefixes()
	{
		this.model.setNsPrefix("sbol", "http://sbols.org/v3#");
		this.model.setNsPrefix("so", Role.SO_Namespace);
		this.model.setNsPrefix("sbo", Role.SBO_Namespace);
		this.model.setNsPrefix("go", Role.GO_Namespace);
		this.model.setNsPrefix("chebi", Role.CHEBI_Namespace);
	}

	public SBOLDocument() {
		this.model = ModelFactory.createDefaultModel();
		setNameSpacePrefixes();
	}
	
	public SBOLDocument(URI base) {
		this();
		RDFUtil.setBaseURI(this.model, base);
	}
	
	protected SBOLDocument(Model model) {
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
	
	List<Component> components;

	public List<Component> getComponents() {
		return components;
	}

	public void setComponents(List<Component> components) {
		this.components = components;
	}
	private <T extends Identified>  void addToList(Model model, List<T> items, URI entityType) throws SBOLException
	{
		List<Resource> resources=RDFUtil.getResourcesOfType(model, entityType);
		for (Resource resource:resources)
		{
			Identified sequence=SBOLEntityFactory.create(resource, entityType) ;
			items.add((T)sequence);
		}
	}
	
	public List<Sequence> getSequences() throws SBOLException {
		if (sequences==null)
		{
			sequences = new ArrayList<Sequence>();
			addToList(model, sequences, URI.create("http://sbols.org/v3#Sequence"));	
		}
		return sequences;
	}

	List<Sequence> sequences;

	public Sequence createSequence(URI uri) {
		Sequence sequence = new Sequence(this.model, uri);
		if (sequences == null) {
			sequences = new ArrayList<Sequence>();
		}
		sequences.add(sequence);
		return sequence;
	}
	
	public Component createComponent(URI uri, URI type) {

		Component component = new Component(this.model, uri);
		List<URI> types=new ArrayList<URI>();
		types.add(type);
		component.setTypes(types);
		if (components == null) {
			components = new ArrayList<Component>();
		}
		components.add(component);
		return component;
	}
	
	public <T extends Identified>Identified getIdentified(URI uri, Class<T> identified) throws SBOLGraphException
	{
		Resource res=this.model.getResource(uri.toString());
		try
		{
			Constructor constructor = identified.getDeclaredConstructor( new Class[] {Resource.class});
			Identified entity= (Identified)constructor.newInstance(new Object[]{res});
			return entity;
		}
		catch (Exception ex)
		{
			throw new SBOLGraphException(ex.getMessage());
		}

	}
}