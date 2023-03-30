package org.sbolstandard.core3.entity.test;

import java.io.IOException;
import java.net.URI;
import java.util.Arrays;
import org.sbolstandard.core3.api.SBOLAPI;
import org.sbolstandard.core3.entity.*;
import org.sbolstandard.core3.test.TestUtil;
import org.sbolstandard.core3.util.SBOLGraphException;
import org.sbolstandard.core3.util.URINameSpace;
import org.sbolstandard.core3.vocabulary.*;

import junit.framework.TestCase;

public class InteractionTest_11804 extends TestCase {
	
	public void testInteraction() throws SBOLGraphException, IOException, Exception
    {
		String baseUri="https://sbolstandard.org/examples/";
        SBOLDocument doc=new SBOLDocument(URI.create(baseUri));
        
        //LacI producer
        Component LacIProducer=SBOLAPI.createDnaComponent(doc, "LacI_producer", "LacI producer", "LacI producer", Role.EngineeredGene, null); 
        Component TetR_protein=SBOLAPI.createComponent(doc, SBOLAPI.append(baseUri, "TetR_protein"), ComponentType.Protein.getUri(), "TetR", "TetR protein", Role.TF);
        Component aTC=SBOLAPI.createComponent(doc, SBOLAPI.append(baseUri, "aTC"), ComponentType.SimpleChemical.getUri(), "aTC", "aTC", Role.Effector);
        Component atC_TetR=SBOLAPI.createComponent(doc, SBOLAPI.append(baseUri, "atC_TetR"), ComponentType.NoncovalentComplex.getUri(), "atC_TetR", "atC_TetR complex", null);
        
        SubComponent TetR_protein_subComponent= SBOLAPI.addSubComponent(LacIProducer, TetR_protein);
        SubComponent aTC_subComponent=SBOLAPI.addSubComponent(LacIProducer, aTC);
        SubComponent atC_TetR_subComponent= SBOLAPI.addSubComponent(LacIProducer, atC_TetR);
      
        //Valid: Only one occurring entity type
        Interaction interaction= LacIProducer.createInteraction(Arrays.asList(InteractionType.NonCovalentBinding.getUri()));
    	//Valid interaction when there are no participants
        TestUtil.validateIdentified(interaction,0);
        
        //InValid interaction since the role does not come from Table 12 altough 15 (substrate) is a subclass of 10 (reactant)
        Participation testParticipation= SBOLAPI.createParticipation(interaction, Arrays.asList(URINameSpace.SBO.local("0000015")), TetR_protein_subComponent);
        TestUtil.validateIdentified(interaction,1);
        
        SBOLAPI.createParticipation(interaction, Arrays.asList(ParticipationRole.Reactant.getUri()), TetR_protein_subComponent);
        TestUtil.validateIdentified(interaction,1);
        
        SBOLAPI.createParticipation(interaction, Arrays.asList(ParticipationRole.Reactant.getUri()), aTC_subComponent);
        TestUtil.validateIdentified(interaction,1);
        
        SBOLAPI.createParticipation(interaction, Arrays.asList(ParticipationRole.Product.getUri()), atC_TetR_subComponent);
        TestUtil.validateIdentified(interaction,1);
        
        testParticipation.setRoles(Arrays.asList(ParticipationRole.Reactant.getUri()));
        TestUtil.validateIdentified(interaction,0);
        
       // Interaction interaction2= LacIProducer.createInteraction(Arrays.asList(InteractionType.NonCovalentBinding.getUri()));
        
    	
        
        
    }
	
}
