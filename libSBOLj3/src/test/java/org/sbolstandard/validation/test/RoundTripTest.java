package org.sbolstandard.validation.test;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.util.Comparator;

import org.apache.commons.compress.utils.FileNameUtils;
import org.junit.Test;
import org.sbolstandard.TestUtil;
import org.sbolstandard.api.SBOLAPI;
import org.sbolstandard.entity.SBOLDocument;
import org.sbolstandard.io.SBOLIO;
import org.sbolstandard.util.SBOLGraphException;
import org.sbolstandard.validation.SBOLComparator;
import org.sbolstandard.validation.SBOLValidator;

public class RoundTripTest {

	private static final String pythonOutputBase="pysbol3-rt1"; 
	@Test
	public void validate() throws IOException, SBOLGraphException {
		
		String message=validateFolder(pythonOutputBase);
		
		if (!message.isEmpty())
		{
			System.out.println(message);
		}
		else
		{
			fail ("Validation could not find any errors!");
		}
	}
	
	public String validateFolder(String folder) throws IOException, SBOLGraphException
	{
		StringBuilder output=new StringBuilder();
		roundTripRecursive(output, new File(folder));
		String result=output.toString();
		return result;
	}
	
	private void roundTripRecursive(StringBuilder output, File folder) throws IOException, SBOLGraphException
	{
		File[] files=folder.listFiles();
		for (int i=0;i<files.length;i++)
		{
			File file=files[i];
			if (file.isDirectory())
			{
				roundTripRecursive(output, file);
			}
			else
			{				
				validateRoundTripFile(output, file);
			}
		}
	}
		
		
		public void validateRoundTripFile(StringBuilder output, File file) throws IOException, SBOLGraphException
		{ 
			System.out.println("Read/Write Check:" + file.getPath());
			String javaOutputFile=file.getPath().replace(pythonOutputBase, TestUtil.baseOutput);
			File javaFile=new File(javaOutputFile);
			SBOLDocument docPython=SBOLIO.read(file);
			SBOLDocument docJava=SBOLIO.read(javaFile);
			
			String message=SBOLComparator.assertEqual(docPython, docJava);
			if (!message.isEmpty())	
			{
				System.out.println("Files are different:");
				System.out.println(message);
			}
			
			
			
		}
}
