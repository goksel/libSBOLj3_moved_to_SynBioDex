package org.sbolstandard.io;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import org.apache.commons.io.IOUtils;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Resource;
import org.sbolstandard.entity.SBOLDocument;
import org.sbolstandard.util.RDFHandler;
import org.sbolstandard.util.RDFUtil;
import org.sbolstandard.vocabulary.DataModel;

public class SBOLWriter{

	public static String write(SBOLDocument doc, String format) throws IOException
	{
		String output=RDFUtil.write(doc.getModel(), format, getTopLevelResources(), doc.getBaseURI());				
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
	public static void write(SBOLDocument doc, File file, String format) throws FileNotFoundException, IOException
	{
		RDFUtil.write(doc.getModel(), file, format, getTopLevelResources(), doc.getBaseURI());				
	}
	
	public static SBOLDocument read(String sbolData, String format)
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
