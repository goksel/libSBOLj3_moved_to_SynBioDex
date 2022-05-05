package org.sbolstandard.core3.entity;

import java.net.URI;
import java.util.List;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.sbolstandard.core3.api.SBOLAPI;
import org.sbolstandard.core3.util.RDFUtil;
import org.sbolstandard.core3.util.SBOLGraphException;
import org.sbolstandard.core3.validation.PropertyValidator;
import org.sbolstandard.core3.vocabulary.DataModel;

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
		return addToList(DataModel.Interaction.participation, Participation.class);
	}

	
	public Participation createParticipation(URI uri, List<URI> roles, URI feature) throws SBOLGraphException
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
	
	private Participation createParticipation(String displayId, List<URI> roles, URI feature) throws SBOLGraphException
	{
		return createParticipation(SBOLAPI.append(this.getUri(), displayId), roles, feature);	
	}
	
	public Participation createParticipation(List<URI> roles, URI feature) throws SBOLGraphException
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
	
}