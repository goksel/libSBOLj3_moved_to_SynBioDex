package org.sbolstandard.core3.entity;

import java.net.URI;
import java.util.OptionalLong;

import org.apache.jena.rdf.model.Resource;
import org.sbolstandard.core3.util.RDFUtil;
import org.sbolstandard.core3.util.SBOLGraphException;
import org.sbolstandard.core3.validation.IdentityValidator;
import org.sbolstandard.core3.vocabulary.DataModel;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

public class Attachment extends TopLevel{
	private URI source=null;
	private URI format=null;
//	private long size=Long.MIN_VALUE;
	private OptionalLong size=OptionalLong.empty();
	private String hash;
	private String hashAlgorithm;
	

	protected  Attachment(org.apache.jena.rdf.model.Model model,URI uri) throws SBOLGraphException
	{
		super(model, uri);
	}
	
	protected  Attachment(Resource resource) throws SBOLGraphException
	{
		super(resource);
	}
	
	@NotNull(message = "Attachment.source cannot be null")
	public URI getSource() throws SBOLGraphException {
		if (source==null)
		{
			source=IdentityValidator.getValidator().getPropertyAsURI(this.resource, DataModel.Model.source);	
		}
		return source;
	}

	public void setSource(URI source) {
		this.source = source;
		RDFUtil.setProperty(resource, DataModel.Model.source, source);
	}

	
	public URI getFormat() throws SBOLGraphException {
		if (format==null)
		{
			format=IdentityValidator.getValidator().getPropertyAsURI(this.resource, DataModel.Attachment.format);	
		}
		return format;
	}

	public void setFormat(URI format) {
		this.format = format;
		RDFUtil.setProperty(resource, DataModel.Attachment.format, format);
	}
	
	@PositiveOrZero(message="Attachment.size can have positive or zero values")
	public OptionalLong getSize() throws SBOLGraphException{
		if (size.isEmpty())
		{
			String value=IdentityValidator.getValidator().getPropertyAsString(this.resource, DataModel.Attachment.size);
			if (value!=null)
			{
				size=OptionalLong.of(Long.parseLong(value));
			}
		}
		return size;
	}

	public void setSize(OptionalLong sizeValue) {
		this.size = sizeValue;
		String stringValue=null;
		if (sizeValue.isPresent())
		{
			stringValue= String.valueOf(size.getAsLong());
		}
		RDFUtil.setProperty(resource, DataModel.Attachment.size, stringValue);
	}

	public String getHash() throws SBOLGraphException {
		if (hash==null)
		{
			hash=IdentityValidator.getValidator().getPropertyAsString(this.resource, DataModel.Attachment.hash);	
		}
		return hash;
	}

	public void setHash(String hash) {
		this.hash = hash;
		RDFUtil.setProperty(resource, DataModel.Attachment.hash, String.valueOf(hash));
	}

	public String getHashAlgorithm() throws SBOLGraphException {
		if (hashAlgorithm==null)
		{
			hashAlgorithm=IdentityValidator.getValidator().getPropertyAsString(this.resource, DataModel.Attachment.hashAlgorithm);	
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