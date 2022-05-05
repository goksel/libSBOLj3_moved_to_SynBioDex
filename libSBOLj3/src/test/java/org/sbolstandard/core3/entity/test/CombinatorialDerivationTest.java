package org.sbolstandard.core3.entity.test;

import java.io.IOException;
import java.net.URI;
import org.sbolstandard.core3.api.SBOLAPI;
import org.sbolstandard.core3.entity.*;
import org.sbolstandard.core3.io.SBOLFormat;
import org.sbolstandard.core3.io.SBOLIO;
import org.sbolstandard.core3.test.TestUtil;
import org.sbolstandard.core3.util.Configuration;
import org.sbolstandard.core3.util.SBOLGraphException;
import org.sbolstandard.core3.vocabulary.*;
import junit.framework.TestCase;

public class CombinatorialDerivationTest extends TestCase {
	
	public void testCombinatorialDerivation() throws SBOLGraphException, IOException, Exception
    {
		URI base=URI.create("https://synbiohub.org/public/igem/");
		SBOLDocument doc=new SBOLDocument(base);
		
		CombinatorialDerivation cd=doc.createCombinatorialDerivation("cs1", URI.create("http://sbolstandard.org/template"));
		
		Component pTetR=SBOLAPI.createDnaComponent(doc, URI.create("https://synbiohub.org/public/igem/BBa_R0040"), "pTetR", "TetR repressible promoter", Role.Promoter, "tccctatcagtgatagagattgacatccctatcagtgatagagatactgagcac");
		
		
		//TestUtil.serialise(doc, "entity_additional/combinatorialderivation", "combinatorialderivation");
        //System.out.println(SBOLIO.write(doc, SBOLFormat.TURTLE));
       // TestUtil.assertReadWrite(doc);
        
        Configuration.getConfiguration().setValidateAfterSettingProperties(false);
        
       // TestUtil.validateIdentified(cd, doc, 0);
        
        //template is required.
        URI tmpURI=cd.getTemplate();
        //TestUtil.validateProperty(cd, "setTemplate", new Object[] {null}, URI.class);
        cd.setTemplate(null);
        //TestUtil.validateIdentified(cd, doc, 1);
        cd.setTemplate(tmpURI);
        
        Component start=SBOLAPI.createDnaComponent(doc, URI.create("https://synbiohub.org/public/igem/BBa_R0040_start"), "pTetR_start", "promoter_start", Role.EngineeredRegion, "tccctat");
		
        pTetR.createSubComponent(start.getUri());
        VariableFeature vf=cd.createVariableFeature(VariableFeatureCardinality.One,start.getUri());
        
        //TestUtil.validateProperty(vf, "setCardinality", new Object[] {null}, VariableFeatureCardinality.class);
        vf.setCardinality(null);
        //TestUtil.validateIdentified(vf, doc, 1);
        
        //TestUtil.validateProperty(vf, "setFeature", new Object[] {null}, URI.class);
        vf.setFeature(null);
       // TestUtil.validateIdentified(vf, doc, 2);
        
        vf.setCardinality(VariableFeatureCardinality.One);
        vf.setFeature(start.getUri());
       // TestUtil.validateIdentified(vf, doc, 0);
        
        cd.setStrategy(CombinatorialDerivationStrategy.Enumerate);
        //TestUtil.validateIdentified(vf, doc, 0);
        
       // TestUtil.validateReturnValue(cd, "toStrategy", new Object[] {URI.create("http://sbols.org/v3#InvalidStrategy")}, URI.class);
        
        cd.setStrategy(CombinatorialDerivationStrategy.Enumerate);
        vf.setCardinality(VariableFeatureCardinality.OneOrMore);
        TestUtil.validateIdentified(cd, doc, 1);
        
        
 	   
    }

}
