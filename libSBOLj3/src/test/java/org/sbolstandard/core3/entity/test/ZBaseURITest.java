package org.sbolstandard.core3.entity.test;


import java.io.IOException;
import java.net.URI;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.riot.RDFFormat;
import org.sbolstandard.core3.api.SBOLAPI;
import org.sbolstandard.core3.entity.*;
import org.sbolstandard.core3.io.SBOLFormat;
import org.sbolstandard.core3.io.SBOLIO;
import org.sbolstandard.core3.test.TestUtil;
import org.sbolstandard.core3.util.Configuration;
import org.sbolstandard.core3.util.SBOLGraphException;
import org.sbolstandard.core3.vocabulary.ComponentType;
import org.sbolstandard.core3.vocabulary.DataModel;
import org.sbolstandard.core3.vocabulary.Role;

import junit.framework.TestCase;

public class ZBaseURITest extends TestCase {
	
	public void testBaseURI() throws SBOLGraphException, IOException, Exception
    {
		//URI base=URI.create("https://synbiohub.org/public/igem/");
		SBOLDocument doc=new SBOLDocument();
		
		try {
			Component popsReceiver=SBOLAPI.createDnaComponent(doc, "BBa_F2620", "BBa_F2620", "PoPS Receiver", Role.EngineeredGene, null); 
		}
		catch (Exception ex){
        	if (!(ex instanceof SBOLGraphException)){
        		throw ex;
        	}
		}
		
		Component popsReceiver=SBOLAPI.createComponent(doc, URI.create("https://synbiohub.org/public/igem/BBa_F2620"), ComponentType.DNA.getUri(), "BBa_F2620", "PoPS Receiver", Role.Promoter);
		popsReceiver.setNamespace(URI.create("https://synbiohub.org/public/igem"));
		
		doc.setBaseURI(URI.create("https://synbiohub.org/public/igemnewbase/"));
		Component popsReceiver2=SBOLAPI.createDnaComponent(doc, "BBa_F2620_2", "BBa_F2620_2", "PoPS Receiver 2", Role.EngineeredGene, null); 
			
		String output=SBOLIO.write(doc,RDFFormat.TURTLE);
		System.out.println(output);
		
    }

}
