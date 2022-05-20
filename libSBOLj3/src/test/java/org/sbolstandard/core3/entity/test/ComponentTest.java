package org.sbolstandard.core3.entity.test;

import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.OptionalLong;

import org.apache.jena.rdf.model.Resource;
import org.sbolstandard.core3.api.SBOLAPI;
import org.sbolstandard.core3.entity.Attachment;
import org.sbolstandard.core3.entity.Component;
import org.sbolstandard.core3.entity.Identified;
import org.sbolstandard.core3.entity.Interaction;
import org.sbolstandard.core3.entity.Model;
import org.sbolstandard.core3.entity.Participation;
import org.sbolstandard.core3.entity.SBOLDocument;
import org.sbolstandard.core3.entity.Sequence;
import org.sbolstandard.core3.entity.SubComponent;
import org.sbolstandard.core3.io.SBOLFormat;
import org.sbolstandard.core3.io.SBOLIO;
import org.sbolstandard.core3.test.TestUtil;
import org.sbolstandard.core3.util.Configuration;
import org.sbolstandard.core3.util.RDFUtil;
import org.sbolstandard.core3.util.SBOLGraphException;
import org.sbolstandard.core3.util.SBOLUtil;
import org.sbolstandard.core3.vocabulary.ComponentType;
import org.sbolstandard.core3.vocabulary.DataModel;
import org.sbolstandard.core3.vocabulary.Encoding;
import org.sbolstandard.core3.vocabulary.InteractionType;
import org.sbolstandard.core3.vocabulary.ModelLanguage;
import org.sbolstandard.core3.vocabulary.ParticipationRole;
import org.sbolstandard.core3.vocabulary.Role;

import junit.framework.TestCase;

public class ComponentTest extends TestCase {
	
