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
import org.sbolstandard.core3.vocabulary.Role;

import junit.framework.TestCase;

public class ToggleSwitch extends TestCase {

	public void testToggleSwitch() throws SBOLGraphException, IOException
    {
		String baseUri="https://sbolstandard.org/examples/";
        SBOLDocument doc=new SBOLDocument(URI.create(baseUri));
        
        Component TetR_protein=SBOLAPI.createComponent(doc, SBOLAPI.append(baseUri, "TetR_protein"), ComponentType.Protein.getUri(), "TetR", "TetR protein", Role.TF);
        Component LacI_protein=SBOLAPI.createComponent(doc, SBOLAPI.append(baseUri, "LacI_protein"), ComponentType.Protein.getUri(), "LacI",  "LacI protein", Role.TF);
        Component IPTG=SBOLAPI.createComponent(doc, SBOLAPI.append(baseUri, "IPTG"), ComponentType.SimpleChemical.getUri(), "IPTG", "IPTG", Role.Effector);
        Component aTC=SBOLAPI.createComponent(doc, SBOLAPI.append(baseUri, "aTC"), ComponentType.SimpleChemical.getUri(), "aTC", "aTC", Role.Effector);
        Component IPTG_LacI=SBOLAPI.createComponent(doc, SBOLAPI.append(baseUri, "IPTG_LacI"), ComponentType.NoncovalentComplex.getUri(), "IPTG_LacI", "IPTG_LacI complex", null);
        Component atC_TetR=SBOLAPI.createComponent(doc, SBOLAPI.append(baseUri, "atC_TetR"), ComponentType.NoncovalentComplex.getUri(), "atC_TetR", "atC_TetR complex", null);
        Component GFP=SBOLAPI.createComponent(doc, SBOLAPI.append(baseUri, "GFP_protein"), ComponentType.Protein.getUri(), "GFP", "GFP", null);
                 
        
        //TetR Producer
        Component TetRProducer=SBOLAPI.createDnaComponent(doc, "TetR_producer","TetR device", "TetR producer", Role.EngineeredGene, null); 
        
        Component pLacI=SBOLAPI.createDnaComponent(doc, "pLacI", "pLacI promoter", "LacI repressible promoter", Role.Promoter, null); 
        Component rbs_tetR=SBOLAPI.createDnaComponent(doc, "rbs_tetR", "rbs", "tetR RBS", Role.RBS, null);
        Component tetR=SBOLAPI.createDnaComponent(doc, "tetR", "tetR", "tetR coding sequence", Role.CDS, null);
        Component ter_tetR=SBOLAPI.createDnaComponent(doc, "ter_tetR", "tetR terminator", "Terminator", Role.Terminator, null);
        //Component TetR_protein=SBOLAPI.createProteinComponent(doc,TetRProducer, SBOLAPI.append(baseUri, "TetR_protein"),"TetR", "TetR_protein", "TetR protein", Role.TF, null);
        
        SBOLAPI.appendComponent(doc, TetRProducer,pLacI);
        SBOLAPI.appendComponent(doc, TetRProducer,rbs_tetR);
        SBOLAPI.appendComponent(doc, TetRProducer,tetR);
        SBOLAPI.appendComponent(doc, TetRProducer,ter_tetR);
        SBOLAPI.addSubComponent(TetRProducer, TetR_protein);
        SubComponent LacI_protein_subComponent=SBOLAPI.addSubComponent(TetRProducer, LacI_protein);
        SubComponent IPTG_subComponent=SBOLAPI.addSubComponent(TetRProducer, IPTG);
        SubComponent IPTG_LacI_subComponent= SBOLAPI.addSubComponent(TetRProducer, IPTG_LacI);
        
        
        SBOLAPI.createInteraction(Arrays.asList(InteractionType.GeneticProduction.getUri()),TetRProducer, tetR, Arrays.asList(ParticipationRole.Template.getUri()), TetR_protein, Arrays.asList(ParticipationRole.Product.getUri()));  
        SBOLAPI.createInteraction(Arrays.asList(InteractionType.Inhibition.getUri()),TetRProducer, pLacI, Arrays.asList(ParticipationRole.Inhibited.getUri()), LacI_protein, Arrays.asList(ParticipationRole.Inhibitor.getUri()));
        //IPTG LacIbinding
     	Interaction interaction= TetRProducer.createInteraction(Arrays.asList(InteractionType.NonCovalentBinding.getUri()));
    	SBOLAPI.createParticipation(interaction, Arrays.asList(ParticipationRole.Reactant.getUri()), LacI_protein_subComponent);
    	SBOLAPI.createParticipation(interaction, Arrays.asList(ParticipationRole.Reactant.getUri()), IPTG_subComponent);
    	SBOLAPI.createParticipation(interaction, Arrays.asList(ParticipationRole.Product.getUri()), IPTG_LacI_subComponent);
       
        //LacI producer
        Component LacIProducer=SBOLAPI.createDnaComponent(doc, "LacI_producer", "LacI producer", "LacI producer", Role.EngineeredGene, null); 
        
        Component pTetR=SBOLAPI.createDnaComponent(doc, "pTetR", "pTetR", "TetR repressible promoter", Role.Promoter, null); 
        Component rbs_lacI=SBOLAPI.createDnaComponent(doc, "rbs_lacI","rbs", "RBS", Role.RBS, null);
        Component lacI=SBOLAPI.createDnaComponent(doc, "lacI", "lacI", "lacI coding sequence", Role.CDS, null);
        Component rbs_gfp=SBOLAPI.createDnaComponent(doc, "rbs_gfp", "rbs", "RBS", Role.RBS, null);
        Component gfp=SBOLAPI.createDnaComponent(doc, "gfp", "gfp", "gfp coding sequence", Role.CDS, null);
        
        Component ter_lacI=SBOLAPI.createDnaComponent(doc, "ter_lacI","lacI terminator", "Terminator", Role.Terminator, null);
        
        SBOLAPI.appendComponent(doc, LacIProducer,pTetR);
        SBOLAPI.appendComponent(doc, LacIProducer,rbs_lacI);
        SBOLAPI.appendComponent(doc, LacIProducer,lacI);
        SBOLAPI.appendComponent(doc, LacIProducer,rbs_gfp);
        SBOLAPI.appendComponent(doc, LacIProducer,gfp);
        SBOLAPI.appendComponent(doc, LacIProducer,ter_lacI);
        SBOLAPI.addSubComponent (LacIProducer, LacI_protein);
        SBOLAPI.addSubComponent (LacIProducer, GFP);
        SubComponent TetR_protein_subComponent= SBOLAPI.addSubComponent(LacIProducer, TetR_protein);
        SubComponent aTC_subComponent=SBOLAPI.addSubComponent(LacIProducer, aTC);
        SubComponent atC_TetR_subComponent= SBOLAPI.addSubComponent(LacIProducer, atC_TetR);
        
        SBOLAPI.createInteraction(Arrays.asList(InteractionType.GeneticProduction.getUri()),LacIProducer, lacI, Arrays.asList(ParticipationRole.Template.getUri()), LacI_protein, Arrays.asList(ParticipationRole.Product.getUri()));  
        SBOLAPI.createInteraction(Arrays.asList(InteractionType.GeneticProduction.getUri()),LacIProducer, gfp, Arrays.asList(ParticipationRole.Template.getUri()), GFP, Arrays.asList(ParticipationRole.Product.getUri()));  
        SBOLAPI.createInteraction(Arrays.asList(InteractionType.Inhibition.getUri()),LacIProducer, pTetR, Arrays.asList(ParticipationRole.Inhibited.getUri()), TetR_protein, Arrays.asList(ParticipationRole.Inhibitor.getUri()));
        //aTC TetR binding
      	Interaction aTCTetRbinding= LacIProducer.createInteraction(Arrays.asList(InteractionType.NonCovalentBinding.getUri()));
    	SBOLAPI.createParticipation(aTCTetRbinding, Arrays.asList(ParticipationRole.Reactant.getUri()), TetR_protein_subComponent);
    	SBOLAPI.createParticipation(aTCTetRbinding, Arrays.asList(ParticipationRole.Reactant.getUri()), aTC_subComponent);
    	SBOLAPI.createParticipation(aTCTetRbinding, Arrays.asList(ParticipationRole.Product.getUri()), atC_TetR_subComponent);
       
    	
        
        //Toggle Switch
        Component toggleSwitch=SBOLAPI.createComponent(doc, SBOLAPI.append(baseUri, "toggle_switch"), ComponentType.DNA.getUri(), "Toggle Switch", "Toggle Switch genetic circuit", null);
        toggleSwitch.setRoles(Arrays.asList(Role.EngineeredGene));
		
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
        
      
        
        String output=SBOLIO.write(doc, SBOLFormat.TURTLE);
        System.out.print(output);
        
        SBOLDocument doc2=SBOLIO.read(output, SBOLFormat.TURTLE); 
        output=SBOLIO.write(doc2, SBOLFormat.RDFXML);
        System.out.print(output);
        
        TestUtil.serialise(doc2, "toggle_switch", "toggle_switch");     
        TestUtil.assertReadWrite(doc);  
        
        TestUtil.assertDNARootComponents(doc2, 1);
  	  
        System.out.println("done");   
    }
	 
	
	
	
	
	
    
}
