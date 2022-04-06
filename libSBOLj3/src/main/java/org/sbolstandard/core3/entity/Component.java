package org.sbolstandard.core3.entity;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.sbolstandard.core3.api.SBOLAPI;
import org.sbolstandard.core3.entity.Location.LocationBuilder;
import org.sbolstandard.core3.util.RDFUtil;
import org.sbolstandard.core3.util.SBOLGraphException;
import org.sbolstandard.core3.util.SBOLUtil;
import org.sbolstandard.core3.vocabulary.DataModel;
import org.sbolstandard.core3.vocabulary.Encoding;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public class Component extends TopLevel {
	
	/*private List<URI> roles=null;
	private List<URI> sequences=null;	
	private List<URI> types=null;
	private List<Feature> features=null;
	private List<SubComponent> subComponents=null;
	private List<ComponentReference> componentReferences=null;
	private List<LocalSubComponent> localSubComponents=null;
	private List<ExternallyDefined> externallyDefineds=null;
	private List<SequenceFeature> sequenceFatures=null;
	private List<Interaction> interactions=null;
	private List<Constraint> constraints=null;
	private Interface componentInterface=null;
	private List<URI> models=null;*/
	
	//private Set<Interaction> interactions2=null;
	
	protected Component(Model model, URI uri) throws SBOLGraphException
	{
		super(model, uri);
	}
	
	protected Component(Resource resource) throws SBOLGraphException
	{
		super(resource);
	}
	
	@Valid
	@NotNull(message = "Component.type cannot be empty")
	public List<URI> getTypes() {
		return RDFUtil.getPropertiesAsURIs(this.resource,DataModel.type);
	}
	
	public void setTypes(List<URI> types) {
		RDFUtil.setProperty(resource, DataModel.type, types);
	}
	
	
	public List<URI> getRoles() {
		return RDFUtil.getPropertiesAsURIs(this.resource, DataModel.role);
	}
	
	public void setRoles(List<URI> roles) {
		RDFUtil.setProperty(resource, DataModel.role, roles);
	}
	
	
	public List<URI> getSequences() {
		return RDFUtil.getPropertiesAsURIs(this.resource, DataModel.Component.sequence);
	}
	
	public void setSequences(List<URI> sequences) {
		RDFUtil.setProperty(resource, DataModel.Component.sequence, sequences);
	}
	
	public List<URI> getSequences(Encoding encoding)
	{
		return filterIdentifieds(this.getSequences(),DataModel.Sequence.encoding, encoding.getUri().toString());
	}
	
	//Features
	
	/*public List<Feature> getFeatures() throws SBOLException, SBOLGraphException {
		if (features==null)
		{
			features = new ArrayList<Feature>();
			addToList(this.resource, features, DataModel.Property.feature, DataModel.Entity.Feature);	
		}
		return features;
	}
	
	private Feature addToFeatures(Feature feature) {
		RDFUtil.setProperty(resource, DataModel.Property.feature, feature.getUri());
		if (features == null) {
			features = new ArrayList<Feature>();
		}
		features.add(feature);
		return feature;
	}
	*/
	
	private void addToList(List<Feature> listA, List<? extends Feature> listB)
	{
		if (listB!=null && listB.size()>0)
		{
			listA.addAll(listB);
		}
	}
	
	public List<Feature> getFeatures() throws SBOLGraphException{
		List<Feature> features=new ArrayList<Feature>();
		addToList(features, getSubComponents());
		addToList(features, getComponentReferences());
		addToList(features, getLocalSubComponents());
		addToList(features, getExternallyDefineds());
		addToList(features, getSequenceFeatures());	
		return features;
	}
	
	@Valid
	public List<SubComponent> getSubComponents() throws SBOLGraphException {
		return addToList(DataModel.Component.feature, SubComponent.class, DataModel.SubComponent.uri);
		/*
		if (subComponents==null)
		{
			List<Resource> resources=RDFUtil.getResourcesWithProperty(this.resource, DataModel.Component.feature, DataModel.Entity.SubComponent);
			for (Resource res:resources)
			{
				SubComponent subComponent=new SubComponent(res);
				subComponents.add(subComponent);			
			}
		}
		return subComponents;*/
	}
	
	public SubComponent createSubComponent(URI uri, URI isInstanceOf) throws SBOLGraphException
	{
		SubComponent feature = new SubComponent(this.resource.getModel(), uri);
		feature.setIsInstanceOf(isInstanceOf);
		addToList(feature, DataModel.Component.feature);
		return feature;	
	}
	
	private SubComponent createSubComponent(String displayId, URI isInstanceOf) throws SBOLGraphException
	{
		return createSubComponent(SBOLAPI.append(this.getUri(), displayId), isInstanceOf);
	}
	
	public SubComponent createSubComponent(URI isInstanceOf) throws SBOLGraphException
	{
		String displayId=SBOLAPI.createLocalName(DataModel.SubComponent.uri, getSubComponents());
		return createSubComponent(displayId, isInstanceOf);
	}
	
	@Valid
	public List<ComponentReference> getComponentReferences() throws SBOLGraphException {
		return addToList(DataModel.Component.feature, ComponentReference.class, DataModel.ComponentReference.uri);
	}
	
	public ComponentReference createComponentReference(URI uri, URI feature, URI inChildOf) throws SBOLGraphException {
		ComponentReference componentReference= new ComponentReference(this.resource.getModel(), uri);
		componentReference.setRefersTo(feature);
		componentReference.setInChildOf(inChildOf);
		addToList(componentReference, DataModel.Component.feature);
		return componentReference;	
	}
	
	private ComponentReference createComponentReference(String displayId, URI feature, URI inChildOf) throws SBOLGraphException {
		return createComponentReference(SBOLAPI.append(this.getUri(), displayId), feature, inChildOf);	
	}
	
	public ComponentReference createComponentReference(URI feature, URI inChildOf) throws SBOLGraphException {
		String displayId=SBOLAPI.createLocalName(DataModel.ComponentReference.uri, getComponentReferences());
		return createComponentReference(displayId, feature, inChildOf);	
	}
	
	public ComponentReference createComponentReference(URI uri, Feature feature, SubComponent inChildOf) throws SBOLGraphException {
		return createComponentReference(uri, feature.getUri(), inChildOf.getUri());
	}
	
	public ComponentReference createComponentReference(Feature feature, SubComponent inChildOf) throws SBOLGraphException {
		return createComponentReference(feature.getUri(), inChildOf.getUri());
	}

	//Local sub components
	@Valid
	public List<LocalSubComponent> getLocalSubComponents() throws SBOLGraphException {
		return addToList(DataModel.Component.feature, LocalSubComponent.class, DataModel.LocalSubComponent.uri);
	}
	
	public LocalSubComponent createLocalSubComponent(URI uri, List<URI> types) throws SBOLGraphException
	{
		LocalSubComponent localSubComponent= new LocalSubComponent(this.resource.getModel(), uri);
		localSubComponent.setTypes(types);
		addToList(localSubComponent, DataModel.Component.feature);
		return localSubComponent;	
	}
	
	private LocalSubComponent createLocalSubComponent(String displayId, List<URI> types) throws SBOLGraphException
	{
		return createLocalSubComponent(SBOLAPI.append(this.getUri(), displayId), types);
	}
	
	public LocalSubComponent createLocalSubComponent(List<URI> types) throws SBOLGraphException
	{
		String displayId=SBOLAPI.createLocalName(DataModel.LocalSubComponent.uri, getLocalSubComponents());
		return createLocalSubComponent(displayId, types);
	}
	
	//Externally Defined 
	@Valid
	public List<ExternallyDefined> getExternallyDefineds() throws SBOLGraphException {
		return addToList(DataModel.Component.feature, ExternallyDefined.class, DataModel.ExternalyDefined.uri);
	}
	
	public ExternallyDefined createExternallyDefined(URI uri, List<URI> types, URI definition) throws SBOLGraphException
	{
		ExternallyDefined externallyDefined= new ExternallyDefined(this.resource.getModel(), uri);
		externallyDefined.setTypes(types);
		externallyDefined.setDefinition(definition);
		addToList(externallyDefined, DataModel.Component.feature);
		return externallyDefined;	
	}
	
	private ExternallyDefined createExternallyDefined(String displayId, List<URI> types, URI definition) throws SBOLGraphException
	{
		return createExternallyDefined(SBOLAPI.append(this.getUri(), displayId), types, definition);
	}
	
	public ExternallyDefined createExternallyDefined(List<URI> types, URI definition) throws SBOLGraphException
	{
		String displayId=SBOLAPI.createLocalName(DataModel.ExternalyDefined.uri, getExternallyDefineds());	
		return createExternallyDefined(displayId, types, definition);
	}
	
	//Sequence features
	@Valid
	public List<SequenceFeature> getSequenceFeatures() throws SBOLGraphException {
		return addToList(DataModel.Component.feature, SequenceFeature.class, DataModel.SequenceFeature.uri);
	}
	
	public SequenceFeature createSequenceFeature(URI uri, List<LocationBuilder> locations) throws SBOLGraphException {
		SequenceFeature sequenceFeature= new SequenceFeature(this.resource.getModel(), uri);
		
		RDFUtil.addProperty(resource, DataModel.Component.feature, sequenceFeature.getUri());
		if (locations!=null && locations.size()>0)
		{
			for (LocationBuilder locationBuilder:locations)
			{
				sequenceFeature.createLocation(locationBuilder);
			}
		}
		return sequenceFeature;	
	}	
	
	private SequenceFeature createSequenceFeature(String displayId, List<LocationBuilder> locations) throws SBOLGraphException {
		return createSequenceFeature(SBOLAPI.append(this.getUri(), displayId), locations);
	}
	
	public SequenceFeature createSequenceFeature(List<LocationBuilder> locations) throws SBOLGraphException {
		String displayId=SBOLAPI.createLocalName(DataModel.SequenceFeature.uri, getSequenceFeatures());	
		return createSequenceFeature(displayId, locations);
	}
	
	/*
	private SequenceFeature createSequenceFeature2(String displayId, List<Location> locations) throws SBOLGraphException {
		return createSequenceFeature2(SBOLAPI.append(this.getUri(), displayId), locations);
	}
	private SequenceFeature createSequenceFeature2(URI uri, List<Location> locations) throws SBOLGraphException {
		SequenceFeature sequenceFeature= new SequenceFeature(this.resource.getModel(), uri);
		
		RDFUtil.addProperty(resource, DataModel.Component.feature, sequenceFeature.getUri());
		if (locations!=null && locations.size()>0)
		{
			for (Location location:locations)
			{
				sequenceFeature.createLocation2(location);
			}
		}
		return sequenceFeature;	
	}
	*/
	
	//Interaction
	public Interaction createInteraction(URI uri, List<URI> types) throws SBOLGraphException {
		Interaction interaction= new Interaction(this.resource.getModel(), uri);
		interaction.setTypes(types);
		addToList(interaction, DataModel.Component.interaction);
		return interaction;
	}
	
	private Interaction createInteraction(String displayId, List<URI> types) throws SBOLGraphException {
		return createInteraction(SBOLAPI.append(this.getUri(), displayId), types);
	}
	
	public Interaction createInteraction(List<URI> types) throws SBOLGraphException {
		String displayId=SBOLAPI.createLocalName(DataModel.Interaction.uri, getInteractions());	
		return createInteraction(displayId, types);
	}
	
	@Valid
	public List<Interaction> getInteractions() throws SBOLGraphException {
		return addToList(DataModel.Component.interaction, Interaction.class);
	}
	
	//Constraint
	public Constraint createConstraint(URI uri, URI restriction, URI subject, URI object) throws SBOLGraphException {
		Constraint constraint= new Constraint(this.resource.getModel(), uri);
		constraint.setRestriction(restriction);
		constraint.setSubject(subject);
		constraint.setObject(object);
		addToList(constraint, DataModel.Component.constraint);
		return constraint;
	}
	
	private Constraint createConstraint(String displayId, URI restriction, URI subject, URI object) throws SBOLGraphException {
		return createConstraint(SBOLAPI.append(this.getUri(), displayId), restriction, subject, object);
	}
	
	public Constraint createConstraint(URI restriction, URI subject, URI object) throws SBOLGraphException {
		String displayId=SBOLAPI.createLocalName(DataModel.Constraint.uri, getConstraints());	
		return createConstraint(displayId, restriction, subject, object);
	}
	
	@Valid
	public List<Constraint> getConstraints() throws SBOLGraphException {
		return addToList(DataModel.Component.constraint, Constraint.class);
	}
	
	//Interface
	public Interface createInterface(URI uri) throws SBOLGraphException {
		Interface componentInterface =getInterface();
		if (componentInterface==null)
		{
			componentInterface = new Interface(this.resource.getModel(), uri);
			RDFUtil.setProperty(resource, DataModel.Component.hasInterface, uri);
		}
		return componentInterface;
	}
	
	public Interface createInterface() throws SBOLGraphException {
		return createInterface(SBOLAPI.append(this.getUri(), "Interface1"));
	}
	
	
	public Interface getInterface() throws SBOLGraphException {
		return contsructIdentified(DataModel.Component.hasInterface, Interface.class);
	}
	
	public List<URI> getModels() {
		return RDFUtil.getPropertiesAsURIs(this.resource, DataModel.Component.model);
	}
	
	public void setModels(List<URI> models) {
		RDFUtil.setProperty(resource, DataModel.Component.model, models);
	}

	public URI getResourceType()
	{
		return DataModel.Component.uri;
	}
}



