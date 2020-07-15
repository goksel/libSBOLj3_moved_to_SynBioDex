package org.sbolstandard.entity;

import java.net.URI;

import org.apache.jena.rdf.model.Resource;
import org.sbolstandard.util.RDFUtil;
import org.sbolstandard.util.SBOLGraphException;
import org.sbolstandard.vocabulary.DataModel;

public class Attachment extends TopLevel{
	private URI source=null;
	private URI format=null;
	private long size=Long.MIN_VALUE;
	private String hash;
	private String hashAlgorithm;
	

	protected  Attachment(org.apache.jena.rdf.model.Model model,URI uri) throws SBOLGraphException
	{
		super(model, uri);
	}
	
	protected  Attachment(Resource resource)
	{
		super(resource);
	}
	
	public URI getSource() throws SBOLGraphException {
		if (source==null)
		{
			source=RDFUtil.getPropertyAsURI(this.resource, DataModel.Model.source);	
		}
		return source;
	}

	public void setSource(URI source) {
		this.source = source;
		RDFUtil.setProperty(resource, DataModel.Model.source, source);
	}

	
	public URI getFormat() {
		if (format==null)
		{
			format=RDFUtil.getPropertyAsURI(this.resource, DataModel.Attachment.format);	
		}
		return format;
	}

	public void setFormat(URI format) {
		this.format = format;
		RDFUtil.setProperty(resource, DataModel.Attachment.format, format);
	}
	
	public long getSize() {
		if (size==Long.MIN_VALUE)
		{
			String value=RDFUtil.getPropertyAsString(this.resource, DataModel.Attachment.size);
			if (value!=null)
			{
				size=Long.parseLong(value);
			}
		}
		return size;
	}

	public void setSize(long size) {
		this.size = size;
		RDFUtil.setProperty(resource, DataModel.Attachment.size, String.valueOf(size));
	}

	public String getHash() {
		if (hash==null)
		{
			hash=RDFUtil.getPropertyAsString(this.resource, DataModel.Attachment.hash);	
		}
		return hash;
		
	}

	public void setHash(String hash) {
		this.hash = hash;
		RDFUtil.setProperty(resource, DataModel.Attachment.hash, String.valueOf(hash));

	}

	public String getHashAlgorithm() {
		if (hashAlgorithm==null)
		{
			hashAlgorithm=RDFUtil.getPropertyAsString(this.resource, DataModel.Attachment.hashAlgorithm);	
		}
		return hashAlgorithm;
	}

	public void setHashAlgorithm(String hashAlgorithm) {
		this.hashAlgorithm = hashAlgorithm;
		RDFUtil.setProperty(resource, DataModel.Attachment.hashAlgorithm, String.valueOf(hashAlgorithm));
	}

	@Override
	public URI getResourceType() {
		return DataModel.Attachment.uri;
	}
	
	
}