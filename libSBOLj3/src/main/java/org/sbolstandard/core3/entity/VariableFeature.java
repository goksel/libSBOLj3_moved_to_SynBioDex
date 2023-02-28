package org.sbolstandard.core3.entity;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.sbolstandard.core3.entity.measure.Measure;
import org.sbolstandard.core3.util.RDFUtil;
import org.sbolstandard.core3.util.SBOLGraphException;
import org.sbolstandard.core3.util.SBOLUtil;
import org.sbolstandard.core3.validation.IdentifiedValidator;
import org.sbolstandard.core3.validation.PropertyValidator;
import org.sbolstandard.core3.validation.ValidationMessage;
import org.sbolstandard.core3.vocabulary.DataModel;
import org.sbolstandard.core3.vocabulary.MeasureDataModel;
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
	
	@Override
	public List<ValidationMessage> getValidationMessages() throws SBOLGraphException
	{
		List<ValidationMessage> validationMessages=super.getValidationMessages();
		validationMessages= IdentifiedValidator.assertEquals(this, DataModel.VariableFeature.variable, this.resource, getVariable(), validationMessages);
		validationMessages= IdentifiedValidator.assertExists(this, DataModel.VariableFeature.variantMeasure, this.resource, getMeasures(), validationMessages);
		validationMessages= IdentifiedValidator.assertExists(this, DataModel.VariableFeature.variantCollection, this.resource, getVariantCollections(), validationMessages);
		validationMessages= IdentifiedValidator.assertExists(this, DataModel.VariableFeature.variantDerivation, this.resource, getVariantDerivations(), validationMessages);
		validationMessages= IdentifiedValidator.assertExists(this, DataModel.VariableFeature.variant, this.resource, getVariants(), validationMessages);
		return validationMessages;
	}

	@NotNull(message = "{VARIABLEFEATURE_CARDINALITY_NOT_NULL}")
	public VariableFeatureCardinality getCardinality() throws SBOLGraphException {
		VariableFeatureCardinality cardinality=null;
		URI value= IdentifiedValidator.getValidator().getPropertyAsURI(this.resource, DataModel.VariableFeature.cardinality);	
		if (value!=null)
		{
			cardinality=toCardinality(value); 
			PropertyValidator.getValidator().validateReturnValue(this, "toCardinality", cardinality, URI.class);
		}
		return cardinality;
	}

	@NotNull(message = "{VARIABLEFEATURE_CARDINALITY_VALID_IF_NOT_NULL}")   
	public VariableFeatureCardinality toCardinality(URI uri)
	{
		return VariableFeatureCardinality.get(uri); 
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
	public Feature getVariable() throws SBOLGraphException {
		//return IdentifiedValidator.getValidator().getPropertyAsURI(this.resource, DataModel.VariableFeature.variable);
		return contsructIdentified(DataModel.VariableFeature.variable, Feature.getSubClassTypes());
	}

	public void setVariable(@NotNull(message = "{VARIABLEFEATURE_FEATURE_NOT_NULL}") Feature feature) throws SBOLGraphException {
		PropertyValidator.getValidator().validate(this, "setVariable", new Object[] {feature}, Feature.class);
		RDFUtil.setProperty(resource, DataModel.VariableFeature.variable, SBOLUtil.toURI(feature));
	}
	
	public List<Component> getVariants() throws SBOLGraphException {
		//return RDFUtil.getPropertiesAsURIs(this.resource, DataModel.VariableFeature.variant);
		return addToList(DataModel.VariableFeature.variant, Component.class, DataModel.Component.uri);
	}
	
	public void setVariants(List<Component> variants) {
		RDFUtil.setProperty(resource, DataModel.VariableFeature.variant, SBOLUtil.getURIs(variants));
	}
	
	public List<Collection> getVariantCollections() throws SBOLGraphException {
		//return RDFUtil.getPropertiesAsURIs(this.resource, DataModel.VariableFeature.variantCollection);
		return addToList(DataModel.VariableFeature.variantCollection, Collection.class, DataModel.Collection.uri);
	}
	
	public void setVariantCollections(List<Collection> variantCollections) {
		RDFUtil.setProperty(resource, DataModel.VariableFeature.variantCollection, SBOLUtil.getURIs(variantCollections));
	}
	
	public List<CombinatorialDerivation> getVariantDerivations() throws SBOLGraphException {
		//return RDFUtil.getPropertiesAsURIs(this.resource, DataModel.VariableFeature.variantDerivation);
		return addToList(DataModel.VariableFeature.variantDerivation, CombinatorialDerivation.class, DataModel.CombinatorialDerivation.uri);
	}
	
	public void setVariantDerivations(List<CombinatorialDerivation> variantDerivations) {
		RDFUtil.setProperty(resource, DataModel.VariableFeature.variantDerivation, SBOLUtil.getURIs(variantDerivations));
	}
	
	public List<Measure> getVariantMeasures() throws SBOLGraphException {
		//return RDFUtil.getPropertiesAsURIs(this.resource, DataModel.VariableFeature.variantMeasure);
		return addToList(DataModel.VariableFeature.variantMeasure, Measure.class, MeasureDataModel.Measure.uri);
	}
	
	public void setVariantMeasures(List<Measure> variantMeasures) {
		RDFUtil.setProperty(resource, DataModel.VariableFeature.variantMeasure, SBOLUtil.getURIs(variantMeasures));
	}
	
	@Override
	public URI getResourceType() {
		return DataModel.VariableFeature.uri;
	}
	
	
}