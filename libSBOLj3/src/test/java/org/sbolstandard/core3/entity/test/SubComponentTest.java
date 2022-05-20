package org.sbolstandard.core3.entity.test;

import java.io.IOException;
import java.net.URI;
import java.util.Arrays;
import java.util.Optional;
import org.sbolstandard.core3.api.SBOLAPI;
import org.sbolstandard.core3.entity.*;
import org.sbolstandard.core3.entity.Location.LocationBuilder;
import org.sbolstandard.core3.io.SBOLFormat;
import org.sbolstandard.core3.io.SBOLIO;
import org.sbolstandard.core3.test.TestUtil;
import org.sbolstandard.core3.util.Configuration;
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
		SubComponent termSubComponent=device.createSubComponent(term.getUri());
		termSubComponent.setOrientation(Orientation.inline);
		
		RoleIntegration ri2=termSubComponent.getRoleIntegration();
		TestUtil.validateReturnValue(termSubComponent, "toRoleIntegration", new Object[] {URI.create("http://invalidroleintegration.org")}, URI.class);
		
		Sequence i13504Sequence= device.getSequences().get(0);
		
		int start=i13504Sequence.getElements().length() + 1;
		int end=start + term_na.length()-1;
    	
		i13504Sequence.setElements(i13504Sequence.getElements() + term_na);
		LocationBuilder locationBuilder=new Location.RangeLocationBuilder(start, end,i13504Sequence);
		locationBuilder.setOrientation(Orientation.inline);
		Range range=(Range)termSubComponent.createLocation(locationBuilder);
		
		LocationBuilder locationBuilder2=new Location.RangeLocationBuilder(start+1, end,i13504Sequence);
		Range range2=(Range)termSubComponent.createSourceLocation(locationBuilder2);
		
		TestUtil.serialise(doc, "entity_additional/subcomponent", "subcomponent");
	    System.out.println(SBOLIO.write(doc, SBOLFormat.TURTLE));
	    TestUtil.assertReadWrite(doc); 
	    
	    Configuration.getConfiguration().setValidateAfterSettingProperties(false);
	     
	    TestUtil.validateIdentified(termSubComponent,doc,0);
	    
	    TestUtil.validateProperty(termSubComponent, "setIsInstanceOf", new Object[] {null}, URI.class);
	    termSubComponent.setIsInstanceOf(null);	    
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
	    
	    termSubComponent.setIsInstanceOf(term.getUri());
	    TestUtil.validateIdentified(device, 3);   
	    termSubComponent.setIsInstanceOf(device.getUri());
	    TestUtil.validateIdentified(device, 4);
	    
	   
    }
}
