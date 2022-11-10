package org.sbolstandard.core3.entity.test;

import java.io.IOException;
import java.net.URI;
import java.util.Arrays;
import java.util.Optional;

import org.sbolstandard.core3.api.SBOLAPI;
import org.sbolstandard.core3.entity.*;
import org.sbolstandard.core3.entity.Location.RangeLocationBuilder;
import org.sbolstandard.core3.io.SBOLFormat;
import org.sbolstandard.core3.io.SBOLIO;
import org.sbolstandard.core3.test.TestUtil;
import org.sbolstandard.core3.util.Configuration;
import org.sbolstandard.core3.util.SBOLGraphException;
import org.sbolstandard.core3.vocabulary.*;
import junit.framework.TestCase;

public class SubComponentTest_10807_3 extends TestCase {
	
	public void testSequenceFeature() throws SBOLGraphException, IOException, Exception
    {
		String baseUri="https://sbolstandard.org/examples/";
        SBOLDocument doc=new SBOLDocument(URI.create(baseUri));
        
        String plasmid_na="aacgatgatgctcactctcgggtaccagcattttcggaggttctctaacagtatggataaccgtgttttcactgtgctgcggttacccatcgcctgaaatccagttggtgtcaagccattccctgtctaggacgccgcatgtagtaaaacatatacattgctcgggttcggtctcgggagttgacggctagctcagtcctaggtacagtgctagctacttgagacctataaacgccaggttgtatccgcatttgatgctaccatggatgagtcagcgtcgagcacgcggcatttattgcatgagtagggttgactaagaaccgttagatgcctcgctgtactaataattgtcaacagatcgtcaagattagaaaatagggtttagtccggcaatacttccggcaaaaaagggcaaggtgtcaccaccctgccctttttctttaaaaccgaaaagattacttcgcgttatgcaggcttcctcgctcactgactcgctgcgctcggtcgttcggctgcggcgagcggtatcagctcactcaaaggcggtaatacggttatccacagaatcaggggataacgcaggaaagaacatgtgagcaaaaggccagcaaaaggccaggaaccgtaaaaaggccgcgttgctggcgtttttccacaggctccgcccccctgacgagcatcacaaaaatcgacgctcaagtcagaggtggcgaaacccgacaggactataaagataccaggcgtttccccctggaagctccctcgtgcgctctcctgttccgaccctgccgcttaccggatacctgtccgcctttctcccttcgggaagcgtggcgctttctcatagctcacgctgtaggtatctcagttcggtgtaggtcgttcgctccaagctgggctgtgtgcacgaaccccccgttcagcccgaccgctgcgccttatccggtaactatcgtcttgagtccaacccggtaagacacgacttatcgccactggcagcagccactggtaacaggattagcagagcgaggtatgtaggcggtgctacagagttcttgaagtggtggcctaactacggctacactagaagaacagtatttggtatctgcgctctgctgaagccagttaccttcggaaaaagagttggtagctcttgatccggcaaacaaaccaccgctggtagcggtggtttttttgtttgcaagcagcagattacgcgcagaaaaaaaggatctcaagaagatcctttgatcttttctacggggtctgacgctcagtggaacgaaaactcacgttaagggattttggtcatgagattatcaaaaaggatcttcacctagatccttttaaattaaaaatgaagttttaaatcaatctaaagtatatatgagtaaacttggtctgacagctcgaggcttggattctcaccaataaaaaacgcccggcggcaaccgagcgttctgaacaaatccagatggagttctgaggtcattactggatctatcaacaggagtccaagcgagctcgatatcaaattacgccccgccctgccactcatcgcagtactgttgtaattcattaagcattctgccgacatggaagccatcacaaacggcatgatgaacctgaatcgccagcggcatcagcaccttgtcgccttgcgtataatatttgcccatggtgaaaacgggggcgaagaagttgtccatattggccacgtttaaatcaaaactggtgaaactcacccagggattggctgagacgaaaaacatattctcaataaaccctttagggaaataggccaggttttcaccgtaacacgccacatcttgcgaatatatgtgtagaaactgccggaaatcgtcgtggtattcactccagagcgatgaaaacgtttcagtttgctcatggaaaacggtgtaacaagggtgaacactatcccatatcaccagctcaccgtctttcattgccatacgaaattccggatgagcattcatcaggcgggcaagaatgtgaataaaggccggataaaacttgtgcttatttttctttacggtctttaaaaaggccgtaatatccagctgaacggtctggttataggtacattgagcaactgactgaaatgcctcaaaatgttctttacgatgccattgggatatatcaacggtggtatatccagtgatttttttctccattttagcttccttagctcctgaaaatctcgataactcaaaaaatacgcccggtagtgatcttatttcattatggtgaaagttggaacctcttacgtgcccgatcaactcgagtgccacctgacgtctaagaaaccattattatcatgacattaacctataaaaataggcgtatcacgaggcagaatttcagataaaaaaaatccttagctttcgctaaggatgatttctg";
        Component plasmid= SBOLAPI.createDnaComponent(doc, "plasmid", null, null, Role.EngineeredGene, plasmid_na);
		
        Component RNAPbinding= SBOLAPI.createDnaComponent(doc, "BBa_J23100_RNAPbinding", null, null, Role.Promoter, "ttgacctagc");
		
	    SubComponent RNAPbindingSC = plasmid.createSubComponent(RNAPbinding);
	    Range range1=RNAPbindingSC.createRange(181,185, plasmid.getSequences().get(0));
	    Range range2=RNAPbindingSC.createRange(210,213, plasmid.getSequences().get(0));
	    
	    TestUtil.validateIdentified(RNAPbindingSC,doc,1);
	    range2.setEnd(Optional.of(214));
	    TestUtil.validateIdentified(RNAPbindingSC,doc,0);
	    String output=SBOLIO.write(doc, SBOLFormat.TURTLE);
	    System.out.println(output);
	   
	    
	    range2.setSequence(RNAPbinding.getSequences().get(0));
	    TestUtil.validateIdentified(RNAPbindingSC,doc,2,3);
	   
	   /* range2.setSequence(plasmid.getSequences().get(0));
	    TestUtil.validateIdentified(RNAPbindingSC,doc,0);
	     
	    String output=SBOLIO.write(doc, SBOLFormat.TURTLE);
	    System.out.println(output);
	   */
	    
	    
	    //Anderson_Promoters_in_vector_ins_BBa_J23100/SubComponent5/Range1
	    //	hasSequence: https://github.com/iGEM-Engineering/iGEM-distribution/Anderson%20Promoters/Anderson_Promoters_in_vector_ins_BBa_J23100_sequence
	    		
	   //Anderson_Promoters_in_vector_ins_BBa_J23100.feature -> Anderson_Promoters_in_vector_ins_BBa_J23100/SubComponent5
	    //	hasSequence: https://github.com/iGEM-Engineering/iGEM-distribution/Anderson%20Promoters/Anderson_Promoters_in_vector_ins_BBa_J23100_sequence
	    //<https://synbiohub.org/public/igem/BBa_J23100
	    //Anderson_Promoters_in_vector_ins_BBa_J23100/
    }

}
