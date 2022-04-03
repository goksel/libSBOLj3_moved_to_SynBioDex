package org.sbolstandard.core3.entity;

import java.net.URI;
import java.util.List;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.sbolstandard.core3.api.SBOLAPI;
import org.sbolstandard.core3.util.RDFUtil;
import org.sbolstandard.core3.util.SBOLGraphException;
import org.sbolstandard.core3.vocabulary.DataModel;

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
	
	public List<URI> getTypes() {
		return RDFUtil.getPropertiesAsURIs(this.resource, DataModel.type);
	}
	
	public void setTypes(List<URI> types) {
		RDFUtil.setProperty(resource, DataModel.type, types);
	}
	
	
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
	
	private Participation createParticipation(String displayId, List<URI> roles, URI feature) throws SBOLGraphException
	{
		return createParticipation(SBOLAPI.append(this.getUri(), displayId), roles, feature);	
	}
	
	public Participation createParticipation(List<URI> roles, URI feature) throws SBOLGraphException
	{
		String displayId=SBOLAPI.createLocalName(DataModel.Participation.uri, getParticipations());	
		return createParticipation(displayId, roles, feature);	
	}
	
	@Override
	public URI getResourceType() {
		return DataModel.Interaction.uri;
	}
	
}