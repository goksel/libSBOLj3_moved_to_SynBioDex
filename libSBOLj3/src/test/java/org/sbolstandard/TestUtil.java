package org.sbolstandard;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.sbolstandard.entity.SBOLDocument;
import org.sbolstandard.io.SBOLWriter;

public class TestUtil {

	public static void serialise(SBOLDocument doc, String directory, String file) throws FileNotFoundException, IOException
	{
		String baseOutput="output";
		File outputDir=new File(baseOutput +  "/" + directory);
        if (!outputDir.exists())
        {
        	boolean result=outputDir.mkdirs();
        }
        
        SBOLWriter.write(doc, new File(String.format("%s/%s/%s.turtle.sbol", baseOutput,directory, file)), "Turtle");
        SBOLWriter.write(doc, new File(String.format("%s/%s/%s.rdfxml.sbol", baseOutput,directory, file)), "RDF/XML-ABBREV");
        SBOLWriter.write(doc, new File(String.format("%s/%s/%s.jsonld.sbol", baseOutput,directory, file)), "JSON-LD");
        SBOLWriter.write(doc, new File(String.format("%s/%s/%s.rdfjson.sbol", baseOutput,directory, file)), "rdfjson");
        SBOLWriter.write(doc, new File(String.format("%s/%s/%s.ntriples.sbol", baseOutput,directory, file)), "N-TRIPLES");
	}
}
