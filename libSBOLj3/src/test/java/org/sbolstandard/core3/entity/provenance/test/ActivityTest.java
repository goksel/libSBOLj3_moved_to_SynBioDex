package org.sbolstandard.core3.entity.provenance.test;

import java.io.IOException;
import java.net.URI;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import org.apache.jena.datatypes.xsd.XSDDateTime;
import org.apache.jena.rdf.model.Resource;
import org.sbolstandard.core3.api.SBOLAPI;
import org.sbolstandard.core3.entity.*;
import org.sbolstandard.core3.entity.provenance.*;
import org.sbolstandard.core3.io.SBOLFormat;
import org.sbolstandard.core3.io.SBOLIO;
import org.sbolstandard.core3.test.TestUtil;
import org.sbolstandard.core3.util.Configuration;
import org.sbolstandard.core3.util.RDFUtil;
import org.sbolstandard.core3.util.SBOLGraphException;
import org.sbolstandard.core3.util.SBOLUtil;
import org.sbolstandard.core3.validation.SBOLComparator;
import org.sbolstandard.core3.vocabulary.*;

import junit.framework.TestCase;

public class ActivityTest extends TestCase {
	
	public void test() throws SBOLGraphException, IOException, Exception
    {
		String baseUri="https://sbolstandard.org/examples/";
        SBOLDocument doc=new SBOLDocument(URI.create(baseUri));
        
        Component toggleSwitch=SBOLAPI.createComponent(doc, "toggle_switch", ComponentType.FunctionalEntity.getUrl(), "Toggle Switch", "Toggle Switch genetic circuit", null);
        Component toggleSwitchOptimised=SBOLAPI.createComponent(doc, "toggle_switch_optimised", ComponentType.FunctionalEntity.getUrl(), "Toggle Switch Optimised", "Toggle Switch genetic circuit - codon optimised", null);
        
        Agent agent=doc.createAgent("CodonOptimiserSoftware");
        agent.setName("Codon Optimiser Software");
        agent.setDescription("Used to optimise bacterial DNA sequences.");
        
        Plan plan=doc.createPlan("CodonOptimisationProtocol");
        plan.setName("Codon Optimisation Protocol");
        plan.setDescription("Optimisation protocol to improve the translation of mRNAs.");
        
        Activity activity=doc.createActivity("codon_optimization_activity");
        activity.setTypes(Arrays.asList(ActivityType.Design.getUrl()));
        activity.setName("Codon optimization activity");
        activity.setDescription("An activity that is used to optimise codons");
        Calendar calendar=Calendar.getInstance();
        calendar.set(2019,Calendar.JULY,29); 
        
        activity.setStartedAtTime(new XSDDateTime(calendar));
        calendar.set(2020,Calendar.AUGUST,30);
        activity.setEndedAtTime(new XSDDateTime(calendar));
           
        Usage usage1=activity.createUsage(toggleSwitch.getUri());
        usage1.setRoles(Arrays.asList(ParticipationRole.Template));
        Usage usage2=activity.createUsage(toggleSwitchOptimised.getUri());
        usage2.setRoles(Arrays.asList(ParticipationRole.Product));
        
        Association association=activity.createAssociation(agent);
        association.setPlan(plan);
        association.setRoles(Arrays.asList(ActivityType.Design.getUrl()));
        
        toggleSwitchOptimised.setWasGeneratedBy(Arrays.asList(activity));
        toggleSwitchOptimised.setWasDerivedFrom(Arrays.asList(toggleSwitch.getUri()));
        
        Activity rbsactivity=doc.createActivity("RBS_optimisation_activity");
        rbsactivity.setTypes(Arrays.asList(ActivityType.Design.getUrl()));
        rbsactivity.setName("RBS optimization activity");
        rbsactivity.setDescription("An activity that is used to RBSs");
        rbsactivity.setWasInformedBys(Arrays.asList(activity));
        
       
        TestUtil.serialise(doc, "provenance_entity/activity", "activity");
        
        String output=SBOLIO.write(doc, SBOLFormat.TURTLE);
        System.out.print("-----Reading back from serialised data!");
        SBOLDocument doc2=SBOLIO.read(output, SBOLFormat.TURTLE); 
        for (Activity act: doc2.getActivities())
        {
        	printActivity(doc2, act);
        	
        }
        SBOLComparator.assertEqual(doc, doc2);
        
        Configuration.getConfiguration().setValidateAfterSettingProperties(false);
	            
    	TestUtil.validateProperty(association, "setAgent", new Object[] {null}, Agent.class); 
        TestUtil.validateProperty(usage1, "setEntity", new Object[] {null}, URI.class); 
        TestUtil.validateDocument(doc, 0);
        association.setAgent(null);
        TestUtil.validateIdentified(association,doc, 1);
        usage1.setEntity(null);
        TestUtil.validateIdentified(usage1,doc, 1,2);
        
        //Clean the errors.
        association.setAgent(agent);
        usage1.setEntity(toggleSwitch.getUri());
        TestUtil.validateIdentified(usage1,doc, 0);
        
		//SBOL_VALID_ENTITY_TYPES - Activity.Usages
		TestUtil.testValidEntity(doc, activity, activity.getUsages(),  activity.getAssociations(), ProvenanceDataModel.Activity.qualifiedUsage);
		
		//SBOL_VALID_ENTITY_TYPES - Activity.WasInformedBys
		TestUtil.testValidEntity(doc, activity, activity.getWasInformedBys(), activity.getAssociations(), ProvenanceDataModel.Activity.wasInformedBy);
		
		//SBOL_VALID_ENTITY_TYPES - Activity.Associations
		TestUtil.testValidEntity(doc, activity, activity.getAssociations(), activity.getUsages(), ProvenanceDataModel.Activity.qualifiedAssociation);
		
		//SBOL_VALID_ENTITY_TYPES - Activity.Associations
		TestUtil.testValidEntity(doc, activity, activity.getAssociations(), activity.getUsages(), ProvenanceDataModel.Activity.qualifiedAssociation);
		
		//SBOL_VALID_ENTITY_TYPES - Association.Agent
		TestUtil.testValidEntity(doc, association, association.getAgent(), Arrays.asList(association.getAgent(), toggleSwitch), ProvenanceDataModel.Association.agent);
		
		//SBOL_VALID_ENTITY_TYPES - Activity.Plans
		TestUtil.testValidEntity(doc, association, association.getPlan(), Arrays.asList(association.getPlan(), toggleSwitch), ProvenanceDataModel.Association.plan);
					

    }
	
	
	
	
	private void printActivity(SBOLDocument document, Activity activity) throws SBOLGraphException
	{
		printMetadata(activity, 0);
		System.out.println("Started:" + activity.getStartedAtTime());
		System.out.println("Ended:" + activity.getEndedAtTime());
		System.out.println("Types:" + activity.getTypes());
		if (activity.getUsages()!=null)
		{
			for (Usage usage:activity.getUsages())	
			{
				System.out.println("Usage:");
				printMetadata(usage, 3);
				System.out.println("   Entity:" + usage.getEntity());
				System.out.println("   Roles:" + usage.getRoles());
			}
		}
		
		if (activity.getAssociations()!=null) {		
			for (Association association:activity.getAssociations())	
			{
				System.out.println("Association:");
				printMetadata(association, 3);
				System.out.println("   Agent:");
				Agent agent=document.getIdentified(association.getAgent().getUri(),Agent.class);
				printMetadata(agent, 6);
				if (association.getPlan()!=null)
				{
					System.out.println("   Plan:");
					Plan plan=association.getPlan();
					printMetadata(plan, 6);
				}	
			}
		}	
	}

	 private void printMetadata(Identified identified, int count) throws SBOLGraphException
	 {
		 String space="";
		 if (count>0)
		 {
			 space=String.format("%"+count+"s", "") ;
		 }
		 System.out.println(space + "uri:" + identified.getUri());
		 System.out.println(space + "name:" + identified.getName());
		 System.out.println(space + "desc:" + identified.getDescription());
		 System.out.println(space + "displayId:" + identified.getDisplayId());	 
	 }
	 
}
