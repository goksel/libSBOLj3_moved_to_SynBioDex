package org.sbolstandard.entity;

import java.lang.reflect.Constructor;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.rdf.model.StmtIterator;
import org.sbolstandard.api.SBOLAPI;
import org.sbolstandard.entity.measure.Measure;
import org.sbolstandard.util.RDFUtil;
import org.sbolstandard.util.SBOLGraphException;
import org.sbolstandard.util.SBOLUtil;
import org.sbolstandard.util.URINameSpace;
import org.sbolstandard.vocabulary.DataModel;
import org.sbolstandard.vocabulary.MeasureDataModel;

public abstract class Identified {
	protected Resource resource=null;
	
	private String displayId;
	private String name;
	private String description;
	private List<URI> wasDerivedFrom;
	private List<URI> wasGeneratedBy;
	private List<Measure> measures;
	//private List<Metadata> metadataList;
	private URI uri;
	
	protected Identified()
	{}
	
	protected Identified(Model model, URI uri) throws SBOLGraphException
	{
		this.uri=uri;
		this.resource=RDFUtil.createResource(model, this.uri,this.getResourceType());
		inferDisplayId(uri);
	}
	
	/*protected Identified(Model model, URI uri, URI resourceType) throws SBOLGraphException
	{
		this.uri=uri;
		this.resource=RDFUtil.createResource(model, this.uri,resourceType);
		inferDisplayId(uri);
	}*/
	
	protected Identified(Resource resource) throws SBOLGraphException
	{
		this.uri=URI.create(resource.getURI());
		this.resource=resource;
		inferDisplayId(this.uri);
	}
	
	/*public Identified (String displayId)
	{
		this.displayId=displayId;
		this.resource=ResourceFactory.createResource();	
	}*/
	
	public String getDisplayId() {
		if (displayId==null)
		{
			displayId=RDFUtil.getPropertyAsString(this.resource, DataModel.Identified.displayId);
		}
		return displayId;
	}
	
	public void setDisplayId(String displayId) {
		this.displayId = displayId;
		RDFUtil.setProperty(resource, DataModel.Identified.displayId, displayId);		
	}
	
	public String getName() {
		if (name==null)
		{
			name=RDFUtil.getPropertyAsString(this.resource, DataModel.Identified.name);
		}
		return name;
	}
	public void setName(String name) {
		if (name!=null){
			this.name = name;
			RDFUtil.setProperty(resource, DataModel.Identified.name, name);	
		}
	}
	public String getDescription() {
		if (description==null)
		{
			description=RDFUtil.getPropertyAsString(this.resource, DataModel.Identified.description);
		}
		return description;
	}
	public void setDescription(String description) {
		if (description!=null)
		{
			this.description = description;
			RDFUtil.setProperty(resource, DataModel.Identified.description, description);
		}
		
	}
	
	public List<URI> getWasDerivedFrom() {
		if (wasDerivedFrom==null)
		{
			wasDerivedFrom=RDFUtil.getPropertiesAsURIs(this.resource, DataModel.Identified.wasDerivedFrom);
		}
		return wasDerivedFrom;
	}
	
	public void setWasDerivedFrom(List<URI> wasDerivedFrom) {
		this.wasDerivedFrom = wasDerivedFrom;
		RDFUtil.setProperty(resource, DataModel.Identified.wasDerivedFrom, this.wasDerivedFrom);
	}
	
	public List<URI> getWasGeneratedBy() {
		if (wasGeneratedBy==null)
		{
			wasGeneratedBy=RDFUtil.getPropertiesAsURIs(this.resource, DataModel.Identified.wasGeneratedBy);
		}
		return wasGeneratedBy;
	}
	
	public void setWasGeneratedBy(List<URI> wasGeneratedBy) {
		this.wasGeneratedBy = wasGeneratedBy;
		RDFUtil.setProperty(resource, DataModel.Identified.wasGeneratedBy, this.wasGeneratedBy);

	}
	
	public List<Measure> getMeasures()throws SBOLGraphException  {
		this.measures=addToList(this.measures, DataModel.Identified.measure, Measure.class, MeasureDataModel.Measure.uri);
		return measures;
	}
	
	

	public Measure createMeasure(URI uri, float value, URI unit) throws SBOLGraphException
	{
		Measure measure = new Measure(this.resource.getModel(), uri) {};
		measure.setValue(value);
		measure.setUnit(unit);		
		this.measures=addToList(this.measures, measure, DataModel.Identified.measure);
		return measure;	
	}
	
	public Measure createMeasure(String displayId, float value, URI unit) throws SBOLGraphException
	{
		return createMeasure(SBOLAPI.append(this.getUri(), displayId), value, unit);
	}
	
	/*public List<Metadata> getMetadata(URI property)throws SBOLGraphException  {
		this.metadataList=addToList(this.metadataList, property, Metadata.class, DataModel.Identified.uri);
		return metadataList;
	}*/
	
