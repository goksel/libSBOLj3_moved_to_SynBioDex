package org.sbolstandard.core3.entity.test;

import java.io.IOException;
import java.net.URI;
import java.util.Arrays;

import org.apache.jena.rdf.model.Resource;
import org.sbolstandard.core3.api.SBOLAPI;
import org.sbolstandard.core3.entity.Component;
import org.sbolstandard.core3.entity.ComponentReference;
import org.sbolstandard.core3.entity.Feature;
import org.sbolstandard.core3.entity.SBOLDocument;
import org.sbolstandard.core3.entity.SubComponent;
import org.sbolstandard.core3.io.SBOLFormat;
import org.sbolstandard.core3.io.SBOLIO;
import org.sbolstandard.core3.test.TestUtil;
import org.sbolstandard.core3.util.Configuration;
import org.sbolstandard.core3.util.RDFUtil;
import org.sbolstandard.core3.util.SBOLGraphException;
import org.sbolstandard.core3.vocabulary.ComponentType;
import org.sbolstandard.core3.vocabulary.DataModel;
import org.sbolstandard.core3.vocabulary.Encoding;
import org.sbolstandard.core3.vocabulary.Role;

import junit.framework.TestCase;

public class ComponentReferenceTest extends TestCase {
	
	public void testComponentReference() throws SBOLGraphException, IOException, Exception
    {
		URI base=URI.create("https://synbiohub.org/public/igem/");
		SBOLDocument doc=new SBOLDocument(base);
		
		String term_na="ccaggcatcaaataaaacgaaaggctcagtcgaaagactgggcctttcgttttatctgttgtttgtcggtgaacgctctc";
		Component simpleDevice=doc.createComponent("simpleDevice", Arrays.asList(ComponentType.DNA.getUrl())); 
		SBOLAPI.addSequence(doc, simpleDevice, Encoding.NucleicAcid, "");
		Component term=SBOLAPI.createDnaComponent(doc, "B0015", "terminator", "B0015 double terminator", Role.Terminator,term_na);
		SubComponent termSubComponent=simpleDevice.createSubComponent(term.getUri());
		
		
		Component i13504_system=SBOLAPI.createComponent(doc,"i13504_system", ComponentType.DNA.getUrl(), "i13504 system", null, Role.FunctionalCompartment);
		Component ilab16_dev1=doc.createComponent("interlab16device1", Arrays.asList(ComponentType.DNA.getUrl())); 
		Component device=doc.createComponent("i13504", Arrays.asList(ComponentType.DNA.getUrl())); 
		SubComponent i13504SubComponent=SBOLAPI.addSubComponent(i13504_system, device);
		SubComponent sc_i13504_system=SBOLAPI.addSubComponent(ilab16_dev1, i13504_system);	
		 
		ComponentReference compRef=ilab16_dev1.createComponentReference(i13504SubComponent, sc_i13504_system);
		
	    TestUtil.serialise(doc, "entity_additonal/componentreference", "componentreference");
        System.out.println(SBOLIO.write(doc, SBOLFormat.TURTLE));
        TestUtil.assertReadWrite(doc);
        
        Configuration.getConfiguration().setValidateAfterSettingProperties(false);
        
		TestUtil.validateIdentified(compRef,doc,0);
		
		//refersTo is required
		TestUtil.validateProperty(compRef, "setRefersTo", new Object[] {null}, Feature.class);
		Feature temp=compRef.getRefersTo();
		compRef.setRefersTo(null);
		TestUtil.validateIdentified(compRef,doc,1);
		compRef.setRefersTo(temp);
		TestUtil.validateIdentified(compRef,doc,0);
		
		
		//SBOL_VALID_ENTITY_TYPES ComponentReference.refersTo
        Resource resource = TestUtil.getResource(compRef);
        RDFUtil.setProperty(resource, DataModel.ComponentReference.refersTo, i13504_system.getUri());
		TestUtil.validateIdentified(compRef,doc,2);
		RDFUtil.setProperty(resource, DataModel.ComponentReference.refersTo, temp.getUri());
		TestUtil.validateIdentified(compRef,doc,0);
		
		
		//inChildOf is required
		TestUtil.validateProperty(compRef, "setInChildOf", new Object[] {null}, SubComponent.class);
		SubComponent tempSubComponent=compRef.getInChildOf();
		compRef.setInChildOf(null);
		TestUtil.validateIdentified(compRef,doc,1);
		compRef.setInChildOf(tempSubComponent);
		TestUtil.validateIdentified(compRef,doc,0);
		
		//inchildOf must refer to a subcomponent in parent component.
		compRef.setInChildOf(termSubComponent);
		TestUtil.validateIdentified(ilab16_dev1,doc, 1);
		compRef.setInChildOf(tempSubComponent);
		TestUtil.validateIdentified(ilab16_dev1,doc,0);
		
		//SBOL_VALID_ENTITY_TYPES ComponentReference.inChildOf
		RDFUtil.setProperty(resource, DataModel.ComponentReference.inChildOf, URI.create("http://invalidcomponentreference.org"));
		TestUtil.validateIdentified(compRef, 2);
		compRef.setInChildOf(tempSubComponent);
		TestUtil.validateIdentified(compRef,0);
		
		
		
		
		
		

    }

}
