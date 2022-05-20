package org.sbolstandard.core3.entity.test;

import java.io.IOException;
import java.net.URI;

import org.apache.jena.rdf.model.Resource;
import org.sbolstandard.core3.api.SBOLAPI;
import org.sbolstandard.core3.entity.*;
import org.sbolstandard.core3.io.SBOLFormat;
import org.sbolstandard.core3.io.SBOLIO;
import org.sbolstandard.core3.test.TestUtil;
import org.sbolstandard.core3.util.Configuration;
import org.sbolstandard.core3.util.RDFUtil;
import org.sbolstandard.core3.util.SBOLGraphException;
import org.sbolstandard.core3.vocabulary.ComponentType;
import org.sbolstandard.core3.vocabulary.DataModel;
import org.sbolstandard.core3.vocabulary.Role;

import junit.framework.TestCase;

public class ImplementationTest extends TestCase {
	
	public void testImplementation() throws SBOLGraphException, IOException, Exception
    {
		String baseUri="https://sbolstandard.org/examples/";
        SBOLDocument doc=new SBOLDocument(URI.create(baseUri));
        
        Component TetR_protein=SBOLAPI.createComponent(doc, "TetR_protein", ComponentType.Protein.getUrl(), "TetR", "TetR protein", Role.TF);
        
        Implementation impl=doc.createImplementation("impl1");
        impl.setComponent(TetR_protein);
        
        TestUtil.serialise(doc, "entity/implementation", "implementation");
        System.out.println(SBOLIO.write(doc, SBOLFormat.TURTLE));
        TestUtil.assertReadWrite(doc);
        
        Configuration.getConfiguration().setValidateAfterSettingProperties(false);
        
        TestUtil.validateIdentified(impl,doc,0); 
        
        Attachment attachment=doc.createAttachment("test", URI.create("http://attachmentsource.com/attachment"));
    
        Resource resource = TestUtil.getResource(impl);
		//SBOL_VALID_ENTITY_TYPES - Implementation.built
		RDFUtil.setProperty(resource, DataModel.Implementation.built, attachment.getUri());
		TestUtil.validateIdentified(impl,doc,1);
		impl.setComponent(TetR_protein);
        TestUtil.validateIdentified(impl,doc,0);
	
	
    }


	
}
