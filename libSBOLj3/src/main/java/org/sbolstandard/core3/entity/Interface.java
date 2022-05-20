package org.sbolstandard.core3.entity;

import java.net.URI;
import java.util.List;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.sbolstandard.core3.util.RDFUtil;
import org.sbolstandard.core3.util.SBOLGraphException;
import org.sbolstandard.core3.util.SBOLUtil;
import org.sbolstandard.core3.validation.IdentifiedValidator;
import org.sbolstandard.core3.validation.ValidationMessage;
import org.sbolstandard.core3.vocabulary.DataModel;

public class Interface extends Identified{
	/*private List<URI> inputs=null;
	private List<URI> outputs=null;
	private List<URI> nonDirectionals=null;*/

	protected  Interface(Model model,URI uri) throws SBOLGraphException
	{
		super(model, uri);
	}
	
	protected  Interface(Resource resource) throws SBOLGraphException
	{
		super(resource);
	}

	public List<Feature> getInputs() throws SBOLGraphException {
		//return RDFUtil.getPropertiesAsURIs(this.resource, DataModel.Interface.input);
		return addToList(DataModel.Interface.input, Feature.getSubClassTypes());
	}
	
	public void setInputs(List<Feature> inputs) {
		RDFUtil.setProperty(resource, DataModel.Interface.input, SBOLUtil.getURIs(inputs));
	}
	
	public List<Feature> getOutputs() throws SBOLGraphException {
		//return RDFUtil.getPropertiesAsURIs(this.resource, DataModel.Interface.output);
		return addToList(DataModel.Interface.output, Feature.getSubClassTypes());
	}
	
	public void setOutputs(List<Feature> outputs) {
		RDFUtil.setProperty(resource, DataModel.Interface.output, SBOLUtil.getURIs(outputs));
	}
	
	public List<Feature> getNonDirectionals() throws SBOLGraphException {
		//return RDFUtil.getPropertiesAsURIs(this.resource, DataModel.Interface.nondirectional);
		return addToList(DataModel.Interface.nondirectional, Feature.getSubClassTypes());
	}
	
	public void setNonDirectionals(List<Feature> nonDirectionals) {
		RDFUtil.setProperty(resource, DataModel.Interface.nondirectional, SBOLUtil.getURIs(nonDirectionals));
	}
	
	@Override
	public URI getResourceType() {
		return DataModel.Interface.uri;
	}
	
	@Override
	public List<ValidationMessage> getValidationMessages() throws SBOLGraphException
	{
		List<ValidationMessage> validationMessages=super.getValidationMessages();
		validationMessages= IdentifiedValidator.assertExists(this, DataModel.Interface.input, this.resource, this.getInputs(), validationMessages);
		validationMessages= IdentifiedValidator.assertExists(this, DataModel.Interface.output, this.resource, this.getOutputs(), validationMessages);
		validationMessages= IdentifiedValidator.assertExists(this, DataModel.Interface.nondirectional, this.resource, this.getNonDirectionals(), validationMessages);
		return validationMessages;
	}
}