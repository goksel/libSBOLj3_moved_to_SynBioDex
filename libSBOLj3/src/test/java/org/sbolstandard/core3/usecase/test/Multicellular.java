package org.sbolstandard.core3.usecase.test;

import java.io.IOException;
import java.net.URI;
import java.util.Arrays;

import org.sbolstandard.core3.api.SBOLAPI;
import org.sbolstandard.core3.entity.Component;
import org.sbolstandard.core3.entity.Interaction;
import org.sbolstandard.core3.entity.SBOLDocument;
import org.sbolstandard.core3.entity.SubComponent;
import org.sbolstandard.core3.io.SBOLFormat;
import org.sbolstandard.core3.io.SBOLIO;
import org.sbolstandard.core3.test.TestUtil;
import org.sbolstandard.core3.util.SBOLGraphException;
import org.sbolstandard.core3.vocabulary.ComponentType;
import org.sbolstandard.core3.vocabulary.InteractionType;
import org.sbolstandard.core3.vocabulary.ParticipationRole;
import org.sbolstandard.core3.vocabulary.RestrictionType;
import org.sbolstandard.core3.vocabulary.Role;

import junit.framework.TestCase;

public class Multicellular extends TestCase {

	public void testApp() throws SBOLGraphException, IOException
    {
		String baseUri="https://sbolstandard.org/examples/";
        SBOLDocument doc=new SBOLDocument(URI.create(baseUri));
        
        Component multicellularSystem=SBOLAPI.createComponent(doc, "MulticellularSystem", ComponentType.FunctionalEntity.getUrl(), "MulticellularSystem", "Multicellular System", Role.FunctionalCompartment);
        Component senderSystem=SBOLAPI.createComponent(doc, "SenderSystem", ComponentType.FunctionalEntity.getUrl(), "SenderSystem", "Sender System", Role.FunctionalCompartment);
        Component receiverSystem=SBOLAPI.createComponent(doc, "ReceiverSystem", ComponentType.FunctionalEntity.getUrl(), "ReceiverSystem", "Receiver System", Role.FunctionalCompartment);
        Component senderCell=SBOLAPI.createComponent(doc, "OrganismA", ComponentType.OptionalComponentType.Cell.getUri(), "OrganismA", "Organism A", Role.PhysicalCompartment);
        Component receiverCell=SBOLAPI.createComponent(doc, "OrganismB", ComponentType.OptionalComponentType.Cell.getUri(), "OrganismB", "Organism B", Role.PhysicalCompartment);
        Component AHL=SBOLAPI.createComponent(doc, "AHL", ComponentType.SimpleChemical.getUrl(), "AHL", "AHL", Role.Effector);
       
        SBOLAPI.createConstraint(senderSystem, senderCell, AHL, RestrictionType.Topology.contains);
        SBOLAPI.createConstraint(receiverSystem, receiverCell, AHL, RestrictionType.Topology.contains);       
        
        SubComponent senderSubComponent=SBOLAPI.addSubComponent(multicellularSystem, senderSystem);
        SubComponent receiverSubComponent=SBOLAPI.addSubComponent(multicellularSystem, receiverSystem);
        SBOLAPI.mapTo(multicellularSystem, senderSystem, AHL, receiverSystem,AHL);
            
        
        //AHL Producer
        Component AHLProducer=SBOLAPI.createDnaComponent(doc, "AHL_producer", "AHL producer", "AHL producer", Role.EngineeredGene, null);  
        Component pConstLuxI=SBOLAPI.createDnaComponent(doc, "pConstLuxI", "pConstLuxI","Constitutive promoter", Role.Promoter, null); 
        Component rbs_luxI=SBOLAPI.createDnaComponent(doc, "rbs_luxI","rbs", "RBS", Role.RBS, null);
        Component luxI=SBOLAPI.createDnaComponent(doc, "luxI","luxI", "luxI coding sequence", Role.CDS, null);
        Component ter_luxI=SBOLAPI.createDnaComponent(doc, "ter_luxI","luxI terminator", "Terminator", Role.Terminator, null);
        
        SBOLAPI.appendComponent(doc, AHLProducer,pConstLuxI);
        SBOLAPI.appendComponent(doc, AHLProducer,rbs_luxI);
        SBOLAPI.appendComponent(doc, AHLProducer,luxI);
        SBOLAPI.appendComponent(doc, AHLProducer,ter_luxI);
        SBOLAPI.addSubComponent(AHLProducer, AHL);
        SBOLAPI.createInteraction(Arrays.asList(InteractionType.GeneticProduction),AHLProducer, luxI, Arrays.asList(ParticipationRole.Template), AHL, Arrays.asList(ParticipationRole.Product));       
        
        //AHL Receiver
        Component AHLReceiver=SBOLAPI.createDnaComponent(doc, "AHL_receiver", "AHL receiver","AHL receiver", Role.EngineeredGene, null);  
        Component pConstLuxR=SBOLAPI.createDnaComponent(doc, "pConstLuxR","pLuxR", "Constituve promoter", Role.Promoter, null); 
        Component rbs_luxR=SBOLAPI.createDnaComponent(doc, "rbs_luxR", "rbs", "RBS", Role.RBS, null);
        Component luxR=SBOLAPI.createDnaComponent(doc, "luxR", "luxR", "luxR coding sequence", Role.CDS, null);
        Component ter_luxR=SBOLAPI.createDnaComponent(doc, "ter_luxR", "luxR terminator", "Terminator", Role.Terminator, null);
        Component pLuxR=SBOLAPI.createDnaComponent(doc, "pLuxR", "pLuxR", "LuxR inducible promoter", Role.Promoter, null); 
        Component rbs_gfp=SBOLAPI.createDnaComponent(doc, "rbs_gfp", "rbs", "RBS", Role.RBS, null);
        Component gfp=SBOLAPI.createDnaComponent(doc, "gfp", "gfp", "gfp coding sequence", Role.CDS, null);
        Component ter_gfp=SBOLAPI.createDnaComponent(doc, "ter_gfp", "gfp terminator", "Terminator", Role.Terminator, null);
        Component LuxR=SBOLAPI.createComponent(doc, "LuxR_protein", ComponentType.Protein.getUrl(), "LuxR_protein", "LuxR", Role.TF);
        Component GFP=SBOLAPI.createComponent(doc, "GFP_protein", ComponentType.Protein.getUrl(), "GFP", "GFP", null);
        Component LuxR_AHL=SBOLAPI.createComponent(doc, "LuxR_AHL", ComponentType.Protein.getUrl(), "LuxR_AHL", "LuxR_AHL complex", Role.TF);
         
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
        Interaction interaction= AHLReceiver.createInteraction(Arrays.asList(InteractionType.NonCovalentBinding));
    	SBOLAPI.createParticipation(interaction, Arrays.asList(ParticipationRole.Reactant), LuxRProteinSubComponent);
    	SBOLAPI.createParticipation(interaction, Arrays.asList(ParticipationRole.Reactant), AHLSubComponent);
    	SBOLAPI.createParticipation(interaction, Arrays.asList(ParticipationRole.Product), LuxRAHLSubComponent);
       
        
    	SBOLAPI.addSubComponent(senderSystem, AHLProducer);
    	SBOLAPI.addSubComponent(receiverSystem, AHLReceiver);
    	
    	SBOLAPI.createConstraint(senderSystem, senderCell, AHLProducer, RestrictionType.Topology.contains);
    	SBOLAPI.createConstraint(receiverSystem, receiverCell, AHLReceiver, RestrictionType.Topology.contains);
    	
    	SBOLAPI.mapTo(senderSystem, AHLProducer, AHL, AHL);
    	SBOLAPI.mapTo(receiverSystem, AHLReceiver, AHL, AHL);
    	
        String output=SBOLIO.write(doc, SBOLFormat.TURTLE);
        System.out.print(output);
        
        SBOLDocument doc2=SBOLIO.read(output, SBOLFormat.TURTLE); 
        output=SBOLIO.write(doc2,SBOLFormat.RDFXML);
        System.out.print(output);
        
        TestUtil.serialise(doc2, "multicellular", "multicellular");    
        
        TestUtil.assertReadWrite(doc);
        System.out.println("done");   
        
    }
	 
	
	
	
	
	
    
}
