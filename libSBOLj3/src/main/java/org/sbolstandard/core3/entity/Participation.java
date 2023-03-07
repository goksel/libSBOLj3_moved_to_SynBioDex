package org.sbolstandard.core3.entity;

import java.net.URI;
import java.util.List;
import java.util.Set;

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
import org.sbolstandard.core3.vocabulary.MeasureDataModel;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public class Participation extends Identified{
	/*private List<URI> roles=null;
	private URI participant=null;
	private URI higherOrderParticipant=null;*/
	
	protected  Participation(Model model,URI uri) throws SBOLGraphException
	{
		super(model, uri);
	}
	
	protected  Participation(Resource resource) throws SBOLGraphException
	{
		super(resource);
	}
	
	@Override
	public List<ValidationMessage> getValidationMessages() throws SBOLGraphException
	{
		List<ValidationMessage> validationMessages=super.getValidationMessages();
		if ((this.getParticipant()==null && this.getHigherOrderParticipant()==null) || (this.getParticipant()!=null && this.getHigherOrderParticipant()!=null))
		{
			validationMessages= addToValidations(validationMessages,new ValidationMessage("{PARTICIPANT_MUST_HAVE_ONE_PARTICIPANT_OR_HIGHERORDERPARTICIPANT}", DataModel.Participation.participant));      	
		}
		validationMessages = assertValidSBORole(validationMessages);
		validationMessages= IdentifiedValidator.assertEquals(this, DataModel.Participation.participant, this.resource, getParticipant(), validationMessages);
		return validationMessages;
	}
	
	@NotEmpty(message = "{PARTICIPANT_ROLES_NOT_EMPTY}")
	public List<URI> getRoles() {
		return RDFUtil.getPropertiesAsURIs(this.resource, DataModel.role);
	}
	
	public void setRoles(@NotEmpty(message = "{PARTICIPANT_ROLES_NOT_EMPTY}") List<URI> roles) throws SBOLGraphException {
		PropertyValidator.getValidator().validate(this, "setRoles", new Object[] {roles}, List.class);
		RDFUtil.setProperty(resource, DataModel.role, roles);
	}
	
	public Feature getParticipant() throws SBOLGraphException {
		//return IdentifiedValidator.getValidator().getPropertyAsURI(this.resource, DataModel.Participation.participant);	
		return contsructIdentified(DataModel.Participation.participant, Feature.getSubClassTypes());
	}

	public void setParticipant(Feature participant) {
		RDFUtil.setProperty(resource, DataModel.Participation.participant, SBOLUtil.toURI(participant));
	}
	
	public URI getHigherOrderParticipant() throws SBOLGraphException {
		return IdentifiedValidator.getValidator().getPropertyAsURI(this.resource, DataModel.Participation.higherOrderParticipant);	
	}

	public void setHigherOrderParticipant(URI higherOrderParticipant) {
		RDFUtil.setProperty(resource, DataModel.Participation.higherOrderParticipant, higherOrderParticipant);
	}
	
	@Override
	public URI getResourceType() {
		return DataModel.Participation.uri;
	}
	
	//PARTICIPANT_ROLE_SBO_VALID
	public List<ValidationMessage> assertValidSBORole(List<ValidationMessage> validationMessages) throws SBOLGraphException {		
		if (Configuration.getInstance().isValidateRecommendedRules()) {
			List<URI> roles=this.getRoles();
			if (roles!=null){
				Set<URI> sboRoles=null;
				for (URI role:roles){
					if (Configuration.getInstance().getSboParticipantRoles().contains(role.toString())) {
						sboRoles=SBOLUtil.addToSet(sboRoles, role);
					}
				}
				String message=null;
				//count should be zero
				if (sboRoles==null || sboRoles.size()==0){
					message="{PARTICIPANT_ROLE_SBO_VALID}";
				}
				else if (sboRoles.size()>1){
						message=String.format("{PARTICIPANT_ROLE_SBO_VALID}%s Multiple valid SBO roles: %s", ValidationMessage.INFORMATION_SEPARATOR, sboRoles);
				}
				
				if (message!=null) {
					ValidationMessage valMessage = new ValidationMessage(message, DataModel.role, roles);
					validationMessages=IdentifiedValidator.addToValidations(validationMessages, valMessage);
				}
				
			}
		}
		return validationMessages;
	}
	
}