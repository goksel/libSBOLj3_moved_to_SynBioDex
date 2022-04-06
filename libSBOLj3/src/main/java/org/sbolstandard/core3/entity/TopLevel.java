package org.sbolstandard.core3.entity;

import java.net.URI;
import java.util.List;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.sbolstandard.core3.util.RDFUtil;
import org.sbolstandard.core3.util.SBOLGraphException;
import org.sbolstandard.core3.util.SBOLUtil;
import org.sbolstandard.core3.validation.IdentityValidator;
import org.sbolstandard.core3.vocabulary.DataModel;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public abstract class TopLevel extends Identified {

	/*private List<URI> attachments=null;
	private URI namespace=null;*/
	
	
	protected TopLevel(Model model, URI uri) throws SBOLGraphException
	{
		super(model, uri);
	}
	protected TopLevel() 
	{
		
	}
	
	protected TopLevel (Resource resource) throws SBOLGraphException
	{
		super(resource);
	}
	
	public List<URI> getAttachments() {
		return RDFUtil.getPropertiesAsURIs(this.resource, DataModel.TopLevel.attachment);
	}
	
	public void setAttachments(List<URI> attachments) {
		RDFUtil.setProperty(resource, DataModel.TopLevel.attachment, attachments);
	}
	
	@NotNull(message = "TopLevel.namespace cannot be null")
	public URI getNamespace() throws SBOLGraphException{
		return IdentityValidator.getValidator().getPropertyAsURI(this.resource, DataModel.TopLevel.namespace);	
	}

	public void setNamespace(URI namespace) throws SBOLGraphException {
		if (namespace==null)
		{
			throw new SBOLGraphException("Namespace cannot be null. URI:" + this.resource.getURI());
		}
		String uriString=namespace.toString();
		if (SBOLUtil.isURL(uriString))
		{
			if (uriString.endsWith("/") || uriString.endsWith("#"))
			{
				uriString=uriString.substring(0, uriString.length()-1);
			}
		}
		RDFUtil.setProperty(resource, DataModel.TopLevel.namespace, URI.create(uriString));
	}
	
	
	 
	@Override
	public URI getResourceType() {
		return DataModel.TopLevel.uri;
	}
}
