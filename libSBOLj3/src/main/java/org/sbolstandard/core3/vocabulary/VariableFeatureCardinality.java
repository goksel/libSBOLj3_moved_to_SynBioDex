package org.sbolstandard.core3.vocabulary;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import org.sbolstandard.core3.util.URINameSpace;

public enum VariableFeatureCardinality {
	
	ZeroOrOne(URINameSpace.SBOL.local("zeroOrOne")), 
	One(URINameSpace.SBOL.local("one")),
	ZeroOrMore(URINameSpace.SBOL.local("zeroOrMore")), 
	OneOrMore(URINameSpace.SBOL.local("oneOrMore"));

	private URI uri;

	VariableFeatureCardinality(URI envUrl) {
		this.uri = envUrl;
	}

	public URI getUri() {
		return uri;
	}
	
	private static final Map<URI, VariableFeatureCardinality> lookup = new HashMap<>();

	static
	{
	    for(VariableFeatureCardinality encoding: VariableFeatureCardinality.values())
	    {
	        lookup.put(encoding.getUri(), encoding);
	    }
	}

	public static VariableFeatureCardinality get(URI uri) 
	{
	    return lookup.get(uri);
	}
}
