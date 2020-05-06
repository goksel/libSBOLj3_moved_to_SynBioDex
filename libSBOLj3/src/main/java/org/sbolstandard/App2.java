package org.sbolstandard;

import java.io.File;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.jena.riot.Lang;
import org.apache.jena.riot.RDFFormat;
import org.sbolstandard.Location.CutLocationBuilder;
import org.sbolstandard.Location.LocationBuilder;
import org.sbolstandard.vocabulary.ComponentType;
import org.sbolstandard.vocabulary.Encoding;
import org.sbolstandard.vocabulary.Orientation;
import org.sbolstandard.vocabulary.Role;

/**
 * Hello world!
 *
 */
public class App2 
{
    public static void main( String[] args ) throws Exception
    {
       SBOLDocument doc=new SBOLDocument(URI.create("https://synbiohub.org/public/igem/"));
       //SBOLDocument doc=new SBOLDocument();
        Component popsReceiver=createDnaComponent(doc, URI.create("https://synbiohub.org/public/igem/BBa_F2620"), "BBa_F2620", "BBa_F2620", "PoPS Receiver", Role.EngineeredGene, null); 
       Component pTetR=createDnaComponent(doc, URI.create("https://synbiohub.org/public/igem/BBa_R0040"), "pTetR", "BBa_R0040", "TetR repressible promoter", Role.Promoter, "tccctatcagtgatagagattgacatccctatcagtgatagagatactgagcac");
       Component rbs=createDnaComponent(doc, URI.create("https://synbiohub.org/public/igem/BBa_B0034"), "BBa_B0034", "BBa_B0034", "RBS based on Elowitz repressilator", Role.RBS, "aaagaggagaaa");
       Component luxR=createDnaComponent(doc, URI.create("https://synbiohub.org/public/igem/BBa_C0062"), "luxR", "BBa_C0062", "luxR repressor/activator,  (no LVA?)", Role.CDS, "atgaaaaacataaatgccgacgacacatacagaataattaataaaattaaagcttgtagaagcaataatgatattaatcaatgcttatctgatatgactaaaatggtacattgtgaatattatttactcgcgatcatttatcctcattctatggttaaatctgatatttcaatcctagataattaccctaaaaaatggaggcaatattatgatgacgctaatttaataaaatatgatcctatagtagattattctaactccaatcattcaccaattaattggaatatatttgaaaacaatgctgtaaataaaaaatctccaaatgtaattaaagaagcgaaaacatcaggtcttatcactgggtttagtttccctattcatacggctaacaatggcttcggaatgcttagttttgcacattcagaaaaagacaactatatagatagtttatttttacatgcgtgtatgaacataccattaattgttccttctctagttgataattatcgaaaaataaatatagcaaataataaatcaaacaacgatttaaccaaaagagaaaaagaatgtttagcgtgggcatgcgaaggaaaaagctcttgggatatttcaaaaatattaggttgcagtgagcgtactgtcactttccatttaaccaatgcgcaaatgaaactcaatacaacaaaccgctgccaaagtatttctaaagcaattttaacaggagcaattgattgcccatactttaaaaattaataacactgatagtgctagtgtagatcac");
       Component doubleTerminator=createDnaComponent(doc, URI.create("https://synbiohub.org/public/igem/BBa_B0015"), "BBa_B0015", "BBa_B0015", "Double terminator consisting of BBa_B0010 and BBa_B0012", Role.Terminator, null);
       //Component doubleTerminator=createDnaComponent(doc, URI.create("https://synbiohub.org/public/igem/BBa_B0015"), "BBa_B0015", "BBa_B0015", "Double terminator consisting of BBa_B0010 and BBa_B0012", Role.Terminator, "ccaggcatcaaataaaacgaaaggctcagtcgaaagactgggcctttcgttttatctgttgtttgtcggtgaacgctctctactagagtcacactggctcaccttcgggtgggcctttctgcgtttata");
       Component pLuxR=createDnaComponent(doc, URI.create("https://synbiohub.org/public/igem/BBa_R0062"), "lux pR", "BBa_R0062", "Promoter (luxR &amp; HSL regulated -- lux pR)", Role.Promoter, "acctgtaggatcgtacaggtttacgcaagaaaatggtttgttatagtcgaataaa");
       Component BBa_B0010=createDnaComponent(doc, URI.create("https://synbiohub.org/public/igem/BBa_B0010"), "BBa_B0010", "BBa_B0010", "Transcriptional terminator consisting of a 64 bp stem-loop", Role.Terminator, "ccaggcatcaaataaaacgaaaggctcagtcgaaagactgggcctttcgttttatctgttgtttgtcggtgaacgctctc");
       Component BBa_B0012=createDnaComponent(doc, URI.create("https://synbiohub.org/public/igem/BBa_B0012"), "BBa_B0012", "BBa_B0012", "Double terminator consisting of BBa_B0010 and BBa_B0012", Role.Terminator, "tcacactggctcaccttcgggtgggcctttctgcgtttata");
     
       appendComponent(doc, doubleTerminator,BBa_B0010);
       appendComponent(doc, doubleTerminator,BBa_B0012);
       
       appendComponent(doc, popsReceiver,pTetR);
       appendComponent(doc, popsReceiver,rbs);
       appendComponent(doc, popsReceiver,luxR);
       appendComponent(doc, popsReceiver,doubleTerminator);
       appendComponent(doc, popsReceiver,pLuxR);
       
       
       String output=SBOLWriter.write(doc, "Turtle");
       System.out.print(output);
       
       SBOLDocument doc2=SBOLWriter.read(output, "Turtle"); 
       output=SBOLWriter.write(doc2, "RDF/XML-ABBREV");
       System.out.print(output);
       
       SBOLWriter.write(doc2, new File("PoPSReceiver.turtle.sbol"), "Turtle");
       SBOLWriter.write(doc2, new File("PoPSReceiver.rdfxml.sbol"), "RDF/XML-ABBREV");
       SBOLWriter.write(doc2, new File("PoPSReceiver.jsonld.sbol"),"JSON-LD");
       SBOLWriter.write(doc2, new File("PoPSReceiver.rdfjson.sbol"), "RDF/JSON");
       SBOLWriter.write(doc2, new File("PoPSReceiver.ntriples.sbol"), "N-TRIPLES");
       //SBOLWriter.write(doc2, new File("PoPSReceiver.rdfxml_plain.sbol"), "RDF/XML");
           
       System.out.println("done");   
   }
    
