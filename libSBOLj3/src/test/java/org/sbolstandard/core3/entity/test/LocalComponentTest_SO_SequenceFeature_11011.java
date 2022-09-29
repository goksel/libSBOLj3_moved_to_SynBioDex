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
import org.sbolstandard.core3.vocabulary.ComponentType;
import org.sbolstandard.core3.vocabulary.ComponentType.OptionalComponentType;
import org.sbolstandard.core3.vocabulary.DataModel;
import org.sbolstandard.core3.vocabulary.Encoding;
import org.sbolstandard.core3.vocabulary.InteractionType;
import org.sbolstandard.core3.vocabulary.ModelLanguage;
import org.sbolstandard.core3.vocabulary.ParticipationRole;
import org.sbolstandard.core3.vocabulary.Role;

import junit.framework.TestCase;

public class LocalComponentTest_SO_SequenceFeature_11011 extends TestCase {
	
	public void testComponent() throws SBOLGraphException, IOException, Exception
    {
		URI base=URI.create("https://synbiohub.org/public/igem/");
		SBOLDocument doc=new SBOLDocument(base);
		
		Component popsReceiver=SBOLAPI.createDnaComponent(doc, "BBa_F2620", "BBa_F2620", "PoPS Receiver", Role.EngineeredGene, null); 
		TestUtil.validateDocument(doc,0);
		
		Component TetR_protein=SBOLAPI.createProteinComponent(doc,popsReceiver, "TetR_protein", "TetR", "TetR protein", Role.CDS, "NNNNNNNNNNN");
		TestUtil.validateDocument(doc,1);
		
		Component LuxR_protein=SBOLAPI.createProteinComponent(doc,popsReceiver, "BBa_C0062_protein", "LuxR",  "LuxR protein", Role.CDS, "NNNNNNNNNNN");
		TestUtil.validateDocument(doc,2);
		
		LocalSubComponent lsComponent = TetR_protein.createLocalSubComponent(Arrays.asList(ComponentType.DNA.getUri()));
		lsComponent.setRoles(Arrays.asList(Role.CDS));
		TestUtil.validateDocument(doc,2);
		
		LocalSubComponent lsComponent2 = TetR_protein.createLocalSubComponent(Arrays.asList(ComponentType.Protein.getUri()));
		lsComponent2.setRoles(Arrays.asList(Role.CDS));
		TestUtil.validateDocument(doc,3);
		
		LocalSubComponent lsComponent3 = TetR_protein.createLocalSubComponent(Arrays.asList(ComponentType.Protein.getUri()));
		lsComponent3.setRoles(Arrays.asList(Role.Promoter));
		TestUtil.validateDocument(doc,4);
		
		
		
		 
    }
}
