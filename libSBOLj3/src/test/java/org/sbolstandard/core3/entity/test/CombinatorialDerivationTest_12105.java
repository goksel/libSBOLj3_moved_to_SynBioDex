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

public class CombinatorialDerivationTest_12105 extends TestCase {
	
	public void testCombinatorialDerivation() throws SBOLGraphException, IOException, Exception
    {
		URI base=URI.create("https://synbiohub.org/public/igem/");
		SBOLDocument doc=new SBOLDocument(base);
		
		
		Component pTetR=SBOLAPI.createDnaComponent(doc, "BBa_R0040", "pTetR", "TetR repressible promoter", Role.Promoter, "tccctatcagtgatagagattgacatccctatcagtgatagagatactgagcac");
		Component start=SBOLAPI.createDnaComponent(doc, "BBa_R0040_start", "pTetR_start", "promoter_start", Role.EngineeredRegion, "tccctat");	
	    SubComponent sc_start=pTetR.createSubComponent(start);	    
	    
	    Component pTetR2=SBOLAPI.createDnaComponent(doc, "BBa_R0040_2", "pTetR2", "TetR repressible promoter", Role.Promoter, "accctatcagtgatagagattgacatccctatcagtgatagagatactgagcac");
	    //Component start2=SBOLAPI.createDnaComponent(doc, "BBa_R0040_2_start", "pTetR2_start", "promoter_start", Role.EngineeredRegion, "accctat");	
	    SubComponent sc_start2=pTetR2.createSubComponent(start);	    
	    
	    
	    CombinatorialDerivation cd=doc.createCombinatorialDerivation("cs1", pTetR);
	    TestUtil.validateDocument(doc, 0);
		   
	    pTetR2.setWasDerivedFrom(Arrays.asList(cd.getUri()));
	    TestUtil.validateDocument(doc, 2, "sbol3-12105,sbol3-12110");
	    
	    sc_start2.setWasDerivedFrom(Arrays.asList(pTetR.getUri()));
	    TestUtil.validateDocument(doc, 2, "sbol3-12105,sbol3-12110");
	    
	    sc_start2.setWasDerivedFrom(Arrays.asList(sc_start.getUri()));
	    TestUtil.validateDocument(doc, 0);
	    
	    Component genericpromoter=SBOLAPI.createDnaComponent(doc, "BBa_R0040gen", "pTetRgen", "TetR repressible promoter", Role.Promoter, "tccctatcagtgatagagattgacatccctatcagtgatagagatactgagcac");
	    pTetR.setWasDerivedFrom(Arrays.asList(genericpromoter.getUri()));
	    TestUtil.validateDocument(doc, 0);
	    
    }

}
