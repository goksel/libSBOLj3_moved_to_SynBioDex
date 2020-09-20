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
import org.sbolstandard.entity.provenance.Plan;
import org.sbolstandard.io.SBOLIO;
import org.sbolstandard.util.SBOLGraphException;
import org.sbolstandard.util.URINameSpace;
import org.sbolstandard.vocabulary.ComponentType;
import org.sbolstandard.vocabulary.ModelFramework;
import org.sbolstandard.vocabulary.ModelLanguage;
import org.sbolstandard.vocabulary.Role;

import junit.framework.TestCase;

public class PlanTest extends TestCase {
	
	public void test() throws SBOLGraphException, IOException
    {
		String baseUri="https://sbolstandard.org/examples/";
        SBOLDocument doc=new SBOLDocument(URI.create(baseUri));
        
        Plan plan=doc.createPlan(SBOLAPI.append(baseUri, "CodonOptimisationProtocol"));
        plan.setName("Codon Optimisation Protocol");
        plan.setDescription("Optimisation protocol to improve the translation of mRNAs.");
         
        TestUtil.serialise(doc, "provenance_entity/plan", "plan");
      
        System.out.println(SBOLIO.write(doc, "Turtle"));
        
        TestUtil.assertReadWrite(doc);
    }

}
