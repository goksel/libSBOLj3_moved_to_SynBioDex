package org.sbolstandard.core3.entity.test;

import java.io.IOException;
import java.net.URI;
import java.util.Arrays;

import org.apache.jena.rdf.model.Resource;
import org.sbolstandard.core3.api.SBOLAPI;
import org.sbolstandard.core3.entity.*;
import org.sbolstandard.core3.io.SBOLFormat;
import org.sbolstandard.core3.io.SBOLIO;
import org.sbolstandard.core3.test.TestUtil;
import org.sbolstandard.core3.util.Configuration;
import org.sbolstandard.core3.util.RDFUtil;
import org.sbolstandard.core3.util.SBOLGraphException;
import org.sbolstandard.core3.util.SBOLUtil;
import org.sbolstandard.core3.vocabulary.*;
import junit.framework.TestCase;

public class CombinatorialDerivationTest_12110 extends TestCase {
	
	public void testCombinatorialDerivation() throws SBOLGraphException, IOException, Exception
    {
		URI base=URI.create("https://synbiohub.org/public/igem/");
		SBOLDocument doc=new SBOLDocument(base);
		Collection col=doc.createCollection("promoterlib");
		
		
		Component pTetR=SBOLAPI.createDnaComponent(doc, "BBa_R0040", "pTetR", "TetR repressible promoter", Role.Promoter, "tccctatcagtgatagagattgacatccctatcagtgatagagatactgagcac");
	    Component pTetR2=SBOLAPI.createDnaComponent(doc, "BBa_R0040_2", "pTetR2", "TetR repressible promoter", Role.Promoter, "accctatcagtgatagagattgacatccctatcagtgatagagatactgagcac");
	    
	    TestUtil.validateDocument(doc, 0);
	    
	    CombinatorialDerivation cd=doc.createCombinatorialDerivation("cs1", pTetR);
		   
	    
	    Component start=SBOLAPI.createDnaComponent(doc, "BBa_R0040_start", "pTetR_start", "promoter_start", Role.EngineeredRegion, "tccctat");	
	    SubComponent sc_start=pTetR.createSubComponent(start);	    
	    
	    Component start2=SBOLAPI.createDnaComponent(doc, "BBa_R0040_start2", "pTetR_start", "promoter_start", Role.EngineeredRegion, "tccctat");	
	    SubComponent sc_start2=pTetR2.createSubComponent(start2);	    
	    	    
	    pTetR2.setWasDerivedFrom(Arrays.asList(cd.getUri()));
	    sc_start2.setWasDerivedFrom(Arrays.asList(sc_start.getUri()));
	   
	    cd.createVariableFeature(VariableFeatureCardinality.One, sc_start);
	    	    
	    Component end=SBOLAPI.createDnaComponent(doc, "BBa_R0040_end", "pTetR_end", "promoter_end", Role.EngineeredRegion, "tccctat");	
	    SubComponent sc_end=pTetR.createSubComponent(end);	    
	   
	    SubComponent sc_end2=pTetR2.createSubComponent(end);	    
	    
	    TestUtil.validateDocument(doc, 2, "sbol3-12105,sbol3-12110");		   
	    sc_end2.setWasDerivedFrom(Arrays.asList(sc_end.getUri()));
		   
	    TestUtil.validateDocument(doc, 0);
	    
	    SequenceFeature cut1=pTetR.createSequenceFeature(5, pTetR.getSequences().get(0));
	    SequenceFeature cut2=pTetR2.createSequenceFeature(5, pTetR2.getSequences().get(0));
	    cut2.setWasDerivedFrom(Arrays.asList(cut1.getUri()));
	    TestUtil.validateDocument(doc, 0);
	    
	    cut2.setWasDerivedFrom(Arrays.asList(cut1.getUri(), sc_end.getUri()));
	    TestUtil.validateDocument(doc, 1, "sbol3-12109");	 
    }
}
