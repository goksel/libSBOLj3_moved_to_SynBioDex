package org.sbolstandard;

import java.net.URI;

import org.sbolstandard.sequence.Sequence;
import org.sbolstandard.util.SBOLWriter;
import org.sbolstandard.vocabulary.Encoding;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) throws Exception
    {
        SBOLDocument doc=new SBOLDocument();
        Sequence seq=new Sequence(URI.create("http://myseq.org/seqabc"));
        seq.setName("pTetR");
        seq.setDisplayId("pTetR");
        seq.setDescription("pTetR promoter");
        seq.setElements("aaaatttggg");
        seq.setEncoding(Encoding.NucleicAcid);
        
       doc.addSequence(seq);
       String output=SBOLWriter.toRDFXML(doc, "RDF/XML-ABBREV");
       System.out.print(output);
        
       output=SBOLWriter.toRDFXML(doc, "Turtle");
       System.out.print(output);
       
        
        
   }
}
