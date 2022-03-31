package org.sbolstandard.core3.entity;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.sbolstandard.core3.api.SBOLAPI;
import org.sbolstandard.core3.util.RDFUtil;
import org.sbolstandard.core3.util.SBOLGraphException;
import org.sbolstandard.core3.validation.IdentityValidator;
import org.sbolstandard.core3.validation.SBOLValidator;
import org.sbolstandard.core3.vocabulary.DataModel;

public class CombinatorialDerivation extends TopLevel{
	private URI template=null;
	private URI strategy=null;
	private List<VariableFeature> variableFeatures=new ArrayList<VariableFeature>();
	
	protected  CombinatorialDerivation(Model model,URI uri) throws SBOLGraphException
	{
		super(model, uri);
	}
	
	protected  CombinatorialDerivation(Resource resource) throws SBOLGraphException
	{
		super(resource);
	}
	
	public URI getTemplate() throws SBOLGraphException {
		if (template==null)
		{
			template=IdentityValidator.getValidator().getPropertyAsURI(this.resource, DataModel.CombinatorialDerivation.template);	
		}
		return template;
	}

	public void setTemplate(URI template) {
		this.template = template;
		RDFUtil.setProperty(resource, DataModel.CombinatorialDerivation.template, template);
	}
	
	public URI getStrategy() throws SBOLGraphException {
		if (strategy==null)
		{
			strategy=IdentityValidator.getValidator().getPropertyAsURI(this.resource, DataModel.CombinatorialDerivation.strategy);	
		}
		return strategy;
	}

	public void setStrategy(URI strategy) {
		this.strategy = strategy;
		RDFUtil.setProperty(resource, DataModel.CombinatorialDerivation.strategy, strategy);
	}


	public List<VariableFeature> getVariableFeatures() throws SBOLGraphException {
		this.variableFeatures=addToList(this.variableFeatures, DataModel.CombinatorialDerivation.variableFeature, VariableFeature.class, DataModel.VariableComponent.uri);
		return this.variableFeatures;
	}
	
	public VariableFeature createVariableFeature(URI uri, URI cardinality, URI subComponent) throws SBOLGraphException
	{
		VariableFeature variableComponent= new VariableFeature(this.resource.getModel(), uri);
		variableComponent.setCardinality(cardinality);
		variableComponent.setFeature(subComponent);
		this.variableFeatures=addToList (this.variableFeatures, variableComponent, DataModel.CombinatorialDerivation.variableFeature);
		return variableComponent;	
	}
	
	private VariableFeature createVariableFeature(String displayId, URI cardinality, URI subComponent) throws SBOLGraphException
	{
		return createVariableFeature(SBOLAPI.append(this.getUri(), displayId), cardinality, subComponent);	
	}
	
	public VariableFeature createVariableComponent(URI cardinality, URI subComponent) throws SBOLGraphException
	{
		String displayId=SBOLAPI.createLocalName(DataModel.VariableComponent.uri, getVariableFeatures());	
		return createVariableFeature(displayId, cardinality, subComponent);	
	}
	

	@Override
	public URI getResourceType() {
		return DataModel.CombinatorialDerivation.uri;
	}
	
	
}