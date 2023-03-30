package org.sbolstandard.core3.entity;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.sbolstandard.core3.util.RDFUtil;
import org.sbolstandard.core3.util.SBOLGraphException;
import org.sbolstandard.core3.validation.IdentifiedValidator;
import org.sbolstandard.core3.validation.PropertyValidator;
import org.sbolstandard.core3.vocabulary.DataModel;
import org.sbolstandard.core3.vocabulary.Orientation;
import jakarta.validation.constraints.NotNull;

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
	
	public void addRole(URI role) {
		RDFUtil.addProperty(resource, DataModel.role, role);
	}
	
	public Orientation getOrientation() throws SBOLGraphException {
		Orientation orientation=null;
		
		URI value=IdentifiedValidator.getValidator().getPropertyAsURI(this.resource, DataModel.orientation);
		if (value!=null){
			
			orientation=toOrientation(value); 	
			PropertyValidator.getValidator().validateReturnValue(this, "toOrientation", orientation, URI.class);
		}
		return orientation;
	}
	
	@NotNull(message = "{FEATURE_ORIENTATION_VALID_IF_NOT_NULL}")   
	public Orientation toOrientation (URI uri)
	{
		return Orientation.get(uri); 
	}
	
	public void setOrientation(Orientation orientation) {
		URI orientationURI=null;
		if (orientation!=null)
		{
			orientationURI=orientation.getUri();
		}
		RDFUtil.setProperty(this.resource, DataModel.orientation, orientationURI);
	}
	
	@SuppressWarnings("unchecked")
	public static <T extends Identified> HashMap<URI, Class<T>> getSubClassTypes()
	{
		HashMap<URI, Class<T>> subclasses=new HashMap<URI, Class<T>>();
		subclasses.put(DataModel.ComponentReference.uri, (Class<T>) ComponentReference.class);
		subclasses.put(DataModel.SubComponent.uri,  (Class<T>)SubComponent.class);
		subclasses.put(DataModel.LocalSubComponent.uri,(Class<T>)  LocalSubComponent.class);
		subclasses.put(DataModel.ExternalyDefined.uri, (Class<T>)  ExternallyDefined.class);
		subclasses.put(DataModel.SequenceFeature.uri,(Class<T>)  SequenceFeature.class);
		return subclasses;
	}
	
	/*public static HashMap<URI, Class> getSubClassTypes()
	{
		HashMap<URI, Class> subclasses=new HashMap<URI, Class>();
		subclasses.put(DataModel.ComponentReference.uri,  ComponentReference.class);
		subclasses.put(DataModel.SubComponent.uri,  SubComponent.class);
		subclasses.put(DataModel.LocalSubComponent.uri,  LocalSubComponent.class);
		subclasses.put(DataModel.ExternalyDefined.uri,  ExternallyDefined.class);
		subclasses.put(DataModel.SequenceFeature.uri,  SequenceFeature.class);
		return subclasses;
	}*/
}