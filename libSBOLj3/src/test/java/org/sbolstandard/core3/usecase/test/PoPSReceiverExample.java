package org.sbolstandard.core3.usecase.test;

import java.io.IOException;
import java.net.URI;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.sbolstandard.core3.api.SBOLAPI;
import org.sbolstandard.core3.entity.Component;
import org.sbolstandard.core3.entity.SBOLDocument;
import org.sbolstandard.core3.entity.Sequence;
import org.sbolstandard.core3.io.SBOLFormat;
import org.sbolstandard.core3.io.SBOLIO;
import org.sbolstandard.core3.test.TestUtil;
import org.sbolstandard.core3.util.SBOLGraphException;
import org.sbolstandard.core3.validation.SBOLComparator;
import org.sbolstandard.core3.vocabulary.ComponentType;
import org.sbolstandard.core3.vocabulary.DataModel;
import org.sbolstandard.core3.vocabulary.Encoding;
import org.sbolstandard.core3.vocabulary.InteractionType;
import org.sbolstandard.core3.vocabulary.ParticipationRole;
import org.sbolstandard.core3.vocabulary.Role;

import junit.framework.TestCase;

public class PoPSReceiverExample extends TestCase {

	public void testApp() throws SBOLGraphException, IOException
    {
        SBOLDocument doc=new SBOLDocument(URI.create("https://synbiohub.org/public/igem/"));
        //SBOLDocument doc=new SBOLDocument();
        Component popsReceiver=SBOLAPI.createDnaComponent(doc, "BBa_F2620", "BBa_F2620", "PoPS Receiver", Role.EngineeredGene, null); 
        Component pTetR=SBOLAPI.createDnaComponent(doc, "BBa_R0040", "pTetR", "TetR repressible promoter", Role.Promoter, "tccctatcagtgatagagattgacatccctatcagtgatagagatactgagcac");
        Component rbs=SBOLAPI.createDnaComponent(doc, "BBa_B0034", "BBa_B0034", "RBS based on Elowitz repressilator", Role.RBS, "aaagaggagaaa");
        Component luxR=SBOLAPI.createDnaComponent(doc, "BBa_C0062", "luxR", "luxR repressor/activator,  (no LVA?)", Role.CDS, "atgaaaaacataaatgccgacgacacatacagaataattaataaaattaaagcttgtagaagcaataatgatattaatcaatgcttatctgatatgactaaaatggtacattgtgaatattatttactcgcgatcatttatcctcattctatggttaaatctgatatttcaatcctagataattaccctaaaaaatggaggcaatattatgatgacgctaatttaataaaatatgatcctatagtagattattctaactccaatcattcaccaattaattggaatatatttgaaaacaatgctgtaaataaaaaatctccaaatgtaattaaagaagcgaaaacatcaggtcttatcactgggtttagtttccctattcatacggctaacaatggcttcggaatgcttagttttgcacattcagaaaaagacaactatatagatagtttatttttacatgcgtgtatgaacataccattaattgttccttctctagttgataattatcgaaaaataaatatagcaaataataaatcaaacaacgatttaaccaaaagagaaaaagaatgtttagcgtgggcatgcgaaggaaaaagctcttgggatatttcaaaaatattaggttgcagtgagcgtactgtcactttccatttaaccaatgcgcaaatgaaactcaatacaacaaaccgctgccaaagtatttctaaagcaattttaacaggagcaattgattgcccatactttaaaaattaataacactgatagtgctagtgtagatcac");
        Component doubleTerminator=SBOLAPI.createDnaComponent(doc, "BBa_B0015", "BBa_B0015", "Double terminator consisting of BBa_B0010 and BBa_B0012", Role.Terminator, null);
        //Component doubleTerminator=createDnaComponent(doc, URI.create("https://synbiohub.org/public/igem/BBa_B0015"), "BBa_B0015", "BBa_B0015", "Double terminator consisting of BBa_B0010 and BBa_B0012", Role.Terminator, "ccaggcatcaaataaaacgaaaggctcagtcgaaagactgggcctttcgttttatctgttgtttgtcggtgaacgctctctactagagtcacactggctcaccttcgggtgggcctttctgcgtttata");
        Component pLuxR=SBOLAPI.createDnaComponent(doc, "BBa_R0062", "lux pR", "Promoter (luxR &amp; HSL regulated -- lux pR)", Role.Promoter, "acctgtaggatcgtacaggtttacgcaagaaaatggtttgttatagtcgaataaa");
        Component BBa_B0010=SBOLAPI.createDnaComponent(doc, "BBa_B0010", "BBa_B0010", "Transcriptional terminator consisting of a 64 bp stem-loop", Role.Terminator, "ccaggcatcaaataaaacgaaaggctcagtcgaaagactgggcctttcgttttatctgttgtttgtcggtgaacgctctc");
        Component BBa_B0012=SBOLAPI.createDnaComponent(doc, "BBa_B0012", "BBa_B0012", "Double terminator consisting of BBa_B0010 and BBa_B0012", Role.Terminator, "tcacactggctcaccttcgggtgggcctttctgcgtttata");
      
        
        Component LuxR_protein=SBOLAPI.createProteinComponent(doc,popsReceiver, "BBa_C0062_protein", "LuxR",  "LuxR protein", Role.TF, "NNNNNNNNNNN");
        Component TetR_protein=SBOLAPI.createProteinComponent(doc,popsReceiver, "TetR_protein", "TetR", "TetR protein", Role.TF, "NNNNNNNNNNN");
          
        SBOLAPI.appendComponent(doc, doubleTerminator,BBa_B0010);
        SBOLAPI.appendComponent(doc, doubleTerminator,BBa_B0012);
        
        SBOLAPI.appendComponent(doc, popsReceiver,pTetR);
        SBOLAPI.appendComponent(doc, popsReceiver,rbs);
        SBOLAPI.appendComponent(doc, popsReceiver,luxR);
        SBOLAPI.appendComponent(doc, popsReceiver,doubleTerminator);
        SBOLAPI.appendComponent(doc, popsReceiver,pLuxR);
        
        
        SBOLAPI.createInteraction(Arrays.asList(InteractionType.GeneticProduction.getUri()),popsReceiver, luxR, Arrays.asList(ParticipationRole.Template.getUri()), LuxR_protein, Arrays.asList(ParticipationRole.Product.getUri()));  
        SBOLAPI.createInteraction(Arrays.asList(InteractionType.Stimulation.getUri()),popsReceiver, pLuxR, Arrays.asList(ParticipationRole.Stimulated.getUri()), LuxR_protein, Arrays.asList(ParticipationRole.Stimulator.getUri()));
        SBOLAPI.createInteraction(Arrays.asList(InteractionType.Stimulation.getUri()),popsReceiver, pTetR, Arrays.asList(ParticipationRole.Stimulated.getUri()), TetR_protein, Arrays.asList(ParticipationRole.Stimulator.getUri()));
        
        String output=SBOLIO.write(doc, SBOLFormat.TURTLE);
        System.out.print(output);
        
        SBOLDocument doc2=SBOLIO.read(output, SBOLFormat.TURTLE); 
        output=SBOLIO.write(doc2, SBOLFormat.RDFXML);
        
        System.out.print(output);
        
        TestUtil.serialise(doc2, "BBa_F2620_PoPSReceiver", "BBa_F2620_PoPSReceiver");     
        

        TestUtil.assertReadWrite(doc); 
        
        System.out.print(SBOLIO.write(doc2, SBOLFormat.JSONLD));
        
        TestUtil.assertDNARootComponents(doc2, 1);
        
        List<Sequence> pLuxRSeqs=pLuxR.getSequences(Encoding.NucleicAcid);
        assertTrue("Could not filter sequences by encoding type", pLuxRSeqs!=null && pLuxRSeqs.size()==1);
        System.out.println("Sequence URI:" + pLuxRSeqs.get(0).getUri());
        
        List<Sequence> naSequences=doc2.getSequences(Encoding.NucleicAcid);
        assertTrue("Could not filter document sequences by encoding type", naSequences!=null && naSequences.size()==8);
         
        Component comp=doc2.getIdentified(pTetR.getUri(), Component.class);
        SBOLComparator.assertEqualEntity(pTetR, comp);
        
        
	  
        System.out.println("done");   
    }
    
}
