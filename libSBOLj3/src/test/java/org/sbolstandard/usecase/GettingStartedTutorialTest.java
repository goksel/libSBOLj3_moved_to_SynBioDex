package org.sbolstandard.usecase;

import java.io.File;
import java.io.IOException;
import org.sbolstandard.TestUtil;
import org.sbolstandard.entity.SBOLDocument;
import org.sbolstandard.io.SBOLFormat;
import org.sbolstandard.io.SBOLIO;
import org.sbolstandard.util.SBOLGraphException;
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
	    
	   /* System.out.println("-------------------------- Now reading from input/gfp.nt --------------------------");
	    SBOLDocument doc2=SBOLIO.read(new File("input/gfp.nt"),SBOLFormat.NTRIPLES);
	    TestUtil.assertReadWrite(doc2);
	    String output2=SBOLIO.write(doc2, SBOLFormat.RDFXML);
	    System.out.println(output2);  */
	}
}