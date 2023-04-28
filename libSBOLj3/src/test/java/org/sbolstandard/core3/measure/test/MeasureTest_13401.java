package org.sbolstandard.core3.measure.test;

import java.io.IOException;
import java.net.URI;
import java.util.Arrays;
import java.util.Optional;

import org.sbolstandard.core3.api.SBOLAPI;
import org.sbolstandard.core3.entity.*;
import org.sbolstandard.core3.entity.measure.Measure;
import org.sbolstandard.core3.entity.measure.PrefixedUnit;
import org.sbolstandard.core3.entity.measure.SIPrefix;
import org.sbolstandard.core3.entity.measure.SingularUnit;
import org.sbolstandard.core3.entity.measure.UnitDivision;
import org.sbolstandard.core3.test.TestUtil;
import org.sbolstandard.core3.util.SBOLGraphException;
import org.sbolstandard.core3.util.URINameSpace;
import org.sbolstandard.core3.vocabulary.*;
import junit.framework.TestCase;

public class MeasureTest_13401 extends TestCase {
	
	public void testInterface() throws SBOLGraphException, IOException, Exception
    {
		String namespace="https://sbolstandard.org";
		String baseUri=namespace + "/examples/";

		SBOLDocument doc=new SBOLDocument(URI.create(baseUri));

		SingularUnit liter=doc.createSingularUnit("litre", "l", "liter");
		liter.setAlternativeLabels(Arrays.asList("liter","litre2"));
		liter.setComment("The litre is a unit of volume defined as 1.0e-3 cubic metre.");
		liter.setAlternativeSymbols(Arrays.asList("L", "L2"));
		liter.setLongComment("This is an example long comment.");
		liter.setFactor(Optional.of(0.001f));

		SingularUnit mole=doc.createSingularUnit("mole", "mol", "mole");

		SIPrefix milli=doc.createSIPrefix("milli", "m", "milli", 0.001f);
		milli.setAlternativeLabels(Arrays.asList("milli1","milli2"));
		milli.setComment("Comment for the milli prefix.");
		milli.setAlternativeSymbols(Arrays.asList("m1", "m2"));
		milli.setLongComment("This is an example long comment for the milli prefix.");

		PrefixedUnit millimole=doc.createPrexiedUnit("millimole", "mmol", "millimole", mole,milli);

		UnitDivision milliMolePerLiter=doc.createUnitDivision("millimolePerLitre", "mmol/l", "millimolar", millimole, liter);


		Component media=SBOLAPI.createComponent(doc, "M9_Glucose_CAA", ComponentType.FunctionalEntity.getUri(), "M9 Glucose CAA", "M9 Glucose CAA growth media", null);

		ExternallyDefined CaCl2=media.createExternallyDefined(Arrays.asList(ComponentType.SimpleChemical.getUri()), URINameSpace.CHEBI.local("3312"));

		//URI milliMolePerLiter=URINameSpace.OM.local("millimolePerLitre");
		Measure measure=CaCl2.createMeasure(SBOLAPI.append(CaCl2.getUri(), "measure1"), 0.1f, milliMolePerLiter);
		measure.setTypes(Arrays.asList(URINameSpace.SBO.local("0000196"),URINameSpace.SBO.local("0000197")));
		
		TestUtil.validateDocument(doc,0); 
		measure.setTypes(Arrays.asList(URI.create("http://nonsbotype")));
		TestUtil.validateDocument(doc,1,"sbol3-13401");
		measure.setTypes(Arrays.asList(URI.create("http://nonsbotype"), URINameSpace.SBO.local("0000196")));
		TestUtil.validateDocument(doc,0);
		measure.setTypes(Arrays.asList(URINameSpace.SBO.local("0000196")));
		TestUtil.validateDocument(doc,0);
		measure.setTypes(Arrays.asList(URINameSpace.SBO.local("0000544")));//metadata representation term
		TestUtil.validateDocument(doc,1);
		
		
		
		
		
		

    }

}
