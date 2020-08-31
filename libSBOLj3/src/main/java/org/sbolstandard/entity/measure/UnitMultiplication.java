package org.sbolstandard.entity.measure;

import java.net.URI;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.sbolstandard.util.RDFUtil;
import org.sbolstandard.util.SBOLGraphException;
import org.sbolstandard.vocabulary.MeasureDataModel;

public class UnitMultiplication extends CompountUnit{
	
	private URI term1;
	private URI term2;
	
	
	protected  UnitMultiplication(Model model,URI uri) throws SBOLGraphException
	{
		super(model, uri);
	}
	
	protected  UnitMultiplication(Resource resource)
	{
		super(resource);
	}
	
	public URI getTerm1() {
		if (term1==null)
		{
			term1=RDFUtil.getPropertyAsURI(this.resource, MeasureDataModel.UnitMultiplication.term1);	
		}
		return term1;
	}

	public void setTerm1(URI term1) {
		this.term1 = term1;
		RDFUtil.setProperty(resource, MeasureDataModel.UnitMultiplication.term1, term1);
	}
	
	public URI getTerm2() {
		if (term2==null)
		{
			term2=RDFUtil.getPropertyAsURI(this.resource, MeasureDataModel.UnitMultiplication.term2);	
		}
		return term2;
	}

	public void setTerm2(URI term2) {
		this.term2 = term2;
		RDFUtil.setProperty(resource, MeasureDataModel.UnitMultiplication.term2, term2);
	}
	
	
	@Override
	public URI getResourceType() {
		return MeasureDataModel.UnitMultiplication.uri;
	}
	
}