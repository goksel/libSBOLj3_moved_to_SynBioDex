package org.sbolstandard;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.util.Collections;
import java.util.List;

import org.apache.jena.datatypes.xsd.XSDDateTime;
import org.sbolstandard.entity.*;
import org.sbolstandard.entity.measure.*;
import org.sbolstandard.entity.provenance.*;
import org.sbolstandard.io.SBOLIO;
import org.sbolstandard.util.SBOLGraphException;
import org.sbolstandard.vocabulary.DataModel;
import org.sbolstandard.vocabulary.MeasureDataModel;
import org.sbolstandard.vocabulary.ProvenanceDataModel;

import static org.junit.Assert.*;

public class TestUtil {

	public static void serialise(SBOLDocument doc, String directory, String file) throws FileNotFoundException, IOException
	{
		String baseOutput="output";
		File outputDir=new File(baseOutput +  "/" + directory);
        if (!outputDir.exists())
        {
        	boolean result=outputDir.mkdirs();
        }
        
        SBOLIO.write(doc, new File(String.format("%s/%s/%s.ttl", baseOutput,directory, file)), "TURTLE");
        SBOLIO.write(doc, new File(String.format("%s/%s/%s.rdf", baseOutput,directory, file)), "RDF/XML-ABBREV");
        SBOLIO.write(doc, new File(String.format("%s/%s/%s.jsonld", baseOutput,directory, file)), "JSON-LD");
        SBOLIO.write(doc, new File(String.format("%s/%s/%s.rj", baseOutput,directory, file)), "RDFJSON");
        SBOLIO.write(doc, new File(String.format("%s/%s/%s.nt", baseOutput,directory, file)), "N-TRIPLES");
	}
	
	public static void assertReadWrite(SBOLDocument doc) throws IOException, SBOLGraphException
	{
		String output=SBOLIO.write(doc, "Turtle");
	    SBOLDocument doc2=SBOLIO.read(output, "Turtle"); 
	    assertEqual(doc, doc2);
	       
	}
	public static void assertEqual(SBOLDocument doc1, SBOLDocument doc2) throws SBOLGraphException
	{
		assertEqualEntity(doc1.getActivities(), doc2,Activity.class);
		assertEqualEntity(doc1.getAgents(), doc2,Agent.class);
		assertEqualEntity(doc1.getAttachments(), doc2,Attachment.class);
		assertEqualEntity(doc1.getBinaryPrefixes(), doc2,BinaryPrefix.class);
		assertEqualEntity(doc1.getCollections(), doc2,Collection.class);
		assertEqualEntity(doc1.getCombinatorialDerivations(), doc2,CombinatorialDerivation.class);
		assertEqualEntity(doc1.getComponents(), doc2,Component.class);
		assertEqualEntity(doc1.getExperimentalData(), doc2,ExperimentalData.class);
		assertEqualEntity(doc1.getExperiments(), doc2,Experiment.class);
		assertEqualEntity(doc1.getImplementations(), doc2,Implementation.class);
		assertEqualEntity(doc1.getModels(), doc2,Model.class);
		assertEqualEntity(doc1.getNamespaces(), doc2,Namespace.class);
		assertEqualEntity(doc1.getPlans(), doc2,Plan.class);
		assertEqualEntity(doc1.getPrefixedUnits(), doc2,PrefixedUnit.class);
		assertEqualEntity(doc1.getSequences(), doc2,Sequence.class);
		assertEqualEntity(doc1.getSingularUnits(), doc2,SingularUnit.class);
		assertEqualEntity(doc1.getSIPrefixes(), doc2,SIPrefix.class);
		assertEqualEntity(doc1.getUnitDivisions(), doc2,UnitDivision.class);
		assertEqualEntity(doc1.getUnitExponentiations(), doc2,UnitExponentiation.class);
		assertEqualEntity(doc1.getUnitMultiplications(), doc2,UnitMultiplication.class);
	
		/*if (doc1.getAgents()!=null)
		{
			for (Agent agent:doc1.getAgents())
			{
				Agent agent2=(Agent) doc2.getIdentified(agent.getUri(), Agent.class);
				assertEqual(agent, agent2);
			}
		}*/
	
	}
	
