package org.sbolstandard.core3.entity.test;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.jena.rdf.model.Resource;
import org.sbolstandard.core3.api.SBOLAPI;
import org.sbolstandard.core3.entity.*;
import org.sbolstandard.core3.io.SBOLFormat;
import org.sbolstandard.core3.io.SBOLIO;
import org.sbolstandard.core3.test.TestUtil;
import org.sbolstandard.core3.util.Configuration;
import org.sbolstandard.core3.util.RDFUtil;
import org.sbolstandard.core3.util.SBOLGraphException;
import org.sbolstandard.core3.vocabulary.*;

import junit.framework.TestCase;

public class InteractionTest extends TestCase {
	
	public void testInteraction() throws SBOLGraphException, IOException, Exception
    {
		String baseUri="https://sbolstandard.org/examples/";
        SBOLDocument doc=new SBOLDocument(URI.create(baseUri));
        
        Component i13504_system=SBOLAPI.createComponent(doc,"i13504_system", ComponentType.DNA.getUri(), "i13504 system", null, Role.FunctionalCompartment);
        i13504_system.setRoles(Arrays.asList(Role.EngineeredGene));
		
        Interaction interaction= i13504_system.createInteraction(Arrays.asList(InteractionType.GeneticProduction.getUri()));
       
        TestUtil.serialise(doc, "entity_additional/interaction", "interaction");
        System.out.println(SBOLIO.write(doc, SBOLFormat.TURTLE));
        TestUtil.assertReadWrite(doc); 
        
        Configuration.getInstance().setValidateAfterSettingProperties(false);
        
        TestUtil.validateIdentified(interaction,doc,0);
        
        //Interaction.types cannot be empty
        TestUtil.validateProperty(interaction, "setTypes", new Object[] {null}, List.class);
        TestUtil.validateProperty(interaction, "setTypes", new Object[] {new ArrayList<URI>()}, List.class);
        
        interaction.setTypes(null);
        
        TestUtil.validateIdentified(interaction,doc,2);
        interaction.setTypes(Arrays.asList(InteractionType.GeneticProduction.getUri()));
        TestUtil.validateIdentified(interaction,doc,0);
        
        Resource resource = TestUtil.getResource(interaction);
        
		//SBOL_VALID_ENTITY_TYPES - Interaction.participation
		RDFUtil.setProperty(resource, DataModel.Interaction.participation, i13504_system.getUri());
		TestUtil.validateIdentified(interaction,doc,1);
		URI tmp=null;
		RDFUtil.setProperty(resource, DataModel.Interaction.participation, tmp);
		TestUtil.validateIdentified(interaction,doc,0);
	
    }
	
}
