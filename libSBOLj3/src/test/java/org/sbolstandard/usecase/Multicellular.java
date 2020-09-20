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
import org.sbolstandard.entity.Interaction;
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

public class Multicellular extends TestCase {

	public void testApp() throws SBOLGraphException, IOException
    {
		String baseUri="https://sbolstandard.org/examples/";
        SBOLDocument doc=new SBOLDocument(URI.create(baseUri));
        
        Component multicellularSystem=SBOLAPI.createComponent(doc, SBOLAPI.append(baseUri, "MulticellularSystem"), ComponentType.FunctionalEntity.getUrl(), "MulticellularSystem", "MulticellularSystem", "Multicellular System", Role.FunctionalCompartment);
        Component senderSystem=SBOLAPI.createComponent(doc, SBOLAPI.append(baseUri, "SenderSystem"), ComponentType.FunctionalEntity.getUrl(), "SenderSystem", "SenderSystem", "Sender System", Role.FunctionalCompartment);
        Component receiverSystem=SBOLAPI.createComponent(doc, SBOLAPI.append(baseUri, "ReceiverSystem"), ComponentType.FunctionalEntity.getUrl(), "ReceiverSystem", "ReceiverSystem", "Receiver System", Role.FunctionalCompartment);
        Component senderCell=SBOLAPI.createComponent(doc, SBOLAPI.append(baseUri, "OrganismA"), ComponentType.Cell.getUrl(), "OrganismA", "OrganismA", "Organism A", Role.PhysicalCompartment);
        Component receiverCell=SBOLAPI.createComponent(doc, SBOLAPI.append(baseUri, "OrganismB"), ComponentType.Cell.getUrl(), "OrganismB", "OrganismB", "Organism B", Role.PhysicalCompartment);
        Component AHL=SBOLAPI.createComponent(doc, SBOLAPI.append(baseUri, "AHL"), ComponentType.SimpleChemical.getUrl(), "AHL", "AHL", "AHL", Role.Effector);
       
        SBOLAPI.createConstraint(senderSystem, senderCell, AHL, RestrictionType.Topology.contains);
        SBOLAPI.createConstraint(receiverSystem, receiverCell, AHL, RestrictionType.Topology.contains);       
        
        SubComponent senderSubComponent=SBOLAPI.addSubComponent(multicellularSystem, senderSystem);
        SubComponent receiverSubComponent=SBOLAPI.addSubComponent(multicellularSystem, receiverSystem);
        SBOLAPI.mapTo(multicellularSystem, senderSystem, AHL, receiverSystem,AHL);
            
        
        //AHL Producer
        Component AHLProducer=SBOLAPI.createDnaComponent(doc, "AHL_producer", "AHL producer", Role.EngineeredGene, null);  
        Component pConstLuxI=SBOLAPI.createDnaComponent(doc, "pConstLuxI", "Constituve promoter", Role.Promoter, null); 
        Component rbs_luxI=SBOLAPI.createDnaComponent(doc, "rbs_luxI", "RBS", Role.RBS, null);
        Component luxI=SBOLAPI.createDnaComponent(doc, "luxI", "luxI coding sequence", Role.CDS, null);
        Component ter_luxI=SBOLAPI.createDnaComponent(doc, "ter_luxI", "Terminator", Role.Terminator, null);
        
        SBOLAPI.appendComponent(doc, AHLProducer,pConstLuxI);
        SBOLAPI.appendComponent(doc, AHLProducer,rbs_luxI);
        SBOLAPI.appendComponent(doc, AHLProducer,luxI);
        SBOLAPI.appendComponent(doc, AHLProducer,ter_luxI);
        SBOLAPI.addSubComponent(AHLProducer, AHL);
        SBOLAPI.createInteraction(Arrays.asList(InteractionType.GeneticProduction),AHLProducer, luxI, Arrays.asList(ParticipationRole.Template), AHL, Arrays.asList(ParticipationRole.Product));       
        
        //AHL Receiver
        Component AHLReceiver=SBOLAPI.createDnaComponent(doc, "AHL_receiver", "AHL receiver", Role.EngineeredGene, null);  
        Component pConstLuxR=SBOLAPI.createDnaComponent(doc, "pConstLuxR", "Constituve promoter", Role.Promoter, null); 
        Component rbs_luxR=SBOLAPI.createDnaComponent(doc, "rbs_luxR", "RBS", Role.RBS, null);
        Component luxR=SBOLAPI.createDnaComponent(doc, "luxR", "luxR coding sequence", Role.CDS, null);
        Component ter_luxR=SBOLAPI.createDnaComponent(doc, "ter_luxR", "Terminator", Role.Terminator, null);
        Component pLuxR=SBOLAPI.createDnaComponent(doc, "pLuxR", "LuxR inducible promoter", Role.Promoter, null); 
        Component rbs_gfp=SBOLAPI.createDnaComponent(doc, "rbs_gfp", "RBS", Role.RBS, null);
        Component gfp=SBOLAPI.createDnaComponent(doc, "gfp", "gfp coding sequence", Role.CDS, null);
        Component ter_gfp=SBOLAPI.createDnaComponent(doc, "ter_gfp", "Terminator", Role.Terminator, null);
        Component LuxR=SBOLAPI.createComponent(doc, SBOLAPI.append(baseUri, "LuxR_protein"), ComponentType.Protein.getUrl(), "LuxR", "LuxR_protein", "LuxR", Role.TF);
        Component GFP=SBOLAPI.createComponent(doc, SBOLAPI.append(baseUri, "GFP_protein"), ComponentType.Protein.getUrl(), "GFP", "GFP", "GFP", null);
        Component LuxR_AHL=SBOLAPI.createComponent(doc, SBOLAPI.append(baseUri, "LuxR_AHL"), ComponentType.Protein.getUrl(), "LuxR_AHL", "LuxR_AHL", "LuxR_AHL complex", Role.TF);
         
        SBOLAPI.appendComponent(doc, AHLReceiver,pConstLuxR);
        SBOLAPI.appendComponent(doc, AHLReceiver,rbs_luxR);
        SBOLAPI.appendComponent(doc, AHLReceiver,luxR);
        SBOLAPI.appendComponent(doc, AHLReceiver,ter_luxR);
        SBOLAPI.appendComponent(doc, AHLReceiver,pLuxR);
        SBOLAPI.appendComponent(doc, AHLReceiver,rbs_gfp);
        SBOLAPI.appendComponent(doc, AHLReceiver,gfp);
        SBOLAPI.appendComponent(doc, AHLReceiver,ter_gfp);        
        SubComponent AHLSubComponent=SBOLAPI.addSubComponent(AHLReceiver, AHL);
        SubComponent LuxRProteinSubComponent=SBOLAPI.addSubComponent(AHLReceiver, LuxR);
        SBOLAPI.addSubComponent(AHLReceiver, GFP);
        SubComponent LuxRAHLSubComponent=SBOLAPI.addSubComponent(AHLReceiver, LuxR_AHL);
        
        
        SBOLAPI.createInteraction(Arrays.asList(InteractionType.GeneticProduction),AHLReceiver, luxR, Arrays.asList(ParticipationRole.Template), LuxR, Arrays.asList(ParticipationRole.Product));  
        SBOLAPI.createInteraction(Arrays.asList(InteractionType.GeneticProduction),AHLReceiver, gfp, Arrays.asList(ParticipationRole.Template), GFP, Arrays.asList(ParticipationRole.Product));  
        SBOLAPI.createInteraction(Arrays.asList(InteractionType.Stimulation),AHLReceiver, pLuxR, Arrays.asList(ParticipationRole.Stimulated), LuxR_AHL, Arrays.asList(ParticipationRole.Stimulator));
                
    	//LuxR AHL binding
        String localName=SBOLAPI.createLocalName(DataModel.Interaction.uri, AHLReceiver.getInteractions()); 
    	Interaction interaction= AHLReceiver.createInteraction(SBOLAPI.append(AHLReceiver.getUri(), localName), Arrays.asList(InteractionType.NonCovalentBinding));
    	SBOLAPI.createParticipation(interaction, Arrays.asList(ParticipationRole.Reactant), LuxRProteinSubComponent);
    	SBOLAPI.createParticipation(interaction, Arrays.asList(ParticipationRole.Reactant), AHLSubComponent);
    	SBOLAPI.createParticipation(interaction, Arrays.asList(ParticipationRole.Product), LuxRAHLSubComponent);
       
        
    	SBOLAPI.addSubComponent(senderSystem, AHLProducer);
    	SBOLAPI.addSubComponent(receiverSystem, AHLReceiver);
    	
    	SBOLAPI.createConstraint(senderSystem, senderCell, AHLProducer, RestrictionType.Topology.contains);
    	SBOLAPI.createConstraint(receiverSystem, receiverCell, AHLReceiver, RestrictionType.Topology.contains);
    	
    	SBOLAPI.mapTo(senderSystem, AHLProducer, AHL, AHL);
    	SBOLAPI.mapTo(receiverSystem, AHLReceiver, AHL, AHL);
    	
        String output=SBOLIO.write(doc, "Turtle");
        System.out.print(output);
        
        SBOLDocument doc2=SBOLIO.read(output, "Turtle"); 
        output=SBOLIO.write(doc2, "RDF/XML-ABBREV");
        System.out.print(output);
        
        TestUtil.serialise(doc2, "multicellular", "multicellular");    
        
        TestUtil.assertReadWrite(doc);
        System.out.println("done");   
        
    }
	 
	
	
	
	
	
    
}
