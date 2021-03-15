package org.sbolstandard.entity.provenance.test;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.Arrays;

import org.sbolstandard.TestUtil;
import org.sbolstandard.api.SBOLAPI;
import org.sbolstandard.entity.Component;
import org.sbolstandard.entity.Model;
import org.sbolstandard.entity.SBOLDocument;
import org.sbolstandard.entity.provenance.Agent;
import org.sbolstandard.io.SBOLFormat;
import org.sbolstandard.io.SBOLIO;
import org.sbolstandard.util.SBOLGraphException;
import org.sbolstandard.util.URINameSpace;
import org.sbolstandard.vocabulary.ComponentType;
import org.sbolstandard.vocabulary.ModelFramework;
import org.sbolstandard.vocabulary.ModelLanguage;
import org.sbolstandard.vocabulary.Role;

import junit.framework.TestCase;

public class AgentTest extends TestCase {
	
	public void test() throws SBOLGraphException, IOException
    {
		String baseUri="https://sbolstandard.org/examples/";
        SBOLDocument doc=new SBOLDocument(URI.create(baseUri));
        
        Component toggleSwitch=SBOLAPI.createComponent(doc, SBOLAPI.append(baseUri, "toggle_switch"), ComponentType.FunctionalEntity.getUrl(), "Toggle Switch", "Toggle Switch genetic circuit", null);
        
        Agent agent=doc.createAgent(SBOLAPI.append(baseUri, "CodonOptimiserSoftware"));
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
        System.out.println(SBOLIO.write(SBOLIO.read(new File("output/provenance_entity/agent/agent.jsonld")), SBOLFormat.JSONLD_EXPAND));
        System.out.println("-------------------------TURTLE-----------------------------");
        System.out.println(SBOLIO.write(SBOLIO.read(new File("output/provenance_entity/agent/agent.ttl")), SBOLFormat.TURTLE));
        System.out.println("-------------------------NTRIPLES-----------------------------");
        System.out.println(SBOLIO.write(SBOLIO.read(new File("output/provenance_entity/agent/agent.nt")), SBOLFormat.NTRIPLES));
        
        //When the wring RDF type is given, the file is still read
        System.out.println(SBOLIO.write(SBOLIO.read(new File("output/provenance_entity/agent/agent.jsonld"), SBOLFormat.RDFXML), SBOLFormat.JSONLD));
        
        
        System.out.println(SBOLIO.write(doc, SBOLFormat.NTRIPLES));
        
        
        
    }

}
