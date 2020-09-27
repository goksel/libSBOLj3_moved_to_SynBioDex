package org.sbolstandard.usecase;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.sbolstandard.TestUtil;
import org.sbolstandard.api.SBOLAPI;
import org.sbolstandard.entity.Component;
import org.sbolstandard.entity.ComponentReference;
import org.sbolstandard.entity.SBOLDocument;
import org.sbolstandard.entity.SubComponent;
import org.sbolstandard.io.SBOLIO;
import org.sbolstandard.util.SBOLGraphException;
import org.sbolstandard.vocabulary.ComponentType;
import org.sbolstandard.vocabulary.DataModel;
import org.sbolstandard.vocabulary.InteractionType;
import org.sbolstandard.vocabulary.ParticipationRole;
import org.sbolstandard.vocabulary.RestrictionType;
import org.sbolstandard.vocabulary.Role;

import junit.framework.TestCase;

public class MulticellularSimple extends TestCase {

	public void testApp() throws SBOLGraphException, IOException
    {
		String baseUri="https://sbolstandard.org/examples/";
        SBOLDocument doc=new SBOLDocument(URI.create(baseUri));
        
        Component multicellularSystem=SBOLAPI.createComponent(doc, SBOLAPI.append(baseUri, "MulticellularSystem"), ComponentType.FunctionalEntity.getUrl(), "MulticellularSystem", "Multicellular System", Role.FunctionalCompartment);
        Component senderSystem=SBOLAPI.createComponent(doc, SBOLAPI.append(baseUri, "SenderSystem"), ComponentType.FunctionalEntity.getUrl(), "SenderSystem", "Sender System", Role.FunctionalCompartment);
        Component receiverSystem=SBOLAPI.createComponent(doc, SBOLAPI.append(baseUri, "ReceiverSystem"), ComponentType.FunctionalEntity.getUrl(), "ReceiverSystem", "Receiver System", Role.FunctionalCompartment);
        Component senderCell=SBOLAPI.createComponent(doc, SBOLAPI.append(baseUri, "OrganismA"), ComponentType.Cell.getUrl(), "OrganismA","Organism A", Role.PhysicalCompartment);
        Component receiverCell=SBOLAPI.createComponent(doc, SBOLAPI.append(baseUri, "OrganismB"), ComponentType.Cell.getUrl(), "OrganismB", "Organism B", Role.PhysicalCompartment);
        Component ahl=SBOLAPI.createComponent(doc, SBOLAPI.append(baseUri, "AHL"), ComponentType.SimpleChemical.getUrl(), "AHL", "AHL", Role.Effector);
       
        SBOLAPI.createConstraint(senderSystem, senderCell, ahl, RestrictionType.Topology.contains);
        SBOLAPI.createConstraint(receiverSystem, receiverCell, ahl, RestrictionType.Topology.contains);       
        
        SubComponent senderSubComponent=SBOLAPI.addSubComponent(multicellularSystem, senderSystem);
        SubComponent receiverSubComponent=SBOLAPI.addSubComponent(multicellularSystem, receiverSystem);
        SBOLAPI.mapTo(multicellularSystem, senderSystem, ahl, receiverSystem,ahl);
                
        String output=SBOLIO.write(doc, "Turtle");
        System.out.print(output);
        
        SBOLDocument doc2=SBOLIO.read(output, "Turtle"); 
        output=SBOLIO.write(doc2, "RDF/XML-ABBREV");
        System.out.print(output);
        
        TestUtil.serialise(doc2, "multicellular_simple", "multicellular_simple");     
        System.out.println("done");   
        TestUtil.assertReadWrite(doc);
    }
	 
	
	
	
	
	
    
}