	private static <T extends Identified> void assertEqualEntity(List<T> entities, SBOLDocument doc2, Class<T> classType) throws SBOLGraphException
	{
		if (entities!=null)
		{
			for (T entity:entities)
			{
				T entity2=(T) doc2.getIdentified(entity.getUri(), classType );
				assertEqualEntity(entity,entity2);
			}
		}
	}
	
	private static <T extends Identified>  Identified getEntity(List<T> entities, URI uri)
	{
		if (entities!=null)
		{
			for (T entity:entities)
			{
				if (entity.getUri().equals(uri))
				{
					return entity;
				}	
			}
		}
		return null;
	}
	
	
	private static <T extends Identified> void assertEqualEntity(List<T> entities, List<T> entities2) throws SBOLGraphException
	{
		if (entities!=null && entities2!=null)
		{
			for (T entity:entities)
			{
				Identified entity2=getEntity(entities2, entity.getUri());
				assertEqualEntity(entity,entity2);
			}
		}
	}
	
	private static void assertEqualEntity(Identified identified1, Identified identified2)  throws SBOLGraphException
	{
		if (identified1!=null && identified2==null)
		{
			throw new SBOLGraphException("Could not compare " + identified1.getUri());
		}
		
		if (identified1 instanceof Activity)
		{
			assertEqualActivity((Activity)identified1,  (Activity) identified2);
		}
		else if (identified1 instanceof Agent)
		{
			assertEqualAgent((Agent)identified1,  (Agent) identified2);
		}
		else if (identified1 instanceof Attachment)
		{
			assertEqualAttachment((Attachment)identified1,  (Attachment) identified2);
		}
		else if (identified1 instanceof BinaryPrefix)
		{
			assertEqualBinaryPrefix((BinaryPrefix)identified1, (BinaryPrefix) identified2);
		}
		else if (identified1 instanceof Collection)
		{
			assertEqualCollection((Collection)identified1, (Collection) identified2);
		}
		else if (identified1 instanceof CombinatorialDerivation)
		{
			assertEqualCombinatorialDerivation((CombinatorialDerivation)identified1, (CombinatorialDerivation) identified2);
		}
		
		else if (identified1 instanceof Component)
		{
			assertEqualComponent((Component)identified1,  (Component) identified2);
		}
		else if (identified1 instanceof ExperimentalData)
		{
			assertEqualExperimentalData((ExperimentalData)identified1, (ExperimentalData) identified2);
		}
		else if (identified1 instanceof Experiment)
		{
			assertEqualExperiment((Experiment)identified1, (Experiment) identified2);
		}
		else if (identified1 instanceof Implementation)
		{
			assertEqualImplementation((Implementation)identified1, (Implementation) identified2);
		}
		else if (identified1 instanceof Model)
		{
			assertEqualModel((Model)identified1, (Model) identified2);
		}
		else if (identified1 instanceof Namespace)
		{
			assertEqualNamespace((Namespace)identified1, (Namespace) identified2);
		}
		else if (identified1 instanceof Model)
		{
			assertEqualModel((Model)identified1, (Model) identified2);
		}
		else if (identified1 instanceof Plan)
		{
			assertEqualPlan((Plan)identified1,  (Plan) identified2);
		}
		else if (identified1 instanceof PrefixedUnit)
		{
			assertEqualPrefixedUnit((PrefixedUnit)identified1, (PrefixedUnit) identified2);
		}
		else if (identified1 instanceof Sequence)
		{
			assertEqualSequence((Sequence)identified1, (Sequence)identified2);
		}
		else if (identified1 instanceof SingularUnit)
		{
			assertEqualSingularUnit((SingularUnit)identified1, (SingularUnit) identified2);
		}	
		else if (identified1 instanceof SIPrefix)
		{
			assertEqualSIPrefix((SIPrefix)identified1, (SIPrefix) identified2);
		}
		else if (identified1 instanceof UnitDivision)
		{
			assertEqualUnitDivision((UnitDivision)identified1, (UnitDivision) identified2);
		}
		
		else if (identified1 instanceof UnitExponentiation)
		{
			assertEqualUnitExponentiation((UnitExponentiation)identified1, (UnitExponentiation) identified2);
		}
		else if (identified1 instanceof UnitMultiplication)
		{
			assertEqualUnitMultiplication((UnitMultiplication)identified1, (UnitMultiplication) identified2);
		}
		else if (identified1 instanceof Measure)
		{
			assertEqualMeasure((Measure)identified1,  (Measure) identified2);
		}
		else if (identified1 instanceof Association)
		{
			assertEqualAssociation((Association)identified1,  (Association) identified2);
		}
		else if (identified1 instanceof Usage)
		{
			assertEqualUsage((Usage)identified1,  (Usage) identified2);
		}
		else if (identified1 instanceof VariableComponent)
		{
			assertEqualVariableComponent((VariableComponent)identified1, (VariableComponent) identified2);
		}
		else if (identified1 instanceof ExternallyDefined)
		{
			assertEqualExternallyDefined((ExternallyDefined)identified1,  (ExternallyDefined) identified2);
		}
		else if (identified1 instanceof ComponentReference)
		{
			assertEqualComponentReference((ComponentReference)identified1,  (ComponentReference) identified2);
		}
		else if (identified1 instanceof Constraint)
		{
			assertEqualConstraint((Constraint)identified1,  (Constraint) identified2);
		}
		else if (identified1 instanceof LocalSubComponent)
		{
			assertEqualLocalSubComponent((LocalSubComponent)identified1,  (LocalSubComponent) identified2);
		}
		else if (identified1 instanceof SequenceFeature)
		{
			assertEqualSequenceFeature((SequenceFeature)identified1,  (SequenceFeature) identified2);
		}
		else if (identified1 instanceof SubComponent)
		{
			assertEqualSubComponent((SubComponent)identified1,  (SubComponent) identified2);
		}
		else if (identified1 instanceof Interaction)
		{
			assertEqualInteraction((Interaction)identified1,  (Interaction) identified2);
		}
		else if (identified1 instanceof Participation)
		{
			assertEqualParticipation((Participation)identified1,  (Participation) identified2);
		}
		else
		{
			throw new SBOLGraphException ("No assert method has been defined for " + identified1.getUri());
		}
	}
	
