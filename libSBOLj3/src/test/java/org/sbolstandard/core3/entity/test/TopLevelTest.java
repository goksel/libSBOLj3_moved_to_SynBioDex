package org.sbolstandard.core3.entity.test;

import java.io.IOException;
import java.net.URI;
import java.util.Arrays;
import java.util.OptionalLong;
import org.sbolstandard.core3.entity.*;
import org.sbolstandard.core3.io.SBOLFormat;
import org.sbolstandard.core3.io.SBOLIO;
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
       // SBOLDocument doc=new SBOLDocument();
         
        Attachment attachment=doc.createAttachment("attachment1", URI.create("https://sbolstandard.org/attachment1"));
        attachment.setFormat(ModelLanguage.SBML);
        attachment.setSize(OptionalLong.of(1000));
        attachment.setHashAlgorithm("Alg1");
        attachment.setHash("aaa");
        //attachment.setNamespace(URI.create("https://sbolstandard.org/examples"));
        System.out.println(SBOLIO.write(doc, SBOLFormat.TURTLE));
        
        Configuration.getConfiguration().setValidateAfterSettingProperties(false);
	  
       
        //TOPLEVEL_NAMESPACE_NOT_NULL
        TestUtil.validateProperty(attachment, "setNamespace", new Object[] {null}, URI.class);
        attachment.setNamespace(null);
	    TestUtil.validateIdentified(attachment,doc,1);
	    
	    //TOPLEVEL_URI_STARTS_WITH_NAMESPACE 
        attachment.setNamespace(URI.create("http://sdfsf.org"));
        TestUtil.validateIdentified(attachment,doc,1);
	    attachment.setNamespace(URI.create("https://sbolstandard.org"));
	    TestUtil.validateIdentified(attachment,doc,0);
	    
	    //TOPLEVEL_URI_PATTERN
	    Attachment attachment2=doc.createAttachment("attachment2", URI.create("https://sbolstandard.org/local/attachment3"));
	    attachment2.setNamespace(URI.create("https://sbolstandard.org"));
	    TestUtil.validateIdentified(attachment2,doc,0);
	    attachment2.setNamespace(URI.create("https://sbolstandard.org/examples"));
	    TestUtil.validateIdentified(attachment2,doc,0);
	    attachment2.setNamespace(URI.create("https://sbolstandard.org/examples/"));
	    TestUtil.validateIdentified(attachment2,doc,1);
	    attachment2.setNamespace(URI.create("https://sbolstandard.org/examples/attachment2"));
	    TestUtil.validateIdentified(attachment2,doc,1);
	    attachment2.setNamespace(URI.create("https://sbolstandard.org/examples/attach"));
	    TestUtil.validateIdentified(attachment2,doc,1);
	    attachment2.setNamespace(URI.create("https://sbolstandard.org/examples"));
	    TestUtil.validateIdentified(attachment2,doc,0);
	    
	    //TOPLEVEL_URI_CANNOT_BE_USED_AS_A_PREFIX
	    Attachment attachment3=doc.createAttachment(URI.create("https://sbolstandard.org/attachment3"), URI.create("https://sbolstandard.org"), URI.create("https://sbolstandard.org/local/attachment3"));
	    TestUtil.validateDocument(doc,0); 
	    Attachment attachment4=doc.createAttachment(URI.create("https://sbolstandard.org/attachment3/withprefix"), URI.create("https://sbolstandard.org"), URI.create("https://sbolstandard.org/local/attachment4"));
	    TestUtil.validateDocument(doc,1);  
    }

}
