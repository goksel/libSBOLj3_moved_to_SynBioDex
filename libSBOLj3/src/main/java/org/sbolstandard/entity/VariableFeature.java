package org.sbolstandard.entity;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.sbolstandard.util.RDFUtil;
import org.sbolstandard.util.SBOLGraphException;
import org.sbolstandard.vocabulary.DataModel;

public class VariableFeature extends Identified{
	private URI cardinality=null;
	private URI feature=null;
	private List<URI> variants=new ArrayList<URI>();
	private List<URI> variantCollections=new ArrayList<URI>();
	private List<URI> variantDerivations=new ArrayList<URI>();
	private List<URI> variantMeasures=new ArrayList<URI>();
	
	
	protected  VariableFeature(Model model,URI uri) throws SBOLGraphException
	{
		super(model, uri);
	}
	
	protected  VariableFeature(Resource resource)
	{
		super(resource);
	}
	
	public URI getCardinality() throws SBOLGraphException {
		if (cardinality==null)
		{
			cardinality=RDFUtil.getPropertyAsURI(this.resource, DataModel.VariableComponent.cardinality);	
		}
		return cardinality;
	}

	public void setCardinality(URI cardinality) {
		this.cardinality = cardinality;
		RDFUtil.setProperty(resource, DataModel.VariableComponent.cardinality, cardinality);
	}
	
	public URI getFeature() throws SBOLGraphException {
		if (feature==null)
		{
			feature=RDFUtil.getPropertyAsURI(this.resource, DataModel.VariableComponent.variable);	
		}
		return feature;
	}

	public void setFeature(URI variant) {
		this.feature = variant;
		RDFUtil.setProperty(resource, DataModel.VariableComponent.variable, variant);
	}
	
	public List<URI> getVariants() {
		if (variants==null)
		{
			variants=RDFUtil.getPropertiesAsURIs(this.resource, DataModel.VariableComponent.variant);
		}
		return variants;
	}
	
	public void setVariants(List<URI> variants) {
		this.variants = variants;
		RDFUtil.setProperty(resource, DataModel.VariableComponent.variant, variants);
	}
	
	public List<URI> getVariantCollections() {
		if (variantCollections==null)
		{
			variantCollections=RDFUtil.getPropertiesAsURIs(this.resource, DataModel.VariableComponent.variantCollection);
		}
		return variantCollections;
	}
	
	public void setVariantCollections(List<URI> variantCollections) {
		this.variantCollections = variantCollections;
		RDFUtil.setProperty(resource, DataModel.VariableComponent.variantCollection, variantCollections);
	}
	
	public List<URI> getVariantDerivations() {
		if (variantDerivations==null)
		{
			variantDerivations=RDFUtil.getPropertiesAsURIs(this.resource, DataModel.VariableComponent.variantDerivation);
		}
		return variantDerivations;
	}
	
	public void setVariantDerivations(List<URI> variantDerivations) {
		this.variantDerivations = variantDerivations;
		RDFUtil.setProperty(resource, DataModel.VariableComponent.variantDerivation, variantDerivations);
	}
	
	
	public List<URI> getVariantMeasures() {
		if (this.variantMeasures==null)
		{
			variantMeasures=RDFUtil.getPropertiesAsURIs(this.resource, DataModel.VariableComponent.variantMeasure);
		}
		return variantMeasures;
	}
	
	public void setVariantMeasures(List<URI> variantMeasures) {
		this.variantMeasures = variantMeasures;
		RDFUtil.setProperty(resource, DataModel.VariableComponent.variantMeasure, variantMeasures);
	}
	
	
	@Override
	public URI getResourceType() {
		return DataModel.VariableComponent.uri;
	}
	
	
}