	private static void assertEqualActivity(Activity entity1, Activity entity2) throws SBOLGraphException
	{
		if (entity1!=null)
		{
			assertEqual(entity1, entity2);
			assertEqual(entity1, entity2, entity1.getTypes(),entity2.getTypes(), ProvenanceDataModel.Activity.type);
			assertEqual(entity1, entity2, entity1.getWasInformedBys(),entity2.getWasInformedBys(), ProvenanceDataModel.Activity.wasInformedBy);
			assertEqual(entity1, entity2, entity1.getEndedAtTime(),entity2.getEndedAtTime(), ProvenanceDataModel.Activity.endedAtTime);
			assertEqual(entity1, entity2, entity1.getStartedAtTime(),entity2.getStartedAtTime(), ProvenanceDataModel.Activity.startedAtTime);
			assertEqualEntity(entity1.getAssociations(), entity2.getAssociations());
			assertEqualEntity(entity1.getUsages(), entity2.getUsages());
		}
	}
	
	private static void assertEqualAssociation(Association entity1, Association entity2)  throws SBOLGraphException
	{
		if (entity1!=null)
		{
			assertEqual(entity1, entity2);
			assertEqual(entity1, entity2, entity1.getAgent(),entity2.getAgent(), ProvenanceDataModel.Association.agent);
			assertEqual(entity1, entity2, entity1.getPlan(),entity2.getPlan(), ProvenanceDataModel.Association.plan);
			assertEqual(entity1, entity2, entity1.getRoles(),entity2.getRoles(), ProvenanceDataModel.Association.role);	
		}
	}
	
	private static void assertEqualUsage(Usage entity1, Usage entity2)  throws SBOLGraphException
	{
		if (entity1!=null)
		{
			assertEqual(entity1, entity2);
			assertEqual(entity1, entity2, entity1.getRoles(),entity2.getRoles(), ProvenanceDataModel.Usage.role);
			assertEqual(entity1, entity2, entity1.getEntity(),entity2.getEntity(), ProvenanceDataModel.Usage.entity);
		}
	}
	
