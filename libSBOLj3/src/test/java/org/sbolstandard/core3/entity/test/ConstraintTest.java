package org.sbolstandard.core3.entity.test;

import java.io.IOException;
import java.net.URI;
import java.util.Arrays;

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

public class ConstraintTest extends TestCase {
	
	public void testConstraintReference() throws SBOLGraphException, IOException, Exception
    {
		URI base=URI.create("https://synbiohub.org/public/igem/");
		SBOLDocument doc=new SBOLDocument(base);
		
		Component device= SBOLAPI.createDnaComponent(doc, "i13504", "i13504", "Screening plasmid intermediate", ComponentType.DNA.getUri(), null);
		device.setRoles(Arrays.asList(Role.EngineeredGene));
		
		Component i13504_system=SBOLAPI.createComponent(doc,"i13504_system", ComponentType.FunctionalEntity.getUri(), "i13504 system", null, Role.FunctionalCompartment);
		 
		Component ilab16_dev1=doc.createComponent("interlab16device1", Arrays.asList(ComponentType.DNA.getUri())); 
		ilab16_dev1.setRoles(Arrays.asList(Role.EngineeredGene));
		
		SubComponent i13504SubComponent=SBOLAPI.addSubComponent(i13504_system, device);
		SubComponent sc_i13504_system=SBOLAPI.addSubComponent(ilab16_dev1, i13504_system);	 	
		ComponentReference compRef_i13504_dev1=ilab16_dev1.createComponentReference(i13504SubComponent, sc_i13504_system);
		Component j23101=doc.createComponent("j23101", Arrays.asList(ComponentType.DNA.getUri())); 
		j23101.setRoles(Arrays.asList(Role.EngineeredGene));
		
		SubComponent sc_j23101=SBOLAPI.addSubComponent(ilab16_dev1, j23101);	
			
		org.sbolstandard.core3.entity.Constraint constraint=ilab16_dev1.createConstraint(RestrictionType.TopologyRestriction.meets.getUri(), sc_j23101, compRef_i13504_dev1);
		
	    TestUtil.serialise(doc, "entity_additional/constraint", "constraint");
      
        System.out.println(SBOLIO.write(doc, SBOLFormat.TURTLE));
        TestUtil.assertReadWrite(doc);
        
        Configuration.getInstance().setValidateAfterSettingProperties(false);
        
    	URI tempRestriction=constraint.getRestriction();
    	Feature tempSubject=constraint.getSubject();
    	Feature tempObject=constraint.getObject();
    	
 
        TestUtil.validateIdentified(constraint,doc,0);
        TestUtil.validateProperty(constraint, "setRestriction", new Object[] {null}, URI.class);
        URI testURI=null;
		constraint.setRestriction(testURI);
		TestUtil.validateIdentified(constraint,doc,2);
		
		TestUtil.validateProperty(constraint, "setObject", new Object[] {null}, Feature.class);
		constraint.setObject(null);
		TestUtil.validateIdentified(constraint,doc,3);
		
		TestUtil.validateProperty(constraint, "setSubject", new Object[] {null}, Feature.class);
		constraint.setSubject(null);
		TestUtil.validateIdentified(constraint,doc,4);
		
		constraint.setRestriction(tempRestriction);
		constraint.setSubject(tempSubject);
		constraint.setObject(tempObject);
		TestUtil.validateIdentified(constraint,doc,0);
		
		//CONSTRAINT_SUBJECT_MUST_REFER_TO_A_FEATURE_OF_THE_PARENT
		constraint.setSubject(i13504SubComponent);
		TestUtil.validateIdentified(ilab16_dev1,doc,1,1);
		constraint.setSubject(tempSubject);
		TestUtil.validateIdentified(ilab16_dev1,doc,0,0);
		
		//CONSTRAINT_OBJECT_MUST_REFER_TO_A_FEATURE_OF_THE_PARENT
		constraint.setObject(i13504SubComponent);
		TestUtil.validateIdentified(ilab16_dev1,doc,1,1);
		
		//In addition to adding an invalid subject URI, the subject and the object uri cannot be the same. Hence the following line will introduce two errors.
		//CONSTRAINT_OBJECT_AND_SUBJECT_ARE_NOT_EQUAL
		constraint.setSubject(i13504SubComponent);
		TestUtil.validateIdentified(ilab16_dev1,doc,3,3);
		constraint.setSubject(tempSubject);
		constraint.setObject(tempObject);
		TestUtil.validateIdentified(ilab16_dev1,doc,0);
		
		Resource resource = TestUtil.getResource(constraint);
		//SBOL_VALID_ENTITY_TYPES - Constraint.subject & object
		RDFUtil.setProperty(resource, DataModel.Constraint.subject, device.getUri());
		TestUtil.validateIdentified(constraint,doc,2);
		RDFUtil.setProperty(resource, DataModel.Constraint.object, device.getUri());
		TestUtil.validateIdentified(constraint,doc,4);
		constraint.setSubject(tempSubject);
		constraint.setObject(tempObject);
		TestUtil.validateIdentified(constraint,doc,0);
		
		
    }

}
