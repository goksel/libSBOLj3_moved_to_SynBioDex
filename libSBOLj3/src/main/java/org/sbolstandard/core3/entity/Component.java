package org.sbolstandard.core3.entity;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
import org.sbolstandard.core3.vocabulary.RestrictionType;
import org.sbolstandard.core3.vocabulary.RestrictionType.ConstraintRestriction;
import org.sbolstandard.core3.vocabulary.RestrictionType.IdentityRestriction;
import org.sbolstandard.core3.vocabulary.RestrictionType.OrientationRestriction;

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
				if (subComponent.getInstanceOf()!=null)
				{
					ValidationMessage message = new ValidationMessage("{SUBCOMPONENT_INSTANCEOF_MUST_NOT_REFER_ITS_PARENT}", DataModel.Component.feature,subComponent, subComponent.getInstanceOf().getUri());
					message.childPath(DataModel.SubComponent.instanceOf);
					validationMessages= IdentifiedValidator.assertNotEqual(this, validationMessages, subComponent.getInstanceOf(), this, message);			
				//validationMessages= IdentifiedValidator.assertNotEqual(this, validationMessages, subComponent.getIsInstanceOf().getUri(), this.getUri(), message);			
				}
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
				/*
				Feature referredFeature=compRef.getRefersTo();
				if (referredFeature instanceof ComponentReference)
				{
					ComponentReference referredCompRef=(ComponentReference) referredFeature;
					SubComponent childSubComponent=referredCompRef.getInChildOf();
					Component parentComponent=compRef.getInChildOf().getInstanceOf();
					message = new ValidationMessage("{COMPONENTREFERENCE_INCHILDOF_SUBCOMPONENT_VALID}", DataModel.Component.feature,compRef, childSubComponent);
					message.childPath(DataModel.ComponentReference.refersTo, referredCompRef).childPath(DataModel.ComponentReference.inChildOf, childSubComponent);
					validationMessages= IdentifiedValidator.assertExists(this, validationMessages, childSubComponent, parentComponent.getSubComponents(), message);			
				
					message = new ValidationMessage("{COMPONENTREFERENCE_REFERSTO_CHILDCOMPONENTREFERENCE_VIA_SUBCOMPONENT}", DataModel.Component.feature,compRef, referredCompRef);
					message.childPath(DataModel.ComponentReference.refersTo, referredCompRef);
					validationMessages= IdentifiedValidator.assertExists(this, validationMessages, referredCompRef, parentComponent.getComponentReferences(), message);				
				}
				*/
			}
		}
		
		/*Removed the validation for sbol3-10604 ⋆ A Component SHOULD have a type property from Table 2.
		 * This is a best practise rule.
		 * if (Configuration.getInstance().isValidateRecommendedRules())
		{
			if(types != null) {
				boolean foundType = false;
				
				for (URI componentType : types) {
					for(ComponentType thisComponentType: ComponentType.values()) {
						if(componentType.equals(thisComponentType.getUri())){
							foundType = true;
							break;
						}
					}
				}
				if(!foundType) {
					validationMessages = addToValidations(validationMessages, new ValidationMessage("{COMPONENT_TYPE_MATCH_PROPERTY}", DataModel.type,types));
				}
			}
		}*/
		
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
			
				//CONSTRAINT_RESTRICTION_FEATURES_COMPATIBLE
				validationMessages=assertValidConstraintsAreCompatibleWithFeatures(constraint, validationMessages);
				
			}
		}
		
		if (Configuration.getInstance().isValidateRecommendedRules())
		{
			//COMPONENT_TYPE_AT_MOST_ONE_TOPOLOGY_TYPE
			validationMessages=IdentifiedValidator.assertAtMostOneTopologyType(types, validationMessages, "{COMPONENT_TYPE_AT_MOST_ONE_TOPOLOGY_TYPE}");
			
			//COMPONENT_TYPE_ONLY_DNA_OR_RNA_INCLUDE_STRAND_OR_TOPOLOGY
			validationMessages=IdentifiedValidator.assertOnlyDNAOrRNAComponentsIncludeStrandOrTopology(types, validationMessages, "{COMPONENT_TYPE_ONLY_DNA_OR_RNA_INCLUDE_STRAND_OR_TOPOLOGY}");
			
			//COMPONENT_TYPE_ONLY_DNA_OR_RNA_INCLUDE_SO_FEATURE_ROLE
			validationMessages=IdentifiedValidator.assertOnlyDNAOrRNAIdentifiedsIncludeSOFeatureRole(types, Configuration.getInstance().getSoSequenceFeatures(), this.getRoles(), validationMessages, "{COMPONENT_TYPE_ONLY_DNA_OR_RNA_INCLUDE_SO_FEATURE_ROLE}", getResourceType(),this); 
					
			//COMPONENT_TYPE_IF_DNA_OR_RNA_SHOULD_INCLUDE_ONE_SO_FEATURE_ROLE
			validationMessages=IdentifiedValidator.assertIfDNAOrRNAThenIdentifiedShouldIncludeOneSOFeatureRole(types, getRoles(), validationMessages, "{COMPONENT_TYPE_IF_DNA_OR_RNA_SHOULD_INCLUDE_ONE_SO_FEATURE_ROLE}", this);					
		}
		
		// COMPONENT_TYPE_SEQUENCE_TYPE_MATCH_COMPONENT_TYPE
		List<Sequence> sequences = this.getSequences();
		if (sequences != null && types != null) {
			for (URI componentTypeURI : types) {
				boolean foundTypeMatch = false;

				ComponentType componentType = ComponentType.get(componentTypeURI);
				if(componentType != null) {
					List<Encoding> typeMatches = ComponentType.checkComponentTypeMatch(componentType);
					outerloop:
					for (Sequence sequence : sequences) {
						Encoding encoding = Encoding.get(sequence.getEncoding());
						if (encoding != null) {
							for (Encoding typeURI: typeMatches) {
								if(typeURI.getUri().equals(encoding.getUri())) {
									foundTypeMatch = true;
									break outerloop;
								}
							}
						}
					}
					
					if (!foundTypeMatch) {
						validationMessages = addToValidations(validationMessages,
								new ValidationMessage("{COMPONENT_TYPE_SEQUENCE_TYPE_MATCH_COMPONENT_TYPE}",
										DataModel.type, componentType));
					}
				
				}
			}
		}

		//SUBCOMPONENT_OBJECTS_CIRCULAR_REFERENCE_CHAIN
		if(subComponents!=null) {
			validationMessages = checkSubComponentMatchToRoot(validationMessages, this, this.getUri(), new ArrayList<URI>());
		}
		
		//COMPONENT_TYPE_SEQUENCE_LENGTH_MATCH
		if (Configuration.getInstance().isValidateRecommendedRules() && sequences != null && types != null) {
			HashMap<Encoding, ArrayList<Integer>> elementLengths = new HashMap<>();
			// loop through all sequences, get the elements for each sequence and store them in a hashmap for later comparison
			for(Sequence sequence: sequences) {
				Encoding encoding = Encoding.get(sequence.getEncoding());
				if (encoding!=null)
				{
					String elements = sequence.getElements();
					if (elements!=null)
					{
						int elementLength = elements.length();
						if(elementLengths.containsKey(encoding)) {
							ArrayList<Integer> lengthList = elementLengths.get(encoding);
							lengthList.add(elementLength);
							elementLengths.put(encoding, lengthList);
						}else {
							ArrayList<Integer> lengthList = new ArrayList<>();
							lengthList.add(elementLength);
							elementLengths.put(encoding, lengthList);
						}
					}
				}
			}
			
			//having added the sequence lengths by element encoding, now check that everything matches
			for (Map.Entry<Encoding, ArrayList<Integer>> entry : elementLengths.entrySet()) {
				boolean sizesMatch = true;
				
				Encoding encoding = entry.getKey();
				ArrayList<Integer> lengthList = entry.getValue();
				if(lengthList.size()>1) {
					int firstSize = lengthList.get(0);
					//System.out.println("First: "+firstSize);
					//check all elements of the array against the first one to find a size
					for(int i = 1; i< lengthList.size();i++) {
						//System.out.println(i+": "+lengthList.get(i));
						if(lengthList.get(i) != firstSize) {
							sizesMatch = false;
						}
					}
				}
				if(!sizesMatch) {
					ValidationMessage message=new ValidationMessage("{COMPONENT_TYPE_SEQUENCE_LENGTH_MATCH}",DataModel.Component.sequence, SBOLUtil.getURIs(getSequences()));
					validationMessages = addToValidations(validationMessages,message);
				}
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
		
		if (features != null) {
			List<Sequence> entireSequences = getEntireSequences(features);
			for (Feature feature : features) {
				if (feature instanceof FeatureWithLocation) {
					FeatureWithLocation locFeature = (FeatureWithLocation) feature;
					List<Location> locations = locFeature.getLocations();
					validationMessages= validateComponentLocation(validationMessages, locations, locFeature, sequences, entireSequences,DataModel.SequenceFeature.location, "{LOCATION_SEQUENCE_VALID}");
				}
				if (feature instanceof SubComponent){
					SubComponent subComponent=(SubComponent) feature;
					List<Location> locations = subComponent.getSourceLocations();
					validationMessages= validateComponentLocation(validationMessages, locations, subComponent, sequences, entireSequences, DataModel.SubComponent.sourceLocation, "{LOCATION_SOURCE_SEQUENCE_VALID}");		
				}
			}
		}
		validationMessages= IdentifiedValidator.assertExists(this, DataModel.Component.sequence, this.resource, getSequences(), validationMessages);
		validationMessages= IdentifiedValidator.assertExists(this, DataModel.Component.feature, this.resource, getFeatures(), validationMessages);
		validationMessages= IdentifiedValidator.assertExists(this, DataModel.Component.interaction, this.resource, getInteractions(), validationMessages);
		validationMessages= IdentifiedValidator.assertExists(this, DataModel.Component.constraint, this.resource, getConstraints(), validationMessages);
		validationMessages= IdentifiedValidator.assertExists(this, DataModel.Component.model, this.resource, getModels(), validationMessages);
		validationMessages= IdentifiedValidator.assertEquals(this, DataModel.Component.hasInterface, this.resource, getInterface(), validationMessages);
		return validationMessages;
	}

	private List<ValidationMessage> validateComponentLocation(List<ValidationMessage> validationMessages, List<Location> locations, Feature feature, List<Sequence> sequences, List<Sequence> entireSequences, URI locationProperty,String message) throws SBOLGraphException {
		if (locations != null) {
			for (Location location : locations) {
				boolean valid = false;
				if (!(location instanceof EntireSequence)) {
					Sequence locSequence = location.getSequence();
					if (locSequence != null) {
						if (sequences != null && SBOLUtil.contains(sequences, locSequence)) {
							valid = true;
						} else if (entireSequences != null && SBOLUtil.contains(entireSequences, locSequence)) {
							valid = true;
						}
					}
					if (!valid) {
						ValidationMessage validationMessage = new ValidationMessage(message, DataModel.Component.feature, feature, locSequence);
						validationMessage.childPath(locationProperty, location).childPath(DataModel.Location.sequence);
						validationMessages = IdentifiedValidator.addToValidations(validationMessages, validationMessage);
					}
				}
			}
		}
		return validationMessages;
	}
			

	private List<Sequence> getEntireSequences(List<Feature> features) throws SBOLGraphException {
		List<Sequence> entireSequences = null;
		if (features != null) {
			for (Feature feature : features) {
				if (feature instanceof FeatureWithLocation) {
					FeatureWithLocation locFeature = (FeatureWithLocation) feature;
					List<Location> locations = locFeature.getLocations();
					if (locations != null) {
						for (Location location : locations) {
							if (location instanceof EntireSequence) {
								Sequence entireSequence = location.getSequence();
								if (entireSequences == null) {
									entireSequences = new ArrayList<Sequence>();
								}
								entireSequences.add(entireSequence);
							}
						}
					}
				}
			}
		}
		return entireSequences;
	}
	
	//CONSTRAINT_RESTRICTION_FEATURES_COMPATIBLE
	private List<ValidationMessage> assertValidOrientationConstraintRestrictions(Constraint constraint, List<ValidationMessage> validationMessages) throws SBOLGraphException
		{
			Feature object=constraint.getObject();
			Feature subject=constraint.getSubject();
			boolean valid=true;
			if (object.getOrientation()!=null && subject.getOrientation()!=null)
			{
				if (constraint.getRestriction().equals(OrientationRestriction.sameOrientationAs.getUri()) && !object.getOrientation().equals(subject.getOrientation()))
				{
					valid=false;
				}
				else if (constraint.getRestriction().equals(OrientationRestriction.oppositeOrientationAs.getUri())
					&& object.getOrientation().equals(subject.getOrientation()))
				{
					valid=false;
				}
					
				if (!valid)
				{
					ValidationMessage message=new ValidationMessage("{CONSTRAINT_RESTRICTION_FEATURES_COMPATIBLE}" + " Restricton:" + constraint.getRestriction(), DataModel.Component.constraint, object, object.getOrientation().getUri());
					message.childPath(DataModel.orientation);
					validationMessages=IdentifiedValidator.addToValidations(validationMessages, message);
				}
			}
			
			return validationMessages;
		}
		
	//CONSTRAINT_RESTRICTION_SEQUENCES_COMPATIBLE
	private List<ValidationMessage> assertValidSequentialConstraintRestriction(Constraint constraint, List<ValidationMessage> validationMessages) throws SBOLGraphException
	{
		Feature object=constraint.getObject();
		Feature subject=constraint.getSubject();
		//TODO
		
		return validationMessages;
	}
	
	//CONSTRAINT_RESTRICTION_FEATURES_COMPATIBLE
	private List<ValidationMessage> assertValidIdentityConstraintRestrictions(Constraint constraint, List<ValidationMessage> validationMessages) throws SBOLGraphException {
		Feature object = constraint.getObject();
		Feature subject = constraint.getSubject();
		if ((subject instanceof ComponentReference) && (object instanceof ComponentReference)) {
			ComponentReference objectCompRef = (ComponentReference) object;
			ComponentReference subjectCompRef = (ComponentReference) subject;
			ValidationMessage message = null;
			
			Feature referredObject = getReferred(objectCompRef);
			Feature referredSubject = getReferred(subjectCompRef);
			if (referredObject != null && referredSubject != null) {
				if ((referredSubject instanceof SubComponent) && (referredObject instanceof SubComponent)) {
					SubComponent subjectSubComponent = (SubComponent) referredSubject;
					SubComponent objectSubComponent = (SubComponent) referredObject;
					boolean valid=true;
					if (constraint.getRestriction().equals(RestrictionType.IdentityRestriction.verifyIdentical.getUri()) && !subjectSubComponent.getInstanceOf().getUri().equals(objectSubComponent.getInstanceOf().getUri())) {
						valid=false;
					} 
					else if (constraint.getRestriction().equals(RestrictionType.IdentityRestriction.differentFrom.getUri()) && subjectSubComponent.getInstanceOf().getUri().equals(objectSubComponent.getInstanceOf().getUri())) {
						valid=false;
					}
					if (!valid)
					{
						message = new ValidationMessage("{CONSTRAINT_RESTRICTION_FEATURES_COMPATIBLE}" + " Restriction:" + constraint.getRestriction(), DataModel.Component.constraint, constraint, SBOLUtil.getURIs(Arrays.asList(subjectSubComponent.getInstanceOf(), objectSubComponent.getInstanceOf())));
						message.childPath(DataModel.Constraint.subject, subjectCompRef).childPath(DataModel.ComponentReference.refersTo, referredSubject).childPath(DataModel.SubComponent.instanceOf, subjectSubComponent.getInstanceOf());
					}
				} 
				else if ((referredSubject instanceof ExternallyDefined) && (referredObject instanceof ExternallyDefined)) {
					boolean valid=true;
					ExternallyDefined subjectExternallyDef = (ExternallyDefined) referredSubject;
					ExternallyDefined objectExternallyDef = (ExternallyDefined) referredObject;
					if (constraint.getRestriction().equals(RestrictionType.IdentityRestriction.verifyIdentical.getUri()) && !subjectExternallyDef.getDefinition().equals(objectExternallyDef.getDefinition())) {
						valid=false;
					} 
					else if (constraint.getRestriction().equals(RestrictionType.IdentityRestriction.differentFrom.getUri()) && subjectExternallyDef.getDefinition().equals(objectExternallyDef.getDefinition())) {
						valid=false;
					}
					if (!valid)
					{
						message = new ValidationMessage("{CONSTRAINT_RESTRICTION_FEATURES_COMPATIBLE}" + " Restriction:" + constraint.getRestriction(), DataModel.Component.constraint, constraint, Arrays.asList(subjectExternallyDef.getDefinition(), objectExternallyDef.getDefinition()));
						message.childPath(DataModel.Constraint.subject, subjectCompRef).childPath(DataModel.ComponentReference.refersTo, referredSubject).childPath(DataModel.ExternalyDefined.definition);
		
					}
				} 
				else// subject and object have different types
				{
					//message=constructMessage(subjectCompRef, constraint, referredSubject,null);
					message = new ValidationMessage("{CONSTRAINT_RESTRICTION_FEATURES_COMPATIBLE}" + " Restriction:" + constraint.getRestriction(), DataModel.Component.constraint, constraint, SBOLUtil.getURIs(Arrays.asList(referredSubject, referredObject)));
					message.childPath(DataModel.Constraint.subject, subjectCompRef).childPath(DataModel.ComponentReference.refersTo, referredSubject);

				}
			}
			if (message!=null) {
				validationMessages = IdentifiedValidator.addToValidations(validationMessages, message);
			}
		}
		return validationMessages;
	}
		
	//CONSTRAINT_RESTRICTION_FEATURES_COMPATIBLE
	private List<ValidationMessage> assertValidConstraintsAreCompatibleWithFeatures(Constraint constraint, List<ValidationMessage> validationMessages) throws SBOLGraphException
	{
		if (constraint.getRestriction()!=null)
		{
			if (constraint.getRestriction().equals(RestrictionType.IdentityRestriction.verifyIdentical.getUri()) ||
					constraint.getRestriction().equals(RestrictionType.IdentityRestriction.differentFrom.getUri())) {
				validationMessages= assertValidIdentityConstraintRestrictions(constraint, validationMessages);
			}
			else if (RestrictionType.OrientationRestriction.get(constraint.getRestriction())!=null) {
				validationMessages= assertValidOrientationConstraintRestrictions(constraint, validationMessages);
			}
			else if (RestrictionType.SequentialRestriction.get(constraint.getRestriction())!=null) {
				validationMessages= assertValidSequentialConstraintRestriction(constraint, validationMessages);
			}
		}		
		return validationMessages;
	}
	
	private Feature getReferred(ComponentReference initialCompRef) throws SBOLGraphException
	{
		Feature referredTo=initialCompRef.getRefersTo();
		Set<URI> visited=new HashSet<URI>();
		visited.add(initialCompRef.getUri());
		
		while (referredTo instanceof ComponentReference)
		{
			if (visited.contains(referredTo.getUri()))
			{
				break;//Cycle detected, referredTo will refer to a ComponentReference, rather than SubComponent or ExternallyDefined
			}
			else
			{
				visited.add(referredTo.getUri());			
			}
			ComponentReference referredToCompRef= (ComponentReference) referredTo;
			referredTo=referredToCompRef.getRefersTo();
		}
		return referredTo;
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
				if (sequence.getEncoding().equals(encoding.getUri()))
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
	
	public Constraint createConstraint(ConstraintRestriction restriction, Feature subject, Feature object) throws SBOLGraphException {
		return createConstraint(restriction.getUri(), subject, object);
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
	
	public List<ValidationMessage> checkSubComponentMatchToRoot(List<ValidationMessage> messages, Component currentComponent, URI rootURI, ArrayList<URI> visitedSubComponents) throws SBOLGraphException {
		List<SubComponent> subComponents=currentComponent.getSubComponents();
		if(subComponents!=null) {
			for(SubComponent subComponent: subComponents) {	
				Component componentInstance = subComponent.getInstanceOf();
				if (componentInstance!=null)
				{		
					if(componentInstance.getUri().equals(rootURI)) {
						ValidationMessage message= new ValidationMessage("{SUBCOMPONENT_OBJECTS_CIRCULAR_REFERENCE_CHAIN}", DataModel.Component.feature,subComponent, currentComponent);
						message.childPath(DataModel.SubComponent.instanceOf, subComponent.getInstanceOf());
						messages=addToValidations(messages, message);
					}
					
					// also check any sub components of the subcomponent if it has not been checked already			
					if(!visitedSubComponents.contains(componentInstance.getUri())) {
						visitedSubComponents.add(componentInstance.getUri());
						messages = checkSubComponentMatchToRoot(messages, componentInstance, rootURI, visitedSubComponents);
					}
				}
			}
		}
		return messages;
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

