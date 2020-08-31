package org.sbolstandard.entity.test;

import java.io.IOException;
import java.net.URI;
import java.util.Arrays;

import org.sbolstandard.TestUtil;
import org.sbolstandard.api.SBOLAPI;
import org.sbolstandard.entity.Component;
import org.sbolstandard.entity.Model;
import org.sbolstandard.entity.SBOLDocument;
import org.sbolstandard.io.SBOLWriter;
import org.sbolstandard.util.SBOLGraphException;
import org.sbolstandard.util.URINameSpace;
import org.sbolstandard.vocabulary.ComponentType;
import org.sbolstandard.vocabulary.ModelFramework;
import org.sbolstandard.vocabulary.ModelLanguage;
import org.sbolstandard.vocabulary.Role;

import junit.framework.TestCase;

public class ModelTest extends TestCase {
	
	public void testInterface() throws SBOLGraphException, IOException
    {
		String baseUri="https://sbolstandard.org/examples/";
        SBOLDocument doc=new SBOLDocument(URI.create(baseUri));
        Component toggleSwitch=SBOLAPI.createComponent(doc, SBOLAPI.append(baseUri, "toggle_switch"), ComponentType.FunctionalEntity.getUrl(), "Toggle Switch", "toggle_switch", "Toggle Switch genetic circuit", null);
       
        Model model=doc.createModel(SBOLAPI.append(baseUri,"model1"), URI.create("http://virtualparts.org"), ModelFramework.Continuous,ModelLanguage.SBML);
        
        toggleSwitch.setModels(Arrays.asList(model.getUri()));
        TestUtil.serialise(doc, "entity/model", "model");
      
        System.out.println(SBOLWriter.write(doc, "Turtle"));
        TestUtil.assertReadWrite(doc);
    }

}
