package org.sbolstandard.entity;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.sbolstandard.util.RDFUtil;
import org.sbolstandard.util.SBOLGraphException;
import org.sbolstandard.util.SBOLUtil;
import org.sbolstandard.vocabulary.DataModel;

public abstract class TopLevel extends Identified {

	private List<URI> attachments=null;
	private URI namespace=null;
	
	
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
		if (attachments==null)
		{
			attachments=RDFUtil.getPropertiesAsURIs(this.resource, DataModel.TopLevel.attachment);
		}
		return attachments;
	}
	
	public void setAttachments(List<URI> attachments) {
		this.attachments = attachments;
		RDFUtil.setProperty(resource, DataModel.TopLevel.attachment, attachments);
	}
	
	public URI getNamespace() {
		if (namespace==null)
		{
			namespace=RDFUtil.getPropertyAsURI(this.resource, DataModel.TopLevel.namespace);	
		}
		return namespace;
	}

	public void setNamespace(URI namespace) {
		
		String uriString=namespace.toString();
		if (SBOLUtil.isURL(uriString))
		{
			if (uriString.endsWith("/") || uriString.endsWith("#"))
			{
				uriString=uriString.substring(0, uriString.length()-1);
			}
		}
		this.namespace = URI.create(uriString);
		RDFUtil.setProperty(resource, DataModel.TopLevel.namespace, this.namespace);
	}
	
	
	 
	@Override
	public URI getResourceType() {
		return DataModel.TopLevel.uri;
	}
}
