package org.sbolstandard.core3.entity.measure;

import java.net.URI;
import java.util.HashMap;
import java.util.Optional;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.sbolstandard.core3.entity.Identified;
import org.sbolstandard.core3.util.SBOLGraphException;
import org.sbolstandard.core3.validation.IdentifiedValidator;
import org.sbolstandard.core3.validation.PropertyValidator;
import org.sbolstandard.core3.vocabulary.MeasureDataModel;

import jakarta.validation.constraints.NotNull;

public abstract class Prefix extends Unit{
	
	//private float factor=Float.NaN;
	protected  Prefix(Model model,URI uri) throws SBOLGraphException
	{
		super(model, uri);
	}
	
	protected  Prefix(Resource resource) throws SBOLGraphException
	{
		super(resource);
	}
	 
	public void setFactor(@NotNull (message = "{PREFIX_FACTOR_NOT_NULL}") Optional<@NotNull (message = "{PREFIX_FACTOR_NOT_NULL}") Float> factor) throws SBOLGraphException {
		PropertyValidator.getValidator().validate(this, "setFactor", new Object[] {factor}, Optional.class);
		IdentifiedValidator.getValidator().setPropertyAsOptional(this.resource, MeasureDataModel.Prefix.factor, factor);		
	}
	
	@NotNull (message = "{PREFIX_FACTOR_NOT_NULL}") 
	public Optional<@NotNull (message = "{PREFIX_FACTOR_NOT_NULL}") Float> getFactor() throws SBOLGraphException {
		return IdentifiedValidator.getValidator().getPropertyAsOptionalFloat(this.resource, MeasureDataModel.Prefix.factor);
	}

	@Override
	public URI getResourceType() {
		return MeasureDataModel.Prefix.uri;
	}	
	
	@SuppressWarnings("unchecked")
	public static <T extends Identified> HashMap<URI, Class<T>> getSubClassTypes()
	{
		HashMap<URI, Class<T>> subclasses=new HashMap<URI, Class<T>>();
		subclasses.put(MeasureDataModel.BinaryPrefix.uri, (Class<T>) BinaryPrefix.class);
		subclasses.put(MeasureDataModel.SIPrefix.uri, (Class<T>) SIPrefix.class);
		return subclasses;
	}
}