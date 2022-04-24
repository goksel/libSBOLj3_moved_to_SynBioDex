package org.sbolstandard.core3.entity;

import java.net.URI;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.sbolstandard.core3.util.RDFUtil;
import org.sbolstandard.core3.util.SBOLGraphException;
import org.sbolstandard.core3.validation.IdentifiedValidator;
import org.sbolstandard.core3.vocabulary.DataModel;
import org.sbolstandard.core3.vocabulary.Encoding;

public class Sequence extends TopLevel {
	/*private String elements;
	private Encoding encoding;*/

	protected  Sequence(Model model,URI uri) throws SBOLGraphException
	{
		super(model, uri);
	}
	
	protected  Sequence(Resource resource) throws SBOLGraphException
	{
		super(resource);
	}

	public String getElements() throws SBOLGraphException{
		return IdentifiedValidator.getValidator().getPropertyAsString(this.resource, DataModel.Sequence.elements);
	}
	
	public void setElements(String elements) {
		RDFUtil.setProperty(this.resource, DataModel.Sequence.elements, elements);
	}
	
	public Encoding getEncoding() throws SBOLGraphException {
		Encoding encoding=null;
		URI encodingValue=IdentifiedValidator.getValidator().getPropertyAsURI(this.resource, DataModel.Sequence.encoding);
		if (encodingValue!=null){
			encoding=Encoding.get(encodingValue); 
		}
		return encoding;
	}
	
	public void setEncoding(Encoding encoding) {
		RDFUtil.setProperty(this.resource, DataModel.Sequence.encoding, encoding.getUri());
	}

	public URI getResourceType()
	{
		return DataModel.Sequence.uri;
	}
	
}