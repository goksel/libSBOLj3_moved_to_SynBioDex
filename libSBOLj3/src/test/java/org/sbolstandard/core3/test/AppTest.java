package org.sbolstandard.core3.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.jena.iri.IRI;
import org.apache.jena.iri.IRIFactory;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.riot.Lang;
import org.apache.jena.riot.RDFFormat;
import org.sbolstandard.core3.api.SBOLAPI;
import org.sbolstandard.core3.entity.Component;
import org.sbolstandard.core3.entity.SBOLDocument;
import org.sbolstandard.core3.io.SBOLFormat;
import org.sbolstandard.core3.io.SBOLIO;
import org.sbolstandard.core3.util.RDFUtil;
import org.sbolstandard.core3.util.SBOLGraphException;
import org.sbolstandard.core3.util.SBOLUtil;
import org.sbolstandard.core3.util.URINameSpace;
import org.sbolstandard.core3.vocabulary.ComponentType;
import org.sbolstandard.core3.vocabulary.InteractionType;
import org.sbolstandard.core3.vocabulary.ParticipationRole;
import org.sbolstandard.core3.vocabulary.Role;

import junit.framework.Assert;
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
        URI a=URI.create("http://abc.org");
        URI b=URI.create("http://abc.org/");
        URI c=URI.create("http://abc.org:443");
        URI d=URI.create("http://abc.org");
        
        
        System.out.print(a.equals(b));
        System.out.print(a.equals(c));
        System.out.print(a.equals(d));
        
        
        String test="";
        
        		
    }
    
    public void IRI() throws SBOLGraphException, IOException
    {
    	boolean shouldPass=false;
    	try {
    	SBOLDocument doc=SBOLIO.read(new File ("/Users/gokselmisirli/Downloads/uri-iri_2.ttl"));
    	URI uri=doc.getComponents().get(0).getUri();
    	System.out.println(uri);
    	String out=SBOLIO.write(doc, SBOLFormat.TURTLE);
    	System.out.println(out);
    	doc.setBaseURI(URI.create("http://keele.ac.uk/scm/"));
    	Component comp2=doc.createComponent("päypal", Arrays.asList(ComponentType.DNA.getUri()));
    	System.out.println(comp2.getUri());
    	Component comp3=doc.createComponent("清华大学", Arrays.asList(ComponentType.DNA.getUri()));
    	System.out.println(comp3.getUri());
    	
    	Component temp=doc.getIdentified(URI.create("http://keele.ac.uk/scm/清华大学"), Component.class);
    	System.out.println(temp.getUri());
    	
    	//URI test=URI.create("\"Howareyou?");
    	//URI test2=URI.create("'Howareyou?");
    	
    	//IRI testiri=IRIFactory.iriImplementation().construct("'Howareyou?");
    	/*Component comp4=doc.createComponent("\"How are you?\"", Arrays.asList(ComponentType.DNA.getUri()));
    	System.out.println(comp4.getUri());
    	*/
    	SBOLIO.write(doc, new File("/Users/gokselmisirli/Downloads/uri-iri_2_modified.ttl"), SBOLFormat.TURTLE);
    	}
    	catch (SBOLGraphException so)
    	{
    		shouldPass=true;
    	}
    	finally
    	{
    		assertTrue("Check the IRI validation", shouldPass);
    	}
    	
    }
    public void testReflection() throws SBOLGraphException
    {
        SBOLDocument doc=new SBOLDocument(URI.create("https://synbiohub.org/public/igem/"));
        //SBOLDocument doc=new SBOLDocument();
        Component popsReceiver=SBOLAPI.createDnaComponent(doc, "BBa_F2620", "BBa_F2620", "PoPS Receiver", Role.EngineeredGene, null); 
        Component pTetR=SBOLAPI.createDnaComponent(doc, "BBa_R0040", "pTetR", "TetR repressible promoter", Role.Promoter, "tccctatcagtgatagagattgacatccctatcagtgatagagatactgagcac");
        Component TetR_protein=SBOLAPI.createProteinComponent(doc,popsReceiver, "TetR_protein", "TetR", "TetR protein", Role.TF, "NNNNNNNNNNN");
          
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
        	SBOLAPI.createInteraction(Arrays.asList(InteractionType.Stimulation.getUri()),popsReceiver, pTetR, Arrays.asList(ParticipationRole.Stimulated.getUri()), TetR_protein, Arrays.asList(ParticipationRole.Stimulator.getUri()));
        }
        //middle= System.nanoTime();
        
        
        end= System.nanoTime();
        print(start,end);
        
        start=System.nanoTime();
        
        for (int i=0;i<3000;i++)
        {
        	popsReceiver.getInteractions(); 
        }
        
        end= System.nanoTime();
        print(start, end);  
    }
    
    private void print(long start, long end)
    {
    	long first=end-start;
    	System.out.println(String.format("   Execution time: %s ns, %s ms", first, first/1000000));
    }
    
    public void testCreateReducedResourceOntologies() throws IOException
    {
    	reduce("../ontologies/edam.owl", "src/main/resources/edam.owl.reduced", "http://edamontology.org/", URINameSpace.EDAM.getUri().toString());	
    	reduce("../ontologies/so-simple.owl", "src/main/resources/so-simple.owl.reduced", "http://purl.obolibrary.org/obo/SO_", URINameSpace.SO.getUri().toString());	
    	reduce("../ontologies/sbo.owl", "src/main/resources/sbo.owl.reduced", "http://biomodels.net/SBO/SBO_", URINameSpace.SBO.getUri().toString());	
    }
    //http://purl.obolibrary.org/obo/SO_
    private void reduce(String sourceFile, String destinationFile, String search, String replaceWith) throws IOException
    {
    	List<String> propertiesToKeep=Arrays.asList("http://www.w3.org/2000/01/rdf-schema#subClassOf", "http://www.w3.org/1999/02/22-rdf-syntax-ns#about");
    	Model model=RDFUtil.read(new File(sourceFile));
    	RDFUtil.removePropertiesExcept(model, propertiesToKeep );
    	RDFUtil.write(model, new File(destinationFile), RDFFormat.RDFXML); 
    	String ontology=IOUtils.toString(new FileInputStream(destinationFile),Charset.defaultCharset());
    	int index=ontology.indexOf(search);
    	System.out.println("index:" + index);
    	ontology=ontology.replace(search, replaceWith);
		IOUtils.write(ontology, new FileOutputStream(destinationFile), Charset.defaultCharset());
    
		//Serialize in Turtle
		model=RDFUtil.read(new File(destinationFile), RDFFormat.RDFXML);
		RDFUtil.write(model, new File(destinationFile), RDFFormat.TURTLE); 
    }
    
   /* public void testParentType() throws FileNotFoundException
    {
    	//3331 --> 1333 --> 2330
    	
    	//3331 -->1333
    		//1333 --> 2066 --> 2350 --> 1915
    		//1333 --> 2330 --> 1915
    	//3331-->2332 -->1915
    	
    	//Model model=SBOLUtil.getModelFromFileResource("edam.owl");
    	Model model=RDFUtil.read(new File("../ontologies/edam.owl"));
    	
    	executeParentFinding(model);
    	
    	long start=System.nanoTime();
    	System.out.print("Removing unncessary lines!..."); 
        RDFUtil.removePropertiesExcept(model, Arrays.asList("http://www.w3.org/2000/01/rdf-schema#subClassOf", "http://www.w3.org/1999/02/22-rdf-syntax-ns#about"));
        System.out.println("Removed");
        print(start, System.nanoTime());  
        
        executeParentFinding(model);
    }
    
     private void executeParentFinding(Model model)
    {
    	
    	String child= "http://edamontology.org/format_3331"; //BLAST XML results format
    	String parent= "http://edamontology.org/format_2330";
    	
    	long start=System.nanoTime();
    	int n=1000;
    	System.out.println("Executing finding parent recursively for " + n + " times:");
    	for (int i=0;i<1000;i++){
    		boolean result=RDFUtil.hasParentRecursively(model, child , parent);
    		assertTrue(String.format("The child entity %s does not have the parent %s", child, parent), result);
        }
        print(start, System.nanoTime());  
    }
    
    */
    
    public void testEDAMParentType() throws FileNotFoundException
    {
    	//3331 --> 1333 --> 2330
    	
    	//3331 -->1333
    		//1333 --> 2066 --> 2350 --> 1915
    		//1333 --> 2330 --> 1915
    	//3331-->2332 -->1915
    	
    	//Model model=SBOLUtil.getModelFromFileResource("edam.owl");
    	Model model=SBOLUtil.getModelFromFileResource("edam.owl.reduced", Lang.TURTLE);
    	String child= "https://identifiers.org/edam:format_3331"; //BLAST XML results format
    	String parent= "https://identifiers.org/edam:format_2330";
    	boolean result=RDFUtil.hasParentRecursively(model, child , parent);
    	assertTrue(String.format("The child entity %s does not have the parent %s", child, parent), result);
    	
    	child= "https://identifiers.org/edam:format_3162"; //
    	result=RDFUtil.hasParentRecursively(model, child , parent);
    	assertTrue(String.format("The child entity %s does not have the parent %s", child, parent), result);
    	
    	
    }
    
}
