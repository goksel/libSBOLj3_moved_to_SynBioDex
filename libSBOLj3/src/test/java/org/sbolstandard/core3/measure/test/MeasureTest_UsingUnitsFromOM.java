package org.sbolstandard.core3.measure.test;

import java.io.IOException;
import java.net.URI;
import java.util.Arrays;

import org.sbolstandard.core3.api.SBOLAPI;
import org.sbolstandard.core3.entity.*;
import org.sbolstandard.core3.entity.measure.PrefixedUnit;
import org.sbolstandard.core3.entity.measure.SIPrefix;
import org.sbolstandard.core3.entity.measure.SingularUnit;
import org.sbolstandard.core3.entity.measure.UnitDivision;
import org.sbolstandard.core3.io.SBOLFormat;
import org.sbolstandard.core3.io.SBOLIO;
import org.sbolstandard.core3.test.TestUtil;
import org.sbolstandard.core3.util.SBOLGraphException;
import org.sbolstandard.core3.util.SBOLUtil;
import org.sbolstandard.core3.util.URINameSpace;
import org.sbolstandard.core3.vocabulary.ComponentType;

import junit.framework.TestCase;

public class MeasureTest_UsingUnitsFromOM extends TestCase {
	
	public void test() throws SBOLGraphException, IOException
    {
		String baseUri="https://sbolstandard.org/examples/";
        SBOLDocument doc=new SBOLDocument(URI.create(baseUri));
        
        Component media=SBOLAPI.createComponent(doc, "M9_Glucose_CAA", ComponentType.FunctionalEntity.getUrl(), "M9 Glucose CAA", "M9 Glucose CAA growth media", null);
        ExternallyDefined CaCl2=media.createExternallyDefined(Arrays.asList(ComponentType.SimpleChemical.getUrl()), URINameSpace.CHEBI.local("3312"));
        
        //URI milliMolePerLiter=URINameSpace.OM.local("millimolePerLitre");
        SingularUnit liter=doc.createSingularUnit(URINameSpace.OM.local("litre"), SBOLUtil.toNameSpace(URINameSpace.OM.getUri()), "l", "liter");
        
        
        SingularUnit mole=doc.createSingularUnit (URINameSpace.OM.local("mole"), SBOLUtil.toNameSpace(URINameSpace.OM.getUri()),"mol", "mole");
        
        SIPrefix milli=doc.createSIPrefix(URINameSpace.OM.local("milli"), SBOLUtil.toNameSpace(URINameSpace.OM.getUri()), "m", "milli", 0.001f);
       
        PrefixedUnit millimole=doc.createPrexiedUnit(URINameSpace.OM.local("millimole"), SBOLUtil.toNameSpace(URINameSpace.OM.getUri()), "mmol", "millimole", mole,milli);
        
        UnitDivision milliMolePerLiter=doc.createUnitDivision(URINameSpace.OM.local("millimolePerLitre"), SBOLUtil.toNameSpace(URINameSpace.OM.getUri()), "mmol/l", "millimolar", millimole, liter);
        
        CaCl2.createMeasure(SBOLAPI.append(CaCl2.getUri(), "measure1"), 0.1f, milliMolePerLiter);
        
        TestUtil.serialise(doc, "measurement_entity/measurement_using_units_From_OM", "measurement_using_units_From_OM");
      
        System.out.println(SBOLIO.write(doc, SBOLFormat.TURTLE));
        
        TestUtil.assertReadWrite(doc);
    }

}
