package org.sbolstandard.usecase;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.Arrays;

import org.sbolstandard.TestUtil;
import org.sbolstandard.api.SBOLAPI;
import org.sbolstandard.entity.Component;
import org.sbolstandard.entity.SBOLDocument;
import org.sbolstandard.io.SBOLWriter;
import org.sbolstandard.util.SBOLGraphException;
import org.sbolstandard.vocabulary.InteractionType;
import org.sbolstandard.vocabulary.ParticipationRole;
import org.sbolstandard.vocabulary.Role;

import junit.framework.TestCase;

public class PoPSReceiverExample extends TestCase {

	public void testApp() throws SBOLGraphException, IOException
    {
        SBOLDocument doc=new SBOLDocument(URI.create("https://synbiohub.org/public/igem/"));
        //SBOLDocument doc=new SBOLDocument();
        Component popsReceiver=SBOLAPI.createDnaComponent(doc, URI.create("https://synbiohub.org/public/igem/BBa_F2620"), "BBa_F2620", "BBa_F2620", "PoPS Receiver", Role.EngineeredGene, null); 
        Component pTetR=SBOLAPI.createDnaComponent(doc, URI.create("https://synbiohub.org/public/igem/BBa_R0040"), "pTetR", "BBa_R0040", "TetR repressible promoter", Role.Promoter, "tccctatcagtgatagagattgacatccctatcagtgatagagatactgagcac");
        Component rbs=SBOLAPI.createDnaComponent(doc, URI.create("https://synbiohub.org/public/igem/BBa_B0034"), "BBa_B0034", "BBa_B0034", "RBS based on Elowitz repressilator", Role.RBS, "aaagaggagaaa");
        Component luxR=SBOLAPI.createDnaComponent(doc, URI.create("https://synbiohub.org/public/igem/BBa_C0062"), "luxR", "BBa_C0062", "luxR repressor/activator,  (no LVA?)", Role.CDS, "atgaaaaacataaatgccgacgacacatacagaataattaataaaattaaagcttgtagaagcaataatgatattaatcaatgcttatctgatatgactaaaatggtacattgtgaatattatttactcgcgatcatttatcctcattctatggttaaatctgatatttcaatcctagataattaccctaaaaaatggaggcaatattatgatgacgctaatttaataaaatatgatcctatagtagattattctaactccaatcattcaccaattaattggaatatatttgaaaacaatgctgtaaataaaaaatctccaaatgtaattaaagaagcgaaaacatcaggtcttatcactgggtttagtttccctattcatacggctaacaatggcttcggaatgcttagttttgcacattcagaaaaagacaactatatagatagtttatttttacatgcgtgtatgaacataccattaattgttccttctctagttgataattatcgaaaaataaatatagcaaataataaatcaaacaacgatttaaccaaaagagaaaaagaatgtttagcgtgggcatgcgaaggaaaaagctcttgggatatttcaaaaatattaggttgcagtgagcgtactgtcactttccatttaaccaatgcgcaaatgaaactcaatacaacaaaccgctgccaaagtatttctaaagcaattttaacaggagcaattgattgcccatactttaaaaattaataacactgatagtgctagtgtagatcac");
        Component doubleTerminator=SBOLAPI.createDnaComponent(doc, URI.create("https://synbiohub.org/public/igem/BBa_B0015"), "BBa_B0015", "BBa_B0015", "Double terminator consisting of BBa_B0010 and BBa_B0012", Role.Terminator, null);
        //Component doubleTerminator=createDnaComponent(doc, URI.create("https://synbiohub.org/public/igem/BBa_B0015"), "BBa_B0015", "BBa_B0015", "Double terminator consisting of BBa_B0010 and BBa_B0012", Role.Terminator, "ccaggcatcaaataaaacgaaaggctcagtcgaaagactgggcctttcgttttatctgttgtttgtcggtgaacgctctctactagagtcacactggctcaccttcgggtgggcctttctgcgtttata");
        Component pLuxR=SBOLAPI.createDnaComponent(doc, URI.create("https://synbiohub.org/public/igem/BBa_R0062"), "lux pR", "BBa_R0062", "Promoter (luxR &amp; HSL regulated -- lux pR)", Role.Promoter, "acctgtaggatcgtacaggtttacgcaagaaaatggtttgttatagtcgaataaa");
        Component BBa_B0010=SBOLAPI.createDnaComponent(doc, URI.create("https://synbiohub.org/public/igem/BBa_B0010"), "BBa_B0010", "BBa_B0010", "Transcriptional terminator consisting of a 64 bp stem-loop", Role.Terminator, "ccaggcatcaaataaaacgaaaggctcagtcgaaagactgggcctttcgttttatctgttgtttgtcggtgaacgctctc");
        Component BBa_B0012=SBOLAPI.createDnaComponent(doc, URI.create("https://synbiohub.org/public/igem/BBa_B0012"), "BBa_B0012", "BBa_B0012", "Double terminator consisting of BBa_B0010 and BBa_B0012", Role.Terminator, "tcacactggctcaccttcgggtgggcctttctgcgtttata");
      
        
        Component LuxR_protein=SBOLAPI.createProteinComponent(doc,popsReceiver, URI.create("https://synbiohub.org/public/igem/BBa_C0062_protein"), "LuxR", "BBa_C0062_protein", "LuxR protein", Role.TF, "NNNNNNNNNNN");
        Component TetR_protein=SBOLAPI.createProteinComponent(doc,popsReceiver, URI.create("https://synbiohub.org/public/igem/TetR_protein"), "TetR", "TetR_protein", "TetR protein", Role.TF, "NNNNNNNNNNN");
          
        SBOLAPI.appendComponent(doc, doubleTerminator,BBa_B0010);
        SBOLAPI.appendComponent(doc, doubleTerminator,BBa_B0012);
        
        SBOLAPI.appendComponent(doc, popsReceiver,pTetR);
        SBOLAPI.appendComponent(doc, popsReceiver,rbs);
        SBOLAPI.appendComponent(doc, popsReceiver,luxR);
        SBOLAPI.appendComponent(doc, popsReceiver,doubleTerminator);
        SBOLAPI.appendComponent(doc, popsReceiver,pLuxR);
        
        
        SBOLAPI.createInteraction(Arrays.asList(InteractionType.GeneticProduction),popsReceiver, luxR, Arrays.asList(ParticipationRole.Template), LuxR_protein, Arrays.asList(ParticipationRole.Product));  
        SBOLAPI.createInteraction(Arrays.asList(InteractionType.Stimulation),popsReceiver, pLuxR, Arrays.asList(ParticipationRole.Stimulated), LuxR_protein, Arrays.asList(ParticipationRole.Stimulator));
        SBOLAPI.createInteraction(Arrays.asList(InteractionType.Stimulation),popsReceiver, pTetR, Arrays.asList(ParticipationRole.Stimulated), TetR_protein, Arrays.asList(ParticipationRole.Stimulator));
        
        String output=SBOLWriter.write(doc, "Turtle");
        System.out.print(output);
        
        SBOLDocument doc2=SBOLWriter.read(output, "Turtle"); 
        output=SBOLWriter.write(doc2, "RDF/XML-ABBREV");
        
        System.out.print(output);
        
        TestUtil.serialise(doc2, "BBa_F2620_PoPSReceiver", "BBa_F2620_PoPSReceiver");     
        

        TestUtil.assertReadWrite(doc);    
        System.out.println("done");   
    }
    
}
