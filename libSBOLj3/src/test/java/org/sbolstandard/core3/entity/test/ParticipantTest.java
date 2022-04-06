package org.sbolstandard.core3.entity.test;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;

import org.sbolstandard.core3.api.SBOLAPI;
import org.sbolstandard.core3.entity.Component;
import org.sbolstandard.core3.entity.ComponentReference;
import org.sbolstandard.core3.entity.Implementation;
import org.sbolstandard.core3.entity.Interaction;
import org.sbolstandard.core3.entity.SBOLDocument;
import org.sbolstandard.core3.entity.SubComponent;
import org.sbolstandard.core3.io.SBOLFormat;
import org.sbolstandard.core3.io.SBOLIO;
import org.sbolstandard.core3.test.TestUtil;
import org.sbolstandard.core3.util.SBOLGraphException;
import org.sbolstandard.core3.vocabulary.ComponentType;
import org.sbolstandard.core3.vocabulary.InteractionType;
import org.sbolstandard.core3.vocabulary.Orientation;
import org.sbolstandard.core3.vocabulary.ParticipationRole;
import org.sbolstandard.core3.vocabulary.Role;

import junit.framework.TestCase;

public class ParticipantTest extends TestCase {
	
	public void testParticipation() throws SBOLGraphException, IOException
    {
		String baseUri="https://sbolstandard.org/examples/";
        SBOLDocument doc=new SBOLDocument(URI.create(baseUri));
        
        Component device=doc.createComponent("i13504", Arrays.asList(ComponentType.DNA.getUrl())); 
        String gfp_na="atgcgtaaaggagaagaacttttcactggagttgtcccaattcttgttgaattagatggtgatgttaatgggcacaaattttctgtcagtggagagggtgaaggtgatgcaacatacggaaaacttacccttaaatttatttgcactactggaaaactacctgttccatggccaacacttgtcactactttcggttatggtgttcaatgctttgcgagatacccagatcatatgaaacagcatgactttttcaagagtgccatgcccgaaggttatgtacaggaaagaactatatttttcaaagatgacgggaactacaagacacgtgctgaagtcaagtttgaaggtgatacccttgttaatagaatcgagttaaaaggtattgattttaaagaagatggaaacattcttggacacaaattggaatacaactataactcacacaatgtatacatcatggcagacaaacaaaagaatggaatcaaagttaacttcaaaattagacacaacattgaagatggaagcgttcaactagcagaccattatcaacaaaatactccaattggcgatggccctgtccttttaccagacaaccattacctgtccacacaatctgccctttcgaaagatcccaacgaaaagagagaccacatggtccttcttgagtttgtaacagctgctgggattacacatggcatggatgaactatacaaataataa";
		Component gfp=SBOLAPI.createDnaComponent(doc, "E0040", "gfp", "gfp coding sequence", Role.CDS, gfp_na);
		
        SubComponent gfpSubComponent=SBOLAPI.appendComponent(doc, device,gfp, Orientation.inline);
		
        Component i13504_system=SBOLAPI.createComponent(doc,"i13504_system", ComponentType.DNA.getUrl(), "i13504 system", null, Role.FunctionalCompartment);
		Component GFP=SBOLAPI.createComponent(doc, "GFP_protein", ComponentType.Protein.getUrl(), "GFP", "GFP", null); 
		SubComponent i13504SubComponent=SBOLAPI.addSubComponent(i13504_system, device);
		SubComponent gfpProteinSubComponent=SBOLAPI.addSubComponent(i13504_system, GFP);
		
        ComponentReference gfpCDSReference=i13504_system.createComponentReference(gfpSubComponent, i13504SubComponent);
		
        Interaction interaction= i13504_system.createInteraction(Arrays.asList(InteractionType.GeneticProduction));
		org.sbolstandard.core3.entity.Participation participation= interaction.createParticipation(Arrays.asList(ParticipationRole.Template), gfpCDSReference.getUri());
		interaction.createParticipation(Arrays.asList(ParticipationRole.Product), gfpProteinSubComponent.getUri());
	    
		TestUtil.serialise(doc, "entity_additional/participation", "participation");
	    System.out.println(SBOLIO.write(doc, SBOLFormat.TURTLE));
	    TestUtil.assertReadWrite(doc); 
	    
	    TestUtil.validateIdentified(participation,doc,0);
	    participation.setRoles(null);
	    TestUtil.validateIdentified(participation,doc,1);
		
	    participation.setRoles(new ArrayList<URI>());
	    TestUtil.validateIdentified(participation,doc,1);
		
    }

}
