package org.sbolstandard.vocabulary;

import java.net.URI;

import org.sbolstandard.util.URINameSpace;

public enum VariableComponentCardinality {
	
	ZeroOrOne(URINameSpace.SBOL.local("zeroOrOne")), 
	One(URINameSpace.SBOL.local("one")),
	ZeroOrMore(URINameSpace.SBOL.local("zeroOrMore")), 
	OneOrMore(URINameSpace.SBOL.local("oneOrMore"));

	private URI url;

	VariableComponentCardinality(URI envUrl) {
		this.url = envUrl;
	}

	public URI getUrl() {
		return url;
	}
}
