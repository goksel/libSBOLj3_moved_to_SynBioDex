package org.sbolstandard.vocabulary;

import java.net.URI;

import org.sbolstandard.util.URINameSpace;

public class DataModel {
	
		public static URI type=URINameSpace.SBOL.local("type");
		public static URI role=URINameSpace.SBOL.local("role");
		public static URI orientation=URINameSpace.SBOL.local("orientation");
		
		public static final class Identified
		{
			public static URI uri=URINameSpace.SBOL.local("Identified");	
			public static URI name=URINameSpace.SBOL.local("name");	
			public static URI description=URINameSpace.SBOL.local("description");	
			public static URI displayId=URINameSpace.SBOL.local("displayId");	
			public static URI wasDerivedFrom=URINameSpace.PROV.local("wasDerivedFrom");	
			public static URI wasGeneratedBy=URINameSpace.PROV.local("wasGeneratedBy");	
			public static URI measure=URINameSpace.SBOL.local("hasMeasure");		
		}
		
		public static final class TopLevel
		{
			public static URI uri=URINameSpace.SBOL.local("TopLevel");
			public static URI attachment=URINameSpace.SBOL.local("hasAttachment");	
		}
		
		public static final class Feature
		{
			public static URI uri=URINameSpace.SBOL.local("Feature");
		}
		
		public static final class Component
		{
			public static URI uri=URINameSpace.SBOL.local("Component");
			public static URI feature=URINameSpace.SBOL.local("hasFeature");	
			public static URI interaction=URINameSpace.SBOL.local("hasInteraction");	
			public static URI constraint=URINameSpace.SBOL.local("hasConstraint");	
			public static URI hasInterface=URINameSpace.SBOL.local("hasInterface");	
			public static URI model=URINameSpace.SBOL.local("hasModel");
			public static URI sequence=URINameSpace.SBOL.local("hasSequence");
			
		}
		
		public static final class Sequence
		{
			public static URI uri=URINameSpace.SBOL.local("Sequence");
			public static URI elements=URINameSpace.SBOL.local("elements");
			public static URI encoding=URINameSpace.SBOL.local("encoding");

		}
		
		public static final class SubComponent
		{
			public static URI uri=URINameSpace.SBOL.local("SubComponent");
			public static URI instanceOf=URINameSpace.SBOL.local("instanceOf");
			public static URI location=URINameSpace.SBOL.local("hasLocation");	
			public static URI sourceLocation=URINameSpace.SBOL.local("sourceLocation");	
			public static URI roleIntegration=URINameSpace.SBOL.local("roleIntegration");
			
		}
		public static final class ComponentReference
		{
			public static URI uri=URINameSpace.SBOL.local("ComponentReference");
			public static URI inChildOf=URINameSpace.SBOL.local("inChildOf");
			public static URI feature=URINameSpace.SBOL.local("hasFeature");	
		}
		
		public static final class LocalSubComponent
		{
			public static URI uri=URINameSpace.SBOL.local("LocalSubComponent");
		}
		
		public static final class ExternalyDefined
		{
			public static URI uri=URINameSpace.SBOL.local("ExternallyDefined");
			public static URI definition=URINameSpace.SBOL.local("definition");
		}
		
		public static final class SequenceFeature
		{
			public static URI uri =URINameSpace.SBOL.local("SequenceFeature");
		}
		
		public static final class Interaction
		{
			public static URI uri=URINameSpace.SBOL.local("Interaction");
			public static URI participation=URINameSpace.SBOL.local("hasParticipation");
		}
		
		public static final class Participation
		{
			public static URI uri=URINameSpace.SBOL.local("Participation");
			public static URI participant=URINameSpace.SBOL.local("participant");
		}
		
		public static final class Location
		{
			public static URI uri=URINameSpace.SBOL.local("Location");
			public static URI order=URINameSpace.SBOL.local("order");
			public static URI sequence=URINameSpace.SBOL.local("hasSequence");
			
		}
		public static final class Cut
		{
			public static URI uri=URINameSpace.SBOL.local("Cut");
			public static URI at=URINameSpace.SBOL.local("at");
		}
		public static final class Range
		{
			public static URI uri=URINameSpace.SBOL.local("Range");
			public static URI start=URINameSpace.SBOL.local("start");
			public static URI end=URINameSpace.SBOL.local("end");
		}
		public static final class EntireSequenceLocation
		{
			public static URI uri=URINameSpace.SBOL.local("EntireSequence");
		}
		
		public static final class Constraint
		{
			public static URI uri=URINameSpace.SBOL.local("Constraint");
			public static URI restriction=URINameSpace.SBOL.local("restriction");
			public static URI subject=URINameSpace.SBOL.local("subject");
			public static URI object=URINameSpace.SBOL.local("object");	
		}
		
		public static final class Interface
		{
			public static URI uri=URINameSpace.SBOL.local("Interface");
			public static URI input=URINameSpace.SBOL.local("input");
			public static URI output=URINameSpace.SBOL.local("output");
			public static URI nondirectional=URINameSpace.SBOL.local("nondirectional");
			
		}
		
		public static final class CombinatorialDerivation
		{
			public static URI uri=URINameSpace.SBOL.local("CombinatorialDerivation");
			public static URI template=URINameSpace.SBOL.local("input");
			public static URI variableComponent=URINameSpace.SBOL.local("hasVariableComponent");
			public static URI strategy=URINameSpace.SBOL.local("strategy");	
		}
		
		public static final class VariableComponent
		{
			public static URI uri=URINameSpace.SBOL.local("VariableComponent");
			public static URI cardinality=URINameSpace.SBOL.local("cardinality");
			public static URI variant=URINameSpace.SBOL.local("variant");
			public static URI variable=URINameSpace.SBOL.local("variable");	
			public static URI variantDerivation=URINameSpace.SBOL.local("variantDerivation");	
			public static URI variantCollection=URINameSpace.SBOL.local("variantCollection");	
		}
		
		
		public static final class Implementation
		{
			public static URI uri=URINameSpace.SBOL.local("Implementation");
			public static URI built=URINameSpace.SBOL.local("built");	
		}
		
		public static final class ExperimentalData
		{
			public static URI uri=URINameSpace.SBOL.local("ExperimentalData");
		}
		
		public static final class Model
		{
			public static URI uri=URINameSpace.SBOL.local("Model");
			public static URI source=URINameSpace.SBOL.local("source");
			public static URI language=URINameSpace.SBOL.local("language");
			public static URI framework=URINameSpace.SBOL.local("framework");	
		}
		
		public static final class Collection
		{
			public static URI uri=URINameSpace.SBOL.local("Collection");
			public static URI member=URINameSpace.SBOL.local("member");
		}
		
		public static final class Namespace
		{
			public static URI uri=URINameSpace.SBOL.local("Namespace");
		}
		
		public static final class Experiment
		{
			public static URI uri=URINameSpace.SBOL.local("Experiment");
		}
		
		public static final class Attachment
		{
			public static URI uri=URINameSpace.SBOL.local("Attachment");
			public static URI source=URINameSpace.SBOL.local("source");
			public static URI size=URINameSpace.SBOL.local("size");
			public static URI format=URINameSpace.SBOL.local("format");	
			public static URI hash=URINameSpace.SBOL.local("hash");	
			public static URI hashAlgorithm=URINameSpace.SBOL.local("hashAlgorithm");	
			
		}
		
}
