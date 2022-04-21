package org.sbolstandard.core3.entity.provenance.test;

import java.io.IOException;
import java.net.URI;
import java.time.Month;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

import org.apache.jena.datatypes.xsd.XSDDateTime;
import org.sbolstandard.core3.api.SBOLAPI;
import org.sbolstandard.core3.entity.Component;
import org.sbolstandard.core3.entity.Identified;
import org.sbolstandard.core3.entity.SBOLDocument;
import org.sbolstandard.core3.entity.provenance.Activity;
import org.sbolstandard.core3.entity.provenance.Agent;
import org.sbolstandard.core3.entity.provenance.Association;
import org.sbolstandard.core3.entity.provenance.Plan;
import org.sbolstandard.core3.entity.provenance.Usage;
import org.sbolstandard.core3.io.SBOLFormat;
import org.sbolstandard.core3.io.SBOLIO;
import org.sbolstandard.core3.test.TestUtil;
import org.sbolstandard.core3.util.Configuration;
import org.sbolstandard.core3.util.SBOLGraphException;
import org.sbolstandard.core3.util.Configuration.PropertyValidationType;
import org.sbolstandard.core3.validation.SBOLComparator;
import org.sbolstandard.core3.validation.SBOLValidator;
import org.sbolstandard.core3.vocabulary.ActivityType;
import org.sbolstandard.core3.vocabulary.ComponentType;
import org.sbolstandard.core3.vocabulary.ParticipationRole;

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
        
        Association association=activity.createAssociation(agent.getUri());
        association.setPlan(plan.getUri());
        association.setRoles(Arrays.asList(ActivityType.Design.getUrl()));
        
        toggleSwitchOptimised.setWasGeneratedBy(Arrays.asList(activity.getUri()));
        toggleSwitchOptimised.setWasDerivedFrom(Arrays.asList(toggleSwitch.getUri()));
        
        
        
        Activity rbsactivity=doc.createActivity("RBS_optimisation_activity");
        rbsactivity.setTypes(Arrays.asList(ActivityType.Design.getUrl()));
        rbsactivity.setName("RBS optimization activity");
        rbsactivity.setDescription("An activity that is used to RBSs");
        rbsactivity.setWasInformedBys(Arrays.asList(activity.getUri()));
        
       
        TestUtil.serialise(doc, "provenance_entity/activity", "activity");
        
        String output=SBOLIO.write(doc, SBOLFormat.TURTLE);
        System.out.print("-----Reading back from serialised data!");
        SBOLDocument doc2=SBOLIO.read(output, SBOLFormat.TURTLE); 
        for (Activity act: doc2.getActivities())
        {
        	printActivity(doc2, act);
        	
        }
        SBOLComparator.assertEqual(doc, doc2);
        
    	Configuration.getConfiguration().setPropertyValidationType(PropertyValidationType.ValidateBeforeSavingSBOLDocuments);
        
    	TestUtil.validateProperty(association, "setAgent", new Object[] {null}, URI.class); 
        TestUtil.validateProperty(usage1, "setEntity", new Object[] {null}, URI.class); 
        TestUtil.validateDocument(doc, 0);
        association.setAgent(null);
        TestUtil.validateIdentified(association,doc, 1);
        usage1.setEntity(null);
        TestUtil.validateIdentified(usage1,doc, 1,2);
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
				Agent agent=(Agent)document.getIdentified(association.getAgent(),Agent.class);
				printMetadata(agent, 6);
				if (association.getPlan()!=null)
				{
					System.out.println("   Plan:");
					Plan plan=(Plan)document.getIdentified(association.getPlan(),Plan.class);
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
