package org.sbolstandard.core3.entity;

import java.net.URI;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.sbolstandard.core3.api.SBOLAPI;
import org.sbolstandard.core3.util.Configuration;
import org.sbolstandard.core3.util.RDFUtil;
import org.sbolstandard.core3.util.SBOLGraphException;
import org.sbolstandard.core3.util.SBOLUtil;
import org.sbolstandard.core3.validation.IdentifiedValidator;
import org.sbolstandard.core3.validation.PropertyValidator;
import org.sbolstandard.core3.validation.ValidationMessage;
import org.sbolstandard.core3.vocabulary.DataModel;
import org.sbolstandard.core3.vocabulary.InteractionType;
import org.sbolstandard.core3.vocabulary.ParticipationRole;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public class Interaction extends Identified{
	/*private List<URI> types=null;
	private List<Participation> participations=null;*/

	protected  Interaction(Model model,URI uri) throws SBOLGraphException
	{
		super(model, uri);
	}
	
	protected  Interaction(Resource resource) throws SBOLGraphException
	{
		super(resource);
	}
	
	@NotEmpty(message = "{INTERACTION_TYPES_NOT_EMPTY}")
	public List<URI> getTypes() {
		return RDFUtil.getPropertiesAsURIs(this.resource, DataModel.type);
	}
	
	public void setTypes(@NotEmpty(message = "{INTERACTION_TYPES_NOT_EMPTY}") List<URI> types) throws SBOLGraphException {
		PropertyValidator.getValidator().validate(this, "setTypes", new Object[] {types}, List.class);
		RDFUtil.setProperty(resource, DataModel.type, types);
	}
	
	@Valid
	public List<Participation> getParticipations() throws SBOLGraphException {
		return addToList(DataModel.Interaction.participation, Participation.class, DataModel.Participation.uri);
	}

	
	public Participation createParticipation(URI uri, List<URI> roles, Feature feature) throws SBOLGraphException
	{
		Participation participation=new Participation(this.resource.getModel(),uri);
		participation.setRoles(roles);
		participation.setParticipant(feature);
		addToList(participation, DataModel.Interaction.participation);
		return participation;
	}
	
	public Participation createHigherOrderParticipation(URI uri, List<URI> roles, URI interaction) throws SBOLGraphException
	{
		Participation participation=new Participation(this.resource.getModel(),uri);
		participation.setRoles(roles);
		participation.setHigherOrderParticipant(interaction);
		addToList(participation, DataModel.Interaction.participation);
		return participation;
	}
	
	private Participation createParticipation(String displayId, List<URI> roles, Feature feature) throws SBOLGraphException
	{
		return createParticipation(SBOLAPI.append(this.getUri(), displayId), roles, feature);	
	}
	
	public Participation createParticipation(List<URI> roles, Feature feature) throws SBOLGraphException
	{
		String displayId=SBOLAPI.createLocalName(DataModel.Participation.uri, getParticipations());	
		return createParticipation(displayId, roles, feature);	
	}

	private Participation createHigherOrderParticipation(String displayId, List<URI> roles, URI interaction) throws SBOLGraphException
	{
		return createHigherOrderParticipation(SBOLAPI.append(this.getUri(), displayId), roles, interaction);	
	}
	
	public Participation createHigherOrderParticipation(List<URI> roles, URI interaction) throws SBOLGraphException
	{
		String displayId=SBOLAPI.createLocalName(DataModel.Participation.uri, getParticipations());	
		return createHigherOrderParticipation(displayId, roles, interaction);	
	}

	
	@Override
	public URI getResourceType() {
		return DataModel.Interaction.uri;
	}
	
	@Override
	public List<Identified> getChildren() throws SBOLGraphException {
		List<Identified> identifieds=super.getChildren();
		identifieds=addToList(identifieds, this.getParticipations());
		return identifieds;
	}
	
	@Override
	public List<ValidationMessage> getValidationMessages() throws SBOLGraphException
	{
		List<ValidationMessage> validationMessages=super.getValidationMessages();
		validationMessages= IdentifiedValidator.assertExists(this, DataModel.Interaction.participation, this.resource, this.getParticipations(), validationMessages);
		
		if (Configuration.getInstance().isValidateRecommendedRules()){
			//INTERACTION_VALID_TYPE
			validationMessages= IdentifiedValidator.assertOneExists(Configuration.getInstance().getSboOccurringEntityInteractionTypes(), getTypes(),validationMessages,"{INTERACTION_VALID_TYPE}",this,DataModel.type);
			
			// INTERACTION_PARTICIPANT_TYPES_COMPATIBLE
			List<Participation> participations = this.getParticipations();
			if (!CollectionUtils.isEmpty(participations)) {
				List<URI> interactionTypes = getTypes();
				for (URI type : interactionTypes) {
					InteractionType interactionType = InteractionType.get(type);
					// If interaction type comes from Table 11
					if (interactionType != null) {
						List<ParticipationRole> expectedRoles = InteractionType.mapParticipationRoles(interactionType);
						if (participations!=null)
						{
							for (Participation participation: participations)
							{
								boolean valid=true;
								
								List<URI> roles=participation.getRoles();
								if (CollectionUtils.isEmpty(roles))
								{
									valid=false;
								}
								else
								{
									boolean found=false;
									for (ParticipationRole expectedRole: expectedRoles)
									{
										if (roles.contains(expectedRole.getUri()))
										{
											found=true;
											break;
										}
									}
									if (!found)
									{
										valid=false;
									}
								}
								if (!valid)
								{
									//ValidationMessage message = new ValidationMessage("{INTERACTION_PARTICIPANT_TYPES_COMPATIBLE}", DataModel.Interaction.participation, SBOLUtil.getURIs(participations));
									ValidationMessage message = new ValidationMessage("{INTERACTION_PARTICIPANT_TYPES_COMPATIBLE}", DataModel.Interaction.participation, participation,roles);
									message.childPath(DataModel.role);
									validationMessages = IdentifiedValidator.addToValidations(validationMessages, message);
								}
							}
							
						}
						/*Set<ParticipationRole>foundExpectedRoles=new HashSet<ParticipationRole>();
						for (ParticipationRole expectedRole : expectedRoles) {
							for (Participation participation : participations) {
								List<URI> roles=participation.getRoles();
								if (roles.contains(expectedRole.getUri())) {
									foundExpectedRoles.add(expectedRole);
								}
							}
						}
						
						for (ParticipationRole expectedRole:expectedRoles)
						{
							if (!foundExpectedRoles.contains(expectedRole))
							{
								ValidationMessage message = new ValidationMessage("{INTERACTION_PARTICIPANT_TYPES_COMPATIBLE}", DataModel.Interaction.participation, SBOLUtil.getURIs(participations));
								validationMessages = IdentifiedValidator.addToValidations(validationMessages, message);
							}
						}*/
					}
				}
			}
		}
		
		return validationMessages;
	}
	
}