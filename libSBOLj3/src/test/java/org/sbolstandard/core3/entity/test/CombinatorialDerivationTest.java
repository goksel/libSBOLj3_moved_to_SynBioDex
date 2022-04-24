package org.sbolstandard.core3.entity.test;

import java.io.IOException;
import java.net.URI;
import java.util.Arrays;
import java.util.List;
import java.util.OptionalLong;

import org.sbolstandard.core3.api.SBOLAPI;
import org.sbolstandard.core3.entity.Attachment;
import org.sbolstandard.core3.entity.Collection;
import org.sbolstandard.core3.entity.CombinatorialDerivation;
import org.sbolstandard.core3.entity.Component;
import org.sbolstandard.core3.entity.ComponentReference;
import org.sbolstandard.core3.entity.Feature;
import org.sbolstandard.core3.entity.SBOLDocument;
import org.sbolstandard.core3.entity.SequenceFeature;
import org.sbolstandard.core3.entity.SubComponent;
import org.sbolstandard.core3.entity.VariableFeature;
import org.sbolstandard.core3.io.SBOLFormat;
import org.sbolstandard.core3.io.SBOLIO;
import org.sbolstandard.core3.test.TestUtil;
import org.sbolstandard.core3.util.Configuration;
import org.sbolstandard.core3.util.SBOLGraphException;
import org.sbolstandard.core3.util.Configuration.PropertyValidationType;
import org.sbolstandard.core3.vocabulary.ComponentType;
import org.sbolstandard.core3.vocabulary.Encoding;
import org.sbolstandard.core3.vocabulary.ModelLanguage;
import org.sbolstandard.core3.vocabulary.Role;
import org.sbolstandard.core3.vocabulary.VariableFeatureCardinality;

import junit.framework.TestCase;

public class CombinatorialDerivationTest extends TestCase {
	
	public void testCombinatorialDerivation() throws SBOLGraphException, IOException, Exception
    {
		URI base=URI.create("https://synbiohub.org/public/igem/");
		SBOLDocument doc=new SBOLDocument(base);
		
		CombinatorialDerivation cd=doc.createCombinatorialDerivation("cs1", URI.create("http://sbolstandard.org/template"));
		
		Component pTetR=SBOLAPI.createDnaComponent(doc, URI.create("https://synbiohub.org/public/igem/BBa_R0040"), "pTetR", "TetR repressible promoter", Role.Promoter, "tccctatcagtgatagagattgacatccctatcagtgatagagatactgagcac");
		
		
		TestUtil.serialise(doc, "entity_additional/combinatorialderivation", "combinatorialderivation");
        System.out.println(SBOLIO.write(doc, SBOLFormat.TURTLE));
        TestUtil.assertReadWrite(doc);
        
    	Configuration.getConfiguration().setPropertyValidationType(PropertyValidationType.ValidateBeforeSavingSBOLDocuments);
        
        TestUtil.validateIdentified(cd, doc, 0);
        
        //template is required.
        URI tmpURI=cd.getTemplate();
        TestUtil.validateProperty(cd, "setTemplate", new Object[] {null}, URI.class);
        cd.setTemplate(null);
        TestUtil.validateIdentified(cd, doc, 1);
        cd.setTemplate(tmpURI);
        
        Component start=SBOLAPI.createDnaComponent(doc, URI.create("https://synbiohub.org/public/igem/BBa_R0040_start"), "pTetR_start", "promoter_start", Role.EngineeredRegion, "tccctat");
		
        pTetR.createSubComponent(start.getUri());
        VariableFeature vf=cd.createVariableFeature(VariableFeatureCardinality.One,start.getUri());
        
        TestUtil.validateProperty(vf, "setCardinality", new Object[] {null}, VariableFeatureCardinality.class);
        vf.setCardinality(null);
        TestUtil.validateIdentified(vf, doc, 1);
        
        TestUtil.validateProperty(vf, "setFeature", new Object[] {null}, URI.class);
        vf.setFeature(null);
        TestUtil.validateIdentified(vf, doc, 2);
    }

}
