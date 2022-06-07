package org.sbolstandard.core3.usecase.test;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.Arrays;

import org.sbolstandard.core3.api.SBOLAPI;
import org.sbolstandard.core3.entity.Component;
import org.sbolstandard.core3.entity.ComponentReference;
import org.sbolstandard.core3.entity.Interaction;
import org.sbolstandard.core3.entity.SBOLDocument;
import org.sbolstandard.core3.entity.SequenceFeature;
import org.sbolstandard.core3.entity.SubComponent;
import org.sbolstandard.core3.io.SBOLFormat;
import org.sbolstandard.core3.io.SBOLIO;
import org.sbolstandard.core3.test.TestUtil;
import org.sbolstandard.core3.util.SBOLGraphException;
import org.sbolstandard.core3.vocabulary.ComponentType;
import org.sbolstandard.core3.vocabulary.InteractionType;
import org.sbolstandard.core3.vocabulary.Orientation;
import org.sbolstandard.core3.vocabulary.ParticipationRole;
import org.sbolstandard.core3.vocabulary.RestrictionType;
import org.sbolstandard.core3.vocabulary.Role;

/**
 * COMBINE 2020 SBOL 3 Tutorial
 * October, 2020
 * This tutorial code goes with the slides at:
 * https://github.com/SynBioDex/Community-Media/blob/master/2020/IWBDA20/SBOL3-IWBDA-2020.pptx
 */
public class GettingStartedTutorial_Short {


