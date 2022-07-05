package org.sbolstandard.core3.entity.test;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

public class LocalSubComponentTest extends TestCase {
	
	public void testLocalSubComponent() throws SBOLGraphException, IOException, Exception
    {
		String baseUri="https://sbolstandard.org/examples/";
        SBOLDocument doc=new SBOLDocument(URI.create(baseUri));
        
        Component i13504_system=SBOLAPI.createComponent(doc,"i13504_system", ComponentType.DNA.getUri(), "i13504 system", null, Role.FunctionalCompartment);
		
        LocalSubComponent lsComponent = i13504_system.createLocalSubComponent(Arrays.asList(ComponentType.DNA.getUri()));
        
        TestUtil.serialise(doc, "entity_additional/localsubcomponent", "localsubcomponent");
        System.out.println(SBOLIO.write(doc, SBOLFormat.TURTLE));
        TestUtil.assertReadWrite(doc);
        
        Configuration.getInstance().setValidateAfterSettingProperties(false);
        
        TestUtil.validateIdentified(lsComponent,doc,0);
        //LocalSubComponent.types cannot be empty
        TestUtil.validateProperty(lsComponent, "setTypes", new Object[] {null}, List.class);
        TestUtil.validateProperty(lsComponent, "setTypes", new Object[] {new ArrayList<URI>()}, List.class);
        
        List<URI> tempTypes=lsComponent.getTypes();
        lsComponent.setTypes(null);
        TestUtil.validateIdentified(lsComponent,doc,1);
        
        lsComponent.setTypes(new ArrayList<URI>());
        TestUtil.validateIdentified(lsComponent,doc,1);
        lsComponent.setTypes(tempTypes);
        TestUtil.validateIdentified(lsComponent,doc,0);
        
        lsComponent.setTypes(Arrays.asList(ComponentType.DNA.getUri(), ComponentType.Protein.getUri() ));
	    TestUtil.validateIdentified(lsComponent,doc,1);
	    lsComponent.setTypes(Arrays.asList(ComponentType.DNA.getUri()));
	    TestUtil.validateIdentified(lsComponent,doc,0);
	    
	    Sequence seq=doc.createSequence("seq1");
	    lsComponent.createCut(2, seq);
	    TestUtil.validateIdentified(lsComponent,doc,0);
		
	    Resource resource = TestUtil.getResource(lsComponent);
		
		//SBOL_VALID_ENTITY_TYPES - LocalSubComponent.locations
		List<URI> tempURIs=SBOLUtil.getURIs(lsComponent.getLocations());
		tempURIs.add(i13504_system.getUri());
		RDFUtil.setProperty(resource, lsComponent.getDefaultLocationProperty(), tempURIs);
		TestUtil.validateIdentified(lsComponent,doc,1);
		tempURIs.remove(i13504_system.getUri());
		RDFUtil.setProperty(resource, lsComponent.getDefaultLocationProperty(), tempURIs);
		TestUtil.validateIdentified(lsComponent,doc,0);

	    //LOCALSUBCOMPONENT_TYPE_FROM_TABLE2
        lsComponent.setTypes(tempTypes);
	    lsComponent.setTypes(Arrays.asList(ComponentType.OptionalComponentType.Cell.getUri()));
	    TestUtil.validateIdentified(lsComponent,doc,1);
        lsComponent.setTypes(tempTypes);
	    lsComponent.setTypes(Arrays.asList(ComponentType.DNA.getUri()));
	    TestUtil.validateIdentified(lsComponent,doc,0);
	    lsComponent.setTypes(Arrays.asList(ComponentType.DNA.getUri(), ComponentType.OptionalComponentType.Cell.getUri()));
	    TestUtil.validateIdentified(lsComponent,doc,0);
	    lsComponent.setTypes(Arrays.asList(ComponentType.TopologyType.Linear.getUri(), ComponentType.OptionalComponentType.Cell.getUri()));
	    TestUtil.validateIdentified(lsComponent,doc,2);
	    lsComponent.setTypes(Arrays.asList(ComponentType.Protein.getUri(), ComponentType.TopologyType.Linear.getUri(), ComponentType.TopologyType.Circular.getUri()));
	    TestUtil.validateIdentified(lsComponent,doc,1);
	    lsComponent.setTypes(Arrays.asList(ComponentType.DNA.getUri(), ComponentType.TopologyType.Linear.getUri(), ComponentType.TopologyType.Circular.getUri()));
	    TestUtil.validateIdentified(lsComponent,doc,1);
	    lsComponent.setTypes(Arrays.asList(ComponentType.RNA.getUri(), ComponentType.TopologyType.Linear.getUri(), ComponentType.TopologyType.Circular.getUri()));
	    TestUtil.validateIdentified(lsComponent,doc,1);
	    lsComponent.setTypes(Arrays.asList(ComponentType.RNA.getUri(), ComponentType.TopologyType.Linear.getUri()));
	    TestUtil.validateIdentified(lsComponent,doc,0);
	    lsComponent.setTypes(Arrays.asList(ComponentType.StrandType.Double.getUri(), ComponentType.Protein.getUri()));
	    TestUtil.validateIdentified(lsComponent,doc,1);
	    lsComponent.setTypes(Arrays.asList(ComponentType.StrandType.Double.getUri(), ComponentType.DNA.getUri()));
	    TestUtil.validateIdentified(lsComponent,doc,0);
		    
	    
	    
	    
	    
	    
	    
	    // ensure it validates is optional checks are disabled
		Configuration.getInstance().setValidateRecommendedRules(false);
	    lsComponent.setTypes(Arrays.asList(ComponentType.OptionalComponentType.Cell.getUri()));
	    TestUtil.validateIdentified(lsComponent,doc,0);

		Configuration.getInstance().setValidateRecommendedRules(true);
		
		
		
    }	
	
}
