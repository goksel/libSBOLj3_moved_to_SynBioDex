package org.sbolstandard.core3.entity;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.apache.jena.rdf.model.Resource;
import org.sbolstandard.core3.api.SBOLAPI;
import org.sbolstandard.core3.entity.Location.LocationBuilder;
import org.sbolstandard.core3.util.Configuration;
import org.sbolstandard.core3.util.RDFUtil;
import org.sbolstandard.core3.util.SBOLGraphException;
import org.sbolstandard.core3.util.SBOLUtil;
import org.sbolstandard.core3.validation.IdentifiedValidator;
import org.sbolstandard.core3.validation.PropertyValidator;
import org.sbolstandard.core3.validation.ValidationMessage;
import org.sbolstandard.core3.vocabulary.ComponentType;
import org.sbolstandard.core3.vocabulary.DataModel;
import org.sbolstandard.core3.vocabulary.Encoding;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
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
	
	protected Component(org.apache.jena.rdf.model.Model model, URI uri) throws SBOLGraphException
	{
		super(model, uri);
	}
	
	protected Component(Resource resource) throws SBOLGraphException
	{
		super(resource);
	}
	
	@Override
	public List<ValidationMessage> getValidationMessages() throws SBOLGraphException
	{
		List<ValidationMessage> validationMessages=super.getValidationMessages();
		List<URI> types=this.getTypes();
		if (SBOLUtil.includesMultipleRootComponentTypes(types))
		{
			validationMessages= addToValidations(validationMessages,new ValidationMessage("{COMPONENT_TYPES_INCLUDE_ONE_ROOT_TYPE}", DataModel.type));      	
		}
		
		List<SubComponent> subComponents=this.getSubComponents();
		if (subComponents!=null)
		{
			for (SubComponent subComponent: subComponents)
			{
				ValidationMessage message = new ValidationMessage("{SUBCOMPONENT_INSTANCEOF_MUST_NOT_REFER_ITS_PARENT}", DataModel.Component.feature,subComponent, subComponent.getInstanceOf());
				message.childPath(DataModel.SubComponent.instanceOf);
				validationMessages= IdentifiedValidator.assertNotEqual(this, validationMessages, subComponent.getInstanceOf(), this, message);			
				//validationMessages= IdentifiedValidator.assertNotEqual(this, validationMessages, subComponent.getIsInstanceOf().getUri(), this.getUri(), message);			
				
			}
		}
		
		List<ComponentReference> componentReferences=this.getComponentReferences();
		if (componentReferences!=null)
		{
			for (ComponentReference compRef: componentReferences)
			{
				ValidationMessage message = new ValidationMessage("{COMPONENTREFERENCE_INCHILDOF_MUST_REFER_TO_A_SUBCOMPONENT_OF_THE_PARENT}", DataModel.Component.feature,compRef, compRef.getInChildOf());
				message.childPath(DataModel.ComponentReference.inChildOf);
				validationMessages= IdentifiedValidator.assertExists(this, validationMessages, compRef.getInChildOf(), subComponents, message);			
				//validationMessages=IdentifiedValidator.assertExists(this, validationMessages, compRef.getInChildOf(), subComponents, "{COMBINATORIALREFERENCE_INCHILDOF_MUST_REFER_TO_A_SUBCOMPONENT_OF_THE_PARENT}", DataModel.ComponentReference.inChildOf);
			}
		}
		
		if (Configuration.getConfiguration().isValidateRecommendedRules())
		{
			if(types != null) {
				for (URI componentType : types) {
					boolean foundType = false;
					for(ComponentType thisComponentType: ComponentType.values()) {
						if(componentType.equals(thisComponentType.getUrl())){
							foundType = true;
						}
					}
					if(!foundType) {
						validationMessages = addToValidations(validationMessages, new ValidationMessage("{COMPONENT_TYPE_MATCH_PROPERTY}", DataModel.Component.feature, DataModel.type));
					}
				}
			}
		}
		
		List<Feature> features=this.getFeatures();
		List<Constraint> constraints=this.getConstraints();
		if (constraints!=null)
		{
			for (Constraint constraint: constraints)
			{
				ValidationMessage message = new ValidationMessage("{CONSTRAINT_SUBJECT_MUST_REFER_TO_A_FEATURE_OF_THE_PARENT}", DataModel.Component.constraint,constraint,constraint.getSubject());
				message.childPath( DataModel.Constraint.subject);
				validationMessages= IdentifiedValidator.assertExists(this, validationMessages, constraint.getSubject(), features, message);			
				
				message = new ValidationMessage("{CONSTRAINT_OBJECT_MUST_REFER_TO_A_FEATURE_OF_THE_PARENT}", DataModel.Component.constraint,constraint,constraint.getObject());
				message.childPath( DataModel.Constraint.object);
				validationMessages= IdentifiedValidator.assertExists(this, validationMessages, constraint.getObject(), features, message);			
				//validationMessages=IdentifiedValidator.assertExists(this, validationMessages, constraint.getSubject(), features, "{CONSTRAINT_SUBJECT_MUST_REFER_TO_A_FEATURE_OF_THE_PARENT}", DataModel.Constraint.subject);
				//validationMessages=IdentifiedValidator.assertExists(this, validationMessages, constraint.getObject(), features, "{CONSTRAINT_OBJECT_MUST_REFER_TO_A_FEATURE_OF_THE_PARENT}", DataModel.Constraint.object);
			}
		}
		
		List<Interaction> interactions=this.getInteractions();
		if (interactions!=null)
		{
			for (Interaction interaction:interactions)
			{
				List<Participation> participations=interaction.getParticipations();
				if (participations!=null)
				{
					for (Participation participation: participations)
					{
						ValidationMessage message = new ValidationMessage("{PARTICIPANT_PARTICIPANT_MUST_REFER_TO_A_FEATURE_OF_THE_PARENT}", DataModel.Component.interaction,interaction,participation.getParticipant());
						message.childPath(DataModel.Interaction.participation, participation).childPath(DataModel.Participation.participant);
						validationMessages=IdentifiedValidator.assertExists(this, validationMessages, participation.getParticipant(), features, message);
						
						message = new ValidationMessage("{PARTICIPANT_HIGHERORDERPARTICIPANT_MUST_REFER_TO_AN_INTERACTION_OF_THE_PARENT}", DataModel.Component.interaction,interaction,participation.getHigherOrderParticipant());
						message.childPath(DataModel.Interaction.participation, participation).childPath(DataModel.Participation.higherOrderParticipant);
						validationMessages=IdentifiedValidator.assertExists(this, validationMessages, participation.getHigherOrderParticipant(), interactions, message);
						
						//validationMessages=IdentifiedValidator.assertExists(this, validationMessages, participation.getParticipant(), features, "{PARTICIPANT_PARTICIPANT_MUST_REFER_TO_A_FEATURE_OF_THE_PARENT}", DataModel.Participation.participant);
						//validationMessages=IdentifiedValidator.assertExists(this, validationMessages, participation.getHigherOrderParticipant(), interactions, "{PARTICIPANT_HIGHERORDERPARTICIPANT_MUST_REFER_TO_AN_INTERACTION_OF_THE_PARENT}", DataModel.Participation.higherOrderParticipant);
					}
				}
			}
		}
		
		Interface compInterface=this.getInterface();
		if (compInterface!=null)
		{
			ValidationMessage message = new ValidationMessage("{INTERFACE_INPUT_MUST_REFER_TO_A_FEATURE_OF_THE_PARENT}", DataModel.Component.hasInterface,compInterface,null);
			message.childPath(DataModel.Interface.input);
			validationMessages= IdentifiedValidator.assertExistsIdentifieds(compInterface, validationMessages, compInterface.getInputs(), features, message);			
			
			message = new ValidationMessage("{INTERFACE_OUTPUT_MUST_REFER_TO_A_FEATURE_OF_THE_PARENT}", DataModel.Component.hasInterface,compInterface,null);
			message.childPath(DataModel.Interface.output);
			validationMessages= IdentifiedValidator.assertExistsIdentifieds(compInterface, validationMessages, compInterface.getOutputs(), features, message);			
			
			message = new ValidationMessage("{INTERFACE_NONDIRECTIONAL_MUST_REFER_TO_A_FEATURE_OF_THE_PARENT}", DataModel.Component.hasInterface,compInterface,null);
			message.childPath(DataModel.Interface.nondirectional);
			validationMessages= IdentifiedValidator.assertExistsIdentifieds(compInterface, validationMessages, compInterface.getNonDirectionals(), features, message);			
			
			//validationMessages= IdentifiedValidator.assertExists(compInterface, validationMessages, compInterface.getInputs(), features, "{INTERFACE_INPUT_MUST_REFER_TO_A_FEATURE_OF_THE_PARENT}", DataModel.Interface.input);			
			//validationMessages= IdentifiedValidator.assertExists(compInterface, validationMessages, compInterface.getOutputs(), features, "{INTERFACE_OUTPUT_MUST_REFER_TO_A_FEATURE_OF_THE_PARENT}", DataModel.Interface.output);
			//validationMessages= IdentifiedValidator.assertExists(compInterface, validationMessages, compInterface.getNonDirectionals(), features, "{INTERFACE_NONDIRECTIONAL_MUST_REFER_TO_A_FEATURE_OF_THE_PARENT}", DataModel.Interface.nondirectional);
		}
		
		validationMessages= IdentifiedValidator.assertExists(this, DataModel.Component.sequence, this.resource, getSequences(), validationMessages);
		validationMessages= IdentifiedValidator.assertExists(this, DataModel.Component.feature, this.resource, getFeatures(), validationMessages);
		validationMessages= IdentifiedValidator.assertExists(this, DataModel.Component.interaction, this.resource, getInteractions(), validationMessages);
		validationMessages= IdentifiedValidator.assertExists(this, DataModel.Component.constraint, this.resource, getConstraints(), validationMessages);
		validationMessages= IdentifiedValidator.assertExists(this, DataModel.Component.model, this.resource, getModels(), validationMessages);
		validationMessages= IdentifiedValidator.assertEquals(this, DataModel.Component.hasInterface, this.resource, getInterface(), validationMessages);
		return validationMessages;
	}
	
	@Valid
	@NotEmpty(message = "{COMPONENT_TYPES_NOT_EMPTY}")
	public List<URI> getTypes() {
		return RDFUtil.getPropertiesAsURIs(this.resource,DataModel.type);
	}
	
	public void setTypes(@NotEmpty(message = "{COMPONENT_TYPES_NOT_EMPTY}")List<URI> types) throws SBOLGraphException {
		PropertyValidator.getValidator().validate(this, "setTypes", new Object[] {types}, List.class);
		RDFUtil.setProperty(resource, DataModel.type, types);
	}
	
	public List<URI> getRoles() {
		return RDFUtil.getPropertiesAsURIs(this.resource, DataModel.role);
	}
	
	public void setRoles(List<URI> roles) {
		RDFUtil.setProperty(resource, DataModel.role, roles);
	}
	
	
	/*public List<URI> getSequences() {
		return RDFUtil.getPropertiesAsURIs(this.resource, DataModel.Component.sequence);
	}
	*/
	
	public List<Sequence> getSequences() throws SBOLGraphException {
		return addToList(DataModel.Component.sequence, Sequence.class, DataModel.Sequence.uri);
	}
	
	public void setSequences(List<Sequence> sequences) {
		RDFUtil.setProperty(resource, DataModel.Component.sequence, SBOLUtil.getURIs(sequences));
	}
	
	public List<Sequence> getSequences(Encoding encoding) throws SBOLGraphException
	{
		ArrayList<Sequence> result=null;
		List<Sequence> sequences=this.getSequences();
		if (encoding!=null && sequences!=null)
		{
			for (Sequence sequence: sequences)
			{
				if (sequence.getEncoding().equals(encoding))
				{
					if (result==null)
					{
						result=new ArrayList<Sequence>();
					}
					result.add(sequence);
				}
			}
		}
		return result;
		//return filterIdentifieds(SBOLUtil.getURIs(this.getSequences()),DataModel.Sequence.encoding, encoding.getUri().toString());
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
	
	private void addToFeatureList(List<Feature> listA, List<? extends Feature> listB)
	{
		if (listB!=null && listB.size()>0)
		{
			listA.addAll(listB);
		}
	}
	
	public List<Feature> getFeatures() throws SBOLGraphException{
		List<Feature> features=new ArrayList<Feature>();
		addToFeatureList(features, getSubComponents());
		addToFeatureList(features, getComponentReferences());
		addToFeatureList(features, getLocalSubComponents());
		addToFeatureList(features, getExternallyDefineds());
		addToFeatureList(features, getSequenceFeatures());	
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
	
	public SubComponent createSubComponent(URI uri, Component isInstanceOf) throws SBOLGraphException
	{
		SubComponent feature = new SubComponent(this.resource.getModel(), uri);
		feature.setInstanceOf(isInstanceOf);
		addToList(feature, DataModel.Component.feature);
		return feature;	
	}
	
	private SubComponent createSubComponent(String displayId, Component isInstanceOf) throws SBOLGraphException
	{
		return createSubComponent(SBOLAPI.append(this.getUri(), displayId), isInstanceOf);
	}
	
	public SubComponent createSubComponent(Component isInstanceOf) throws SBOLGraphException
	{
		String displayId=SBOLAPI.createLocalName(DataModel.SubComponent.uri, getSubComponents());
		return createSubComponent(displayId, isInstanceOf);
	}
	
	@Valid
	public List<ComponentReference> getComponentReferences() throws SBOLGraphException {
		return addToList(DataModel.Component.feature, ComponentReference.class, DataModel.ComponentReference.uri);
	}
	
	public ComponentReference createComponentReference(URI uri, Feature feature, SubComponent inChildOf) throws SBOLGraphException {
		ComponentReference componentReference= new ComponentReference(this.resource.getModel(), uri);
		componentReference.setRefersTo(feature);
		componentReference.setInChildOf(inChildOf);
		addToList(componentReference, DataModel.Component.feature);
		return componentReference;	
	}
	
	private ComponentReference createComponentReference(String displayId, Feature feature, SubComponent inChildOf) throws SBOLGraphException {
		return createComponentReference(SBOLAPI.append(this.getUri(), displayId), feature, inChildOf);	
	}
	
	public ComponentReference createComponentReference(Feature feature, SubComponent inChildOf) throws SBOLGraphException {
		String displayId=SBOLAPI.createLocalName(DataModel.ComponentReference.uri, getComponentReferences());
		return createComponentReference(displayId, feature, inChildOf);	
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
	
	/*public SequenceFeature createSequenceFeature(URI uri, List<LocationBuilder> locations) throws SBOLGraphException {
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
	*/
	
	public SequenceFeature createSequenceFeature(Sequence sequence) throws SBOLGraphException {
		SequenceFeature feature=createSequenceFeature();
		feature.createEntireSequence(sequence);
		return feature;
	}
	
	public SequenceFeature createSequenceFeature(int at, Sequence sequence) throws SBOLGraphException {
		SequenceFeature feature=createSequenceFeature();
		feature.createCut(at, sequence);
		return feature;
	}
	
	public SequenceFeature createSequenceFeature(int start, int end, Sequence sequence) throws SBOLGraphException {
		SequenceFeature feature=createSequenceFeature();
		feature.createRange(start, end, sequence);
		return feature;
	}
	
	private SequenceFeature createSequenceFeature() throws SBOLGraphException {
		String displayId=SBOLAPI.createLocalName(DataModel.SequenceFeature.uri, getSequenceFeatures());	
		SequenceFeature seqFeature=createSequenceFeature(displayId);
		return seqFeature;
	}
	
	private SequenceFeature createSequenceFeature(String displayId) throws SBOLGraphException {
		SequenceFeature seqFeature=createSequenceFeature(SBOLAPI.append(this.getUri(), displayId));
		return seqFeature;
	}
	
	private SequenceFeature createSequenceFeature(URI uri) throws SBOLGraphException {
		SequenceFeature sequenceFeature= new SequenceFeature(this.resource.getModel(), uri);
		RDFUtil.addProperty(resource, DataModel.Component.feature, sequenceFeature.getUri());
		return sequenceFeature;	
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
		return addToList(DataModel.Component.interaction, Interaction.class, DataModel.Interaction.uri);
	}
	
	//Constraint
	public Constraint createConstraint(URI uri, URI restriction, Feature subject, Feature object) throws SBOLGraphException {
		Constraint constraint= new Constraint(this.resource.getModel(), uri);
		constraint.setRestriction(restriction);
		constraint.setSubject(subject);
		constraint.setObject(object);
		addToList(constraint, DataModel.Component.constraint);
		return constraint;
	}
	
	private Constraint createConstraint(String displayId, URI restriction, Feature subject, Feature object) throws SBOLGraphException {
		return createConstraint(SBOLAPI.append(this.getUri(), displayId), restriction, subject, object);
	}
	
	public Constraint createConstraint(URI restriction, Feature subject, Feature object) throws SBOLGraphException {
		String displayId=SBOLAPI.createLocalName(DataModel.Constraint.uri, getConstraints());	
		return createConstraint(displayId, restriction, subject, object);
	}
	
	@Valid
	public List<Constraint> getConstraints() throws SBOLGraphException {
		return addToList(DataModel.Component.constraint, Constraint.class, DataModel.Constraint.uri);
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
	
	@Valid
	public Interface getInterface() throws SBOLGraphException {
		return contsructIdentified(DataModel.Component.hasInterface, Interface.class, DataModel.Interface.uri);
	}
	
	/*public List<URI> getModels() {
		return RDFUtil.getPropertiesAsURIs(this.resource, DataModel.Component.model);
	}
	
	public void setModels(List<URI> models) {
		RDFUtil.setProperty(resource, DataModel.Component.model, models);
	}*/

	public List<Model> getModels() throws SBOLGraphException {
		return addToList(DataModel.Component.model, Model.class, DataModel.Model.uri);
	}
	
	public void setModels(List<Model> models) {
		RDFUtil.setProperty(resource, DataModel.Component.model, SBOLUtil.getURIs(models));
	}
	
	
	public URI getResourceType()
	{
		return DataModel.Component.uri;
	}
	
	@Override
	public List<Identified> getChildren() throws SBOLGraphException {
		List<Identified> identifieds=super.getChildren();
		identifieds=addToList(identifieds, this.getFeatures());
		identifieds=addToList(identifieds, this.getInteractions());
		identifieds=addToList(identifieds, this.getInterface());
		return identifieds;
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

