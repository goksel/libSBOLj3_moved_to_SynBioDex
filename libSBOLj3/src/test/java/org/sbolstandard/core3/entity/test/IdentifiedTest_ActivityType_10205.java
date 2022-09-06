package org.sbolstandard.core3.entity.test;

import java.io.IOException;
import java.net.URI;
import java.util.Arrays;
import java.util.Calendar;
import java.util.OptionalLong;

import org.apache.jena.datatypes.xsd.XSDDateTime;
import org.apache.jena.rdf.model.Resource;
import org.sbolstandard.core3.entity.*;
import org.sbolstandard.core3.entity.provenance.Activity;
import org.sbolstandard.core3.entity.provenance.Agent;
import org.sbolstandard.core3.entity.provenance.Association;
import org.sbolstandard.core3.entity.provenance.Plan;
import org.sbolstandard.core3.entity.provenance.Usage;
import org.sbolstandard.core3.test.TestUtil;
import org.sbolstandard.core3.util.Configuration;
import org.sbolstandard.core3.util.RDFUtil;
import org.sbolstandard.core3.util.SBOLGraphException;
import org.sbolstandard.core3.util.URINameSpace;
import org.sbolstandard.core3.vocabulary.*;
import junit.framework.TestCase;

public class IdentifiedTest_ActivityType_10205 extends TestCase {
	
	public void testIdentified() throws SBOLGraphException, IOException, Exception
    {
		String baseUri="https://sbolstandard.org/examples/";
        SBOLDocument doc=new SBOLDocument(URI.create(baseUri));
        
        Attachment attachment=doc.createAttachment("attachment1", URI.create("https://sbolstandard.org/attachment1"));
        attachment.setFormat(ModelLanguage.SBML);
        attachment.setSize(OptionalLong.of(1000));
        attachment.setHashAlgorithm("Alg1");
        attachment.setHash("aaa");
        
        Configuration.getInstance().setValidateAfterSettingProperties(false);
        
        Agent agent=doc.createAgent("CodonOptimiserSoftware");
        agent.setName("Codon Optimiser Software");
        agent.setDescription("Used to optimise bacterial DNA sequences.");
        
        Activity activity=doc.createActivity("codon_optimization_activity");
        activity.setTypes(Arrays.asList(ActivityType.Design.getUri()));
        activity.setName("Codon optimization activity");
             
        Association association=activity.createAssociation(agent);
        association.setRoles(Arrays.asList(ActivityType.Design.getUri()));
        
        attachment.setWasGeneratedBy(Arrays.asList(activity));
        TestUtil.validateIdentified(attachment, doc, 0);
        
        association.setRoles(Arrays.asList(ActivityType.Build.getUri()));
        TestUtil.validateIdentified(attachment, doc, 1);
        
        activity.setTypes(Arrays.asList(ActivityType.Design.getUri(), ActivityType.Build.getUri()));
        TestUtil.validateIdentified(attachment, doc, 0);
        
        
        
		
    }
}
