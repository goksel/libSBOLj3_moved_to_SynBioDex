package org.sbolstandard.core3.usecase.test;

import java.io.IOException;
import java.net.URI;
import java.util.Arrays;
import org.sbolstandard.core3.api.SBOLAPI;
import org.sbolstandard.core3.entity.*;
import org.sbolstandard.core3.entity.measure.*;
import org.sbolstandard.core3.entity.provenance.*;
import org.sbolstandard.core3.io.SBOLFormat;
import org.sbolstandard.core3.io.SBOLIO;
import org.sbolstandard.core3.test.TestUtil;
import org.sbolstandard.core3.util.SBOLGraphException;
import org.sbolstandard.core3.vocabulary.*;
import org.sbolstandard.core3.vocabulary.RestrictionType.SequentialRestriction;

import junit.framework.TestCase;

public class PoPSReceiverExample_Detailed extends TestCase {

	public void testPoPSReceiverExample() throws SBOLGraphException, IOException
    {
		//http://parts.igem.org/Part:BBa_F2620
			
        SBOLDocument doc=new SBOLDocument(URI.create("https://synbiohub.org/public/igem/"));
        //SBOLDocument doc=new SBOLDocument();
        Component templateReceiver=SBOLAPI.createDnaComponent(doc, "Template", "Template", "Receiver Template", Role.EngineeredGene, null); 
        Component popsReceiver=SBOLAPI.createDnaComponent(doc, "BBa_F2620", "BBa_F2620", "PoPS Receiver", Role.EngineeredGene, null); 
        Component pGeneric=SBOLAPI.createDnaComponent(doc, "pGeneric", "pGeneric", "A repressible promoter", Role.Promoter, null);                
        Component pTetR=SBOLAPI.createDnaComponent(doc, "BBa_R0040", "pTetR", "TetR repressible promoter", Role.Promoter, "tccctatcagtgatagagattgacatccctatcagtgatagagatactgagcac");        
        Component pLacI=SBOLAPI.createDnaComponent(doc, "BBa_K174004", "pLacI", "LacI repressible promoter", Role.Promoter, "agaacaacctctgctaaaattcctgaaaaattttgcaaaaagttgttgactttatctacaaggtgtggcataatgtgtggaattgtgagcgctcacaattaagctt");        
        
        Component rbs=SBOLAPI.createDnaComponent(doc, "BBa_B0034", "BBa_B0034", "RBS based on Elowitz repressilator", Role.RBS, "aaagaggagaaa");
        Component luxR=SBOLAPI.createDnaComponent(doc, "BBa_C0062", "luxR", "luxR repressor/activator,  (no LVA?)", Role.CDS, "atgaaaaacataaatgccgacgacacatacagaataattaataaaattaaagcttgtagaagcaataatgatattaatcaatgcttatctgatatgactaaaatggtacattgtgaatattatttactcgcgatcatttatcctcattctatggttaaatctgatatttcaatcctagataattaccctaaaaaatggaggcaatattatgatgacgctaatttaataaaatatgatcctatagtagattattctaactccaatcattcaccaattaattggaatatatttgaaaacaatgctgtaaataaaaaatctccaaatgtaattaaagaagcgaaaacatcaggtcttatcactgggtttagtttccctattcatacggctaacaatggcttcggaatgcttagttttgcacattcagaaaaagacaactatatagatagtttatttttacatgcgtgtatgaacataccattaattgttccttctctagttgataattatcgaaaaataaatatagcaaataataaatcaaacaacgatttaaccaaaagagaaaaagaatgtttagcgtgggcatgcgaaggaaaaagctcttgggatatttcaaaaatattaggttgcagtgagcgtactgtcactttccatttaaccaatgcgcaaatgaaactcaatacaacaaaccgctgccaaagtatttctaaagcaattttaacaggagcaattgattgcccatactttaaaaattaataacactgatagtgctagtgtagatcac");
        Component doubleTerminator=SBOLAPI.createDnaComponent(doc, "BBa_B0015", "BBa_B0015", "Double terminator consisting of BBa_B0010 and BBa_B0012", Role.Terminator, null);
        Component pLuxR=SBOLAPI.createDnaComponent(doc, "BBa_R0062", "pLuxR", "Promoter (luxR &amp; HSL regulated -- lux pR)", Role.Promoter, "acctgtaggatcgtacaggtttacgcaagaaaatggtttgttatagtcgaataaa");
        Component BBa_B0010=SBOLAPI.createDnaComponent(doc, "BBa_B0010", "BBa_B0010", "Transcriptional terminator consisting of a 64 bp stem-loop", Role.Terminator, "ccaggcatcaaataaaacgaaaggctcagtcgaaagactgggcctttcgttttatctgttgtttgtcggtgaacgctctc");
        Component BBa_B0012=SBOLAPI.createDnaComponent(doc, "BBa_B0012", "BBa_B0012", "Double terminator consisting of BBa_B0010 and BBa_B0012", Role.Terminator, "tcacactggctcaccttcgggtgggcctttctgcgtttata");
      
        
        Component LuxR=SBOLAPI.createProteinComponent(doc, "BBa_C0062_protein", "LuxR",  "LuxR protein", Role.TF, "NNNNNNNNNNN");
        Component TetR=SBOLAPI.createProteinComponent(doc, "TetR_protein", "TetR", "TetR protein", Role.TF, "NNNNNNNNNNN");
        
        Component luxRDevice=SBOLAPI.createDnaComponent(doc, "LuxRDevice", "LuxRDevice", "LuxR Device", Role.EngineeredGene, null); 
                
        SBOLAPI.appendComponent(doc, doubleTerminator,BBa_B0010);
        SBOLAPI.appendComponent(doc, doubleTerminator,BBa_B0012);
        
        SBOLAPI.appendComponent(doc, luxRDevice,rbs);
        SBOLAPI.appendComponent(doc, luxRDevice,luxR);
        SBOLAPI.appendComponent(doc, luxRDevice,doubleTerminator);
        SBOLAPI.appendComponent(doc, luxRDevice,pLuxR);
            	
		SubComponent scGenericPromoterTemplate=templateReceiver.createSubComponent(pGeneric);			
		SubComponent scLuxRDeviceTemplate = templateReceiver.createSubComponent(luxRDevice);
		SubComponent scLuxRTemplate = templateReceiver.createSubComponent(LuxR);
		SubComponent scTetRTemplate = templateReceiver.createSubComponent(TetR);
		
		templateReceiver.createConstraint(SequentialRestriction.precedes, scGenericPromoterTemplate, scLuxRDeviceTemplate);	
		
		
		SubComponent scTetRPromoterReceiver=popsReceiver.createSubComponent(pTetR);			
		SubComponent scLuxRDeviceReceiver = popsReceiver.createSubComponent(luxRDevice);
		SubComponent scLuxRReceiver= popsReceiver.createSubComponent(LuxR);
		SubComponent scTetRReceiver= popsReceiver.createSubComponent(TetR);
		popsReceiver.createConstraint(SequentialRestriction.precedes, scTetRPromoterReceiver, scLuxRDeviceReceiver);
				
        
        SBOLAPI.createInteraction(Arrays.asList(InteractionType.GeneticProduction.getUri()),luxRDevice, luxR, Arrays.asList(ParticipationRole.Template.getUri()), LuxR, Arrays.asList(ParticipationRole.Product.getUri()));  
        SBOLAPI.createInteraction(Arrays.asList(InteractionType.Stimulation.getUri()),luxRDevice, pLuxR, Arrays.asList(ParticipationRole.Stimulated.getUri()), LuxR, Arrays.asList(ParticipationRole.Stimulator.getUri()));
        SBOLAPI.createInteraction(Arrays.asList(InteractionType.Stimulation.getUri()),popsReceiver, pTetR, Arrays.asList(ParticipationRole.Stimulated.getUri()), TetR, Arrays.asList(ParticipationRole.Stimulator.getUri()));
        
		
        CombinatorialDerivation compDerPro= doc.createCombinatorialDerivation("PromoterDerivation",templateReceiver);
		compDerPro.setStrategy(CombinatorialDerivationStrategy.Enumerate);
		VariableFeature varFeaturePro= compDerPro.createVariableFeature(VariableFeatureCardinality.One, scGenericPromoterTemplate);
		varFeaturePro.setVariants(Arrays.asList(pLacI, pTetR));
		
		popsReceiver.setWasDerivedFrom(Arrays.asList(compDerPro.getUri(),templateReceiver.getUri()));
		scTetRPromoterReceiver.addWasDerivedFrom(scGenericPromoterTemplate);
		scLuxRDeviceReceiver.addWasDerivedFrom(scLuxRDeviceTemplate);
		scLuxRReceiver.addWasDerivedFrom(scLuxRTemplate);
		scTetRReceiver.addWasDerivedFrom(scTetRTemplate);
					
		//ExperimentalData ed=doc.createExperimentalData("experimentaldata");
		Attachment at=doc.createAttachment("F2620Datasheet", URI.create("https://openwetware.org/wiki/File:F2620.de2-NEW.pdf"));
		popsReceiver.setAttachments(at);	
		SingularUnit polymerase=doc.createSingularUnit("polymerase", "polymerase", "polymerase");
		SingularUnit cell=doc.createSingularUnit("cell", "cell", "cell");
		UnitDivision pops=doc.createUnitDivision("PoPS", "PoPS", "PoPS", polymerase, cell);	    
		popsReceiver.createMeasure("PoPS_per_cell", 6.6f, pops );
		
		
		Model model=doc.createModel("PoPSReceiverSBMLModel", URI.create("http://sbolstandard.org/models/popsreceiver"), ModelFramework.Continuous,ModelLanguage.SBML);
        Activity activity=doc.createActivity("activity1");
        activity.addType(ActivityType.Design.getUri());
        activity.setName("ModelBasedDesignActivity");
           
        Usage usage1=activity.createUsage(model.getUri());
        usage1.addRole(ActivityType.Learn.getUri());
        
        Agent agent=doc.createAgent("Designer");
        agent.setName("Designer ");
        
        Plan plan=doc.createPlan("ModelBasedDesign");
        plan.setName("ModelBasedDesign");
              
        Association association=activity.createAssociation(agent);
        association.setPlan(plan);
        association.addRole(ActivityType.Design.getUri());
        
        popsReceiver.addWasGeneratedBy(activity);
		
        String output=SBOLIO.write(doc, SBOLFormat.TURTLE);
        System.out.print(output);
        
        TestUtil.assertReadWrite(doc);
        
        System.out.println("done");   
        //http://parts.igem.org/Part:BBa_F2620
    }
    
}
