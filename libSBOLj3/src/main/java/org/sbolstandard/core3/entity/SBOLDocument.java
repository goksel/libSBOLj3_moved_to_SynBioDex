package org.sbolstandard.core3.entity;

import java.lang.reflect.Constructor;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.lang3.tuple.MutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.query.Syntax;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;
import org.sbolstandard.core3.api.SBOLAPI;
import org.sbolstandard.core3.entity.measure.BinaryPrefix;
import org.sbolstandard.core3.entity.measure.Prefix;
import org.sbolstandard.core3.entity.measure.PrefixedUnit;
import org.sbolstandard.core3.entity.measure.SIPrefix;
import org.sbolstandard.core3.entity.measure.SingularUnit;
import org.sbolstandard.core3.entity.measure.Unit;
import org.sbolstandard.core3.entity.measure.UnitDivision;
import org.sbolstandard.core3.entity.measure.UnitExponentiation;
import org.sbolstandard.core3.entity.measure.UnitMultiplication;
import org.sbolstandard.core3.entity.provenance.*;
import org.sbolstandard.core3.util.Configuration;
import org.sbolstandard.core3.util.RDFUtil;
import org.sbolstandard.core3.util.SBOLGraphException;
import org.sbolstandard.core3.util.SBOLUtil;
import org.sbolstandard.core3.util.URINameSpace;
import org.sbolstandard.core3.validation.IdentifiedValidator;
import org.sbolstandard.core3.validation.ValidSBOLEntity;
import org.sbolstandard.core3.validation.ValidatableSBOLEntity;
import org.sbolstandard.core3.validation.ValidationMessage;
import org.sbolstandard.core3.vocabulary.ComponentType;
import org.sbolstandard.core3.vocabulary.DataModel;
import org.sbolstandard.core3.vocabulary.Encoding;
import org.sbolstandard.core3.vocabulary.MeasureDataModel;
import org.sbolstandard.core3.vocabulary.ProvenanceDataModel;
import org.sbolstandard.core3.vocabulary.Role;
import org.sbolstandard.core3.vocabulary.VariableFeatureCardinality;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

