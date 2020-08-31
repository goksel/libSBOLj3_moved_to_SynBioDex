package org.sbolstandard.entity;

import java.net.URI;
import java.util.List;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.sbolstandard.util.RDFUtil;
import org.sbolstandard.util.SBOLGraphException;
import org.sbolstandard.vocabulary.DataModel;
import org.sbolstandard.vocabulary.Orientation;

public abstract class Feature extends Identified{
	private List<URI> roles=null;
	private Orientation orientation=null;
	
	
	protected  Feature(Model model,URI uri) throws SBOLGraphException
	{
		super(model, uri);
	}
	
	protected  Feature(Resource resource)
	{
		super(resource);
	}

	public List<URI> getRoles() {
		if (roles==null)
		{
			roles=RDFUtil.getPropertiesAsURIs(this.resource, DataModel.role);
		}
		return roles;
	}
	
	public void setRoles(List<URI> roles) {
		this.roles = roles;
		RDFUtil.setProperty(resource, DataModel.role, roles);
	}
	
	public Orientation getOrientation() {
		if (orientation==null)
		{
			URI value=RDFUtil.getPropertyAsURI(this.resource, DataModel.orientation);
			if (value!=null)
			{
				orientation=Orientation.get(value); 			
			}
		}
		return orientation;
	}
	
	public void setOrientation(Orientation orientation) {
		this.orientation = orientation;
		RDFUtil.setProperty(this.resource, DataModel.orientation, this.orientation.getUri());
	}
	
	
}