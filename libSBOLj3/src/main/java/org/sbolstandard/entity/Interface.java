package org.sbolstandard.entity;

import java.net.URI;
import java.util.List;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.sbolstandard.util.RDFUtil;
import org.sbolstandard.util.SBOLGraphException;
import org.sbolstandard.vocabulary.DataModel;

public class Interface extends Identified{
	private List<URI> inputs=null;
	private List<URI> outputs=null;
	private List<URI> nonDirectionals=null;

	protected  Interface(Model model,URI uri) throws SBOLGraphException
	{
		super(model, uri);
	}
	
	protected  Interface(Resource resource)
	{
		super(resource);
	}
	

	public List<URI> getInputs() {
		if (inputs==null)
		{
			inputs=RDFUtil.getPropertiesAsURIs(this.resource, DataModel.Interface.input);
		}
		return inputs;
	}
	
	public void setInputs(List<URI> inputs) {
		this.inputs = inputs;
		RDFUtil.setProperty(resource, DataModel.Interface.input, inputs);
	}
	

	public List<URI> getOutputs() {
		if (outputs==null)
		{
			outputs=RDFUtil.getPropertiesAsURIs(this.resource, DataModel.Interface.output);
		}
		return outputs;
	}
	
	public void setOutputs(List<URI> outputs) {
		this.outputs = outputs;
		RDFUtil.setProperty(resource, DataModel.Interface.output, outputs);
	}
	
	public List<URI> getNonDirectionals() {
		if (this.nonDirectionals==null)
		{
			nonDirectionals=RDFUtil.getPropertiesAsURIs(this.resource, DataModel.Interface.nondirectional);
		}
		return nonDirectionals;
	}
	
	public void setNonDirectionals(List<URI> nonDirectionals) {
		this.nonDirectionals = nonDirectionals;
		RDFUtil.setProperty(resource, DataModel.Interface.nondirectional, nonDirectionals);
	}
	
	@Override
	public URI getResourceType() {
		return DataModel.Interface.uri;
	}

}