package org.sbolstandard.usecase;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.Arrays;
import java.util.List;

import org.sbolstandard.TestUtil;
import org.sbolstandard.api.SBOLAPI;
import org.sbolstandard.entity.Component;
import org.sbolstandard.entity.ComponentReference;
import org.sbolstandard.entity.Constraint;
import org.sbolstandard.entity.Identified;
import org.sbolstandard.entity.Interaction;
import org.sbolstandard.entity.Location;
import org.sbolstandard.entity.Participation;
import org.sbolstandard.entity.Location.LocationBuilder;
import org.sbolstandard.entity.SBOLDocument;
import org.sbolstandard.entity.Sequence;
import org.sbolstandard.entity.SequenceFeature;
import org.sbolstandard.entity.SubComponent;
import org.sbolstandard.io.SBOLIO;
import org.sbolstandard.util.SBOLGraphException;
import org.sbolstandard.vocabulary.ComponentType;
import org.sbolstandard.vocabulary.DataModel;
import org.sbolstandard.vocabulary.Encoding;
import org.sbolstandard.vocabulary.InteractionType;
import org.sbolstandard.vocabulary.Orientation;
import org.sbolstandard.vocabulary.ParticipationRole;
import org.sbolstandard.vocabulary.RestrictionType;
import org.sbolstandard.vocabulary.Role;

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
	    
	    System.out.println("-------------------------- Now reading from input/gfp.nt --------------------------");
	    SBOLDocument doc2=SBOLIO.read(new File("input/gfp.nt"),"N-TRIPLES");
	    TestUtil.assertReadWrite(doc2);
	    String output2=SBOLIO.write(doc2, "RDF/XML-ABBREV");
	    System.out.println(output2);  
	}
}