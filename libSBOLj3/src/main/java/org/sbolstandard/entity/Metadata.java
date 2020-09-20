package org.sbolstandard.entity;

import java.net.URI;

import org.apache.jena.rdf.model.Resource;
import org.sbolstandard.util.RDFUtil;
import org.sbolstandard.util.SBOLGraphException;
import org.sbolstandard.vocabulary.DataModel;

public class Metadata extends Identified{
	
	
	protected  Metadata(org.apache.jena.rdf.model.Model model,URI uri) throws SBOLGraphException
	{
		super(model, uri);
	}
	
	protected  Metadata(Resource resource)
	{
		super(resource);
	}
	
	public Metadata(SBOLDocument doc, URI uri, URI dataType) throws SBOLGraphException
	{
		this(doc, uri,dataType,false);
		if (dataType==null)
		{
			throw new SBOLGraphException("Application specific types MUST have a datatype property specified. " + "Metadata URI:" + uri);
		}
		
	}
	
	public Metadata(SBOLDocument doc, URI uri, URI dataType, boolean isTopLevel) throws SBOLGraphException
	{
		super(doc.model,uri,dataType);
		if (isTopLevel)
		{
			//TODO GMGM
			//doc.addTopLevelResourceType(dataType);
			RDFUtil.addType(resource, DataModel.TopLevel.uri);
		}
		else
		{
			RDFUtil.addType(resource, DataModel.Identified.uri);
		}
	}

	@Override
	public URI getResourceType() {
		return null;
	}
	
	
}