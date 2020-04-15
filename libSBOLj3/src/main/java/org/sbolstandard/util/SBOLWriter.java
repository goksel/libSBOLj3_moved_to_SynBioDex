package org.sbolstandard.util;

import java.net.URI;
import java.util.ArrayList;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Resource;
import org.sbolstandard.SBOLDocument;
import org.sbolstandard.sequence.Sequence;

public class SBOLWriter{

	public static String toRDFXML(SBOLDocument doc, String format) throws Exception
	{
		Model model = ModelFactory.createDefaultModel();
		model.setNsPrefix("sbol", "http://sbols.org/v3#");
		ArrayList<Resource> resources=new ArrayList<Resource>();
		resources.add(RDFHandler.createResource(model, URI.create("http://sbols.org/v3#Component")));
		resources.add(RDFHandler.createResource(model, URI.create("http://sbols.org/v3#Sequence")));
		Resource[] topLevelResources=resources.toArray(new Resource[resources.size()]);
    	
    	
		if (doc.getSequences()!=null)
		{
			for (Sequence sequence:doc.getSequences())
			{
				sequence.toResource(model);
			}
		}
		String output=RDFHandler.getRdfString(model, format, topLevelResources, "http://testbase.org");
				
		return output;
		
	}

}
