package org.sbolstandard.core3.entity;

import java.net.URI;
import java.util.List;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.sbolstandard.core3.util.RDFUtil;
import org.sbolstandard.core3.util.SBOLGraphException;
import org.sbolstandard.core3.validation.IdentifiedValidator;
import org.sbolstandard.core3.vocabulary.DataModel;
import org.sbolstandard.core3.vocabulary.Orientation;

public abstract class Feature extends Identified{
	/*private List<URI> roles=null;
	private Orientation orientation=null;*/
	
	protected  Feature(Model model,URI uri) throws SBOLGraphException
	{
		super(model, uri);
	}
	
	protected  Feature(Resource resource) throws SBOLGraphException
	{
		super(resource);
	}

	public List<URI> getRoles() {
		return RDFUtil.getPropertiesAsURIs(this.resource, DataModel.role);
	}
	
	public void setRoles(List<URI> roles) {
		RDFUtil.setProperty(resource, DataModel.role, roles);
	}
	
	public Orientation getOrientation() throws SBOLGraphException {
		Orientation orientation=null;
		
		URI value=IdentifiedValidator.getValidator().getPropertyAsURI(this.resource, DataModel.orientation);
		if (value!=null){
			orientation=Orientation.get(value); 			
		}
		return orientation;
	}
	
	public void setOrientation(Orientation orientation) {
		URI orientationURI=null;
		if (orientation!=null)
		{
			orientationURI=orientation.getUri();
		}
		RDFUtil.setProperty(this.resource, DataModel.orientation, orientationURI);
	}
}