package org.sbolstandard;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.sbolstandard.Location.CutLocationBuilder;
import org.sbolstandard.Location.LocationBuilder;
import org.sbolstandard.vocabulary.ComponentType;
import org.sbolstandard.vocabulary.Encoding;
import org.sbolstandard.vocabulary.Orientation;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) throws Exception
    {
//        SBOLDocument doc=new SBOLDocument();
//        
//       Sequence seq=doc.createSequence(URI.create("http://myseq.org/pTetR_seq"));
//       seq.setName("pTetR_seq");
//       seq.setName("pTetR_seq2");
//       
//       seq.setDisplayId("pTetR_seq");
//       seq.setDescription("pTetR promoter sequence");
//       seq.setElements("aaaatttggg");
//       seq.setEncoding(Encoding.NucleicAcid);
//       
//       Component design=doc.createComponent(URI.create("http://sbolstandard.org/popsReceiver"), ComponentType.DNA.getUrl()); 
//       
//       
//       Component component=doc.createComponent(URI.create("http://sbolstandard.org/pTetR"), ComponentType.DNA.getUrl()); 
//       component.setName("pTetR");
//       component.setDisplayId("pTetR");
//       component.setDescription("pTetR promoter");
//       List<URI> seqs=new ArrayList<URI>();
//       seqs.add(seq.getUri());
//       component.setSequences(seqs);
//       
//       SubComponent subComp1=design.createSubComponent(URI.create("http://sbolstandard.org/pTetR/sub1"),component.getUri());
//       subComp1.setRoles(Arrays.asList(URI.create("http://sbolstanddard.org/role/testrole1")));
//       subComp1.setOrientation(Orientation.inline);
//       subComp1.setRoleIntegration(URI.create("http://sbolstandard.org/role/testroleintegration1"));
//       subComp1.createLocation(new Location.CutLocationBuilder(URI.create("http://sbolstandard.org/pTetR/sub1/loc1"),1));
//       
//       design.createComponentReference(URI.create("http://sbolstandard.org/pTetR/compRef1"), subComp1.getUri(), component.getUri());
//       String output=SBOLWriter.write(doc, "RDF/XML-ABBREV");
//       System.out.print(output);
//       
//       LocalSubComponent lsComp= design.createLocalSubComponent(URI.create("http://sbolstandard.org/pTetR/localSubComp1"),Arrays.asList(URI.create("http://sbolstandard.org/testType")));
//       lsComp.createLocation(new Location.CutLocationBuilder(URI.create("http://sbolstandard.org/pTetR/localSubComp1/loc1"),2));
//       output=SBOLWriter.write(doc, "Turtle");
//       System.out.print(output);
//       
//       CutLocationBuilder builder=new Location.CutLocationBuilder(URI.create("http://sbolstandard.org/pTetR/sf1/loc1"), 3);
//       design.createSequenceFeature(URI.create("http://sbolstandard.org/pTetR/sf1"),Arrays.asList(builder));
//       output=SBOLWriter.write(doc, "Turtle");
//       System.out.print(output);
//       
//       
//       
//       SBOLDocument doc2=SBOLWriter.read(output, "Turtle");
//       output=SBOLWriter.write(doc2, "RDF/XML-ABBREV");
//       System.out.print(output);
//       System.out.println("done");   
   }
    
    private static Component createDnaComponent(SBOLDocument doc, URI uri, URI type, String name, String displayId, String description, URI role, String sequence)
    {
    	Component dna=createComponent(doc, uri, type, name, displayId, description, role);
    	Sequence seq=createSequence(doc, append(uri,"seq"), name + " sequence", displayId + "_seq", description + "" , sequence, Encoding.NucleicAcid);
    	dna.setSequences(Arrays.asList(seq.getUri())); 
    	return dna;
    }
    
    
    private static Component createComponent(SBOLDocument doc, URI uri, URI type, String name, String displayId, String description, URI role)
    {
    	Component component=doc.createComponent(uri, Arrays.asList(type)); 
        setCommonProperties(component, name, displayId, description);
        component.setRoles(Arrays.asList(role));
        
        return component;   
    }
    
    private static URI append(URI uri, String id)
    {
    	return URI.create(String.format("%s/%s", uri,id));
    }
    
    private static Sequence createSequence(SBOLDocument doc, URI uri, String name, String displayId, String description, String sequence, Encoding encoding)
    {
        Sequence sequenceEntity=doc.createSequence(uri);
        setCommonProperties(sequenceEntity, name, displayId, description);
        sequenceEntity.setElements(sequence);
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
