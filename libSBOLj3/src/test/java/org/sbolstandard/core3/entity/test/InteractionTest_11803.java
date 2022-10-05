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
import org.sbolstandard.core3.util.URINameSpace;
import org.sbolstandard.core3.vocabulary.*;

import junit.framework.TestCase;

public class InteractionTest_11803 extends TestCase {
	
	public void testInteraction() throws SBOLGraphException, IOException, Exception
    {
		String baseUri="https://sbolstandard.org/examples/";
        SBOLDocument doc=new SBOLDocument(URI.create(baseUri));
        
        Component i13504_system=SBOLAPI.createComponent(doc,"i13504_system", ComponentType.DNA.getUri(), "i13504 system", null, Role.FunctionalCompartment);
        i13504_system.setRoles(Arrays.asList(Role.EngineeredGene));
		
        //Valid: Only one occurring entity type
        Interaction interaction= i13504_system.createInteraction(Arrays.asList(InteractionType.GeneticProduction));
        TestUtil.validateIdentified(interaction,0);
        
        //Valid: Only one occurring entity type. 4--> modelling framework
        interaction.setTypes(Arrays.asList(InteractionType.GeneticProduction, URINameSpace.SBO.local("0000004")));
        TestUtil.validateIdentified(interaction,0);
        
        //Invalid: no valid type
        interaction.setTypes(Arrays.asList(URINameSpace.SBO.local("00006444"), URINameSpace.SBO.local("0000004")));
        TestUtil.validateIdentified(interaction,1);
        
        //Invalid: Two valid types.
        interaction.setTypes(Arrays.asList(InteractionType.GeneticProduction,InteractionType.Degradation));
        TestUtil.validateIdentified(interaction,1); 
    }
	
}
