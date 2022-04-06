package org.sbolstandard.core3.entity.test;

import java.io.IOException;
import java.net.URI;
import java.util.Arrays;

import org.sbolstandard.core3.api.SBOLAPI;
import org.sbolstandard.core3.entity.Component;
import org.sbolstandard.core3.entity.ComponentReference;
import org.sbolstandard.core3.entity.Implementation;
import org.sbolstandard.core3.entity.Interaction;
import org.sbolstandard.core3.entity.LocalSubComponent;
import org.sbolstandard.core3.entity.SBOLDocument;
import org.sbolstandard.core3.entity.SubComponent;
import org.sbolstandard.core3.io.SBOLFormat;
import org.sbolstandard.core3.io.SBOLIO;
import org.sbolstandard.core3.test.TestUtil;
import org.sbolstandard.core3.util.SBOLGraphException;
import org.sbolstandard.core3.vocabulary.ComponentType;
import org.sbolstandard.core3.vocabulary.InteractionType;
import org.sbolstandard.core3.vocabulary.Orientation;
import org.sbolstandard.core3.vocabulary.ParticipationRole;
import org.sbolstandard.core3.vocabulary.Role;

import junit.framework.TestCase;

public class LocalSubComponentTest extends TestCase {
	
	public void testLocalSubComponent() throws SBOLGraphException, IOException
    {
		String baseUri="https://sbolstandard.org/examples/";
        SBOLDocument doc=new SBOLDocument(URI.create(baseUri));
        
        Component i13504_system=SBOLAPI.createComponent(doc,"i13504_system", ComponentType.DNA.getUrl(), "i13504 system", null, Role.FunctionalCompartment);
		
        LocalSubComponent lsComponent = i13504_system.createLocalSubComponent(Arrays.asList(ComponentType.DNA.getUrl()));
        
        TestUtil.serialise(doc, "entity_additional/localsubcomponent", "localsubcomponent");
        System.out.println(SBOLIO.write(doc, SBOLFormat.TURTLE));
        TestUtil.assertReadWrite(doc);
        
        TestUtil.validateIdentified(lsComponent,doc,0);
        //LocalSubComponent.types cannot be empty
        lsComponent.setTypes(null);
        TestUtil.validateIdentified(lsComponent,doc,1);
        
    }	
	
}
