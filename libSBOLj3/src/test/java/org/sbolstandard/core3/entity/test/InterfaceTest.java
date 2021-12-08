package org.sbolstandard.core3.entity.test;

import java.io.IOException;
import java.net.URI;
import java.util.Arrays;

import org.sbolstandard.core3.api.SBOLAPI;
import org.sbolstandard.core3.entity.Component;
import org.sbolstandard.core3.entity.Interface;
import org.sbolstandard.core3.entity.SBOLDocument;
import org.sbolstandard.core3.entity.SubComponent;
import org.sbolstandard.core3.io.SBOLFormat;
import org.sbolstandard.core3.io.SBOLIO;
import org.sbolstandard.core3.test.TestUtil;
import org.sbolstandard.core3.util.SBOLGraphException;
import org.sbolstandard.core3.vocabulary.ComponentType;
import org.sbolstandard.core3.vocabulary.Role;

import junit.framework.TestCase;

public class InterfaceTest extends TestCase {
	
	public void testInterface() throws SBOLGraphException, IOException
    {
		String baseUri="https://sbolstandard.org/examples/";
        SBOLDocument doc=new SBOLDocument(URI.create(baseUri));
        
        Component TetR_protein=SBOLAPI.createComponent(doc, "TetR_protein", ComponentType.Protein.getUrl(), "TetR", "TetR protein", Role.TF);
        Component LacI_protein=SBOLAPI.createComponent(doc, "LacI_protein", ComponentType.Protein.getUrl(), "LacI", "LacI protein", Role.TF);
        Component aTC=SBOLAPI.createComponent(doc, "aTC", ComponentType.SimpleChemical.getUrl(), "aTC","aTC", Role.Effector);
        
  
        //LacI producer
        Component LacIProducer=SBOLAPI.createDnaComponent(doc, "LacI_producer", "LacI produce", "LacI producer", Role.EngineeredGene, null); 
        
        
        SubComponent lacISubComponent=SBOLAPI.addSubComponent(LacIProducer, LacI_protein);
        SubComponent tetRSubComponent=SBOLAPI.addSubComponent(LacIProducer, TetR_protein);
        SubComponent aTCSubComponent=SBOLAPI.addSubComponent(LacIProducer, aTC);
        
        Interface compInterface=LacIProducer.createInterface();
        compInterface.setInputs(Arrays.asList(lacISubComponent.getUri(),tetRSubComponent.getUri()));
        compInterface.setOutputs(Arrays.asList(lacISubComponent.getUri()));
        compInterface.setNonDirectionals(Arrays.asList(aTCSubComponent.getUri()));
        
        TestUtil.serialise(doc, "entity/interface", "interface");
      
        System.out.println(SBOLIO.write(doc, SBOLFormat.TURTLE));
        TestUtil.assertReadWrite(doc);
    }

}