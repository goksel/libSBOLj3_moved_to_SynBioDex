package org.sbolstandard.core3.entity.test;

import java.io.IOException;
import java.net.URI;
import java.util.Arrays;

import org.sbolstandard.core3.api.SBOLAPI;
import org.sbolstandard.core3.entity.Attachment;
import org.sbolstandard.core3.entity.Component;
import org.sbolstandard.core3.entity.Implementation;
import org.sbolstandard.core3.entity.SBOLDocument;
import org.sbolstandard.core3.io.SBOLFormat;
import org.sbolstandard.core3.io.SBOLIO;
import org.sbolstandard.core3.test.TestUtil;
import org.sbolstandard.core3.util.SBOLGraphException;
import org.sbolstandard.core3.vocabulary.ComponentType;
import org.sbolstandard.core3.vocabulary.ModelLanguage;
import org.sbolstandard.core3.vocabulary.Role;

import junit.framework.TestCase;

public class AttachmentTest extends TestCase {
	
	public void testImplementation() throws SBOLGraphException, IOException
    {
		String baseUri="https://sbolstandard.org/examples/";
        SBOLDocument doc=new SBOLDocument(URI.create(baseUri));
        
        Component TetR_protein=SBOLAPI.createComponent(doc,"TetR_protein", ComponentType.Protein.getUrl(), "TetR", "TetR protein", Role.TF);
      
        
        Attachment attachment=doc.createAttachment("attachment1", URI.create("https://sbolstandard.org/attachment1"));
        attachment.setFormat(ModelLanguage.SBML);
        attachment.setSize(1000);
        attachment.setHash("aaa");
        attachment.setHashAlgorithm("Alg1");
        
        
        Implementation impl=doc.createImplementation("impl1");
        impl.setComponent(TetR_protein.getUri());
        impl.setAttachments(Arrays.asList(attachment.getUri()));
        
        TestUtil.serialise(doc, "entity/attachment", "attachment");
      
        System.out.println(SBOLIO.write(doc, SBOLFormat.TURTLE));
        TestUtil.assertReadWrite(doc);
    }

}