	private static void assertEqualAgent(Agent entity1, Agent entity2)  throws SBOLGraphException
	{
		if (entity1!=null)
		{
			assertEqual(entity1, entity2);
		}
	}
	
	private static void assertEqualAttachment(Attachment entity1, Attachment entity2)  throws SBOLGraphException
	{
		if (entity1!=null)
		{
			assertEqual(entity1, entity2);
			assertEqual(entity1, entity2, entity1.getFormat(),entity2.getFormat(), DataModel.Attachment.format);
			assertEqual(entity1, entity2, entity1.getHash(),entity2.getHash(), DataModel.Attachment.hash);
			assertEqual(entity1, entity2, entity1.getHashAlgorithm(),entity2.getHashAlgorithm(), DataModel.Attachment.hashAlgorithm);
			assertEqual(entity1, entity2, entity1.getSource(),entity2.getSource(), DataModel.Attachment.source);
			assertEqual(entity1, entity2, entity1.getSize(),entity2.getSize(), DataModel.Attachment.size);
		}
	}
	
	private static void assertEqualBinaryPrefix(BinaryPrefix entity1, BinaryPrefix entity2)  throws SBOLGraphException
	{
		if (entity1!=null)
		{
			assertEqualPrefix(entity1, entity2);
		}
	}
	
	private static void assertEqualSIPrefix(SIPrefix entity1, SIPrefix entity2)  throws SBOLGraphException
	{
		if (entity1!=null)
		{
			assertEqualPrefix(entity1, entity2);
		}
	}
	
	private static void assertEqualPrefix(Prefix entity1, Prefix entity2)  throws SBOLGraphException
	{
		if (entity1!=null)
		{
			assertEqualUnit(entity1, entity2);
			assertEqual(entity1, entity2, entity1.getFactor(),entity2.getFactor(), MeasureDataModel.Prefix.factor);
		}
	}
	
	private static void assertEqualUnit(Unit entity1, Unit entity2)  throws SBOLGraphException
	{
		if (entity1!=null)
		{
			assertEqual(entity1, entity2);
			assertEqual(entity1, entity2, entity1.getComment(),entity2.getComment(), MeasureDataModel.Unit.comment);
			assertEqual(entity1, entity2, entity1.getLabel(),entity2.getLabel(), MeasureDataModel.Unit.label);
			assertEqual(entity1, entity2, entity1.getLongComment(),entity2.getLongComment(), MeasureDataModel.Unit.longComment);
			assertEqual(entity1, entity2, entity1.getSymbol(),entity2.getSymbol(), MeasureDataModel.Unit.symbol);	
			assertEqualStrings(entity1, entity2, entity1.getAlternativeLabels(),entity2.getAlternativeLabels(), MeasureDataModel.Unit.alternativeLabel);
			assertEqualStrings(entity1, entity2, entity1.getAlternativeSymbols(),entity2.getAlternativeSymbols(), MeasureDataModel.Unit.alternativeSymbol);		
		}
	}
	
	
	private static void assertEqualCollection(Collection entity1, Collection entity2)  throws SBOLGraphException
	{
		if (entity1!=null)
		{
			assertEqual(entity1, entity2);
			assertEqual(entity1, entity2, entity1.getTopLevels(),entity2.getTopLevels(), DataModel.Collection.member);
		}
	}
	
	private static void assertEqualNamespace(Namespace entity1, Namespace entity2)  throws SBOLGraphException
	{
		if (entity1!=null)
		{
			assertEqualCollection(entity1, entity2);
		}
	}
	
	private static void assertEqualCombinatorialDerivation(CombinatorialDerivation entity1, CombinatorialDerivation entity2)  throws SBOLGraphException
	{
		if (entity1!=null)
		{
			assertEqual(entity1, entity2);
			assertEqual(entity1, entity2, entity1.getStrategy(),entity2.getStrategy(), DataModel.CombinatorialDerivation.strategy);
			assertEqual(entity1, entity2, entity1.getTemplate(),entity2.getTemplate(), DataModel.CombinatorialDerivation.template);
			assertEqualEntity(entity1.getVariableComponents(), entity2.getVariableComponents());	
		}
	}
	
