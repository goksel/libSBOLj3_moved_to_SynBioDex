package org.sbolstandard.entity.provenance.test;

import java.io.IOException;
import java.net.URI;
import java.util.Arrays;

import org.sbolstandard.TestUtil;
import org.sbolstandard.api.SBOLAPI;
import org.sbolstandard.entity.Component;
import org.sbolstandard.entity.Model;
import org.sbolstandard.entity.SBOLDocument;
import org.sbolstandard.entity.provenance.Agent;
import org.sbolstandard.io.SBOLWriter;
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
        
        Component toggleSwitch=SBOLAPI.createComponent(doc, SBOLAPI.append(baseUri, "toggle_switch"), ComponentType.FunctionalEntity.getUrl(), "Toggle Switch", "toggle_switch", "Toggle Switch genetic circuit", null);
        
        Agent agent=doc.createAgent(SBOLAPI.append(baseUri, "CodonOptimiserSoftware"));
        agent.setName("Codon Optimiser Software");
        agent.setDescription("Used to optimise bacterial DNA sequences.");
        
        TestUtil.serialise(doc, "provenance_entity/agent", "agent");
      
        System.out.println(SBOLWriter.write(doc, "Turtle"));
        
        //TestUtil.assertReadWrite(doc);
    }

}
