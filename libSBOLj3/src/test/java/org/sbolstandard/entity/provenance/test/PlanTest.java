package org.sbolstandard.entity.provenance.test;

import java.io.IOException;
import java.net.URI;

import org.sbolstandard.TestUtil;
import org.sbolstandard.entity.SBOLDocument;
import org.sbolstandard.entity.provenance.Plan;
import org.sbolstandard.io.SBOLFormat;
import org.sbolstandard.io.SBOLIO;
import org.sbolstandard.util.SBOLGraphException;

import junit.framework.TestCase;

public class PlanTest extends TestCase {
	
	public void test() throws SBOLGraphException, IOException
    {
		String baseUri="https://sbolstandard.org/examples/";
        SBOLDocument doc=new SBOLDocument(URI.create(baseUri));
        
        Plan plan=doc.createPlan("CodonOptimisationProtocol");
        plan.setName("Codon Optimisation Protocol");
        plan.setDescription("Optimisation protocol to improve the translation of mRNAs.");
        
        System.out.println(SBOLIO.write(doc, SBOLFormat.JSONLD));
        System.out.println("--------------------");
        
        System.out.println(SBOLIO.write(doc, SBOLFormat.TURTLE));
        
        
        TestUtil.serialise(doc, "provenance_entity/plan", "plan");
      
        
        TestUtil.assertReadWrite(doc);
    }

}
