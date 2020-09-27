package org.sbolstandard.entity.test;

import java.io.IOException;
import java.net.URI;
import java.util.Arrays;

import org.sbolstandard.TestUtil;
import org.sbolstandard.api.SBOLAPI;
import org.sbolstandard.entity.Component;
import org.sbolstandard.entity.Interface;
import org.sbolstandard.entity.Location;
import org.sbolstandard.entity.Location.RangeLocationBuilder;
import org.sbolstandard.entity.RangeLocation;
import org.sbolstandard.entity.SBOLDocument;
import org.sbolstandard.entity.Sequence;
import org.sbolstandard.entity.SequenceFeature;
import org.sbolstandard.entity.SubComponent;
import org.sbolstandard.io.SBOLIO;
import org.sbolstandard.util.SBOLGraphException;
import org.sbolstandard.vocabulary.ComponentType;
import org.sbolstandard.vocabulary.Orientation;
import org.sbolstandard.vocabulary.Role;

import junit.framework.TestCase;

public class SequenceFeatureTest extends TestCase {
	
	public void testSequenceFeature() throws SBOLGraphException, IOException
    {
		String baseUri="https://sbolstandard.org/examples/";
        SBOLDocument doc=new SBOLDocument(URI.create(baseUri));
        
        String gfp_na="atgcgtaaaggagaagaacttttcactggagttgtcccaattcttgttgaattagatggtgatgttaatgggcacaaattttctgtcagtggagagggtgaaggtgatgcaacatacggaaaacttacccttaaatttatttgcactactggaaaactacctgttccatggccaacacttgtcactactttcggttatggtgttcaatgctttgcgagatacccagatcatatgaaacagcatgactttttcaagagtgccatgcccgaaggttatgtacaggaaagaactatatttttcaaagatgacgggaactacaagacacgtgctgaagtcaagtttgaaggtgatacccttgttaatagaatcgagttaaaaggtattgattttaaagaagatggaaacattcttggacacaaattggaatacaactataactcacacaatgtatacatcatggcagacaaacaaaagaatggaatcaaagttaacttcaaaattagacacaacattgaagatggaagcgttcaactagcagaccattatcaacaaaatactccaattggcgatggccctgtccttttaccagacaaccattacctgtccacacaatctgccctttcgaaagatcccaacgaaaagagagaccacatggtccttcttgagtttgtaacagctgctgggattacacatggcatggatgaactatacaaataataa";
		Component gfp=SBOLAPI.createDnaComponent(doc, "E0040", null, Role.CDS, gfp_na);
		
		Sequence seq= (Sequence)doc.getIdentified(gfp.getSequences().get(0),Sequence.class);
		
		RangeLocationBuilder location=new RangeLocationBuilder("location1", 1, 3, seq.getUri());
		gfp.createSequenceFeature("startcodon", Arrays.asList(location));
		
		System.out.println(SBOLIO.write(doc, "Turtle"));
        
    }

}
