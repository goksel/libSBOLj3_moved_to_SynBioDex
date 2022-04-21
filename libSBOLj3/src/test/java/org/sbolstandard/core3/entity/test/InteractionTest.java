package org.sbolstandard.core3.entity.test;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.sbolstandard.core3.api.SBOLAPI;
import org.sbolstandard.core3.entity.Component;
import org.sbolstandard.core3.entity.ComponentReference;
import org.sbolstandard.core3.entity.Implementation;
import org.sbolstandard.core3.entity.Interaction;
import org.sbolstandard.core3.entity.SBOLDocument;
import org.sbolstandard.core3.entity.SubComponent;
import org.sbolstandard.core3.io.SBOLFormat;
import org.sbolstandard.core3.io.SBOLIO;
import org.sbolstandard.core3.test.TestUtil;
import org.sbolstandard.core3.util.Configuration;
import org.sbolstandard.core3.util.SBOLGraphException;
import org.sbolstandard.core3.util.Configuration.PropertyValidationType;
import org.sbolstandard.core3.vocabulary.ComponentType;
import org.sbolstandard.core3.vocabulary.InteractionType;
import org.sbolstandard.core3.vocabulary.Orientation;
import org.sbolstandard.core3.vocabulary.ParticipationRole;
import org.sbolstandard.core3.vocabulary.Role;

import junit.framework.TestCase;

public class InteractionTest extends TestCase {
	
	public void testInteraction() throws SBOLGraphException, IOException, Exception
    {
		String baseUri="https://sbolstandard.org/examples/";
        SBOLDocument doc=new SBOLDocument(URI.create(baseUri));
        
        Component i13504_system=SBOLAPI.createComponent(doc,"i13504_system", ComponentType.DNA.getUrl(), "i13504 system", null, Role.FunctionalCompartment);
		
        Interaction interaction= i13504_system.createInteraction(Arrays.asList(InteractionType.GeneticProduction));
       
        TestUtil.serialise(doc, "entity_additional/interaction", "interaction");
        System.out.println(SBOLIO.write(doc, SBOLFormat.TURTLE));
        TestUtil.assertReadWrite(doc); 
        
    	Configuration.getConfiguration().setPropertyValidationType(PropertyValidationType.ValidateBeforeSavingSBOLDocuments);
        
        TestUtil.validateIdentified(interaction,doc,0);
        
        //Interaction.types cannot be empty
        TestUtil.validateProperty(interaction, "setTypes", new Object[] {null}, List.class);
        TestUtil.validateProperty(interaction, "setTypes", new Object[] {new ArrayList<URI>()}, List.class);
        
        interaction.setTypes(null);
        
        TestUtil.validateIdentified(interaction,doc,1);
    }
	
}
