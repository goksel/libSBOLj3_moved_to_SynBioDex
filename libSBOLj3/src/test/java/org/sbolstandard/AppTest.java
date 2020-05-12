package org.sbolstandard;

import java.net.URI;
import java.util.Arrays;

import org.sbolstandard.vocabulary.InteractionType;
import org.sbolstandard.vocabulary.ParticipationRole;
import org.sbolstandard.vocabulary.Role;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple App.
 */
public class AppTest 
    extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public AppTest( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( AppTest.class );
    }

    /**
     * Rigourous Test :-)
     */
    public void testApp()
    {
        assertTrue( true );
    }
    
    public void testReflection() throws SBOLGraphException, SBOLException
    {
        SBOLDocument doc=new SBOLDocument(URI.create("https://synbiohub.org/public/igem/"));
        //SBOLDocument doc=new SBOLDocument();
        Component popsReceiver=SBOLAPI.createDnaComponent(doc, URI.create("https://synbiohub.org/public/igem/BBa_F2620"), "BBa_F2620", "BBa_F2620", "PoPS Receiver", Role.EngineeredGene, null); 
        Component pTetR=SBOLAPI.createDnaComponent(doc, URI.create("https://synbiohub.org/public/igem/BBa_R0040"), "pTetR", "BBa_R0040", "TetR repressible promoter", Role.Promoter, "tccctatcagtgatagagattgacatccctatcagtgatagagatactgagcac");
        Component TetR_protein=SBOLAPI.createProteinComponent(doc,popsReceiver, URI.create("https://synbiohub.org/public/igem/TetR_protein"), "TetR", "TetR_protein", "TetR protein", Role.TF, "NNNNNNNNNNN");
          
        
        SBOLAPI.appendComponent(doc, popsReceiver,pTetR);
        
        long start,end,middle;
        
        /*start=System.nanoTime();
        
        for (int i=0;i<10000;i++)
        {
        	SBOLAPI.createInteraction(Arrays.asList(InteractionType.Stimulation),popsReceiver, pTetR, Arrays.asList(ParticipationRole.Stimulated), TetR_protein, Arrays.asList(ParticipationRole.Stimulator));
        }
        middle= System.nanoTime();
        
        for (int i=0;i<10000;i++)
        {
        	SBOLAPI.createInteractionDel(Arrays.asList(InteractionType.Stimulation),popsReceiver, pTetR, Arrays.asList(ParticipationRole.Stimulated), TetR_protein, Arrays.asList(ParticipationRole.Stimulator));
        }
        
        end= System.nanoTime();
        print(start,middle, end);
       
        
        start=System.nanoTime();
        
        for (int i=0;i<10;i++)
        {
        	popsReceiver.getInteractions(); 
        }
        middle= System.nanoTime();
        
        
        for (int i=0;i<10;i++)
        {
        	popsReceiver.getInteractionsDel(); 
        }
        end= System.nanoTime();
        print(start,middle, end);
        
        */
        
        start=System.nanoTime();
        
        for (int i=0;i<50;i++)
        {
        	SBOLAPI.createInteraction(Arrays.asList(InteractionType.Stimulation),popsReceiver, pTetR, Arrays.asList(ParticipationRole.Stimulated), TetR_protein, Arrays.asList(ParticipationRole.Stimulator));
        }
        middle= System.nanoTime();
        
        
        end= System.nanoTime();
        print(start,middle, end);
        
        
        start=System.nanoTime();
        
        for (int i=0;i<300000;i++)
        {
        	popsReceiver.getInteractions(); 
        }
        middle= System.nanoTime();
        
        
        end= System.nanoTime();
        print(start,middle, end);
       
        
        
    }
    
    private void print(long start, long middle, long end)
    {
    	 long first=middle-start;
         long second=end-middle;
         System.out.println(String.format("First execution time: %s ns, %s ms", first, first/1000000));
         System.out.println(String.format("Second execution time: %s ns, %s ms", second, second/1000000));
    }
}
