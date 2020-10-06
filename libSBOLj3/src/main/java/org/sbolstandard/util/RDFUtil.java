package org.sbolstandard.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import org.apache.jena.datatypes.xsd.impl.XSDFloat;
import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.ResultSet;
import org.apache.jena.query.ResultSetFactory;
import org.apache.jena.query.Syntax;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.RDFWriter;
import org.apache.jena.rdf.model.ResIterator;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.ResourceFactory;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.rdf.model.StmtIterator;
import org.apache.jena.vocabulary.RDF;

public class RDFUtil {
	
    private static String RDFXMLABBREV = "RDF/XML-ABBREV";
    
	public static void setBaseURI(Model model, URI uri)
	{
		if (uri != null && uri.toString().length() > 0) {
			model.setNsPrefix("", uri.toString());
		}
	}
	
	public static Resource createResource(Model model, URI resourceUri, URI type) throws SBOLGraphException {
		String resourceUriString=resourceUri.toString();
		Resource resource=null;
		boolean exists=model.containsResource(ResourceFactory.createResource(resourceUriString));				
		if (!exists)
		{
			resource = model.createResource(resourceUriString);
			if (type!=null)
			{
				Resource typeResource=model.createResource(type.toString());	
				resource.addProperty(RDF.type, typeResource);	
			}
		}
		else
		{
			throw new SBOLGraphException(String.format("Resource with the URI already exists! URI:%s", resourceUriString));
		}
		return resource;
	}
	
	public static Resource createResource(Model model, URI resourceUri) {
		Resource resource = model.createResource(resourceUri.toString());
		return resource;
	}
	
	public static void addType(Resource resource, URI type)
	{
		if (type!=null && resource!=null)
		{
			Resource typeResource=resource.getModel().createResource(type.toString());	
			resource.addProperty(RDF.type, typeResource);	
		}
	}

	
	
	private static void removeIfExists(Resource resource, Property p)
	{
		StmtIterator stmt =resource.listProperties(p);
		if (stmt!=null && stmt.hasNext())
		{
			resource.getModel().remove(stmt);
		}
	}
	
	public static void setProperty(Resource resource, URI property, String value)
	{
		Property p=resource.getModel().createProperty(property.toString());
		removeIfExists(resource,p);
		resource.addProperty(p, value);	
	}
	
	public static void setProperty(Resource resource, URI property, float value)
	{
		Property p=resource.getModel().createProperty(property.toString());
		removeIfExists(resource,p);
		resource.addProperty(p, String.valueOf(value), XSDFloat.XSDfloat);	
	}
	

	
	public static void setProperty(Resource resource, URI property, URI value)
	{
		Property p=resource.getModel().createProperty(property.toString());
		removeIfExists(resource,p);
		Resource resourceValue = resource.getModel().createResource(value.toString());
		resource.addProperty(p, resourceValue);	
	}
	
	public static void setProperty(Resource resource, URI property, List<URI> values)
	{
		if (values!=null && values.size()>0)
		{
			Property p=resource.getModel().createProperty(property.toString());
			removeIfExists(resource,p);
		
			for (URI uri:values)
			{
				addProperty (resource, property, uri);
			}
		}
	}
	
	public static void setPropertyAsStrings(Resource resource, URI property, List<String> values)
	{
		if (values!=null && values.size()>0)
		{
			Property p=resource.getModel().createProperty(property.toString());
			removeIfExists(resource,p);
		
			for (String value:values)
			{
				addProperty (resource, property, value);
			}
		}
	}
	
	public static void addProperty(Resource resource, URI property, String value)
	{
		Property p=resource.getModel().createProperty(property.toString());
		resource.addProperty(p, value);	
	}
	
	public static void addProperty(Resource resource, URI property, URI value)
	{
		Property p=resource.getModel().createProperty(property.toString());
		Resource resourceValue = resource.getModel().createResource(value.toString());
		resource.addProperty(p, resourceValue);	
	}
	
	public static void addProperty(Resource resource, URI property, List<URI> values)
	{
		if (values!=null && values.size()>0)
		{
			for (URI uri:values)
			{
				addProperty (resource, property, uri);
			}
		}
	}
	
