package org.sbolstandard.core3.entity;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

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
		ArrayList<URI> table8List = new ArrayList<URI>();
		table8List.add(RestrictionType.Identity.verifyIdentical);
		table8List.add(RestrictionType.Identity.differentFrom);
		table8List.add(RestrictionType.Identity.replaces);
		table8List.add(RestrictionType.Orientation.sameOrientationAs);
		table8List.add(RestrictionType.Orientation.oppositeOrientationAs);
		
		ArrayList<URI> table9List = new ArrayList<URI>();
		table9List.add(RestrictionType.Topology.isDisjointFrom);
		table9List.add(RestrictionType.Topology.strictlyContains);
		table9List.add(RestrictionType.Topology.contains);
		table9List.add(RestrictionType.Topology.equals);
		table9List.add(RestrictionType.Topology.meets);
		table9List.add(RestrictionType.Topology.covers);
		table9List.add(RestrictionType.Topology.overlaps);
		
		ArrayList<URI> table10List = new ArrayList<URI>();
		table10List.add(RestrictionType.Sequential.precedes);
		table10List.add(RestrictionType.Sequential.strictlyPrecedes);
		table10List.add(RestrictionType.Sequential.meets);
		table10List.add(RestrictionType.Sequential.overlaps);
		table10List.add(RestrictionType.Sequential.contains);
		table10List.add(RestrictionType.Sequential.strictlyContains);
		table10List.add(RestrictionType.Sequential.equals);
		table10List.add(RestrictionType.Sequential.finishes);
		table10List.add(RestrictionType.Sequential.starts);
		
		List<ValidationMessage> validationMessages=super.getValidationMessages();
		validationMessages=IdentifiedValidator.assertNotEqual(this, validationMessages, this.getObject(), this.getSubject(), "{CONSTRAINT_OBJECT_AND_SUBJECT_ARE_NOT_EQUAL}", DataModel.Constraint.object);
		validationMessages= IdentifiedValidator.assertEquals(this, DataModel.Constraint.subject, this.resource, this.getSubject(), validationMessages);
		validationMessages= IdentifiedValidator.assertEquals(this, DataModel.Constraint.object, this.resource, this.getObject(), validationMessages);
		
		URI restriction = this.getRestriction();
		// CONSTRAINT_RESTRICTION_VALUE_FROM_TABLE_8_9_10_37
		if (Configuration.getInstance().isValidateRecommendedRules()){
			if(!table8List.contains(restriction) && !table9List.contains(restriction) && !table10List.contains(restriction)) {	
				validationMessages = addToValidations(validationMessages,new ValidationMessage("{CONSTRAINT_RESTRICTION_VALUE_FROM_TABLE_8_9_10_37}", DataModel.type));
			}
		}
		
		// CONSTRAINT_RESTRICTION_TABLE8_MUST_MATCH_FEATURE
		if(table8List.contains(restriction)) {
			//If the restriction property of a Constraint is drawn from Table 8, 
			Feature subject = this.getSubject();
			Feature object = this.getObject();
			if(!restriction.equals(subject.getUri()) || !restriction.equals(object.getUri())) {
				validationMessages = addToValidations(validationMessages,new ValidationMessage("{CONSTRAINT_RESTRICTION_TABLE8_MUST_MATCH_FEATURE}", DataModel.type));
			}
		}
		
		return validationMessages;
	}
	
	@NotNull(message = "{CONSTRAINT_RESTRICTION_NOT_NULL}")
	public URI getRestriction() throws SBOLGraphException {
		return IdentifiedValidator.getValidator().getPropertyAsURI(this.resource, DataModel.Constraint.restriction);	
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