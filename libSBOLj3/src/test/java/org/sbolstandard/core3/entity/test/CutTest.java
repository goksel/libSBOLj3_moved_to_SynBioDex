package org.sbolstandard.core3.entity.test;

import java.io.IOException;
import java.net.URI;
import java.util.Arrays;
import java.util.List;
import java.util.OptionalInt;

import org.sbolstandard.core3.api.SBOLAPI;
import org.sbolstandard.core3.entity.Collection;
import org.sbolstandard.core3.entity.Component;
import org.sbolstandard.core3.entity.ComponentReference;
import org.sbolstandard.core3.entity.Cut;
import org.sbolstandard.core3.entity.Feature;
import org.sbolstandard.core3.entity.Location;
import org.sbolstandard.core3.entity.Location.LocationBuilder;
import org.sbolstandard.core3.entity.SBOLDocument;
import org.sbolstandard.core3.entity.Sequence;
import org.sbolstandard.core3.entity.SequenceFeature;
import org.sbolstandard.core3.entity.SubComponent;
import org.sbolstandard.core3.io.SBOLFormat;
import org.sbolstandard.core3.io.SBOLIO;
import org.sbolstandard.core3.test.TestUtil;
import org.sbolstandard.core3.util.SBOLGraphException;
import org.sbolstandard.core3.vocabulary.ComponentType;
import org.sbolstandard.core3.vocabulary.Encoding;
import org.sbolstandard.core3.vocabulary.RestrictionType;
import org.sbolstandard.core3.vocabulary.Role;

import jakarta.validation.Constraint;
import junit.framework.TestCase;

public class CutTest extends TestCase {
	
	public void testConstraintReference() throws SBOLGraphException, IOException
    {
		URI base=URI.create("https://synbiohub.org/public/igem/");
		SBOLDocument doc=new SBOLDocument(base);
		
		Component pTetR=SBOLAPI.createDnaComponent(doc, URI.create("https://synbiohub.org/public/igem/BBa_R0040"), "pTetR", "TetR repressible promoter", Role.Promoter, "tccctatcagtgatagagattgacatccctatcagtgatagagatactgagcac");
	    Sequence sequence=doc.getSequences().get(0);
		LocationBuilder locationBuilder=new Location.CutLocationBuilder(5, sequence.getUri());
		
		SequenceFeature feature=pTetR.createSequenceFeature(Arrays.asList(locationBuilder));
		
		List<SequenceFeature> seqFeatures=pTetR.getSequenceFeatures();
		List<Feature> features=pTetR.getFeatures();
		assertTrue(features!=null && features.size()>0);
		assertTrue(features.size()==seqFeatures.size());
		
	    TestUtil.serialise(doc, "entity_additional/cut", "cut");
        System.out.println(SBOLIO.write(doc, SBOLFormat.TURTLE));
        TestUtil.assertReadWrite(doc); 
        
    	Cut cut=(Cut)feature.getLocations().get(0);
    	TestUtil.validateIdentified(cut,doc,0);
    	//Cut.at can't be null
    	cut.setAt(OptionalInt.empty());
    	TestUtil.validateIdentified(cut,doc,1);
    	
    	//Cut.at can't be negative
    	cut.setAt(OptionalInt.of(-5));
    	TestUtil.validateIdentified(cut,doc,1);
    	
    	cut.setAt(OptionalInt.of(5));
    	TestUtil.validateIdentified(cut,doc,0);
	
    }

}