	public void runExample() throws SBOLGraphException, IOException
	{
		// Create a new SBOL document
		URI base=URI.create("https://synbiohub.org/public/igem/");
		SBOLDocument doc=new SBOLDocument(base);
		
		/**
		 *Slide 26: GFP expression cassette
		 * --------------------------------------------------
		 * Component
		 * identity: iGEM#I13504
		 * name: "iGEM 2016 interlab reporter"
		 * description: "GFP expression cassette used for 2016 iGEM interlab"
		 * type: SBO:0000251 (DNA)
		 * role: SO:0000804 (Engineered Region)
		 */
		System.out.println("Creating GFP expression cassette");
		Component device= SBOLAPI.createDnaComponent(doc, "i13504", "i13504", "Screening plasmid intermediate", ComponentType.DNA.getUrl(), null);
		System.out.println(String.format("Created GFP expression cassette component: %s", device.getUri()));
		  
		/* --------------------------------------------------
		 Slide 28: expression cassette parts
		-------------------------------------------------- */
		//Add the RBS subcomponent:
		//Create the RBS component
		Component rbs=SBOLAPI.createDnaComponent(doc, "B0034", "rbs", "RBS (Elowitz 1999)", Role.RBS, "aaagaggagaaa");
		
		//Start assembling the i13504 device's sequence by adding the RBS component.
		SBOLAPI.appendComponent(doc, device,rbs,Orientation.inline);
		System.out.println(String.format("Added the RBS subcomponent: %s", rbs.getUri()));
			
		//Add the scar sequence between the RBS and CDS components
		SequenceFeature scar1=SBOLAPI.appendSequenceFeature(doc, device, "tactag", Orientation.inline);
		System.out.println(String.format("Added the scar sequence between the RBS and the CDS components: %s", scar1.getUri()));
		
		//Create the GFP component and add it as a subcomponent to continue assembling the i13504 device.
		String gfp_na="atgcgtaaaggagaagaacttttcactggagttgtcccaattcttgttgaattagatggtgatgttaatgggcacaaattttctgtcagtggagagggtgaaggtgatgcaacatacggaaaacttacccttaaatttatttgcactactggaaaactacctgttccatggccaacacttgtcactactttcggttatggtgttcaatgctttgcgagatacccagatcatatgaaacagcatgactttttcaagagtgccatgcccgaaggttatgtacaggaaagaactatatttttcaaagatgacgggaactacaagacacgtgctgaagtcaagtttgaaggtgatacccttgttaatagaatcgagttaaaaggtattgattttaaagaagatggaaacattcttggacacaaattggaatacaactataactcacacaatgtatacatcatggcagacaaacaaaagaatggaatcaaagttaacttcaaaattagacacaacattgaagatggaagcgttcaactagcagaccattatcaacaaaatactccaattggcgatggccctgtccttttaccagacaaccattacctgtccacacaatctgccctttcgaaagatcccaacgaaaagagagaccacatggtccttcttgagtttgtaacagctgctgggattacacatggcatggatgaactatacaaataataa";
		Component gfp=SBOLAPI.createDnaComponent(doc, "E0040", "gfp", "gfp coding sequence", Role.CDS, gfp_na);
		SubComponent gfpSubComponent=SBOLAPI.appendComponent(doc, device,gfp, Orientation.inline);
		System.out.println(String.format("Added the GFP subcomponent %s", gfp.getUri()));
		
		//Add the scar between the CDS and terminator components
		SequenceFeature scar2=SBOLAPI.appendSequenceFeature(doc, device, "tactagag", Orientation.inline);
		System.out.println(String.format("Added the scar sequence between the CDS and the terminator components: %s", scar2.getUri()));
		
		//Create the terminator component
		String term_na="ccaggcatcaaataaaacgaaaggctcagtcgaaagactgggcctttcgttttatctgttgtttgtcggtgaacgctctc";
		Component term=SBOLAPI.createDnaComponent(doc, "B0015", "terminator", "B0015 double terminator", Role.Terminator,term_na);
		
		//Add the terminator as a subcomponent.
		SBOLAPI.appendComponent(doc, device,term, Orientation.inline);
		System.out.println(String.format("Added the terminator subcomponent %s", term.getUri()));
		
		/* --------------------------------------------------
		 Slide 32: GFP production from expression cassette
		 -------------------------------------------------- */
		 Component i13504_system=SBOLAPI.createComponent(doc,"i13504_system", ComponentType.FunctionalEntity.getUrl(), "i13504 system", null, Role.FunctionalCompartment);
		 Component GFP=SBOLAPI.createComponent(doc, "GFP_protein", ComponentType.Protein.getUrl(), "GFP", "GFP", null); 
		 SubComponent i13504SubComponent=SBOLAPI.addSubComponent(i13504_system, device);
		 SubComponent gfpProteinSubComponent=SBOLAPI.addSubComponent(i13504_system, GFP);
		 
		// List<ComponentReference> gfpRefs=SBOLAPI.createComponentReference(i13504_system, i13504, gfp);
	     
		 ComponentReference gfpCDSReference=i13504_system.createComponentReference(gfpSubComponent, i13504SubComponent);
					    
		 Interaction interaction= i13504_system.createInteraction(Arrays.asList(InteractionType.GeneticProduction));
    	 interaction.createParticipation(Arrays.asList(ParticipationRole.Template), gfpCDSReference);
    	 interaction.createParticipation(Arrays.asList(ParticipationRole.Product), gfpProteinSubComponent);
	    	
		 /* --------------------------------------------------
		  Slide 34: Example: concatenating & reusing components
		  -------------------------------------------------- */
		 
		 //Left hand side of slide: interlab16device1
		 Component ilab16_dev1=doc.createComponent("interlab16device1", Arrays.asList(ComponentType.DNA.getUrl())); 
		 Component j23101=doc.createComponent("j23101", Arrays.asList(ComponentType.DNA.getUrl())); 
		 SubComponent sc_j23101=SBOLAPI.addSubComponent(ilab16_dev1, j23101);	
		 SubComponent sc_i13504_system=SBOLAPI.addSubComponent(ilab16_dev1, i13504_system);	
		 
		 ComponentReference compRef_i13504_dev1=ilab16_dev1.createComponentReference(i13504SubComponent, sc_i13504_system);
		 ilab16_dev1.createConstraint(RestrictionType.Topology.meets, sc_j23101, compRef_i13504_dev1);
	        
		 // Right hand side of slide: interlab16device2
		 Component ilab16_dev2=doc.createComponent("interlab16device2", Arrays.asList(ComponentType.DNA.getUrl())); 
		 Component j23106=doc.createComponent("j23106", Arrays.asList(ComponentType.DNA.getUrl())); 
		 SubComponent sc_j23106=SBOLAPI.addSubComponent(ilab16_dev2, j23106);	
		 SubComponent sc_i13504_system_dev2=SBOLAPI.addSubComponent(ilab16_dev2, i13504_system);	
		 
		 ComponentReference compRef_i13504_dev2=ilab16_dev2.createComponentReference(i13504SubComponent, sc_i13504_system_dev2);
		 ilab16_dev2.createConstraint(RestrictionType.Topology.meets, sc_j23106, compRef_i13504_dev2);
		    
		 String output=SBOLIO.write(doc, SBOLFormat.RDFXML);
		 System.out.println("");
		 System.out.println("SBOL:");
		 System.out.println(output);   
	     TestUtil.serialise(doc, "combine2020", "combine2020");   
	     System.out.println("--------------------------");
	     //SBOLDocument doc2=SBOLIO.read(new File("input/gfp.nt"),SBOLFormat.NTRIPLES);
	     SBOLDocument doc2=SBOLIO.read(new File("output/combine2020/combine2020.nt"),SBOLFormat.NTRIPLES);
	     //SBOLDocument doc2=SBOLIO.read(new File("output/combine2020/combine2020.rdf"),"RDF/XML-ABBREV");
	     
	     
	     TestUtil.assertReadWrite(doc2);
	     String output2=SBOLIO.write(doc2, SBOLFormat.RDFXML);
	     System.out.println(output2);   
		    
	     System.out.println("done");   
	     
	}
	
	public static void main(String[] args) throws SBOLGraphException, IOException
	{
		GettingStartedTutorial_Short tutorial=new GettingStartedTutorial_Short();
		tutorial.runExample();
	}
}