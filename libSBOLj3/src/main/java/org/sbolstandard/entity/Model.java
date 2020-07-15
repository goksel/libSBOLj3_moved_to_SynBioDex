package org.sbolstandard.entity;

import java.net.URI;

import org.apache.jena.rdf.model.Resource;
import org.sbolstandard.util.RDFUtil;
import org.sbolstandard.util.SBOLGraphException;
import org.sbolstandard.vocabulary.DataModel;

public class Model extends TopLevel{
	private URI source=null;
	private URI framework=null;
	private URI language=null;

	protected  Model(org.apache.jena.rdf.model.Model model,URI uri) throws SBOLGraphException
	{
		super(model, uri);
	}
	
	protected  Model(Resource resource)
	{
		super(resource);
	}
	
	public URI getSource() throws SBOLGraphException {
		if (source==null)
		{
			source=RDFUtil.getPropertyAsURI(this.resource, DataModel.Model.source);	
		}
		return source;
	}

	public void setSource(URI source) {
		this.source = source;
		RDFUtil.setProperty(resource, DataModel.Model.source, source);
	}

	
	public URI getFramework() {
		if (framework==null)
		{
			framework=RDFUtil.getPropertyAsURI(this.resource, DataModel.Model.framework);	
		}
		return framework;
	}

	public void setFramework(URI framework) {
		this.framework = framework;
		RDFUtil.setProperty(resource, DataModel.Model.framework, framework);
	}

	public URI getLanguage() {
		if (language==null)
		{
			language=RDFUtil.getPropertyAsURI(this.resource, DataModel.Model.language);	
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