package org.sbolstandard.core3.entity.test;

import java.io.IOException;
import java.net.URI;
import java.util.Arrays;
import org.sbolstandard.core3.api.SBOLAPI;
import org.sbolstandard.core3.entity.Collection;
import org.sbolstandard.core3.entity.Component;
import org.sbolstandard.core3.entity.ComponentReference;
import org.sbolstandard.core3.entity.SBOLDocument;
import org.sbolstandard.core3.entity.SubComponent;
import org.sbolstandard.core3.io.SBOLFormat;
import org.sbolstandard.core3.io.SBOLIO;
import org.sbolstandard.core3.test.TestUtil;
import org.sbolstandard.core3.util.SBOLGraphException;
import org.sbolstandard.core3.vocabulary.ComponentType;
import org.sbolstandard.core3.vocabulary.Role;

import junit.framework.TestCase;

public class ComponentReferenceTest extends TestCase {
	
	public void testComponentReference() throws SBOLGraphException, IOException
    {
		URI base=URI.create("https://synbiohub.org/public/igem/");
		SBOLDocument doc=new SBOLDocument(base);
		
		Component i13504_system=SBOLAPI.createComponent(doc,"i13504_system", ComponentType.DNA.getUrl(), "i13504 system", null, Role.FunctionalCompartment);
		Component ilab16_dev1=doc.createComponent("interlab16device1", Arrays.asList(ComponentType.DNA.getUrl())); 
		Component device=doc.createComponent("i13504", Arrays.asList(ComponentType.DNA.getUrl())); 
		SubComponent i13504SubComponent=SBOLAPI.addSubComponent(i13504_system, device);
		SubComponent sc_i13504_system=SBOLAPI.addSubComponent(ilab16_dev1, i13504_system);	
		 
		ComponentReference compRef=ilab16_dev1.createComponentReference(i13504SubComponent, sc_i13504_system);
		
		TestUtil.validateIdentified(compRef,0);
		
		//refersTo is required
		URI temp=compRef.getRefersTo();
		compRef.setRefersTo(null);
		TestUtil.validateIdentified(compRef,1);
		compRef.setRefersTo(temp);
		
		//inChildOf is required
		temp=compRef.getInChildOf();
		compRef.setInChildOf(null);
		TestUtil.validateIdentified(compRef,1);
		compRef.setInChildOf(temp);
		
	    //TestUtil.serialise(doc, "entity/componentreference", "componentreference");
      
        System.out.println(SBOLIO.write(doc, SBOLFormat.TURTLE));
        TestUtil.assertReadWrite(doc);
    }

}
