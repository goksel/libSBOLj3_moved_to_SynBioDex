package org.sbolstandard.core3.entity.test;

import java.io.IOException;
import java.net.URI;
import java.util.Arrays;
import org.sbolstandard.core3.api.SBOLAPI;
import org.sbolstandard.core3.entity.Collection;
import org.sbolstandard.core3.entity.Component;
import org.sbolstandard.core3.entity.SBOLDocument;
import org.sbolstandard.core3.io.SBOLFormat;
import org.sbolstandard.core3.io.SBOLIO;
import org.sbolstandard.core3.test.TestUtil;
import org.sbolstandard.core3.util.SBOLGraphException;
import org.sbolstandard.core3.vocabulary.ComponentType;
import org.sbolstandard.core3.vocabulary.Role;

import junit.framework.TestCase;

public class CollectionTest extends TestCase {
	
	public void testCollection() throws SBOLGraphException, IOException
    {
		String baseUri="https://sbolstandard.org/examples/";
        SBOLDocument doc=new SBOLDocument(URI.create(baseUri));
        
        Component TetR_protein=SBOLAPI.createComponent(doc, "TetR_protein", ComponentType.Protein.getUrl(), "TetR", "TetR protein", Role.TF);
        Component LacI_protein=SBOLAPI.createComponent(doc, "LacI_protein", ComponentType.Protein.getUrl(), "LacI", "LacI protein", Role.TF);
      
        Collection col=doc.createCollection("col1");
        //Collections can be empty
        TestUtil.validateIdentified(col,doc,0);
                
        col.setMembers(Arrays.asList(TetR_protein.getUri(), LacI_protein.getUri()));
        //Collections can have members
        TestUtil.validateIdentified(col,doc,0); 
        
        TestUtil.serialise(doc, "entity/collection", "collection");
        System.out.println(SBOLIO.write(doc, SBOLFormat.TURTLE));
        TestUtil.assertReadWrite(doc);
        
        col.setMembers(Arrays.asList(TetR_protein.getUri(), LacI_protein.getUri(), URI.create("http://invalidmemberuri.org")));
        TestUtil.validateIdentified(col,doc,0,1);
        
        
    }

}
