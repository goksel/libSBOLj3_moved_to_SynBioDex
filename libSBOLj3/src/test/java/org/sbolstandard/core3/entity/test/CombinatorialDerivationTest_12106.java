package org.sbolstandard.core3.entity.test;

import java.io.IOException;
import java.net.URI;
import java.util.Arrays;

import org.sbolstandard.core3.api.SBOLAPI;
import org.sbolstandard.core3.entity.*;
import org.sbolstandard.core3.test.TestUtil;
import org.sbolstandard.core3.util.SBOLGraphException;
import org.sbolstandard.core3.vocabulary.*;
import junit.framework.TestCase;

public class CombinatorialDerivationTest_12106 extends TestCase {
	
	public void testCombinatorialDerivation() throws SBOLGraphException, IOException, Exception
    {
		URI base=URI.create("https://synbiohub.org/public/igem/");
		SBOLDocument doc=new SBOLDocument(base);
		Collection col=doc.createCollection("promoterlib");
		
		
		Component pTetR=SBOLAPI.createDnaComponent(doc, "BBa_R0040", "pTetR", "TetR repressible promoter", Role.Promoter, "tccctatcagtgatagagattgacatccctatcagtgatagagatactgagcac");
	    Component pTetR2=SBOLAPI.createDnaComponent(doc, "BBa_R0040_2", "pTetR2", "TetR repressible promoter", Role.Promoter, "accctatcagtgatagagattgacatccctatcagtgatagagatactgagcac");
	    Component pTetR3=SBOLAPI.createDnaComponent(doc, "BBa_R0040_3", "pTetR3", "TetR repressible promoter", Role.Promoter, "tccctatcagtgatagagattgacatccctatcagtgatagagatactgagcac");
	    
	    
	    //pTetR.setWasDerivedFrom(Arrays.asList(genericpromoter.getUri()));
	    //col.addMember(pTetR);
	    col.addMembers(Arrays.asList(pTetR2, pTetR3));
	    TestUtil.validateDocument(doc, 0);
	    
	    CombinatorialDerivation cd=doc.createCombinatorialDerivation("cs1", pTetR);
		   
	    col.setWasDerivedFrom(Arrays.asList(cd.getUri()));
	    
	    Component start=SBOLAPI.createDnaComponent(doc, "BBa_R0040_start", "pTetR_start", "promoter_start", Role.EngineeredRegion, "tccctat");	
	    SubComponent sc_start=pTetR.createSubComponent(start);	    
	    
	    
	    TestUtil.validateDocument(doc, 2,"sbol3-12106");
		
	    col.addMember(pTetR);
	    TestUtil.validateDocument(doc, 2,"sbol3-12106");
		
	    
    }

}
