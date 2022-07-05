package org.sbolstandard.core3.entity.test;

import java.io.IOException;
import java.net.URI;
import java.util.Arrays;
import java.util.UUID;

import org.sbolstandard.core3.entity.Component;
import org.sbolstandard.core3.entity.SBOLDocument;
import org.sbolstandard.core3.io.SBOLFormat;
import org.sbolstandard.core3.io.SBOLIO;
import org.sbolstandard.core3.test.TestUtil;
import org.sbolstandard.core3.util.SBOLGraphException;
import org.sbolstandard.core3.vocabulary.ComponentType;

import junit.framework.TestCase;

public class Component_with_URN_URI_Test extends TestCase {
	
	public void testImplementation() throws SBOLGraphException, IOException
    {
		String baseUri="https://sbolstandard.org/examples/";
        SBOLDocument doc=new SBOLDocument(URI.create(baseUri));
        
       // JenaUUID test=JenaUUID.generate();
        
        //String uriString=getUniqueIri("urn:");
        URI uri=getUniqueURN();
        URI namespace=getUniqueURN();
        
        Component comp=doc.createComponent(uri, namespace, Arrays.asList(ComponentType.Protein.getUri()));
        comp.setName("TetR");
        Component compRead=(Component)doc.getIdentified(uri, Component.class);
        assertNotNull("Could not retrieve the compoenent identitied by a urn:uuid URI ",compRead);
        assertTrue ("Names are not equal", comp.getName().equals(compRead.getName()));
        
        
        TestUtil.serialise(doc, "entity/component_urn_uri", "component_urn_uri");
      
        System.out.println(SBOLIO.write(doc, SBOLFormat.TURTLE));
        TestUtil.assertReadWrite(doc);
        
    }
	
	public static URI getUniqueURN() {
		  return URI.create("urn:uuid:" + UUID.randomUUID().toString()); //<urn:uuid:0c27b730-2c64-11b2-8059-acde48001122>
				  //JenaUUID.generate().asURN());
		}
	
	/*public static String getUniqueIri(String prefix) {
		  return prefix + JenaUUID.generate().asString();
		}
	*/

}
