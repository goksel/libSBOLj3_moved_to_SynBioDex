package org.sbolstandard.core3.entity.test;

import java.io.IOException;
import java.net.URI;
import java.util.Arrays;
import java.util.List;

import org.sbolstandard.core3.api.SBOLAPI;
import org.sbolstandard.core3.entity.Collection;
import org.sbolstandard.core3.entity.Component;
import org.sbolstandard.core3.entity.ComponentReference;
import org.sbolstandard.core3.entity.Feature;
import org.sbolstandard.core3.entity.SBOLDocument;
import org.sbolstandard.core3.entity.SequenceFeature;
import org.sbolstandard.core3.entity.SubComponent;
import org.sbolstandard.core3.io.SBOLFormat;
import org.sbolstandard.core3.io.SBOLIO;
import org.sbolstandard.core3.test.TestUtil;
import org.sbolstandard.core3.util.SBOLGraphException;
import org.sbolstandard.core3.vocabulary.ComponentType;
import org.sbolstandard.core3.vocabulary.Encoding;
import org.sbolstandard.core3.vocabulary.Role;

import junit.framework.TestCase;

public class ComponentTest extends TestCase {
	
	public void testComponentReference() throws SBOLGraphException, IOException
    {
		URI base=URI.create("https://synbiohub.org/public/igem/");
		SBOLDocument doc=new SBOLDocument(base);
		
		Component popsReceiver=SBOLAPI.createDnaComponent(doc, URI.create("https://synbiohub.org/public/igem/BBa_F2620"), "BBa_F2620", "PoPS Receiver", Role.EngineeredGene, null); 
	    TestUtil.serialise(doc, "entity_additional/component", "component");
        System.out.println(SBOLIO.write(doc, SBOLFormat.TURTLE));
        TestUtil.assertReadWrite(doc);
        
		//Component.hasSequence can have zero values
		TestUtil.validateIdentified(popsReceiver,doc,0);
		
		Component pTetR=SBOLAPI.createDnaComponent(doc, URI.create("https://synbiohub.org/public/igem/BBa_R0040"), "pTetR", "TetR repressible promoter", Role.Promoter, "tccctatcagtgatagagattgacatccctatcagtgatagagatactgagcac");
		TestUtil.validateIdentified(pTetR,doc,0);
		
		//Component.hasSequence can have multiple values
		List<URI> tempList=pTetR.getSequences();
		SBOLAPI.addSequence(doc, pTetR, Encoding.NucleicAcid, "aaaa");
		TestUtil.validateIdentified(pTetR,doc,0);
		pTetR.setSequences(tempList);
		
		//Component.type is required
		tempList=pTetR.getTypes();
		pTetR.setTypes(null);
		System.out.println(SBOLIO.write(doc, SBOLFormat.TURTLE));
		TestUtil.validateIdentified(pTetR,doc,1);
		pTetR.setTypes(tempList);
		

    }

}
