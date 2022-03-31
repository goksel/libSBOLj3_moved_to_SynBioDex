package org.sbolstandard.core3.entity;

import java.net.URI;
import java.util.List;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.sbolstandard.core3.util.RDFUtil;
import org.sbolstandard.core3.util.SBOLGraphException;
import org.sbolstandard.core3.validation.IdentityValidator;
import org.sbolstandard.core3.vocabulary.DataModel;

public class Participation extends Identified{
	private List<URI> roles=null;
	private URI participant=null;
	private URI higherOrderParticipant=null;
	

	protected  Participation(Model model,URI uri) throws SBOLGraphException
	{
		super(model, uri);
	}
	
	protected  Participation(Resource resource) throws SBOLGraphException
	{
		super(resource);
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
	
	
	public URI getParticipant() throws SBOLGraphException {
		if (participant==null)
		{
			participant=IdentityValidator.getValidator().getPropertyAsURI(this.resource, DataModel.Participation.participant);	
		}
		return participant;
	}

	public void setParticipant(URI participant) {
		this.participant = participant;
		RDFUtil.setProperty(resource, DataModel.Participation.participant, participant);
	}
	
	public URI getHigherOrderParticipant() throws SBOLGraphException {
		if (higherOrderParticipant==null)
		{
			higherOrderParticipant=IdentityValidator.getValidator().getPropertyAsURI(this.resource, DataModel.Participation.higherOrderParticipant);	
		}
		return participant;
	}

	public void setHigherOrderParticipant(URI higherOrderParticipant) {
		this.higherOrderParticipant = higherOrderParticipant;
		RDFUtil.setProperty(resource, DataModel.Participation.higherOrderParticipant, higherOrderParticipant);
	}
	

	
	
	@Override
	public URI getResourceType() {
		return DataModel.Participation.uri;
	}
	
	
}