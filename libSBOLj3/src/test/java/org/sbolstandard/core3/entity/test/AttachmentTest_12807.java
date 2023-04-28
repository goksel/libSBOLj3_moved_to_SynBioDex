package org.sbolstandard.core3.entity.test;

import java.io.IOException;
import java.net.URI;
import java.util.OptionalLong;
import org.apache.jena.rdf.model.Resource;
import org.sbolstandard.core3.entity.Attachment;
import org.sbolstandard.core3.entity.SBOLDocument;
import org.sbolstandard.core3.test.TestUtil;
import org.sbolstandard.core3.util.Configuration;
import org.sbolstandard.core3.util.RDFUtil;
import org.sbolstandard.core3.util.SBOLGraphException;
import org.sbolstandard.core3.vocabulary.DataModel;
import org.sbolstandard.core3.vocabulary.HashAlgorithm;
import org.sbolstandard.core3.vocabulary.ModelLanguage;

import junit.framework.TestCase;

public class AttachmentTest_12807 extends TestCase {
	
	public void testAttachment() throws SBOLGraphException, IOException, Exception
    {
		String baseUri="https://sbolstandard.org/examples/";
        SBOLDocument doc=new SBOLDocument(URI.create(baseUri));
        
        Attachment attachment2=doc.createAttachment("attachment2", URI.create("https://sbolstandard.org/attachment2"));
        attachment2.setFormat(ModelLanguage.SBML);
        attachment2.setSize(OptionalLong.of(1000));
        attachment2.setHashAlgorithm(HashAlgorithm.blake2b_256);
        attachment2.setHash("aaa");
        
        TestUtil.validateIdentified(attachment2,doc,0);        
    	
        Resource resource = TestUtil.getResource(attachment2);		
    	RDFUtil.setProperty(resource, DataModel.Attachment.hashAlgorithm, "invalidAlg");
    	Configuration.getInstance().setValidateRecommendedRules(false);
    	TestUtil.validateIdentified(attachment2,doc,0);
    	Configuration.getInstance().setValidateRecommendedRules(true);				
    	TestUtil.validateIdentified(attachment2,doc,1, "sbol3-12807");
    	
    	attachment2.setHashAlgorithm(HashAlgorithm.blake2b_256);      	
      	TestUtil.validateIdentified(attachment2,doc,0);    	
    }

}
