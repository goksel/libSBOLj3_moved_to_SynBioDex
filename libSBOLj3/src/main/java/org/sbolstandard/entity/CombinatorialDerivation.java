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
	private List<VariableComponent> variableComponents=new ArrayList<VariableComponent>();
	
	protected  CombinatorialDerivation(Model model,URI uri) throws SBOLGraphException
	{
		super(model, uri);
	}
	
	protected  CombinatorialDerivation(Resource resource)
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


	public List<VariableComponent> getVariableComponents() throws SBOLGraphException {
		this.variableComponents=addToList(this.variableComponents, DataModel.CombinatorialDerivation.variableComponent, VariableComponent.class, DataModel.VariableComponent.uri);
		return this.variableComponents;
	}
	
	public VariableComponent createVariableComponent(URI uri, URI cardinality, URI subComponent) throws SBOLGraphException
	{
		VariableComponent variableComponent= new VariableComponent(this.resource.getModel(), uri);
		variableComponent.setCardinality(cardinality);
		variableComponent.setSubComponent(subComponent);
		this.variableComponents=addToList (this.variableComponents, variableComponent, DataModel.CombinatorialDerivation.variableComponent);
		return variableComponent;	
	}
	
	private VariableComponent createVariableComponent(String displayId, URI cardinality, URI subComponent) throws SBOLGraphException
	{
		return createVariableComponent(SBOLAPI.append(this.getUri(), displayId), cardinality, subComponent);	
	}
	
	public VariableComponent createVariableComponent(URI cardinality, URI subComponent) throws SBOLGraphException
	{
		String displayId=SBOLAPI.createLocalName(DataModel.VariableComponent.uri, getVariableComponents());	
		return createVariableComponent(displayId, cardinality, subComponent);	
	}
	

	@Override
	public URI getResourceType() {
		return DataModel.CombinatorialDerivation.uri;
	}
	
	
}