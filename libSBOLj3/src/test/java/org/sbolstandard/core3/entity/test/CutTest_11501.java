package org.sbolstandard.core3.entity.test;

import java.io.IOException;
import java.net.URI;
import java.util.Optional;
import org.sbolstandard.core3.api.SBOLAPI;
import org.sbolstandard.core3.entity.Component;
import org.sbolstandard.core3.entity.Cut;
import org.sbolstandard.core3.entity.SBOLDocument;
import org.sbolstandard.core3.entity.Sequence;
import org.sbolstandard.core3.entity.SequenceFeature;
import org.sbolstandard.core3.test.TestUtil;
import org.sbolstandard.core3.util.SBOLGraphException;
import org.sbolstandard.core3.vocabulary.Role;
import junit.framework.TestCase;

public class CutTest_11501 extends TestCase {
	
	public void testSut() throws SBOLGraphException, IOException, Exception
    {
		URI base=URI.create("https://synbiohub.org/public/igem/");
		SBOLDocument doc=new SBOLDocument(base);
		
		Component pTetR=SBOLAPI.createDnaComponent(doc, "BBa_R0040", "pTetR", "TetR repressible promoter", Role.Promoter, "tccctatcagtgatagagattgacatccctatcagtgatagagatactgagcac");
	    Sequence sequence=doc.getSequences().get(0);
		
		SequenceFeature feature=pTetR.createSequenceFeature(5, sequence);
		
		
    	Cut cut=(Cut)feature.getLocations().get(0);
    	TestUtil.validateIdentified(cut,doc,0);
    	
    	String elements=sequence.getElements();
    	cut.setAt(Optional.of(elements.length()));
    	TestUtil.validateIdentified(cut,doc,0);
    	
    	cut.setAt(Optional.of(elements.length()+1));
    	TestUtil.validateIdentified(cut,doc,1);
    	
    	cut.setAt(Optional.of(elements.length()-1));
    	TestUtil.validateIdentified(cut,doc,0);
    	
    	
    	
    	
    }
}
