package org.sbolstandard.vocabulary;

import java.net.URI;

import org.apache.jena.ext.xerces.xni.QName;
import org.sbolstandard.NameSpace;

public class Role {
	
		public static URI Promoter = NameSpace.SO.local("0000167");
		public static URI RBS = NameSpace.SO.local("0000139");
		public static URI CDS = NameSpace.SO.local("0000316");
		public static URI Terminator = NameSpace.SO.local("0000141");
		public static URI Gene = NameSpace.SO.local("0000704");
		public static URI Operator = NameSpace.SO.local("0000057");
		public static URI EngineeredGene = NameSpace.SO.local("0000704");
		
		public static URI mRNA = NameSpace.SO.local("0000234");
		public static URI Effector = NameSpace.CHEBI.local("35224");
		public static URI TF = NameSpace.GO.local("0003700");
		
}

