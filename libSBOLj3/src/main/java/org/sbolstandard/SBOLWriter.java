package org.sbolstandard;

import java.io.File;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URI;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.riot.Lang;
import org.apache.jena.riot.RDFFormat;
import org.apache.jena.vocabulary.RDFS;
import org.sbolstandard.util.RDFHandler;
import org.sbolstandard.util.RDFUtil;
import org.sbolstandard.vocabulary.DataModel;

public class SBOLWriter{

	public static String write(SBOLDocument doc, String format) throws Exception
	{
		String output=RDFUtil.write(doc.getModel(), format, getTopLevelResources(), doc.getBaseURI());				
		return output;
	}
	
	public static String writedel(SBOLDocument doc, String format) throws Exception
	{
		Model model = ModelFactory.createDefaultModel();
		model.setNsPrefix("sbol", "http://sbols.org/v3#");
		ArrayList<Resource> resources=new ArrayList<Resource>();
		resources.add(RDFHandler.createResource(model, DataModel.Component.uri));
		resources.add(RDFHandler.createResource(model,  DataModel.Sequence.uri));
		Resource[] topLevelResources=resources.toArray(new Resource[resources.size()]);
		String output=RDFUtil.write(doc.getModel(), format, topLevelResources, doc.getBaseURI());				
		return output;
	}
	
	private static Resource[] getTopLevelResources()
	{
		Model model = ModelFactory.createDefaultModel();
		ArrayList<Resource> resources=new ArrayList<Resource>();
		resources.add(RDFHandler.createResource(model, DataModel.Component.uri));
		resources.add(RDFHandler.createResource(model,  DataModel.Sequence.uri));
		Resource[] topLevelResources=resources.toArray(new Resource[resources.size()]);
		return topLevelResources;
	}
	public static void write(SBOLDocument doc, File file, String format) throws Exception
	{
		RDFUtil.write(doc.getModel(), file, format, getTopLevelResources(), doc.getBaseURI());				
	}
	
	public static SBOLDocument read(String sbolData, String format) throws Exception
	{
		InputStream is=IOUtils.toInputStream(sbolData, Charset.defaultCharset());
		Model model = ModelFactory.createDefaultModel();
		model.read(is,null,format);
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
