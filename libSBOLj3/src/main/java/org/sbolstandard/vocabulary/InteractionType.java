package org.sbolstandard.vocabulary;

import java.net.URI;

import org.apache.jena.ext.xerces.xni.QName;
import org.sbolstandard.NameSpace;

public class InteractionType {
	
		public static URI Inhibition = NameSpace.SBO.local("0000169");
		public static URI Stimulation = NameSpace.SBO.local("0000170");
		public static URI BiochemicalReaction = NameSpace.SBO.local("0000176");
		public static URI NonCovalentBinding = NameSpace.SBO.local("0000177");
		public static URI Degradation = NameSpace.SBO.local("0000179");
		public static URI GeneticProduction = NameSpace.SBO.local("0000589");
		public static URI Control = NameSpace.SBO.local("0000168");
}
