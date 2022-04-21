package org.sbolstandard.core3.entity.test;

import java.io.IOException;
import java.net.URI;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;

import org.sbolstandard.core3.api.SBOLAPI;
import org.sbolstandard.core3.entity.Collection;
import org.sbolstandard.core3.entity.Component;
import org.sbolstandard.core3.entity.ComponentReference;
import org.sbolstandard.core3.entity.Cut;
import org.sbolstandard.core3.entity.Feature;
import org.sbolstandard.core3.entity.Location;
import org.sbolstandard.core3.entity.Location.LocationBuilder;
import org.sbolstandard.core3.entity.Range;
import org.sbolstandard.core3.entity.SBOLDocument;
import org.sbolstandard.core3.entity.Sequence;
import org.sbolstandard.core3.entity.SequenceFeature;
import org.sbolstandard.core3.entity.SubComponent;
import org.sbolstandard.core3.io.SBOLFormat;
import org.sbolstandard.core3.io.SBOLIO;
import org.sbolstandard.core3.test.TestUtil;
import org.sbolstandard.core3.util.Configuration;
import org.sbolstandard.core3.util.SBOLGraphException;
import org.sbolstandard.core3.util.Configuration.PropertyValidationType;
import org.sbolstandard.core3.vocabulary.ComponentType;
import org.sbolstandard.core3.vocabulary.Encoding;
import org.sbolstandard.core3.vocabulary.Orientation;
import org.sbolstandard.core3.vocabulary.RestrictionType;
import org.sbolstandard.core3.vocabulary.Role;

import jakarta.validation.Constraint;
import junit.framework.TestCase;

public class RangeTest extends TestCase {
	
	public void testRange() throws SBOLGraphException, IOException, Exception
    {
		URI base=URI.create("https://synbiohub.org/public/igem/");
		SBOLDocument doc=new SBOLDocument(base);
		
		String term_na="ccaggcatcaaataaaacgaaaggctcagtcgaaagactgggcctttcgttttatctgttgtttgtcggtgaacgctctc";
		Component device=doc.createComponent("i13504", Arrays.asList(ComponentType.DNA.getUrl())); 
		SBOLAPI.addSequence(doc, device, Encoding.NucleicAcid, "");
		
		Component term=SBOLAPI.createDnaComponent(doc, "B0015", "terminator", "B0015 double terminator", Role.Terminator,term_na);
		SubComponent termSubComponent=device.createSubComponent(term.getUri());
		termSubComponent.setOrientation(Orientation.inline);
		
		Sequence i13504Sequence= doc.getIdentified(device.getSequences().get(0),Sequence.class);
		
		int start=i13504Sequence.getElements().length() + 1;
		int end=start + term_na.length()-1;
    	
		i13504Sequence.setElements(i13504Sequence.getElements() + term_na);
		LocationBuilder locationBuilder=new Location.RangeLocationBuilder(start, end,i13504Sequence.getUri());
		locationBuilder.setOrientation(Orientation.inline);
		Range range=(Range)termSubComponent.createLocation(locationBuilder);
		
		
		TestUtil.serialise(doc, "entity_additional/range", "range");
	    System.out.println(SBOLIO.write(doc, SBOLFormat.TURTLE));
	    TestUtil.assertReadWrite(doc); 
	    
		Configuration.getConfiguration().setPropertyValidationType(PropertyValidationType.ValidateBeforeSavingSBOLDocuments);
	     
	    TestUtil.validateIdentified(range,doc,0);
	    
	    //Range.start cannot be empty
	    TestUtil.validateProperty(range, "setStart", new Object[] {Optional.empty()}, Optional.class);        
	    TestUtil.validateProperty(range, "setEnd", new Object[] {Optional.empty()}, Optional.class);        
	    range.setStart(Optional.empty());
	    range.setEnd(Optional.empty());
	    TestUtil.validateIdentified(range,doc,2);
	    
	    //Range.start cannot be negative
	    range.setStart(Optional.of(-1));
	    range.setEnd(Optional.of(-1));
	    TestUtil.validateIdentified(range,doc,2);
	    
	    //Range.start cannot be negative
	    range.setStart(Optional.of(0));
	    range.setEnd(Optional.of(0));
		TestUtil.validateIdentified(range,doc,2);
	    
	   //Range.start cannot be negative
	    range.setStart(Optional.of(1));
	    range.setEnd(Optional.of(2));
	    TestUtil.validateIdentified(range,doc,0);
	    

    }
}
