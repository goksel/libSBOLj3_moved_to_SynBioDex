package org.sbolstandard.core3.entity.test;

import java.io.IOException;
import java.net.URI;
import java.util.Arrays;
import java.util.Optional;
import java.util.OptionalInt;

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

public class RangeTest_11401_11402 extends TestCase {
	
	public void testRange() throws SBOLGraphException, IOException, Exception
    {
		URI base=URI.create("https://synbiohub.org/public/igem/");
		SBOLDocument doc=new SBOLDocument(base);
		
		String term_na="ccaggcatcaaataaaacgaaaggctcagtcgaaagactgggcctttcgttttatctgttgtttgtcggtgaacgctctc";
		Component device=doc.createComponent("i13504", Arrays.asList(ComponentType.DNA.getUri())); 
		device.setRoles(Arrays.asList(Role.EngineeredGene));
		
		SBOLAPI.addSequence(doc, device, Encoding.NucleicAcid, term_na);
		
		Component term=SBOLAPI.createDnaComponent(doc, "B0015", "terminator", "B0015 double terminator", Role.Terminator,term_na);
		SubComponent termSubComponent=device.createSubComponent(term);
		termSubComponent.setOrientation(Orientation.inline);
		
		TestUtil.validateIdentified(device, 0);
		
		//Sequence seqStart = SBOLAPI.addSequence(doc, device, Encoding.NucleicAcid, "cca");
		
		Range startRange=termSubComponent.createSourceRange(1, 3, device.getSequences().get(0));
		TestUtil.validateIdentified(device, 0);
		
		//RANGE_START_VALID_FOR_SEQUENCE
		Range startRange2=termSubComponent.createSourceRange(100, 3, device.getSequences().get(0));
		TestUtil.validateIdentified(device, 2);
		
		//RANGE_END_VALID_FOR_SEQUENCE
		startRange2.setEnd(Optional.of(200));
		TestUtil.validateIdentified(device, 2);
		
		startRange2.setStart(Optional.of(1));
		startRange2.setEnd(Optional.of(20));
		TestUtil.validateIdentified(device, 0);

    }
}
