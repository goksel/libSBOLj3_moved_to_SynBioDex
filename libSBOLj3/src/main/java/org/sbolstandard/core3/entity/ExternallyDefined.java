package org.sbolstandard.core3.entity;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.sbolstandard.core3.util.RDFUtil;
import org.sbolstandard.core3.util.SBOLGraphException;
import org.sbolstandard.core3.validation.IdentityValidator;
import org.sbolstandard.core3.vocabulary.DataModel;

public class ExternallyDefined extends Feature{
	private List<URI> types=new ArrayList<URI>();
	private URI definition=null;

	protected  ExternallyDefined(Model model,URI uri) throws SBOLGraphException
	{
		super(model, uri);
	}
	
	protected  ExternallyDefined(Resource resource) throws SBOLGraphException
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
	
	
	public URI getDefinition() throws SBOLGraphException {
		if (this.definition==null)
		{
			this.definition=IdentityValidator.getValidator().getPropertyAsURI(this.resource, DataModel.ExternalyDefined.definition);
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