package org.sbolstandard.core3.entity;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.sbolstandard.core3.util.RDFUtil;
import org.sbolstandard.core3.util.SBOLGraphException;
import org.sbolstandard.core3.validation.IdentifiedValidator;
import org.sbolstandard.core3.validation.PropertyValidator;
import org.sbolstandard.core3.vocabulary.DataModel;
import org.sbolstandard.core3.vocabulary.Orientation;
import org.sbolstandard.core3.vocabulary.VariableFeatureCardinality;

import jakarta.validation.constraints.NotNull;

public class VariableFeature extends Identified{
	/*private URI cardinality=null;
	private URI feature=null;
	private List<URI> variants=new ArrayList<URI>();
	private List<URI> variantCollections=new ArrayList<URI>();
	private List<URI> variantDerivations=new ArrayList<URI>();
	private List<URI> variantMeasures=new ArrayList<URI>();*/
	
	
	protected  VariableFeature(Model model,URI uri) throws SBOLGraphException
	{
		super(model, uri);
	}
	
	protected  VariableFeature(Resource resource) throws SBOLGraphException
	{
		super(resource);
	}
	
	@NotNull(message = "{VARIABLEFEATURE_CARDINALITY_NOT_NULL}")
	public VariableFeatureCardinality getCardinality() throws SBOLGraphException {
		VariableFeatureCardinality cardinality=null;
		URI value= IdentifiedValidator.getValidator().getPropertyAsURI(this.resource, DataModel.VariableFeature.cardinality);	
		if (value!=null)
		{
			cardinality=VariableFeatureCardinality.get(value);
		}
		return cardinality;
	}

	public void setCardinality(@NotNull(message = "{VARIABLEFEATURE_CARDINALITY_NOT_NULL}") VariableFeatureCardinality cardinality) throws SBOLGraphException {
		PropertyValidator.getValidator().validate(this, "setCardinality", new Object[] {cardinality}, VariableFeatureCardinality.class);
		URI uri=null;
		if (cardinality!=null)
		{
			uri=cardinality.getUri();
		}
		RDFUtil.setProperty(resource, DataModel.VariableFeature.cardinality, uri);
	}
	
	@NotNull(message = "{VARIABLEFEATURE_FEATURE_NOT_NULL}")
	public URI getFeature() throws SBOLGraphException {
		return IdentifiedValidator.getValidator().getPropertyAsURI(this.resource, DataModel.VariableFeature.variable);	
	}

	public void setFeature(@NotNull(message = "{VARIABLEFEATURE_FEATURE_NOT_NULL}") URI feature) throws SBOLGraphException {
		PropertyValidator.getValidator().validate(this, "setFeature", new Object[] {feature}, URI.class);
		RDFUtil.setProperty(resource, DataModel.VariableFeature.variable, feature);
	}
	
	public List<URI> getVariants() {
		return RDFUtil.getPropertiesAsURIs(this.resource, DataModel.VariableFeature.variant);
	}
	
	public void setVariants(List<URI> variants) {
		RDFUtil.setProperty(resource, DataModel.VariableFeature.variant, variants);
	}
	
	public List<URI> getVariantCollections() {
		return RDFUtil.getPropertiesAsURIs(this.resource, DataModel.VariableFeature.variantCollection);
	}
	
	public void setVariantCollections(List<URI> variantCollections) {
		RDFUtil.setProperty(resource, DataModel.VariableFeature.variantCollection, variantCollections);
	}
	
	public List<URI> getVariantDerivations() {
		return RDFUtil.getPropertiesAsURIs(this.resource, DataModel.VariableFeature.variantDerivation);
	}
	
	public void setVariantDerivations(List<URI> variantDerivations) {
		RDFUtil.setProperty(resource, DataModel.VariableFeature.variantDerivation, variantDerivations);
	}
	
	public List<URI> getVariantMeasures() {
		return RDFUtil.getPropertiesAsURIs(this.resource, DataModel.VariableFeature.variantMeasure);
	}
	
	public void setVariantMeasures(List<URI> variantMeasures) {
		RDFUtil.setProperty(resource, DataModel.VariableFeature.variantMeasure, variantMeasures);
	}
	
	@Override
	public URI getResourceType() {
		return DataModel.VariableFeature.uri;
	}
	
	
}