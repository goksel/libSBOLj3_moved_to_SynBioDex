package org.sbolstandard.core3.usecase.test;

import java.io.File;
import java.io.IOException;
import java.util.Set;

import org.sbolstandard.core3.api.SBOLAPI;
import org.sbolstandard.core3.entity.Component;
import org.sbolstandard.core3.entity.SBOLDocument;
import org.sbolstandard.core3.io.SBOLFormat;
import org.sbolstandard.core3.io.SBOLIO;
import org.sbolstandard.core3.test.TestUtil;
import org.sbolstandard.core3.util.SBOLGraphException;
import org.sbolstandard.core3.vocabulary.ComponentType;

import junit.framework.TestCase;

/**
 * COMBINE 2020 SBOL 3 Tutorial
 * October, 2020
 * This tutorial code goes with the slides at:
 * https://github.com/SynBioDex/Community-Media/blob/master/2020/COMBINE20/SBOL3-COMBINE-2020.pptx
 */
public class GettingStartedTutorialTest  extends TestCase {

	public void testApp() throws SBOLGraphException, IOException{
		GettingStartedTutorial tutorial=new GettingStartedTutorial();
		SBOLDocument doc=tutorial.runExample();
	    TestUtil.serialise(doc, "combine2020", "combine2020");   
	    
	    Set<Component> roots=SBOLAPI.getRootComponents(doc, ComponentType.DNA);
	   
	    assertTrue("Could not find the root DNA components", roots!=null && roots.size()==2);
	    
	    for (Component comp:roots)
	    {
	    	System.out.println("Root:" + comp.getUri());
	    }
	    /* System.out.println("-------------------------- Now reading from input/gfp.nt --------------------------");
	    SBOLDocument doc2=SBOLIO.read(new File("input/gfp.nt"),SBOLFormat.NTRIPLES);
	    TestUtil.assertReadWrite(doc2);
	    String output2=SBOLIO.write(doc2, SBOLFormat.RDFXML);
	    System.out.println(output2);  */
	}
}