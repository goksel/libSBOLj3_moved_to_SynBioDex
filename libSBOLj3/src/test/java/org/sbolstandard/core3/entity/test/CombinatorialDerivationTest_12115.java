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

public class CombinatorialDerivationTest_12115 extends TestCase {
	
	public void testCombinatorialDerivation() throws SBOLGraphException, IOException, Exception
    {
		URI base=URI.create("https://synbiohub.org/public/igem/");
		SBOLDocument doc=new SBOLDocument(base);
		Collection col=doc.createCollection("promoterlib");
		
		
		Component pTetR=SBOLAPI.createDnaComponent(doc, "BBa_R0040", "pTetR", "TetR repressible promoter", Role.Promoter, "tccctatcagtgatagagattgacatccctatcagtgatagagatactgagcac");
	    Component pTetR2=SBOLAPI.createDnaComponent(doc, "BBa_R0040_2", "pTetR2", "TetR repressible promoter", Role.Promoter, "accctatcagtgatagagattgacatccctatcagtgatagagatactgagcac");
	    
	    	    
	    Component start=SBOLAPI.createDnaComponent(doc, "BBa_R0040_start", "pTetR_start", "promoter_start", Role.EngineeredRegion, "tccctat");	
	    SubComponent sc_start=pTetR.createSubComponent(start);	    
	    SequenceFeature pTetRSequenceFeature=pTetR.createSequenceFeature(pTetR.getSequences().get(0));
	    
	    Component start2=SBOLAPI.createDnaComponent(doc, "BBa_R0040_start2", "pTetR_start2", "promoter_start2", Role.EngineeredRegion, "accctat");	
	    SubComponent sc_start2=pTetR2.createSubComponent(start2);	    
	    SequenceFeature pTetRSequenceFeature2=pTetR2.createSequenceFeature(pTetR2.getSequences().get(0));
	    
	    CombinatorialDerivation cd=doc.createCombinatorialDerivation("cd1", pTetR);	   	    
	    pTetR2.setWasDerivedFrom(Arrays.asList(cd.getUri()));
	   
	    VariableFeature varFeature=cd.createVariableFeature(VariableFeatureCardinality.One, sc_start);
	    varFeature.setVariants(Arrays.asList(start2));
	    sc_start2.setWasDerivedFrom(Arrays.asList(sc_start.getUri()));
	    pTetRSequenceFeature2.setWasDerivedFrom(Arrays.asList(pTetRSequenceFeature.getUri()));
	    		  	    
	    TestUtil.validateDocument(doc, 0);
	    start.addType(URI.create("http:sbolstandard.org/testtype"));	    
	    TestUtil.validateDocument(doc, 1, "sbol3-12115");
	    start2.addType(URI.create("http:sbolstandard.org/testtype"));	    
	    TestUtil.validateDocument(doc, 0);
		    
	    LocalSubComponent lsc1= pTetR.createLocalSubComponent(Arrays.asList(URI.create("http:sbolstandard.org/localsubcomponenttype1")));
	    LocalSubComponent lsc2= pTetR2.createLocalSubComponent(Arrays.asList(URI.create("http:sbolstandard.org/localsubcomponenttype2")));
	    lsc2.addWasDerivedFrom(lsc1);	    
	    TestUtil.validateDocument(doc, 1, "sbol3-12115");
	    lsc2.addType(URI.create("http:sbolstandard.org/localsubcomponenttype1"));
	    TestUtil.validateDocument(doc, 0);
		
	    ExternallyDefined ed1= pTetR.createExternallyDefined(Arrays.asList(URI.create("http:sbolstandard.org/externallyDefinedtType1")), URI.create("http://sbolstandard.org/externalentity1"));
	    ExternallyDefined ed2= pTetR2.createExternallyDefined(Arrays.asList(URI.create("http:sbolstandard.org/externallyDefinedtType2")), URI.create("http://sbolstandard.org/externalentity2"));
	    ed2.addWasDerivedFrom(ed1);	    
	    TestUtil.validateDocument(doc, 1, "sbol3-12115");
	    ed2.addType(URI.create("http:sbolstandard.org/externallyDefinedtType1"));
	    TestUtil.validateDocument(doc, 0);
	    
	    
	    Component end=SBOLAPI.createDnaComponent(doc, "BBa_R0040_end", "pTetR_end", "promoter_end", Role.EngineeredRegion, "gagcac");	
	    LocalSubComponent lsc3=end.createLocalSubComponent(Arrays.asList(URI.create("http:sbolstandard.org/localsubcomponenttype3")));
	    SubComponent endSubComponent=SBOLAPI.addSubComponent(pTetR, end);		
	    ComponentReference cr1= pTetR.createComponentReference(lsc3, endSubComponent);
	    		
	    Component end2=SBOLAPI.createDnaComponent(doc, "BBa_R0040_end2", "pTetR_end2", "promoter_end2", Role.EngineeredRegion, "gagcac");	
	    LocalSubComponent lsc4=end2.createLocalSubComponent(Arrays.asList(URI.create("http:sbolstandard.org/localsubcomponenttype4")));
	    SubComponent endSubComponent2=SBOLAPI.addSubComponent(pTetR2, end2);		
	    ComponentReference cr2= pTetR2.createComponentReference(lsc4, endSubComponent2);
	    
	    cr2.addWasDerivedFrom(cr1);	 
	    endSubComponent2.addWasDerivedFrom(endSubComponent);	 
	    
	    /*Configuration.getInstance().setValidateBeforeSaving(false);
	    System.out.println(SBOLIO.write(doc,SBOLFormat.RDFXML));
	    Configuration.getInstance().setValidateBeforeSaving(true);*/
		   
	    TestUtil.validateDocument(doc, 1, "sbol3-12115");
	    
	    lsc4.addType(URI.create("http:sbolstandard.org/localsubcomponenttype3"));
	    TestUtil.validateDocument(doc, 0);
	
	    
	    
	    
		    
	    
	    
	    
	    
		
    }
}
