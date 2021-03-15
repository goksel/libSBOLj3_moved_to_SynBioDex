package org.sbolstandard.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Iterator;

import org.apache.commons.io.IOUtils;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.riot.RDFFormat;
import org.sbolstandard.entity.SBOLDocument;
import org.sbolstandard.util.RDFUtil;

public class SBOLIO{

	public static String write(SBOLDocument doc, SBOLFormat format) throws IOException
	{
		return write(doc, format.getFormat());
	}
	
	public static String write(SBOLDocument doc, RDFFormat format) throws IOException
	{
		String output=RDFUtil.write(doc.getRDFModel(), format, getTopLevelResources(doc));				
		return output;
	}
	
	public static void write(SBOLDocument doc, File file, SBOLFormat format) throws FileNotFoundException, IOException
	{
		write(doc, file, format.getFormat());			
	}
	
	public static void write(SBOLDocument doc, File file, RDFFormat format) throws FileNotFoundException, IOException
	{
		RDFUtil.write(doc.getRDFModel(), file, format, getTopLevelResources(doc));				
	}

	
	public static void write(SBOLDocument doc, OutputStream stream, SBOLFormat format) throws FileNotFoundException, IOException
	{
		write(doc, stream, format.getFormat());			
	}
	
	public static void write(SBOLDocument doc, OutputStream stream, RDFFormat format) throws FileNotFoundException, IOException
	{
		RDFUtil.write(doc.getRDFModel(), stream, format, getTopLevelResources(doc));				
	}

	/*private static Resource[] getTopLevelResources()
	{
		Model model = ModelFactory.createDefaultModel();
		ArrayList<Resource> resources=new ArrayList<Resource>();
		resources.add(RDFHandler.createResource(model, DataModel.Component.uri));
		resources.add(RDFHandler.createResource(model,  DataModel.Sequence.uri));
		resources.add(RDFHandler.createResource(model,  DataModel.Model.uri));
		resources.add(RDFHandler.createResource(model,  DataModel.Implementation.uri));
		resources.add(RDFHandler.createResource(model,  DataModel.ExperimentalData.uri));
		resources.add(RDFHandler.createResource(model,  DataModel.Attachment.uri));
		resources.add(RDFHandler.createResource(model,  DataModel.Collection.uri));
		resources.add(RDFHandler.createResource(model,  DataModel.Namespace.uri));
		resources.add(RDFHandler.createResource(model,  DataModel.CombinatorialDerivation.uri));
		resources.add(RDFHandler.createResource(model,  DataModel.Namespace.uri));
		Resource[] topLevelResources=resources.toArray(new Resource[resources.size()]);
		return topLevelResources;
	}*/
	
	private static Resource[] getTopLevelResources(SBOLDocument doc)
	{
		Model model = ModelFactory.createDefaultModel();
		ArrayList<Resource> resources=new ArrayList<Resource>();
		Iterator<URI> it=doc.getTopLevelResourceTypes().iterator();
		
		while (it.hasNext())
		{
			resources.add(RDFUtil.createResource(model, it.next()));	
		}
		Resource[] topLevelResources=resources.toArray(new Resource[resources.size()]);
		return topLevelResources;
	}
	
	public static SBOLDocument readOld(String sbolData, String format)
	{
		InputStream is=IOUtils.toInputStream(sbolData, Charset.defaultCharset());
		Model model = ModelFactory.createDefaultModel();
		model.read(is,null,format);
		SBOLDocument doc=new SBOLDocument(model);
		return doc;
	}
	
	public static SBOLDocument readOld(File file, String format) throws FileNotFoundException
	{
		InputStream is=new FileInputStream(file);
		Model model = ModelFactory.createDefaultModel();
		model.read(is,null,format);
		SBOLDocument doc=new SBOLDocument(model);
		return doc;
	}
	
	public static SBOLDocument read(File file, SBOLFormat format) throws FileNotFoundException
	{
		return read(file, format.getFormat());
	}
	
	public static SBOLDocument read(File file, RDFFormat format) throws FileNotFoundException
	{
		Model model = RDFUtil.read(file,format) ;
		SBOLDocument doc=new SBOLDocument(model);
		return doc;
	}
	public static SBOLDocument read(File file) throws FileNotFoundException
	{
		Model model = RDFUtil.read(file) ;
		SBOLDocument doc=new SBOLDocument(model);
		return doc;
	}
	
	public static SBOLDocument read(URI uri, SBOLFormat format) throws FileNotFoundException
	{
		return read(uri, format.getFormat());
	}
	
	public static SBOLDocument read(URI uri, RDFFormat format) throws FileNotFoundException
	{
		Model model = RDFUtil.read(uri,format) ;
		SBOLDocument doc=new SBOLDocument(model);
		return doc;
	}
	
	public static SBOLDocument read(URI uri) throws FileNotFoundException
	{
		Model model = RDFUtil.read(uri) ;
		SBOLDocument doc=new SBOLDocument(model);
		return doc;
	}
	
	public static SBOLDocument read(String input, SBOLFormat format) throws FileNotFoundException
	{
		return read(input, format.getFormat());
	}
	
	public static SBOLDocument read(String input, RDFFormat format) throws FileNotFoundException
	{
		Model model = RDFUtil.read(input, format) ;
		SBOLDocument doc=new SBOLDocument(model);
		return doc;
	}
	
	public static SBOLDocument read(InputStream stream, SBOLFormat format) throws FileNotFoundException
	{
		return read(stream, format.getFormat());
	}
	
	public static SBOLDocument read(InputStream stream, RDFFormat format) throws FileNotFoundException
	{
		Model model = RDFUtil.read(stream, format) ;
		SBOLDocument doc=new SBOLDocument(model);
		return doc;
	}
	
	
	/*private static Object createChildEntity(Object parent, String childEntityType, String uri) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException
	{
		String methodName="create" + childEntityType;
		Method myObjMethod = parent.getClass().getMethod(methodName, URI.class);
		Object o=myObjMethod.invoke(parent, URI.create(uri)); //prints "Method1 impl."
		return o;
		
	}
	
	private static SBOLDocument populateSBOLDocument(Model model) throws Exception
	{
		SBOLDocument doc=new SBOLDocument(model);
		String type="Sequence";
		List<Resource> resources=RDFUtil.getResourcesOfType(model, URI.create("http://sbols.org/v3#Sequence"));
		for (Resource r:resources)
		{
			Object entity=createChildEntity(doc,type,r.getURI());
			String str="";
			str="";
			
		}
		return null;
	}
	
	public static SBOLDocument read(String sbolData, String format) throws Exception
	{
		InputStream is=IOUtils.toInputStream(sbolData, Charset.defaultCharset());
		Model model = ModelFactory.createDefaultModel();
		model.read(is,null,format);
		
		return populateSBOLDocument(model);
		
	}*/
	
	
	
	

}
