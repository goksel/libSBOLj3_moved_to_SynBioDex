package org.sbolstandard;

import java.net.URI;
import java.util.List;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.ResourceFactory;
import org.sbolstandard.util.RDFUtil;
import org.sbolstandard.vocabulary.DataModel;

public abstract class Identified {
	protected Resource resource=null;
	protected Identified()
	{}
	
	protected Identified(Model model, URI uri)
	{
		this.uri=uri;
		this.resource=RDFUtil.createResource(model, this.uri,this.getResourceType());
	}
	
	protected Identified(Resource resource)
	{
		this.uri=URI.create(resource.getURI());
		this.resource=resource;
	}
	
	public Identified (URI uri)
	{
		this.uri=uri;
	}
	
	public String getDisplayId() {
		return displayId;
	}
	public void setDisplayId(String displayId) {
		this.displayId = displayId;
		RDFUtil.setProperty(resource, URI.create("http://sbols.org/v3#displayId"), displayId);		
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
		RDFUtil.setProperty(resource, URI.create("http://sbols.org/v3#name"), name);	
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
		RDFUtil.setProperty(resource, URI.create("http://sbols.org/v3#description"), description);	
		
	}
	public List<URI> getWasDerivedFrom() {
		return wasDerivedFrom;
	}
	public void setWasDerivedFrom(List<URI> wasDerivedFrom) {
		this.wasDerivedFrom = wasDerivedFrom;
		RDFUtil.setProperty(resource, URI.create("https://www.w3.org/TR/prov-o/wasDerivedFrom"), this.wasDerivedFrom);
	}
	public List<URI> getWasGeneratedy() {
		return wasGeneratedBy;
	}
	public void setWasGeneratedy(List<URI> wasGeneratedy) {
		this.wasGeneratedBy = wasGeneratedy;
		RDFUtil.setProperty(resource, URI.create("https://www.w3.org/TR/prov-o/wasGeneratedBy"), this.wasGeneratedBy);

	}
	
	private String displayId;
	private String name;
	private String description;
	private List<URI> wasDerivedFrom;
	private List<URI> wasGeneratedBy;
	private URI uri;
	
	public URI getUri() {
		return uri;
	}
	
	abstract public URI getResourceType();
	
	/*protected <T extends Identified>  void addToList(Resource res, List<T> items, URI property, URI entityType) throws SBOLException, SBOLGraphException
	{
		List<Resource> resources=RDFUtil.getResourcesWithProperty(res, property);
		for (Resource subResource:resources)
		{
			Identified identified=SBOLEntityFactory.create(subResource, entityType) ;
			items.add((T)identified);
		}
	}
	*/
	
	protected Identified get(Resource res, URI property, URI entityType) throws SBOLGraphException, SBOLException
	{
		Identified identified=null;
		List<Resource> resources=RDFUtil.getResourcesWithProperty(res, property);
		if (resources!=null && resources.size()>1)
		{
			String message=String.format("Found multiple versiond of %s entity", entityType.toString());
			throw new SBOLGraphException(message);
		}
		else
		{
			identified=SBOLEntityFactory.create(resources.get(0), entityType) ;
		}
		return identified;
	}

	
}
