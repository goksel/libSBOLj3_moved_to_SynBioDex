package org.sbolstandard;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.sbolstandard.entity.Identified;
import org.sbolstandard.entity.SBOLDocument;
import org.sbolstandard.entity.TopLevel;
import org.sbolstandard.entity.provenance.Agent;
import org.sbolstandard.io.SBOLWriter;
import org.sbolstandard.util.SBOLGraphException;

import static org.junit.Assert.*;

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
	
	public static void assertReadWrite(SBOLDocument doc) throws IOException, SBOLGraphException
	{
		String output=SBOLWriter.write(doc, "Turtle");
	    SBOLDocument doc2=SBOLWriter.read(output, "Turtle"); 
	    assertEqual(doc, doc2);
	       
	}
	public static void assertEqual(SBOLDocument doc1, SBOLDocument doc2) throws SBOLGraphException
	{
		if (doc1.getAgents()!=null)
		{
			for (Agent agent:doc1.getAgents())
			{
				Agent agent2=(Agent) doc2.getIdentified(agent.getUri(), Agent.class);
				assertEqual(agent, agent2);
			}
		}
	}
	
	private static void assertEqual(TopLevel topLevel1, TopLevel topLevel2)
	{
		assertEqual((Identified)topLevel1, (Identified)topLevel2);	
	}
	
	private static void assertEqual(Identified identified1, Identified identified2)
	{
		assertEquals("Name:" + identified1.getName() + " - ", identified1.getName(),identified2.getName());
		assertEquals("Description:" + identified1.getDescription() + " - ", identified1.getDescription(),identified2.getDescription());
		assertEquals("DisplayId:" + identified1.getDisplayId() + " - ", identified1.getName(),identified2.getDisplayId());
		assertEquals("URI:" + identified1.getUri() + " - ", identified1.getUri(),identified2.getUri());
		assertEquals("WasDerivedFrom:" + identified1.getWasDerivedFrom() + " - ", identified1.getWasDerivedFrom(),identified2.getWasDerivedFrom());
		
		assert(identified1.getDescription().equals(identified2.getDescription()));
	}
}
