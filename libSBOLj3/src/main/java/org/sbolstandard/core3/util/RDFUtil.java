package org.sbolstandard.core3.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.jena.datatypes.xsd.impl.XSDFloat;
import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.ResultSet;
import org.apache.jena.query.ResultSetFactory;
import org.apache.jena.query.Syntax;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.ResIterator;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.ResourceFactory;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.rdf.model.StmtIterator;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.riot.RDFFormat;
import org.apache.jena.riot.RDFParser;
import org.apache.jena.riot.RDFParserBuilder;
import org.apache.jena.riot.RDFWriter;
import org.apache.jena.riot.RDFWriterBuilder;
import org.apache.jena.riot.SysRIOT;
import org.apache.jena.sparql.util.Context;
import org.apache.jena.vocabulary.RDF;

//IO: https://jena.apache.org/documentation/io/rdf-input.html
//https://jena.apache.org/tutorials/rdf_api.html#ch-Writing-RDF
	
//Some examples: https://github.com/apache/jena/tree/main/jena-arq/src-examples/arq/examples/riot/
//https://franz.com/agraph/support/documentation/current/java-tutorial/jena-tutorial.html

//Serialisation performance: https://users.jena.apache.narkive.com/DkpVI0cr/json-ld-serialization-performances
//Shacl Tools: https://github.com/griddigit/CimPal/blob/master/src/main/java/core/ShaclTools.java
	
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
		StmtIterator stmtIt =resource.listProperties(p);
		if (stmtIt!=null && stmtIt.hasNext())
		{
			Statement stmt=stmtIt.next();
			resource.getModel().remove(stmt);
		}
	}
	
	public static void setProperty(Resource resource, URI property, String value)
	{
		Property p=resource.getModel().createProperty(property.toString());
		removeIfExists(resource,p);
		if (value!=null)
		{
			resource.addProperty(p, value);	
		}
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
		if (value!=null)
		{
			Resource resourceValue = resource.getModel().createResource(value.toString());
			resource.addProperty(p, resourceValue);	
		}
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
	  
	 /* private static Object getLiteralValue(Resource resource, URI propertyURI)
	  {
		    Object value=null;
			Property property=resource.getModel().getProperty(propertyURI.toString());   
			Statement stmt = resource.getProperty(property);
			if (stmt!=null && stmt.getObject()!=null){
				value=stmt.getObject().asLiteral().getValue();
			}
			return value;
	  }*/
	  
	  /*public static Long getPropertyAsLong(Resource resource, URI propertyURI) {
			long result=Long.MIN_VALUE;
			Object value=getLiteralValue(resource, propertyURI);
			if (value!=null)
			{
			 result=(long) value;
			}
		    return result;  	
		}*/
	  
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
	    
	    public static boolean hasTypePrefix(Resource resource, URI prefix)
		{
			boolean result=false;
			List<String> types=getPropertiesAsStrings(resource, URI.create(RDF.type.getURI()));
			for (String typeURI: types)
			{
				if (typeURI.toLowerCase().startsWith(prefix.toString().toLowerCase()))
				{
					result=true;
					break;
				}
			}
			return result;
		}
	    
	   
	   /* private static void writeToStreamORG(Model model, OutputStream stream, String format, Resource[] topLevelResources, URI baseUri)
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
	    }*/
	    
	    /*private static void writeToStreamRDFXML(Model model, OutputStream stream, String format, Resource[] topLevelResources, URI baseUri)
	    {
	    	org.apache.jena.rdf.model.RDFWriter writer = model.getWriter(format);
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
		*/
	    
	    private static void configureRDFWriter_RDFXML(RDFWriterBuilder writerBuilder, Model model, RDFFormat format, Resource[] topLevelResources, String baseUri)
	    {
	    	Map<String, Object> properties = new HashMap<>();
			if (baseUri!=null)
			{
				properties.put("xmlbase", baseUri);
				writerBuilder.base(baseUri.toString());
			}
			properties.put("tab", 3);
			properties.put("prettyTypes", topLevelResources);
			Context ctx = new Context();
			ctx.set(SysRIOT.sysRdfWriterProperties, properties);
			writerBuilder.context(ctx);	
	    }
	    
		public static void write(Model model, OutputStream stream, RDFFormat format, Resource[] topLevelResources
				) {
			if (format == null) {
				format = RDFFormat.RDFXML_ABBREV;
			}
			boolean baseNsRemoved = false;
			String baseUri = model.getNsPrefixURI("");
			RDFWriterBuilder writerBuilder = RDFWriter.create().source(model).format(format);

			if (format.equals(RDFFormat.RDFXML_ABBREV)){ // RDF/XML
				configureRDFWriter_RDFXML(writerBuilder, model, format, topLevelResources, baseUri);
			} 
			else if (format.getLang().getLabel().toString().toLowerCase().startsWith("json")) { // JSON-LD...
				if (baseUri!=null)
				{
					model.removeNsPrefix("");
					baseNsRemoved = true;
					//writerBuilder.base(baseUri.toString());
				}
			} 
			else { // Turtle, NTriples
				if (baseUri != null) {
					writerBuilder.base(baseUri.toString());
				}
			}

			RDFWriter writer = writerBuilder.build();
			writer.output(stream);

			if (baseNsRemoved) {
				model.setNsPrefix("", baseUri);
			}
		}

		 public static String write(Model model, RDFFormat format, Resource[] topLevelResources) throws IOException {
				String rdfData = null;
				ByteArrayOutputStream stream = new ByteArrayOutputStream();
				try {
					write(model, stream, format, topLevelResources);
					rdfData = new String(stream.toString());
				} finally {
					if (stream != null) {
						stream.close();
						stream = null;
					}
				}
				return rdfData;
			}
		    

	    public static void write(Model model, File file, RDFFormat format, Resource[] topLevelResources) throws IOException, FileNotFoundException {
			FileOutputStream stream = new FileOutputStream(file);
			try {
				write(model, stream, format, topLevelResources);
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
	    	//System.out.println ("Executing the query " + query + "...");
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
	    
	    /**
	     * 
	     * @param path The file path or remote web URL
	     * @param format
	     * @return
	     * @throws FileNotFoundException
	     */
	    
	    public static Model read(File file) throws FileNotFoundException
		{
			Model model = RDFDataMgr.loadModel(file.getPath()) ;
			return model;			
		}
	    
	    /**
	     * 
	     * @param file The file path 
	     * @param format
	     * @return
	     * @throws FileNotFoundException
	     */
	    public static Model read(File file, RDFFormat format) throws FileNotFoundException
		{
			Model model = RDFDataMgr.loadModel(file.getPath(), format.getLang());
			return model;			
		}
	    
	    /**
	     * 
	     * @param file The file path 
	     * @param format
	     * @return
	     * @throws FileNotFoundException
	     */
	    public static Model read(URI uri, RDFFormat format) throws FileNotFoundException
		{
			Model model = RDFDataMgr.loadModel(uri.toString(), format.getLang());
			return model;			
		}
	   
	    public static Model read(URI uri) throws FileNotFoundException
		{
			Model model = RDFDataMgr.loadModel(uri.toString());
			return model;			
		}
	   
	    
	    
	    public static Model read(InputStream stream, RDFFormat format) throws FileNotFoundException
		{
	    	Model model = ModelFactory.createDefaultModel();
	        RDFParserBuilder rdfBuilder= RDFParser.create().source(stream);
	        if (format!=null)
	        {
	        	rdfBuilder.lang(format.getLang());
	        }
	        RDFParser parser=rdfBuilder.build();
	        parser.parse(model);    
			return model;			
		}
	    
	    public static Model read(String input, RDFFormat format) throws FileNotFoundException
		{
	    	Model model = ModelFactory.createDefaultModel();
	        RDFParserBuilder rdfBuilder= RDFParser.create().fromString(input);
	        if (format!=null)
	        {
	        	rdfBuilder.lang(format.getLang());
	        }
	        RDFParser parser=rdfBuilder.build();
	        parser.parse(model);    
			return model;			
		}
	    
	    public static List<URI> filterItems(Model model, List<URI> resources, URI property, URI value)
		{
			return filterItems(model, resources, property, value.toString());
		}
	    
	    
	    public static List<URI> filterItems(Model model, List<URI> resources, URI property, String value)
		 {
			ArrayList<URI> filtered=null; 
	    	if (resources!=null){
	    		Property rdfProperty=model.getProperty(property.toString());   
				for (URI uri :resources){
					Resource resource=model.getResource(uri.toString());
					StmtIterator it=resource.listProperties(rdfProperty);
					if (it!=null){
						while (it.hasNext()){
							Statement stmt=it.next();
							String rdfValue=toLiteralString(stmt.getObject());
							if (value.equals(rdfValue)){
								if (filtered==null){
									filtered=new ArrayList<URI>();
								}
								filtered.add(uri);
								break;
							}
						}
					}
	    		}
			 }
	    	 return filtered; 
		 }
	    
	   
}

/*
 
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
		
		
	SERIALISATION:	
		if (format.toLowerCase().equals("json-ld"))
				{
					//RDFDataMgr.write(stream, model, RDFFormat.JSONLD_FLAT);
					//RDFDataMgr.write(stream, model, RDFFormat.JSONLD_COMPACT_PRETTY);
					//RDFDataMgr.write(stream, model, RDFFormat.JSONLD);
					writeToStream(model, stream, format, topLevelResources, baseUri);
					
					//model.write(stream, format, baseUri.toString());
				}	
				else {
					writeToStream(model, stream, format, topLevelResources, baseUri);
				}
	
	    
	    RDFDataMgr.read(graph, is, Lang.RDFXML);    
				Map<String, Object> properties = new HashMap<>();
				properties.put("xmlbase", "http://example#");
				Context cxt = new Context();
				cxt.set(SysRIOT.sysRdfWriterProperties, properties);
				RDFWriter.create().source(graph).format(RDFFormat.RDFXML_PLAIN).base("http://example#").context(cxt).output(os);
				
						//String atContextAsJson = "{\"@vocab\":\"" + baseUri.toString() +  "\"}";
						//JsonLDWriteContext ctx = new JsonLDWriteContext();
					    //ctx.setJsonLDContext(atContextAsJson);
					    
						//JsonLDWriteContext
						Context cxt = new Context();
						Map<String, Object> properties = new HashMap<>();
						properties.put("base", "http://hede.hedo");
						properties.put("vocab", "http://hede.hedod");
						
						cxt.set(SysRIOT.sysRdfWriterProperties, properties);
					    writerBuilder.context(cxt);
					    
					    JsonLDWriteContext ctx = new JsonLDWriteContext();
					    JsonLdOptions opts = new JsonLdOptions();
					    ctx.setOptions(opts);
					    // default is true
					    opts.setBase("http://hede.hedo");
					    writerBuilder.context(ctx);
 */
