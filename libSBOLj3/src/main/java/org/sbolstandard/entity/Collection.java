package org.sbolstandard.entity;

import java.net.URI;
import java.util.List;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.sbolstandard.util.RDFUtil;
import org.sbolstandard.util.SBOLGraphException;
import org.sbolstandard.vocabulary.DataModel;

public class Collection extends TopLevel{
	private List<URI> members=null;
	
	protected  Collection(Model model,URI uri) throws SBOLGraphException
	{
		super(model, uri);
	}
	
	protected  Collection(Resource resource)
	{
		super(resource);
	}
	
	public List<URI> getTopLevels() throws SBOLGraphException {
		if (members==null)
		{
			members=RDFUtil.getPropertiesAsURIs(this.resource, DataModel.Collection.member);	
		}
		return members;
	}

	public void setTopLevels(List<URI> members) {
		this.members = members;
		RDFUtil.setProperty(resource, DataModel.Collection.member, members);
	}	

	@Override
	public URI getResourceType() {
		return DataModel.Collection.uri;
	}
	
	
}