@ValidSBOLEntity
public class SBOLDocument implements ValidatableSBOLEntity {
	protected Model model = null;
	/*@NotNull (message="SBOLDocument.test cannot be null")
	private URI test=null;*/
	private List<Component> components;
	private List<Sequence> sequences;
	private List<CombinatorialDerivation> combinatorialDerivations;
	private List<Implementation> implementations;
	private List<ExperimentalData> experimentalDatas;
	private List<org.sbolstandard.core3.entity.Model> models;
	private List<Collection> collections;
	private List<Experiment> experiments;
	private List<Attachment> attachments;
	private List<Agent> agents;
	private List<Plan> plans;
	private List<Activity> activities;
	private List<SIPrefix> siPrefixes;
	private List<BinaryPrefix> binaryPrefixes;
	private List<SingularUnit> singularUnits;
	private List<UnitMultiplication> unitMultiplications;
	private List<UnitDivision> unitDivisions;
	private List<UnitExponentiation> unitExponentiations;
	private List<PrefixedUnit> prefixedUnits;
	private List<TopLevelMetadata> metadataList;
	
	
	public Model getRDFModel() {
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
		this.model.setNsPrefix(URINameSpace.PROV.getPrefix(), URINameSpace.PROV.getUri().toString());
		this.model.setNsPrefix(URINameSpace.OM.getPrefix(), URINameSpace.OM.getUri().toString());
		this.model.setNsPrefix(URINameSpace.RDFS.getPrefix(), URINameSpace.RDFS.getUri().toString());
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
	
	public void setBaseURI(URI base)
	{
		RDFUtil.setBaseURI(this.model, base);
	}
	
	@Valid
	public List<Component> getComponents() throws SBOLGraphException {
		this.components=addToList(model, this.components, DataModel.Component.uri,Component.class);
		return components;
	}
	
	public Component createComponent(URI uri, URI namespace, List<URI> types) throws SBOLGraphException {

		Component component = new Component(this.model, uri);
		component.setTypes(types);
		component.setNamespace(namespace);
		addToInMemoryList(component, components);
		return component;
	}
	
	public Component createComponent(String displayId, List<URI> types) throws SBOLGraphException {
		if (this.getBaseURI()!=null)
		{
			return createComponent(SBOLAPI.append(this.getBaseURI(), displayId), SBOLUtil.toNameSpace(this.getBaseURI()), types);
		}
		else
		{
			throw new SBOLGraphException("Display ids can be used to construct entities only if the base URI property of the document is set. Displayid:" + displayId);
		}
	}
	
	@Valid
	public List<Sequence> getSequences() throws SBOLGraphException {
		this.sequences=addToList(model, this.sequences, DataModel.Sequence.uri,Sequence.class);
		return sequences;
	}
	
	/*public List<Sequence> getSequences(Encoding encoding) throws SBOLGraphException {
		List<Sequence> filteredSequences=null;		
		getSequences();
		if (sequences!=null)
		{
			List<URI> uris=getURIs(sequences);
			List<URI> filteredUris=this.filterIdentifieds(uris, DataModel.Sequence.encoding, encoding.getUri().toString());
			for (Sequence sequence:sequences)
			{
				if (filteredUris.contains(sequence.getUri()))
				{
					if (filteredSequences==null)
					{
						filteredSequences=new ArrayList<Sequence>();
					}
					filteredSequences.add(sequence);
				}
			}
		}
		return filteredSequences;
	}*/
	
	
	public List<Sequence> getSequences(Encoding encoding) throws SBOLGraphException {
		getSequences();
		
		List<Sequence> filteredSequences=filterIdentifiedItems(this.sequences, DataModel.Sequence.encoding, encoding.getUri().toString());
		return filteredSequences;
	}
	
	public <T extends Identified> List<T> filterIdentifiedItems(List<T> identifieds, URI property, String value) throws SBOLGraphException {
		List<T> filteredIdentifieds=null;		
		if (identifieds!=null)
		{
			List<URI> uris=SBOLUtil.getURIs(identifieds);
			List<URI> filteredUris=this.filterIdentifieds(uris, property, value);
			for (T identified:identifieds)
			{
				if (filteredUris.contains(identified.getUri()))
				{
					if (filteredIdentifieds==null)
					{
						filteredIdentifieds=new ArrayList<T>();
					}
					filteredIdentifieds.add(identified);
				}
				/*else
				{
					throw new SBOLGraphException(String.format("Could not filter items. Property:%s Value:%s", property, value));
				}*/
			}
		}
		return filteredIdentifieds;
	}
	
	
	public Sequence createSequence(URI uri, URI namespace) throws SBOLGraphException {
		Sequence sequence = new Sequence(this.model, uri);
		sequence.setNamespace(namespace);
		addToInMemoryList(sequence, sequences);
		return sequence;
	}
	
	public Sequence createSequence(String displayId) throws SBOLGraphException {
		if (this.getBaseURI()!=null)
		{
			return createSequence(SBOLAPI.append(this.getBaseURI(), displayId), SBOLUtil.toNameSpace(this.getBaseURI()));
		}
		else
		{
			throw new SBOLGraphException("Display ids can be used to construct entities only if the base URI property of the document is set. Displayid:" + displayId);
		}
	}

	@Valid
	public List<CombinatorialDerivation> getCombinatorialDerivations() throws SBOLGraphException {
		this.combinatorialDerivations=addToList(model, this.combinatorialDerivations, DataModel.CombinatorialDerivation.uri,CombinatorialDerivation.class);
		return combinatorialDerivations;
	}

	public CombinatorialDerivation createCombinatorialDerivation(URI uri, URI namespace, Component template) throws SBOLGraphException {

		CombinatorialDerivation combinatorialDerivation= new CombinatorialDerivation(this.model, uri);
		combinatorialDerivation.setTemplate(template);
		combinatorialDerivation.setNamespace(namespace);
		addToInMemoryList(combinatorialDerivation, combinatorialDerivations);
		return combinatorialDerivation;
	}
	
	public CombinatorialDerivation createCombinatorialDerivation(String displayId, Component template) throws SBOLGraphException {
		if (this.getBaseURI()!=null)
		{
			return createCombinatorialDerivation(SBOLAPI.append(this.getBaseURI(), displayId), SBOLUtil.toNameSpace(this.getBaseURI()), template);
		}
		else
		{
			throw new SBOLGraphException("Display ids can be used to construct entities only if the base URI property of the document is set. Displayid:" + displayId);
		}
	}
	
	@Valid	
	public List<Implementation> getImplementations() throws SBOLGraphException {
		this.implementations=addToList(model, this.implementations, DataModel.Implementation.uri,Implementation.class);
		return implementations;
	}

	public Implementation createImplementation(URI uri, URI namespace) throws SBOLGraphException {
		Implementation implementation= new Implementation(this.model, uri);
		implementation.setNamespace(namespace);
		addToInMemoryList(implementation, implementations);
		return implementation;
	}
	
	public Implementation createImplementation(String displayId) throws SBOLGraphException {
		if (this.getBaseURI()!=null)
		{
			return createImplementation(SBOLAPI.append(this.getBaseURI(), displayId), SBOLUtil.toNameSpace(this.getBaseURI()));
		}
		else
		{
			throw new SBOLGraphException("Display ids can be used to construct entities only if the base URI property of the document is set. Displayid:" + displayId);
		}
	}
	
	@Valid
	public List<ExperimentalData> getExperimentalData() throws SBOLGraphException {
		this.experimentalDatas=addToList(model, this.experimentalDatas, DataModel.ExperimentalData.uri,ExperimentalData.class);
		return experimentalDatas;
	}
	

	public ExperimentalData createExperimentalData(URI uri, URI namespace) throws SBOLGraphException {
		ExperimentalData experimentalData= new ExperimentalData(this.model, uri);
		experimentalData.setNamespace(namespace);
		addToInMemoryList(experimentalData, experimentalDatas);
		return experimentalData;
	}
	
	public ExperimentalData createExperimentalData(String displayId) throws SBOLGraphException {
		if (this.getBaseURI()!=null)
		{
			return createExperimentalData(SBOLAPI.append(this.getBaseURI(), displayId), SBOLUtil.toNameSpace(this.getBaseURI()));
		}
		else
		{
			throw new SBOLGraphException("Display ids can be used to construct entities only if the base URI property of the document is set. Displayid:" + displayId);
		}
	}
	
	@Valid
	public List<org.sbolstandard.core3.entity.Model> getModels() throws SBOLGraphException {
		this.models=addToList(model, this.models, DataModel.Model.uri,org.sbolstandard.core3.entity.Model.class);
		return models;
	}

	public org.sbolstandard.core3.entity.Model createModel(URI uri, URI namespace, URI source, URI framework, URI language) throws SBOLGraphException {
		org.sbolstandard.core3.entity.Model model= new org.sbolstandard.core3.entity.Model(this.model, uri);
		model.setNamespace(namespace);
		model.setSource(source);
		model.setFramework(framework);
		model.setLanguage(language);
		addToInMemoryList(model, models);
		return model;
	}
	
	public org.sbolstandard.core3.entity.Model createModel(String displayId, URI source, URI framework, URI language) throws SBOLGraphException {
		if (this.getBaseURI()!=null)
		{
			return createModel(SBOLAPI.append(this.getBaseURI(), displayId), SBOLUtil.toNameSpace(this.getBaseURI()), source, framework, language);
		}
		else
		{
			throw new SBOLGraphException("Display ids can be used to construct entities only if the base URI property of the document is set. Displayid:" + displayId);
		}
	}
	
	@Valid
	public List<Collection> getCollections() throws SBOLGraphException {
		this.collections=addToList(model, this.collections, DataModel.Collection.uri,Collection.class);
		return collections;
	}

	public Collection createCollection(URI uri, URI namespace) throws SBOLGraphException {
		Collection collection= new Collection(this.model, uri);
		collection.setNamespace(namespace);
		addToInMemoryList(collection, collections);
		return collection;
	}
	
	public Collection createCollection(String displayId) throws SBOLGraphException {
		if (this.getBaseURI()!=null)
		{
			return createCollection(SBOLAPI.append(this.getBaseURI(), displayId), SBOLUtil.toNameSpace(this.getBaseURI()));
		}
		else
		{
			throw new SBOLGraphException("Display ids can be used to construct entities only if the base URI property of the document is set. Displayid:" + displayId);
		}
	}
	
	@Valid	
	public List<Experiment> getExperiments() throws SBOLGraphException {
		this.experiments=addToList(model, this.experiments, DataModel.Experiment.uri,Experiment.class);
		return experiments;
	}

	public Experiment createExperiment(URI uri, URI namespace) throws SBOLGraphException {
		Experiment experiment= new Experiment(this.model, uri);
		experiment.setNamespace(namespace);
		addToInMemoryList(experiment, experiments);
		return experiment;
	}
	
	public Experiment createExperiment(String displayId) throws SBOLGraphException {
		if (this.getBaseURI()!=null)
		{
			return createExperiment(SBOLAPI.append(this.getBaseURI(), displayId), SBOLUtil.toNameSpace(getBaseURI()));
		}
		else
		{
			throw new SBOLGraphException("Display ids can be used to construct entities only if the base URI property of the document is set. Displayid:" + displayId);
		}
	}
	
	@Valid
	public List<Attachment> getAttachments() throws SBOLGraphException {
		this.attachments=addToList(model, this.attachments, DataModel.Attachment.uri,Attachment.class);
		return attachments;
	}

	public Attachment createAttachment(URI uri, URI namespace, URI source) throws SBOLGraphException {
		Attachment attachment= new Attachment(this.model, uri);
		attachment.setNamespace(namespace);
		attachment.setSource(source);
		addToInMemoryList(attachment, attachments);
		return attachment;
	}
	
	public Attachment createAttachment(String displayId, URI source) throws SBOLGraphException {
		if (this.getBaseURI()!=null)
		{
			return createAttachment(SBOLAPI.append(this.getBaseURI(), displayId),SBOLUtil.toNameSpace(this.getBaseURI()), source);
		}
		else
		{
			throw new SBOLGraphException("Display ids can be used to construct entities only if the base URI property of the document is set. Displayid:" + displayId);
		}
	}
	
	
	public Agent createAgent(URI uri, URI namespace) throws SBOLGraphException {

		Agent agent = new Agent(this.model, uri) {};
		agent.setNamespace(namespace);
		addToInMemoryList(agent, agents);
		return agent;
	}
	
	public Agent createAgent(String displayId) throws SBOLGraphException {
		if (this.getBaseURI()!=null)
		{
			return createAgent(SBOLAPI.append(this.getBaseURI(), displayId), SBOLUtil.toNameSpace(getBaseURI()));
		}
		else
		{
			throw new SBOLGraphException("Display ids can be used to construct entities only if the base URI property of the document is set. Displayid:" + displayId);
		}
	}
	
	@Valid
	public List<Agent> getAgents() throws SBOLGraphException {
		this.agents=addToList(model, this.agents, ProvenanceDataModel.Agent.uri,Agent.class);
		return agents;
	}
	
	public Plan createPlan(URI uri, URI namespace) throws SBOLGraphException {
		Plan plan = new Plan(this.model, uri) {};
		plan.setNamespace(namespace);
		addToInMemoryList(plan, plans);
		return plan;
	}
	
	public Plan createPlan(String displayId) throws SBOLGraphException {
		if (this.getBaseURI()!=null)
		{
			return createPlan(SBOLAPI.append(this.getBaseURI(), displayId), SBOLUtil.toNameSpace(getBaseURI()));
		}
		else
		{
			throw new SBOLGraphException("Display ids can be used to construct entities only if the base URI property of the document is set. Displayid:" + displayId);
		}
	}
	
	@Valid
	public List<Plan> getPlans() throws SBOLGraphException {
		this.plans=addToList(model, this.plans, ProvenanceDataModel.Plan.uri,Plan.class);
		return plans;
	}
	
	public Activity createActivity(URI uri, URI namespace) throws SBOLGraphException {

		Activity activity = new Activity(this.model, uri) {};
		activity.setNamespace(namespace);
		addToInMemoryList(activity, activities);
		return activity;
	}
	
	public Activity createActivity(String displayId) throws SBOLGraphException {
		if (this.getBaseURI()!=null)
		{
			return createActivity(SBOLAPI.append(this.getBaseURI(), displayId), SBOLUtil.toNameSpace(getBaseURI()));
		}
		else
		{
			throw new SBOLGraphException("Display ids can be used to construct entities only if the base URI property of the document is set. Displayid:" + displayId);
		}
	}
	
	@Valid
	public List<Activity> getActivities() throws SBOLGraphException {
		this.activities=addToList(model, this.activities, ProvenanceDataModel.Activity.uri,Activity.class);
		return activities;
	}
	
	private void initialisePrefix(Prefix prefix, String symbol, String name, float factor) throws SBOLGraphException{
		prefix.setSymbol(symbol);
		prefix.setLabel(name);
		prefix.setFactor(Optional.of(factor));
	}
	
	public SIPrefix createSIPrefix(URI uri, URI namespace, String symbol, String name, float factor) throws SBOLGraphException {		
		SIPrefix prefix = new SIPrefix(this.model, uri) {};
		prefix.setNamespace(namespace);
		initialisePrefix(prefix, symbol, name, factor);
		addToInMemoryList(prefix, siPrefixes);
		return prefix;
	}
	
	public SIPrefix createSIPrefix(String displayId, String symbol, String name, float factor) throws SBOLGraphException {
		if (this.getBaseURI()!=null)
		{
			return createSIPrefix(SBOLAPI.append(this.getBaseURI(), displayId), SBOLUtil.toNameSpace(getBaseURI()), symbol, name, factor);
		}
		else
		{
			throw new SBOLGraphException("Display ids can be used to construct entities only if the base URI property of the document is set. Displayid:" + displayId);
		}
	}
	
	@Valid
	public List<SIPrefix> getSIPrefixes() throws SBOLGraphException {
		this.siPrefixes=addToList(model, this.siPrefixes, MeasureDataModel.SIPrefix.uri,SIPrefix.class);
		return siPrefixes;
	}
	
	public BinaryPrefix createBinaryPrefix(URI uri, URI namespace, String symbol, String name, float factor) throws SBOLGraphException {

		BinaryPrefix prefix = new BinaryPrefix(this.model, uri) {};
		prefix.setNamespace(namespace);
		initialisePrefix(prefix, symbol, name, factor);
		addToInMemoryList(prefix, binaryPrefixes);
		return prefix;
	}
	
	public BinaryPrefix createBinaryPrefix(String displayId, String symbol, String name, float factor) throws SBOLGraphException {
		if (this.getBaseURI()!=null)
		{
			return createBinaryPrefix(SBOLAPI.append(this.getBaseURI(), displayId), SBOLUtil.toNameSpace(getBaseURI()), symbol, name, factor);
		}
		else
		{
			throw new SBOLGraphException("Display ids can be used to construct entities only if the base URI property of the document is set. Displayid:" + displayId);
		}
	}
	
	@Valid
	public List<BinaryPrefix> getBinaryPrefixes() throws SBOLGraphException {
		this.binaryPrefixes=addToList(model, this.binaryPrefixes, MeasureDataModel.BinaryPrefix.uri,BinaryPrefix.class);
		return binaryPrefixes;
	}
	
	public SingularUnit createSingularUnit(URI uri, URI namespace, String symbol, String name) throws SBOLGraphException {

		SingularUnit unit = new SingularUnit(this.model, uri) {};
		unit.setNamespace(namespace);
		unit.setSymbol(symbol);
		unit.setLabel(name);
		addToInMemoryList(unit, singularUnits);
		return unit;
	}
	
	public SingularUnit createSingularUnit(String displayId, String symbol, String name) throws SBOLGraphException {
		if (this.getBaseURI()!=null)
		{
			return createSingularUnit(SBOLAPI.append(this.getBaseURI(), displayId), SBOLUtil.toNameSpace(getBaseURI()), symbol, name);
		}
		else
		{
			throw new SBOLGraphException("Display ids can be used to construct entities only if the base URI property of the document is set. Displayid:" + displayId);
		}
	}
	
	@Valid
	public List<SingularUnit> getSingularUnits() throws SBOLGraphException {
		this.singularUnits=addToList(model, this.singularUnits, MeasureDataModel.SingularUnit.uri,SingularUnit.class);
		return singularUnits;
	}
	
	public UnitMultiplication createUnitMultiplication(URI uri, URI namespace, String symbol, String name, Unit unit1, Unit unit2) throws SBOLGraphException {

		UnitMultiplication unit = new UnitMultiplication(this.model, uri) {};
		unit.setNamespace(namespace);
		unit.setSymbol(symbol);
		unit.setLabel(name);
		unit.setTerm1(unit1);
		unit.setTerm2(unit2);
		addToInMemoryList(unit, unitMultiplications);
		return unit;
	}
	
	public UnitMultiplication createUnitMultiplication(String displayId, String symbol, String name, Unit unit1, Unit unit2) throws SBOLGraphException {
		if (this.getBaseURI()!=null)
		{
			return createUnitMultiplication(SBOLAPI.append(this.getBaseURI(), displayId), SBOLUtil.toNameSpace(this.getBaseURI()), symbol, name, unit1, unit2);
		}
		else
		{
			throw new SBOLGraphException("Display ids can be used to construct entities only if the base URI property of the document is set. Displayid:" + displayId);
		}
	}
	
	@Valid
	public List<UnitMultiplication> getUnitMultiplications() throws SBOLGraphException {
		this.unitMultiplications=addToList(model, this.unitMultiplications, MeasureDataModel.UnitMultiplication.uri,UnitMultiplication.class);
		return unitMultiplications;
	}
	
	public UnitDivision createUnitDivision(URI uri, URI namespace, String symbol, String name, Unit numerator, Unit denominator) throws SBOLGraphException {

		UnitDivision unit = new UnitDivision(this.model, uri) {};
		unit.setNamespace(namespace);
		unit.setSymbol(symbol);
		unit.setLabel(name);
		unit.setNumerator(numerator);
		unit.setDenominator(denominator);
		addToInMemoryList(unit, unitDivisions);
		return unit;
	}
	
	public UnitDivision createUnitDivision(String displayId, String symbol, String name, Unit numerator, Unit denominator) throws SBOLGraphException {
		if (this.getBaseURI()!=null)
		{
			return createUnitDivision(SBOLAPI.append(this.getBaseURI(), displayId), SBOLUtil.toNameSpace(this.getBaseURI()), symbol, name, numerator, denominator);
		}
		else
		{
			throw new SBOLGraphException("Display ids can be used to construct entities only if the base URI property of the document is set. Displayid:" + displayId);
		}
	}
	
	@Valid
	public List<UnitDivision> getUnitDivisions() throws SBOLGraphException {
		this.unitDivisions=addToList(model, this.unitDivisions, MeasureDataModel.UnitDivision.uri,UnitDivision.class);
		return unitDivisions;
	}
	
	public UnitExponentiation createUnitExponentiation(URI uri, URI namespace, String symbol, String name, Unit baseUnit, int exponent) throws SBOLGraphException {

		UnitExponentiation unit = new UnitExponentiation(this.model, uri) {};
		unit.setNamespace(namespace);
		unit.setSymbol(symbol);
		unit.setLabel(name);
		unit.setBase(baseUnit);
		unit.setExponent(Optional.of(exponent));
		addToInMemoryList(unit, unitExponentiations);
		return unit;
	}
	
	public UnitExponentiation createUnitExponentiation(String displayId, String symbol, String name, Unit baseUnit, int exponent) throws SBOLGraphException {
		if (this.getBaseURI()!=null)
		{
			return createUnitExponentiation(SBOLAPI.append(this.getBaseURI(), displayId), SBOLUtil.toNameSpace(this.getBaseURI()), symbol, name, baseUnit, exponent);
		}
		else
		{
			throw new SBOLGraphException("Display ids can be used to construct entities only if the base URI property of the document is set. Displayid:" + displayId);
		}
	}
	
	@Valid
	public List<UnitExponentiation> getUnitExponentiations() throws SBOLGraphException {
		this.unitExponentiations=addToList(model, this.unitExponentiations, MeasureDataModel.UnitExponentiation.uri,UnitExponentiation.class);
		return unitExponentiations;
	}
	
	public PrefixedUnit createPrexiedUnit(URI uri, URI namespace, String symbol, String name, Unit unit, Prefix prefix) throws SBOLGraphException {

		PrefixedUnit prefixedUnit = new PrefixedUnit(this.model, uri) {};
		prefixedUnit.setNamespace(namespace);
		prefixedUnit.setSymbol(symbol);
		prefixedUnit.setLabel(name);
		prefixedUnit.setUnit(unit);
		prefixedUnit.setPrefix(prefix);
		addToInMemoryList(prefixedUnit, prefixedUnits);
		return prefixedUnit;
	}
	
	public PrefixedUnit createPrexiedUnit(String displayId, String symbol, String name, Unit unit, Prefix prefix) throws SBOLGraphException {
		if (this.getBaseURI()!=null)
		{
			return createPrexiedUnit(SBOLAPI.append(this.getBaseURI(), displayId), SBOLUtil.toNameSpace(this.getBaseURI()), symbol, name, unit, prefix);
		}
		else
		{
			throw new SBOLGraphException("Display ids can be used to construct entities only if the base URI property of the document is set. Displayid:" + displayId);
		}
	}
	
	@Valid
	public List<PrefixedUnit> getPrefixedUnits() throws SBOLGraphException {
		this.prefixedUnits=addToList(model, this.prefixedUnits, MeasureDataModel.PrefixedUnit.uri,PrefixedUnit.class);
		return prefixedUnits;
	}
	
	public TopLevelMetadata createMetadata(URI uri, URI namespace, URI dataType) throws SBOLGraphException
	{
		if (dataType==null)
		{
			throw new SBOLGraphException("Application specific types MUST have a datatype property specified. " + "Metadata URI:" + uri);
		}
		TopLevelMetadata metadata=new TopLevelMetadata(this.model, uri);
		metadata.addAnnotationType(dataType);
		metadata.setNamespace(namespace);
		return metadata;
	}
	
	public TopLevelMetadata createMetadata(String displayId, URI dataType) throws SBOLGraphException
	{
		if (this.getBaseURI()!=null)
		{
			return createMetadata(SBOLAPI.append(this.getBaseURI(), displayId), SBOLUtil.toNameSpace(this.getBaseURI()), dataType);
		}
		else
		{
			throw new SBOLGraphException("Display ids can be used to construct entities only if the base URI property of the document is set. Displayid:" + displayId);
		}
	}

	public List<TopLevelMetadata> getTopLevelMetadataList(URI metaDataType) throws SBOLGraphException {
		return addToList(model, this.metadataList, metaDataType,TopLevelMetadata.class);
	}
	
	/*public Measure createMeasure(URI uri, float value, URI unit) throws SBOLGraphException {

		Measure measure = new Measure(this.model, uri) {};
		measure.setValue(value);
		measure.setUnit(unit);
		if (measures== null) {
			measures = new ArrayList<Measure>();
		}
		measures.add(measure);
		return measure;
	}
	*/
	/*public List<Measure> getMeasures() throws SBOLGraphException {
		this.measures=addToList(model, this.measures, MeasureDataModel.Measure.uri,Measure.class);
		return measures;
	}
	*/
	
	
	/*Keep this for now. The usage: 	
	 * Sequence i13504Sequence1= (Sequence)doc.getIdentified(device.getSequences().get(0),Sequence.class);
	
	 * public <T extends Identified>Identified getIdentified(URI uri, Class<T> identified) throws SBOLGraphException
	{
		Resource res=this.model.getResource(uri.toString());
		try
		{
			Constructor<T> constructor = identified.getDeclaredConstructor( new Class[] {Resource.class});
			if (!constructor.isAccessible())
			{
				constructor.setAccessible(true);
			}
			Identified entity= (Identified)constructor.newInstance(new Object[]{res});
			return entity;
		}
		catch (Exception ex)
		{
			throw new SBOLGraphException(ex.getMessage());
		}

	}*/
	
	
	public <T extends Identified> T getIdentified(URI uri, Class<T> identified) throws SBOLGraphException
	{
		Resource res=this.model.getResource(uri.toString());
		try
		{
			Constructor<T> constructor = identified.getDeclaredConstructor( new Class[] {Resource.class});
			if (!constructor.isAccessible())
			{
				constructor.setAccessible(true);
			}
			Identified entity= (Identified)constructor.newInstance(new Object[]{res});
			
			URI type=entity.getResourceType();
			boolean hasType=RDFUtil.hasType(model, res, type);
			
			if (hasType)
			{
				return (T)entity;
			}
			else
			{
				return null;
			}	
		}
		catch (Exception ex)
		{
			throw new SBOLGraphException(ex.getMessage());
		}

	}
	
	
	
	/*public <T extends Identified>Identified getIdentified2(URI uri) throws SBOLGraphException
	{
		Resource res=this.model.getResource(uri.toString());
		try
		{
			Constructor<T> constructor = this.identified.getDeclaredConstructor( new Class[] {Resource.class});
			if (!constructor.isAccessible())
			{
				constructor.setAccessible(true);
			}
			Identified entity= (Identified)constructor.newInstance(new Object[]{res});
			return entity;
		}
		catch (Exception ex)
		{
			throw new SBOLGraphException(ex.getMessage());
		}

	}*/
	
	private String constructQuery(String search)
	{
		StringBuilder query=new StringBuilder();
		for (Entry<String, String> ns:this.model.getNsPrefixMap().entrySet())
		{
			String prefix=ns.getKey();
			String prefixUri=ns.getValue();
			if (prefix!=null && prefix!="")
			{
				query.append(String.format("PREFIX %s: <%s>", prefix, prefixUri));
				query.append(System.lineSeparator());
			}
		}
		query.append("SELECT ?identified" + System.lineSeparator());
		query.append("WHERE {" + System.lineSeparator()); 
			//query=query + "  ?identified a sbol:Component ." + System.lineSeparator();identified.
		query.append(search);
		query.append(System.lineSeparator());
		query.append("}");
		return query.toString();
	}
	
	
	public <T extends Identified> List<T> getIdentifieds(String search, Class<T> identified) throws SBOLGraphException
	{
		List<T> items=new ArrayList<T>();
		ResultSet rs=null;
		try
		{
			String query=constructQuery(search);
	   	        rs=RDFUtil.executeSPARQLSelectQuery(this.model, query, Syntax.syntaxSPARQL_11);
	        if (rs!=null)
	        {
	        	String resultColumnName=rs.getResultVars().get(0);
		        while (rs.hasNext())
	 	        {
	 	            QuerySolution solution=rs.next();
	 	            RDFNode node=solution.get(resultColumnName);
	 	            if (node.isResource())
	 	            {
	 	                String found=node.asResource().getURI();
	 	                Identified item=getIdentified(URI.create(found), identified);
	 	                items.add((T)item);
	 	            }
	 	        }
	        }
		}
		catch (Exception ex)
		{
			throw new SBOLGraphException(ex.getMessage());
		}
		return items;
	}
	
	
	protected <T extends Identified> Identified createIdentified(Resource res, Class<T> identified) throws SBOLGraphException
	{
		try
		{
			Constructor<T> constructor = identified.getDeclaredConstructor( new Class[] {Resource.class});
			Identified entity=null;
			if (!constructor.isAccessible())
			{
				constructor.setAccessible(true);
			}
			entity= (Identified)constructor.newInstance(new Object[]{res});
			return entity;
		}
		catch (Exception ex)
		{
			throw new SBOLGraphException(ex.getMessage());
		}
	}

	private <T extends Identified>  List<T> addToList(Model model, List<T> items, URI entityType, Class<T> identifiedClass) throws SBOLGraphException
	{
		if (items==null)
		{
			List<Resource> resources=RDFUtil.getResourcesOfType(model, entityType);
			if (resources!=null && resources.size()>0)
			{
				items=new ArrayList<T>();
				for (Resource resource:resources)
				{
					Identified identified=createIdentified(resource, identifiedClass) ;
					items.add((T)identified);
				}
			}
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
					DataModel.CombinatorialDerivation.uri,
					DataModel.TopLevel.uri,
					ProvenanceDataModel.Agent.uri,
					ProvenanceDataModel.Plan.uri,
					ProvenanceDataModel.Activity.uri,
					MeasureDataModel.Measure.uri,
					MeasureDataModel.SIPrefix.uri,
					MeasureDataModel.BinaryPrefix.uri,
					MeasureDataModel.SingularUnit.uri,
					MeasureDataModel.UnitMultiplication.uri,
					MeasureDataModel.UnitDivision.uri,
					MeasureDataModel.UnitExponentiation.uri,
					MeasureDataModel.PrefixedUnit.uri
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
	
	public List<URI> filterIdentifieds(List<URI> identifieds, URI property, String value)
	{
		return RDFUtil.filterItems(this.getRDFModel(), identifieds, property, value);
	}
	
	public List<TopLevel> getTopLevels() throws SBOLGraphException
	{
		List<TopLevel> topLevels=new ArrayList<TopLevel>();
		addToList(topLevels, this.getComponents());
		addToList(topLevels, this.getActivities());
		addToList(topLevels, this.getAgents());
		addToList(topLevels, this.getAttachments());
		addToList(topLevels, this.getBinaryPrefixes());
		addToList(topLevels, this.getCollections());
		addToList(topLevels, this.getCombinatorialDerivations());
		addToList(topLevels, this.getComponents());
		addToList(topLevels, this.getExperimentalData());
		addToList(topLevels, this.getExperiments());
		addToList(topLevels, this.getImplementations());
		addToList(topLevels, this.getModels());
		addToList(topLevels, this.getPlans());
		addToList(topLevels, this.getPrefixedUnits());
		addToList(topLevels, this.getSequences());
		addToList(topLevels, this.getSingularUnits());
		addToList(topLevels, this.getSIPrefixes());
		addToList(topLevels, this.getUnitDivisions());
		addToList(topLevels, this.getUnitExponentiations());
		addToList(topLevels, this.getUnitMultiplications());
		return topLevels;
	}
	
	private void addToList(List<TopLevel> listA, List<? extends TopLevel> listB)
	{
		if (listB!=null && listB.size()>0)
		{
			listA.addAll(listB);
		}
	}
		
	public List<ValidationMessage> getValidationMessages() throws SBOLGraphException
	{
		List<ValidationMessage> messages=null;
		List<TopLevel> topLevels= getTopLevels();
		if (topLevels!=null)
		{
			for (TopLevel topLevel:topLevels)
			{
				String targetURI=topLevel.getUri().toString() + "/";
				for (TopLevel searchTopLevel:topLevels)
				{
					if (searchTopLevel.getUri().toString().toLowerCase().startsWith(targetURI.toLowerCase()))
					{
				  		ValidationMessage message=new ValidationMessage("{TOPLEVEL_URI_CANNOT_BE_USED_AS_A_PREFIX}", DataModel.TopLevel.uri,searchTopLevel,searchTopLevel.getUri());      
				  		message.childPath(DataModel.TopLevel.uri, topLevel);
				  		messages=IdentifiedValidator.addToValidations(messages, message);
					}
				}
			}
		}
		
		List<Collection> collections=this.getCollections();
		if (collections!=null)
		{
			List<URI> topLevelURIs= SBOLUtil.getURIs(topLevels);
			for (Collection collection:collections)
			{
				List<URI> members=collection.getMembers();
				if (members != null) {
					for (URI member: members){
						if (!topLevelURIs.contains(member)){
							ValidationMessage message = new ValidationMessage("{SBOL_VALID_ENTITY_TYPES}", DataModel.Collection.uri, collection, member);
							message.childPath(DataModel.Collection.member, null);
							messages=IdentifiedValidator.addToValidations(messages, message);
						}
					}
				
				}
			}
		}
		
		if (Configuration.getInstance().isValidateRecommendedRules())
		{
			messages=assertCombDerWasDerivedRestrictionForCollections(messages);			
		}		
		
		messages=assertCombDerVariantCollectionsIncludeComponentMembers(messages);
		messages=assertCombDerRestrictionForComponents(messages);
		
		//COMBINATORIALDERIVATION_VARIABLEFEATURE_NOT_CIRCULAR		
		messages=assertCombDerVariantFeaturesNotCircular(messages);
		
		return messages;
	}
	
	
	private List<ValidationMessage> assertCombDerRestrictionForComponents(List<ValidationMessage> validationMessages) throws SBOLGraphException
	{
		List<Component> components=this.getComponents();
		if (components!=null){
			for (Component component:components){
				List<URI> derivedFroms=component.getWasDerivedFrom();
				if (derivedFroms!=null){
					for (URI uri:derivedFroms){
						CombinatorialDerivation cd= this.getIdentified(uri, CombinatorialDerivation.class);
						if (cd!=null){
							Component template=cd.getTemplate();
							if (template!=null){								
								List<Feature> features=component.getFeatures();
								if (features!=null){
									for (Feature feature:features){
										//Each feature must be derived from a template's feature.
										Set<URI> foundTemplateFeatureURIs=IdentifiedValidator.getMatchingSearchURIs(SBOLUtil.getURIs(template.getFeatures()), feature.getWasDerivedFrom());
										
										//COMBINATORIALDERIVATION_COMPONENT_WASDERIVEDFROM_RESTRICTION
										validationMessages=assertCombDerComponentWasDerivedFromRestriction(validationMessages, component, feature, foundTemplateFeatureURIs);
										
										//COMBINATORIALDERIVATION_COMPONENT_STATIC_FEATUREPROPERTY_RESTRICTION
										validationMessages=assertCombDerComponentStaticFeaturePropertyRestriction(validationMessages, cd, component, feature, foundTemplateFeatureURIs);
									
										//COMBINATORIALDERIVATION_COMPONENT_SUBCOMPONENT_INSTANCEOF_VALID
										validationMessages = assertCombDerSubComponentInstanceOfValuesAreValid(validationMessages, cd, component, feature);
										
										//COMBINATORIALDERIVATION_COMPONENT_FEATURE_CONSTRAINT_RESTRICTION = sbol3-12113  If the prov:wasDerivedFrom property of a Component refers to a CombinatorialDerivation and the template Component of the CombinatorialDerivation contains Constraint objects, then for any Feature contained by the Component that has a prov:wasDerivedFrom property that refers to the subject or object Feature of any of the template Constraint objects, that feature MUST adhere to the restriction properties of the template Constraint objects.
										validationMessages=assertCombDerComponentFeatureConstraintRestriction(validationMessages, component, template, feature, foundTemplateFeatureURIs);

										//COMBINATORIALDERIVATION_COMPONENT_FEATURE_ROLE_RESTRICTION = sbol3-12114 - If the prov:wasDerivedFrom property of a Component refers to a CombinatorialDerivation, then for any Feature in the Component with a prov:wasDerivedFrom property referring to a variable Feature in the template Component of the CombinatorialDerivation, then the role properties of that Feature SHOULD contain all URIs contained by the role properties of the template Feature.
										validationMessages=assertCombDerComponentFeatureRoleRestriction(validationMessages, component, template, feature, foundTemplateFeatureURIs);
										
										//COMBINATORIALDERIVATION_COMPONENT_FEATURE_TYPE_RESTRICTION = sbol3-12115 - Let the type-determining referent of a Feature be the Feature itself for a LocalSubComponent or ExternallyDefined, the Component referred by the instanceOf property of a SubComponent and the type-determining referent of the Feature referred to be a ComponentReference. If the prov:wasDerivedFrom property of a Component refers to a CombinatorialDerivation, then for any Feature in the Component with a prov:wasDerivedFrom property referring to a variable Feature in the template Component of the CombinatorialDerivation, then the type properties of the Feature\u2019s type-determining referent SHOULD contain all URIs contained by the type properties of the template Feature\u2019s type-determining referent.
										validationMessages=assertCombDerComponentFeatureTypeRestriction(validationMessages, component, template, feature, foundTemplateFeatureURIs);
										
									}
								}
								
								//COMBINATORIALDERIVATION_COMPONENT_VARIABLE_FEATURE_CARDINALITY_RESTRICTION
								validationMessages= assertStaticAndVariableFeatureCardinality(validationMessages,cd, component, template);
																
								//COMBINATORIALDERIVATION_COMPONENT_TYPE_RESTRICTION
								validationMessages=assertCombDerComponentTypeRestriction(validationMessages, component, template);
								
								//COMBINATORIALDERIVATION_COMPONENT_ROLE_RESTRICTION
								validationMessages = assertCombDerComponentRoleRestriction(validationMessages, component, template);																						
							}							
						}			
					}		
				}
			}
		}
		return validationMessages;
	}
	
	//COMBINATORIALDERIVATION_COMPONENT_FEATURE_TYPE_RESTRICTION 
		private List<ValidationMessage> assertCombDerComponentFeatureTypeRestriction(List<ValidationMessage> validationMessages, Component derived, Component template, Feature featureOfDerived, Set<URI> foundTemplateFeatureURIs) throws SBOLGraphException
		{
			if (Configuration.getInstance().isValidateRecommendedRules()) {
				if (foundTemplateFeatureURIs!=null){
					List<Feature> templateFeatures=template.getFeatures();			
					for (URI templateFeatureURI: foundTemplateFeatureURIs){
						for (Feature templateFeature:templateFeatures){
							if (templateFeature.getUri().equals(templateFeatureURI)) {
								List<URI> urisTemplate= getFeatureTypes(templateFeature,null);
								List<URI> urisDerived =getFeatureTypes(featureOfDerived,null);
								if (urisTemplate!=null){
									if (urisDerived==null){
										String message=String.format("{COMBINATORIALDERIVATION_COMPONENT_FEATURE_TYPE_RESTRICTION}%sTemplate feature types: %s",ValidationMessage.INFORMATION_SEPARATOR, urisTemplate);									
										ValidationMessage validationMessage = new ValidationMessage(message, DataModel.Component.uri, derived, urisDerived);
										validationMessage.childPath(DataModel.Component.feature, featureOfDerived).childPath(DataModel.type);
										validationMessages=IdentifiedValidator.addToValidations(validationMessages, validationMessage);
									}
									else{
										for (URI uriTemplate:urisTemplate){
											boolean exists=urisDerived.contains(uriTemplate);
											if (!exists){
												String message=String.format("{COMBINATORIALDERIVATION_COMPONENT_FEATURE_TYPE_RESTRICTION}%sTemplate feature type: %s",ValidationMessage.INFORMATION_SEPARATOR, uriTemplate);
												ValidationMessage validationMessage = new ValidationMessage(message, DataModel.Component.uri, derived, urisDerived);
												validationMessage.childPath(DataModel.Component.feature, featureOfDerived).childPath(DataModel.type);
												validationMessages=IdentifiedValidator.addToValidations(validationMessages, validationMessage);
											}
										}
									}
								}
							}
						}
					}
				}
			}
		return validationMessages;
	}
		 
	private List<URI> getFeatureTypes(Feature feature, Set<URI> visitedComponentRefs) throws SBOLGraphException{
		if (feature instanceof SubComponent){
			SubComponent referred = (SubComponent) feature;
			Component instanceOf=referred.getInstanceOf();
			if (instanceOf!=null){
				return instanceOf.getTypes();
			}			
		}		
		else if (feature instanceof ComponentReference){
			URI featureURI=feature.getUri();
			if (visitedComponentRefs==null || !visitedComponentRefs.contains(featureURI) ) {
				if (visitedComponentRefs==null){//Prevent loops, if ComponentReferences refersTo themselves hierarchically
					visitedComponentRefs=new HashSet<URI>();
				}
				visitedComponentRefs.add(featureURI);
				ComponentReference referred= (ComponentReference) feature;
				Feature crFeature=referred.getRefersTo();
				return getFeatureTypes(crFeature,visitedComponentRefs);	//Return the type recursively.
			}
		}
		else if (feature instanceof ExternallyDefined){
			ExternallyDefined exDefined= (ExternallyDefined) feature;
			return exDefined.getTypes();			
		}
		else if (feature instanceof LocalSubComponent){
			LocalSubComponent referred = (LocalSubComponent) feature;
			return referred.getTypes();			
		}		
		return null;
	}
	
	
	//COMBINATORIALDERIVATION_COMPONENT_FEATURE_ROLE_RESTRICTION 
	private List<ValidationMessage> assertCombDerComponentFeatureRoleRestriction(List<ValidationMessage> validationMessages, Component derived, Component template, Feature featureOfDerived, Set<URI> foundTemplateFeatureURIs) throws SBOLGraphException
	{
		if (Configuration.getInstance().isValidateRecommendedRules()) {
			if (foundTemplateFeatureURIs!=null){
				List<Feature> templateFeatures=template.getFeatures();			
				for (URI templateFeatureURI: foundTemplateFeatureURIs){
					for (Feature templateFeature:templateFeatures){
						if (templateFeature.getUri().equals(templateFeatureURI)) {
							List<URI> rolesTemplate= templateFeature.getRoles();
							List<URI> rolesDerived =featureOfDerived.getRoles();
							if (rolesTemplate!=null){
								if (rolesDerived==null){
									String message=String.format("{COMBINATORIALDERIVATION_COMPONENT_FEATURE_ROLE_RESTRICTION}%sTemplate feature roles: %s",ValidationMessage.INFORMATION_SEPARATOR, rolesTemplate);									
									ValidationMessage validationMessage = new ValidationMessage(message, DataModel.Component.uri, derived, rolesDerived);
									validationMessage.childPath(DataModel.Component.feature, featureOfDerived).childPath(DataModel.role);
									validationMessages=IdentifiedValidator.addToValidations(validationMessages, validationMessage);
								}
								else{
									for (URI role:rolesTemplate){
										boolean exists=rolesDerived.contains(role);
										if (!exists){
											String message=String.format("{COMBINATORIALDERIVATION_COMPONENT_FEATURE_ROLE_RESTRICTION}%sTemplate feature role: %s",ValidationMessage.INFORMATION_SEPARATOR, role);
											ValidationMessage validationMessage = new ValidationMessage(message, DataModel.Component.uri, derived, rolesDerived);
											validationMessage.childPath(DataModel.Component.feature, featureOfDerived).childPath(DataModel.role);
											validationMessages=IdentifiedValidator.addToValidations(validationMessages, validationMessage);
										}
									}
								}
							}
						}
					}
				}
			}
		}
		return validationMessages;
	}
	
	//COMBINATORIALDERIVATION_COMPONENT_FEATURE_CONSTRAINT_RESTRICTION = sbol3-12113  If the prov:wasDerivedFrom property of a Component refers to a CombinatorialDerivation and the template Component of the CombinatorialDerivation contains Constraint objects, then for any Feature contained by the Component that has a prov:wasDerivedFrom property that refers to the subject or object Feature of any of the template Constraint objects, that feature MUST adhere to the restriction properties of the template Constraint objects.
	private List<ValidationMessage> assertCombDerComponentFeatureConstraintRestriction(List<ValidationMessage> validationMessages, Component derived, Component template, Feature featureOfDerived, Set<URI> foundTemplateFeatureURIs) throws SBOLGraphException
	{
		if (foundTemplateFeatureURIs!=null){
			for (URI templateFeatureURI: foundTemplateFeatureURIs){
				List<Constraint> templateConstraints=template.getConstraints();
				
				Set<Constraint> templateConstraintsWithFeature=getConstraints(templateConstraints,templateFeatureURI);
				if (templateConstraintsWithFeature!=null){
					List<Constraint> derivedConstraints=derived.getConstraints();
					for (Constraint templateConstraint: templateConstraintsWithFeature){
						boolean isSubject=false;
						Feature constraintSubject=templateConstraint.getSubject();
						if (constraintSubject!=null && constraintSubject.getUri().equals(templateFeatureURI))
						{
							isSubject=true;
						}												
						Set<Constraint> derivedContraintsWithFeature=getConstraints(derivedConstraints,featureOfDerived.getUri(), templateConstraint.getRestriction(), isSubject);
						if (derivedContraintsWithFeature==null || derivedContraintsWithFeature.size()==0){
							String message=String.format("{COMBINATORIALDERIVATION_COMPONENT_FEATURE_CONSTRAINT_RESTRICTION}%s Missing constraint from template:%s, Missing constraint feature in derived.constraints:%s, Missing restriction in derived.constraints:%s",
									ValidationMessage.INFORMATION_SEPARATOR, 
									templateConstraint.getUri(),
									featureOfDerived.getUri(),
									templateConstraint.getRestriction()									
									);						
							ValidationMessage validationMessage = new ValidationMessage(message, DataModel.Component.uri, derived, SBOLUtil.getURIs(derivedConstraints));
							validationMessage.childPath(DataModel.Component.constraint);
							validationMessages=IdentifiedValidator.addToValidations(validationMessages, validationMessage);				
				
						}						
					}
				}
			}
		}
		return validationMessages;
	}

	
	private Set<Constraint> getConstraints(List<Constraint> constraints,URI featureURI) throws SBOLGraphException
	{
		Set<Constraint> found=null;
		if (constraints!=null){
			for (Constraint constraint: constraints){
				Feature subject=constraint.getSubject();
				Feature object=constraint.getObject();
				if (subject!=null && subject.getUri().equals(featureURI)){
					found=addToSet(found, constraint);
				}
				else if (object!=null && object.getUri().equals(featureURI)){
					found=addToSet(found, constraint);
				}				
			}
		}
		return found;		
	}
	
	
	private Set<Constraint> getConstraints(List<Constraint> constraints,URI featureURI, URI restrictionURI, boolean isSubject) throws SBOLGraphException
	{
		Set<Constraint> found=null;
		if (constraints!=null){
			for (Constraint constraint: constraints){
				Feature subject=constraint.getSubject();
				Feature object=constraint.getObject();
				if (isSubject && subject!=null && subject.getUri().equals(featureURI) && constraint.getRestriction().equals(restrictionURI)){
					found=addToSet(found, constraint);
				}
				else if (!isSubject && object!=null && object.getUri().equals(featureURI) && constraint.getRestriction().equals(restrictionURI)){
					found=addToSet(found, constraint);
				}				
			}
		}
		return found;		
	}
	
	//COMBINATORIALDERIVATION_COMPONENT_SUBCOMPONENT_INSTANCEOF_VALID= sbol3-12112  If the prov:wasDerivedFrom property of a Component refers to a CombinatorialDerivation, then for any SubComponent in the Component with a prov:wasDerivedFrom property referring to a variable Feature in the template Component of the CombinatorialDerivation, that derived SubComponent MUST have an instanceOf property that refers to a Component specified by the corresponding VariableFeature. In particular, that Component must be a value of the variant property, a member or recursive member of a Collection that is a value of the variantCollection property, or a Component with a prov:wasDerivedFrom property that refers to a CombinatorialDerivation specified by a variantDerivation property of the VariableFeature.
	private List<ValidationMessage>  assertCombDerSubComponentInstanceOfValuesAreValid(List<ValidationMessage> validationMessages,CombinatorialDerivation cd, Component derived, Feature feature) throws SBOLGraphException
	{	
		if (feature instanceof SubComponent){
			SubComponent subComp= (SubComponent) feature;
			   
			VariableFeature varFeature=getVariableFeature(feature, cd.getVariableFeatures());
			if (varFeature!=null)//Variable feature
			{
				Set<Component> validComponents=getVariableFeatureComponents(varFeature);
				Component instanceOf=subComp.getInstanceOf();
				if (instanceOf!=null){
					if (validComponents==null || !SBOLUtil.contains(validComponents,instanceOf)){												
						String message=String.format("{COMBINATORIALDERIVATION_COMPONENT_SUBCOMPONENT_INSTANCEOF_VALID}%s wasDerivedFroms:%s",ValidationMessage.INFORMATION_SEPARATOR, subComp.getWasDerivedFrom());						
						ValidationMessage validationMessage = new ValidationMessage(message, DataModel.Component.uri, derived, instanceOf);
						validationMessage.childPath(DataModel.Component.feature, subComp).childPath(DataModel.SubComponent.instanceOf);
						validationMessages=IdentifiedValidator.addToValidations(validationMessages, validationMessage);				
					}					
				}				
			}			
		}		
		return validationMessages;
	}
	
	private <T extends Identified> Set<T> addToSet(Set<T> current, java.util.Collection<T> newItems){
		if (newItems!=null && newItems.size()>0){
			if (current==null){
				current=new HashSet<>();
			}
			current.addAll(newItems);
		}
		return current;
	}
	
	private <T extends Identified> Set<T> addToSet(Set<T> current, T item){
		if (item!=null){
			if (current==null){
				current=new HashSet<>();
			}
			current.add(item);
		}
		return current;
	}
	
	private Set<Component> getVariableFeatureComponents(VariableFeature varFeature) throws SBOLGraphException
	{
		Set<Component> validComponents=new HashSet<Component>();
		if (varFeature!=null)
		{
			//Components in getVariants
			List<Component> variants=varFeature.getVariants();
			validComponents=addToSet(validComponents, variants);
						
			//Components in getVariantCollections recursively
			List<Collection> collections=varFeature.getVariantCollections();
			if (collections!=null){
				for (Collection collection: collections){
					Set<Component> componentsInVariantCollection=getMembers(collection, null,null);
					validComponents=addToSet(validComponents, componentsInVariantCollection);					
				}
			}
			
			//Components in getVariantDerivations
			List<CombinatorialDerivation> cds= varFeature.getVariantDerivations();
			if (cds!=null){
				for (CombinatorialDerivation cd: cds){
					Set<Component> derivedComponents=getDerivedComponents(cd);
					validComponents=addToSet(validComponents, derivedComponents);					
				}
			}			
		}
		return validComponents;
	}
	
	private Set<Component> getDerivedComponents(CombinatorialDerivation cd) throws SBOLGraphException
	{
		Set<Component> derivedComps=null;
		List<Component> components=this.getComponents();
		URI cdURI=cd.getUri();
		
		for (Component component:components){
			List<URI> wasDerivedFroms=component.getWasDerivedFrom();
			if (wasDerivedFroms!=null && wasDerivedFroms.contains(cdURI)){
				derivedComps=addToSet(derivedComps, component);								
			}
		}
		return derivedComps;		
	}
	
	private Set<Component> getMembers(Collection collection, Set<Component> memberComponents, Set<Collection> visited) throws SBOLGraphException
	{
		if (collection!=null){
			if (visited==null){
				visited=new HashSet<Collection>();
			}
			
			if (visited.contains(collection)){
				return memberComponents;
			}
			else{
				visited.add(collection);
			}
			
			List<URI> members=collection.getMembers();
			if (members!=null){
				for (URI memberURI:members){
					Component memberComponent=this.getIdentified(memberURI, Component.class);
					if (memberComponent!=null){
						memberComponents=addToSet(memberComponents, memberComponent);
					}
					else{
						Collection memberCollection=this.getIdentified(memberURI, Collection.class);
						if (memberCollection!=null){
							memberComponents=getMembers(memberCollection, memberComponents,visited);
						}						
					}
				}
			}			
		}
		return memberComponents;
	}
	
	private List<ValidationMessage>  assertStaticAndVariableFeatureCardinality(List<ValidationMessage> validationMessages,CombinatorialDerivation cd, Component derived, Component template) throws SBOLGraphException
	{		
		if (Configuration.getInstance().isValidateRecommendedRules()) {	
			List<Feature> templateFeatures=template.getFeatures();
			List<Feature> derivedFeatures=derived.getFeatures();	
			List<VariableFeature> variableFeatures=cd.getVariableFeatures();
			if (templateFeatures!=null){
				for (Feature templateFeature: templateFeatures){
					URI templateFeatureURI= templateFeature.getUri();
 					VariableFeature varFeature=getVariableFeature(variableFeatures, templateFeatureURI);
 					
 					int count=0;
					if (derivedFeatures!=null){
						for (Feature derivedFeature: derivedFeatures){
							List<URI> featureDerivedFroms=derivedFeature.getWasDerivedFrom();
							if (featureDerivedFroms!=null && featureDerivedFroms.contains(templateFeatureURI)){
								count ++;								
							}
						}
					}
					if (varFeature!=null)//Variable feature
					{
						//COMBINATORIALDERIVATION_COMPONENT_VARIABLE_FEATURE_CARDINALITY_RESTRICTION = sbol3-12111 - If the prov:wasDerivedFrom property of a Component refers to a CombinatorialDerivation, then each variable Feature in the template Component SHOULD be referred to by a prov:wasDerivedFrom property from a number of Feature objects in the derived Component. that is compatible with the cardinality property of the corresponding VariableFeature.							
						VariableFeatureCardinality cardinality= varFeature.getCardinality();
						boolean error=false;
						if (cardinality==VariableFeatureCardinality.One && count!=1){
							error=true;
						}
						else if (cardinality==VariableFeatureCardinality.OneOrMore && count==0){
							error=true;
						}
						else if (cardinality==VariableFeatureCardinality.ZeroOrOne && count>1){
							error=true;
						}
						if (error){
							String message=String.format("{COMBINATORIALDERIVATION_COMPONENT_VARIABLE_FEATURE_CARDINALITY_RESTRICTION}%s wasDerivedFrom count: %s, Cardinality: %s",ValidationMessage.INFORMATION_SEPARATOR, count, cardinality);
							ValidationMessage validationMessage = new ValidationMessage(message, DataModel.Component.uri, template, templateFeature);
							validationMessage.childPath(DataModel.Component.feature);
							validationMessages=IdentifiedValidator.addToValidations(validationMessages, validationMessage);
						}
					}
					else if (varFeature==null && count!=1){//static feature										
						//COMBINATORIALDERIVATION_COMPONENT_STATIC_FEATURE_ONLYONE_RESTRICTION = sbol3-12110 - If the prov:wasDerivedFrom property of a Component refers to a CombinatorialDerivation, then each static Feature in the template Component SHOULD be referred to by a prov:wasDerivedFrom property from exactly one Feature in the derived Component.						
						String message=String.format("{COMBINATORIALDERIVATION_COMPONENT_STATIC_FEATURE_ONLYONE_RESTRICTION}%s wasDerivedFrom count: %s",ValidationMessage.INFORMATION_SEPARATOR, count);
						ValidationMessage validationMessage = new ValidationMessage(message, DataModel.Component.uri, template, templateFeature);
						validationMessage.childPath(DataModel.Component.feature);
						validationMessages=IdentifiedValidator.addToValidations(validationMessages, validationMessage);											
 					}
				}
			}
		}
		return validationMessages;
	}
	private List<ValidationMessage>  assertCombDerComponentStaticFeaturePropertyRestriction(List<ValidationMessage> validationMessages, CombinatorialDerivation cd, Component component, Feature feature, Set<URI> foundTemplateFeatureURIs) throws SBOLGraphException
	{
		//COMBINATORIALDERIVATION_COMPONENT_STATIC_FEATUREPROPERTY_RESTRICTION - Blue		
			if (foundTemplateFeatureURIs!=null && foundTemplateFeatureURIs.size()>0){			
				//Implement validation rules for static features
				for (URI foundTemplateFeatureURI:foundTemplateFeatureURIs){
					VariableFeature varFeature=getVariableFeature(cd.getVariableFeatures(), foundTemplateFeatureURI);															
					
					if (varFeature==null){//If a  static feature
						Set<String> notFoundProperties=RDFUtil.getNotExistingTemplatePropertiesv2(this.getRDFModel(),foundTemplateFeatureURI, feature.getUri());
						if (notFoundProperties!=null){
							for (String notFoundProperty:notFoundProperties){
								String message=String.format("{COMBINATORIALDERIVATION_COMPONENT_STATIC_FEATUREPROPERTY_RESTRICTION}%sTemplate feature property is missing: %s",ValidationMessage.INFORMATION_SEPARATOR, notFoundProperty);
								ValidationMessage validationMessage = new ValidationMessage(message, DataModel.Component.uri, component, feature);
								validationMessage.childPath(DataModel.Component.feature);
								validationMessages=IdentifiedValidator.addToValidations(validationMessages, validationMessage);
							}
						}
						
					}												
				}
			}
		
		return validationMessages;
	}
	
	private List<ValidationMessage>  assertCombDerComponentWasDerivedFromRestriction(List<ValidationMessage> validationMessages, Component component, Feature feature,Set<URI> foundTemplateFeatureURIs) throws SBOLGraphException
	{
		//COMBINATORIALDERIVATION_COMPONENT_WASDERIVEDFROM_RESTRICTION
		if (Configuration.getInstance().isValidateRecommendedRules())
		{
			if (foundTemplateFeatureURIs==null || foundTemplateFeatureURIs.size()==0){
				ValidationMessage message = new ValidationMessage("{COMBINATORIALDERIVATION_COMPONENT_WASDERIVEDFROM_RESTRICTION}", DataModel.Component.uri, component, feature.getWasDerivedFrom());
				message.childPath(DataModel.Component.feature, feature).childPath(DataModel.Identified.wasDerivedFrom);
				validationMessages=IdentifiedValidator.addToValidations(validationMessages, message);
			}
		}
		return validationMessages;
	}
	
	private List<ValidationMessage>  assertCombDerComponentRoleRestriction(List<ValidationMessage> validationMessages, Component component, Component template) throws SBOLGraphException
	{
		//COMBINATORIALDERIVATION_COMPONENT_ROLE_RESTRICTION
		if (Configuration.getInstance().isValidateRecommendedRules())
		{
			List<URI> roles= template.getRoles();
			if (roles!=null)
			{
				if (component.getRoles()==null)
				{
					ValidationMessage validationMessage = new ValidationMessage("{COMBINATORIALDERIVATION_COMPONENT_ROLE_RESTRICTION}", DataModel.Component.uri, component, null);
					validationMessage.childPath(DataModel.role);
					validationMessages=IdentifiedValidator.addToValidations(validationMessages, validationMessage);
				}
				else{
					for (URI role:roles){
						boolean exists=component.getRoles().contains(role);
						if (!exists){
							String message=String.format("{COMBINATORIALDERIVATION_COMPONENT_ROLE_RESTRICTION}%sTemplate component role: %s",ValidationMessage.INFORMATION_SEPARATOR, role);
							ValidationMessage validationMessage = new ValidationMessage(message, DataModel.Component.uri, component, component.getRoles());
							validationMessage.childPath(DataModel.role);
							validationMessages=IdentifiedValidator.addToValidations(validationMessages, validationMessage);
						}
					}
				}
			}
		}
		return validationMessages;
	}	
	
	private List<ValidationMessage>  assertCombDerComponentTypeRestriction(List<ValidationMessage> validationMessages, Component component, Component template) throws SBOLGraphException
	{
		//COMBINATORIALDERIVATION_COMPONENT_TYPE_RESTRICTION
		if (Configuration.getInstance().isValidateRecommendedRules())
		{
			List<URI> types= template.getTypes();
			if (types!=null)
			{
				for (URI type:types)
				{
					boolean exists=component.getTypes().contains(type);
					if (!exists)
					{
						String message=String.format("{COMBINATORIALDERIVATION_COMPONENT_TYPE_RESTRICTION}%sTemplate component type: %s",ValidationMessage.INFORMATION_SEPARATOR, type);
						ValidationMessage validationMessage = new ValidationMessage(message, DataModel.Component.uri, component, component.getTypes());
						validationMessage.childPath(DataModel.type);
						validationMessages=IdentifiedValidator.addToValidations(validationMessages, validationMessage);
					}
				}
			}
		}
		return validationMessages;
	}
	
	private VariableFeature getVariableFeature(Feature derivedFeature, List<VariableFeature> variableFeatures) throws SBOLGraphException
	{
		VariableFeature varFeature=null;
		List<URI> derivedFroms=derivedFeature.getWasDerivedFrom();
		if (derivedFroms!=null)
		{
			for (URI wasDerivedFrom: derivedFroms)
			{
				varFeature=getVariableFeature(variableFeatures, wasDerivedFrom);
				if (varFeature!=null)
				{
					break;
				}
				
			}
		}
		return varFeature;		
	}
	
	private VariableFeature getVariableFeature(List<VariableFeature> varFeatures, URI templateFeatureURI ) throws SBOLGraphException
	{
		VariableFeature varFeature=null;
		if (varFeatures!=null) {
			for (VariableFeature variableFeature:varFeatures){
				Feature variable=variableFeature.getVariable();
				if (variable!=null){
					if (variable.getUri().equals(templateFeatureURI)){
						varFeature=variableFeature;
						break;
					}
				}
			}
		}
		return varFeature;
	}
	
	
	
	
	private List<ValidationMessage> assertCombDerWasDerivedRestrictionForCollections(List<ValidationMessage> validationMessages) throws SBOLGraphException
	{
		List<Collection> collections=this.getCollections();
		if (collections!=null){
			for (Collection collection:collections){
				List<URI> derivedFroms=collection.getWasDerivedFrom();
				if (derivedFroms!=null){
					for (URI uri:derivedFroms){
						CombinatorialDerivation cd= this.getIdentified(uri, CombinatorialDerivation.class);
						if (cd!=null){
							List<URI> memberURIs=collection.getMembers();
							if (memberURIs !=null){
								for (URI memberURI:memberURIs){
									URI templateURI=cd.getTemplate().getUri();
									if (!memberURI.equals(templateURI)){
										boolean exists=RDFUtil.exists(this.model, memberURI, DataModel.Identified.wasDerivedFrom, cd.getUri());
										if (!exists){
											String message=String.format("{COMBINATORIALDERIVATION_COLLECTION_WASDERIVEDFROM_RESTRICTION}%sCollection was derived from: %s, Template component: %s",ValidationMessage.INFORMATION_SEPARATOR, cd.getUri(), cd.getTemplate().getUri());
											ValidationMessage validationMessage = new ValidationMessage(message, DataModel.Collection.uri, collection, memberURI);
											validationMessage.childPath(DataModel.Collection.member);
											validationMessages=IdentifiedValidator.addToValidations(validationMessages, validationMessage);
										}
									}
								}
							}
						}
						
					}		
				}
			}
		}
		return validationMessages;
	}
	
	//COMBINATORIALDERIVATION_VARIABLEFEATURE_VARIANTCOLLECTION_RESTRICTION
	private List<ValidationMessage> assertCombDerVariantCollectionsIncludeComponentMembers(List<ValidationMessage> validationMessages) throws SBOLGraphException
	{	
		List<CombinatorialDerivation> cds=this.getCombinatorialDerivations();
		if (cds!=null){
			for (CombinatorialDerivation cd: cds) {
				List<VariableFeature> vfs=cd.getVariableFeatures();
				if (vfs!=null){										
					MutablePair<List<ValidationMessage>, Set<Collection>> collectionsVisited=new MutablePair<List<ValidationMessage>, Set<Collection>>(validationMessages, null);
					for (VariableFeature vf:vfs) {
						// getVariantCollections recursively
						List<Collection> collections=vf.getVariantCollections();
						if (collections!=null){
							for (Collection collection: collections){
								collectionsVisited=assertComponentOrCollectionMembersOnly(cd, vf, collection,collectionsVisited);													
							}
						}
						validationMessages=collectionsVisited.getLeft();						
					}
						
				}
			}
		}
		return validationMessages;
	}
	
	//COMBINATORIALDERIVATION_VARIABLEFEATURE_NOT_CIRCULAR = sbol3-12204 - VariableFeature objects MUST NOT form circular reference chains via their variantDerivation properties and parent CombinatorialDerivation objects.
	private List<ValidationMessage> assertCombDerVariantFeaturesNotCircular(List<ValidationMessage> validationMessages) throws SBOLGraphException
	{	
		List<CombinatorialDerivation> cds=this.getCombinatorialDerivations();
		if (cds!=null){
			for (CombinatorialDerivation cd: cds) {
				validationMessages=assertCombDerNotCircular(cd, null,cd, validationMessages);	
			}
		}
		return validationMessages;
	}
	
	//COMBINATORIALDERIVATION_VARIABLEFEATURE_NOT_CIRCULAR
	private List<ValidationMessage> assertCombDerNotCircular(CombinatorialDerivation rootCd, VariableFeature rootFeature, CombinatorialDerivation cdReferred, List<ValidationMessage> validationMessages) throws SBOLGraphException
	{		
		List<VariableFeature> vfs=cdReferred.getVariableFeatures();
		if (vfs!=null){										
			for (VariableFeature vf:vfs) {				
				// getVariantCollections recursively
				if (cdReferred==rootCd){//First iteration, cdReferred and rootCd are the same objects
					rootFeature=vf;
				}
				List<CombinatorialDerivation> cds=vf.getVariantDerivations();
				if (cds!=null && cds.size()>0){
					if (SBOLUtil.getURIs(cds).contains(rootCd.getUri())){
						String message=String.format("{COMBINATORIALDERIVATION_VARIABLEFEATURE_NOT_CIRCULAR}%s Linked from a referred CombinatorialDerivation: %s, VariableFeature:%s",ValidationMessage.INFORMATION_SEPARATOR, cdReferred.getUri(), vf.getUri());
						ValidationMessage validationMessage = new ValidationMessage(message, DataModel.CombinatorialDerivation.uri, rootCd, rootFeature);
						validationMessage.childPath(DataModel.CombinatorialDerivation.variableFeature);
						validationMessages=IdentifiedValidator.addToValidations(validationMessages, validationMessage);
					}
					else{
						for (CombinatorialDerivation cdNextReferred:cds){
							validationMessages=assertCombDerNotCircular(rootCd, rootFeature, cdNextReferred, validationMessages);
						}
					}
				}
			}
				
		}
		return validationMessages;
	}
		
	
	private MutablePair<List<ValidationMessage>, Set<Collection>> assertComponentOrCollectionMembersOnly(CombinatorialDerivation cd, VariableFeature vf,  Collection collection, MutablePair<List<ValidationMessage>, Set<Collection>> visited) throws SBOLGraphException
	{
		if (collection!=null){
			if (visited.getRight()==null){
				visited.setRight(new HashSet<Collection>());
			}
			
			if (visited.getRight().contains(collection)){
				return visited;
			}
			else{
				visited.getRight().add(collection);
			}
			
			List<URI> members=collection.getMembers();
			if (members!=null){
				for (URI memberURI:members){
					boolean validMember=false;
					Component memberComponent=this.getIdentified(memberURI, Component.class);
					if (memberComponent!=null){
						validMember=true;
					}
					else{
						Collection memberCollection=this.getIdentified(memberURI, Collection.class);
						if (memberCollection!=null){
							validMember=true;
							visited=assertComponentOrCollectionMembersOnly(cd, vf, memberCollection, visited);
						}	
						else{
							validMember=false;
						}
					}
					if (!validMember) {
						String message=String.format("{COMBINATORIALDERIVATION_VARIABLEFEATURE_VARIANTCOLLECTION_RESTRICTION}%s CombinatorialDerivation:%s, VariableFeature:%s",ValidationMessage.INFORMATION_SEPARATOR, cd.getUri(), vf.getUri());
						ValidationMessage validationMessage = new ValidationMessage(message, DataModel.Collection.uri, collection, memberURI);
						validationMessage.childPath(DataModel.Collection.member);
						visited.setLeft(IdentifiedValidator.addToValidations(visited.getLeft(), validationMessage));		
					}				
				}
			}			
		}
		return visited;
	}
	
	private Set<URI> ignoredProperties()
	{
		Set<URI> props=new HashSet<URI>();
		props.add(DataModel.Identified.displayId);
		return props;
	}
	
	
			
}
