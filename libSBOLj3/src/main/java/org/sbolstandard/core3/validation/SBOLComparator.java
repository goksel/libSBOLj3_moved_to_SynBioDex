package org.sbolstandard.core3.validation;

import java.net.URI;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;

import org.apache.jena.datatypes.xsd.XSDDateTime;
import org.sbolstandard.core3.entity.Attachment;
import org.sbolstandard.core3.entity.Collection;
import org.sbolstandard.core3.entity.CombinatorialDerivation;
import org.sbolstandard.core3.entity.Component;
import org.sbolstandard.core3.entity.ComponentReference;
import org.sbolstandard.core3.entity.Constraint;
import org.sbolstandard.core3.entity.Experiment;
import org.sbolstandard.core3.entity.ExperimentalData;
import org.sbolstandard.core3.entity.ExternallyDefined;
import org.sbolstandard.core3.entity.Feature;
import org.sbolstandard.core3.entity.Identified;
import org.sbolstandard.core3.entity.Implementation;
import org.sbolstandard.core3.entity.Interaction;
import org.sbolstandard.core3.entity.Interface;
import org.sbolstandard.core3.entity.LocalSubComponent;
import org.sbolstandard.core3.entity.Model;
import org.sbolstandard.core3.entity.Participation;
import org.sbolstandard.core3.entity.SBOLDocument;
import org.sbolstandard.core3.entity.Sequence;
import org.sbolstandard.core3.entity.SequenceFeature;
import org.sbolstandard.core3.entity.SubComponent;
import org.sbolstandard.core3.entity.TopLevel;
import org.sbolstandard.core3.entity.VariableFeature;
import org.sbolstandard.core3.entity.measure.BinaryPrefix;
import org.sbolstandard.core3.entity.measure.Measure;
import org.sbolstandard.core3.entity.measure.Prefix;
import org.sbolstandard.core3.entity.measure.PrefixedUnit;
import org.sbolstandard.core3.entity.measure.SIPrefix;
import org.sbolstandard.core3.entity.measure.SingularUnit;
import org.sbolstandard.core3.entity.measure.Unit;
import org.sbolstandard.core3.entity.measure.UnitDivision;
import org.sbolstandard.core3.entity.measure.UnitExponentiation;
import org.sbolstandard.core3.entity.measure.UnitMultiplication;
import org.sbolstandard.core3.entity.provenance.Activity;
import org.sbolstandard.core3.entity.provenance.Agent;
import org.sbolstandard.core3.entity.provenance.Association;
import org.sbolstandard.core3.entity.provenance.Plan;
import org.sbolstandard.core3.entity.provenance.Usage;
import org.sbolstandard.core3.util.SBOLGraphException;
import org.sbolstandard.core3.util.SBOLUtil;
import org.sbolstandard.core3.vocabulary.DataModel;
import org.sbolstandard.core3.vocabulary.MeasureDataModel;
import org.sbolstandard.core3.vocabulary.ProvenanceDataModel;
import org.sbolstandard.core3.vocabulary.RoleIntegration;

public class SBOLComparator {

	private static StringBuilder add(StringBuilder builder, StringBuilder message)
	{
		if (builder==null)
		{
			builder=new StringBuilder();
		}
		if (message!=null && message.length()>0)
		{
			if (builder.length()>0)
			{
				builder.append(System.lineSeparator());
			}
			builder.append(message);
		}
		return builder;
	}
	
	private static StringBuilder add(StringBuilder builder, String message)
	{
		if (builder==null)
		{
			builder=new StringBuilder();
		}
		if (message!=null)
		{
			if (builder.length()>0)
			{
				builder.append(System.lineSeparator());
			}
			builder.append(message);
		}
		return builder;
	}
	
	public static String assertEqual(SBOLDocument doc1, SBOLDocument doc2) throws SBOLGraphException
	{
		StringBuilder output=null;
		output = add(output, assertEqualEntity(doc1.getActivities(), doc2,Activity.class));
		output = add(output, assertEqualEntity(doc1.getAgents(), doc2,Agent.class));
		output = add(output, assertEqualEntity(doc1.getAttachments(), doc2,Attachment.class));
		output = add(output, assertEqualEntity(doc1.getBinaryPrefixes(), doc2,BinaryPrefix.class));
		output = add(output, assertEqualEntity(doc1.getCollections(), doc2,Collection.class));
		output = add(output, assertEqualEntity(doc1.getCombinatorialDerivations(), doc2,CombinatorialDerivation.class));
		output = add(output, assertEqualEntity(doc1.getComponents(), doc2,Component.class));
		output = add(output, assertEqualEntity(doc1.getExperimentalData(), doc2,ExperimentalData.class));
		output = add(output, assertEqualEntity(doc1.getExperiments(), doc2,Experiment.class));
		output = add(output, assertEqualEntity(doc1.getImplementations(), doc2,Implementation.class));
		output = add(output, assertEqualEntity(doc1.getModels(), doc2,Model.class));
		output = add(output, assertEqualEntity(doc1.getPlans(), doc2,Plan.class));
		output = add(output, assertEqualEntity(doc1.getPrefixedUnits(), doc2,PrefixedUnit.class));
		output = add(output, assertEqualEntity(doc1.getSequences(), doc2,Sequence.class));
		output = add(output, assertEqualEntity(doc1.getSingularUnits(), doc2,SingularUnit.class));
		output = add(output, assertEqualEntity(doc1.getSIPrefixes(), doc2,SIPrefix.class));
		output = add(output, assertEqualEntity(doc1.getUnitDivisions(), doc2,UnitDivision.class));
		output = add(output, assertEqualEntity(doc1.getUnitExponentiations(), doc2,UnitExponentiation.class));
		output = add(output, assertEqualEntity(doc1.getUnitMultiplications(), doc2,UnitMultiplication.class));
		if (output!=null)
		{
			return output.toString();
		}
		else
		{
			return null;
		}
		/*if (doc1.getAgents()!=null)
		{
			for (Agent agent:doc1.getAgents())
			{
				Agent agent2=(Agent) doc2.getIdentified(agent.getUri(), Agent.class);
				assertEqual(agent, agent2);
			}
		}*/
	
	}
	
