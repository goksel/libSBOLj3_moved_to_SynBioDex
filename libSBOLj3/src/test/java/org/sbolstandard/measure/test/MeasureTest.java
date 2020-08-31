package org.sbolstandard.measure.test;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.Arrays;
import java.util.List;

import org.sbolstandard.TestUtil;
import org.sbolstandard.api.SBOLAPI;
import org.sbolstandard.entity.Component;
import org.sbolstandard.entity.ExternallyDefined;
import org.sbolstandard.entity.Model;
import org.sbolstandard.entity.SBOLDocument;
import org.sbolstandard.entity.measure.Measure;
import org.sbolstandard.entity.measure.PrefixedUnit;
import org.sbolstandard.entity.measure.SIPrefix;
import org.sbolstandard.entity.measure.SingularUnit;
import org.sbolstandard.entity.measure.UnitDivision;
import org.sbolstandard.entity.measure.UnitExponentiation;
import org.sbolstandard.entity.measure.UnitMultiplication;
import org.sbolstandard.entity.provenance.Agent;
import org.sbolstandard.io.SBOLWriter;
import org.sbolstandard.util.SBOLGraphException;
import org.sbolstandard.util.URINameSpace;
import org.sbolstandard.vocabulary.ComponentType;
import org.sbolstandard.vocabulary.ModelFramework;
import org.sbolstandard.vocabulary.ModelLanguage;
import org.sbolstandard.vocabulary.Role;

import junit.framework.TestCase;

public class MeasureTest extends TestCase {
	
	public void test() throws SBOLGraphException, IOException
    {
		String baseUri="https://sbolstandard.org/examples/";
        SBOLDocument doc=new SBOLDocument(URI.create(baseUri));
        
        Component media=SBOLAPI.createComponent(doc, SBOLAPI.append(baseUri, "M9_Glucose_CAA"), ComponentType.FunctionalEntity.getUrl(), "M9 Glucose CAA", "M9_Glucose_CAA", "M9 Glucose CAA growth media", null);
        ExternallyDefined CaCl2=media.createExternallyDefined(SBOLAPI.append(media.getUri(), "CaCl2"), Arrays.asList(ComponentType.SimpleChemical.getUrl()), URINameSpace.CHEBI.local("3312"));
        
        
        SingularUnit liter=doc.createSingularUnit(SBOLAPI.append(baseUri, "litre"), "l", "liter");
        liter.setAlternativeLabels(Arrays.asList("liter","litre2"));
        liter.setComment("The litre is a unit of volume defined as 1.0e-3 cubic metre.");
        liter.setAlternativeSymbols(Arrays.asList("L", "L2"));
        liter.setLongComment("This is an example long comment.");
        liter.setFactor(0.001f);
       
        SingularUnit mole=doc.createSingularUnit(SBOLAPI.append(baseUri, "mole"), "mol", "mole");
        
        SIPrefix milli=doc.createSIPrefix(SBOLAPI.append(baseUri, "milli"), "m", "milli", 0.001f);
        milli.setAlternativeLabels(Arrays.asList("milli1","milli2"));
        milli.setComment("Comment for the milli prefix.");
        milli.setAlternativeSymbols(Arrays.asList("m1", "m2"));
        milli.setLongComment("This is an example long comment for the milli prefix.");
        
        
        PrefixedUnit millimole=doc.createPrexiedUnit(SBOLAPI.append(baseUri, "millimole"), "mmol", "millimole", mole.getUri(),milli.getUri());
       
        
        UnitDivision milliMolePerLiter=doc.createUnitDivision(SBOLAPI.append(baseUri, "millimolePerLitre"), "mmol/l", "millimolar", millimole.getUri(), liter.getUri());
        
        //URI milliMolePerLiter=URINameSpace.OM.local("millimolePerLitre");
        Measure measure=CaCl2.createMeasure(SBOLAPI.append(CaCl2.getUri(), "measure1"), 0.1f, milliMolePerLiter.getUri());
        measure.setTypes(Arrays.asList(URINameSpace.SBO.local("0000196"),URINameSpace.SBO.local("0000197")));
        
        
        SingularUnit kelvin=doc.createSingularUnit(SBOLAPI.append(baseUri, "kelvin"), "kelvin", "kelvin");
        UnitMultiplication kelvinMole=doc.createUnitMultiplication(SBOLAPI.append(baseUri, "kelvinMole"), "K mol", "kelvinMole", kelvin.getUri(), mole.getUri());
        
        SingularUnit meter=doc.createSingularUnit(SBOLAPI.append(baseUri, "meter"), "m", "meter");
        UnitExponentiation cubicMeter=doc.createUnitExponentiation(SBOLAPI.append(baseUri, "cubicMeter"), "m3", "cubicMeter", meter.getUri(), 3);
         
        
        
        TestUtil.serialise(doc, "measurement_entity/measurement", "measurement");
        SBOLDocument doc2=SBOLWriter.read(new File("output/measurement_entity/measurement/measurement.ttl"), "turtle");
      
        System.out.println(SBOLWriter.write(doc2, "Turtle"));
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