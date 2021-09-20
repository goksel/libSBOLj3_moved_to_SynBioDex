package org.sbolstandard.entity;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.sbolstandard.api.SBOLAPI;
import org.sbolstandard.util.RDFUtil;
import org.sbolstandard.util.SBOLGraphException;
import org.sbolstandard.vocabulary.DataModel;

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
			template=RDFUtil.getPropertyAsURI(this.resource, DataModel.CombinatorialDerivation.template);	
		}
		return template;
	}

	public void setTemplate(URI template) {
		this.template = template;
		RDFUtil.setProperty(resource, DataModel.CombinatorialDerivation.template, template);
	}
	
	public URI getStrategy() {
		if (strategy==null)
		{
			strategy=RDFUtil.getPropertyAsURI(this.resource, DataModel.CombinatorialDerivation.strategy);	
		}
		return strategy;
	}

	public void setStrategy(URI strategy) {
		this.strategy = strategy;
		RDFUtil.setProperty(resource, DataModel.CombinatorialDerivation.strategy, strategy);
	}


	public List<VariableFeature> getVariableComponents() throws SBOLGraphException {
		this.variableFeatures=addToList(this.variableFeatures, DataModel.CombinatorialDerivation.variableFeature, VariableFeature.class, DataModel.VariableComponent.uri);
		return this.variableFeatures;
	}
	
	public VariableFeature createVariableComponent(URI uri, URI cardinality, URI subComponent) throws SBOLGraphException
	{
		VariableFeature variableComponent= new VariableFeature(this.resource.getModel(), uri);
		variableComponent.setCardinality(cardinality);
		variableComponent.setFeature(subComponent);
		this.variableFeatures=addToList (this.variableFeatures, variableComponent, DataModel.CombinatorialDerivation.variableFeature);
		return variableComponent;	
	}
	
	private VariableFeature createVariableComponent(String displayId, URI cardinality, URI subComponent) throws SBOLGraphException
	{
		return createVariableComponent(SBOLAPI.append(this.getUri(), displayId), cardinality, subComponent);	
	}
	
	public VariableFeature createVariableComponent(URI cardinality, URI subComponent) throws SBOLGraphException
	{
		String displayId=SBOLAPI.createLocalName(DataModel.VariableComponent.uri, getVariableComponents());	
		return createVariableComponent(displayId, cardinality, subComponent);	
	}
	

	@Override
	public URI getResourceType() {
		return DataModel.CombinatorialDerivation.uri;
	}
	
	
}