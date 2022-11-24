package org.sbolstandard.core3.entity;

import java.net.URI;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.sbolstandard.core3.util.Configuration;
import org.sbolstandard.core3.util.RDFUtil;
import org.sbolstandard.core3.util.SBOLGraphException;
import org.sbolstandard.core3.util.SBOLUtil;
import org.sbolstandard.core3.validation.IdentifiedValidator;
import org.sbolstandard.core3.validation.PropertyValidator;
import org.sbolstandard.core3.validation.ValidationMessage;
import org.sbolstandard.core3.vocabulary.DataModel;
import org.sbolstandard.core3.vocabulary.RestrictionType;
import org.sbolstandard.core3.vocabulary.RestrictionType.ConstraintRestriction;
import org.sbolstandard.core3.vocabulary.RestrictionType.OrientationRestriction;
import org.sbolstandard.core3.vocabulary.RestrictionType.SequentialRestriction;

import jakarta.validation.constraints.NotNull;

public class Constraint extends Identified{
	/*private URI restriction=null;
	private URI subject=null;
	private URI object=null;*/

	protected  Constraint(Model model,URI uri) throws SBOLGraphException
	{
		super(model, uri);
	}
	
	protected  Constraint(Resource resource) throws SBOLGraphException
	{
		super(resource);
	}
	
