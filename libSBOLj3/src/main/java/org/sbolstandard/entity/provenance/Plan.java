package org.sbolstandard.entity.provenance;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.sbolstandard.entity.Identified;
import org.sbolstandard.entity.TopLevel;
import org.sbolstandard.util.RDFUtil;
import org.sbolstandard.util.SBOLGraphException;
import org.sbolstandard.vocabulary.DataModel;
import org.sbolstandard.vocabulary.ProvenanceDataModel;

public class Plan extends TopLevel{
	
	protected  Plan(Model model,URI uri) throws SBOLGraphException
	{
		super(model, uri);
	}
	
	protected  Plan(Resource resource)
	{
		super(resource);
	}
	
	
	@Override
	public URI getResourceType() {
		return ProvenanceDataModel.Plan.uri;
	}
	
}