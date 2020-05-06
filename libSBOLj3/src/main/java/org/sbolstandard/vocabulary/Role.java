package org.sbolstandard.vocabulary;

import java.net.URI;

import org.apache.jena.ext.xerces.xni.QName;

public class Role {
	public static final String SO_Namespace="https://identifiers.org/SO:";
	public static final String SBO_Namespace="https://identifiers.org/SBO:";
	public static final String CHEBI_Namespace="https://identifiers.org/CHEBI:";
	public static final String GO_Namespace="https://identifiers.org/GO:";
	
		public static URI Promoter = URI.create(SO_Namespace + "0000167");
		public static URI RBS = URI.create(SO_Namespace + "0000139");
		public static URI CDS = URI.create(SO_Namespace + "0000316");
		public static URI Terminator = URI.create(SO_Namespace + "0000141");
		public static URI Gene = URI.create(SO_Namespace + "0000704");
		public static URI Operator = URI.create(SO_Namespace + "0000057");
		public static URI EngineeredGene = URI.create(SO_Namespace + "0000704");
		
		public static URI mRNA = URI.create(SO_Namespace + "0000234");
		public static URI Effector = URI.create(CHEBI_Namespace + "35224");
		public static URI TF = URI.create(GO_Namespace + "0003700");
		
}

