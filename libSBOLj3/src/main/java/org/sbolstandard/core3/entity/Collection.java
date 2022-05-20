package org.sbolstandard.core3.entity;

import java.net.URI;
import java.util.List;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.sbolstandard.core3.util.RDFUtil;
import org.sbolstandard.core3.util.SBOLGraphException;
import org.sbolstandard.core3.validation.IdentifiedValidator;
import org.sbolstandard.core3.validation.ValidationMessage;
import org.sbolstandard.core3.vocabulary.DataModel;

public class Collection extends TopLevel{
	//private List<URI> members=null;
	
	protected  Collection(Model model,URI uri) throws SBOLGraphException
	{
		super(model, uri);
	}
	
	protected  Collection(Resource resource) throws SBOLGraphException
	{
		super(resource);
	}
	

	public List<URI> getMembers() throws SBOLGraphException {
		return RDFUtil.getPropertiesAsURIs(this.resource, DataModel.Collection.member);	
	}

	public void setMembers(List<URI> members) {
		RDFUtil.setProperty(resource, DataModel.Collection.member, members);
	}	
	
	/*
	public List<TopLevel> getMembers() throws SBOLGraphException {
		List<URI> members = RDFUtil.getPropertiesAsURIs(this.resource, DataModel.Collection.member);	
		
	}

	public void setMembers(List<URI> members) {
		RDFUtil.setProperty(resource, DataModel.Collection.member, members);
	}	
	 */
	
	@Override
	public URI getResourceType() {
		return DataModel.Collection.uri;
	}
	
}