	  public static List<Resource> getResourcesOfType(Model rdfModel,URI type) 
	   {
	    	Resource typeResource=rdfModel.createResource(type.toString());
	        ArrayList<Resource> resources=null;
	        for (ResIterator iterator =  rdfModel.listResourcesWithProperty(RDF.type, typeResource);iterator.hasNext();)
	        {
	           if (resources==null)
	           {
	        	   resources=new ArrayList<Resource>();        
	           }
	           Resource resource=iterator.next();
	           resources.add(resource);
	        }
	        return resources;
	   }
	  
	  
	  public static List<Resource> getResourcesWithProperty(Resource resource,URI propertyURI) throws SBOLGraphException 
	   {
		  ArrayList<Resource> resources=null;
		  Property property=resource.getModel().getProperty(propertyURI.toString());   
		  StmtIterator it=resource.listProperties(property);
		  while (it.hasNext())
		  {
			  Statement stmt=it.nextStatement();
			  RDFNode object=stmt.getObject();
			  if (object.isResource())
			  {
				  if (resources==null)
				  {
					  resources=new ArrayList<Resource>();
				  }
				  resources.add(object.asResource());
			  }
			  else
			  {
				  String message=String.format("The property %s has literal value!", propertyURI.toString());
				  throw new SBOLGraphException(message);
			  }
		  }
	      return resources;
	   }
	  
	  public static List<Resource> getResourcesWithProperty(Resource resource,URI propertyURI, URI entityType) throws SBOLGraphException 
	   {
		  ArrayList<Resource> resources=null;       
		  Property property=resource.getModel().getProperty(propertyURI.toString());   
		  StmtIterator it=resource.listProperties(property);
		  while (it.hasNext())
		  {
			  Statement stmt=it.nextStatement();
			  RDFNode object=stmt.getObject();
			  if (object.isResource())
			  {
				  Resource objectResource=object.asResource();
				  if (hasType(resource.getModel(), objectResource, entityType))
				  {
					  if (resources==null)
					  {
						  resources=new ArrayList<Resource>();
					  }
					  resources.add(objectResource);
				  }
			  }
			  /*else
			  {
				  String message=String.format("The property %s has literal value! Resource: %s", propertyURI.toString(), resource.getURI().toString());
				  throw new SBOLGraphException(message);
			  }*/
		  }
	      return resources;
	   }
	  
	  private static Object getLiteralValue(Resource resource, URI propertyURI)
	  {
		    Object value=null;
			Property property=resource.getModel().getProperty(propertyURI.toString());   
			Statement stmt = resource.getProperty(property);
			if (stmt!=null && stmt.getObject()!=null){
				value=stmt.getObject().asLiteral().getValue();
			}
			return value;
	  }
	  
	  public static Long getPropertyAsLong(Resource resource, URI propertyURI) {
			long result=Long.MIN_VALUE;
			Object value=getLiteralValue(resource, propertyURI);
			if (value!=null)
			{
			 result=(long) value;
			}
		    return result;  	
		}
	  
	  public static String getPropertyAsString(Resource resource, URI propertyURI) {
			Property property=resource.getModel().getProperty(propertyURI.toString());   
			Statement stmt = resource.getProperty(property);
		    if (stmt!=null && stmt.getObject()!=null)
		    {
		    	return toLiteralString(stmt.getObject());
		    }
		    else
		    {
		    	return null;
		    }	
		}
	  
	  public static Resource getPropertyAsResource(Resource resource, URI propertyURI)
		{
			Property property=resource.getModel().getProperty(propertyURI.toString());
			Resource value=resource.getPropertyResourceValue(property);			
			return value;
		}
	  
	  public static URI getPropertyAsURI(Resource resource, URI propertyURI) {
		  URI result=null;	
		  Property property=resource.getModel().getProperty(propertyURI.toString());   
		  Statement stmt = resource.getProperty(property);
		  if (stmt!=null){
			  RDFNode object=stmt.getObject();
			  if (object.isResource()){
				  result= URI.create(object.asResource().getURI());
			  }
		  }
		  return result;  
		}
	  
	  

	    /**
	     * Gets the  property values for a given property and a resource.
	     * @param model Model to search the property for
	     * @param resource Resource to search the property values for 
	     * @param propertyURI the URI of the property
	     * @return List of String objects with the corresponding values
	     */
	    public static List<URI> getPropertiesAsURIs(Resource resource, URI propertyURI) 
	    {
	        ArrayList<URI> values=null;
	        Property property=resource.getModel().getProperty(propertyURI.toString());
	        for (StmtIterator iterator=resource.listProperties(property);iterator.hasNext();)
	        {
	        	Statement stmt=iterator.next();
	        	RDFNode object=stmt.getObject();
	        	if (object.isResource())
	        	{
	        		if (values==null)
	        		{
	        			values=new ArrayList<URI>();
	        		}
	        		values.add(URI.create(object.asResource().getURI()));
	        	}
	        }
	        return values;
	    }
	    
