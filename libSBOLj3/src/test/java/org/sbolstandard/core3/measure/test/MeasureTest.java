package org.sbolstandard.core3.measure.test;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.sbolstandard.core3.api.SBOLAPI;
import org.sbolstandard.core3.entity.*;
import org.sbolstandard.core3.entity.measure.*;
import org.sbolstandard.core3.io.SBOLFormat;
import org.sbolstandard.core3.io.SBOLIO;
import org.sbolstandard.core3.test.TestUtil;
import org.sbolstandard.core3.util.Configuration;
import org.sbolstandard.core3.util.SBOLGraphException;
import org.sbolstandard.core3.util.URINameSpace;
import org.sbolstandard.core3.vocabulary.ComponentType;
import org.sbolstandard.core3.vocabulary.MeasureDataModel;

import junit.framework.TestCase;

public class MeasureTest extends TestCase {
	
	public void test() throws SBOLGraphException, IOException, Exception
    {
		String baseUri="https://sbolstandard.org/examples/";
        SBOLDocument doc=new SBOLDocument(URI.create(baseUri));
        
        Component media=SBOLAPI.createComponent(doc, "M9_Glucose_CAA", ComponentType.FunctionalEntity.getUri(), "M9 Glucose CAA", "M9 Glucose CAA growth media", null);
        ExternallyDefined CaCl2=media.createExternallyDefined(Arrays.asList(ComponentType.SimpleChemical.getUri()), URINameSpace.CHEBI.local("3312"));
        
        
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
        
        //URI milliMolePerLiter=URINameSpace.OM.local("millimolePerLitre");
        Measure measure=CaCl2.createMeasure(SBOLAPI.append(CaCl2.getUri(), "measure1"), 0.1f, milliMolePerLiter);
        measure.setTypes(Arrays.asList(URINameSpace.SBO.local("0000196"),URINameSpace.SBO.local("0000197")));
        
        SingularUnit kelvin=doc.createSingularUnit("kelvin", "kelvin", "kelvin");
        UnitMultiplication um= doc.createUnitMultiplication("kelvinMole", "K mol", "kelvinMole", kelvin, mole);
        
        SingularUnit meter=doc.createSingularUnit("meter", "m", "meter");
        UnitExponentiation m3=doc.createUnitExponentiation("cubicMeter", "m3", "cubicMeter", meter, 3);
        
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
        
        //TestUtil.assertReadWrite(doc);
        
        
        //measure.setTest(Optional.of(5));
        //CaCl2.g
        //TestUtil.validateIdentified(measure, 0);
        //TestUtil.validateDocument(doc, 0);
        
        /*URI tmp=measure.getUnit();
        measure.setUnit(null);
        TestUtil.validateIdentified(measure, doc, 1);
        measure.setUnit(tmp);
        TestUtil.validateIdentified(measure, doc, 0);
       */ 
        
        Configuration.getInstance().setValidateAfterSettingProperties(false);

        Optional<Float> temp=measure.getValue();
        measure.setValue(Optional.of(4f));
        TestUtil.validateIdentified(measure,doc,  0);  
        
        TestUtil.validateProperty(measure, "setValue", new Object[] {Optional.empty()}, Optional.class);
        TestUtil.validateProperty(measure, "setValue", new Object[] {null}, Optional.class);
        measure.setValue(Optional.empty());
        TestUtil.validateIdentified(measure, doc, 1);
        measure.setValue(null);
        TestUtil.validateIdentified(measure, doc, 1);
        
        TestUtil.validateIdentified(CaCl2, 1);  
        measure.setValue(temp);
        TestUtil.validateIdentified(CaCl2, 0);  
        
        TestUtil.validateProperty(measure, "setUnit", new Object[] {null}, Unit.class);
        Unit tempUnit=measure.getUnit();
        measure.setUnit(null);
        TestUtil.validateIdentified(measure, doc, 1);
        measure.setUnit(tempUnit);
        
        TestUtil.validateProperty(millimole, "setPrefix", new Object[] {null}, Prefix.class);
        TestUtil.validateProperty(millimole, "setUnit", new Object[] {null}, Unit.class);
        TestUtil.validateProperty(millimole, "setSymbol", new Object[] {null}, String.class);
        TestUtil.validateProperty(millimole, "setSymbol", new Object[] {""}, String.class);
        TestUtil.validateProperty(millimole, "setLabel", new Object[] {null}, String.class);
        TestUtil.validateProperty(millimole, "setLabel", new Object[] {""}, String.class);
        
        Prefix tempPrefix=millimole.getPrefix();
        tempUnit=millimole.getUnit();
        String tempSymbol= millimole.getSymbol();
        String tempLabel=millimole.getLabel();
       
        millimole.setPrefix(null);
        millimole.setUnit(null);
        millimole.setSymbol(null);
        millimole.setLabel(null);
        TestUtil.validateIdentified(millimole,doc, 4);  
        TestUtil.validateDocument(doc, 4);  
        millimole.setSymbol("");
        millimole.setLabel("");
        TestUtil.validateIdentified(millimole,doc, 4);  
        millimole.setPrefix(tempPrefix);
        millimole.setUnit(tempUnit);
        millimole.setSymbol(tempSymbol);
        millimole.setLabel(tempLabel);
        TestUtil.validateDocument(doc, 0);  
          
        TestUtil.validateProperty(milli, "setFactor", new Object[] {null}, Optional.class);
        TestUtil.validateProperty(milli, "setFactor", new Object[] {Optional.empty()}, Optional.class);
        Optional<Float> tempFactor=milli.getFactor();
        
        milli.setFactor(Optional.empty());
        TestUtil.validateIdentified(milli,1);  
        milli.setFactor(null);
        TestUtil.validateIdentified(milli,1);  
        milli.setFactor(tempFactor);
        
        TestUtil.validateDocument(doc,0);  
        
        TestUtil.validateProperty(milliMolePerLiter, "setDenominator", new Object[] {null}, Unit.class);
        TestUtil.validateProperty(milliMolePerLiter, "setNumerator", new Object[] {null}, Unit.class);
        
        Unit tempDen=milliMolePerLiter.getDenominator();
        Unit tempNum=milliMolePerLiter.getNumerator();
        milliMolePerLiter.setDenominator(null);
        milliMolePerLiter.setNumerator(null);
        TestUtil.validateIdentified(milliMolePerLiter,doc,2);  
        milliMolePerLiter.setDenominator(tempDen);
        milliMolePerLiter.setNumerator(tempNum);
        TestUtil.validateIdentified(milliMolePerLiter,doc,0);  
        
        
        TestUtil.validateProperty(m3, "setBase", new Object[] {null}, Unit.class);
        TestUtil.validateProperty(m3, "setExponent", new Object[] {Optional.empty()}, Optional.class);
        TestUtil.validateProperty(m3, "setExponent", new Object[] {null}, Optional.class);
        Optional<Integer> tempExp=m3.getExponent();
        Unit tempBase=m3.getBase();
        m3.setExponent(Optional.empty());
        m3.setBase(null);
        TestUtil.validateIdentified(m3,doc,2); 
        
        m3.setExponent(null);
        TestUtil.validateIdentified(m3,2); 
        m3.setExponent(tempExp);
        m3.setBase(tempBase);
        TestUtil.validateIdentified(m3,0); 
        
        
        TestUtil.validateProperty(um, "setTerm1", new Object[] {null}, Unit.class);
        TestUtil.validateProperty(um, "setTerm2", new Object[] {null}, Unit.class);
        Unit tempTerm1=um.getTerm1();
        Unit tempTerm2=um.getTerm2();
        um.setTerm1(null);
        um.setTerm2(null);
        TestUtil.validateIdentified(um,doc,2);     
        um.setTerm1(tempTerm1);
        um.setTerm2(tempTerm2);
        TestUtil.validateIdentified(um,doc,0);     
        
        BinaryPrefix kilo=doc.createBinaryPrefix("kilo", "kilo", "kilo", 1000f);
        
        
    	//SBOL_VALID_ENTITY_TYPES - Measure.Unit
		TestUtil.testValidEntity(doc, measure, measure.getUnit(), Arrays.asList(measure.getUnit(), measure), MeasureDataModel.Measure.unit);
		
		//SBOL_VALID_ENTITY_TYPES - PrefixedUnit.Unit
		TestUtil.testValidEntity(doc, millimole, millimole.getUnit(), Arrays.asList(millimole.getUnit(), measure), MeasureDataModel.PrefixedUnit.unit);
		
		//SBOL_VALID_ENTITY_TYPES - SingularUnit.Unit
		liter.setUnit(milli);//Just to provide a valid value!
		TestUtil.testValidEntity(doc, millimole, liter.getUnit(), Arrays.asList(liter.getUnit(), measure), MeasureDataModel.SingularUnit.unit);
		
		//SBOL_VALID_ENTITY_TYPES - UnitDivison.Denominator
		TestUtil.testValidEntity(doc, milliMolePerLiter, milliMolePerLiter.getDenominator(), Arrays.asList(milliMolePerLiter.getDenominator(), measure), MeasureDataModel.UnitDivision.denominator);
		
		//SBOL_VALID_ENTITY_TYPES - UnitDivison.Numerator
		TestUtil.testValidEntity(doc, milliMolePerLiter, milliMolePerLiter.getNumerator(), Arrays.asList(milliMolePerLiter.getNumerator(), measure), MeasureDataModel.UnitDivision.numerator);
				
		//SBOL_VALID_ENTITY_TYPES - UnitExponentiation.Base
		TestUtil.testValidEntity(doc, m3, m3.getBase(), Arrays.asList(m3.getBase(), measure), MeasureDataModel.UnitExponentiation.base);
		
		//SBOL_VALID_ENTITY_TYPES - UnitMultiplication.Base
		TestUtil.testValidEntity(doc, um, um.getTerm1(), Arrays.asList(um.getTerm1(), measure), MeasureDataModel.UnitMultiplication.term1);
				
		//SBOL_VALID_ENTITY_TYPES - UnitMultiplication.Base
		TestUtil.testValidEntity(doc, um, um.getTerm2(), Arrays.asList(um.getTerm2(), measure), MeasureDataModel.UnitMultiplication.term2);		
    
		TestUtil.assertReadWrite(doc);
	       
    }
}

//        om:hasFactor          "0.001"^^<http://www.w3.org/2001/XMLSchema#float> ;