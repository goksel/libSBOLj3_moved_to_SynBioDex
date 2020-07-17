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
import org.sbolstandard.io.SBOLWriter;
import org.sbolstandard.util.SBOLGraphException;
import org.sbolstandard.vocabulary.ComponentType;
import org.sbolstandard.vocabulary.DataModel;
import org.sbolstandard.vocabulary.InteractionType;
import org.sbolstandard.vocabulary.ParticipationRole;
import org.sbolstandard.vocabulary.RestrictionType;
import org.sbolstandard.vocabulary.Role;

import junit.framework.TestCase;

public class ToggleSwitch extends TestCase {

	public void testApp() throws SBOLGraphException, IOException
    {
		String baseUri="https://sbolstandard.org/examples/";
        SBOLDocument doc=new SBOLDocument(URI.create(baseUri));
        
        Component TetR_protein=SBOLAPI.createComponent(doc, SBOLAPI.append(baseUri, "TetR_protein"), ComponentType.Protein.getUrl(), "TetR", "TetR_protein", "TetR protein", Role.TF);
        Component LacI_protein=SBOLAPI.createComponent(doc, SBOLAPI.append(baseUri, "LacI_protein"), ComponentType.Protein.getUrl(), "LacI", "LacI_protein", "LacI protein", Role.TF);
        
        //TetR Producer
        Component TetRProducer=SBOLAPI.createDnaComponent(doc, "TetR_producer", "TetR producer", Role.EngineeredGene, null); 
        
        Component pLacI=SBOLAPI.createDnaComponent(doc, "pLacI", "LacI repressible promoter", Role.Promoter, null); 
        Component rbs_tetR=SBOLAPI.createDnaComponent(doc, "rbs_tetR", "RBS", Role.RBS, null);
        Component tetR=SBOLAPI.createDnaComponent(doc, "tetR", "tetR coding sequence", Role.CDS, null);
        Component ter_tetR=SBOLAPI.createDnaComponent(doc, "ter_tetR", "Terminator", Role.Terminator, null);
        //Component TetR_protein=SBOLAPI.createProteinComponent(doc,TetRProducer, SBOLAPI.append(baseUri, "TetR_protein"),"TetR", "TetR_protein", "TetR protein", Role.TF, null);
        
        SBOLAPI.appendComponent(doc, TetRProducer,pLacI);
        SBOLAPI.appendComponent(doc, TetRProducer,rbs_tetR);
        SBOLAPI.appendComponent(doc, TetRProducer,tetR);
        SBOLAPI.appendComponent(doc, TetRProducer,ter_tetR);
        SBOLAPI.addSubComponent(TetRProducer, TetR_protein);
        SBOLAPI.addSubComponent(TetRProducer, LacI_protein);
        
        
        SBOLAPI.createInteraction(Arrays.asList(InteractionType.GeneticProduction),TetRProducer, tetR, Arrays.asList(ParticipationRole.Template), TetR_protein, Arrays.asList(ParticipationRole.Product));  
        SBOLAPI.createInteraction(Arrays.asList(InteractionType.Stimulation),TetRProducer, pLacI, Arrays.asList(ParticipationRole.Inhibited), LacI_protein, Arrays.asList(ParticipationRole.Modifier));
        
       
        //LacI producer
        Component LacIProducer=SBOLAPI.createDnaComponent(doc, "LacI_producer", "LacI producer", Role.EngineeredGene, null); 
        
        Component pTetR=SBOLAPI.createDnaComponent(doc, "pTetR", "TetR repressible promoter", Role.Promoter, null); 
        Component rbs_lacI=SBOLAPI.createDnaComponent(doc, "rbs_lacI", "RBS", Role.RBS, null);
        Component lacI=SBOLAPI.createDnaComponent(doc, "lacI", "lacI coding sequence", Role.CDS, null);
        Component ter_lacI=SBOLAPI.createDnaComponent(doc, "ter_lacI", "Terminator", Role.Terminator, null);
        
        SBOLAPI.appendComponent(doc, LacIProducer,pTetR);
        SBOLAPI.appendComponent(doc, LacIProducer,rbs_lacI);
        SBOLAPI.appendComponent(doc, LacIProducer,lacI);
        SBOLAPI.appendComponent(doc, LacIProducer,ter_lacI);
        SBOLAPI.addSubComponent (LacIProducer, LacI_protein);
        SBOLAPI.addSubComponent(LacIProducer, TetR_protein);
        
        
        SBOLAPI.createInteraction(Arrays.asList(InteractionType.GeneticProduction),LacIProducer, lacI, Arrays.asList(ParticipationRole.Template), LacI_protein, Arrays.asList(ParticipationRole.Product));  
        SBOLAPI.createInteraction(Arrays.asList(InteractionType.Inhibition),LacIProducer, pTetR, Arrays.asList(ParticipationRole.Inhibited), TetR_protein, Arrays.asList(ParticipationRole.Modifier));
        
        
        //Toggle Switch
        Component toggleSwitch=SBOLAPI.createComponent(doc, SBOLAPI.append(baseUri, "toggle_switch"), ComponentType.FunctionalEntity.getUrl(), "Toggle Switch", "toggle_switch", "Toggle Switch genetic circuit", null);
       
        /*SubComponent TetRSubComponent=SBOLAPI.addSubComponent(toggleSwitch, TetR_protein);
        SubComponent LacISubComponent=SBOLAPI.addSubComponent(toggleSwitch, LacI_protein);
        
        
        SBOLAPI.mapTo(LacISubComponent, toggleSwitch, LacIProducer, LacI_protein);
        SBOLAPI.mapTo(LacISubComponent, toggleSwitch, TetRProducer, LacI_protein);
        SBOLAPI.mapTo(TetRSubComponent, toggleSwitch, LacIProducer, TetR_protein);
        SBOLAPI.mapTo(TetRSubComponent, toggleSwitch, TetRProducer, TetR_protein);
        */
        SBOLAPI.addSubComponent(toggleSwitch, LacIProducer);
        SBOLAPI.addSubComponent(toggleSwitch, TetRProducer);
        
        SBOLAPI.mapTo(toggleSwitch, LacIProducer, LacI_protein, TetRProducer,LacI_protein);
        SBOLAPI.mapTo(toggleSwitch, LacIProducer, TetR_protein, TetRProducer,TetR_protein);
        
      
        
        String output=SBOLWriter.write(doc, "Turtle");
        System.out.print(output);
        
        SBOLDocument doc2=SBOLWriter.read(output, "Turtle"); 
        output=SBOLWriter.write(doc2, "RDF/XML-ABBREV");
        System.out.print(output);
        
        TestUtil.serialise(doc2, "toggle_switch", "toggle_switch");     
        System.out.println("done");   
    }
	 
	
	
	
	
	
    
}
