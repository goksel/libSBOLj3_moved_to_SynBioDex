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

public class ComponentReferenceTest_10904 extends TestCase {
	
	public void testConstraintReference() throws SBOLGraphException, IOException, Exception
    {
		String baseUri="https://sbolstandard.org/examples/";
        SBOLDocument doc=new SBOLDocument(URI.create(baseUri));
        
        Component multicellularSystem=SBOLAPI.createComponent(doc, "MulticellularSystem", ComponentType.FunctionalEntity.getUri(), "MulticellularSystem", "Multicellular System", Role.FunctionalCompartment);
        
        //CompRef-->CompRef--> SubComponent test
        Component IPTG=SBOLAPI.createComponent(doc, "IPTG", ComponentType.SimpleChemical.getUri(), "IPTG", "IPTG", Role.Effector);
        Component Ara=SBOLAPI.createComponent(doc, "Ara", ComponentType.SimpleChemical.getUri(), "Ara", "Ara", Role.Effector);
         
        Component iptgSenderL2=SBOLAPI.createComponent(doc, "senderIPTGSubSystem", ComponentType.FunctionalEntity.getUri(), "SenderIPTGSubSystem", "Sender IPTG Sub System", Role.FunctionalCompartment);
        SubComponent scIPTG_SenderL2= iptgSenderL2.createSubComponent(IPTG);
        
        SubComponent scIPTGSenderL2_L1= multicellularSystem.createSubComponent(iptgSenderL2);
        ComponentReference csIPTG_SenderL1=multicellularSystem.createComponentReference(scIPTG_SenderL2, scIPTGSenderL2_L1);
        
        TestUtil.validateDocument(doc, 0);
        //Error: Create a Cref referring to a subComponent from the parent rather than a feature from the subcomponent linked via the invhildOf property.
        SubComponent scAra_L1= multicellularSystem.createSubComponent(Ara);
        ComponentReference csAra_SenderL1=multicellularSystem.createComponentReference(scAra_L1, scIPTGSenderL2_L1);
        TestUtil.validateDocument(doc, 1);
        
        
       
    }
}
