package org.sbolstandard.entity.test;

import java.io.IOException;
import java.net.URI;
import java.util.Arrays;

import org.apache.jena.shared.uuid.JenaUUID;
import org.sbolstandard.TestUtil;
import org.sbolstandard.api.SBOLAPI;
import org.sbolstandard.entity.Component;
import org.sbolstandard.entity.Implementation;
import org.sbolstandard.entity.SBOLDocument;
import org.sbolstandard.io.SBOLIO;
import org.sbolstandard.util.SBOLGraphException;
import org.sbolstandard.util.URINameSpace;
import org.sbolstandard.vocabulary.ComponentType;
import org.sbolstandard.vocabulary.ModelFramework;
import org.sbolstandard.vocabulary.ModelLanguage;
import org.sbolstandard.vocabulary.Role;

import junit.framework.TestCase;

public class Component_with_URN_URI_Test extends TestCase {
	
	public void testImplementation() throws SBOLGraphException, IOException
    {
		String baseUri="https://sbolstandard.org/examples/";
        SBOLDocument doc=new SBOLDocument(URI.create(baseUri));
        
        JenaUUID test=JenaUUID.generate();
        
        //String uriString=getUniqueIri("urn:");
        URI uri=getUniqueURN();
        Component comp=doc.createComponent(uri, Arrays.asList(ComponentType.Protein.getUrl()));
        comp.setName("TetR");
        Component compRead=(Component)doc.getIdentified(uri, Component.class);
        assertNotNull("Could not retrieve the compoennt identitied by a urn:uuid URI ",compRead);
        assertTrue ("Names are not equal", comp.getName().equals(compRead.getName()));
        
        
        TestUtil.serialise(doc, "entity/component_urn_uri", "component_urn_uri");
      
        System.out.println(SBOLIO.write(doc, "Turtle"));
        TestUtil.assertReadWrite(doc);
        
    }
	
	public static URI getUniqueURN() {
		  return URI.create(JenaUUID.generate().asURN());
		}
	
	public static String getUniqueIri(String prefix) {
		  return prefix + JenaUUID.generate().asString();
		}
	

}
