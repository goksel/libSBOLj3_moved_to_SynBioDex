package org.sbolstandard.core3.measure.test;

import java.io.IOException;
import java.net.URI;
import java.util.Arrays;
import org.apache.jena.rdf.model.Resource;
import org.sbolstandard.core3.entity.*;
import org.sbolstandard.core3.entity.measure.SIPrefix;
import org.sbolstandard.core3.test.TestUtil;
import org.sbolstandard.core3.util.RDFUtil;
import org.sbolstandard.core3.util.SBOLGraphException;
import org.sbolstandard.core3.vocabulary.*;
import junit.framework.TestCase;

public class PrefixTest_14201 extends TestCase {
	
	public void testInterface() throws SBOLGraphException, IOException, Exception
    {
		String namespace="https://sbolstandard.org";
		String baseUri=namespace + "/examples/";

		SBOLDocument doc=new SBOLDocument(URI.create(baseUri));

		SIPrefix milli=doc.createSIPrefix("milli", "m", "milli", 0.001f);
	    milli.setAlternativeLabels(Arrays.asList("milli1","milli2"));
	    milli.setComment("Comment for the milli prefix.");
	    milli.setAlternativeSymbols(Arrays.asList("m1", "m2"));
	    milli.setLongComment("This is an example long comment for the milli prefix.");
	      
	        
		TestUtil.validateDocument(doc,0);
		
		milli.setLabel("milliA");
		TestUtil.validateDocument(doc,0);
		
		Resource res=TestUtil.getResource(milli);
		RDFUtil.setProperty(res, DataModel.Identified.name, "milliB");
		TestUtil.validateDocument(doc,1, "sbol3-14201");
		milli.setLabel("milliA");
		TestUtil.validateDocument(doc,0);
    }

}
