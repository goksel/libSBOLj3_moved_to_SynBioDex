package org.sbolstandard.vocabulary;

import java.net.URI;
import org.sbolstandard.util.URINameSpace;

public class ParticipationRole {
	public static URI SBML = URINameSpace.SBO.local("0000020");
	public static URI Inhibited = URINameSpace.SBO.local("0000642");
	public static URI Stimulator = URINameSpace.SBO.local("0000459");
	public static URI Stimulated = URINameSpace.SBO.local("0000643");
	public static URI Reactant = URINameSpace.SBO.local("0000010");
	public static URI Product = URINameSpace.SBO.local("0000011");
	public static URI Promoter = URINameSpace.SBO.local("0000598");
	public static URI Modifier = URINameSpace.SBO.local("0000019");
	public static URI Modified = URINameSpace.SBO.local("0000644");
	public static URI Template = URINameSpace.SBO.local("0000645");
}