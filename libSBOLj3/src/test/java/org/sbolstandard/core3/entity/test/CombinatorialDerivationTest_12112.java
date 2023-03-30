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

public class CombinatorialDerivationTest_12112 extends TestCase {
	
	public void testCombinatorialDerivation() throws SBOLGraphException, IOException, Exception
    {
		URI base=URI.create("https://synbiohub.org/public/igem/");
		SBOLDocument doc=new SBOLDocument(base);
		Collection col=doc.createCollection("promoterlib");
		
		
		Component pTetR=SBOLAPI.createDnaComponent(doc, "BBa_R0040", "pTetR", "TetR repressible promoter", Role.Promoter, "tccctatcagtgatagagattgacatccctatcagtgatagagatactgagcac");
	    Component pTetR2=SBOLAPI.createDnaComponent(doc, "BBa_R0040_2", "pTetR2", "TetR repressible promoter", Role.Promoter, "accctatcagtgatagagattgacatccctatcagtgatagagatactgagcac");
	    
	    TestUtil.validateDocument(doc, 0);
	    	    
	    Component start=SBOLAPI.createDnaComponent(doc, "BBa_R0040_start", "pTetR_start", "promoter_start", Role.EngineeredRegion, "tccctat");	
	    SubComponent sc_start=pTetR.createSubComponent(start);	    
	    
	    CombinatorialDerivation cd=doc.createCombinatorialDerivation("cd1", pTetR);
	    
	    
	    Component start2=SBOLAPI.createDnaComponent(doc, "BBa_R0040_start2", "pTetR_start2", "promoter_start2", Role.EngineeredRegion, "accctat");	
	    Component start3=SBOLAPI.createDnaComponent(doc, "BBa_R0040_start3", "pTetR_start3", "promoter_start3", Role.EngineeredRegion, "cccctat");	
	    
	    SubComponent sc_start2=pTetR2.createSubComponent(start2);	    
	    
	    
	    pTetR2.setWasDerivedFrom(Arrays.asList(cd.getUri()));
	   
	    VariableFeature varFeature=cd.createVariableFeature(VariableFeatureCardinality.One, sc_start);
	    sc_start2.setWasDerivedFrom(Arrays.asList(sc_start.getUri()));
	    System.out.println(sc_start2.getWasDerivedFrom());
	    TestUtil.validateDocument(doc, 1, "sbol3-12112");
	    
	    varFeature.setVariants(Arrays.asList(start2, start3));
	    TestUtil.validateDocument(doc, 0);
	    	    
	    varFeature.setVariants(null);
	    TestUtil.validateDocument(doc, 1, "sbol3-12112");
	    
	    Attachment attachment=doc.createAttachment("testattachment",URI.create("http://sbolstandard.org/attachment1"));	    
	    col.addMember(attachment);
	    Collection innerCol=doc.createCollection("innerCol");
	    innerCol.addMembers(Arrays.asList(start2, start3));
	    col.addMember(innerCol);
	    varFeature.setVariantCollections(Arrays.asList(col));
	    TestUtil.validateDocument(doc, 1, "sbol3-12203");
	    
	    varFeature.setVariantCollections(null);
	    TestUtil.validateDocument(doc, 1, "sbol3-12112");
	    
	    
	    Component genericstart=SBOLAPI.createDnaComponent(doc, "genericstart", "genericstart", "genericstart", Role.EngineeredRegion, "accctat");		    
	    CombinatorialDerivation cd2=doc.createCombinatorialDerivation("cd2", genericstart);
	    SequenceFeature sf1=genericstart.createSequenceFeature(genericstart.getSequences().get(0));
	    SequenceFeature sf2=start2.createSequenceFeature(start2.getSequences().get(0));
	    sf2.setWasDerivedFrom(Arrays.asList(sf1.getUri()));
	    
	    start2.setWasDerivedFrom(Arrays.asList(cd2.getUri()));	    
	    varFeature.setVariantDerivations(Arrays.asList(cd2));
	    
	    TestUtil.validateDocument(doc, 0);
	    
		   
	    
	    
	    
	    
	    
	    
	    
	    
    }
}
