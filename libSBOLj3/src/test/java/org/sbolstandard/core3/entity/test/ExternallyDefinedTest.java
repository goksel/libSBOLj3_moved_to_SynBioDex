package org.sbolstandard.core3.entity.test;

import java.io.IOException;
import java.net.URI;
import java.util.Arrays;
import java.util.List;

import org.sbolstandard.core3.api.SBOLAPI;
import org.sbolstandard.core3.entity.Component;
import org.sbolstandard.core3.entity.ExternallyDefined;
import org.sbolstandard.core3.entity.Interface;
import org.sbolstandard.core3.entity.Location;
import org.sbolstandard.core3.entity.Range;
import org.sbolstandard.core3.entity.SBOLDocument;
import org.sbolstandard.core3.entity.Sequence;
import org.sbolstandard.core3.entity.SequenceFeature;
import org.sbolstandard.core3.entity.SubComponent;
import org.sbolstandard.core3.entity.Location.RangeLocationBuilder;
import org.sbolstandard.core3.io.SBOLFormat;
import org.sbolstandard.core3.io.SBOLIO;
import org.sbolstandard.core3.test.TestUtil;
import org.sbolstandard.core3.util.Configuration;
import org.sbolstandard.core3.util.SBOLGraphException;
import org.sbolstandard.core3.util.Configuration.PropertyValidationType;
import org.sbolstandard.core3.vocabulary.ComponentType;
import org.sbolstandard.core3.vocabulary.Orientation;
import org.sbolstandard.core3.vocabulary.Role;

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
	    
		Configuration.getConfiguration().setPropertyValidationType(PropertyValidationType.ValidateBeforeSavingSBOLDocuments);
	     
	    TestUtil.validateIdentified(exDefined,doc,0);
	    
	    TestUtil.validateProperty(exDefined, "setDefinition", new Object[] {null}, URI.class);
        exDefined.setDefinition(null);
        TestUtil.validateIdentified(exDefined,doc,1);
        
        TestUtil.validateProperty(exDefined, "setTypes", new Object[] {null}, List.class);
        exDefined.setTypes(null);
        TestUtil.validateIdentified(exDefined,doc,2);
        
        exDefined2.setDefinition(null);
        TestUtil.validateIdentified(exDefined2,doc,1,3); 
    }
}
