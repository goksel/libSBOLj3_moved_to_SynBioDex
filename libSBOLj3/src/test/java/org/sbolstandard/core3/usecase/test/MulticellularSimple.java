package org.sbolstandard.core3.usecase.test;

import java.io.IOException;
import java.net.URI;

import org.sbolstandard.core3.api.SBOLAPI;
import org.sbolstandard.core3.entity.*;
import org.sbolstandard.core3.io.SBOLFormat;
import org.sbolstandard.core3.io.SBOLIO;
import org.sbolstandard.core3.test.TestUtil;
import org.sbolstandard.core3.util.SBOLGraphException;
import org.sbolstandard.core3.vocabulary.*;

import junit.framework.TestCase;

public class MulticellularSimple extends TestCase {

	public void testMulticellularSimple() throws SBOLGraphException, IOException
    {
		String baseUri="https://sbolstandard.org/examples/";
        SBOLDocument doc=new SBOLDocument(URI.create(baseUri));
        
        Component multicellularSystem=SBOLAPI.createComponent(doc, "MulticellularSystem", ComponentType.FunctionalEntity.getUri(), "MulticellularSystem", "Multicellular System", Role.FunctionalCompartment);
        Component senderSystem=SBOLAPI.createComponent(doc, "SenderSystem", ComponentType.FunctionalEntity.getUri(), "SenderSystem", "Sender System", Role.FunctionalCompartment);
        Component receiverSystem=SBOLAPI.createComponent(doc, "ReceiverSystem", ComponentType.FunctionalEntity.getUri(), "ReceiverSystem", "Receiver System", Role.FunctionalCompartment);
        Component senderCell=SBOLAPI.createComponent(doc, "OrganismA", ComponentType.OptionalComponentType.Cell.getUri(), "OrganismA","Organism A", Role.PhysicalCompartment);
        Component receiverCell=SBOLAPI.createComponent(doc, "OrganismB", ComponentType.OptionalComponentType.Cell.getUri(), "OrganismB", "Organism B", Role.PhysicalCompartment);
        Component ahl=SBOLAPI.createComponent(doc, "AHL", ComponentType.SimpleChemical.getUri(), "AHL", "AHL", Role.Effector);
       
        SBOLAPI.createConstraint(senderSystem, senderCell, ahl, RestrictionType.TopologyRestriction.contains.getUri());
        SBOLAPI.createConstraint(receiverSystem, receiverCell, ahl, RestrictionType.TopologyRestriction.contains.getUri());       
        
        SubComponent senderSubComponent=SBOLAPI.addSubComponent(multicellularSystem, senderSystem);
        SubComponent receiverSubComponent=SBOLAPI.addSubComponent(multicellularSystem, receiverSystem);
        SBOLAPI.mapTo(multicellularSystem, senderSystem, ahl, receiverSystem,ahl);
                
        String output=SBOLIO.write(doc, SBOLFormat.TURTLE);
        System.out.print(output);
        
        SBOLDocument doc2=SBOLIO.read(output, SBOLFormat.TURTLE); 
        output=SBOLIO.write(doc2, SBOLFormat.RDFXML);
        System.out.print(output);
        
        TestUtil.serialise(doc2, "multicellular_simple", "multicellular_simple");     
        System.out.println("done");   
        TestUtil.assertReadWrite(doc);
    }
	 
	
	
	
	
	
    
}