	public Metadata createMetadata(URI uri, URI dataType, URI property) throws SBOLGraphException
	{
		if (dataType==null)
		{
			throw new SBOLGraphException("Application specific types MUST have a datatype property specified. " + "Metadata URI:" + uri);
		}
		Metadata metadata=new Metadata(this.resource.getModel(), uri);
		metadata.addAnnotationType(dataType);
		this.addAnnotion(property, metadata);
		return metadata;
	}
	
	public Metadata createMetadata(String displayId, URI dataType, URI property) throws SBOLGraphException
	{
		return createMetadata(SBOLAPI.append(this.getUri(), displayId), dataType, property);
	}
	
	public URI getUri() {
		return uri;
	}
	
	abstract public URI getResourceType();
	
	protected <T extends Identified> Identified createIdentified(Resource res, Class<T> identified) throws SBOLGraphException
	{
		try
		{
			Constructor<T> constructor = identified.getDeclaredConstructor( new Class[] {Resource.class});
			if (!constructor.isAccessible())
			{
				constructor.setAccessible(true);
			}
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
				for (Resource res:resources)
				{
					Identified identified=createIdentified(res, identifiedClass);
					items.add((T)identified);
				}
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
				for (Resource res:resources)
				{
					Identified identified=createIdentified(res, identifiedClass);
					items.add((T)identified);
				}
			}
			
		}
		return items;
	}
	
	public void addAnnotion(URI property, String value)
	{
		RDFUtil.addProperty(resource, property, value);
	}
	
	public void addAnnotion(URI property, URI value)
	{
		RDFUtil.addProperty(resource, property, value);
	}
	
	public void addAnnotion(URI property, Identified value)
	{
		RDFUtil.addProperty(resource, property, value.getUri());
	}
	
	public void addAnnotion(URI property, TopLevel value)
	{
		RDFUtil.addProperty(resource, property, value.getUri());
	}
	
	public void addAnnotationType(URI typeURI)
	{
		RDFUtil.addType(resource, typeURI);
	}
	
	public List<Object> getAnnotion(URI propertyURI) throws SBOLGraphException
	{
		ArrayList<Object> values=null;
        Property property=resource.getModel().getProperty(propertyURI.toString());
        for (StmtIterator iterator=resource.listProperties(property);iterator.hasNext();)
        {
        	if (values==null)
        	{
        		values=new ArrayList<Object>();
        	}
        	Statement stmt=iterator.next();
        	RDFNode object=stmt.getObject();
        	
        	if (object.isResource()) 
        	{
        		if (object.asResource().listProperties().hasNext()==false)
        		{
        			values.add(object.asResource().getURI());
        		}
        		else
        		{
        			Resource metadataResource=object.asResource();
        			Identified metadata=null;
        			if (RDFUtil.hasType(metadataResource.getModel(), metadataResource, DataModel.TopLevel.uri))
        			{
        				metadata=new TopLevelMetadata(metadataResource);
        			}
        			else
        			{
        				metadata=new Metadata(metadataResource);
        			}
        			values.add(metadata);
        		}
        	}
        	else
        	{
        		values.add(object.asLiteral().getValue());
        	}
        }
        return values;
		
	}
	
		 	 
	 
	private boolean hasSBOLType (List<URI> types){
		boolean result=false;
		if (types!=null)
		{
			for (URI rdfType:types)
			{
				if (rdfType.toString().toLowerCase().startsWith(URINameSpace.SBOL.getUri().toString().toLowerCase()))
				{
					result=true;
					break;
				}
			}
		}
		return result;
	}
	
	private void inferDisplayId(URI uri) throws SBOLGraphException {
		displayId = getDisplayId();
		if (StringUtils.isEmpty(displayId)) {
			String result = null;
			String uriString = uri.toString();

			if (SBOLUtil.isURL(uriString))// .contains("://"))
			{
				String path=uri.getPath();
				int index = path.lastIndexOf("/");
				if (path.length() > index + 1) {
					result = path.substring(index + 1);
				} else {
					result = null;
				}
				if (result != null) {
					setDisplayId(result);
				}
				else
				{
					throw new SBOLGraphException("An SBOL URI MUST include the display id fragment. URI:" + uri);
				}
			}	
		}
	}
	
}

/*private void inferDisplayId(URI uri) {
	if ((uri.getPath() != null && uri.getPath().length() > 0)|| (uri.getFragment() != null && uri.getFragment().length() > 0)) {
		displayId = getDisplayId();
		if (StringUtils.isEmpty(displayId)) {
			String result = null;
			String uriString = uri.toString();

			if (SBOLUtil.isURL(uriString))// .contains("://"))
			{
				int index = uriString.lastIndexOf("#");
				int index2 = uriString.lastIndexOf("/");
				if (index2 > index) {
					index = index2;
				}
				if (uriString.length() > index + 1) {
					result = uriString.substring(index + 1);
				} else {
					result = null;
				}
			}
			if (result != null) {
				setDisplayId(result);
			}
		}
	}
}
// }
}
*/

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

