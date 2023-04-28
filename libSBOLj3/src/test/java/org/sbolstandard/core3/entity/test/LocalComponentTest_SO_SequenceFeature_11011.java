package org.sbolstandard.core3.entity.test;

import java.io.IOException;
import java.net.URI;
import java.util.Arrays;

import org.sbolstandard.core3.api.SBOLAPI;
import org.sbolstandard.core3.entity.*;
import org.sbolstandard.core3.test.TestUtil;
import org.sbolstandard.core3.util.SBOLGraphException;
import org.sbolstandard.core3.vocabulary.ComponentType;
import org.sbolstandard.core3.vocabulary.Role;
import junit.framework.TestCase;

public class LocalComponentTest_SO_SequenceFeature_11011 extends TestCase {
	
	public void testComponent() throws SBOLGraphException, IOException, Exception
    {
		URI base=URI.create("https://synbiohub.org/public/igem/");
		SBOLDocument doc=new SBOLDocument(base);
		
		Component popsReceiver=SBOLAPI.createDnaComponent(doc, "BBa_F2620", "BBa_F2620", "PoPS Receiver", Role.EngineeredGene, null); 
		TestUtil.validateDocument(doc,0);
		
		Component TetR_protein=SBOLAPI.createProteinComponent(doc,popsReceiver, "TetR_protein", "TetR", "TetR protein", Role.CDS, "NNNNNNNNNNN");
		TestUtil.validateDocument(doc,1);
		
		Component LuxR_protein=SBOLAPI.createProteinComponent(doc,popsReceiver, "BBa_C0062_protein", "LuxR",  "LuxR protein", Role.CDS, "NNNNNNNNNNN");
		TestUtil.validateDocument(doc,2);
		
		LocalSubComponent lsComponent = TetR_protein.createLocalSubComponent(Arrays.asList(ComponentType.DNA.getUri()));
		lsComponent.setRoles(Arrays.asList(Role.CDS));
		TestUtil.validateDocument(doc,2);
		
		LocalSubComponent lsComponent2 = TetR_protein.createLocalSubComponent(Arrays.asList(ComponentType.Protein.getUri()));
		lsComponent2.setRoles(Arrays.asList(Role.CDS));
		TestUtil.validateDocument(doc,3);
		
		LocalSubComponent lsComponent3 = TetR_protein.createLocalSubComponent(Arrays.asList(ComponentType.Protein.getUri()));
		lsComponent3.setRoles(Arrays.asList(Role.Promoter));
		TestUtil.validateDocument(doc,4);
		
		
		
		 
    }
}
