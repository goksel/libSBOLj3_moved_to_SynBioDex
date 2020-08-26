package org.sbolstandard.measure.test;

import java.io.IOException;
import java.net.URI;
import java.util.Arrays;

import org.sbolstandard.TestUtil;
import org.sbolstandard.api.SBOLAPI;
import org.sbolstandard.entity.Component;
import org.sbolstandard.entity.ExternallyDefined;
import org.sbolstandard.entity.Model;
import org.sbolstandard.entity.SBOLDocument;
import org.sbolstandard.entity.measure.Measure;
import org.sbolstandard.entity.provenance.Agent;
import org.sbolstandard.io.SBOLWriter;
import org.sbolstandard.util.SBOLGraphException;
import org.sbolstandard.util.URINameSpace;
import org.sbolstandard.vocabulary.ComponentType;
import org.sbolstandard.vocabulary.ModelFramework;
import org.sbolstandard.vocabulary.ModelLanguage;
import org.sbolstandard.vocabulary.Role;

import junit.framework.TestCase;

public class MeasureTest_UsingUnitsFromOM extends TestCase {
	
	public void test() throws SBOLGraphException, IOException
    {
		String baseUri="https://sbolstandard.org/examples/";
        SBOLDocument doc=new SBOLDocument(URI.create(baseUri));
        
        Component media=SBOLAPI.createComponent(doc, SBOLAPI.append(baseUri, "M9_Glucose_CAA"), ComponentType.FunctionalEntity.getUrl(), "M9 Glucose CAA", "M9_Glucose_CAA", "M9 Glucose CAA growth media", null);
        ExternallyDefined CaCl2=media.createExternallyDefined(SBOLAPI.append(media.getUri(), "CaCl2"), Arrays.asList(ComponentType.SimpleChemical.getUrl()), URINameSpace.CHEBI.local("3312"));
        
        URI milliMolePerLiter=URINameSpace.OM.local("millimolePerLitre");
        Measure measure=CaCl2.createMeasure(SBOLAPI.append(CaCl2.getUri(), "measure1"), 0.1f, milliMolePerLiter);
        
        TestUtil.serialise(doc, "measurement_entity/measurement_using_units_From_OM", "measurement_using_units_From_OM");
      
        System.out.println(SBOLWriter.write(doc, "Turtle"));
        
        TestUtil.assertReadWrite(doc);
    }

}
