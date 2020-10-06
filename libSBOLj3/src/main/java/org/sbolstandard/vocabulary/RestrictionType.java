package org.sbolstandard.vocabulary;

import java.net.URI;
import org.sbolstandard.util.URINameSpace;

public class RestrictionType {
	public static class Orientation
	{
		public static URI sameOrientationAs = URINameSpace.SBOL.local("sameOrientationAs");
		public static URI oppositeOrientationAs = URINameSpace.SBOL.local("oppositeOrientationAs");
	}
	
	public static class Identity
	{
		public static URI verifyIdentical = URINameSpace.SBOL.local("verifyIdentical");
		public static URI differentFrom = URINameSpace.SBOL.local("differentFrom");
		public static URI replaces = URINameSpace.SBOL.local("replaces");
	}
	
	public static class Topology
	{
		public static URI isDisjointFrom = URINameSpace.SBOL.local("isDisjointFrom");
		public static URI strictlyContains = URINameSpace.SBOL.local("strictlyContains");
		public static URI contains = URINameSpace.SBOL.local("contains");
		public static URI equals = URINameSpace.SBOL.local("equals");
		public static URI meets = URINameSpace.SBOL.local("meets");
		public static URI covers = URINameSpace.SBOL.local("covers");
		public static URI overlaps = URINameSpace.SBOL.local("overlaps");	
	}
	
	public static class Sequential
	{
		public static URI precedes = URINameSpace.SBOL.local("precedes");
		public static URI strictlyPrecedes = URINameSpace.SBOL.local("strictlyPrecedes");
		public static URI meets = URINameSpace.SBOL.local("meets");
		public static URI overlaps = URINameSpace.SBOL.local("overlaps");
		public static URI contains = URINameSpace.SBOL.local("contains");
		public static URI strictlyContains = URINameSpace.SBOL.local("strictlyContains");
		public static URI equals = URINameSpace.SBOL.local("equals");	
		public static URI finishes = URINameSpace.SBOL.local("finishes");	
		public static URI starts = URINameSpace.SBOL.local("starts");	
	}		
}

