package org.sbolstandard.core3.entity.test;

import java.io.IOException;
import java.net.URI;
import java.util.Arrays;
import java.util.Optional;
import org.sbolstandard.core3.api.SBOLAPI;
import org.sbolstandard.core3.entity.*;
import org.sbolstandard.core3.entity.Location.LocationBuilder;
import org.sbolstandard.core3.io.SBOLFormat;
import org.sbolstandard.core3.io.SBOLIO;
import org.sbolstandard.core3.test.TestUtil;
import org.sbolstandard.core3.util.Configuration;
import org.sbolstandard.core3.util.SBOLGraphException;
import org.sbolstandard.core3.vocabulary.*;
import junit.framework.TestCase;

public class LocationTest_11303 extends TestCase {
	
	public void testRange() throws SBOLGraphException, IOException, Exception
    {
		URI base=URI.create("https://synbiohub.org/public/igem/");
		SBOLDocument doc=new SBOLDocument(base);
		
		String term_na="ccaggcatcaaataaaacgaaaggctcagtcgaaagactgggcctttcgttttatctgttgtttgtcggtgaacgctctc";
		Component device=doc.createComponent("i13504", Arrays.asList(ComponentType.DNA.getUri())); 
		device.setRoles(Arrays.asList(Role.EngineeredGene));
		
		SBOLAPI.addSequence(doc, device, Encoding.NucleicAcid, "");
		
		Component term=SBOLAPI.createDnaComponent(doc, "B0015", "terminator", "B0015 double terminator", Role.Terminator,term_na);
		SubComponent termSubComponent=device.createSubComponent(term);
		termSubComponent.setOrientation(Orientation.inline);
		
		TestUtil.validateIdentified(device, 0);
		
		Sequence seqStart=doc.createSequence("start");
		seqStart.setElements("cca");
		seqStart.setEncoding(Encoding.NucleicAcid);
		
		Range startRange=termSubComponent.createSourceRange(1, 3, seqStart);
		TestUtil.validateIdentified(device, 1);
		startRange.setSequence(device.getSequences().get(0));
		TestUtil.validateIdentified(device, 0);
		
		startRange.setSequence(seqStart);
		TestUtil.validateIdentified(device, 1);
		
		
		
		
		
		
		
		
		Sequence secondTermSeq=doc.createSequence("term_na2");
		SequenceFeature seqFeature=device.createSequenceFeature(secondTermSeq);
		//Use a sequence that has been added to the parent (device) as an EntireSequence entity.
		startRange.setSequence(secondTermSeq);
		TestUtil.validateIdentified(device, 0);
		
		
	}
}
