package org.sbolstandard.core3.entity;

import java.net.URI;

import org.apache.jena.rdf.model.Resource;
import org.sbolstandard.core3.util.RDFUtil;
import org.sbolstandard.core3.util.SBOLGraphException;
import org.sbolstandard.core3.validation.IdentityValidator;
import org.sbolstandard.core3.vocabulary.DataModel;

public class Model extends TopLevel{
	private URI source=null;
	private URI framework=null;
	private URI language=null;

	protected  Model(org.apache.jena.rdf.model.Model model,URI uri) throws SBOLGraphException
	{
		super(model, uri);
	}
	
	protected  Model(Resource resource) throws SBOLGraphException
	{
		super(resource);
	}
	
	public URI getSource() throws SBOLGraphException {
		if (source==null)
		{
			source=IdentityValidator.getValidator().getPropertyAsURI(this.resource, DataModel.Model.source);	
		}
		return source;
	}

	public void setSource(URI source) {
		this.source = source;
		RDFUtil.setProperty(resource, DataModel.Model.source, source);
	}

	
	public URI getFramework() throws SBOLGraphException {
		if (framework==null)
		{
			framework=IdentityValidator.getValidator().getPropertyAsURI(this.resource, DataModel.Model.framework);	
		}
		return framework;
	}

	public void setFramework(URI framework) {
		this.framework = framework;
		RDFUtil.setProperty(resource, DataModel.Model.framework, framework);
	}

	public URI getLanguage() throws SBOLGraphException {
		if (language==null)
		{
			language=IdentityValidator.getValidator().getPropertyAsURI(this.resource, DataModel.Model.language);	
		}
		return language;
	}

	public void setLanguage(URI language) {
		this.language = language;
		RDFUtil.setProperty(resource, DataModel.Model.language, language);
	}

	@Override
	public URI getResourceType() {
		return DataModel.Model.uri;
	}
	
}