    private static void appendComponent(SBOLDocument document, Component parent, Component child) throws SBOLGraphException 
    {
    	int index=1;
    	if (parent.getSubComponents()!=null)
    	{
    		index=parent.getSubComponents().size()+1;
    	}
    	
    	SubComponent subComponent=parent.createSubComponent(URI.create(parent.getUri() + "/subComponent_" +  index), child.getUri());
    	subComponent.setOrientation(Orientation.inline);
    	
    	List<URI> sequences= parent.getSequences();
    	Sequence sequence=null;
    	if (sequences!=null && sequences.size()>0)
    	{
    		 sequence=(Sequence)document.getIdentified(sequences.get(0),Sequence.class);
    	}
    	else
    	{
    		sequence=createSequence(document, parent, Encoding.NucleicAcid, "");	
    	}
    	
    		URI childSequenceUri=child.getSequences().get(0);
    		Sequence childSequence=(Sequence)document.getIdentified(childSequenceUri, Sequence.class);
    		sequence.setElements(sequence.getElements() + childSequence.getElements());
    		int locationIndex=1;
    		if (subComponent.getLocations()!=null)
    		{
    			locationIndex=subComponent.getLocations().size() + 1;
    		}
    		String locationUri=String.format("%s/location_%s", subComponent.getUri().toString(), locationIndex);
        	int start=sequence.getElements().length() + 1;
        	int end=start + childSequence.getElements().length()-1;
        	
        	LocationBuilder builder=new Location.RangeLocationBuilder(URI.create(locationUri), start, end);
        	subComponent.createLocation(builder);
        	
    	
    	
    	
    }
    
    private static Component createDnaComponent(SBOLDocument doc, URI uri, String name, String displayId, String description, URI role, String sequence)
    {
    	Component dna=createComponent(doc, uri, ComponentType.DNA.getUrl(), name, displayId, description, role);
    	if (sequence!=null && sequence.length()>0)
    	{
    		createSequence(doc, dna, Encoding.NucleicAcid, sequence);
    	}
    	return dna;
    }
    
    private static Sequence createSequence(SBOLDocument doc, Component component, Encoding encoding, String elements)
    {
 		Sequence seq=createSequence(doc, append(component.getUri(),"seq"), component.getName() + " sequence", component.getDisplayId() + "_seq", component.getName() + " sequence" , elements, encoding);
 		component.setSequences(Arrays.asList(seq.getUri())); 
 		return seq;
    }
   
    
    private static Component createComponent(SBOLDocument doc, URI uri, URI type, String name, String displayId, String description, URI role)
    {
    	Component component=doc.createComponent(uri, type); 
        setCommonProperties(component, name, displayId, description);
        component.setRoles(Arrays.asList(role));
        
        return component;   
    }
    
    private static URI append(URI uri, String id)
    {
    	return URI.create(String.format("%s/%s", uri,id));
    }
    
    private static URI append(String text, String add)
    {
    	return URI.create(String.format("%s %s", text,add));
    }
    
    
    private static Sequence createSequence(SBOLDocument doc, URI uri, String name, String displayId, String description, String sequence, Encoding encoding)
    {
        Sequence sequenceEntity=doc.createSequence(uri);
        setCommonProperties(sequenceEntity, name, displayId, description);
        if (sequence!=null)
        {
        	sequenceEntity.setElements(sequence);
        }
        sequenceEntity.setEncoding(encoding);
        return sequenceEntity;
        
    }
    
    private static void setCommonProperties(Identified identified, String name, String displayId, String description)
    {
    	identified.setName(name);
    	identified.setDisplayId(displayId);
    	identified.setDescription(description);
    }
}