	    /**
	     * Gets the  property values for a given property and a resource.
	     * @param model Model to search the property for
	     * @param resource Resource to search the property values for 
	     * @param propertyURI the URI of the property
	     * @return List of String objects with the corresponding values
	     */
	    public static List<String> getPropertiesAsStrings(Resource resource, URI propertyURI) 
	    {
	        ArrayList<String> values=null;
	        Property property=resource.getModel().getProperty(propertyURI.toString());
	        for (StmtIterator iterator=resource.listProperties(property);iterator.hasNext();)
	        {
	        	Statement stmt=iterator.next();
	        	RDFNode object=stmt.getObject();
	        	if (!object.isResource())
	        	{
	        		if (values==null)
	        		{
	        			values=new ArrayList<String>();
	        		}
	        		values.add(object.asLiteral().getLexicalForm());
	        	}
	        }
	        return values;
	    }
	    
	    
	    public static String toLiteralString(RDFNode node)
		{
			if (node.isLiteral())
			{
				return node.asLiteral().getValue().toString();
			}
			else
			{
				return node.asResource().getURI();
			}
		}
	    
	    public static boolean hasType(Model rdfModel,Resource resource, URI type)
		{
			boolean result=false;
			Resource typeResource=rdfModel.createResource(type.toString());	
			result=resource.hasProperty (RDF.type, typeResource);
		    return result;
		}
	    
	   
	    private static void writeToStreamORG(Model model, OutputStream stream, String format, Resource[] topLevelResources, URI baseUri)
	    {
	    	RDFWriter writer = model.getWriter(format);
			writer.setProperty("tab", "3");
			if (topLevelResources != null && topLevelResources.length > 0) {
				writer.setProperty("prettyTypes", topLevelResources);
				writer.setProperty("relativeURIs","same-document,relative");
			}
			String base=null;
			if (baseUri!=null)
			{
				base=baseUri.toString();
				if (format.startsWith("RDF"))
				{
					writer.setProperty("xmlbase",base);
				}
			}
			writer.write(model, stream, base);
	    }
	    
	    private static void writeToStream(Model model, OutputStream stream, String format, Resource[] topLevelResources, URI baseUri)
	    {
	    	RDFWriter writer = model.getWriter(format);
			writer.setProperty("tab", "3");
			if (topLevelResources != null && topLevelResources.length > 0) {
				writer.setProperty("prettyTypes", topLevelResources);
				//writer.setProperty("relativeURIs","same-document,relative");
			}
			String base=null;
			if (baseUri!=null)
			{
				base=baseUri.toString();
				if (format.startsWith("RDF"))
				{
					writer.setProperty("xmlbase",base);
				}
			}
			writer.write(model, stream, base);
	    }
	    
	    public static String write(Model model, String format, Resource[] topLevelResources,URI baseUri) throws IOException {
			if (format == null || format.length() == 0) {
				format = "RDF/XML-ABBREV";
			}
			String rdfData = null;
			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			try {
				writeToStream(model, stream, format, topLevelResources, baseUri);
				rdfData = new String(stream.toString());
			} finally {
				if (stream != null) {
					stream.close();
					stream = null;
				}
			}
			return rdfData;
		}
	    

	    public static String getRdfString_Del(Model model, String format, Resource[] topLevelResources,URI baseUri)
				throws Exception {
			String rdfData = null;
			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			if (format == null || format.length() == 0) {
				format = "RDF/XML-ABBREV";
			}
			try {
				RDFWriter writer = model.getWriter(format);
				writer.setProperty("tab", "3");
				if (topLevelResources != null && topLevelResources.length > 0) {
					writer.setProperty("prettyTypes", topLevelResources);
					writer.setProperty("relativeURIs","same-document,relative");
				}
				String base=null;
				if (baseUri!=null)
				{
					base=baseUri.toString();
				}
				writer.write(model, stream, base);
				rdfData = new String(stream.toString());
			} finally {
				if (stream != null) {
					stream.close();
					stream = null;
				}
			}
			return rdfData;
		}
	    
	  
		

	    
	    public static void write(Model model, File file, String format, Resource[] topLevelResources,URI baseUri) throws IOException, FileNotFoundException {
			if (format == null || format.length() == 0) {
				format = "RDF/XML-ABBREV";
			}
			FileOutputStream stream = null;
			try {
				stream = new FileOutputStream(file);
				writeToStream(model, stream, format, topLevelResources, baseUri);
			} 
			finally {
				if (stream != null) {
					stream.close();
					stream = null;
				}
			}
		}
	    
	    public static ResultSet executeSPARQLSelectQuery(Model model, String query, Syntax syntax)
	    {
	    	System.out.println ("Executing the query 2" + query + "...");
	    	Query q = QueryFactory.create(query, syntax);
		    QueryExecution qe = QueryExecutionFactory.create(q, model);
		    ResultSet rsMemory=null;
		    try
		    {
		    	ResultSet rs = qe.execSelect();
		    	rsMemory=ResultSetFactory.copyResults(rs);
		    }
		    finally
		    {
		    	qe.close();
		    }
		    return rsMemory;		          
	    }
}
