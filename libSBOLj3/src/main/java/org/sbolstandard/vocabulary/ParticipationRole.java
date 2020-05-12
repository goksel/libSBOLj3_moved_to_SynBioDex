package org.sbolstandard.vocabulary;

import java.net.URI;

import org.apache.jena.ext.xerces.xni.QName;
import org.sbolstandard.NameSpace;

public class ParticipationRole {
	public static URI Inhibitor = NameSpace.SBO.local("0000020");
	public static URI Inhibited = NameSpace.SBO.local("0000642");
	public static URI Stimulator = NameSpace.SBO.local("0000459");
	public static URI Stimulated = NameSpace.SBO.local("0000643");
	public static URI Reactant = NameSpace.SBO.local("0000010");
	public static URI Product = NameSpace.SBO.local("0000011");
	public static URI Promoter = NameSpace.SBO.local("0000598");
	public static URI Modifier = NameSpace.SBO.local("0000019");
	public static URI Modified = NameSpace.SBO.local("0000644");
	public static URI Template = NameSpace.SBO.local("0000645");
}