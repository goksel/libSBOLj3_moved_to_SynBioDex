package org.sbolstandard.core3.entity.measure;

import java.net.URI;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.sbolstandard.core3.entity.SBOLDocument;
import org.sbolstandard.core3.util.SBOLGraphException;
import org.sbolstandard.core3.vocabulary.MeasureDataModel;

public class SIPrefix extends Prefix{
	
	protected  SIPrefix(Model model,URI uri) throws SBOLGraphException
	{
		super(model, uri);
	}
	
	protected  SIPrefix(Resource resource) throws SBOLGraphException
	{
		super(resource);
	}
	
	
	@Override
	public URI getResourceType() {
		return MeasureDataModel.SIPrefix.uri;
	}

	public static SIPrefix create(SBOLDocument sbolDocument, URI uri, URI namespace) throws SBOLGraphException {
		SIPrefix prefix = new SIPrefix(sbolDocument.getRDFModel(), uri);
		prefix.setNamespace(namespace);
		return prefix;
	}
	
}