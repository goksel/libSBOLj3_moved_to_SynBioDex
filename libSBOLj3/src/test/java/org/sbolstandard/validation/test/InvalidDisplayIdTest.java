package org.sbolstandard.validation.test;

import java.io.IOException;
import java.net.URI;
import java.util.Arrays;

import org.apache.jena.riot.RDFFormat;
import org.sbolstandard.TestUtil;
import org.sbolstandard.api.SBOLAPI;
import org.sbolstandard.entity.Collection;
import org.sbolstandard.entity.Component;
import org.sbolstandard.entity.SBOLDocument;
import org.sbolstandard.io.SBOLFormat;
import org.sbolstandard.io.SBOLIO;
import org.sbolstandard.util.SBOLGraphException;
import org.sbolstandard.vocabulary.ComponentType;
import org.sbolstandard.vocabulary.Role;

import junit.framework.TestCase;

public class InvalidDisplayIdTest extends TestCase {

	public void testCreateInvalidDisplayId() throws SBOLGraphException, IOException
    {
		String baseUri="https://sbolstandard.org/examples/";
        SBOLDocument doc=new SBOLDocument(URI.create(baseUri));
        
       
		
        Component TetR_protein=SBOLAPI.createComponent(doc, "TetR_protein", ComponentType.Protein.getUrl(), "TetR", "TetR protein", Role.TF);
        TetR_protein.setDisplayId("1TetR");
        //Component LacI_protein=SBOLAPI.createComponent(doc, "LacI_protein", ComponentType.Protein.getUrl(), "LacI", "LacI protein", Role.TF);
        TetR_protein.setNamespace(URI.create("https://sbolstandard.org/examples/"));
		
        
        Component rbs=doc.createComponent(URI.create("https://sbolstandard.org/examples"),URI.create("https://sbolstandard.org/examples/rbs"), Arrays.asList(ComponentType.DNA.getUrl())); 
		rbs.setName("B0034");
		rbs.setDisplayId("B0034 rbs");
		rbs.setDescription("RBS (Elowitz 1999)");
		rbs.setRoles(Arrays.asList(Role.RBS));
		
        TestUtil.serialise(doc, "invalid/displayid", "displayid");
      
        System.out.println(SBOLIO.write(doc, SBOLFormat.TURTLE));
       
    }
}

	