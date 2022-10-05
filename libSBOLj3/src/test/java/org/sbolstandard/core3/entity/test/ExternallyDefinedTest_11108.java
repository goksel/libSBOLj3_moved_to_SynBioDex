package org.sbolstandard.core3.entity.test;

import java.io.IOException;
import java.net.URI;
import java.util.Arrays;
import java.util.List;
import org.sbolstandard.core3.entity.*;
import org.sbolstandard.core3.io.SBOLFormat;
import org.sbolstandard.core3.io.SBOLIO;
import org.sbolstandard.core3.test.TestUtil;
import org.sbolstandard.core3.util.Configuration;
import org.sbolstandard.core3.util.SBOLGraphException;
import org.sbolstandard.core3.vocabulary.ComponentType;

import junit.framework.TestCase;

public class ExternallyDefinedTest_11108 extends TestCase {

	public void testSequenceFeature() throws SBOLGraphException, IOException, Exception {
		String baseUri = "https://sbolstandard.org/examples/";
		SBOLDocument doc = new SBOLDocument(URI.create(baseUri));

		Component ilab16_dev1 = doc.createComponent("interlab16device1", Arrays.asList(ComponentType.FunctionalEntity.getUri()));
		ExternallyDefined exDefined = ilab16_dev1.createExternallyDefined(Arrays.asList(ComponentType.DNA.getUri()), URI.create("http://uniprot.org/gfp"));

		//EXTERNALLYDEFINED_TYPE_ONLY_DNA_OR_RNA_INCLUDE_STRAND_OR_TOPOLOGY
		exDefined.setTypes(Arrays.asList(ComponentType.OptionalComponentType.Cell.getUri()));
	    TestUtil.validateIdentified(exDefined,doc,0);
	    
	    exDefined.setTypes(Arrays.asList(ComponentType.OptionalComponentType.Cell.getUri()));
	    TestUtil.validateDocument(doc,0);
        
	    exDefined.setTypes(Arrays.asList(ComponentType.DNA.getUri()));
	    TestUtil.validateIdentified(exDefined,doc,0);
	    
	    exDefined.setTypes(Arrays.asList(ComponentType.DNA.getUri(), ComponentType.OptionalComponentType.Cell.getUri()));
	    TestUtil.validateIdentified(exDefined,doc,0);
	    
	    exDefined.setTypes(Arrays.asList(ComponentType.TopologyType.Linear.getUri(), ComponentType.OptionalComponentType.Cell.getUri()));
	    TestUtil.validateDocument(doc,1);
	    
	    exDefined.setTypes(Arrays.asList(ComponentType.Protein.getUri(), ComponentType.TopologyType.Linear.getUri(), ComponentType.TopologyType.Circular.getUri()));
	    TestUtil.validateDocument(doc,1);
	    
	    exDefined.setTypes(Arrays.asList(ComponentType.DNA.getUri(), ComponentType.TopologyType.Linear.getUri(), ComponentType.TopologyType.Circular.getUri()));
	    TestUtil.validateIdentified(exDefined,doc,1);
	    
	    exDefined.setTypes(Arrays.asList(ComponentType.RNA.getUri(), ComponentType.TopologyType.Linear.getUri(), ComponentType.TopologyType.Circular.getUri()));
	    TestUtil.validateIdentified(exDefined,doc,1);
	    
	    exDefined.setTypes(Arrays.asList(ComponentType.RNA.getUri(), ComponentType.TopologyType.Linear.getUri()));
	    TestUtil.validateIdentified(exDefined,doc,0);
	    
	    exDefined.setTypes(Arrays.asList(ComponentType.StrandType.Double.getUri(), ComponentType.Protein.getUri()));
	    TestUtil.validateDocument(doc,1);
	    
	    exDefined.setTypes(Arrays.asList(ComponentType.StrandType.Double.getUri(), ComponentType.DNA.getUri()));
	    TestUtil.validateIdentified(exDefined,doc,0);
	}
}
