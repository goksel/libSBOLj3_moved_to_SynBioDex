package org.sbolstandard.vocabulary;

import java.net.URI;

import org.sbolstandard.util.NameSpace;

public class DataModel {
	
		public static URI type=NameSpace.SBOL.local("type");
		public static URI role=NameSpace.SBOL.local("role");
		public static URI orientation=NameSpace.SBOL.local("orientation");
		
		public static final class Feature
		{
			public static URI uri=NameSpace.SBOL.local("Feature");
		}
		
		public static final class Component
		{
			public static URI uri=NameSpace.SBOL.local("Component");
			public static URI interacton=NameSpace.SBOL.local("hasInteraction");
			public static URI feature=NameSpace.SBOL.local("hasFeature");	
			public static URI interaction=NameSpace.SBOL.local("hasInteraction");	
		}
		
		public static final class Sequence
		{
			public static URI uri=NameSpace.SBOL.local("Sequence");
		}
		
		public static final class SubComponent
		{
			public static URI uri=NameSpace.SBOL.local("SubComponent");
			public static URI instanceOf=NameSpace.SBOL.local("instanceOf");
			public static URI location=NameSpace.SBOL.local("hasLocation");	
			public static URI sourceLocation=NameSpace.SBOL.local("sourceLocation");	
			public static URI roleIntegration=NameSpace.SBOL.local("roleIntegration");
			
		}
		public static final class ComponentReference
		{
			public static URI uri=NameSpace.SBOL.local("ComponentReference");
			public static URI inChildOf=NameSpace.SBOL.local("inChildOf");
			public static URI hasFeature=NameSpace.SBOL.local("feature");	
		}
		
		public static final class LocalSubComponent
		{
			public static URI uri=NameSpace.SBOL.local("LocalSubComponent");
		}
		
		public static final class ExternalyDefined
		{
			public static URI uri=NameSpace.SBOL.local("ExternallyDefined");
			public static URI definition=NameSpace.SBOL.local("definition");
		}
		
		public static final class SequenceFeature
		{
			public static URI uri =NameSpace.SBOL.local("SequenceFeature");
		}
		
		public static final class Interaction
		{
			public static URI uri=NameSpace.SBOL.local("Interaction");
			public static URI participation=NameSpace.SBOL.local("hasParticipation");
		}
		
		public static final class Participation
		{
			public static URI uri=NameSpace.SBOL.local("Participation");
			public static URI participant=NameSpace.SBOL.local("participant");
		}
		
		public static final class Location
		{
			public static URI uri=NameSpace.SBOL.local("Location");
			public static URI order=NameSpace.SBOL.local("order");
		}
		public static final class Cut
		{
			public static URI uri=NameSpace.SBOL.local("Cut");
			public static URI at=NameSpace.SBOL.local("at");
		}
		public static final class Range
		{
			public static URI uri=NameSpace.SBOL.local("Range");
			public static URI start=NameSpace.SBOL.local("start");
			public static URI end=NameSpace.SBOL.local("end");
		}

}
