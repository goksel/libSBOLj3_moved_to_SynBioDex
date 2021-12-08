package org.sbolstandard.core3.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Collections;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.jena.rdf.model.Model;
import org.sbolstandard.core3.entity.Identified;
import org.sbolstandard.core3.entity.SBOLDocument;

public class SBOLUtil {

	 public static boolean isURL(String s){
	        try{
	            URL url = new URL(s);
	            url.toURI();
	            return true;
	        }catch(MalformedURLException e){
	            return false;
	        } catch (URISyntaxException e) {
	            return false;
	        }
	    }
	 
	 public static void sort(File inputFile, File outputFile, Charset encoding) throws IOException
	 {
		 	FileInputStream inputStream=new FileInputStream(inputFile);
		 	FileOutputStream outputStream=new FileOutputStream(outputFile);
			sort(inputStream, encoding, outputStream);
			
		 	if (outputStream!=null)
			{
		 		outputStream.close();
			}
			
	 }
	 
	 public static String sort(String input, Charset encoding) throws IOException
	 {
			ByteArrayInputStream inputStream=new ByteArrayInputStream(input.getBytes());
			ByteArrayOutputStream outputStream=new ByteArrayOutputStream();
			
			sort(inputStream, encoding, outputStream);
			String output = new String(outputStream.toString());
			
			if (outputStream!=null)
		 	{
		 		outputStream.close();
		 	}
		 	return output;
		 	
	 }
	 
	 public static void sort(InputStream inputStream, Charset encoding, OutputStream outputStream) throws IOException
	 {
			List<String> lineList=IOUtils.readLines(inputStream,encoding);
		 	Collections.sort(lineList);
			IOUtils.writeLines(lineList, null, outputStream, encoding);
			if (inputStream!=null)
			{
				inputStream.close();
			}
	 }
	 
	 public static List<URI> filterItems2(SBOLDocument document, List<URI> identifieds, URI property, URI value)
	 {
		return RDFUtil.filterItems(document.getRDFModel(), identifieds, property, value.toString());
	 }
	 
	 /*public static List<URI> filterItems(Identified identified, List<URI> identifieds, URI property, URI value)
	 {
		return RDFUtil.filterItems(identified.resource.getRDFModel(), identifieds, property, value.toString());
	 }*/
	
}