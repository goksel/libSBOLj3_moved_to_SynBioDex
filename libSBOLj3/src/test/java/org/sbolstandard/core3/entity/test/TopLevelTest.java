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

public class TopLevelTest extends TestCase {
	
	public void testTopLevel() throws SBOLGraphException, IOException, Exception
    {
		String baseUri="https://sbolstandard.org/examples/";
        SBOLDocument doc=new SBOLDocument(URI.create(baseUri));
        
        Attachment attachment=doc.createAttachment("attachment1", URI.create("https://sbolstandard.org/attachment1"));
        attachment.setFormat(ModelLanguage.SBML);
        attachment.setSize(OptionalLong.of(1000));
        attachment.setHashAlgorithm("Alg1");
        attachment.setHash("aaa");
        
        Configuration.getConfiguration().setValidateAfterSettingProperties(false);
	    
        attachment.setWasDerivedFrom(Arrays.asList(attachment.getUri()));  
        attachment.setNamespace(URI.create("http://sdfsf.org"));
        TestUtil.validateIdentified(attachment,doc,2);
      
        TestUtil.validateProperty(attachment, "setNamespace", new Object[] {null}, URI.class);
        attachment.setNamespace(null);
	    TestUtil.validateIdentified(attachment,doc,2);
	  
    }

}
