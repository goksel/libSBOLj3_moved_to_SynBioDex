package org.sbolstandard.core3.entity;

import java.net.URI;
import java.util.List;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.sbolstandard.core3.util.RDFUtil;
import org.sbolstandard.core3.util.SBOLGraphException;
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

	public List<URI> getInputs() {
		return RDFUtil.getPropertiesAsURIs(this.resource, DataModel.Interface.input);
	}
	
	public void setInputs(List<URI> inputs) {
		RDFUtil.setProperty(resource, DataModel.Interface.input, inputs);
	}
	

	public List<URI> getOutputs() {
		return RDFUtil.getPropertiesAsURIs(this.resource, DataModel.Interface.output);
	}
	
	public void setOutputs(List<URI> outputs) {
		RDFUtil.setProperty(resource, DataModel.Interface.output, outputs);
	}
	
	public List<URI> getNonDirectionals() {
		return RDFUtil.getPropertiesAsURIs(this.resource, DataModel.Interface.nondirectional);
	}
	
	public void setNonDirectionals(List<URI> nonDirectionals) {
		RDFUtil.setProperty(resource, DataModel.Interface.nondirectional, nonDirectionals);
	}
	
	@Override
	public URI getResourceType() {
		return DataModel.Interface.uri;
	}
}