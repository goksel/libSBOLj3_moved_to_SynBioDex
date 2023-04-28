package org.sbolstandard.core3.validation.test;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;

import org.apache.commons.compress.utils.FileNameUtils;
import org.junit.Test;
import org.sbolstandard.core3.entity.SBOLDocument;
import org.sbolstandard.core3.io.SBOLFormat;
import org.sbolstandard.core3.io.SBOLIO;
import org.sbolstandard.core3.test.TestUtil;
import org.sbolstandard.core3.util.SBOLGraphException;
import org.sbolstandard.core3.validation.SBOLComparator;

public class RoundTripTest {

	private static final String pythonOutputBase="pysbol3-rt2"; 
	
	//@Test
	public void validate() throws IOException, SBOLGraphException {
		
		String message=validateFolder(pythonOutputBase);
		
		if (!message.isEmpty())
		{
			System.out.println(message);
			if (message.contains("Could'not"))
			{
				fail ("Validation found errors!");		
			}
		}

	}
	
	public static String validateFolder(String folder) throws IOException, SBOLGraphException
	{
		StringBuilder output=new StringBuilder();
		roundTripRecursive(output, new File(folder));
		String result=output.toString();
		return result;
	}
	
	private static StringBuilder roundTripRecursive(StringBuilder output, File folder) throws IOException, SBOLGraphException
	{
		File[] files=folder.listFiles();
		for (int i=0;i<files.length;i++)
		{
			File file=files[i];
			if (file.isDirectory())
			{
				output=roundTripRecursive(output, file);
			}
			else
			{				
				output=validateRoundTripFile(output, file);
			}
		}
		return output;
	}
		
		
		private static StringBuilder validateRoundTripFile(StringBuilder output, File file) throws IOException, SBOLGraphException
		{ 
			if (file.getName().startsWith(".") || file.getName().endsWith(".rj")  || file.getName().endsWith(".xml") )
			{
				return output;
			}
			
			/*if (!file.getName().startsWith("toggle"))
			{
				return output;
			}*/
			
			//System.out.println("Read/Write Check:" + file.getPath());
			output.append("\n-------------------------------\n");
			output.append("Read/Write Check:" + file.getPath());
			String javaOutputFile=file.getPath().replace(pythonOutputBase, TestUtil.baseOutput);
			//String javaOutputFile=file.getPath().replace(pythonOutputBase, "java-" + pythonOutputBase);
			File javaFile=new File(javaOutputFile);
			SBOLDocument docPython=SBOLIO.read(file,getFormat(file));
			SBOLDocument docJava=SBOLIO.read(javaFile,getFormat(file));
			
			String message=SBOLComparator.assertEqual(docPython, docJava);
			
			if (!message.isEmpty())	
			{
				//System.out.println("Files are different:");
				//System.out.println(message);
				output.append(System.lineSeparator());
				output.append(message);
			}
			//saveFromJava(docPython, file);
			return output;
			
			
			
		}
		
		/*private void saveFromJava(SBOLDocument doc, File file) throws IOException, SBOLGraphException
		{
			String newFileName="java-" + file.getPath();
			File newFile=new File(newFileName);
			File newDirectory=newFile.getParentFile();
			if (!newDirectory.exists())
			{
				newDirectory.mkdirs();
			}
			SBOLIO.write(doc, newFile, getFormat(file));			
		}*/
		
		private static SBOLFormat getFormat(File file) throws SBOLGraphException
		{
			String extension=FileNameUtils.getExtension(file.getName());
			if (extension.equals("ttl"))
			{
				return SBOLFormat.TURTLE;
			}
			else if (extension.equals("rdf"))
			{
				return SBOLFormat.RDFXML;
			}
			else if (extension.equals("jsonld"))
			{
				return SBOLFormat.JSONLD;
			}
			else if (extension.equals("nt"))
			{
				return SBOLFormat.NTRIPLES;
			}
			else if (extension.equals("jsonld_expanded"))
			{
				return SBOLFormat.JSONLD_EXPAND;
			}
			
			else
			{
				throw new SBOLGraphException("Unsupported file extension. File: " + file.getName());
			}	
		}
}
