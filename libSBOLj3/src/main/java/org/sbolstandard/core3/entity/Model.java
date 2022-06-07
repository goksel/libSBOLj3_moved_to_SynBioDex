package org.sbolstandard.core3.entity;

import java.net.URI;
import java.util.List;

import org.apache.jena.rdf.model.Resource;
import org.sbolstandard.core3.util.RDFUtil;
import org.sbolstandard.core3.util.SBOLGraphException;
import org.sbolstandard.core3.validation.IdentifiedValidator;
import org.sbolstandard.core3.validation.PropertyValidator;
import org.sbolstandard.core3.vocabulary.DataModel;

import jakarta.validation.constraints.NotNull;

public class Model extends TopLevel{
	/*private URI source=null;
	private URI framework=null;
	private URI language=null;*/

	protected  Model(org.apache.jena.rdf.model.Model model,URI uri) throws SBOLGraphException
	{
		super(model, uri);
	}
	
	protected  Model(Resource resource) throws SBOLGraphException
	{
		super(resource);
	}
	
	@NotNull(message = "{MODEL_SOURCE_NOT_NULL}")
	public URI getSource() throws SBOLGraphException {
		return IdentifiedValidator.getValidator().getPropertyAsURI(this.resource, DataModel.Model.source);	
	}

	public void setSource(@NotNull(message = "{MODEL_SOURCE_NOT_NULL}") URI source) throws SBOLGraphException {
		PropertyValidator.getValidator().validate(this, "setSource", new Object[] {source}, URI.class);
		RDFUtil.setProperty(resource, DataModel.Model.source, source);
	}

	@NotNull(message = "{MODEL_FRAMEWORK_NOT_NULL}")
	public URI getFramework() throws SBOLGraphException {
		return IdentifiedValidator.getValidator().getPropertyAsURI(this.resource, DataModel.Model.framework);	
	}

	public void setFramework(@NotNull(message = "{MODEL_FRAMEWORK_NOT_NULL}") URI framework) throws SBOLGraphException{
		PropertyValidator.getValidator().validate(this, "setFramework", new Object[] {framework}, URI.class);
		RDFUtil.setProperty(resource, DataModel.Model.framework, framework);
	}

	@NotNull(message = "{MODEL_LANGUAGE_NOT_NULL}")
	public URI getLanguage() throws SBOLGraphException {
		return IdentifiedValidator.getValidator().getPropertyAsURI(this.resource, DataModel.Model.language);	
	}

	public void setLanguage(@NotNull(message = "{MODEL_LANGUAGE_NOT_NULL}") URI language) throws SBOLGraphException {
		PropertyValidator.getValidator().validate(this, "setLanguage", new Object[] {language}, URI.class);
		RDFUtil.setProperty(resource, DataModel.Model.language, language);
	}

	@Override
	public URI getResourceType() {
		return DataModel.Model.uri;
	}
	
}