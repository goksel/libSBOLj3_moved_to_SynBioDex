package org.sbolstandard.vocabulary;

import java.net.URI;

public class DataModel {
	
		public static URI type=URI.create("http://sbols.org/v3#type");
		
		public static URI role=URI.create("http://sbols.org/v3#role");
		public static URI orientation=URI.create("http://sbols.org/v3#orientation");
		
		public static final class Component
		{
			public static URI uri=URI.create("http://sbols.org/v3#Component");
			public static URI interacton=URI.create("http://sbols.org/v3#hasInteraction");
			public static URI feature=URI.create("http://sbols.org/v3#hasFeature");		
		}
		
		public static final class Sequence
		{
			public static URI uri=URI.create("http://sbols.org/v3#Sequence");
		}
		
		public static final class SubComponent
		{
			public static URI instanceOf=URI.create("http://sbols.org/v3#instanceOf");
			public static URI location=URI.create("http://sbols.org/v3#hasLocation");	
			public static URI sourceLocation=URI.create("http://sbols.org/v3#sourceLocation");	
			public static URI roleIntegration=URI.create("http://sbols.org/v3#roleIntegration");
			
		}
		public static final class ComponentReference
		{
			public static URI inChildOf=URI.create("http://sbols.org/v3#inChildOf");
			public static URI hasFeature=URI.create("http://sbols.org/v3#feature");	
		}
		
		public static final class ExternalyDefined
		{
			public static URI definition=URI.create("http://sbols.org/v3#definition");
		}
		
		public static final class Interaction
		{
			public static URI uri=URI.create("http://sbols.org/v3#Interaction");
			public static URI participation=URI.create("http://sbols.org/v3#hasParticipation");
		}
		
		public static final class Participation
		{
			public static URI uri=URI.create("http://sbols.org/v3#Participation");
			public static URI participant=URI.create("http://sbols.org/v3#participant");
		}
		
		
		
		
		
		public static final class Location
		{
			public static URI order=URI.create("http://sbols.org/v3#order");
		}
		public static final class Cut
		{
			public static URI at=URI.create("http://sbols.org/v3#at");
		}
		public static final class Range
		{
			public static URI uri=URI.create("http://sbols.org/v3#Range");
			public static URI start=URI.create("http://sbols.org/v3#start");
			public static URI end=URI.create("http://sbols.org/v3#end");
		}
		
	
	
	public static final class Entity
	{
		public static URI Feature=URI.create("http://sbols.org/v3#Feature");
		public static URI SubComponent=URI.create("http://sbols.org/v3#SubComponent");
		public static URI ComponentReference=URI.create("http://sbols.org/v3#ComponentReference");
		public static URI LocalSubComponent=URI.create("http://sbols.org/v3#LocalSubComponent");
		public static URI ExternallyDefined=URI.create("http://sbols.org/v3#ExternallyDefined");
		public static URI SequenceFeature=URI.create("http://sbols.org/v3#SequenceFeature");
		
		public static URI Location=URI.create("http://sbols.org/v3#Location");
		public static URI Cut=URI.create("http://sbols.org/v3#Cut");
		
		
		
		
		
	}

}
