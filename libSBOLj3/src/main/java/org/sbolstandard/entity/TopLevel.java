package org.sbolstandard.entity;

import java.net.URI;
import java.util.List;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.sbolstandard.util.RDFUtil;
import org.sbolstandard.util.SBOLGraphException;
import org.sbolstandard.vocabulary.DataModel;

public abstract class TopLevel extends Identified {

	private List<URI> attachments=null;
	
	
	protected TopLevel(Model model, URI uri) throws SBOLGraphException
	{
		super(model, uri);
	}
	protected TopLevel() 
	{
		
	}
	
	protected TopLevel (Resource resource)
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
	
	@Override
	public URI getResourceType() {
		return DataModel.TopLevel.uri;
	}
}