	@Override
	public List<ValidationMessage> getValidationMessages() throws SBOLGraphException
	{
		List<ValidationMessage> validationMessages=super.getValidationMessages();
		validationMessages=IdentifiedValidator.assertNotEqual(this, validationMessages, this.getObject(), this.getSubject(), "{CONSTRAINT_OBJECT_AND_SUBJECT_ARE_NOT_EQUAL}", DataModel.Constraint.object);
		validationMessages= IdentifiedValidator.assertEquals(this, DataModel.Constraint.subject, this.resource, this.getSubject(), validationMessages);
		validationMessages= IdentifiedValidator.assertEquals(this, DataModel.Constraint.object, this.resource, this.getObject(), validationMessages);
		
		
		if (Configuration.getInstance().isValidateRecommendedRules())
		{
			URI restrictionUri=getRestriction();
			if (!RestrictionType.getUris().contains(restrictionUri))
			{
				ValidationMessage message=new ValidationMessage("{CONSTRAINT_RESTRICTION_VALID}", DataModel.Constraint.restriction, restrictionUri);
				validationMessages=IdentifiedValidator.addToValidations(validationMessages, message);
			}
		}
		
		validationMessages=assertValidConstraintsAreCompatibleWithFeatures(validationMessages);
		
		return validationMessages;
	}
	
	
	//CONSTRAINT_RESTRICTION_FEATURES_COMPATIBLE
		private List<ValidationMessage> assertValidConstraintsAreCompatibleWithFeatures(List<ValidationMessage> validationMessages) throws SBOLGraphException
		{
			URI restrictionUri=getRestriction();
			
			if (restrictionUri!=null)
			{
				if (RestrictionType.OrientationRestriction.get(restrictionUri)!=null) {
					validationMessages= assertValidOrientationConstraintRestrictions(this, validationMessages);
				}
				else if (RestrictionType.SequentialRestriction.get(this.getRestriction())!=null) {
					validationMessages= assertValidSequentialConstraintRestriction(this, validationMessages);
				}
			}	
			return validationMessages;
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
						//ValidationMessage message=new ValidationMessage("{CONSTRAINT_RESTRICTION_FEATURES_COMPATIBLE}" + " Restricton:" + constraint.getRestriction(), DataModel.Component.constraint, object, object.getOrientation().getUri());
						//message.childPath(DataModel.orientation);
						String messageContent=String.format("%s %s Restriction:%s", "{CONSTRAINT_RESTRICTION_FEATURES_COMPATIBLE}", ValidationMessage.INFORMATION_SEPARATOR, constraint.getRestriction());
						ValidationMessage message=new ValidationMessage(messageContent, DataModel.Constraint.object, object, object.getOrientation().getUri());
						message.childPath(DataModel.orientation);					
						validationMessages=IdentifiedValidator.addToValidations(validationMessages, message);
					}
				}
				return validationMessages;
			}
		
	//CONSTRAINT_RESTRICTION_SEQUENCES_COMPATIBLE = sbol3-11706 - If the restriction property of a Constraint is drawn from Table 10 and if the Feature objects referred to by the subject and object properties both have hasLocation properties with Location objects whose hasSequence property refers to the same Sequence, then the positions of the referred Location objects MUST comply with the relation specified in Table 10.
	private List<ValidationMessage> assertValidSequentialConstraintRestriction(Constraint constraint, List<ValidationMessage> validationMessages) throws SBOLGraphException
	{
		if (isApplySequentialConstraintRestriction(constraint))
		{
			FeatureWithLocation object = (FeatureWithLocation) constraint.getObject();
			FeatureWithLocation subject = (FeatureWithLocation) constraint.getSubject();
			List<Location> objectLocations= object.getLocations();
			List<Location> subjectLocations= subject.getLocations();
			
			//Apply the restrictions:
			Set<URI> restrictions=getPairwiseRestrictions();
			
			if (restrictions.contains(constraint.getRestriction()))
			{
				validationMessages=assertPairwiseLocationRestrictions(validationMessages, constraint, subjectLocations, objectLocations, subject);
			}
			else if (constraint.getRestriction().equals(RestrictionType.SequentialRestriction.overlaps.getUri()))
			{
				//SomeValues --> At least one
				validationMessages=assertForAnySubjectObjectLocationPair(validationMessages, constraint, subjectLocations, objectLocations, subject);
			}		
			else if (constraint.getRestriction().equals(RestrictionType.SequentialRestriction.contains.getUri()) ||
					constraint.getRestriction().equals(RestrictionType.SequentialRestriction.strictlyContains.getUri())){
				validationMessages=assertForEveryObjectLocation(validationMessages, constraint, subjectLocations, objectLocations, object);
			}		
			else if (constraint.getRestriction().equals(RestrictionType.SequentialRestriction.equals.getUri())) {
				validationMessages=assertEquality(validationMessages, constraint, subjectLocations, objectLocations, subject);		
			}
		}
		return validationMessages;
	}
	
	private Set<URI> getPairwiseRestrictions()
	{
		Set<URI> restrictions=new HashSet<URI>();
		restrictions.add(RestrictionType.SequentialRestriction.precedes.getUri());
		restrictions.add(RestrictionType.SequentialRestriction.strictlyPrecedes.getUri());
		restrictions.add(RestrictionType.SequentialRestriction.meets.getUri());
		restrictions.add(RestrictionType.SequentialRestriction.starts.getUri());
		restrictions.add(RestrictionType.SequentialRestriction.finishes.getUri());
		return restrictions;
	}

	public List<ValidationMessage> assertPairwiseLocationRestrictions(List<ValidationMessage> validationMessages, Constraint constraint, List<Location> subjectLocations, List<Location> objectLocations, Feature subject)
			throws SBOLGraphException {
		boolean validLocation = true;
		Pair<Integer, Integer> objCoord = Location.getStartEnd(objectLocations);
		Pair<Integer, Integer> sbjCoord = Location.getStartEnd(subjectLocations);

		if (constraint.getRestriction().equals(RestrictionType.SequentialRestriction.precedes.getUri())) {
			validLocation = SequentialRestriction.checkPrecedes(sbjCoord, objCoord);
		} 
		else if (constraint.getRestriction().equals(RestrictionType.SequentialRestriction.strictlyPrecedes.getUri())) {
			validLocation = SequentialRestriction.checkStrictlyPrecedes(sbjCoord, objCoord);
		} 
		else if (constraint.getRestriction().equals(RestrictionType.SequentialRestriction.meets.getUri())) {
			validLocation = SequentialRestriction.checkMeets(sbjCoord, objCoord);
		} 
		else if (constraint.getRestriction().equals(RestrictionType.SequentialRestriction.starts.getUri())) {
			validLocation = SequentialRestriction.checkStarts(sbjCoord, objCoord);
		} 
		else if (constraint.getRestriction().equals(RestrictionType.SequentialRestriction.finishes.getUri())) {
			validLocation = SequentialRestriction.checkFinishes(sbjCoord, objCoord);
		}
		if (!validLocation) {
			String calculatedLocations = String.format(", Calculated Subject Location: [%s, %s], Calculated Object Location:[%s, %s]", sbjCoord.getLeft(), sbjCoord.getRight(), objCoord.getLeft(), objCoord.getRight());
			validationMessages = createLocationMessage(validationMessages, constraint, calculatedLocations, DataModel.Constraint.subject, subject, SBOLUtil.getURIs(subjectLocations));			
		}
		return validationMessages;
	}
	
	public List<ValidationMessage> assertForAnySubjectObjectLocationPair(List<ValidationMessage> validationMessages,Constraint constraint,List<Location> subjectLocations, List<Location> objectLocations, Feature subject) throws SBOLGraphException
	{
		boolean found=false;
		for (Location sbjLoc: subjectLocations){
			Pair<Integer, Integer> sbjLocCoord=Location.getStartEnd(sbjLoc);
			for (Location objLoc: objectLocations)
			{
				Pair<Integer, Integer> objLocCoord=Location.getStartEnd(objLoc);
				boolean overlaps=SequentialRestriction.checkOverlaps(sbjLocCoord,objLocCoord);
				if (overlaps)
				{
					found=true;
					break;
				}
			}
			if (found)
			{
				break;
			}
		}
		//The overlaps relation holds for the two sets if it holds for any pair of locations between the two sets.
		if (!found)
		{
			validationMessages = createLocationMessage(validationMessages, constraint, "", DataModel.Constraint.subject, subject, SBOLUtil.getURIs(subjectLocations));
		}
		return validationMessages;
	}
	
	public List<ValidationMessage> assertEquality(List<ValidationMessage> validationMessages,Constraint constraint,List<Location> subjectLocations, List<Location> objectLocations, Feature subject) throws SBOLGraphException
	{
		if (subjectLocations.size()==objectLocations.size())
		{
			for (Location sbjLoc: subjectLocations){
				Pair<Integer, Integer> sbjLocCoord=Location.getStartEnd(sbjLoc);
				boolean found=false;
				for (Location objLoc: objectLocations){
					Pair<Integer, Integer> objLocCoord=Location.getStartEnd(objLoc);
					
					if (constraint.getRestriction().equals(RestrictionType.SequentialRestriction.equals.getUri())){
						found=SequentialRestriction.checkEquals(sbjLocCoord,objLocCoord);
					}
					if (found){
						break;
					}
				}
				//The contains and strictlyContains relations hold for the two sets if the relation holds for every member of the object set with respect to at least one member of the subject set.
				if (found){
					break;
				}
				else{
					String calculatedLocations = String.format(", Subject location: [%s, %s]", sbjLocCoord.getLeft(), sbjLocCoord.getRight());
					validationMessages=createLocationMessage(validationMessages, constraint, calculatedLocations, DataModel.Constraint.subject, subject, sbjLoc);
				}
			}
		}
		else
		{
			String customMessage= String.format(", Number of subject location: %s, Number of object location: %s", subjectLocations.size(), objectLocations.size());
			validationMessages =createLocationMessage(validationMessages, constraint, customMessage, DataModel.Constraint.subject, subject, SBOLUtil.getURIs(subjectLocations));
		}
		return validationMessages;
	}
	
	private List<ValidationMessage> createLocationMessage(List<ValidationMessage> validationMessages, Constraint constraint, String customMessage, URI constProperty, Feature feature,Location featureLoc) throws SBOLGraphException
	{
		String message = String.format("%s %s Restriction:%s%s", "{CONSTRAINT_RESTRICTION_SEQUENCES_COMPATIBLE}", ValidationMessage.INFORMATION_SEPARATOR, constraint.getRestriction(),customMessage);
		ValidationMessage validationMessage = new ValidationMessage(message, constProperty, feature, featureLoc);
		validationMessage.childPath(DataModel.FeatureWithLocation.location);
		validationMessages = IdentifiedValidator.addToValidations(validationMessages, validationMessage);
		return validationMessages;
	}
	
	private List<ValidationMessage> createLocationMessage(List<ValidationMessage> validationMessages, Constraint constraint, String customMessage, URI constProperty, Feature feature,List<URI> locations) throws SBOLGraphException
	{
		String message = String.format("%s %s Restriction:%s%s", "{CONSTRAINT_RESTRICTION_SEQUENCES_COMPATIBLE}", ValidationMessage.INFORMATION_SEPARATOR, constraint.getRestriction(),customMessage);
		ValidationMessage validationMessage = new ValidationMessage(message, constProperty, feature, locations);
		validationMessage.childPath(DataModel.FeatureWithLocation.location);
		validationMessages = IdentifiedValidator.addToValidations(validationMessages, validationMessage);
		return validationMessages;
	}
	
	public List<ValidationMessage> assertForEveryObjectLocation(List<ValidationMessage> validationMessages,Constraint constraint,List<Location> subjectLocations, List<Location> objectLocations, Feature object) throws SBOLGraphException
	{
			for (Location objLoc: objectLocations){
				boolean found=false;
				Pair<Integer, Integer> objLocCoord=Location.getStartEnd(objLoc);
				
				for (Location sbjLoc: subjectLocations){
					Pair<Integer, Integer> sbjLocCoord=Location.getStartEnd(sbjLoc);
				
					if (constraint.getRestriction().equals(RestrictionType.SequentialRestriction.contains.getUri())){
						found=SequentialRestriction.checkContains(sbjLocCoord,objLocCoord);
					}
					else if (constraint.getRestriction().equals(RestrictionType.SequentialRestriction.strictlyContains.getUri())){
						found=SequentialRestriction.checkStrictlyContains(sbjLocCoord,objLocCoord);
					}
					else if (constraint.getRestriction().equals(RestrictionType.SequentialRestriction.equals.getUri())){
						found=SequentialRestriction.checkEquals(sbjLocCoord,objLocCoord);
					}
					
					if (found)
					{
						break;
					}
				}
			
				//The contains and strictlyContains relations hold for the two sets if the relation holds for every member of the object set with respect to at least one member of the subject set.
				if (found){
					break;
				}
				else{
					String customMessage = String.format(", Object not found with location: [%s, %s]", objLocCoord.getLeft(), objLocCoord.getRight());
					validationMessages=createLocationMessage(validationMessages, constraint, customMessage, DataModel.Constraint.object, object, objLoc);					
				}
		}
		return validationMessages;
	}
	
	private boolean isApplySequentialConstraintRestriction(Constraint constraint) throws SBOLGraphException
	{
		boolean result=false;
		Feature objectFeature=constraint.getObject();
		Feature subjectFeature=constraint.getSubject();
		
		if (objectFeature instanceof FeatureWithLocation && subjectFeature instanceof FeatureWithLocation)
		{
			FeatureWithLocation object = (FeatureWithLocation) objectFeature;
			FeatureWithLocation subject = (FeatureWithLocation) subjectFeature;
			Map<URI, Integer> counterMap=  new HashMap<URI,Integer> ();
			List<Location> objectLocations= object.getLocations();
			List<Location> subjectLocations= subject.getLocations();
			if (!CollectionUtils.isEmpty(subjectLocations) && !CollectionUtils.isEmpty(objectLocations)){
				counterMap=countSequences(counterMap, objectLocations);
				counterMap=countSequences(counterMap, subjectLocations);
				//If there is only one sequence used in locations
				if (counterMap.size()==1){
					int counter=counterMap.entrySet().iterator().next().getValue();
					//If this sequence was used for all locations
					if (counter== (objectLocations.size() + subjectLocations.size())){
						result=true;
					}
				}	
			}
		}
		return result;
	}
	
	
	private Map<URI, Integer> countSequences(Map<URI, Integer> counterMap, List<Location> locations) throws SBOLGraphException {
		if (locations!=null)
		{
			for (Location location : locations) {
				Sequence sequence = location.getSequence();
				if (sequence != null) {
					URI sequenceURI = sequence.getUri();
					Integer value = counterMap.get(sequenceURI);
					int counter = 0;
					if (value != null) {
						counter = ((int) value);
					}
					counter++;
					counterMap.put(sequenceURI, counter);
				}
			}
		}
		return counterMap;
	}

	
	@NotNull(message = "{CONSTRAINT_RESTRICTION_NOT_NULL}")
	public URI getRestriction() throws SBOLGraphException {
		return IdentifiedValidator.getValidator().getPropertyAsURI(this.resource, DataModel.Constraint.restriction);	
	}

	public void setRestriction(@NotNull(message = "{CONSTRAINT_RESTRICTION_NOT_NULL}") ConstraintRestriction restriction) throws SBOLGraphException{
		setRestriction(restriction.getUri());
	}
	
	public void setRestriction(@NotNull(message = "{CONSTRAINT_RESTRICTION_NOT_NULL}") URI restriction) throws SBOLGraphException{
		PropertyValidator.getValidator().validate(this, "setRestriction", new Object[] {restriction}, URI.class);
		RDFUtil.setProperty(resource, DataModel.Constraint.restriction, restriction);
	}
	
	@NotNull(message = "{CONSTRAINT_SUBJECT_NOT_NULL}")	
	public Feature getSubject() throws SBOLGraphException{
		//return IdentifiedValidator.getValidator().getPropertyAsURI(this.resource, DataModel.Constraint.subject);	
		return contsructIdentified(DataModel.Constraint.subject, Feature.getSubClassTypes());		
	}

	public void setSubject(@NotNull(message = "{CONSTRAINT_SUBJECT_NOT_NULL}") Feature subject) throws SBOLGraphException{
		PropertyValidator.getValidator().validate(this, "setSubject", new Object[] {subject}, Feature.class);
		RDFUtil.setProperty(resource, DataModel.Constraint.subject, SBOLUtil.toURI(subject));
	}

	@NotNull(message = "{CONSTRAINT_OBJECT_NOT_NULL}")
	public Feature getObject() throws SBOLGraphException {
		//return IdentifiedValidator.getValidator().getPropertyAsURI(this.resource, DataModel.Constraint.object);	
		return contsructIdentified(DataModel.Constraint.object, Feature.getSubClassTypes());		
	}

	public void setObject(@NotNull(message = "{CONSTRAINT_OBJECT_NOT_NULL}") Feature object) throws SBOLGraphException {
		PropertyValidator.getValidator().validate(this, "setObject", new Object[] {object}, Feature.class);
		RDFUtil.setProperty(resource, DataModel.Constraint.object, SBOLUtil.toURI(object));
	}

	@Override
	public URI getResourceType() {
		return DataModel.Constraint.uri;
	}
}