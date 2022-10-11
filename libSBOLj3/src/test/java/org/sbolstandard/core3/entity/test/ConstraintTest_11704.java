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

public class ConstraintTest_11704 extends TestCase {
	
	public void testConstraintReference() throws SBOLGraphException, IOException, Exception
    {
		URI base=URI.create("https://synbiohub.org/public/igem/");
		SBOLDocument doc=new SBOLDocument(base);
		
		Component device= SBOLAPI.createDnaComponent(doc, "i13504", "i13504", "Screening plasmid intermediate", ComponentType.DNA.getUri(), null);
		device.setRoles(Arrays.asList(Role.EngineeredGene));
		
		Component i13504_system=SBOLAPI.createComponent(doc,"i13504_system", ComponentType.FunctionalEntity.getUri(), "i13504 system", null, Role.FunctionalCompartment);
		
		Component ilab16_dev1=doc.createComponent("interlab16device1", Arrays.asList(ComponentType.DNA.getUri())); 
		ilab16_dev1.setRoles(Arrays.asList(Role.EngineeredGene));
		
		Component j23101=doc.createComponent("j23101", Arrays.asList(ComponentType.DNA.getUri())); 
		j23101.setRoles(Arrays.asList(Role.EngineeredGene));
		
		SubComponent sc_j23101=SBOLAPI.addSubComponent(ilab16_dev1, j23101);	
		SubComponent i13504SubComponent=SBOLAPI.addSubComponent(i13504_system, device);
		SubComponent sc_i13504_system=SBOLAPI.addSubComponent(ilab16_dev1, i13504_system);	 	
		
		ComponentReference compRef_i13504_dev1=ilab16_dev1.createComponentReference(i13504SubComponent, sc_i13504_system);
		
		org.sbolstandard.core3.entity.Constraint constraint=ilab16_dev1.createConstraint(RestrictionType.TopologyRestriction.meets.getUri(), sc_j23101, compRef_i13504_dev1);
		TestUtil.validateIdentified(constraint,doc,0);
		org.sbolstandard.core3.entity.Constraint constraint2=ilab16_dev1.createConstraint(ParticipationRole.Inhibited.getUri(), sc_j23101, compRef_i13504_dev1);
		TestUtil.validateIdentified(constraint2,doc,1);
		
	    
		
		
    }

}
