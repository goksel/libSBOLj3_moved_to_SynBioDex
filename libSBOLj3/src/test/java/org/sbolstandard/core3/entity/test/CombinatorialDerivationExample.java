package org.sbolstandard.core3.entity.test;

import java.io.IOException;
import java.net.URI;
import java.util.Arrays;

import org.apache.jena.rdf.model.Resource;
import org.sbolstandard.core3.api.SBOLAPI;
import org.sbolstandard.core3.entity.*;
import org.sbolstandard.core3.io.SBOLFormat;
import org.sbolstandard.core3.io.SBOLIO;
import org.sbolstandard.core3.test.TestUtil;
import org.sbolstandard.core3.util.Configuration;
import org.sbolstandard.core3.util.RDFUtil;
import org.sbolstandard.core3.util.SBOLGraphException;
import org.sbolstandard.core3.util.SBOLUtil;
import org.sbolstandard.core3.vocabulary.*;
import org.sbolstandard.core3.vocabulary.RestrictionType.ConstraintRestriction;
import org.sbolstandard.core3.vocabulary.RestrictionType.SequentialRestriction;

import junit.framework.TestCase;

public class CombinatorialDerivationExample extends TestCase {
	
	public void testCombinatorialDerivation() throws SBOLGraphException, IOException, Exception
    {
		URI base=URI.create("https://synbiohub.org/public/igem/");
		SBOLDocument doc=new SBOLDocument(base);
		
		Component Pro=SBOLAPI.createDnaComponent(doc, "Pro", "Pro", null, Role.Promoter,null);		
		Component RBS=SBOLAPI.createDnaComponent(doc, "P1", "P1", null, Role.Promoter,"CTATGGACTATGTTTGAAAGGGAGAAATACTAG");
		Component pAmtR=SBOLAPI.createDnaComponent(doc, "pAmtR", "pAmtR", null, Role.Promoter,"CTTGTCCAACCAAATGATTCGTTACCAATTGACAGTTTCTATCGATCTATAGATAATGCTAGC");
		Component pBetI=SBOLAPI.createDnaComponent(doc, "pBetI", "pBetI", null, Role.Promoter,"AGCGCGGGTGAGAGGGATTCGTTACCAATTGACAATTGATTGGACGTTCAATATAATGCTAGC");
		
		
		Component PhlF=SBOLAPI.createDnaComponent(doc, "PhlF", "PhlF", null, Role.CDS,"ATGGCACGTACCCCGAGCCGTAGCAGCATTGGTAGCCTGCGTAGTCCGCATACCCATAAAGCAATTCTGA"
				+ "CCAGCACCATTGAAATCCTGAAAGAATGTGGTTATAGCGGTCTGAGCATTGAAAGCGTTGCACGTCGTGCCGGTGCAAGCAAAC"
				+ "CGACCATTTATCGTTGGTGGACCAATAAAGCAGCACTGATTGCCGAAGTGTATGAAAATGAAAGCGAACAGGTGCGTAAATTTC"
				+ "CGGATCTGGGTAGCTTTAAAGCCGATCTGGATTTTCTGCTGCGTAATCTGTGGAAAGTTTGGCGTGAAACCATTTGTGGTGAAGC"
				+ "ATTTCGTTGTGTTATTGCAGAAGCACAGCTGGACCCTGCAACCCTGACCCAGCTGAAAGATCAGTTTATGGAACGTCGTCGTGAG"
				+ "ATGCCGAAAAAACTGGTTGAAAATGCCATTAGCAATGGTGAACTGCCGAAAGATACCAATCGTGAACTGCTGCTGGATATGATTT"
				+ "TTGGTTTTTGTTGGTATCGCCTGCTGACCGAACAGCTGACCGTTGAACAGGATATTGAAGAATTTACCTTCCTGCTGATTAATGGTG"
				+ "TTTGTCCGGGTACACAGCGTTAA");
		
		
		
		Component Ter=SBOLAPI.createDnaComponent(doc, "Ter", "Ter", null, Role.Terminator,null);
		Component L3S3P31=SBOLAPI.createDnaComponent(doc, "L3S3P31", "L3S3P31", null, Role.Terminator,"CCAATTATTGAACACCCTAACGGGTGTTTTTTTTTTTTTGGTCTACC");
		Component L3S2P55=SBOLAPI.createDnaComponent(doc, "L3S2P55", "L3S2P55", null, Role.Terminator,"CTCGGTACCAAAGACGAACAATAAGACGCTGAAAAGCGTCTTTTTTCGTTTTGGTCC");
		
		
		Component Gen=SBOLAPI.createDnaComponent(doc, "Gen", "Gen", null, Role.EngineeredRegion,"CTATGGACTATGTTTGAAAGGGAGAAATACTAGATGGCACGTACCCCGAGCCGTAGCAGCATTGGTAGCCTG"
				+ "CGTAGTCCGCATACCCATAAAGCAATTCTGACCAGCACCATTGAAATCCTGAAAGAATGTGGTTATAGCGGTCTGAGCATTGAAA"
				+ "GCGTTGCACGTCGTGCCGGTGCAAGCAAACCGACCATTTATCGTTGGTGGACCAATAAAGCAGCACTGATTGCCGAAGTGTATGA"
				+ "AAATGAAAGCGAACAGGTGCGTAAATTTCCGGATCTGGGTAGCTTTAAAGCCGATCTGGATTTTCTGCTGCGTAATCTGTGGAAAG"
				+ "TTTGGCGTGAAACCATTTGTGGTGAAGCATTTCGTTGTGTTATTGCAGAAGCACAGCTGGACCCTGCAACCCTGACCCAGCTGAAA"
				+ "GATCAGTTTATGGAACGTCGTCGTGAGATGCCGAAAAAACTGGTTGAAAATGCCATTAGCAATGGTGAACTGCCGAAAGATACCA"
				+ "ATCGTGAACTGCTGCTGGATATGATTTTTGGTTTTTGTTGGTATCGCCTGCTGACCGAACAGCTGACCGTTGAACAGGATATTGAA"
				+ "GAATTTACCTTCCTGCTGATTAATGGTGTTTGTCCGGGTACACAGCGTTAA");
		
		Component Derivation_example=SBOLAPI.createDnaComponent(doc, "Derivation_example", "Derivation_example", null, Role.EngineeredRegion,"CTATGGACTATGTTTGAAAGGGAGAAATACTAGATGGCACGTACCCCGAGCCGTAGCAGCATTGGTAGCC"
				+ "TGCGTAGTCCGCATACCCATAAAGCAATTCTGACCAGCACCATTGAAATCCTGAAAGAATGTGGTTATAGCGGTCTGAGCATT"
				+ "GAAAGCGTTGCACGTCGTGCCGGTGCAAGCAAACCGACCATTTATCGTTGGTGGACCAATAAAGCAGCACTGATTGCCGAAGTG"
				+ "TATGAAAATGAAAGCGAACAGGTGCGTAAATTTCCGGATCTGGGTAGCTTTAAAGCCGATCTGGATTTTCTGCTGCGTAATCTGT"
				+ "GGAAAGTTTGGCGTGAAACCATTTGTGGTGAAGCATTTCGTTGTGTTATTGCAGAAGCACAGCTGGACCCTGCAACCCTGACCCA"
				+ "GCTGAAAGATCAGTTTATGGAACGTCGTCGTGAGATGCCGAAAAAACTGGTTGAAAATGCCATTAGCAATGGTGAACTGCCGAAA"
				+ "GATACCAATCGTGAACTGCTGCTGGATATGATTTTTGGTTTTTGTTGGTATCGCCTGCTGACCGAACAGCTGACCGTTGAACAGGA"
				+ "TATTGAAGAATTTACCTTCCTGCTGATTAATGGTGTTTGTCCGGGTACACAGCGTTAA");
		
		Component Gen_Generated= SBOLAPI.createDnaComponent(doc, "Gen_Generated", "Gen_Generated", null, Role.EngineeredRegion,null);
		Gen_Generated.setSequences(Gen.getSequences());
		
		
		Component Derivation_Generated= SBOLAPI.createDnaComponent(doc, "Derivation_Generated", "Derivation_Generated", null, Role.EngineeredRegion,null);
		Derivation_Generated.setSequences(Derivation_example.getSequences());
		
		//Gen
		SubComponent sc_RBS_Gen=Gen.createSubComponent(RBS);
		SubComponent sc_PhlF_Gen=Gen.createSubComponent(PhlF);
		SubComponent sc_Ter_Gen=Gen.createSubComponent(Ter);
		sc_RBS_Gen.createRange(1, 33, Gen.getSequences().get(0));
		sc_PhlF_Gen.createRange(34, 636, Gen.getSequences().get(0));
		Gen.createConstraint(SequentialRestriction.precedes, sc_RBS_Gen, sc_PhlF_Gen);
		Gen.createConstraint(SequentialRestriction.precedes, sc_PhlF_Gen, sc_Ter_Gen);
		
		//Gen_Generated
		SubComponent sc_RBS_Gen_Generated=Gen_Generated.createSubComponent(RBS);
		SubComponent sc_PhlF_Gen_Generated=Gen_Generated.createSubComponent(PhlF);
		SubComponent sc_Ter_GenGenerated=Gen_Generated.createSubComponent(L3S3P31);
		sc_RBS_Gen_Generated.createRange(1, 33, Gen.getSequences().get(0));
		sc_PhlF_Gen_Generated.createRange(34, 636, Gen.getSequences().get(0));		
		Gen_Generated.createConstraint(SequentialRestriction.precedes, sc_RBS_Gen_Generated, sc_PhlF_Gen_Generated);
		Gen_Generated.createConstraint(SequentialRestriction.precedes, sc_PhlF_Gen_Generated, sc_Ter_GenGenerated);
		
		//Derivation
		SubComponent sc_Pro_Derivation=Derivation_example.createSubComponent(Pro);
		SubComponent sc_Gen_Derivation=Derivation_example.createSubComponent(Gen);
		Derivation_example.createConstraint(SequentialRestriction.precedes, sc_Pro_Derivation, sc_Gen_Derivation);
		
		//DerivationGenerated
		SubComponent sc_pAmtR_DerivationGenerated=Derivation_Generated.createSubComponent(pAmtR);
		SubComponent sc_GenGenerated_DerivationGenerated=Derivation_Generated.createSubComponent(Gen_Generated);
		Derivation_Generated.createConstraint(SequentialRestriction.precedes, sc_pAmtR_DerivationGenerated, sc_GenGenerated_DerivationGenerated);
		
		
		CombinatorialDerivation compDerTer= doc.createCombinatorialDerivation("TerminatorDerivation",Gen);
		VariableFeature varFeatureTer= compDerTer.createVariableFeature(VariableFeatureCardinality.One, sc_Ter_Gen);
		varFeatureTer.setVariants(Arrays.asList(L3S3P31, L3S2P55));
		
				
		CombinatorialDerivation compDerPro= doc.createCombinatorialDerivation("PromoterDerivation",Derivation_example);
		compDerPro.setStrategy(CombinatorialDerivationStrategy.Enumerate);
		VariableFeature varFeaturePro= compDerPro.createVariableFeature(VariableFeatureCardinality.One, sc_Pro_Derivation);
		varFeaturePro.setVariants(Arrays.asList(pBetI, pAmtR));
		
		VariableFeature varFeatureGen= compDerPro.createVariableFeature(VariableFeatureCardinality.One, sc_Gen_Derivation);
		varFeatureGen.setVariantDerivations(Arrays.asList(compDerTer));
		
		//Gen design: RBS-PhlF-Ter
		//compDerTer CombinatorialDerivation specifies Gen.Ter options: L3S3 or L3S2
		//Gen.Gen_Generated.RBS derives from Gen.RBS
		//Gen.Gen_Generated.PhlF derives from Gen.PhlF
		//Gen.Gen_Generated.L3S3 derives from Gen.Ter		
		Gen_Generated.setWasDerivedFrom(Arrays.asList(compDerTer.getUri(),Gen.getUri()));
		sc_RBS_Gen_Generated.setWasDerivedFrom(Arrays.asList(sc_RBS_Gen.getUri()));
		sc_PhlF_Gen_Generated.setWasDerivedFrom(Arrays.asList(sc_PhlF_Gen.getUri()));
		sc_Ter_GenGenerated.setWasDerivedFrom(Arrays.asList(sc_Ter_Gen.getUri()));
		
		
		//Derivation design: Pro - Gen (Gen: RBS-PhlF-Ter)
		//compDerPro CombinatorialDerivation specifies
		//    Derivation.Pro options: pBetI or pAmtR
		//    Derivation.Gen is derived via compDerTer  -> Gen.Ter options: L3S3 or L3S2
		//DerivationGenerated.pAmtR derives from Derivation.Pro
		//DerivationGenerated.GenGenerated derives from Derivation.Gen
		Derivation_Generated.setWasDerivedFrom(Arrays.asList(compDerPro.getUri(),Derivation_example.getUri()));
		sc_pAmtR_DerivationGenerated.setWasDerivedFrom(Arrays.asList(sc_Pro_Derivation.getUri()));
		sc_GenGenerated_DerivationGenerated.setWasDerivedFrom(Arrays.asList(sc_Gen_Derivation.getUri()));
		
		
		
		Configuration.getInstance().setValidateBeforeSaving(false);
		System.out.print(SBOLIO.write(doc, SBOLFormat.TURTLE));
		Configuration.getInstance().setValidateBeforeSaving(true);
		
		TestUtil.validateDocument(doc, 0);
		     
    }

}
