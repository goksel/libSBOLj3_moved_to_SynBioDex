package org.sbolstandard;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.sbolstandard.Location.LocationBuilder;
import org.sbolstandard.util.RDFHandler;
import org.sbolstandard.util.RDFUtil;
import org.sbolstandard.vocabulary.DataModel;

public class Component extends TopLevel {
	
	private List<URI> roles=new ArrayList<URI>();
	private List<URI> sequences=new ArrayList<URI>();	
	private List<URI> types=new ArrayList<URI>();
	private List<Feature> features=null;
	private List<SubComponent> subComponents=null;
	private List<ComponentReference> componentReferences=null;
	private List<LocalSubComponent> localSubComponents=null;
	private List<ExternallyDefined> externallyDefineds=null;
	private List<SequenceFeature> sequenceFatures=null;
	
	
	
	
	
	
	protected Component(Model model, URI uri)
	{
		super(model, uri);
	}
	
	protected Component(Resource resource)
	{
		super(resource);
	}
	
	
	public List<URI> getTypes() {
		if (types==null)
		{
			types=RDFUtil.getPropertiesAsURIs(this.resource, URI.create("http://sbols.org/v3#type"));
		}
		return types;
	}
	
	public void setTypes(List<URI> types) {
		this.types = types;
		RDFUtil.setProperty(resource, URI.create("http://sbols.org/v3#type"), types);
	}
	
	
	public List<URI> getRoles() {
		if (roles==null)
		{
			roles=RDFUtil.getPropertiesAsURIs(this.resource, DataModel.role);
		}
		return roles;
	}
	
	public void setRoles(List<URI> roles) {
		this.roles = roles;
		RDFUtil.setProperty(resource, DataModel.role, roles);
	}
	
	
	public List<URI> getSequences() {
		if (sequences==null)
		{
			sequences=RDFUtil.getPropertiesAsURIs(this.resource, URI.create("http://sbols.org/v3#sequence"));
		}
		return sequences;
	}
	
	public void setSequences(List<URI> sequences) {
		this.sequences = sequences;
		RDFUtil.setProperty(resource, URI.create("http://sbols.org/v3#sequence"), sequences);
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
	
	public List<SubComponent> getSubComponents() throws SBOLGraphException {
		if (subComponents==null)
		{
			List<Resource> resources=RDFUtil.getResourcesWithProperty(this.resource, DataModel.Component.feature, DataModel.Entity.SubComponent);
			for (Resource res:resources)
			{
				SubComponent subComponent=new SubComponent(res);
				subComponents.add(subComponent);			
			}
		}
		return subComponents;
	}
	
	public SubComponent createSubComponent(URI uri, URI isInstanceOf) {
		SubComponent feature = new SubComponent(this.resource.getModel(), uri);
		RDFUtil.addProperty(resource, DataModel.Component.feature, feature.getUri());
		feature.setIsInstanceOf(isInstanceOf);
		if (subComponents==null)
		{
			subComponents=new ArrayList<SubComponent>();
		}
		subComponents.add(feature);
		return feature;	
	}
	
	
	public List<ComponentReference> getComponentReferences() throws SBOLGraphException {
		if (this.componentReferences==null)
		{
			List<Resource> resources=RDFUtil.getResourcesWithProperty(this.resource, DataModel.Component.feature, DataModel.Entity.ComponentReference);
			for (Resource res:resources)
			{
				ComponentReference componentReference=new ComponentReference(res);
				this.componentReferences.add(componentReference);			
			}
		}
		return this.componentReferences;
	}
	
	public ComponentReference createComponentReference(URI uri, URI feature, URI inChildOf) {
		ComponentReference componentReference= new ComponentReference(this.resource.getModel(), uri);
		RDFUtil.addProperty(resource, DataModel.Component.feature, componentReference.getUri());
		componentReference.setFeature(feature);
		componentReference.setInChildOf(inChildOf);
		if (componentReferences==null)
		{
			componentReferences=new ArrayList<ComponentReference>();
		}
		componentReferences.add(componentReference);
		return componentReference;	
	}	
	
	public List<LocalSubComponent> getLocalSubComponent() throws SBOLGraphException {
		if (this.localSubComponents==null)
		{
			List<Resource> resources=RDFUtil.getResourcesWithProperty(this.resource, DataModel.Component.feature, DataModel.Entity.LocalSubComponent);
			for (Resource res:resources)
			{
				LocalSubComponent localSubComponent=new LocalSubComponent(res);
				this.localSubComponents.add(localSubComponent);			
			}
		}
		return this.localSubComponents;
	}
	
	public LocalSubComponent createLocalSubComponent(URI uri, List<URI> types) {
		LocalSubComponent localSubComponent= new LocalSubComponent(this.resource.getModel(), uri);
		RDFUtil.addProperty(resource, DataModel.Component.feature, localSubComponent.getUri());
		localSubComponent.setTypes(types);
		if (this.localSubComponents==null)
		{
			localSubComponents=new ArrayList<LocalSubComponent>();
		}
		localSubComponents.add(localSubComponent);
		return localSubComponent;	
	}	
	
	
	public List<ExternallyDefined> getExternallyDefined() throws SBOLGraphException {
		if (this.externallyDefineds==null)
		{
			List<Resource> resources=RDFUtil.getResourcesWithProperty(this.resource, DataModel.Component.feature, DataModel.Entity.ExternallyDefined);
			for (Resource res:resources)
			{
				ExternallyDefined externallyDefined=new ExternallyDefined(res);
				this.externallyDefineds.add(externallyDefined);			
			}
		}
		return this.externallyDefineds;
	}
	
	public ExternallyDefined createExternallyDefined(URI uri, List<URI> types, URI definition) {
		ExternallyDefined externallyDefined= new ExternallyDefined(this.resource.getModel(), uri);
		RDFUtil.addProperty(resource, DataModel.Component.feature, externallyDefined.getUri());
		externallyDefined.setTypes(types);
		externallyDefined.setDefinition(definition);
		if (this.externallyDefineds==null)
		{
			externallyDefineds=new ArrayList<ExternallyDefined>();
		}
		externallyDefineds.add(externallyDefined);
		return externallyDefined;	
	}	
	
	
	public List<SequenceFeature> getSequenceFeatures() throws SBOLGraphException {
		if (this.sequenceFatures==null)
		{
			List<Resource> resources=RDFUtil.getResourcesWithProperty(this.resource, DataModel.Component.feature, DataModel.Entity.SequenceFeature);
			for (Resource res:resources)
			{
				SequenceFeature sequenceFeature=new SequenceFeature(res);
				this.sequenceFatures.add(sequenceFeature);			
			}
		}
		return this.sequenceFatures;
	}
	
	public SequenceFeature createSequenceFeature(URI uri, List<LocationBuilder> locations) {
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
	
	
	
	public URI getResourceType()
	{
		return URI.create("http://sbols.org/v3#Component");
	}
	
	

}
