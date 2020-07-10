package org.sbolstandard.entity;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.sbolstandard.util.RDFUtil;
import org.sbolstandard.util.SBOLGraphException;
import org.sbolstandard.vocabulary.DataModel;

public class ExternallyDefined extends Feature{
	private List<URI> types=new ArrayList<URI>();
	private URI definition=null;

	protected  ExternallyDefined(Model model,URI uri) throws SBOLGraphException
	{
		super(model, uri);
	}
	
	protected  ExternallyDefined(Resource resource)
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
	
	
	public URI getDefinition() {
		if (this.definition==null)
		{
			this.definition=RDFUtil.getPropertyAsURI(this.resource, DataModel.ExternalyDefined.definition);
		}
		return this.definition;
	}

	public void setDefinition(URI definition) {
		this.definition = definition;
		RDFUtil.setProperty(this.resource, DataModel.ExternalyDefined.definition, this.definition);	
	}
	
	
	@Override
	public URI getResourceType() {
		return DataModel.ExternalyDefined.uri;
	}
	
	
}