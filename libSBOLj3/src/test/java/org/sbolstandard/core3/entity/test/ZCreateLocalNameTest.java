package org.sbolstandard.core3.entity.test;


import java.io.IOException;
import java.net.URI;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.sbolstandard.core3.api.SBOLAPI;
import org.sbolstandard.core3.entity.*;
import org.sbolstandard.core3.io.SBOLFormat;
import org.sbolstandard.core3.io.SBOLIO;
import org.sbolstandard.core3.test.TestUtil;
import org.sbolstandard.core3.util.Configuration;
import org.sbolstandard.core3.util.SBOLGraphException;
import org.sbolstandard.core3.vocabulary.DataModel;
import org.sbolstandard.core3.vocabulary.Role;

import junit.framework.TestCase;

public class ZCreateLocalNameTest extends TestCase {
	
	public void testComponent() throws SBOLGraphException, IOException, Exception
    {
		URI base=URI.create("https://synbiohub.org/public/igem/");
		SBOLDocument doc=new SBOLDocument(base);
		
		Component popsReceiver=SBOLAPI.createDnaComponent(doc, "BBa_F2620", "BBa_F2620", "PoPS Receiver", Role.EngineeredGene, null); 
	   
		Configuration.getInstance().setValidateAfterSettingProperties(false);
        
		Component pTetR=SBOLAPI.createDnaComponent(doc, "pTetR", "pTetR", "TetR repressible promoter", Role.Promoter, "tccctatcagtgatagagattgacatccctatcagtgatagagatactgagcac");
		SubComponent s1= popsReceiver.createSubComponent(pTetR);
		SubComponent s2= popsReceiver.createSubComponent(pTetR);
		SubComponent s3= popsReceiver.createSubComponent(pTetR);
				
		
		removeSubComponent(doc, popsReceiver, s2);
		//model.createResource("http://sbols.org/v3#BBa_F2620/SubComponent2"));
		/*StmtIterator it= model.listStatements(recRes, model.createProperty("http://sbols.org/v3#hasFeature"), s2Res);
		while (it.hasNext()) {
			Statement stmt=it.next();
			System.out.println(stmt.getResource().getURI());	
			model.remove(stmt);
			break;			
		}*/
		
		/*String output1=RDFUtil.write(model, RDFFormat.TURTLE, null);
		System.out.println(output1);*/
		
		SubComponent s4= popsReceiver.createSubComponent(pTetR);
		removeSubComponent(doc, popsReceiver, s3);
		SubComponent s3_new= popsReceiver.createSubComponent(pTetR);
		SubComponent s5= popsReceiver.createSubComponent(pTetR);
		
		System.out.println(SBOLIO.write(doc, SBOLFormat.TURTLE));		
    }
	
	private void removeSubComponent(SBOLDocument doc, Component parent, SubComponent sc) throws Exception {
		Model model=doc.getRDFModel();
		Resource parentRes = TestUtil.getResource(parent);
		Resource scRes = TestUtil.getResource(sc);		
		model.remove(parentRes, model.createProperty(DataModel.Component.feature.toString()), scRes);
		scRes.removeProperties();		
	}

}
