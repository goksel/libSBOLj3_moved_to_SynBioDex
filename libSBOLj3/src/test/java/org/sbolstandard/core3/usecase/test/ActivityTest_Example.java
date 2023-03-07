package org.sbolstandard.core3.usecase.test;

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

public class ActivityTest_Example extends TestCase {
	
	public void testAttachment() throws SBOLGraphException, IOException, Exception
    {
		String baseUri="https://sbolstandard.org/examples/";
        SBOLDocument doc=new SBOLDocument(URI.create(baseUri));
        
        Model model=doc.createModel("model1", URI.create("http://virtualparts.org"), ModelFramework.Continuous,ModelLanguage.SBML);
        
        //Component toggleSwitch=SBOLAPI.createComponent(doc, "toggle_switch", ComponentType.FunctionalEntity.getUri(), "Toggle Switch", "Toggle Switch genetic circuit", null);
        Component toggleSwitchOptimised=SBOLAPI.createComponent(doc, "toggle_switch_optimised", ComponentType.FunctionalEntity.getUri(), "Toggle Switch Optimised", "Toggle Switch genetic circuit - codon optimised", null);
        toggleSwitchOptimised.addWasDerivedFrom(model);
        
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
        usage1.addRole(ActivityType.Learn.getUri());
        
        Association association=activity.createAssociation(agent);
        association.setPlan(plan);
        association.addRole(ActivityType.Design.getUri());
        
        toggleSwitchOptimised.addWasGeneratedBy(activity);
             	
        TestUtil.validateIdentified(activity,doc,0);
        
        //2nd Activity
        Plan planAssembly=doc.createPlan("GibsonAssembly");
        planAssembly.setName("GibsonAssembly");
        planAssembly.setDescription("DNA Assembly");
        
        Agent agentTech=doc.createAgent("jdoe");
        agentTech.setName("J. Doe");
        agentTech.setDescription("Lab technician");
        
        Implementation imp=doc.createImplementation("Implemantation");
        Activity activity2=doc.createActivity("activity2");
        activity2.addType(ActivityType.Build.getUri());
        activity2.setName("activity 2");
        Usage usage2=activity2.createUsage(toggleSwitchOptimised.getUri());
        usage1.addRole(ActivityType.Design.getUri());
        
        Association association2=activity2.createAssociation(agentTech);
        association2.setPlan(planAssembly);
        association2.addRole(ActivityType.Build.getUri());
        
        imp.addWasGeneratedBy(activity2);
        imp.addWasDerivedFrom(toggleSwitchOptimised);
        
        TestUtil.validateIdentified(activity,doc,0);
        
        
      //3nd Activity
        Plan planProtocol=doc.createPlan("Protocol");
        planProtocol.setName("Protocol");
        
        Agent agentReader=doc.createAgent("TecanPlateReader");
        agentReader.setName("Tecan Plate Reader");
        
        ExperimentalData exp=doc.createExperimentalData("ExpData");
        Activity activity3=doc.createActivity("activity3");
        activity3.addType(ActivityType.Test.getUri());
        activity3.setName("activity 3");
        Usage usage3=activity3.createUsage(imp.getUri());
        usage3.addRole(ActivityType.Build.getUri());
        
        Association association3=activity3.createAssociation(agentReader);
        association3.setPlan(planProtocol);
        association3.addRole(ActivityType.Test.getUri());
        
        exp.addWasGeneratedBy(activity3);
        exp.addWasDerivedFrom(imp);
        
        TestUtil.validateIdentified(activity,doc,0);
        
        
        //4th Activity
        Plan planOptimiser=doc.createPlan("Optimiser");
        planOptimiser.setName("Optimiser");
        
        Agent agentFellow=doc.createAgent("jschmoe");
        agentFellow.setName("Research Fellow");
        
        Model model2=doc.createModel("model2", URI.create("http://virtualparts.org"), ModelFramework.Continuous,ModelLanguage.SBML);
        
        Activity activity4=doc.createActivity("activity4");
        activity4.addType(ActivityType.Learn.getUri());
        activity4.setName("activity 4");
        Usage usage4=activity4.createUsage(exp.getUri());
        usage4.addRole(ActivityType.Test.getUri());
        
        Association association4=activity4.createAssociation(agentFellow);
        association4.setPlan(planOptimiser);
        association4.addRole(ActivityType.Learn.getUri());
        
        model2.addWasGeneratedBy(activity4);
        model2.addWasDerivedFrom(exp);
        
        TestUtil.validateIdentified(activity,doc,0);
       
    }

}
