package org.sbolstandard.core3.entity;

import java.net.URI;

import org.apache.jena.rdf.model.Resource;
import org.sbolstandard.core3.util.RDFUtil;
import org.sbolstandard.core3.util.SBOLGraphException;
import org.sbolstandard.core3.validation.IdentityValidator;
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
	
	@NotNull(message = "Model.source cannot be null")
	public URI getSource() throws SBOLGraphException {
		return IdentityValidator.getValidator().getPropertyAsURI(this.resource, DataModel.Model.source);	
	}

	public void setSource(URI source) {
		RDFUtil.setProperty(resource, DataModel.Model.source, source);
	}

	@NotNull(message = "Model.framework cannot be null")
	public URI getFramework() throws SBOLGraphException {
		return IdentityValidator.getValidator().getPropertyAsURI(this.resource, DataModel.Model.framework);	
	}

	public void setFramework(URI framework) {
		RDFUtil.setProperty(resource, DataModel.Model.framework, framework);
	}

	@NotNull(message = "Model.language cannot be null")
	public URI getLanguage() throws SBOLGraphException {
		return IdentityValidator.getValidator().getPropertyAsURI(this.resource, DataModel.Model.language);	
	}

	public void setLanguage(URI language) {
		RDFUtil.setProperty(resource, DataModel.Model.language, language);
	}

	@Override
	public URI getResourceType() {
		return DataModel.Model.uri;
	}
	
}