package org.sbolstandard.core3.entity.provenance.test;

import java.io.IOException;
import java.net.URI;
import java.util.Arrays;
import org.sbolstandard.core3.api.SBOLAPI;
import org.sbolstandard.core3.entity.*;
import org.sbolstandard.core3.entity.provenance.*;
import org.sbolstandard.core3.test.TestUtil;
import org.sbolstandard.core3.util.*;
import org.sbolstandard.core3.vocabulary.*;

import junit.framework.TestCase;

public class UsageTest_13001 extends TestCase {
	
	public void testAttachment() throws SBOLGraphException, IOException, Exception
    {
		String baseUri="https://sbolstandard.org/examples/";
        SBOLDocument doc=new SBOLDocument(URI.create(baseUri));
        
        Model model=doc.createModel("model1", URI.create("http://virtualparts.org"), ModelFramework.Continuous,ModelLanguage.SBML);
        
        //Component toggleSwitch=SBOLAPI.createComponent(doc, "toggle_switch", ComponentType.FunctionalEntity.getUri(), "Toggle Switch", "Toggle Switch genetic circuit", null);
        Component toggleSwitchOptimised=SBOLAPI.createComponent(doc, "toggle_switch_optimised", ComponentType.FunctionalEntity.getUri(), "Toggle Switch Optimised", "Toggle Switch genetic circuit - codon optimised", null);
        
        Agent agent=doc.createAgent("iBioSim");
        agent.setName("iBioSim ");
        agent.setDescription("Software tool");
        
        Plan plan=doc.createPlan("SBMLtoSBOLconversion");
        plan.setName("Codon Optimisation Protocol");
        plan.setDescription("Optimisation protocol to improve the translation of mRNAs.");
        
        
        Activity activity=doc.createActivity("activity1");
        activity.addType(ActivityType.Design.getUri());
        activity.setName("activity 1");
           
        Usage usage1=activity.createUsage(model.getUri());
        
        Association association=activity.createAssociation(agent);
        association.setPlan(plan);
        association.addRole(ActivityType.Design.getUri());
        
        toggleSwitchOptimised.addWasGeneratedBy(activity);
             	
        TestUtil.validateIdentified(activity,0);  
        Implementation imp=doc.createImplementation("implementation");
        
        usage1.addRole(ActivityType.Learn.getUri());        
        usage1.setEntity(imp.getUri());
        TestUtil.validateDocument(doc,1,"sbol3-13001");  
        ExperimentalData exp=doc.createExperimentalData("ExpData");
        usage1.setEntity(exp.getUri());
        TestUtil.validateDocument(doc,0); 
        
        
        usage1.setRoles(Arrays.asList(ActivityType.Test.getUri()));  
        TestUtil.validateDocument(doc,1,"sbol3-12901");  
        
        
        usage1.setRoles(Arrays.asList(ActivityType.Build.getUri())); 
        TestUtil.validateDocument(doc,2,"sbol3-12901,sbol3-13001");
        usage1.setEntity(imp.getUri());
        TestUtil.validateDocument(doc,1, "sbol3-12901") ; 
        
        usage1.setRoles(Arrays.asList(ActivityType.Design.getUri())); 
        TestUtil.validateDocument(doc,1,"sbol3-13001");
        usage1.setEntity(toggleSwitchOptimised.getUri());
        TestUtil.validateDocument(doc,0);
         
        
        
         
        
        /*usage1.setEntity(toggleSwitchOptimised.getUri());
        TestUtil.validateDocument(doc,0); 
        */
        
        
        
        
        
        
        
        
        
        
        
        
        activity.setTypes(Arrays.asList(URI.create("http://non-dbtl_type.org"))); 
        usage1.setRoles(Arrays.asList(URI.create("http://non-dbtl_type.org")));         
        TestUtil.validateDocument(doc,0);
        
    }

}