	private static void assertEqualVariableComponent(VariableComponent entity1, VariableComponent entity2)  throws SBOLGraphException
	{
		if (entity1!=null)
		{
			assertEqual(entity1, entity2);
			assertEqual(entity1, entity2, entity1.getCardinality(),entity2.getCardinality(), DataModel.VariableComponent.cardinality);
			assertEqual(entity1, entity2, entity1.getSubComponent(),entity2.getSubComponent(), DataModel.VariableComponent.variable);
			assertEqual(entity1, entity2, entity1.getVariantCollections(),entity2.getVariantCollections(), DataModel.VariableComponent.variantCollection);
			assertEqual(entity1, entity2, entity1.getVariantDerivations(),entity2.getVariantDerivations(), DataModel.VariableComponent.variantDerivation);
			assertEqual(entity1, entity2, entity1.getVariants(),entity2.getVariants(), DataModel.VariableComponent.variant);	
		}
	}
	
	private static void assertEqualComponent(Component entity1, Component entity2) throws SBOLGraphException
	{
		if (entity1!=null)
		{
			assertEqual(entity1, entity2);
			assertEqual(entity1, entity2, entity1.getModels(),entity2.getModels(), DataModel.Component.model);
			assertEqual(entity1, entity2, entity1.getRoles(),entity2.getRoles(), DataModel.role);
			assertEqual(entity1, entity2, entity1.getSequences(),entity2.getSequences(), DataModel.Component.sequence);
			assertEqual(entity1, entity2, entity1.getTypes(),entity2.getTypes(), DataModel.type);
			assertEqualInterface(entity1.getInterface(),entity2.getInterface());
			assertEqualEntity(entity1.getComponentReferences(), entity2.getComponentReferences());
			assertEqualEntity(entity1.getConstraints(), entity2.getConstraints());
			assertEqualEntity(entity1.getExternallyDefineds(), entity2.getExternallyDefineds());
			assertEqualEntity(entity1.getInteractions(), entity2.getInteractions());
			assertEqualEntity(entity1.getLocalSubComponents(), entity2.getLocalSubComponents());
			assertEqualEntity(entity1.getSequenceFeatures(), entity2.getSequenceFeatures());
			assertEqualEntity(entity1.getSubComponents(), entity2.getSubComponents());
		}
	}
	
	private static void assertEqualInterface(Interface entity1, Interface entity2) throws SBOLGraphException
	{
		if (entity1!=null)
		{
			assertEqual(entity1, entity2);
			assertEqual(entity1, entity2, entity1.getInputs(),entity2.getInputs(), DataModel.Interface.input);
			assertEqual(entity1, entity2, entity1.getNonDirectionals(),entity2.getNonDirectionals(), DataModel.Interface.nondirectional);
			assertEqual(entity1, entity2, entity1.getOutputs(),entity2.getOutputs(), DataModel.Interface.output);
		}
	}
	
	private static void assertEqualComponentReference(ComponentReference entity1, ComponentReference entity2) throws SBOLGraphException
	{
		if (entity1!=null)
		{
			assertEqualFeature(entity1, entity2);
			assertEqual(entity1, entity2, entity1.getFeature(),entity2.getFeature(), DataModel.ComponentReference.feature);
			assertEqual(entity1, entity2, entity1.getInChildOf(),entity2.getInChildOf(), DataModel.ComponentReference.inChildOf);
		}
	}
	
	private static void assertEqualConstraint(Constraint entity1, Constraint entity2) throws SBOLGraphException
	{
		if (entity1!=null)
		{
			assertEqual(entity1, entity2);
			assertEqual(entity1, entity2, entity1.getObject(),entity2.getObject(), DataModel.Constraint.object);
			assertEqual(entity1, entity2, entity1.getSubject(),entity2.getSubject(), DataModel.Constraint.subject);
			assertEqual(entity1, entity2, entity1.getRestriction(),entity2.getRestriction(), DataModel.Constraint.restriction);
		}
	}
	
