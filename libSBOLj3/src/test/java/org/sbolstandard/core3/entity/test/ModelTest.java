package org.sbolstandard.core3.entity.test;

import java.io.IOException;
import java.net.URI;
import java.util.Arrays;

import org.sbolstandard.core3.api.SBOLAPI;
import org.sbolstandard.core3.entity.Component;
import org.sbolstandard.core3.entity.Model;
import org.sbolstandard.core3.entity.SBOLDocument;
import org.sbolstandard.core3.io.SBOLFormat;
import org.sbolstandard.core3.io.SBOLIO;
import org.sbolstandard.core3.test.TestUtil;
import org.sbolstandard.core3.util.SBOLGraphException;
import org.sbolstandard.core3.vocabulary.ComponentType;
import org.sbolstandard.core3.vocabulary.ModelFramework;
import org.sbolstandard.core3.vocabulary.ModelLanguage;

import junit.framework.TestCase;

public class ModelTest extends TestCase {
	
	public void testInterface() throws SBOLGraphException, IOException
    {
		String namespace="https://sbolstandard.org";
		String baseUri=namespace + "/examples/";
        
		SBOLDocument doc=new SBOLDocument(URI.create(baseUri));
        Component toggleSwitch=SBOLAPI.createComponent(doc, "toggle_switch", ComponentType.FunctionalEntity.getUrl(), "Toggle Switch", "Toggle Switch genetic circuit", null);
        Model model=doc.createModel("model1", URI.create("http://virtualparts.org"), ModelFramework.Continuous,ModelLanguage.SBML);
        model.setNamespace(URI.create(namespace));
        toggleSwitch.setModels(Arrays.asList(model.getUri()));
        
        TestUtil.serialise(doc, "entity/model", "model");
        System.out.println(SBOLIO.write(doc, SBOLFormat.TURTLE));
        TestUtil.assertReadWrite(doc);
        
        TestUtil.validateIdentified(model,doc,0);
		model.setSource(null);
		TestUtil.validateIdentified(model,doc,1);
		model.setFramework(null);
		TestUtil.validateIdentified(model,doc,2);
		model.setLanguage(null);
		TestUtil.validateIdentified(model,doc,3);
    }

}
