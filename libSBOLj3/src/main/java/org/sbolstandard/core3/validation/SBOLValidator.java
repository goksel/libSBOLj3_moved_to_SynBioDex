package org.sbolstandard.core3.validation;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.Charset;

import org.apache.commons.compress.utils.FileNameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.query.Syntax;
import org.apache.jena.rdf.model.RDFNode;
import org.sbolstandard.core3.api.SBOLAPI;
import org.sbolstandard.core3.entity.SBOLDocument;
import org.sbolstandard.core3.io.SBOLIO;
import org.sbolstandard.core3.util.RDFUtil;

public class SBOLValidator {

	public static String validateFolder(String folder, String extension) throws IOException
	{
		StringBuilder output=new StringBuilder();
		validateFolderRecursive(output, new File(folder), extension);
		String result=output.toString();
		return result;
	}
	
	public static String validateFolder(String folder) throws IOException
	{
		return validateFolder(folder,null);
	}

	private static void validateFolderRecursive(StringBuilder output, File folder, String extension) throws IOException
	{
		File[] files=folder.listFiles();
		for (int i=0;i<files.length;i++)
		{
			File file=files[i];
			if (file.isDirectory())
			{
				validateFolderRecursive(output, file, extension);
			}
			else
			{
				boolean validate=true;
				if (extension!=null)
				{
					String ext=FileNameUtils.getExtension(file.getName());
					if (!ext.equalsIgnoreCase(extension))
					{
						validate=false;
					}
				}
				if (validate)
				{
					validateFile(output, file);
				}
			}
		}
	}
	
	
	public static void validateFile(StringBuilder output, File file) throws IOException
	{ 
		SBOLDocument doc=SBOLIO.read(file);
		String folder=SBOLAPI.class.getClassLoader().getResource("validation").getFile();
		String[] files=new File(folder).list();
		
		//while (validationFiles.hasMoreElements())
		for (int i=0;i<files.length;i++)
		{
			//String resourceFile=validationFiles.nextElement().getFile();
			String resourceFile="validation/" + files[i];
			
			String sparqlQuery=loadQuery(resourceFile);
			String queryName= new File(resourceFile).getName();
			validateFileWithQuery(output, doc, sparqlQuery, file, queryName);
		}
		
		//String sparqlDisplayId=loadQuery("validation/displayid.sparql");
		//validateFileWithQuery(output, doc, sparqlDisplayId, file, "displayid");
	}
	
	private static void validateFileWithQuery(StringBuilder output, SBOLDocument doc, String query, File file, String queryName)
	{
		ResultSet rs=RDFUtil.executeSPARQLSelectQuery(doc.getRDFModel(), query, Syntax.syntaxSPARQL_11);
		while (rs.hasNext())
		{
			QuerySolution qs=rs.next();
			RDFNode identified=qs.get(rs.getResultVars().get(0));
			RDFNode value=qs.get(rs.getResultVars().get(1));
			
			String identifiedLiteral=RDFUtil.toLiteralString(identified);
			String valueLiteral=RDFUtil.toLiteralString(value);
			
			String message=String.format("Validation failed for %s - %s. Identified: %s, value: '%s'", file.getName(), queryName, identifiedLiteral, valueLiteral);
			
			output.append(message);
			output.append(System.lineSeparator());
		}
	
	}
	//   FILTER (!regex(?displayId, "^[a-zA-Z_]+[a-zA-Z0-9_]*$")) .
	   
	//   FILTER (!regex(?displayId, "^[a-zA-Z0-9_]*$")) .
	/*public static String toLiteralString(RDFNode node)
	{
		if (node.isLiteral())
		{
			return node.asLiteral().getValue().toString();
		}
		else
		{
			return node.asResource().getURI();
		}
	}	*/
	
	private static String loadQuery(String queryFile) throws IOException
	{
		try
		{
			File file = new File(SBOLAPI.class.getClassLoader().getResource(queryFile).getFile());
			return IOUtils.toString(new FileInputStream(file), Charset.defaultCharset());
		}
		catch (Exception e)
		{
			throw new IOException("Could not load the sparql file " + queryFile,e);
		}
	}
	
	

}
