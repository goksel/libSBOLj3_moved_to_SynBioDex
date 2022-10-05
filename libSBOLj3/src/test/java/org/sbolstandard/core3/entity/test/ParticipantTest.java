package org.sbolstandard.core3.entity.test;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

public class ParticipantTest extends TestCase {
	
	public void testParticipation() throws SBOLGraphException, IOException, Exception
    {
		String baseUri="https://sbolstandard.org/examples/";
        SBOLDocument doc=new SBOLDocument(URI.create(baseUri));
        
        Component device=doc.createComponent("i13504", Arrays.asList(ComponentType.DNA.getUri())); 
        device.setRoles(Arrays.asList(Role.EngineeredGene));
        String gfp_na="atgcgtaaaggagaagaacttttcactggagttgtcccaattcttgttgaattagatggtgatgttaatgggcacaaattttctgtcagtggagagggtgaaggtgatgcaacatacggaaaacttacccttaaatttatttgcactactggaaaactacctgttccatggccaacacttgtcactactttcggttatggtgttcaatgctttgcgagatacccagatcatatgaaacagcatgactttttcaagagtgccatgcccgaaggttatgtacaggaaagaactatatttttcaaagatgacgggaactacaagacacgtgctgaagtcaagtttgaaggtgatacccttgttaatagaatcgagttaaaaggtattgattttaaagaagatggaaacattcttggacacaaattggaatacaactataactcacacaatgtatacatcatggcagacaaacaaaagaatggaatcaaagttaacttcaaaattagacacaacattgaagatggaagcgttcaactagcagaccattatcaacaaaatactccaattggcgatggccctgtccttttaccagacaaccattacctgtccacacaatctgccctttcgaaagatcccaacgaaaagagagaccacatggtccttcttgagtttgtaacagctgctgggattacacatggcatggatgaactatacaaataataa";
		Component gfp=SBOLAPI.createDnaComponent(doc, "E0040", "gfp", "gfp coding sequence", Role.CDS, gfp_na);
		
        SubComponent gfpSubComponent=SBOLAPI.appendComponent(doc, device,gfp, Orientation.inline);
		
        Component i13504_system=SBOLAPI.createComponent(doc,"i13504_system", ComponentType.DNA.getUri(), "i13504 system", null, Role.FunctionalCompartment);
        i13504_system.setRoles(Arrays.asList(Role.FunctionalCompartment, Role.EngineeredGene));
		Component GFP=SBOLAPI.createComponent(doc, "GFP_protein", ComponentType.Protein.getUri(), "GFP", "GFP", null);//MSKGEELFTG 
		SubComponent i13504SubComponent=SBOLAPI.addSubComponent(i13504_system, device);
		SubComponent gfpProteinSubComponent=SBOLAPI.addSubComponent(i13504_system, GFP);
		Sequence seqGfpStart=doc.createSequence("gfp_start");
		SequenceFeature sf=gfp.createSequenceFeature(seqGfpStart);
		
		
		
        ComponentReference gfpCDSReference=i13504_system.createComponentReference(gfpSubComponent, i13504SubComponent);
		
        Interaction interaction= i13504_system.createInteraction(Arrays.asList(InteractionType.GeneticProduction));
       
        Participation participation= interaction.createParticipation(Arrays.asList(ParticipationRole.Template), gfpCDSReference);
		interaction.createParticipation(Arrays.asList(ParticipationRole.Product), gfpProteinSubComponent);
	    
		
		Interaction interaction2= i13504_system.createInteraction(Arrays.asList(InteractionType.Inhibition));
		Component inhibitor=doc.createComponent("TetR", Arrays.asList(ComponentType.Protein.getUri())); 
		Participation participation2=interaction2.createHigherOrderParticipation(Arrays.asList(ParticipationRole.Inhibitor), interaction.getUri());
			
		 
		TestUtil.serialise(doc, "entity_additional/participation", "participation");
	    System.out.println(SBOLIO.write(doc, SBOLFormat.TURTLE));
	    TestUtil.assertReadWrite(doc); 
	    
	    Configuration.getInstance().setValidateAfterSettingProperties(false);
	        
	    TestUtil.validateIdentified(participation,doc,0);
	    
	    TestUtil.validateProperty(participation, "setRoles", new Object[] {null}, List.class);
	    TestUtil.validateProperty(participation, "setRoles", new Object[] {new ArrayList<URI>()}, List.class);
	    
	    List<URI> tempRoles=participation.getRoles();
	    participation.setRoles(null);
	    TestUtil.validateIdentified(participation,doc,1);
		
	    participation.setRoles(new ArrayList<URI>());
	    TestUtil.validateIdentified(participation,doc,1);
	    
	    //PARTICIPANT_MUST_HAVE_ONE_PARTICIPANT_OR_HIGHERORDERPARTICIPANT
	    Feature temp=participation.getParticipant();
	    participation.setParticipant(null);
	    TestUtil.validateIdentified(participation,doc,2);
	    
	    participation.setHigherOrderParticipant(interaction2.getUri());
	    TestUtil.validateIdentified(participation,doc,1);
	    
	   //PARTICIPANT_MUST_HAVE_ONE_PARTICIPANT_OR_HIGHERORDERPARTICIPANT 
	    participation.setHigherOrderParticipant(interaction2.getUri());
	    participation.setParticipant(temp);
	    TestUtil.validateIdentified(participation,doc,2);
	    
	    participation.setHigherOrderParticipant(null);
	    participation.setRoles(tempRoles);
	    TestUtil.validateIdentified(participation,doc,0);
	    
	    Resource resource = TestUtil.getResource(participation);
		
	    //SBOL_VALID_ENTITY_TYPES - Participation.Feature/Participant
	    Feature participant=participation.getParticipant();
	  	RDFUtil.setProperty(resource, DataModel.Participation.participant, Arrays.asList(participant.getUri(), inhibitor.getUri()));
	  	TestUtil.validateIdentified(participation,1);
	  	participation.setParticipant(participant);
	  	TestUtil.validateIdentified(participation,doc,0);		
	  		
	  		
	    //PARTICIPANT_PARTICIPANT_MUST_REFER_TO_A_FEATURE_OF_THE_PARENT
	    participation.setParticipant(sf);
	    TestUtil.validateIdentified(i13504_system,doc, 1);
	    
	    participation.setParticipant(temp);
	    TestUtil.validateIdentified(i13504_system,doc, 0);
	    
	    //PARTICIPANT_HIGHERORDERPARTICIPANT_MUST_REFER_TO_AN_INTERACTION_OF_THE_PARENT
	    participation2.setHigherOrderParticipant(URI.create("http://someinvalidhigherorderparticipant.org"));
	    TestUtil.validateIdentified(i13504_system,doc, 1);
		    
	    /*
	    URI uri=URI.create("https://www.abc.org");
	    URI uri2=URI.create("https://www.abc.org:443");
	    URI uri3=URI.create("HTTPS://www.abc.org:443/");
	    URI uri4=URI.create("HTTPS://www.abc.org:443");
	    
	    System.out.println(uri.equals(uri2));
	    System.out.println(uri2.equals(uri3));
	    System.out.println(uri3.equals(uri4));*/
	    
	    URI uri=URI.create("https://synbiohub.org/public/igem/cs1");
	    System.out.print(URI.create("https://synbiohub.org/public"));
	    
	    
		    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
		
    }

}
