package org.sbolstandard.vocabulary;

import java.net.URI;
import org.sbolstandard.util.URINameSpace;

public class InteractionType {
	
		public static URI Inhibition = URINameSpace.SBO.local("0000169");
		public static URI Stimulation = URINameSpace.SBO.local("0000170");
		public static URI BiochemicalReaction = URINameSpace.SBO.local("0000176");
		public static URI NonCovalentBinding = URINameSpace.SBO.local("0000177");
		public static URI Degradation = URINameSpace.SBO.local("0000179");
		public static URI GeneticProduction = URINameSpace.SBO.local("0000589");
		public static URI Control = URINameSpace.SBO.local("0000168");
}
