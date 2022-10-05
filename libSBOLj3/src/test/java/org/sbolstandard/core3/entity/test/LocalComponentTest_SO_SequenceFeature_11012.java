package org.sbolstandard.core3.entity.test;

import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.OptionalLong;

import org.apache.jena.rdf.model.Resource;
import org.sbolstandard.core3.api.SBOLAPI;
import org.sbolstandard.core3.entity.Attachment;
import org.sbolstandard.core3.entity.Component;
import org.sbolstandard.core3.entity.Identified;
import org.sbolstandard.core3.entity.Interaction;
import org.sbolstandard.core3.entity.LocalSubComponent;
import org.sbolstandard.core3.entity.Model;
import org.sbolstandard.core3.entity.Participation;
import org.sbolstandard.core3.entity.SBOLDocument;
import org.sbolstandard.core3.entity.Sequence;
import org.sbolstandard.core3.entity.SubComponent;
import org.sbolstandard.core3.io.SBOLFormat;
import org.sbolstandard.core3.io.SBOLIO;
import org.sbolstandard.core3.test.TestUtil;
import org.sbolstandard.core3.util.Configuration;
import org.sbolstandard.core3.util.RDFUtil;
import org.sbolstandard.core3.util.SBOLGraphException;
import org.sbolstandard.core3.util.SBOLUtil;
import org.sbolstandard.core3.util.URINameSpace;
import org.sbolstandard.core3.vocabulary.ComponentType;
import org.sbolstandard.core3.vocabulary.ComponentType.OptionalComponentType;
import org.sbolstandard.core3.vocabulary.DataModel;
import org.sbolstandard.core3.vocabulary.Encoding;
import org.sbolstandard.core3.vocabulary.InteractionType;
import org.sbolstandard.core3.vocabulary.ModelLanguage;
import org.sbolstandard.core3.vocabulary.ParticipationRole;
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
