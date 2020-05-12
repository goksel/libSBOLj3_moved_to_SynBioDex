package org.sbolstandard;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.sbolstandard.Location.LocationBuilder;
import org.sbolstandard.Location.LocationFactory;
import org.sbolstandard.util.RDFUtil;
import org.sbolstandard.vocabulary.DataModel;

public class Interaction extends Feature{
	private List<URI> types=new ArrayList<URI>();
	private List<Participation> participations=null;

	protected  Interaction(Model model,URI uri)
	{
		super(model, uri);
	}
	
	protected  Interaction(Resource resource)
	{
		super(resource);
	}

	
	public List<URI> getTypes() {
		if (types==null)
		{
			types=RDFUtil.getPropertiesAsURIs(this.resource, DataModel.type);
		}
		return types;
	}
	
	public void setTypes(List<URI> types) {
		this.types = types;
		RDFUtil.setProperty(resource, DataModel.type, types);
	}
	
	
	public List<Participation> getParticipations() throws SBOLGraphException, SBOLException {
		this.participations=addToList(this.participations, DataModel.Interaction.participation, Participation.class);
		return this.participations;
	}


	public Participation createParticipation(URI uri, List<URI> roles, URI feature) {
		Participation participation=new Participation(this.resource.getModel(),uri);
		participation.setRoles(roles);
		participation.setParticipant(feature);
		this.participations=addToList(this.participations, participation, DataModel.Interaction.participation);
		return participation;
	}
	
	
	@Override
	public URI getResourceType() {
		return DataModel.Interaction.uri;
	}
	
	
}