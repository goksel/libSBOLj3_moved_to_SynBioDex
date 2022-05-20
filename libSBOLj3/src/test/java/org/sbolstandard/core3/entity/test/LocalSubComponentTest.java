package org.sbolstandard.core3.entity.test;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.sbolstandard.core3.api.SBOLAPI;
import org.sbolstandard.core3.entity.*;
import org.sbolstandard.core3.io.SBOLFormat;
import org.sbolstandard.core3.io.SBOLIO;
import org.sbolstandard.core3.test.TestUtil;
import org.sbolstandard.core3.util.Configuration;
import org.sbolstandard.core3.util.SBOLGraphException;
import org.sbolstandard.core3.vocabulary.*;
import junit.framework.TestCase;

public class LocalSubComponentTest extends TestCase {
	
	public void testLocalSubComponent() throws SBOLGraphException, IOException, Exception
    {
		String baseUri="https://sbolstandard.org/examples/";
        SBOLDocument doc=new SBOLDocument(URI.create(baseUri));
        
        Component i13504_system=SBOLAPI.createComponent(doc,"i13504_system", ComponentType.DNA.getUrl(), "i13504 system", null, Role.FunctionalCompartment);
		
        LocalSubComponent lsComponent = i13504_system.createLocalSubComponent(Arrays.asList(ComponentType.DNA.getUrl()));
        
        TestUtil.serialise(doc, "entity_additional/localsubcomponent", "localsubcomponent");
        System.out.println(SBOLIO.write(doc, SBOLFormat.TURTLE));
        TestUtil.assertReadWrite(doc);
        
        Configuration.getConfiguration().setValidateAfterSettingProperties(false);
        
        TestUtil.validateIdentified(lsComponent,doc,0);
        //LocalSubComponent.types cannot be empty
        TestUtil.validateProperty(lsComponent, "setTypes", new Object[] {null}, List.class);
        TestUtil.validateProperty(lsComponent, "setTypes", new Object[] {new ArrayList<URI>()}, List.class);
        
        List<URI> tempTypes=lsComponent.getTypes();
        lsComponent.setTypes(null);
        TestUtil.validateIdentified(lsComponent,doc,1);
        
        lsComponent.setTypes(new ArrayList<URI>());
        TestUtil.validateIdentified(lsComponent,doc,1);
        lsComponent.setTypes(tempTypes);
        TestUtil.validateIdentified(lsComponent,doc,0);
        
        lsComponent.setTypes(Arrays.asList(ComponentType.DNA.getUrl(), ComponentType.Protein.getUrl() ));
	    TestUtil.validateIdentified(lsComponent,doc,1);
	
	    
	    
    }	
	
}
