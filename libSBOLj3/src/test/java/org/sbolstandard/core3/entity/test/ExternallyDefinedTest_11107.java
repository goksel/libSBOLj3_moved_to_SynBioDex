package org.sbolstandard.core3.entity.test;

import java.io.IOException;
import java.net.URI;
import java.util.Arrays;
import org.sbolstandard.core3.entity.*;
import org.sbolstandard.core3.test.TestUtil;
import org.sbolstandard.core3.util.SBOLGraphException;
import org.sbolstandard.core3.vocabulary.ComponentType;

import junit.framework.TestCase;

public class ExternallyDefinedTest_11107 extends TestCase {

	public void testSequenceFeature() throws SBOLGraphException, IOException, Exception {
		String baseUri = "https://sbolstandard.org/examples/";
		SBOLDocument doc = new SBOLDocument(URI.create(baseUri));

		Component ilab16_dev1 = doc.createComponent("interlab16device1", Arrays.asList(ComponentType.FunctionalEntity.getUri()));
		ExternallyDefined exDefined = ilab16_dev1.createExternallyDefined(Arrays.asList(ComponentType.Protein.getUri()), URI.create("http://uniprot.org/gfp"));

		// COMPONENT_TYPE_AT_MOST_ONE_TOPOLOGY_TYPE
		TestUtil.validateIdentified(exDefined, doc, 0);
		
		exDefined.setTypes(Arrays.asList(ComponentType.DNA.getUri()));
		TestUtil.validateIdentified(exDefined, doc, 0);
		
		exDefined.setTypes(Arrays.asList(ComponentType.DNA.getUri(), ComponentType.TopologyType.Circular.getUri()));
		TestUtil.validateIdentified(exDefined, doc, 0);
		
		exDefined.setTypes(Arrays.asList(ComponentType.DNA.getUri(), ComponentType.TopologyType.Circular.getUri(), ComponentType.TopologyType.Linear.getUri()));
		TestUtil.validateIdentified(exDefined, doc, 1);
		
		exDefined.setTypes(Arrays.asList(ComponentType.Protein.getUri(), ComponentType.TopologyType.Circular.getUri(), ComponentType.TopologyType.Linear.getUri()));
		TestUtil.validateIdentified(exDefined, doc, 1);
	}
}
