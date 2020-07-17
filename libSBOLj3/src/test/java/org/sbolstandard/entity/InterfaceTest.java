package org.sbolstandard.entity;

import java.io.IOException;
import java.net.URI;
import java.util.Arrays;

import org.sbolstandard.TestUtil;
import org.sbolstandard.api.SBOLAPI;
import org.sbolstandard.io.SBOLWriter;
import org.sbolstandard.util.SBOLGraphException;
import org.sbolstandard.vocabulary.ComponentType;
import org.sbolstandard.vocabulary.Role;

import junit.framework.TestCase;

public class InterfaceTest extends TestCase {
	
	public void testInterface() throws SBOLGraphException, IOException
    {
		String baseUri="https://sbolstandard.org/examples/";
        SBOLDocument doc=new SBOLDocument(URI.create(baseUri));
        
        Component TetR_protein=SBOLAPI.createComponent(doc, SBOLAPI.append(baseUri, "TetR_protein"), ComponentType.Protein.getUrl(), "TetR", "TetR_protein", "TetR protein", Role.TF);
        Component LacI_protein=SBOLAPI.createComponent(doc, SBOLAPI.append(baseUri, "LacI_protein"), ComponentType.Protein.getUrl(), "LacI", "LacI_protein", "LacI protein", Role.TF);
        Component aTC=SBOLAPI.createComponent(doc, SBOLAPI.append(baseUri, "aTC"), ComponentType.SimpleChemical.getUrl(), "aTC", "aTC", "aTC", Role.Effector);
        
  
        //LacI producer
        Component LacIProducer=SBOLAPI.createDnaComponent(doc, "LacI_producer", "LacI producer", Role.EngineeredGene, null); 
        
        
        SubComponent lacISubComponent=SBOLAPI.addSubComponent(LacIProducer, LacI_protein);
        SubComponent tetRSubComponent=SBOLAPI.addSubComponent(LacIProducer, TetR_protein);
        SubComponent aTCSubComponent=SBOLAPI.addSubComponent(LacIProducer, aTC);
        
        URI uri=SBOLAPI.append(LacIProducer.getUri(),"interface");
        Interface compInterface=LacIProducer.createInterface(uri);
        compInterface.setInputs(Arrays.asList(lacISubComponent.getUri(),tetRSubComponent.getUri()));
        compInterface.setOutputs(Arrays.asList(lacISubComponent.getUri()));
        compInterface.setNonDirectionals(Arrays.asList(aTCSubComponent.getUri()));
        
        TestUtil.serialise(doc, "entity/interface", "interface");
      
        System.out.println(SBOLWriter.write(doc, "Turtle"));
    }

}
