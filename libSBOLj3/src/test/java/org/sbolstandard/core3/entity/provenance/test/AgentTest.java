package org.sbolstandard.core3.entity.provenance.test;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.charset.Charset;

import org.apache.jena.riot.RDFFormat;
import org.sbolstandard.core3.api.SBOLAPI;
import org.sbolstandard.core3.entity.SBOLDocument;
import org.sbolstandard.core3.entity.provenance.Agent;
import org.sbolstandard.core3.io.SBOLFormat;
import org.sbolstandard.core3.io.SBOLIO;
import org.sbolstandard.core3.test.TestUtil;
import org.sbolstandard.core3.util.SBOLGraphException;
import org.sbolstandard.core3.util.SBOLUtil;
import org.sbolstandard.core3.vocabulary.ComponentType;

import junit.framework.TestCase;

public class AgentTest extends TestCase {
	
	public void test() throws SBOLGraphException, IOException
    {
		String baseUri="https://sbolstandard.org/examples/";
        SBOLDocument doc=new SBOLDocument(URI.create(baseUri));
        
        SBOLAPI.createComponent(doc, "toggle_switch", ComponentType.FunctionalEntity.getUrl(), "Toggle Switch", "Toggle Switch genetic circuit", null);
        
        Agent agent=doc.createAgent("CodonOptimiserSoftware");
        agent.setName("Codon Optimiser Software");
        agent.setDescription("Used to optimise bacterial DNA sequences.");
        
        TestUtil.serialise(doc, "provenance_entity/agent", "agent");
      
        
        System.out.println(SBOLIO.write(doc, SBOLFormat.TURTLE));
        
        TestUtil.assertReadWrite(doc);
        
        SBOLDocument doc2=SBOLIO.read(new File("output/provenance_entity/agent/agent.rdf"));
        System.out.println("-------------------------RDF/XML:-----------------------------");
        System.out.println(SBOLIO.write(doc2, SBOLFormat.RDFXML));
        System.out.println("-------------------------JSON-LD-----------------------------");
        System.out.println(SBOLIO.write(SBOLIO.read(new File("output/provenance_entity/agent/agent.jsonld")), SBOLFormat.JSONLD));
        System.out.println("-------------------------JSON-LD-EXPANDED-----------------------------");
        System.out.println(SBOLIO.write(SBOLIO.read(new File("output/provenance_entity/agent/agent.jsonld_expanded"), SBOLFormat.JSONLD_EXPAND), SBOLFormat.JSONLD_EXPAND));
        System.out.println("-------------------------TURTLE-----------------------------");
        System.out.println(SBOLIO.write(SBOLIO.read(new File("output/provenance_entity/agent/agent.ttl")), SBOLFormat.TURTLE));
        System.out.println("-------------------------NTRIPLES-----------------------------");
        System.out.println(SBOLIO.write(SBOLIO.read(new File("output/provenance_entity/agent/agent.nt")), SBOLFormat.NTRIPLES));
        System.out.println("-------------------------RDF/JSON-----------------------------");
        System.out.println(SBOLIO.write(SBOLIO.read(new File("output/provenance_entity/agent/agent.rj")), RDFFormat.RDFJSON));
                
        
        //When the wrong RDF type is given, the file is still read
        System.out.println("-------------------------WRONG RDF FORMAT INTO PROVIDED  - STILL READ-----------------------------"); 
        System.out.println(SBOLIO.write(SBOLIO.read(new File("output/provenance_entity/agent/agent.jsonld"), SBOLFormat.RDFXML), SBOLFormat.JSONLD));
        
        System.out.println("-------------------------ORDERED NTRIPLES-----------------------------");        
        String output=SBOLIO.write(doc, SBOLFormat.NTRIPLES);
        String sortedOutput=SBOLUtil.sort(output, Charset.defaultCharset());
        System.out.println(sortedOutput);
        
        
        /*
        System.out.println("-------------------------READING FROM STRING WITH NO RDF FORMAT INFO -----------------------------"); 
        String output=SBOLIO.write(doc, SBOLFormat.TURTLE);
        InputStream targetStream = new ByteArrayInputStream(output.getBytes());
        SBOLDocument docReadFromStringWithNoFormatInfo=SBOLIO.read(targetStream, RDFFormat.JSONLD);
         
        docReadFromStringWithNoFormatInfo=SBOLIO.read(output);
        System.out.println(SBOLIO.write(docReadFromStringWithNoFormatInfo, SBOLFormat.TURTLE));
         */
    }

}