/*public Interaction createInteractionDel(URI uri, List<URI> types ) {
Interaction interaction= new Interaction(this.resource.getModel(), uri);
interaction.setTypes(types);
RDFUtil.addProperty(resource, DataModel.Component.interaction, interaction.getUri());
if (this.interactions==null)
{
	interactions=new ArrayList<Interaction>();
}
interactions.add(interaction);
return interaction;	
}


public Interaction createInteractionDel2(URI uri, List<URI> types ) {
Interaction interaction= new Interaction(this.resource.getModel(), uri);
interaction.setTypes(types);
RDFUtil.addProperty(resource, DataModel.Component.interaction, interaction.getUri());
if (this.interactions2==null)
{
	interactions2=new HashSet<Interaction>();
}
interactions2.add(interaction);
return interaction;	
}
*/

/*
public List<Interaction> getInteractionsDel() throws SBOLGraphException {
	if (interactions==null)
	{
		List<Resource> resources=RDFUtil.getResourcesWithProperty(this.resource, DataModel.Component.interaction);
		if (resources!=null && resources.size()>0)
		{
			interactions=new ArrayList<Interaction>();
		}
		for (Resource res:resources)
		{
			Interaction interaction=new Interaction(res);
			interactions.add(interaction);			
		}
	}
	return interactions;
}

public Set<Interaction> getInteractionsDel2() throws SBOLGraphException {
	if (interactions2==null)
	{
		List<Resource> resources=RDFUtil.getResourcesWithProperty(this.resource, DataModel.Component.interaction);
		if (resources!=null && resources.size()>0)
		{
			interactions2=new HashSet<Interaction>();
		}
		for (Resource res:resources)
		{
			Interaction interaction=new Interaction(res);
			interactions2.add(interaction);			
		}
	}
	return interactions2;
}
*/

