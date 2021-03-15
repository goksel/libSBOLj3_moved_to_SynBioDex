package org.sbolstandard.entity.test;

import java.io.IOException;
import java.net.URI;
import java.util.Arrays;

import org.sbolstandard.TestUtil;
import org.sbolstandard.api.SBOLAPI;
import org.sbolstandard.entity.Component;
import org.sbolstandard.entity.Implementation;
import org.sbolstandard.entity.SBOLDocument;
import org.sbolstandard.io.SBOLFormat;
import org.sbolstandard.io.SBOLIO;
import org.sbolstandard.util.SBOLGraphException;
import org.sbolstandard.util.URINameSpace;
import org.sbolstandard.vocabulary.ComponentType;
import org.sbolstandard.vocabulary.ModelFramework;
import org.sbolstandard.vocabulary.ModelLanguage;
import org.sbolstandard.vocabulary.Role;

import junit.framework.TestCase;

public class ImplementationTest extends TestCase {
	
	public void testImplementation() throws SBOLGraphException, IOException
    {
		String baseUri="https://sbolstandard.org/examples/";
        SBOLDocument doc=new SBOLDocument(URI.create(baseUri));
        
        Component TetR_protein=SBOLAPI.createComponent(doc, SBOLAPI.append(baseUri, "TetR_protein"), ComponentType.Protein.getUrl(), "TetR", "TetR protein", Role.TF);
        
        
        Implementation impl=doc.createImplementation(SBOLAPI.append(baseUri,"impl1"));
        impl.setComponent(TetR_protein.getUri());
        
        TestUtil.serialise(doc, "entity/implementation", "implementation");
      
        System.out.println(SBOLIO.write(doc, SBOLFormat.TURTLE));
        TestUtil.assertReadWrite(doc);
    }


	
}
