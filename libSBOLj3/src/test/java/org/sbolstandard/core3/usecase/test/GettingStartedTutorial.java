package org.sbolstandard.core3.usecase.test;

import java.io.IOException;
import java.net.URI;
import java.util.Arrays;
import java.util.List;

import org.sbolstandard.core3.api.SBOLAPI;
import org.sbolstandard.core3.entity.*;
import org.sbolstandard.core3.entity.Location.*;
import org.sbolstandard.core3.io.SBOLFormat;
import org.sbolstandard.core3.io.SBOLIO;
import org.sbolstandard.core3.util.SBOLGraphException;
import org.sbolstandard.core3.vocabulary.*;
/**
 * COMBINE 2020 SBOL 3 Tutorial
 * October, 2020
 * This tutorial code goes with the slides at:
 * https://github.com/SynBioDex/Community-Media/blob/master/2020/COMBINE20/SBOL3-COMBINE-2020.pptx
 * For more information see: https://goksel.github.io/libSBOLj3/
 */
public class GettingStartedTutorial {


	public SBOLDocument runExample() throws SBOLGraphException, IOException
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
		Component device=doc.createComponent("i13504", Arrays.asList(ComponentType.DNA.getUri())); 
		device.setName("i13504");
		device.setDescription("Screening plasmid intermediate");
		device.setRoles(Arrays.asList(Role.EngineeredGene));
		System.out.println(String.format("Created GFP expression cassette component: %s", device.getUri()));
		  
		/* --------------------------------------------------
		 Slide 28: expression cassette parts
		-------------------------------------------------- */
		//Add the RBS subcomponent:
		//Create the RBS component
		Component rbs=doc.createComponent("B0034", Arrays.asList(ComponentType.DNA.getUri())); 
		rbs.setName("B0034");
		rbs.setDescription("RBS (Elowitz 1999)");
		rbs.setRoles(Arrays.asList(Role.RBS));
		
		//Create a sequence entity for the RBS component
		Sequence rbs_seq=doc.createSequence("B0034_Sequence");
		rbs_seq.setElements("aaagaggagaaa");
		rbs_seq.setEncoding(Encoding.NucleicAcid);
		rbs.setSequences(Arrays.asList(rbs_seq));
		
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
		
		//Add the terminator as a subcomponent. This time we will be using low level API methods, which can be used to create features and locations.
		SubComponent termSubComponent=device.createSubComponent(term);
		termSubComponent.setOrientation(Orientation.inline);
		//Sequence i13504Sequence= doc.getIdentified(device.getSequences().get(0),Sequence.class);
		Sequence i13504Sequence= device.getSequences().get(0);
		
		
		
		
		int start=i13504Sequence.getElements().length() + 1;
		int end=start + term_na.length()-1;
    	
		i13504Sequence.setElements(i13504Sequence.getElements() + term_na);
		Range range=termSubComponent.createRange(start, end,i13504Sequence);
		range.setOrientation(Orientation.inline);
		
		System.out.println(String.format("Added the terminator subcomponent %s", term.getUri()));
		
		//Iterate through sub components
		System.out.println("Subcomponents:");
		for (SubComponent subComp: device.getSubComponents())
		{
			System.out.println(subComp.getInstanceOf().getUri());
		}

		//Search for components using the SPARQL graph query language.
		List<Component> components=(List<Component>)doc.getIdentifieds("?identified a sbol:Component; sbol:role  SO:0000141; sbol:type SBO:0000251 .", Component.class);
		System.out.println("Graph query results:");
		for (Component component:components)
		{
			System.out.println("  " +  component.getDisplayId());
		}
    	
		/* --------------------------------------------------
 		Slide 32: GFP production from expression cassette
		-------------------------------------------------- */
		Component i13504_system=SBOLAPI.createComponent(doc,"i13504_system", ComponentType.DNA.getUri(), "i13504 system", null, Role.FunctionalCompartment);
		i13504_system.setRoles(Arrays.asList(Role.EngineeredGene));
		
		Component GFP=SBOLAPI.createComponent(doc, "GFP_protein", ComponentType.Protein.getUri(), "GFP", "GFP", null); 
		SubComponent i13504SubComponent=SBOLAPI.addSubComponent(i13504_system, device);
		SubComponent gfpProteinSubComponent=SBOLAPI.addSubComponent(i13504_system, GFP);
		  
		ComponentReference gfpCDSReference=i13504_system.createComponentReference(gfpSubComponent, i13504SubComponent);
					    
		Interaction interaction= i13504_system.createInteraction(Arrays.asList(InteractionType.GeneticProduction.getUri()));
		interaction.createParticipation(Arrays.asList(ParticipationRole.Template.getUri()), gfpCDSReference);
		interaction.createParticipation(Arrays.asList(ParticipationRole.Product.getUri()), gfpProteinSubComponent);
	    	
		 /* --------------------------------------------------
		  Slide 34: Example: concatenating & reusing components
		  -------------------------------------------------- */
		 //Left hand side of slide: interlab16device1
		 Component ilab16_dev1=doc.createComponent("interlab16device1", Arrays.asList(ComponentType.DNA.getUri())); 
		 ilab16_dev1.setRoles(Arrays.asList(Role.EngineeredGene));
			
		 Component j23101=doc.createComponent("j23101", Arrays.asList(ComponentType.DNA.getUri())); 
		 j23101.setRoles(Arrays.asList(Role.EngineeredGene));
			
		 SubComponent sc_j23101=SBOLAPI.addSubComponent(ilab16_dev1, j23101);	
		 SubComponent sc_i13504_system=SBOLAPI.addSubComponent(ilab16_dev1, i13504_system);	
		 
		 ComponentReference compRef_i13504_dev1=ilab16_dev1.createComponentReference(i13504SubComponent, sc_i13504_system);
		 ilab16_dev1.createConstraint(RestrictionType.TopologyRestriction.meets.getUri(), sc_j23101, compRef_i13504_dev1);
	        
		 // Right hand side of slide: interlab16device2
		 Component ilab16_dev2=doc.createComponent("interlab16device2", Arrays.asList(ComponentType.DNA.getUri())); 
		 ilab16_dev2.setRoles(Arrays.asList(Role.EngineeredGene));
		 
		 Component j23106=doc.createComponent("j23106", Arrays.asList(ComponentType.DNA.getUri())); 
		 j23106.setRoles(Arrays.asList(Role.EngineeredGene));
		 SubComponent sc_j23106=SBOLAPI.addSubComponent(ilab16_dev2, j23106);	
		 SubComponent sc_i13504_system_dev2=SBOLAPI.addSubComponent(ilab16_dev2, i13504_system);	
		 
		 ComponentReference compRef_i13504_dev2=ilab16_dev2.createComponentReference(i13504SubComponent, sc_i13504_system_dev2);
		 ilab16_dev2.createConstraint(RestrictionType.TopologyRestriction.meets.getUri(), sc_j23106, compRef_i13504_dev2);
		 
		 System.out.println(System.lineSeparator() + "SBOL:");
		 String output=SBOLIO.write(doc, SBOLFormat.TURTLE);
		 System.out.println(output); 
		 System.out.println("...done!");   
		 return doc;
	}
	
	public static void main(String[] args) throws SBOLGraphException, IOException
	{
		GettingStartedTutorial tutorial=new GettingStartedTutorial();
		tutorial.runExample();
	}
}