	public void testComponent() throws SBOLGraphException, IOException, Exception
    {
		URI base=URI.create("https://synbiohub.org/public/igem/");
		SBOLDocument doc=new SBOLDocument(base);
		
		Component popsReceiver=SBOLAPI.createDnaComponent(doc, URI.create("https://synbiohub.org/public/igem/BBa_F2620"), "BBa_F2620", "PoPS Receiver", Role.EngineeredGene, null); 
	    TestUtil.serialise(doc, "entity_additional/component", "component");
        System.out.println(SBOLIO.write(doc, SBOLFormat.TURTLE));
        TestUtil.assertReadWrite(doc);
        
        Configuration.getConfiguration().setValidateAfterSettingProperties(false);
        
        
		//Component.hasSequence can have zero values
		TestUtil.validateIdentified(popsReceiver,doc,0);
		
		Component pTetR=SBOLAPI.createDnaComponent(doc, URI.create("https://synbiohub.org/public/igem/BBa_R0040"), "pTetR", "TetR repressible promoter", Role.Promoter, "tccctatcagtgatagagattgacatccctatcagtgatagagatactgagcac");
		TestUtil.validateIdentified(pTetR,doc,0);
		
		//Component.hasSequence can have multiple values
		List<Sequence> tempSequences=pTetR.getSequences();
		SBOLAPI.addSequence(doc, pTetR, Encoding.NucleicAcid, "aaaa");
		TestUtil.validateIdentified(pTetR,doc,0);
		pTetR.setSequences(tempSequences);
		
		Resource resource = TestUtil.getResource(pTetR);
		
		//SBOL_VALID_ENTITY_TYPES - Component.Sequences
		RDFUtil.setProperty(resource, DataModel.Component.sequence, popsReceiver.getUri());
		TestUtil.validateIdentified(pTetR,doc,1);
		RDFUtil.setProperty(resource, DataModel.Component.sequence, Arrays.asList(popsReceiver.getUri(), pTetR.getUri(), tempSequences.get(0).getUri()));	
		TestUtil.validateIdentified(pTetR,doc,2);
		pTetR.setSequences(tempSequences);
		TestUtil.validateIdentified(pTetR,doc,0);
		
		//SBOL_VALID_ENTITY_TYPES - Component.Models
		List<Model> tempModels=pTetR.getModels();
		RDFUtil.setProperty(resource, DataModel.Component.model, SBOLUtil.getURIs(tempSequences));
		TestUtil.validateIdentified(pTetR,doc,1);
		pTetR.setModels(tempModels);
		TestUtil.validateIdentified(pTetR,doc,0);
		
		
		//SBOL_VALID_ENTITY_TYPES - Component.Features
		List<URI> tempURIs=SBOLUtil.getURIs(pTetR.getFeatures());
		RDFUtil.setProperty(resource, DataModel.Component.feature, SBOLUtil.getURIs(pTetR.getSequences()));
		TestUtil.validateIdentified(pTetR,doc,1);
		RDFUtil.setProperty(resource, DataModel.Component.feature, tempURIs);
		TestUtil.validateIdentified(pTetR,doc,0);
		
		//SBOL_VALID_ENTITY_TYPES - Component.Constraints
		tempURIs=SBOLUtil.getURIs(pTetR.getConstraints());
		RDFUtil.setProperty(resource, DataModel.Component.constraint, SBOLUtil.getURIs(pTetR.getSequences()));
		TestUtil.validateIdentified(pTetR,doc,1);
		RDFUtil.setProperty(resource, DataModel.Component.constraint, tempURIs);
		TestUtil.validateIdentified(pTetR,doc,0);

		//SBOL_VALID_ENTITY_TYPES - Component.Interactions
		tempURIs=SBOLUtil.getURIs(pTetR.getInteractions());
		RDFUtil.setProperty(resource, DataModel.Component.interaction, SBOLUtil.getURIs(pTetR.getSequences()));
		TestUtil.validateIdentified(pTetR,doc,1);
		RDFUtil.setProperty(resource, DataModel.Component.interaction, tempURIs);
		TestUtil.validateIdentified(pTetR,doc,0);
		
		
		//Component.type is required
		TestUtil.validateProperty(pTetR, "setTypes", new Object[] {null}, List.class);
		List<URI> tempList=pTetR.getTypes();
		pTetR.setTypes(null);
		TestUtil.validateIdentified(pTetR,doc,1);
		pTetR.setTypes(new ArrayList<URI>());
		TestUtil.validateIdentified(pTetR,doc,1);		
		pTetR.setTypes(tempList);
		TestUtil.validateIdentified(pTetR,doc,0);		
		
		
		Attachment attachment=doc.createAttachment("attachment1", URI.create("https://sbolstandard.org/attachment1"));
	    attachment.setFormat(ModelLanguage.SBML);
	    attachment.setSize(OptionalLong.of(1000));
	    
	    pTetR.setAttachments(Arrays.asList(attachment.getUri()));
	    TestUtil.validateIdentified(pTetR,doc,0);
	    attachment.setSize(OptionalLong.of(-1));
	    TestUtil.validateDocument(doc,1);
	    attachment.setSize(OptionalLong.of(100));
	    
	    
	    //Encoding must be provided if elements are set
	    Sequence seq=doc.getSequences().get(0);
	    seq.setEncoding(null);
	    TestUtil.validateIdentified(seq,doc,1,1);
		
	    //One main component type must be provided.
	    pTetR.setTypes(Arrays.asList(ComponentType.DNA.getUrl(), ComponentType.Protein.getUrl() ));
	    TestUtil.validateIdentified(pTetR,doc,1,2);
	    pTetR.setTypes(Arrays.asList(ComponentType.DNA.getUrl()));
	    seq.setEncoding(Encoding.NucleicAcid);
	    TestUtil.validateIdentified(pTetR,doc,0);
	    
		
	   //IDENTIFIED_URI_MUST_BE_USED_AS_A_PREFIX_FOR_CHILDREN
        Interaction interaction= popsReceiver.createInteraction(SBOLAPI.append(base, "protein_production"), Arrays.asList(InteractionType.GeneticProduction));
        TestUtil.validateIdentified(popsReceiver,doc,1);
        
        Interaction interaction2= popsReceiver.createInteraction(Arrays.asList(InteractionType.GeneticProduction));
        Component TetR=SBOLAPI.createComponent(doc, URI.create("https://synbiohub.org/public/igem/TetR"),ComponentType.Protein.getUrl(), "TetR", "TetR repressor", Role.TF);
        SubComponent gfpProteinSubComponent=SBOLAPI.addSubComponent(popsReceiver, TetR);
        Participation participation= interaction2.createParticipation(SBOLAPI.append(base, "inhibitor_participation"), Arrays.asList(ParticipationRole.Inhibitor), gfpProteinSubComponent.getUri());
        TestUtil.validateIdentified(popsReceiver,doc,2);
        TestUtil.validateIdentified(interaction2,1);
        
        //Introduce two more errors. 
        Interaction interaction3= popsReceiver.createInteraction(SBOLAPI.append(base, "protein_production3"), Arrays.asList(InteractionType.GeneticProduction));
        Participation participation3= interaction3.createParticipation(SBOLAPI.append(base, "inhibitor_participation3"), Arrays.asList(ParticipationRole.Inhibitor), gfpProteinSubComponent.getUri());
        TestUtil.validateIdentified(popsReceiver, doc,4);
        TestUtil.validateIdentified(interaction3, 1);
        
        
        
        
        
		
        
       
		   
	    
    }

}
