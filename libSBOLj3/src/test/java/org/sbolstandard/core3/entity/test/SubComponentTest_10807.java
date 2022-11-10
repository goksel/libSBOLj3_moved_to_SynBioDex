package org.sbolstandard.core3.entity.test;

import java.io.IOException;
import java.net.URI;
import java.util.Arrays;

import org.sbolstandard.core3.api.SBOLAPI;
import org.sbolstandard.core3.entity.*;
import org.sbolstandard.core3.entity.Location.RangeLocationBuilder;
import org.sbolstandard.core3.io.SBOLFormat;
import org.sbolstandard.core3.io.SBOLIO;
import org.sbolstandard.core3.test.TestUtil;
import org.sbolstandard.core3.util.Configuration;
import org.sbolstandard.core3.util.SBOLGraphException;
import org.sbolstandard.core3.vocabulary.*;
import junit.framework.TestCase;

public class SubComponentTest_10807 extends TestCase {
	
	public void testSequenceFeature() throws SBOLGraphException, IOException, Exception
    {
		String baseUri="https://sbolstandard.org/examples/";
        SBOLDocument doc=new SBOLDocument(URI.create(baseUri));
        
        String gfp_na="atgcgtaaaggagaagaacttttca";
		Component gfp=SBOLAPI.createDnaComponent(doc, "parent", "parent", null, Role.CDS, gfp_na);
		Sequence seq= gfp.getSequences().get(0);
		
		Component region=SBOLAPI.createDnaComponent(doc, "child", "child", "child", Role.EngineeredRegion, "atgaaa");//atgaaagga
		Sequence seqRegion= region.getSequences().get(0);
		
		SubComponent feature=gfp.createSubComponent(region);
		feature.setOrientation(Orientation.inline);
		
		
		TestUtil.validateIdentified(feature,doc,0);

		//https://github.com/SynBioDex/SEPs/blob/master/sep_026.md
		
		//sbol3-10807 - If a SubComponent object has at least one hasLocation and zero sourceLocation properties, and the Component linked by its instanceOf has precisely one hasSequence property whose Sequence has a value for its elements property, then the sum of the lengths of the Location objects referred to by the hasLocation properties MUST equal the length of the elements value of the Sequence.
		
		/*Component test=SBOLAPI.createDnaComponent(doc, "test", "test", null, Role.CDS, "aaaaaa");
		feature.createRange(1, 3, test.getSequences().get(0));   
		 TestUtil.validateIdentified(feature,doc,1);*/
		 
		 
		
	    feature.createRange(1, 3, seq);   
	    feature.createRange(7, 9, seq);
	    
	    
	    TestUtil.validateIdentified(feature,doc,0);
	    
	    Configuration.getInstance().setValidateBeforeSaving(false);
	    String output=SBOLIO.write(doc, SBOLFormat.TURTLE);
	    System.out.println(output);
	    
	    
	    
    }

}
