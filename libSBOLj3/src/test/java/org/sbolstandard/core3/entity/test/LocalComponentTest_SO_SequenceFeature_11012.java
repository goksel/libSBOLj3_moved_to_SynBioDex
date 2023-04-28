package org.sbolstandard.core3.entity.test;

import java.io.IOException;
import java.net.URI;
import java.util.Arrays;
import org.sbolstandard.core3.api.SBOLAPI;
import org.sbolstandard.core3.entity.*;
import org.sbolstandard.core3.test.TestUtil;
import org.sbolstandard.core3.util.SBOLGraphException;
import org.sbolstandard.core3.util.URINameSpace;
import org.sbolstandard.core3.vocabulary.ComponentType;
import org.sbolstandard.core3.vocabulary.Role;
import junit.framework.TestCase;

public class LocalComponentTest_SO_SequenceFeature_11012 extends TestCase {

	// LOCALCOMPONENT_TYPE_IF_DNA_OR_RNA_SHOULD_INCLUDE_ONE_SO_FEATURE_ROLE
	public void testComponent() throws SBOLGraphException, IOException, Exception {
		URI base = URI.create("https://synbiohub.org/public/igem/");
		SBOLDocument doc = new SBOLDocument(base);

		Component popsReceiver = SBOLAPI.createDnaComponent(doc, "BBa_F2620", "BBa_F2620", "PoPS Receiver", Role.EngineeredGene, null);
		TestUtil.validateDocument(doc, 0);

		LocalSubComponent lsComponent = popsReceiver.createLocalSubComponent(Arrays.asList(ComponentType.DNA.getUri()));

		// No role
		TestUtil.validateDocument(doc, 1);

		// Assign one valid URI
		lsComponent.setRoles(Arrays.asList(Role.RBS));// biological_region
		TestUtil.validateDocument(doc, 0);

		// Assign two valid URIs:
		lsComponent.setRoles(Arrays.asList(Role.RBS, URINameSpace.SO.local("0001411")));// biological_region
		TestUtil.validateDocument(doc, 1);

		// Assign three valid SO URIs.
		lsComponent.setRoles(Arrays.asList(Role.RBS, URINameSpace.SO.local("0001411"), URINameSpace.SO.local("0000842")));// biological_region and gene_component_region
		TestUtil.validateDocument(doc, 1);

		// Assign an invalid URI
		lsComponent.setRoles(Arrays.asList(URINameSpace.SO.local("0000738")));// nuclear_sequence
		TestUtil.validateDocument(doc, 1);

		// Assign two invalid URIs
		lsComponent.setRoles(Arrays.asList(URINameSpace.SO.local("0000738"), URINameSpace.SO.local("0000736")));// nuclear_sequence
		TestUtil.validateDocument(doc, 1);

	}
}
