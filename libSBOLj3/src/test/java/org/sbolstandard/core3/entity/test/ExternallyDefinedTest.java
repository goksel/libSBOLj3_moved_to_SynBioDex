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

public class ExternallyDefinedTest extends TestCase {
	
	public void testSequenceFeature() throws SBOLGraphException, IOException, Exception
    {
		String baseUri="https://sbolstandard.org/examples/";
        SBOLDocument doc=new SBOLDocument(URI.create(baseUri));
        
    	Component ilab16_dev1=doc.createComponent("interlab16device1", Arrays.asList(ComponentType.FunctionalEntity.getUrl())); 
    	ExternallyDefined exDefined= ilab16_dev1.createExternallyDefined(Arrays.asList(ComponentType.Protein.getUrl()), URI.create("http://uniprot.org/gfp"));
    	ExternallyDefined exDefined2= ilab16_dev1.createExternallyDefined(Arrays.asList(ComponentType.Protein.getUrl()), URI.create("http://uniprot.org/rfp"));
    	 
    	TestUtil.serialise(doc, "entity_additional/externallydefined", "externallydefined");    	     
		System.out.println(SBOLIO.write(doc, SBOLFormat.TURTLE));
	    TestUtil.assertReadWrite(doc);
	    
	    Configuration.getConfiguration().setValidateAfterSettingProperties(false);
	       	     
	    TestUtil.validateIdentified(exDefined,doc,0);
	    
	    TestUtil.validateProperty(exDefined, "setDefinition", new Object[] {null}, URI.class);
        exDefined.setDefinition(null);
        TestUtil.validateIdentified(exDefined,doc,1);
        
        TestUtil.validateProperty(exDefined, "setTypes", new Object[] {null}, List.class);
        exDefined.setTypes(null);
        TestUtil.validateIdentified(exDefined,doc,2);
        
        exDefined2.setDefinition(null);
        TestUtil.validateIdentified(exDefined2,doc,1,3); 
        
        exDefined2.setTypes(Arrays.asList(ComponentType.DNA.getUrl(), ComponentType.Protein.getUrl() ));
	    TestUtil.validateIdentified(ilab16_dev1,doc,4);
        
    }
}
