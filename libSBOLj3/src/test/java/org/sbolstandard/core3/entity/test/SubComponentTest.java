package org.sbolstandard.core3.entity.test;

import java.io.IOException;
import java.net.URI;
import java.util.Arrays;
import java.util.Optional;

import org.apache.jena.rdf.model.Resource;
import org.sbolstandard.core3.api.SBOLAPI;
import org.sbolstandard.core3.entity.*;
import org.sbolstandard.core3.entity.Location.LocationBuilder;
import org.sbolstandard.core3.io.SBOLFormat;
import org.sbolstandard.core3.io.SBOLIO;
import org.sbolstandard.core3.test.TestUtil;
import org.sbolstandard.core3.util.Configuration;
import org.sbolstandard.core3.util.RDFUtil;
import org.sbolstandard.core3.util.SBOLGraphException;
import org.sbolstandard.core3.vocabulary.*;
import junit.framework.TestCase;

public class SubComponentTest extends TestCase {
	
	public void testRange() throws SBOLGraphException, IOException, Exception
    {
		URI base=URI.create("https://synbiohub.org/public/igem/");
		SBOLDocument doc=new SBOLDocument(base);
		
		String term_na="ccaggcatcaaataaaacgaaaggctcagtcgaaagactgggcctttcgttttatctgttgtttgtcggtgaacgctctc";
		Component device=doc.createComponent("i13504", Arrays.asList(ComponentType.DNA.getUrl())); 
		SBOLAPI.addSequence(doc, device, Encoding.NucleicAcid, "");
		
		Component term=SBOLAPI.createDnaComponent(doc, "B0015", "terminator", "B0015 double terminator", Role.Terminator,term_na);
		SubComponent termSubComponent=device.createSubComponent(term);
		termSubComponent.setOrientation(Orientation.inline);
		
		RoleIntegration ri2=termSubComponent.getRoleIntegration();
		TestUtil.validateReturnValue(termSubComponent, "toRoleIntegration", new Object[] {URI.create("http://invalidroleintegration.org")}, URI.class);
		
		Sequence i13504Sequence= device.getSequences().get(0);
		
		int start=i13504Sequence.getElements().length() + 1;
		int end=start + term_na.length()-1;
    	
		i13504Sequence.setElements(i13504Sequence.getElements() + term_na);
		Range range=(Range)termSubComponent.createRange(start, end,i13504Sequence);
		range.setOrientation(Orientation.inline);
		
		Range range2=(Range)termSubComponent.createSourceRange(start+1, end,i13504Sequence);
		
		TestUtil.serialise(doc, "entity_additional/subcomponent", "subcomponent");
	    System.out.println(SBOLIO.write(doc, SBOLFormat.TURTLE));
	    TestUtil.assertReadWrite(doc); 
	    
	    Configuration.getConfiguration().setValidateAfterSettingProperties(false);
	     
	    TestUtil.validateIdentified(termSubComponent,doc,0);
	    
	    TestUtil.validateProperty(termSubComponent, "setInstanceOf", new Object[] {null}, Component.class);
	    termSubComponent.setInstanceOf(null);	    
	    range.setEnd(Optional.empty());
	    range2.setEnd(Optional.empty());
	    TestUtil.validateIdentified(termSubComponent,doc,3);
	    termSubComponent.setRoleIntegration(null);
	    TestUtil.validateIdentified(termSubComponent,doc,3);
	    termSubComponent.setRoleIntegration(RoleIntegration.mergeRoles);
	    TestUtil.validateIdentified(termSubComponent,doc,3);
	    
	    //Roles must be provided if roleIntegration is not nulls
	    termSubComponent.setRoleIntegration(null);
	    termSubComponent.setRoles(Arrays.asList(URI.create("http://testrole.org")));
	    TestUtil.validateIdentified(termSubComponent,doc,4);
	    
	    termSubComponent.setInstanceOf(term);
	    TestUtil.validateIdentified(device, 3);   
	    termSubComponent.setInstanceOf(device);
	    TestUtil.validateIdentified(device, 4);
	    
	    //Clean the errors
	    termSubComponent.setInstanceOf(term);
	    TestUtil.validateIdentified(device, 3);
	    range.setEnd(Optional.of(end));
	    range2.setEnd(Optional.of(end));
	    termSubComponent.setRoleIntegration(RoleIntegration.mergeRoles);
	    TestUtil.validateIdentified(device, 0);
	    
	    
	    Resource resource = TestUtil.getResource(termSubComponent);
		
	    //SBOL_VALID_ENTITY_TYPES - SubComponent.instanceOf
	    Component instanceOf=termSubComponent.getInstanceOf();
	  	RDFUtil.setProperty(resource, DataModel.SubComponent.instanceOf, Arrays.asList(instanceOf.getUri(), range.getUri()));
	  	TestUtil.validateIdentified(termSubComponent,doc,1);
	  	termSubComponent.setInstanceOf(term);
	  	TestUtil.validateIdentified(termSubComponent,doc,0);	
	  	
	  	Cut cutSource=termSubComponent.createSourceCut(1, i13504Sequence);
	  	RDFUtil.setProperty(resource, DataModel.SubComponent.sourceLocation, Arrays.asList(cutSource.getUri(), i13504Sequence.getUri()));
	  	TestUtil.validateIdentified(termSubComponent,doc,1);
	  	RDFUtil.setProperty(resource, DataModel.SubComponent.sourceLocation, Arrays.asList(cutSource.getUri()));
	  	TestUtil.validateIdentified(termSubComponent,doc,0);	
	  	
	  
    }
}
