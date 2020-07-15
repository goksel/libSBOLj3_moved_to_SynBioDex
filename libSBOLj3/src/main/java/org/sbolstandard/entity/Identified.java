package org.sbolstandard.entity;

import java.lang.reflect.Constructor;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.sbolstandard.util.RDFHandler;
import org.sbolstandard.util.RDFUtil;
import org.sbolstandard.util.SBOLGraphException;

public abstract class Identified {
	protected Resource resource=null;
	protected Identified()
	{}
	
	protected Identified(Model model, URI uri) throws SBOLGraphException
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
	
	protected <T extends Identified> Identified createIdentified(Resource res, Class<T> identified) throws SBOLGraphException
	{
		try
		{
			Constructor<T> constructor = identified.getDeclaredConstructor( new Class[] {Resource.class});
			Identified entity= (Identified)constructor.newInstance(new Object[]{res});
			return entity;
		}
		catch (Exception ex)
		{
			throw new SBOLGraphException(ex.getMessage());
		}
	}
	
	/***
	 * Deserialises an array of objects from RDF resources
	 * @param items
	 * @param property
	 * @param identifiedClass
	 * @return
	 * @throws SBOLGraphException
	 */
	protected <T extends Identified>  List<T> addToList(List<T> items, URI property, Class<T> identifiedClass) throws SBOLGraphException
	{
		if (items==null)
		{
			List<Resource> resources=RDFUtil.getResourcesWithProperty(this.resource, property);
			if (resources!=null && resources.size()>0)
			{
				items=new ArrayList<T>();
			}
			for (Resource res:resources)
			{
				Identified identified=createIdentified(res, identifiedClass);
				items.add((T)identified);
			}
		}
		return items;
	}
	
	protected <T extends Identified>  T contsructIdentified(URI property, Class<T> identifiedClass) throws SBOLGraphException
	{
		T identified=null;
		List<Resource> resources=RDFUtil.getResourcesWithProperty(this.resource, property);
		if (resources!=null)
		{
			if (resources.size()==1)
			{
				 identified=(T)createIdentified(resources.get(0), identifiedClass);
			}
			else
			{
				String message=String.format("Multiple property values exist for the property %s. The entity URI:%s", property.toString(),this.resource.getURI());
				throw new SBOLGraphException(message);
			}
		}		
		return identified;
	}
	
	
	/***
	 * Serialises an array of objects into RDF resources.
	 * @param items
	 * @param identified
	 * @param property
	 * @return
	 */
	protected <T extends Identified> List<T> addToList(List<T> items, Identified identified, URI property)
	{
		RDFUtil.addProperty(this.resource,property, identified.getUri());
		
		if (items==null)
		{
			items=new ArrayList<T>();
		}
		items.add((T)identified);
		return items;
	}
	
	
	protected <T extends Identified> List<T> addToList(List<T> items, URI property, Class<T> identifiedClass, URI identifiedResourceType) throws SBOLGraphException
	{
		if (items==null)
		{
			List<Resource> resources=RDFUtil.getResourcesWithProperty(this.resource, property, identifiedResourceType);
			if (resources!=null && resources.size()>0)
			{
				items=new ArrayList<T>();
			}
			for (Resource res:resources)
			{
				Identified identified=createIdentified(res, identifiedClass);
				items.add((T)identified);
			}
		}
		return items;
	}
	
	
	
}

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

/*protected Identified get_del(Resource res, URI property, URI entityType) throws SBOLGraphException, SBOLException
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

public Resource toResource(Model model)
	{
		URI resourceType=this.getResourceType();
		
		Resource resource=RDFHandler.createResource(model, this.getUri(), resourceType);
		
		RDFHandler.addProperty(model, resource, URI.create("http://sbols.org/v3#displayId"), this.displayId);
		RDFHandler.addProperty(model, resource, URI.create("http://sbols.org/v3#name"), this.name);
		RDFHandler.addProperty(model, resource, URI.create("http://sbols.org/v3#description"), this.description);		
		RDFHandler.addProperty(model, resource, URI.create("https://www.w3.org/TR/prov-o/wasGeneratedBy"), this.wasGeneratedBy);
		RDFHandler.addProperty(model, resource, URI.create("https://www.w3.org/TR/prov-o/wasDerivedFrom"), this.wasDerivedFrom);
		
		this.addEntitySpecificProperties(model, resource);
		
		return resource;
	}
	
*
*/