	private static void assertEqualExternallyDefined(ExternallyDefined entity1, ExternallyDefined entity2) throws SBOLGraphException
	{
		if (entity1!=null)
		{
			assertEqualFeature(entity1, entity2);
			assertEqual(entity1, entity2, entity1.getDefinition(),entity2.getDefinition(), DataModel.ExternalyDefined.definition);
		}
	}
	
	private static void assertEqualLocalSubComponent(LocalSubComponent entity1, LocalSubComponent entity2) throws SBOLGraphException
	{
		if (entity1!=null)
		{
			assertEqualFeature(entity1, entity2);
		//TODO: assertEqualLocations(entity1, entity2, entity1.getLocations(),entity2.getLocations(), DataModel.SubComponent.location);
			assertEqual(entity1, entity2, entity1.getTypes(),entity2.getTypes(), DataModel.type);
		}
	}
	
	private static void assertEqualFeature(Feature entity1, Feature entity2) throws SBOLGraphException
	{
		if (entity1!=null)
		{
			assertEqual(entity1, entity2);
			if (entity1.getOrientation()!=null)
			{
				assertEqual(entity1, entity2, entity1.getOrientation().getUri(),entity2.getOrientation().getUri(), DataModel.orientation);
			}
			assertEqual(entity1, entity2, entity1.getRoles(),entity2.getRoles(), DataModel.role);
		}
	}
	
	private static void assertEqualSequenceFeature(SequenceFeature entity1, SequenceFeature entity2) throws SBOLGraphException
	{
		if (entity1!=null)
		{
			assertEqualFeature(entity1, entity2);
		}
		//TODO: assertEqualLocations(entity1, entity2, entity1.getLocations(),entity2.getLocations(), DataModel.SubComponent.location);
	}
	
	private static void assertEqualSubComponent(SubComponent entity1, SubComponent entity2) throws SBOLGraphException
	{
		if (entity1!=null)
		{
			assertEqualFeature(entity1, entity2);
		//TODO: assertEqualLocations(entity1, entity2, entity1.getLocations(),entity2.getLocations(), DataModel.SubComponent.location);
		//TODO: assertEqualLocations(entity1, entity2, entity1.getSourceLocations(),entity2.getSourceLocations(), DataModel.SubComponent.sourcelocation);
			assertEqual(entity1, entity2, entity1.getIsInstanceOf(),entity2.getIsInstanceOf(), DataModel.SubComponent.instanceOf);
			assertEqual(entity1, entity2, entity1.getRoleIntegration(),entity2.getRoleIntegration(), DataModel.SubComponent.roleIntegration);		
		}
	}
	
	private static void assertEqualInteraction(Interaction entity1, Interaction entity2) throws SBOLGraphException
	{
		if (entity1!=null)
		{
			assertEqual(entity1, entity2);
			assertEqualEntity(entity1.getParticipations(), entity2.getParticipations());
			assertEqual(entity1, entity2, entity1.getTypes(),entity2.getTypes(), DataModel.type);	
		}
	}
	
	private static void assertEqualParticipation(Participation entity1, Participation entity2) throws SBOLGraphException
	{
		if (entity1!=null)
		{
			assertEqual(entity1, entity2);
			assertEqual(entity1, entity2, entity1.getParticipant(), entity2.getParticipant(),DataModel.Participation.participant);
			assertEqual(entity1, entity2, entity1.getRoles(),entity2.getRoles(), DataModel.role);	
		}
	}
	
	private static void assertEqualExperimentalData(ExperimentalData entity1, ExperimentalData entity2) throws SBOLGraphException
	{
		if (entity1!=null)
		{
			assertEqual(entity1, entity2);
		}
	}
	
	private static void assertEqualExperiment(Experiment entity1, Experiment entity2) throws SBOLGraphException
	{
		if (entity1!=null)
		{
			assertEqual(entity1, entity2);	
		}
	}
	
