package org.sbolstandard.core3.measure.test;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.Arrays;
import java.util.List;

import org.sbolstandard.core3.api.SBOLAPI;
import org.sbolstandard.core3.entity.Component;
import org.sbolstandard.core3.entity.ExternallyDefined;
import org.sbolstandard.core3.entity.SBOLDocument;
import org.sbolstandard.core3.entity.measure.Measure;
import org.sbolstandard.core3.entity.measure.PrefixedUnit;
import org.sbolstandard.core3.entity.measure.SIPrefix;
import org.sbolstandard.core3.entity.measure.SingularUnit;
import org.sbolstandard.core3.entity.measure.UnitDivision;
import org.sbolstandard.core3.io.SBOLFormat;
import org.sbolstandard.core3.io.SBOLIO;
import org.sbolstandard.core3.test.TestUtil;
import org.sbolstandard.core3.util.SBOLGraphException;
import org.sbolstandard.core3.util.URINameSpace;
import org.sbolstandard.core3.vocabulary.ComponentType;

import junit.framework.TestCase;

public class MeasureTest extends TestCase {
	
	public void test() throws SBOLGraphException, IOException
    {
		String baseUri="https://sbolstandard.org/examples/";
        SBOLDocument doc=new SBOLDocument(URI.create(baseUri));
        
        Component media=SBOLAPI.createComponent(doc, "M9_Glucose_CAA", ComponentType.FunctionalEntity.getUrl(), "M9 Glucose CAA", "M9 Glucose CAA growth media", null);
        ExternallyDefined CaCl2=media.createExternallyDefined(Arrays.asList(ComponentType.SimpleChemical.getUrl()), URINameSpace.CHEBI.local("3312"));
        
        
        SingularUnit liter=doc.createSingularUnit("litre", "l", "liter");
        liter.setAlternativeLabels(Arrays.asList("liter","litre2"));
        liter.setComment("The litre is a unit of volume defined as 1.0e-3 cubic metre.");
        liter.setAlternativeSymbols(Arrays.asList("L", "L2"));
        liter.setLongComment("This is an example long comment.");
        liter.setFactor(0.001f);
       
        SingularUnit mole=doc.createSingularUnit("mole", "mol", "mole");
        
        SIPrefix milli=doc.createSIPrefix("milli", "m", "milli", 0.001f);
        milli.setAlternativeLabels(Arrays.asList("milli1","milli2"));
        milli.setComment("Comment for the milli prefix.");
        milli.setAlternativeSymbols(Arrays.asList("m1", "m2"));
        milli.setLongComment("This is an example long comment for the milli prefix.");
        
        PrefixedUnit millimole=doc.createPrexiedUnit("millimole", "mmol", "millimole", mole.getUri(),milli.getUri());
        
        UnitDivision milliMolePerLiter=doc.createUnitDivision("millimolePerLitre", "mmol/l", "millimolar", millimole.getUri(), liter.getUri());
        
        //URI milliMolePerLiter=URINameSpace.OM.local("millimolePerLitre");
        Measure measure=CaCl2.createMeasure(SBOLAPI.append(CaCl2.getUri(), "measure1"), 0.1f, milliMolePerLiter.getUri());
        measure.setTypes(Arrays.asList(URINameSpace.SBO.local("0000196"),URINameSpace.SBO.local("0000197")));
        
        SingularUnit kelvin=doc.createSingularUnit("kelvin", "kelvin", "kelvin");
        doc.createUnitMultiplication("kelvinMole", "K mol", "kelvinMole", kelvin.getUri(), mole.getUri());
        
        SingularUnit meter=doc.createSingularUnit("meter", "m", "meter");
        doc.createUnitExponentiation("cubicMeter", "m3", "cubicMeter", meter.getUri(), 3);
        
        TestUtil.serialise(doc, "measurement_entity/measurement", "measurement");
        SBOLDocument doc2=SBOLIO.read(new File("output/measurement_entity/measurement/measurement.ttl"), SBOLFormat.TURTLE);
      
        System.out.println(SBOLIO.write(doc2, SBOLFormat.TURTLE));
        List<SingularUnit> units=doc2.getSingularUnits();
        if (units!=null)
        {
        	for (SingularUnit unit:units)
        	{
        		System.out.print("Name:" + unit.getName() + " ");
        		System.out.println("Factor:" + unit.getFactor());
        	}
        }
        
        List<PrefixedUnit> prefixedUnits=doc2.getPrefixedUnits();
        if (prefixedUnits!=null)
        {
        	for (PrefixedUnit unit:prefixedUnits)
        	{
        		System.out.print("Name:" + unit.getName() + " ");
        		System.out.println("Prefix:" + unit.getPrefix());
        		
        	}
        }
        
        
        TestUtil.assertReadWrite(doc);
    }

}

//        om:hasFactor          "0.001"^^<http://www.w3.org/2001/XMLSchema#float> ;