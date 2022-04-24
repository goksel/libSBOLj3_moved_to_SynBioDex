package org.sbolstandard.core3.entity;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.sbolstandard.core3.api.SBOLAPI;
import org.sbolstandard.core3.util.RDFUtil;
import org.sbolstandard.core3.util.SBOLGraphException;
import org.sbolstandard.core3.validation.IdentifiedValidator;
import org.sbolstandard.core3.validation.PropertyValidator;
import org.sbolstandard.core3.validation.SBOLValidator;
import org.sbolstandard.core3.vocabulary.DataModel;
import org.sbolstandard.core3.vocabulary.VariableFeatureCardinality;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public class CombinatorialDerivation extends TopLevel{
	/*private URI template=null;
	private URI strategy=null;
	private List<VariableFeature> variableFeatures=new ArrayList<VariableFeature>();*/
	
	protected  CombinatorialDerivation(Model model,URI uri) throws SBOLGraphException
	{
		super(model, uri);
	}
	
	protected  CombinatorialDerivation(Resource resource) throws SBOLGraphException
	{
		super(resource);
	}
	
	@NotNull(message = "{COMBINATORIALDERIVATION_TEMPLATE_NOT_NULL}")
	public URI getTemplate() throws SBOLGraphException {
		return IdentifiedValidator.getValidator().getPropertyAsURI(this.resource, DataModel.CombinatorialDerivation.template);
	}

	public void setTemplate(@NotNull(message = "{COMBINATORIALDERIVATION_TEMPLATE_NOT_NULL}") URI template) throws SBOLGraphException {
		PropertyValidator.getValidator().validate(this, "setTemplate", new Object[] {template}, URI.class);
		RDFUtil.setProperty(resource, DataModel.CombinatorialDerivation.template, template);
	}
	
	public URI getStrategy() throws SBOLGraphException {
		return IdentifiedValidator.getValidator().getPropertyAsURI(this.resource, DataModel.CombinatorialDerivation.strategy);
	}

	public void setStrategy(URI strategy) {
		RDFUtil.setProperty(resource, DataModel.CombinatorialDerivation.strategy, strategy);
	}

	@Valid
	public List<VariableFeature> getVariableFeatures() throws SBOLGraphException {
		return addToList(DataModel.CombinatorialDerivation.variableFeature, VariableFeature.class, DataModel.VariableComponent.uri);
	}
	
	public VariableFeature createVariableFeature(URI uri, VariableFeatureCardinality cardinality, URI subComponent) throws SBOLGraphException
	{
		VariableFeature variableComponent= new VariableFeature(this.resource.getModel(), uri);
		variableComponent.setCardinality(cardinality);
		variableComponent.setFeature(subComponent);
		addToList(variableComponent, DataModel.CombinatorialDerivation.variableFeature);
		return variableComponent;	
	}
	
	private VariableFeature createVariableFeature(String displayId, VariableFeatureCardinality cardinality, URI subComponent) throws SBOLGraphException
	{
		return createVariableFeature(SBOLAPI.append(this.getUri(), displayId), cardinality, subComponent);	
	}
	
	public VariableFeature createVariableFeature(VariableFeatureCardinality cardinality, URI subComponent) throws SBOLGraphException
	{
		String displayId=SBOLAPI.createLocalName(DataModel.VariableComponent.uri, getVariableFeatures());	
		return createVariableFeature(displayId, cardinality, subComponent);	
	}

	@Override
	public URI getResourceType() {
		return DataModel.CombinatorialDerivation.uri;
	}
	
	
}