package org.sbolstandard.core3.entity.test;

import java.io.IOException;
import java.net.URI;
import org.sbolstandard.core3.api.SBOLAPI;
import org.sbolstandard.core3.entity.Component;
import org.sbolstandard.core3.entity.SBOLDocument;
import org.sbolstandard.core3.test.TestUtil;
import org.sbolstandard.core3.util.SBOLGraphException;
import org.sbolstandard.core3.vocabulary.Role;
import junit.framework.TestCase;

public class ComponentTest_SO_SequenceFeature_10612 extends TestCase {
	
	//COMPONENT_TYPE_ONLY_DNA_OR_RNA_INCLUDE_SO_FEATURE_ROLE
	public void testComponent() throws SBOLGraphException, IOException, Exception
    {
		URI base=URI.create("https://synbiohub.org/public/igem/");
		SBOLDocument doc=new SBOLDocument(base);
		
		Component popsReceiver=SBOLAPI.createDnaComponent(doc, "BBa_F2620", "BBa_F2620", "PoPS Receiver", Role.EngineeredGene, null); 
		TestUtil.validateDocument(doc,0);
		
		Component TetR_protein=SBOLAPI.createProteinComponent(doc,popsReceiver, "TetR_protein", "TetR", "TetR protein", Role.CDS, "NNNNNNNNNNN");
		TestUtil.validateIdentified(TetR_protein, doc,1);
		
		Component LuxR_protein=SBOLAPI.createProteinComponent(doc,popsReceiver, "BBa_C0062_protein", "LuxR",  "LuxR protein", Role.CDS, "NNNNNNNNNNN");
		TestUtil.validateIdentified(LuxR_protein, doc,1, 2);
		
		
		
		
			
		
		
	    
               
        
    }
}
