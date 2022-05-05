package org.sbolstandard.core3.entity;

import java.net.URI;
import java.util.List;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.sbolstandard.core3.util.RDFUtil;
import org.sbolstandard.core3.util.SBOLGraphException;
import org.sbolstandard.core3.validation.IdentifiedValidator;
import org.sbolstandard.core3.validation.PropertyValidator;
import org.sbolstandard.core3.validation.ValidationMessage;
import org.sbolstandard.core3.vocabulary.DataModel;

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
	
	public URI getParticipant() throws SBOLGraphException {
		return IdentifiedValidator.getValidator().getPropertyAsURI(this.resource, DataModel.Participation.participant);	
	}

	public void setParticipant(URI participant) {
		RDFUtil.setProperty(resource, DataModel.Participation.participant, participant);
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
	
}