	private static void assertEqualImplementation(Implementation entity1, Implementation entity2) throws SBOLGraphException
	{
		if (entity1!=null)
		{
			assertEqual(entity1, entity2);		
			assertEqual(entity1, entity2, entity1.getComponent(), entity2.getComponent(),DataModel.Implementation.built);
		}
	}
	
	private static void assertEqualModel(Model entity1, Model entity2) throws SBOLGraphException
	{
		if (entity1!=null)
		{
			assertEqual(entity1, entity2);		
			assertEqual(entity1, entity2, entity1.getFramework(), entity2.getFramework(),DataModel.Model.framework);
			assertEqual(entity1, entity2, entity1.getLanguage(), entity2.getLanguage(),DataModel.Model.language);
			assertEqual(entity1, entity2, entity1.getSource(), entity2.getSource(),DataModel.Model.source);		
		}
	}
	
	private static void assertEqualPrefixedUnit(PrefixedUnit entity1, PrefixedUnit entity2) throws SBOLGraphException
	{
		if (entity1!=null)
		{
			assertEqualUnit(entity1, entity2);		
			assertEqual(entity1, entity2, entity1.getPrefix(), entity2.getPrefix(),MeasureDataModel.PrefixedUnit.prefix);
			assertEqual(entity1, entity2, entity1.getUnit(), entity2.getUnit(),MeasureDataModel.PrefixedUnit.unit);
		}
	}
	
	private static void assertEqualSingularUnit(SingularUnit entity1, SingularUnit entity2) throws SBOLGraphException
	{
		if (entity1!=null)
		{
			assertEqualUnit(entity1, entity2);		
			assertEqual(entity1, entity2, entity1.getFactor(), entity2.getFactor(),MeasureDataModel.SingularUnit.factor);
			assertEqual(entity1, entity2, entity1.getUnit(), entity2.getUnit(),MeasureDataModel.SingularUnit.unit);
		}
	}
	private static void assertEqualSequence(Sequence entity1, Sequence entity2) throws SBOLGraphException
	{
		if (entity1!=null)
		{
			assertEqual(entity1, entity2);		
			assertEqual(entity1, entity2, entity1.getElements(), entity2.getElements(),DataModel.Sequence.elements);
			assertEqual(entity1, entity2, entity1.getEncoding().getUri(), entity2.getEncoding().getUri(),DataModel.Sequence.encoding);
		}
	}
	
	private static void assertEqualUnitDivision(UnitDivision entity1, UnitDivision entity2) throws SBOLGraphException
	{
		if (entity1!=null)
		{
			assertEqualUnit(entity1, entity2);		
			assertEqual(entity1, entity2, entity1.getDenominator(), entity2.getDenominator(),MeasureDataModel.UnitDivision.denominator);
			assertEqual(entity1, entity2, entity1.getNumerator(), entity2.getNumerator(),MeasureDataModel.UnitDivision.numerator);		
		}
	}

	private static void assertEqualUnitExponentiation (UnitExponentiation entity1, UnitExponentiation entity2) throws SBOLGraphException
	{
		if (entity1!=null)
		{
			assertEqualUnit(entity1, entity2);		
			assertEqual(entity1, entity2, entity1.getBase(), entity2.getBase(),MeasureDataModel.UnitExponentiation.base);
			assertEqual(entity1, entity2, entity1.getExponent(), entity2.getExponent(),MeasureDataModel.UnitExponentiation.base);		
		}
	}
	
	private static void assertEqualUnitMultiplication(UnitMultiplication entity1, UnitMultiplication entity2) throws SBOLGraphException
	{
		if (entity1!=null)
		{
			assertEqualUnit(entity1, entity2);		
			assertEqual(entity1, entity2, entity1.getTerm1(), entity2.getTerm1(),MeasureDataModel.UnitMultiplication.term1);
			assertEqual(entity1, entity2, entity1.getTerm2(), entity2.getTerm2(),MeasureDataModel.UnitMultiplication.term2);		
		}
	}
	
	private static void assertEqualPlan(Plan entity1, Plan entity2) throws SBOLGraphException
	{
		if (entity1!=null)
		{
			assertEqual(entity1, entity2);
		}
	}
	
