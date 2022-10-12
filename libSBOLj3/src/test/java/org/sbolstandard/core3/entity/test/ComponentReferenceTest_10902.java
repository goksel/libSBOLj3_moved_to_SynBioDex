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
import org.sbolstandard.core3.vocabulary.RestrictionType.OrientationRestriction;

import junit.framework.TestCase;

public class ComponentReferenceTest_10902 extends TestCase {
	
	public void testConstraintReference() throws SBOLGraphException, IOException, Exception
    {
		String baseUri="https://sbolstandard.org/examples/";
        SBOLDocument doc=new SBOLDocument(URI.create(baseUri));
        
        Component multicellularSystem=SBOLAPI.createComponent(doc, "MulticellularSystem", ComponentType.FunctionalEntity.getUri(), "MulticellularSystem", "Multicellular System", Role.FunctionalCompartment);
        
        //CompRef-->CompRef--> SubComponent test
        Component IPTG=SBOLAPI.createComponent(doc, "IPTG", ComponentType.SimpleChemical.getUri(), "IPTG", "IPTG", Role.Effector);
        
        Component iptgSenderL2=SBOLAPI.createComponent(doc, "senderIPTGSubSystem", ComponentType.FunctionalEntity.getUri(), "SenderIPTGSubSystem", "Sender IPTG Sub System", Role.FunctionalCompartment);
        Component iptgSenderL3=SBOLAPI.createComponent(doc, "senderIPTGSubSubSystem", ComponentType.FunctionalEntity.getUri(), "SenderIPTGSubSubSystem", "Sender IPTG SubSub System", Role.FunctionalCompartment);
        SubComponent scIPTG_SenderL3= iptgSenderL3.createSubComponent(IPTG);
        
        //SubComponent scIPTGSenderL3_L2= iptgSenderL2.createSubComponent(iptgSenderL3);
        //Introduce error: Rather creating the subcomponent in Level3 from a component in Level 2, create the component from the Level 1 multicellularSystem component.
        SubComponent scIPTGSenderL3_L2= multicellularSystem.createSubComponent(iptgSenderL3);
        
        SubComponent scIPTGSenderL2_L1= multicellularSystem.createSubComponent(iptgSenderL2);
        
        ComponentReference csIPTG_SenderL2=iptgSenderL2.createComponentReference(scIPTG_SenderL3, scIPTGSenderL3_L2);
        ComponentReference csIPTG_SenderL1=multicellularSystem.createComponentReference(csIPTG_SenderL2, scIPTGSenderL2_L1);
        
        TestUtil.validateDocument(doc, 2);
       
    }
}
