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

public class ParticipantTest_11906 extends TestCase {

	public void testParticipation() throws SBOLGraphException, IOException, Exception
    {
		String baseUri="https://sbolstandard.org/examples/";
        SBOLDocument doc=new SBOLDocument(URI.create(baseUri));
        
        Component device=doc.createComponent("i13504", Arrays.asList(ComponentType.DNA.getUri())); 
        device.setRoles(Arrays.asList(Role.EngineeredGene));
        String gfp_na="atgcgtaaaggagaagaacttttcactggagttgtcccaattcttgttgaattagatggtgatgttaatgggcacaaattttctgtcagtggagagggtgaaggtgatgcaacatacggaaaacttacccttaaatttatttgcactactggaaaactacctgttccatggccaacacttgtcactactttcggttatggtgttcaatgctttgcgagatacccagatcatatgaaacagcatgactttttcaagagtgccatgcccgaaggttatgtacaggaaagaactatatttttcaaagatgacgggaactacaagacacgtgctgaagtcaagtttgaaggtgatacccttgttaatagaatcgagttaaaaggtattgattttaaagaagatggaaacattcttggacacaaattggaatacaactataactcacacaatgtatacatcatggcagacaaacaaaagaatggaatcaaagttaacttcaaaattagacacaacattgaagatggaagcgttcaactagcagaccattatcaacaaaatactccaattggcgatggccctgtccttttaccagacaaccattacctgtccacacaatctgccctttcgaaagatcccaacgaaaagagagaccacatggtccttcttgagtttgtaacagctgctgggattacacatggcatggatgaactatacaaataataa";
		Component gfp=SBOLAPI.createDnaComponent(doc, "E0040", "gfp", "gfp coding sequence", Role.CDS, gfp_na);
		TestUtil.validateDocument(doc, 0);
        SubComponent gfpSubComponent=SBOLAPI.appendComponent(doc, device,gfp, Orientation.inline);
        TestUtil.validateDocument(doc, 0);
		
        Component i13504_system=SBOLAPI.createComponent(doc,"i13504_system", ComponentType.DNA.getUri(), "i13504 system", null, Role.FunctionalCompartment);
        i13504_system.setRoles(Arrays.asList(Role.FunctionalCompartment, Role.EngineeredGene));
		Component GFP=SBOLAPI.createComponent(doc, "GFP_protein", ComponentType.Protein.getUri(), "GFP", "GFP", null);//MSKGEELFTG 
		
		SubComponent i13504SubComponent=SBOLAPI.addSubComponent(i13504_system, device);
		
		SubComponent gfpProteinSubComponent=SBOLAPI.addSubComponent(i13504_system, GFP);
		Sequence seqGfpStart=doc.createSequence("gfp_start");
		SequenceFeature sf=gfp.createSequenceFeature(seqGfpStart);
		
		
		
        ComponentReference gfpCDSReference=i13504_system.createComponentReference(gfpSubComponent, i13504SubComponent);
		
        Interaction interaction= i13504_system.createInteraction(Arrays.asList(InteractionType.GeneticProduction.getUri()));
       
        Participation participation= interaction.createParticipation(Arrays.asList(ParticipationRole.Template.getUri()), gfpCDSReference);
		TestUtil.validateDocument(doc, 0);
        
		Participation par2= interaction.createParticipation(Arrays.asList(URI.create("http://sbolstandard.prg/nonSboUri")), gfpCDSReference);		
        TestUtil.validateDocument(doc, 2,"sbol3-11804, sbol3-11906");
        
        par2.setRoles(Arrays.asList(ParticipationRole.Product.getUri(), ParticipationRole.Template.getUri()));
        TestUtil.validateDocument(doc, 1, "sbol3-11906");
        
        par2.setRoles(Arrays.asList(URI.create("http://sbolstandard.prg/nonSboUri"), ParticipationRole.Product.getUri(), ParticipationRole.Template.getUri()));
        TestUtil.validateDocument(doc, 1, "sbol3-11906");
        
        par2.setRoles(Arrays.asList(URI.create("http://sbolstandard.prg/nonSboUri"), ParticipationRole.Product.getUri()));
        TestUtil.validateDocument(doc, 0);

   }

}
