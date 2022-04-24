package org.sbolstandard.core3.validation.test;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;
import org.sbolstandard.core3.api.SBOLAPI;
import org.sbolstandard.core3.entity.Component;
import org.sbolstandard.core3.entity.ExternallyDefined;
import org.sbolstandard.core3.entity.SBOLDocument;
import org.sbolstandard.core3.entity.measure.Measure;
import org.sbolstandard.core3.entity.measure.PrefixedUnit;
import org.sbolstandard.core3.entity.measure.SIPrefix;
import org.sbolstandard.core3.entity.measure.SingularUnit;
import org.sbolstandard.core3.entity.measure.UnitDivision;
import org.sbolstandard.core3.entity.measure.UnitExponentiation;
import org.sbolstandard.core3.entity.measure.UnitMultiplication;
import org.sbolstandard.core3.io.SBOLFormat;
import org.sbolstandard.core3.io.SBOLIO;
import org.sbolstandard.core3.test.TestUtil;
import org.sbolstandard.core3.util.Configuration;
import org.sbolstandard.core3.util.Configuration.PropertyValidationType;
import org.sbolstandard.core3.util.SBOLGraphException;
import org.sbolstandard.core3.util.URINameSpace;
import org.sbolstandard.core3.validation.SBOLValidator;
import org.sbolstandard.core3.vocabulary.ComponentType;

import junit.framework.TestCase;

public class ValidationReadWriteTest extends TestCase {
	
	public void test() throws SBOLGraphException, IOException
    {
		String baseUri="https://sbolstandard.org/examples/";
        SBOLDocument doc=new SBOLDocument(URI.create(baseUri));
        Configuration.getConfiguration().setPropertyValidationType(PropertyValidationType.ValidateBeforeSavingSBOLDocuments);
        Component media=SBOLAPI.createComponent(doc, "M9_Glucose_CAA", ComponentType.FunctionalEntity.getUrl(), "M9 Glucose CAA", "M9 Glucose CAA growth media", null);
        ExternallyDefined CaCl2=media.createExternallyDefined(Arrays.asList(ComponentType.SimpleChemical.getUrl()), URINameSpace.CHEBI.local("3312"));
        
        
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
        
        PrefixedUnit millimole=doc.createPrexiedUnit("millimole", "mmol", "millimole", mole.getUri(),milli.getUri());
        
        UnitDivision milliMolePerLiter=doc.createUnitDivision("millimolePerLitre", "mmol/l", "millimolar", millimole.getUri(), liter.getUri());
        
        //URI milliMolePerLiter=URINameSpace.OM.local("millimolePerLitre");
        Measure measure=CaCl2.createMeasure(SBOLAPI.append(CaCl2.getUri(), "measure1"), 0.1f, milliMolePerLiter.getUri());
        measure.setTypes(Arrays.asList(URINameSpace.SBO.local("0000196"),URINameSpace.SBO.local("0000197")));
        
        SingularUnit kelvin=doc.createSingularUnit("kelvin", "kelvin", "kelvin");
        UnitMultiplication um= doc.createUnitMultiplication("kelvinMole", "K mol", "kelvinMole", kelvin.getUri(), mole.getUri());
        
        SingularUnit meter=doc.createSingularUnit("meter", "m", "meter");
        UnitExponentiation m3=doc.createUnitExponentiation("cubicMeter", "m3", "cubicMeter", meter.getUri(), 3);
        
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
        
        Optional<Float> temp=measure.getValue();
        measure.setValue(Optional.of(4f));
        TestUtil.validateIdentified(measure,doc,  0);  
        
        measure.setValue(Optional.empty());
        TestUtil.validateIdentified(measure, doc, 1);  
      
        TestUtil.validateIdentified(CaCl2, 1);  
        measure.setValue(temp);
        TestUtil.validateIdentified(CaCl2, 0);  
         
        millimole.setPrefix(null);
        millimole.setUnit(null);
        millimole.setSymbol(null);
        millimole.setLabel(null);
        TestUtil.validateIdentified(millimole,doc, 4);  
        TestUtil.validateDocument(doc, 4);  
        
        
        milli.setFactor(Optional.empty());
        TestUtil.validateIdentified(milli,1);  
        TestUtil.validateDocument(doc,5);  
        
        milliMolePerLiter.setDenominator(null);
        milliMolePerLiter.setNumerator(null);
        TestUtil.validateIdentified(milliMolePerLiter,doc,2,7);  
        
        m3.setExponent(Optional.empty());
        m3.setBase(null);
        TestUtil.validateIdentified(m3,doc,2,9);  
        
        um.setTerm1(null);
        um.setTerm2(null);
        TestUtil.validateIdentified(um,doc,2,11);  
        
        
        //Validating the invalid SBOL document will throw exceptions	   
        boolean exception=false;
        try
 	    {
        	  boolean isValid=SBOLValidator.getValidator().isValid(doc);
 	    }
 	    catch (SBOLGraphException e)
 	    {
 	    	exception=true;
 	    	System.out.print("Validaton exception message:" + e.getMessage());
 	    }
 	    assertTrue(exception);
 	    
     
 	    String output=null;
       
 	   	//Writing the invalid SBOL document will fail	   
        exception=false;
	    try
	    {
	    	 output=SBOLIO.write(doc, SBOLFormat.TURTLE);
	    }
	    catch (SBOLGraphException e)
	    {
	    	exception=true;
	    }
	    assertTrue(exception);
	    
       
	    //Write the invalid SBOL document
	    Configuration.getConfiguration().setPropertyValidationType(PropertyValidationType.NoValidation);
	    exception=false;
	    try
	    {
	    	output=SBOLIO.write(doc, SBOLFormat.TURTLE);
	    }
	    catch (SBOLGraphException e)
	    {
	    	exception=true;
	    }
	    assertTrue(!exception);
	    
	    
	    //Reading the invalid SBOL document will fail	   
	    SBOLDocument doc3=null;
	    exception=false;
	    try
	    {
	    	doc3=SBOLIO.read(output, SBOLFormat.TURTLE);
	    }
	    catch (SBOLGraphException e)
	    {
	    	exception=true;
	    }
	    assertTrue(exception);
	   
	    
	    //Read the invalid SBOL document with errors and validate programmatically
	    Configuration.getConfiguration().setValidateAfterReadingSBOLDocuments(false);
	    exception=false;
	    try
	    {
	    	doc3=SBOLIO.read(output, SBOLFormat.TURTLE);
	    }
	    catch (SBOLGraphException e)
	    {
	    	exception=true;
	    }
	    assertTrue(!exception);
	  
	   TestUtil.validateDocument(doc3,11);  
        
    }
}

//        om:hasFactor          "0.001"^^<http://www.w3.org/2001/XMLSchema#float> ;