	private static <T extends Identified> StringBuilder assertEqualEntity(List<T> entities, SBOLDocument doc2, Class<T> classType) throws SBOLGraphException
	{
		StringBuilder output=null;
		if (entities!=null)
		{
			for (T entity:entities)
			{
				T entity2=(T) doc2.getIdentified(entity.getUri(), classType );
				output = add(output, assertEqualEntity(entity,entity2));
			}
		}
		return output;
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
	
	
	private static <T extends Identified> StringBuilder assertEqualEntity(List<T> entities, List<T> entities2) throws SBOLGraphException
	{
		StringBuilder output=null;
		if (entities!=null && entities2!=null)
		{
			for (T entity:entities)
			{
				Identified entity2=getEntity(entities2, entity.getUri());
				output = add(output, assertEqualEntity(entity,entity2));
			}
		}
		return output;
	}
	
	public static StringBuilder assertEqualEntity(Identified identified1, Identified identified2)  throws SBOLGraphException
	{
		StringBuilder output=null;
		if (identified1!=null && identified2==null)
		{
			throw new SBOLGraphException("Could not compare " + identified1.getUri());
		}
		
		if (identified1 instanceof Activity)
		{
			output = add(output, assertEqualActivity((Activity)identified1,  (Activity) identified2));
		}
		else if (identified1 instanceof Agent)
		{
			output = add(output, assertEqualAgent((Agent)identified1,  (Agent) identified2));
		}
		else if (identified1 instanceof Attachment)
		{
			output = add(output, assertEqualAttachment((Attachment)identified1,  (Attachment) identified2));
		}
		else if (identified1 instanceof BinaryPrefix)
		{
			output = add(output, assertEqualBinaryPrefix((BinaryPrefix)identified1, (BinaryPrefix) identified2));
		}
		else if (identified1 instanceof Collection)
		{
			output = add(output, assertEqualCollection((Collection)identified1, (Collection) identified2));
		}
		else if (identified1 instanceof CombinatorialDerivation)
		{
			output = add(output, assertEqualCombinatorialDerivation((CombinatorialDerivation)identified1, (CombinatorialDerivation) identified2));
		}
		
		else if (identified1 instanceof Component)
		{
			output = add(output, assertEqualComponent((Component)identified1,  (Component) identified2));
		}
		else if (identified1 instanceof ExperimentalData)
		{
			output = add(output, assertEqualExperimentalData((ExperimentalData)identified1, (ExperimentalData) identified2));
		}
		else if (identified1 instanceof Experiment)
		{
			output = add(output, assertEqualExperiment((Experiment)identified1, (Experiment) identified2));
		}
		else if (identified1 instanceof Implementation)
		{
			output = add(output, assertEqualImplementation((Implementation)identified1, (Implementation) identified2));
		}
		/*else if (identified1 instanceof Model)
		{
			output = add(output, assertEqualModel((Model)identified1, (Model) identified2));
		}*/
		else if (identified1 instanceof Model)
		{
			output = add(output, assertEqualModel((Model)identified1, (Model) identified2));
		}
		else if (identified1 instanceof Plan)
		{
			output = add(output, assertEqualPlan((Plan)identified1,  (Plan) identified2));
		}
		else if (identified1 instanceof PrefixedUnit)
		{
			output = add(output, assertEqualPrefixedUnit((PrefixedUnit)identified1, (PrefixedUnit) identified2));
		}
		else if (identified1 instanceof Sequence)
		{
			output = add(output, assertEqualSequence((Sequence)identified1, (Sequence)identified2));
		}
		else if (identified1 instanceof SingularUnit)
		{
			output = add(output, assertEqualSingularUnit((SingularUnit)identified1, (SingularUnit) identified2));
		}	
		else if (identified1 instanceof SIPrefix)
		{
			output = add(output, assertEqualSIPrefix((SIPrefix)identified1, (SIPrefix) identified2));
		}
		else if (identified1 instanceof UnitDivision)
		{
			output = add(output, assertEqualUnitDivision((UnitDivision)identified1, (UnitDivision) identified2));
		}
		else if (identified1 instanceof UnitExponentiation)
		{
			output = add(output, assertEqualUnitExponentiation((UnitExponentiation)identified1, (UnitExponentiation) identified2));
		}
		else if (identified1 instanceof UnitMultiplication)
		{
			output = add(output, assertEqualUnitMultiplication((UnitMultiplication)identified1, (UnitMultiplication) identified2));
		}
		else if (identified1 instanceof Measure)
		{
			output = add(output, assertEqualMeasure((Measure)identified1,  (Measure) identified2));
		}
		else if (identified1 instanceof Association)
		{
			output = add(output, assertEqualAssociation((Association)identified1,  (Association) identified2));
		}
		else if (identified1 instanceof Usage)
		{
			output = add(output, assertEqualUsage((Usage)identified1,  (Usage) identified2));
		}
		else if (identified1 instanceof VariableFeature)
		{
			output = add(output, assertEqualVariableComponent((VariableFeature)identified1, (VariableFeature) identified2));
		}
		else if (identified1 instanceof ExternallyDefined)
		{
			output = add(output, assertEqualExternallyDefined((ExternallyDefined)identified1,  (ExternallyDefined) identified2));
		}
		else if (identified1 instanceof ComponentReference)
		{
			output = add(output, assertEqualComponentReference((ComponentReference)identified1,  (ComponentReference) identified2));
		}
		else if (identified1 instanceof Constraint)
		{
			output = add(output, assertEqualConstraint((Constraint)identified1,  (Constraint) identified2));
		}
		else if (identified1 instanceof LocalSubComponent)
		{
			output = add(output, assertEqualLocalSubComponent((LocalSubComponent)identified1,  (LocalSubComponent) identified2));
		}
		else if (identified1 instanceof SequenceFeature)
		{
			output = add(output, assertEqualSequenceFeature((SequenceFeature)identified1,  (SequenceFeature) identified2));
		}
		else if (identified1 instanceof SubComponent)
		{
			output = add(output, assertEqualSubComponent((SubComponent)identified1,  (SubComponent) identified2));
		}
		else if (identified1 instanceof Interaction)
		{
			output = add(output, assertEqualInteraction((Interaction)identified1,  (Interaction) identified2));
		}
		else if (identified1 instanceof Participation)
		{
			output = add(output, assertEqualParticipation((Participation)identified1,  (Participation) identified2));
		}
		else
		{
			throw new SBOLGraphException ("No assert method has been defined for " + identified1.getUri());
		}
		return output;
	}
	
	private static StringBuilder assertEqualActivity(Activity entity1, Activity entity2) throws SBOLGraphException
	{
		StringBuilder output=null;
		if (entity1!=null)
		{
			output = add(output,assertEqual(entity1, entity2));
			output = add(output,assertEqual(entity1, entity2, entity1.getTypes(),entity2.getTypes(), ProvenanceDataModel.Activity.type));
			output = add(output,assertEqual(entity1, entity2, entity1.getWasInformedBys(),entity2.getWasInformedBys(), ProvenanceDataModel.Activity.wasInformedBy));
			output = add(output,assertEqual(entity1, entity2, entity1.getEndedAtTime(),entity2.getEndedAtTime(), ProvenanceDataModel.Activity.endedAtTime));
			output = add(output,assertEqual(entity1, entity2, entity1.getStartedAtTime(),entity2.getStartedAtTime(), ProvenanceDataModel.Activity.startedAtTime));
			output = add(output,assertEqualEntity(entity1.getAssociations(), entity2.getAssociations()));
			output = add(output,assertEqualEntity(entity1.getUsages(), entity2.getUsages()));
		}
		return output;
	}
	
	private static StringBuilder assertEqualAssociation(Association entity1, Association entity2)  throws SBOLGraphException
	{
		StringBuilder output=null;
		if (entity1!=null)
		{
			output = add(output,assertEqual(entity1, entity2));
			output = add(output,assertEqual(entity1, entity2, entity1.getAgent(),entity2.getAgent(), ProvenanceDataModel.Association.agent));
			output = add(output,assertEqual(entity1, entity2, entity1.getPlan(),entity2.getPlan(), ProvenanceDataModel.Association.plan));
			output = add(output,assertEqual(entity1, entity2, entity1.getRoles(),entity2.getRoles(), ProvenanceDataModel.Association.role));	
		}
		return output;
	}
	
	private static StringBuilder assertEqualUsage(Usage entity1, Usage entity2)  throws SBOLGraphException
	{
		StringBuilder output=null;
		if (entity1!=null)
		{
			output = add(output,assertEqual(entity1, entity2));
			output = add(output,assertEqual(entity1, entity2, entity1.getRoles(),entity2.getRoles(), ProvenanceDataModel.Usage.role));
			output = add(output,assertEqual(entity1, entity2, entity1.getEntity(),entity2.getEntity(), ProvenanceDataModel.Usage.entity));
		}
		return output;
	}
	
	private static StringBuilder assertEqualAgent(Agent entity1, Agent entity2)  throws SBOLGraphException
	{
		StringBuilder output=null;
		if (entity1!=null)
		{
			output = add(output, assertEqual(entity1, entity2));
		}
		return output;
	}
	
	private static StringBuilder assertEqualAttachment(Attachment entity1, Attachment entity2)  throws SBOLGraphException
	{
		StringBuilder output=null;
		if (entity1!=null)
		{
			output = add(output, assertEqual(entity1, entity2));
			output = add(output, assertEqual(entity1, entity2, entity1.getFormat(),entity2.getFormat(), DataModel.Attachment.format));
			output = add(output, assertEqual(entity1, entity2, entity1.getHash(),entity2.getHash(), DataModel.Attachment.hash));
			output = add(output, assertEqual(entity1, entity2, entity1.getHashAlgorithm(),entity2.getHashAlgorithm(), DataModel.Attachment.hashAlgorithm));
			output = add(output, assertEqual(entity1, entity2, entity1.getSource(),entity2.getSource(), DataModel.Attachment.source));
			output = add(output, assertEqual(entity1, entity2, entity1.getSize().toString(),entity2.getSize().toString(), DataModel.Attachment.size));
		}
		return output;
	}
	
	private static StringBuilder assertEqualBinaryPrefix(BinaryPrefix entity1, BinaryPrefix entity2)  throws SBOLGraphException
	{
		StringBuilder output=null;
		if (entity1!=null)
		{
			output = add(output, assertEqualPrefix(entity1, entity2));
		}
		return output;
	}
	
	private static StringBuilder assertEqualSIPrefix(SIPrefix entity1, SIPrefix entity2)  throws SBOLGraphException
	{
		StringBuilder output=null;
		if (entity1!=null)
		{
			output = add(output, assertEqualPrefix(entity1, entity2));
		}
		return output;
	}
	
	private static StringBuilder assertEqualPrefix(Prefix entity1, Prefix entity2)  throws SBOLGraphException
	{
		StringBuilder output=null;
		if (entity1!=null)
		{
			output = add(output, assertEqualUnit(entity1, entity2));
			output = add(output, assertEqual(entity1, entity2, entity1.getFactor().toString(),entity2.getFactor().toString(), MeasureDataModel.Prefix.factor));
		}
		return output;
	}
	
	private static StringBuilder assertEqualUnit(Unit entity1, Unit entity2)  throws SBOLGraphException
	{
		StringBuilder output=null;
		if (entity1!=null)
		{
			output = add(output, assertEqual(entity1, entity2));
			output = add(output, assertEqual(entity1, entity2, entity1.getComment(),entity2.getComment(), MeasureDataModel.Unit.comment));
			output = add(output, assertEqual(entity1, entity2, entity1.getLabel(),entity2.getLabel(), MeasureDataModel.Unit.label));
			output = add(output, assertEqual(entity1, entity2, entity1.getLongComment(),entity2.getLongComment(), MeasureDataModel.Unit.longComment));
			output = add(output, assertEqual(entity1, entity2, entity1.getSymbol(),entity2.getSymbol(), MeasureDataModel.Unit.symbol));	
			output = add(output, assertEqualStrings(entity1, entity2, entity1.getAlternativeLabels(),entity2.getAlternativeLabels(), MeasureDataModel.Unit.alternativeLabel));
			output = add(output, assertEqualStrings(entity1, entity2, entity1.getAlternativeSymbols(),entity2.getAlternativeSymbols(), MeasureDataModel.Unit.alternativeSymbol));		
		}
		return output;
	}
	
	
	private static StringBuilder assertEqualCollection(Collection entity1, Collection entity2)  throws SBOLGraphException
	{
		StringBuilder output=null;
		if (entity1!=null)
		{
			output = add(output, assertEqual(entity1, entity2));
			output = add(output, assertEqual(entity1, entity2, entity1.getMembers(),entity2.getMembers(), DataModel.Collection.member));
		}
		return output;
	}
	
	private static StringBuilder assertEqualCombinatorialDerivation(CombinatorialDerivation entity1, CombinatorialDerivation entity2)  throws SBOLGraphException
	{
		StringBuilder output=null;
		if (entity1!=null)
		{
			output = add(output, assertEqual(entity1, entity2));
			if (entity1.getStrategy()!=null && entity2.getStrategy()!=null)
			{
				output = add(output, assertEqual(entity1, entity2, entity1.getStrategy().getUri(),entity2.getStrategy().getUri(), DataModel.CombinatorialDerivation.strategy));
			}
			
			else 
			{
				output=add(output, assertBothNullOrNotNull(entity1, entity1, entity1.getStrategy(), entity2.getStrategy(), DataModel.CombinatorialDerivation.strategy));
			}
			output = add(output, assertEqual(entity1, entity2, entity1.getTemplate(),entity2.getTemplate(), DataModel.CombinatorialDerivation.template));
			output = add(output, assertEqualEntity(entity1.getVariableFeatures(), entity2.getVariableFeatures()));	
		}
		return output;
	}
	
	private static StringBuilder assertEqualVariableComponent(VariableFeature entity1, VariableFeature entity2)  throws SBOLGraphException
	{
		StringBuilder output=null;
		if (entity1!=null)
		{
			output = add(output, assertEqual(entity1, entity2));
			output = add(output, assertEqual(entity1, entity2, entity1.getCardinality().getUri(),entity2.getCardinality().getUri(), DataModel.VariableFeature.cardinality));
			output = add(output, assertEqual(entity1, entity2, entity1.getVariable(),entity2.getVariable(), DataModel.VariableFeature.variable));
			output = add(output, assertEqual(entity1, entity2, entity1.getVariantCollections(),entity2.getVariantCollections(), DataModel.VariableFeature.variantCollection));
			output = add(output, assertEqual(entity1, entity2, entity1.getVariantDerivations(),entity2.getVariantDerivations(), DataModel.VariableFeature.variantDerivation));
			output = add(output, assertEqual(entity1, entity2, entity1.getVariants(),entity2.getVariants(), DataModel.VariableFeature.variant));	
		}
		return output;
	}
	
	private static StringBuilder assertEqualComponent(Component entity1, Component entity2) throws SBOLGraphException
	{
		StringBuilder output=null;
		if (entity1!=null)
		{
			output = add(output, assertEqual(entity1, entity2));
			output = add(output, assertEqual(entity1, entity2, SBOLUtil.getURIs(entity1.getModels()),SBOLUtil.getURIs(entity2.getModels()), DataModel.Component.model));
			output = add(output, assertEqual(entity1, entity2, entity1.getRoles(),entity2.getRoles(), DataModel.role));
			output = add(output, assertEqual(entity1, entity2, SBOLUtil.getURIs(entity1.getSequences()),SBOLUtil.getURIs(entity2.getSequences()), DataModel.Component.sequence));
			output = add(output, assertEqual(entity1, entity2, entity1.getTypes(),entity2.getTypes(), DataModel.type));
			output = add(output, assertEqualInterface(entity1.getInterface(),entity2.getInterface()));
			output = add(output, assertEqualEntity(entity1.getComponentReferences(), entity2.getComponentReferences()));
			output = add(output, assertEqualEntity(entity1.getConstraints(), entity2.getConstraints()));
			output = add(output, assertEqualEntity(entity1.getExternallyDefineds(), entity2.getExternallyDefineds()));
			output = add(output, assertEqualEntity(entity1.getInteractions(), entity2.getInteractions()));
			output = add(output, assertEqualEntity(entity1.getLocalSubComponents(), entity2.getLocalSubComponents()));
			output = add(output, assertEqualEntity(entity1.getSequenceFeatures(), entity2.getSequenceFeatures()));
			output = add(output, assertEqualEntity(entity1.getSubComponents(), entity2.getSubComponents()));
		}
		return output;
	}
	
	private static StringBuilder assertEqualInterface(Interface entity1, Interface entity2) throws SBOLGraphException
	{
		StringBuilder output=null;
		if (entity1!=null)
		{
			output = add(output, assertEqual(entity1, entity2));
			output = add(output, assertEqual(entity1, entity2, entity1.getInputs(),entity2.getInputs(), DataModel.Interface.input));
			output = add(output, assertEqual(entity1, entity2, entity1.getNonDirectionals(),entity2.getNonDirectionals(), DataModel.Interface.nondirectional));
			output = add(output, assertEqual(entity1, entity2, entity1.getOutputs(),entity2.getOutputs(), DataModel.Interface.output));
		}
		return output;
	}
	
	private static StringBuilder assertEqualComponentReference(ComponentReference entity1, ComponentReference entity2) throws SBOLGraphException
	{
		StringBuilder output=null;
		if (entity1!=null)
		{
			output = add(output, assertEqualFeature(entity1, entity2));
			output = add(output, assertEqual(entity1, entity2, entity1.getRefersTo(),entity2.getRefersTo(), DataModel.ComponentReference.refersTo));
			output = add(output, assertEqual(entity1, entity2, entity1.getInChildOf(),entity2.getInChildOf(), DataModel.ComponentReference.inChildOf));
		}
		return output;
	}
	
	private static StringBuilder assertEqualConstraint(Constraint entity1, Constraint entity2) throws SBOLGraphException
	{
		StringBuilder output=null;
		if (entity1!=null)
		{
			output = add(output, assertEqual(entity1, entity2));
			output = add(output, assertEqual(entity1, entity2, entity1.getObject(),entity2.getObject(), DataModel.Constraint.object));
			output = add(output, assertEqual(entity1, entity2, entity1.getSubject(),entity2.getSubject(), DataModel.Constraint.subject));
			output = add(output, assertEqual(entity1, entity2, entity1.getRestriction(),entity2.getRestriction(), DataModel.Constraint.restriction));
		}
		return output;
	}
	
	private static StringBuilder assertEqualExternallyDefined(ExternallyDefined entity1, ExternallyDefined entity2) throws SBOLGraphException
	{
		StringBuilder output=null;
		if (entity1!=null)
		{
			output = add(output, assertEqualFeature(entity1, entity2));
			output = add(output, assertEqual(entity1, entity2, entity1.getDefinition(),entity2.getDefinition(), DataModel.ExternalyDefined.definition));
		}
		return output;
	}
	
	private static StringBuilder assertEqualLocalSubComponent(LocalSubComponent entity1, LocalSubComponent entity2) throws SBOLGraphException
	{
		StringBuilder output=null;
		if (entity1!=null)
		{
			output = add(output, assertEqualFeature(entity1, entity2));
		//TODO: assertEqualLocations(entity1, entity2, entity1.getLocations(),entity2.getLocations(), DataModel.SubComponent.location);
			output = add(output, assertEqual(entity1, entity2, entity1.getTypes(),entity2.getTypes(), DataModel.type));
		}
		return output;
	}
	
	private static StringBuilder assertEqualFeature(Feature entity1, Feature entity2) throws SBOLGraphException
	{
		StringBuilder output=null;
		if (entity1!=null)
		{
			assertEqual(entity1, entity2);
			if (entity1.getOrientation()!=null && entity2.getOrientation()!=null)
			{
				output = add(output, assertEqual(entity1, entity2, entity1.getOrientation().getUri(),entity2.getOrientation().getUri(), DataModel.orientation));
			}
			
			else 
			{
				output=add(output, assertBothNullOrNotNull(entity1, entity1, entity1.getOrientation(), entity2.getOrientation(), DataModel.orientation));
			}
			output = add(output, assertEqual(entity1, entity2, entity1.getRoles(),entity2.getRoles(), DataModel.role));
		}
		return output;
	}
	
	 
	private static StringBuilder assertEqualSequenceFeature(SequenceFeature entity1, SequenceFeature entity2) throws SBOLGraphException
	{
		StringBuilder output=null;
		if (entity1!=null)
		{
			output = add(output, assertEqualFeature(entity1, entity2));
		}
		return output;
		//TODO: assertEqualLocations(entity1, entity2, entity1.getLocations(),entity2.getLocations(), DataModel.SubComponent.location);
	}
	
	private static StringBuilder assertEqualSubComponent(SubComponent entity1, SubComponent entity2) throws SBOLGraphException
	{
		StringBuilder output=null;
		if (entity1!=null)
		{
			output = add(output, assertEqualFeature(entity1, entity2));
		//TODO: assertEqualLocations(entity1, entity2, entity1.getLocations(),entity2.getLocations(), DataModel.SubComponent.location);
		//TODO: assertEqualLocations(entity1, entity2, entity1.getSourceLocations(),entity2.getSourceLocations(), DataModel.SubComponent.sourcelocation);
			output = add(output, assertEqual(entity1, entity2, entity1.getIsInstanceOf(),entity2.getIsInstanceOf(), DataModel.SubComponent.instanceOf));
			output = add(output, assertEqualEnum(entity1, entity2, entity1.getRoleIntegration(),entity2.getRoleIntegration(), DataModel.SubComponent.roleIntegration));		
		}
		return output;
	}
	
	private static StringBuilder assertEqualInteraction(Interaction entity1, Interaction entity2) throws SBOLGraphException
	{
		StringBuilder output=null;
		if (entity1!=null)
		{
			output = add(output, assertEqual(entity1, entity2));
			output = add(output, assertEqualEntity(entity1.getParticipations(), entity2.getParticipations()));
			output = add(output, assertEqual(entity1, entity2, entity1.getTypes(),entity2.getTypes(), DataModel.type));	
		}
		return output;
	}
	
	private static StringBuilder assertEqualParticipation(Participation entity1, Participation entity2) throws SBOLGraphException
	{
		StringBuilder output=null;
		if (entity1!=null)
		{
			output = add(output, assertEqual(entity1, entity2));
			output = add(output, assertEqual(entity1, entity2, entity1.getParticipant(), entity2.getParticipant(),DataModel.Participation.participant));
			output = add(output, assertEqual(entity1, entity2, entity1.getRoles(),entity2.getRoles(), DataModel.role));	
		}
		return output;
	}
	
	private static StringBuilder assertEqualExperimentalData(ExperimentalData entity1, ExperimentalData entity2) throws SBOLGraphException
	{
		StringBuilder output=null;
		if (entity1!=null)
		{
			output = add(output, assertEqual(entity1, entity2));
		}
		return output;
	}
	
	private static StringBuilder assertEqualExperiment(Experiment entity1, Experiment entity2) throws SBOLGraphException
	{
		StringBuilder output=null;
		if (entity1!=null)
		{
			output = add(output, assertEqual(entity1, entity2));	
		}
		return output;
	}
	
	private static StringBuilder assertEqualImplementation(Implementation entity1, Implementation entity2) throws SBOLGraphException
	{
		StringBuilder output=null;
		if (entity1!=null)
		{
			output = add(output, assertEqual(entity1, entity2));		
			output = add(output, assertEqual(entity1, entity2, entity1.getComponent(), entity2.getComponent(),DataModel.Implementation.built));
		}
		return output;
	}
	
	private static StringBuilder assertEqualModel(Model entity1, Model entity2) throws SBOLGraphException
	{
		StringBuilder output=null;
		if (entity1!=null)
		{
			output = add(output, assertEqual(entity1, entity2));		
			output = add(output, assertEqual(entity1, entity2, entity1.getFramework(), entity2.getFramework(),DataModel.Model.framework));
			output = add(output, assertEqual(entity1, entity2, entity1.getLanguage(), entity2.getLanguage(),DataModel.Model.language));
			output = add(output, assertEqual(entity1, entity2, entity1.getSource(), entity2.getSource(),DataModel.Model.source));		
		}
		return output;
	}
	
	private static StringBuilder assertEqualPrefixedUnit(PrefixedUnit entity1, PrefixedUnit entity2) throws SBOLGraphException
	{
		StringBuilder output=null;
		if (entity1!=null)
		{
			output = add(output, assertEqualUnit(entity1, entity2));		
			output = add(output, assertEqual(entity1, entity2, entity1.getPrefix(), entity2.getPrefix(),MeasureDataModel.PrefixedUnit.prefix));
			output = add(output, assertEqual(entity1, entity2, entity1.getUnit(), entity2.getUnit(),MeasureDataModel.PrefixedUnit.unit));
		}
		return output;
	}
	
	private static StringBuilder assertEqualSingularUnit(SingularUnit entity1, SingularUnit entity2) throws SBOLGraphException
	{
		StringBuilder output=null;
		if (entity1!=null)
		{
			output = add(output, assertEqualUnit(entity1, entity2));		
			output = add(output, assertEqual(entity1, entity2, entity1.getFactor().toString(), entity2.getFactor().toString(),MeasureDataModel.SingularUnit.factor));
			output = add(output, assertEqual(entity1, entity2, entity1.getUnit(), entity2.getUnit(),MeasureDataModel.SingularUnit.unit));
		}
		return output;
	}
	
	private static StringBuilder assertEqualSequence(Sequence entity1, Sequence entity2) throws SBOLGraphException
	{
		StringBuilder output=null;
		if (entity1!=null)
		{
			output = add(output, assertEqual(entity1, entity2));		
			output = add(output, assertEqual(entity1, entity2, entity1.getElements(), entity2.getElements(),DataModel.Sequence.elements));
			output = add(output, assertEqual(entity1, entity2, entity1.getEncoding().getUri(), entity2.getEncoding().getUri(),DataModel.Sequence.encoding));
		}
		return output;
	}
	
	private static StringBuilder assertEqualUnitDivision(UnitDivision entity1, UnitDivision entity2) throws SBOLGraphException
	{
		StringBuilder output=null;
		if (entity1!=null)
		{
			output = add(output, assertEqualUnit(entity1, entity2));		
			output = add(output, assertEqual(entity1, entity2, entity1.getDenominator(), entity2.getDenominator(),MeasureDataModel.UnitDivision.denominator));
			output = add(output, assertEqual(entity1, entity2, entity1.getNumerator(), entity2.getNumerator(),MeasureDataModel.UnitDivision.numerator));		
		}
		return output;
	}

	private static StringBuilder assertEqualUnitExponentiation (UnitExponentiation entity1, UnitExponentiation entity2) throws SBOLGraphException
	{
		StringBuilder output=null;
		if (entity1!=null)
		{
			output = add(output, assertEqualUnit(entity1, entity2));		
			output = add(output, assertEqual(entity1, entity2, entity1.getBase(), entity2.getBase(),MeasureDataModel.UnitExponentiation.base));
			output = add(output, assertEqual(entity1, entity2, entity1.getExponent().toString(), entity2.getExponent().toString(),MeasureDataModel.UnitExponentiation.base));		
		}
		return output;
	}
	
	private static StringBuilder assertEqualUnitMultiplication(UnitMultiplication entity1, UnitMultiplication entity2) throws SBOLGraphException
	{
		StringBuilder output=null;
		if (entity1!=null)
		{
			output = add(output, assertEqualUnit(entity1, entity2));		
			output = add(output, assertEqual(entity1, entity2, entity1.getTerm1(), entity2.getTerm1(),MeasureDataModel.UnitMultiplication.term1));
			output = add(output, assertEqual(entity1, entity2, entity1.getTerm2(), entity2.getTerm2(),MeasureDataModel.UnitMultiplication.term2));		
		}
		return output;
	}
	
	private static StringBuilder assertEqualPlan(Plan entity1, Plan entity2) throws SBOLGraphException
	{
		StringBuilder output=null;
		if (entity1!=null)
		{
			output = add(output, assertEqual(entity1, entity2));
		}
		return output;
	}
	
	private static StringBuilder assertEqualMeasure(Measure entity1, Measure entity2) throws SBOLGraphException
	{
		StringBuilder output=null;
		if (entity1!=null)
		{
			output = add(output, assertEqual(entity1, entity2));
			output = add(output, assertEqual(entity1, entity2, entity1.getUnit(),entity2.getUnit(), MeasureDataModel.Measure.unit));
			output = add(output, assertEqual(entity1, entity2, entity1.getValue().toString(),entity2.getValue().toString(), MeasureDataModel.Measure.value));
			output = add(output, assertEqual(entity1, entity2, entity1.getTypes(),entity2.getTypes(), MeasureDataModel.Measure.type));	
		}
		return output;
	}
	
	private static StringBuilder assertEqual(TopLevel topLevel1, TopLevel topLevel2) throws SBOLGraphException
	{
		StringBuilder output=null;
		output = add(output, assertEqual((Identified)topLevel1, (Identified)topLevel2));	
		output = add(output, assertEqual(topLevel1,topLevel2, topLevel1.getNamespace(), topLevel2.getNamespace(), DataModel.TopLevel.namespace));	
		output = add(output, assertEqual(topLevel1, topLevel2, topLevel1.getAttachments(),topLevel2.getAttachments(), DataModel.TopLevel.attachment));
		return output;
	}
	
	private static StringBuilder assertEqual(Identified identified1, Identified identified2) throws SBOLGraphException
	{
		StringBuilder output=null;
		output = add(output, assertEqual(identified1, identified2, identified1.getName(),identified2.getName(), DataModel.Identified.name));
		output = add(output, assertEqual(identified1, identified2, identified1.getDescription(),identified2.getDescription(), DataModel.Identified.description));
		output = add(output, assertEqual(identified1, identified2, identified1.getDisplayId(),identified2.getDisplayId(), DataModel.Identified.displayId));
		output = add(output, assertEqual(identified1, identified2, identified1.getUri(),identified2.getUri(), DataModel.Identified.uri));
		output = add(output, assertEqual(identified1, identified2, identified1.getWasDerivedFrom(),identified2.getWasDerivedFrom(), DataModel.Identified.wasDerivedFrom));
		output = add(output, assertEqual(identified1, identified2, identified1.getWasGeneratedBy(),identified2.getWasGeneratedBy(), DataModel.Identified.wasGeneratedBy));
		output = add(output, assertEqualEntity(identified1.getMeasures(), identified2.getMeasures()));
		return output;
	}
	
	private static StringBuilder assertEqual(Identified identified1, Identified identified2, String value1, String value2, URI property)
	{
		StringBuilder output=null;
		String message=String.format("Could not read the %s property. Entity:%s, Value1:%s, Value2:%s", property.toString(), identified1.getUri().toString(), value1, value2);
		if (!stringEquals(value1,value2))
		{
			output = add(output,message);
		}
		return output;	
	}
	
	private static StringBuilder assertEqualEnum(Identified identified1, Identified identified2, Object value1, Object value2, URI property)
	{
		StringBuilder output=null;
		URI uri1=null;
		URI uri2=null;
		if (value1!=null && value1 instanceof RoleIntegration)
		{
			uri1=((RoleIntegration) value1).getUri();
		}
		if (value2!=null && value2 instanceof RoleIntegration)
		{
			uri2=((RoleIntegration) value2).getUri();
		}
		return assertEqual(identified1, identified2, uri1, uri2, property);
		
	}
	
	private static StringBuilder assertBothNullOrNotNull(Identified identified1, Identified identified2, Object value1, Object value2, URI property)
	{
		StringBuilder output=null;
		String message=null;
		if (value1==null && value2!=null)
		{
			message=String.format("Could not read the %s property. Entity:%s, Value1:%s, Value2:%s", property.toString(), identified1.getUri().toString(), value1, value2);		
		}
		else if (value1!=null && value2==null)	
		{
			message=String.format("Could not read the %s property. Entity:%s, Value1:%s, Value2:%s", property.toString(), identified2.getUri().toString(), value1, value2);					
		}
		output = add(output,message);
		return output;
		
	}
	
	public static boolean stringEquals(String s1, String s2){
	    if(s1 != null && s2!=null)
	    {
	    	return s1.equals(s2);
	    }
	    else if(s1 == null && s2==null)
	    {
	    	return true;
	    }
	    else
	    {
	    	return false;
	    }
	}
	
	private static StringBuilder assertEqual(Identified identified1, Identified identified2, URI value1, URI value2, URI property)
	{
		StringBuilder output=null;
		String message=String.format("Could not read the %s property. Entity:%s, Value1:%s, Value2:%s", property.toString(), identified1.getUri().toString(), value1, value2);
		if (!stringEquals(String.valueOf(value1),String.valueOf(value2)))
		{
			output = add(output,message);
		}
		return output;
	}
	
	private static StringBuilder assertEqual(Identified identified1, Identified identified2, Identified value1, Identified value2, URI property) throws SBOLGraphException
	{
		StringBuilder output=null;
		String message=String.format("Could not read the %s property. Entity:%s, Value1:%s, Value2:%s", property.toString(), identified1.getUri().toString(), value1, value2);
		output = add(output, assertEqual(value1, value2));	
		
		if (output!=null && output.length()>0)
		{
			output = add(output,message);
		}
		return output;
	}
	
	/*private static StringBuilder assertEqual(Identified identified1, Identified identified2, Optional<Float> value1, Optional<Float> value2, URI property)
	{
		StringBuilder output=null;
		String message=String.format("Could not read the %s property. Entity:%s, Value1:%s, Value2:%s", property.toString(), identified1.getUri().toString(), String.valueOf(value1), String.valueOf(value2));
		if (!stringEquals(String.valueOf(value1),String.valueOf(value2)))
		{
			output = add(output,message);
		}
		return output;
	}*/
	
	/*private static StringBuilder assertEqual(Identified identified1, Identified identified2, Optional<?> value1, Optional<?> value2, URI property)
	{
		StringBuilder output=null;
		String message=String.format("Could not read the %s property. Entity:%s, Value1:%s, Value2:%s", property.toString(), identified1.getUri().toString(), String.valueOf(value1), String.valueOf(value2));
		if (!stringEquals(String.valueOf(value1),String.valueOf(value2)))
		{
			output = add(output,message);
		}
		return output;
	}
	
	private static StringBuilder assertEqual(Identified identified1, Identified identified2, OptionalInt value1, OptionalInt value2, URI property)
	{
		StringBuilder output=null;
		String message=String.format("Could not read the %s property. Entity:%s, Value1:%s, Value2:%s", property.toString(), identified1.getUri().toString(), String.valueOf(value1), String.valueOf(value2));
		if (!stringEquals(String.valueOf(value1),String.valueOf(value2)))
		{
			output = add(output,message);
		}
		return output;
	}*/
	
	private static StringBuilder assertEqual(Identified identified1, Identified identified2, XSDDateTime value1, XSDDateTime value2, URI property)
	{
		StringBuilder output=null;
		String message=String.format("Could not read the %s property. Entity:%s, Value1:%s, Value2:%s", property.toString(), identified1.getUri().toString(), value1, value2);
		if (!stringEquals(String.valueOf(value1),String.valueOf(value2)))
		{
			output = add(output,message);
		}
		return output;
	}
	
	private static StringBuilder assertEqual(Identified identified1, Identified identified2, List<URI> value1, List<URI> value2, URI property)
	{
		StringBuilder output=null;
		if (value1!=null && value2!=null)
		{
			String message=String.format("Could not read the %s property. Entity: %s", property.toString(), identified1.getUri().toString());
			Collections.sort(value1);
			Collections.sort(value2);
			if (!value1.equals(value2))
			{
				output = add(output,message);
			} 
		}
		return output;
	}
	
	private static StringBuilder assertEqualStrings(Identified identified1, Identified identified2, List<String> value1, List<String> value2, URI property)
	{
		StringBuilder output=null;
		if (value1!=null && value2!=null)
		{
			String message=String.format("Could not read the %s property. Entity: %s", property.toString(), identified1.getUri().toString());
			Collections.sort(value1);
			Collections.sort(value2);
			if (!value1.equals(value2))
			{
				output = add(output,message);
			}
		}
		return output;
	}
}
