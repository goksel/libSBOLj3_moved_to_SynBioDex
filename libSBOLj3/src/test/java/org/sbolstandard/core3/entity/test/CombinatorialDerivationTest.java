package org.sbolstandard.core3.entity.test;

import java.io.IOException;
import java.net.URI;
import java.util.Arrays;

import org.apache.jena.rdf.model.Resource;
import org.sbolstandard.core3.api.SBOLAPI;
import org.sbolstandard.core3.entity.*;
import org.sbolstandard.core3.io.SBOLFormat;
import org.sbolstandard.core3.io.SBOLIO;
import org.sbolstandard.core3.test.TestUtil;
import org.sbolstandard.core3.util.Configuration;
import org.sbolstandard.core3.util.RDFUtil;
import org.sbolstandard.core3.util.SBOLGraphException;
import org.sbolstandard.core3.util.SBOLUtil;
import org.sbolstandard.core3.vocabulary.*;
import junit.framework.TestCase;

public class CombinatorialDerivationTest extends TestCase {
	
	public void testCombinatorialDerivation() throws SBOLGraphException, IOException, Exception
    {
		URI base=URI.create("https://synbiohub.org/public/igem/");
		SBOLDocument doc=new SBOLDocument(base);
		
		
		Component pTetR=SBOLAPI.createDnaComponent(doc, URI.create("https://synbiohub.org/public/igem/BBa_R0040"), "pTetR", "TetR repressible promoter", Role.Promoter, "tccctatcagtgatagagattgacatccctatcagtgatagagatactgagcac");
		CombinatorialDerivation cd=doc.createCombinatorialDerivation("cs1", pTetR);
		
		
		TestUtil.serialise(doc, "entity_additional/combinatorialderivation", "combinatorialderivation");
        System.out.println(SBOLIO.write(doc, SBOLFormat.TURTLE));
        TestUtil.assertReadWrite(doc);
        
        Configuration.getConfiguration().setValidateAfterSettingProperties(false);
        
        TestUtil.validateIdentified(cd, doc, 0);
        
        //template is required.
        Component tmpURI=cd.getTemplate();
        TestUtil.validateProperty(cd, "setTemplate", new Object[] {null}, Component.class);
        cd.setTemplate(null);
        TestUtil.validateIdentified(cd, doc, 1);
        cd.setTemplate(tmpURI);
        TestUtil.validateIdentified(cd,doc,0);
		
        
        //SBOL_VALID_ENTITY_TYPES CombinatorialDerivation.template
        Resource resource = TestUtil.getResource(cd);
        RDFUtil.setProperty(resource, DataModel.CombinatorialDerivation.template, Arrays.asList(pTetR.getUri(), cd.getUri()));
		TestUtil.validateIdentified(cd,doc,1);
		RDFUtil.setProperty(resource, DataModel.CombinatorialDerivation.template, pTetR.getUri());
		TestUtil.validateIdentified(cd,doc,0);
		
		
        
        
        Component start=SBOLAPI.createDnaComponent(doc, URI.create("https://synbiohub.org/public/igem/BBa_R0040_start"), "pTetR_start", "promoter_start", Role.EngineeredRegion, "tccctat");
		
        pTetR.createSubComponent(start.getUri());
        VariableFeature vf=cd.createVariableFeature(VariableFeatureCardinality.One,start.getUri());
        
        TestUtil.validateProperty(vf, "setCardinality", new Object[] {null}, VariableFeatureCardinality.class);
        vf.setCardinality(null);
        TestUtil.validateIdentified(vf, doc, 1);
        
        TestUtil.validateProperty(vf, "setVariable", new Object[] {null}, URI.class);
        vf.setVariable(null);
        TestUtil.validateIdentified(vf, doc, 2);
        
        vf.setCardinality(VariableFeatureCardinality.One);
        vf.setVariable(start.getUri());
       TestUtil.validateIdentified(vf, doc, 0);
        
        cd.setStrategy(CombinatorialDerivationStrategy.Enumerate);
        TestUtil.validateIdentified(vf, doc, 0);
        
       TestUtil.validateReturnValue(cd, "toStrategy", new Object[] {URI.create("http://sbols.org/v3#InvalidStrategy")}, URI.class);
        
        cd.setStrategy(CombinatorialDerivationStrategy.Enumerate);
        vf.setCardinality(VariableFeatureCardinality.OneOrMore);
        TestUtil.validateIdentified(cd, doc, 1);
        vf.setCardinality(VariableFeatureCardinality.One);
        TestUtil.validateIdentified(cd, doc, 0);
        
        VariableFeature vf2=cd.createVariableFeature(VariableFeatureCardinality.One,start.getUri());
        TestUtil.validateIdentified(cd, doc, 1);
        VariableFeature vf3=cd.createVariableFeature(VariableFeatureCardinality.One,pTetR.getUri());
        VariableFeature vf4=cd.createVariableFeature(VariableFeatureCardinality.One,pTetR.getUri());
        TestUtil.validateIdentified(cd, doc, 2);
        
        TestUtil.validateReturnValue(vf4, "toCardinality", new Object[] {URI.create("http://invalidcardinality.org")}, URI.class);
        TestUtil.validateReturnValue(false, vf4, "toCardinality", new Object[] {VariableFeatureCardinality.One.getUri()}, URI.class);
        
        
          
        
        
        
        
        
        
 	   
    }

}
