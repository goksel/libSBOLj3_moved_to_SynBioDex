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
import org.sbolstandard.core3.vocabulary.ComponentType.OptionalComponentType;
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
		
		Component popsReceiver=SBOLAPI.createDnaComponent(doc, "BBa_F2620", "BBa_F2620", "PoPS Receiver", Role.EngineeredGene, null); 
	    TestUtil.serialise(doc, "entity_additional/component", "component");
        System.out.println(SBOLIO.write(doc, SBOLFormat.TURTLE));
        TestUtil.assertReadWrite(doc);
        
        Configuration.getInstance().setValidateAfterSettingProperties(false);
        
        
		//Component.hasSequence can have zero values
		TestUtil.validateIdentified(popsReceiver,doc,0);
		
		Component pTetR=SBOLAPI.createDnaComponent(doc, "BBa_R0040", "pTetR", "TetR repressible promoter", Role.Promoter, "tccctatcagtgatagagattgacatccctatcagtgatagagatactgagcac");
		TestUtil.validateIdentified(pTetR,doc,0);
		
		//Component.hasSequence can have multiple values
		List<Sequence> tempSequences=pTetR.getSequences();
		SBOLAPI.addSequence(doc, pTetR, Encoding.NucleicAcid, "aaaa");
		// disable testing option requirements so it doesn't match against COMPONENT_TYPE_SEQUENCE_LENGTH_MATCH
		Configuration.getInstance().setValidateRecommendedRules(false);
		TestUtil.validateIdentified(pTetR,doc,0);
		Configuration.getInstance().setValidateRecommendedRules(true);
		pTetR.setSequences(tempSequences);
		
		// COMPONENT_TYPE_SEQUENCE_LENGTH_MATCH
		SBOLAPI.addSequence(doc, pTetR, Encoding.NucleicAcid, "tttttttttttttttttttttttttttttttttttttttttttttttttttttt");
		TestUtil.validateIdentified(pTetR,doc,0);
		pTetR.getSequences().get(0).setElements("aaa");
		TestUtil.validateIdentified(pTetR,doc,1);
		Configuration.getInstance().setValidateRecommendedRules(false);
		TestUtil.validateIdentified(pTetR,doc,0);

		Configuration.getInstance().setValidateRecommendedRules(true);
		pTetR.setSequences(tempSequences); //use previously saved vales above
		
		
		// COMPONENT_TYPE_SEQUENCE_TYPE_MATCH_COMPONENT_TYPE
		pTetR.getSequences().get(0).setEncoding(Encoding.INCHI);
		TestUtil.validateIdentified(pTetR,doc,1);
		pTetR.getSequences().get(0).setEncoding(Encoding.NucleicAcid);
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

        // COMPONENT_TYPE_MATCH_PROPERTY
		pTetR.setTypes(Arrays.asList(URI.create("http://invalidtype.org")));
		TestUtil.validateIdentified(pTetR,doc,2); // will also error against COMPONENT_TYPE_SEQUENCE_TYPE_MATCH_COMPONENT_TYPE
		pTetR.setTypes(Arrays.asList(ComponentType.DNA.getUri()));
		TestUtil.validateIdentified(pTetR,doc,0); 
		//also check that the configuration option properly disables the check and allows an invalid type
		Configuration.getInstance().setValidateRecommendedRules(false);
		pTetR.setTypes(Arrays.asList(URI.create("http://invalidtype.org")));
		TestUtil.validateIdentified(pTetR,doc,1); // will also error against COMPONENT_TYPE_SEQUENCE_TYPE_MATCH_COMPONENT_TYPE
		//Reset the values
		pTetR.setTypes(tempList);
		Configuration.getInstance().setValidateRecommendedRules(true);
		TestUtil.validateIdentified(pTetR,doc,0);	
		
		Attachment attachment=doc.createAttachment("attachment1", URI.create("https://sbolstandard.org/attachment1"));
	    attachment.setFormat(ModelLanguage.SBML);
	    attachment.setSize(OptionalLong.of(1000));
	    
	    pTetR.setAttachments(Arrays.asList(attachment));
	    TestUtil.validateIdentified(pTetR,doc,0);
	    attachment.setSize(OptionalLong.of(-1));
	    TestUtil.validateDocument(doc,1);
	    attachment.setSize(OptionalLong.of(100));
	    
	    
	    //Encoding must be provided if elements are set
	    Sequence seq=doc.getSequences().get(0);
	    URI encodingValue=null;
	    seq.setEncoding(encodingValue);
	    TestUtil.validateIdentified(seq,doc,1,2); // will also error against COMPONENT_TYPE_SEQUENCE_TYPE_MATCH_COMPONENT_TYPE
		
	    //One main component type must be provided.
	    pTetR.setTypes(Arrays.asList(ComponentType.DNA.getUri(), ComponentType.Protein.getUri() ));
	    TestUtil.validateIdentified(pTetR,doc,3,4); // will also error against COMPONENT_TYPE_SEQUENCE_TYPE_MATCH_COMPONENT_TYPE
	    pTetR.setTypes(Arrays.asList(ComponentType.DNA.getUri()));
	    seq.setEncoding(Encoding.NucleicAcid);
	    TestUtil.validateIdentified(pTetR,doc,0);
	    
		
	   //IDENTIFIED_URI_MUST_BE_USED_AS_A_PREFIX_FOR_CHILDREN
        Interaction interaction= popsReceiver.createInteraction(SBOLAPI.append(base, "protein_production"), Arrays.asList(InteractionType.GeneticProduction));
        TestUtil.validateIdentified(popsReceiver,doc,1);
        
        Interaction interaction2= popsReceiver.createInteraction(Arrays.asList(InteractionType.GeneticProduction));
        Component TetR=SBOLAPI.createComponent(doc, URI.create("https://synbiohub.org/public/igem/TetR"),ComponentType.Protein.getUri(), "TetR", "TetR repressor", Role.TF);
        SubComponent gfpProteinSubComponent=SBOLAPI.addSubComponent(popsReceiver, TetR);
        Participation participation= interaction2.createParticipation(SBOLAPI.append(base, "inhibitor_participation"), Arrays.asList(ParticipationRole.Inhibitor), gfpProteinSubComponent);
        TestUtil.validateIdentified(popsReceiver,doc,2);
        TestUtil.validateIdentified(interaction2,1);
        
        //Introduce two more errors. 
        Interaction interaction3= popsReceiver.createInteraction(SBOLAPI.append(base, "protein_production3"), Arrays.asList(InteractionType.GeneticProduction));
        Participation participation3= interaction3.createParticipation(SBOLAPI.append(base, "inhibitor_participation3"), Arrays.asList(ParticipationRole.Inhibitor), gfpProteinSubComponent);
        TestUtil.validateIdentified(popsReceiver, doc,4);
        TestUtil.validateIdentified(interaction3, 1);

        //SUBCOMPONENT_OBJECTS_CIRCULAR_REFERENCE_CHAIN
        Component TetRbindingDomain=SBOLAPI.createComponent(doc, URI.create("https://synbiohub.org/public/igem/TetR_binding"),ComponentType.Protein.getUri(), "TetRbinding", "TetR binding domain", Role.TF);
        SubComponent tetRProteinSubComponent=SBOLAPI.addSubComponent(popsReceiver, TetR);//Valid
	    TestUtil.validateIdentified(TetR,doc, 0, 4);
        SubComponent tetRBindingProteinSubComponent=SBOLAPI.addSubComponent(TetR, TetRbindingDomain);//Valid
	    TestUtil.validateIdentified(TetR,doc, 0, 4);
        SubComponent tetRBindingProteinSubComponent2=SBOLAPI.addSubComponent(TetR, TetR);//InValid
        TestUtil.validateIdentified(TetR,doc, 2, 6);
        
        SubComponent tetRBindingProteinSubComponent3=SBOLAPI.addSubComponent(TetRbindingDomain, TetR); //InValid
	    TestUtil.validateIdentified(TetR,doc, 3, 8);
	    TestUtil.validateIdentified(TetRbindingDomain,1);
	    
        SubComponent tetRBindingProteinSubComponent4=SBOLAPI.addSubComponent(TetRbindingDomain, popsReceiver); //InValid
        
       // Configuration.getConfiguration().setValidateBeforeSaving(false);
       // System.out.println(SBOLIO.write(doc, SBOLFormat.TURTLE));
        
        
        TestUtil.validateIdentified(TetR,doc, 5, 11);
	    TestUtil.validateIdentified(popsReceiver,doc,5, 11);

           
     	//SEQUENCE_ELEMENTS_CONSISTENT_WITH_ENCODING
        Sequence seqAA = doc.createSequence("seqAA");
        seqAA.setEncoding(Encoding.AminoAcid);
        seqAA.setElements("ATgz");
        TestUtil.validateIdentified(seqAA, 1);
        seqAA.setElements("ATgd");
        TestUtil.validateIdentified(seqAA, 0);

        Sequence seqNA = doc.createSequence("seqNA");
        seqNA.setEncoding(Encoding.NucleicAcid);
        seqNA.setElements("ZAtc");
        TestUtil.validateIdentified(seqNA, 1);
        seqNA.setElements("ATGcn");
        TestUtil.validateIdentified(seqNA, 0);

        // examples taken from https://archive.epa.gov/med/med_archive_03/web/html/smiles.html
        Sequence seqSMILES = doc.createSequence("seqSMILES");
        seqSMILES.setEncoding(Encoding.SMILES);
        seqSMILES.setElements("Hydrogen Dioxode"); //pretty much anything is allowed other than a space
        TestUtil.validateIdentified(seqSMILES, 1);
        seqSMILES.setElements("c1c(N(=O)=O)cccc1"); // nitrobenzene
        TestUtil.validateIdentified(seqSMILES, 0);
        seqSMILES.setElements("C=1CCCCC1"); // Cyclohexene
        TestUtil.validateIdentified(seqSMILES, 0);

        // examples taken from https://en.wikipedia.org/wiki/International_Chemical_Identifier
        Sequence seqINCHI = doc.createSequence("seqINCHI");
        seqINCHI.setEncoding(Encoding.INCHI);
        seqINCHI.setElements("InChI=JS/C2H6O/c1-2-3/h3H,2H2,1H3"); //ethanol but starting with a J
        TestUtil.validateIdentified(seqINCHI, 1);
        seqINCHI.setElements("InChI=1S/C2H6O/c1-2-3/h3H,2H2,1H3"); //ethanol
        TestUtil.validateIdentified(seqINCHI, 0);
        seqINCHI.setElements("InChI=1S/C6H8O6/c7-1-2(8)5-3(9)4(10)6(11)12-5/h2,5,7-10H,1H2/t2-,5+/m0/s1"); //L-ascorbic acid with InChI
        TestUtil.validateIdentified(seqINCHI, 0);        
	    
        ComponentType[] values=ComponentType.values();
        System.out.println(values.length);
        OptionalComponentType[] optionalValues2=ComponentType.OptionalComponentType.values();
        System.out.println(optionalValues2.length);
        
        
    }
}
