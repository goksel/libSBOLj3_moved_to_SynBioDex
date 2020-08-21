package org.sbolstandard.entity;

import java.lang.reflect.Constructor;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Resource;
import org.sbolstandard.util.URINameSpace;
import org.sbolstandard.util.RDFHandler;
import org.sbolstandard.util.RDFUtil;
import org.sbolstandard.util.SBOLGraphException;
import org.sbolstandard.vocabulary.DataModel;

public class SBOLDocument {
	protected Model model = null;
	
	private List<Component> components;
	private List<Sequence> sequences;
	private List<CombinatorialDerivation> combinatorialDerivations;
	private List<Implementation> implementations;
	private List<ExperimentalData> experimentalDatas;
	private List<org.sbolstandard.entity.Model> models;
	private List<Collection> collections;
	private List<Namespace> namespaces;
	private List<Experiment> experiments;
	private List<Attachment> attachments;
	
	public Model getModel() {
		return model;
	}
	
	private void setNameSpacePrefixes()
	{
		this.model.setNsPrefix(URINameSpace.SBOL.getPrefix(), URINameSpace.SBOL.getUri().toString());
		this.model.setNsPrefix(URINameSpace.SO.getPrefix(), URINameSpace.SO.getUri().toString());
		this.model.setNsPrefix(URINameSpace.SBO.getPrefix(), URINameSpace.SBO.getUri().toString());
		this.model.setNsPrefix(URINameSpace.GO.getPrefix(), URINameSpace.GO.getUri().toString());
		this.model.setNsPrefix(URINameSpace.CHEBI.getPrefix(), URINameSpace.CHEBI.getUri().toString());
		this.model.setNsPrefix(URINameSpace.EDAM.getPrefix(), URINameSpace.EDAM.getUri().toString());
		
	}
	
	public void addNameSpacePrefixes(String prefix, URI namespace)
	{
		if (this.model.getNsURIPrefix(namespace.toString())==null)
		{
			this.model.setNsPrefix(prefix, namespace.toString());	
		}
	}
	
	public void addNameSpacePrefixes(URINameSpace ns)
	{
		addNameSpacePrefixes(ns.getPrefix(), ns.getUri());
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
	
	public List<CombinatorialDerivation> getCombinatorialDerivations() throws SBOLGraphException {
		this.combinatorialDerivations=addToList(model, this.combinatorialDerivations, DataModel.CombinatorialDerivation.uri,CombinatorialDerivation.class);
		return combinatorialDerivations;
	}

	public CombinatorialDerivation createCombinatorialDerivation(URI uri, URI template) throws SBOLGraphException {

		CombinatorialDerivation combinatorialDerivation= new CombinatorialDerivation(this.model, uri);
		combinatorialDerivation.setTemplate(template);
		addToInMemoryList(combinatorialDerivation, combinatorialDerivations);
		return combinatorialDerivation;
	}
	
	
	public List<Implementation> getImplementations() throws SBOLGraphException {
		this.implementations=addToList(model, this.implementations, DataModel.Implementation.uri,Implementation.class);
		return implementations;
	}

	public Implementation createImplementation(URI uri) throws SBOLGraphException {
		Implementation implementation= new Implementation(this.model, uri);
		addToInMemoryList(implementation, implementations);
		return implementation;
	}
	
	public List<ExperimentalData> getExperimentalData() throws SBOLGraphException {
		this.experimentalDatas=addToList(model, this.experimentalDatas, DataModel.ExperimentalData.uri,ExperimentalData.class);
		return experimentalDatas;
	}

	public ExperimentalData createExperimentalData(URI uri) throws SBOLGraphException {
		ExperimentalData experimentalData= new ExperimentalData(this.model, uri);
		addToInMemoryList(experimentalData, experimentalDatas);
		return experimentalData;
	}
	
	
	public List<org.sbolstandard.entity.Model> getModels() throws SBOLGraphException {
		this.models=addToList(model, this.models, DataModel.Model.uri,org.sbolstandard.entity.Model.class);
		return models;
	}

	public org.sbolstandard.entity.Model createModel(URI uri, URI source, URI framework, URI language) throws SBOLGraphException {
		org.sbolstandard.entity.Model model= new org.sbolstandard.entity.Model(this.model, uri);
		model.setSource(source);
		model.setFramework(framework);
		model.setLanguage(language);
		addToInMemoryList(model, models);
		return model;
	}
	
	public List<Collection> getCollections() throws SBOLGraphException {
		this.collections=addToList(model, this.collections, DataModel.Collection.uri,Collection.class);
		return collections;
	}

	public Collection createCollection(URI uri) throws SBOLGraphException {
		Collection collection= new Collection(this.model, uri);
		addToInMemoryList(collection, collections);
		return collection;
	}
	
	public List<Namespace> getNamespaces() throws SBOLGraphException {
		this.namespaces=addToList(model, this.namespaces, DataModel.Namespace.uri,Namespace.class);
		return namespaces;
	}

	public Collection createNamespace(URI uri) throws SBOLGraphException {
		Namespace namespace= new Namespace(this.model, uri);
		addToInMemoryList(namespace, namespaces);
		return namespace;
	}
	
	public List<Experiment> getExperiments() throws SBOLGraphException {
		this.experiments=addToList(model, this.experiments, DataModel.Experiment.uri,Experiment.class);
		return experiments;
	}

	public Experiment createExperiment(URI uri) throws SBOLGraphException {
		Experiment experiment= new Experiment(this.model, uri);
		addToInMemoryList(experiment, experiments);
		return experiment;
	}
	
	public List<Attachment> getAttachments() throws SBOLGraphException {
		this.attachments=addToList(model, this.attachments, DataModel.Attachment.uri,Attachment.class);
		return attachments;
	}

	public Attachment createAttachment(URI uri, URI source) throws SBOLGraphException {
		Attachment attachment= new Attachment(this.model, uri);
		attachment.setSource(source);
		addToInMemoryList(attachment, attachments);
		return attachment;
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
	
	private <T extends Identified> void addToInMemoryList(T item, List<T> list)
	{
		if (list == null) {
			list = new ArrayList<T>();
		}
		list.add(item);
	}
	
	private Set<URI> topLevelResourceTypes;
	public Set<URI> getTopLevelResourceTypes()
	{
		if (topLevelResourceTypes==null)
		{
			List<URI> types = Arrays.asList(DataModel.Component.uri,
					DataModel.Sequence.uri,
					DataModel.Model.uri,
					DataModel.Implementation.uri,
					DataModel.ExperimentalData.uri,
					DataModel.Attachment.uri,
					DataModel.Collection.uri,
					DataModel.Namespace.uri,
					DataModel.CombinatorialDerivation.uri					
					);
			topLevelResourceTypes=new HashSet<URI>(types);
		}
		return topLevelResourceTypes;
	}
	
	public void addTopLevelResourceType(URI type)
	{
		getTopLevelResourceTypes();
		if (type!=null)
		{
			topLevelResourceTypes.add(type);
		}
	}

	
}
