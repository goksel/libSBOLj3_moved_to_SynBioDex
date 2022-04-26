package org.sbolstandard.core3.entity.test;

import java.io.IOException;
import java.net.URI;
import java.util.Arrays;
import java.util.OptionalLong;
import org.sbolstandard.core3.entity.*;
import org.sbolstandard.core3.test.TestUtil;
import org.sbolstandard.core3.util.Configuration;
import org.sbolstandard.core3.util.SBOLGraphException;
import org.sbolstandard.core3.vocabulary.*;
import junit.framework.TestCase;

public class IdentifiedTest extends TestCase {
	
	public void testIdentified() throws SBOLGraphException, IOException, Exception
    {
		String baseUri="https://sbolstandard.org/examples/";
        SBOLDocument doc=new SBOLDocument(URI.create(baseUri));
        
        Attachment attachment=doc.createAttachment("attachment1", URI.create("https://sbolstandard.org/attachment1"));
        attachment.setFormat(ModelLanguage.SBML);
        attachment.setSize(OptionalLong.of(1000));
        attachment.setHashAlgorithm("Alg1");
        attachment.setHash("aaa");
        
        Configuration.getConfiguration().setValidateAfterSettingProperties(false);
        
        attachment.setDisplayId("test");
        TestUtil.validateIdentified(attachment,doc,0);
        attachment.setDisplayId("1test");
        TestUtil.validateIdentified(attachment,doc,1);
        attachment.setDisplayId("_test");
        TestUtil.validateIdentified(attachment,doc,0);
        TestUtil.validateProperty(attachment, "setDisplayId", new Object[] {"!qq"}, String.class);
        
        Attachment attachment2=doc.createAttachment("2attachment", URI.create("https://sbolstandard.org/attachment2_source"));
        TestUtil.validateIdentified(attachment2,doc,1);
        attachment2.setDisplayId("attachment2");
        TestUtil.validateIdentified(attachment2,doc,0);
      
        attachment.setWasDerivedFrom(null);
        TestUtil.validateIdentified(attachment,doc,0);
        attachment.setWasDerivedFrom(Arrays.asList(attachment.getUri()));
        TestUtil.validateIdentified(attachment, doc, 1);
    }
}
