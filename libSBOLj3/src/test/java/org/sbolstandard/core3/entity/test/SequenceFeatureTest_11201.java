package org.sbolstandard.core3.entity.test;

import java.io.IOException;
import java.net.URI;
import org.sbolstandard.core3.api.SBOLAPI;
import org.sbolstandard.core3.entity.*;
import org.sbolstandard.core3.test.TestUtil;
import org.sbolstandard.core3.util.SBOLGraphException;
import org.sbolstandard.core3.vocabulary.*;
import junit.framework.TestCase;

public class SequenceFeatureTest_11201 extends TestCase {
	
	public void testSequenceFeature() throws SBOLGraphException, IOException, Exception
    {
		String baseUri="https://sbolstandard.org/examples/";
        SBOLDocument doc=new SBOLDocument(URI.create(baseUri));
        
        String gfp_na="atgcgtaaaggagaagaacttttcactggagttgtcccaattcttgttgaattagatggtgatgttaatgggcacaaattttctgtcagtggagagggtgaaggtgatgcaacatacggaaaacttacccttaaatttatttgcactactggaaaactacctgttccatggccaacacttgtcactactttcggttatggtgttcaatgctttgcgagatacccagatcatatgaaacagcatgactttttcaagagtgccatgcccgaaggttatgtacaggaaagaactatatttttcaaagatgacgggaactacaagacacgtgctgaagtcaagtttgaaggtgatacccttgttaatagaatcgagttaaaaggtattgattttaaagaagatggaaacattcttggacacaaattggaatacaactataactcacacaatgtatacatcatggcagacaaacaaaagaatggaatcaaagttaacttcaaaattagacacaacattgaagatggaagcgttcaactagcagaccattatcaacaaaatactccaattggcgatggccctgtccttttaccagacaaccattacctgtccacacaatctgccctttcgaaagatcccaacgaaaagagagaccacatggtccttcttgagtttgtaacagctgctgggattacacatggcatggatgaactatacaaataataa";
		Component gfp=SBOLAPI.createDnaComponent(doc, "E0040", "E0040", null, Role.CDS, gfp_na);
		
		Sequence seq= gfp.getSequences().get(0);
		
		SequenceFeature feature=gfp.createSequenceFeature(1, 3, seq);
	    TestUtil.validateIdentified(feature,doc,0);

	    feature.createCut(3, seq);
	    feature.createRange(6, 10, seq);
	       
	    feature.createCut(12, seq);
	    feature.createCut(13, seq);
	    feature.createRange(20, 30, seq);
	    feature.createRange(40, 60, seq);
	    TestUtil.validateIdentified(feature,doc,0);
	    
	    //Overlapping range:
	    feature.createRange(60, 61, seq);
	    TestUtil.validateIdentified(feature,doc,1);
	    
	   feature.createRange(16, 20, seq);
	    feature.createRange(7, 8, seq);
	    TestUtil.validateIdentified(feature,doc,3); 
	    
    }

}
