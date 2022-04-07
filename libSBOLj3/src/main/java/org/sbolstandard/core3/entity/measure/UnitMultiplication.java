package org.sbolstandard.core3.entity.measure;

import java.net.URI;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.sbolstandard.core3.util.RDFUtil;
import org.sbolstandard.core3.util.SBOLGraphException;
import org.sbolstandard.core3.validation.IdentityValidator;
import org.sbolstandard.core3.vocabulary.MeasureDataModel;

import jakarta.validation.constraints.NotNull;

public class UnitMultiplication extends CompoundUnit{
	/*private URI term1;
	private URI term2;*/
	protected  UnitMultiplication(Model model,URI uri) throws SBOLGraphException
	{
		super(model, uri);
	}
	
	protected  UnitMultiplication(Resource resource) throws SBOLGraphException
	{
		super(resource);
	}
	
	@NotNull(message = "UnitMultiplication.term1 cannot be null")	
	public URI getTerm1() throws SBOLGraphException {
		return IdentityValidator.getValidator().getPropertyAsURI(this.resource, MeasureDataModel.UnitMultiplication.term1);	
	}

	public void setTerm1(URI term1) {
		RDFUtil.setProperty(resource, MeasureDataModel.UnitMultiplication.term1, term1);
	}
	
	@NotNull(message = "UnitMultiplication.term2 cannot be null")	
	public URI getTerm2() throws SBOLGraphException {
		return IdentityValidator.getValidator().getPropertyAsURI(this.resource, MeasureDataModel.UnitMultiplication.term2);	
	}

	public void setTerm2(URI term2) {
		RDFUtil.setProperty(resource, MeasureDataModel.UnitMultiplication.term2, term2);
	}
	
	@Override
	public URI getResourceType() {
		return MeasureDataModel.UnitMultiplication.uri;
	}
}