	private static void assertEqualMeasure(Measure entity1, Measure entity2) throws SBOLGraphException
	{
		if (entity1!=null)
		{
			assertEqual(entity1, entity2);
			assertEqual(entity1, entity2, entity1.getUnit(),entity2.getUnit(), MeasureDataModel.Measure.unit);
			assertEqual(entity1, entity2, entity1.getValue(),entity2.getValue(), MeasureDataModel.Measure.value);
			assertEqual(entity1, entity2, entity1.getTypes(),entity2.getTypes(), MeasureDataModel.Measure.type);	
		}
	}
	
	private static void assertEqual(TopLevel topLevel1, TopLevel topLevel2) throws SBOLGraphException
	{
		assertEqual((Identified)topLevel1, (Identified)topLevel2);	
		assertEqual(topLevel1, topLevel1, topLevel1.getAttachments(),topLevel2.getAttachments(), DataModel.TopLevel.attachment);
	}
	
	private static void assertEqual(Identified identified1, Identified identified2) throws SBOLGraphException
	{
		assertEqual(identified1, identified2, identified1.getName(),identified2.getName(), DataModel.Identified.name);
		assertEqual(identified1, identified2, identified1.getDescription(),identified2.getDescription(), DataModel.Identified.description);
		assertEqual(identified1, identified2, identified1.getDisplayId(),identified2.getDisplayId(), DataModel.Identified.displayId);
		assertEqual(identified1, identified2, identified1.getUri(),identified2.getUri(), DataModel.Identified.uri);
		assertEqual(identified1, identified2, identified1.getWasDerivedFrom(),identified2.getWasDerivedFrom(), DataModel.Identified.wasDerivedFrom);
		assertEqual(identified1, identified2, identified1.getWasGeneratedBy(),identified2.getWasGeneratedBy(), DataModel.Identified.wasGeneratedBy);
		assertEqualEntity(identified1.getMeasures(), identified2.getMeasures());
	}
	
	private static void assertEqual(Identified identified1, Identified identified2, String value1, String value2, URI property)
	{
		String message=String.format("Could not read the %s property. Entity:%s, Value1:%s, Value2:%s", property.toString(), identified1.getUri().toString(), value1, value2);
		assertEquals(message, value1, value2); 
	}
	
	private static void assertEqual(Identified identified1, Identified identified2, URI value1, URI value2, URI property)
	{
		String message=String.format("Could not read the %s property. Entity:%s, Value1:%s, Value2:%s", property.toString(), identified1.getUri().toString(), value1, value2);
		assertEquals(message, value1, value2); 
	}
	
	private static void assertEqual(Identified identified1, Identified identified2, float value1, float value2, URI property)
	{
		String message=String.format("Could not read the %s property. Entity:%s, Value1:%s, Value2:%s", property.toString(), identified1.getUri().toString(), String.valueOf(value1), String.valueOf(value2));
		assertEquals(message, String.valueOf(value1), String.valueOf(value2)); 
	}
	
	private static void assertEqual(Identified identified1, Identified identified2, XSDDateTime value1, XSDDateTime value2, URI property)
	{
		String message=String.format("Could not read the %s property. Entity:%s, Value1:%s, Value2:%s", property.toString(), identified1.getUri().toString(), value1, value2);
		assertEquals(message, String.valueOf(value1), String.valueOf(value2)); 
	}
	
	private static void assertEqual(Identified identified1, Identified identified2, List<URI> value1, List<URI> value2, URI property)
	{
		if (value1!=null && value2!=null)
		{
			String message=String.format("Could not read the %s property. Entity: %s", property.toString(), identified1.getUri().toString());
			Collections.sort(value1);
			Collections.sort(value2);
			
			assertEquals(message, value1, value2); 
		}
	}
	
	private static void assertEqualStrings(Identified identified1, Identified identified2, List<String> value1, List<String> value2, URI property)
	{
		if (value1!=null && value2!=null)
		{
			String message=String.format("Could not read the %s property. Entity: %s", property.toString(), identified1.getUri().toString());
			Collections.sort(value1);
			Collections.sort(value2);
			
			assertEquals(message, value1, value2); 
		}
	}
}
