package org.sbolstandard.vocabulary;

import java.net.URI;

import org.sbolstandard.util.URINameSpace;

public class ProvenanceDataModel {
	
	public static final class Agent
	{
		public static URI uri=URINameSpace.PROV.local("Agent");
	}

	public static final class Association 
	{
		public static URI uri=URINameSpace.PROV.local("Association");
		public static URI role=URINameSpace.PROV.local("hadRole");
		public static URI agent=URINameSpace.PROV.local("hadAgent");
		public static URI plan=URINameSpace.PROV.local("hadPlan");
	}
	
	public static final class Usage
	{
		public static URI uri=URINameSpace.PROV.local("Usage");
		public static URI role=URINameSpace.PROV.local("hadRole");
		public static URI entity=URINameSpace.PROV.local("entity");
		
	}
	
	public static final class Activity
	{
		public static URI uri=URINameSpace.PROV.local("Activity");
		public static URI startedAtTime=URINameSpace.PROV.local("startedAtTime");
		public static URI endedAtTime=URINameSpace.PROV.local("endedAtTime");
		public static URI type=URINameSpace.SBOL.local("type");
		public static URI qualifiedUsage=URINameSpace.PROV.local("qualifiedUsage");
		public static URI qualifiedAssociation=URINameSpace.PROV.local("qualifiedAssociation");
		public static URI wasInformedBy=URINameSpace.PROV.local("wasInformedBy");
	}
	
	public static final class Plan
	{
		public static URI uri=URINameSpace.PROV.local("Plan");
	}
		
}
