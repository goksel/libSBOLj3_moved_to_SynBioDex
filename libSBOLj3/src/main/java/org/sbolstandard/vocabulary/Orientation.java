package org.sbolstandard.vocabulary;

import java.net.URI;

import org.sbolstandard.util.URINameSpace;

public enum Orientation {
	inline(URINameSpace.SBOL.local("inline")), 
	reverseComplement(URINameSpace.SBOL.local("reverseComplement"));

	private URI url;

	Orientation(URI envUrl) {
		this.url = envUrl;
	}

	public URI getUrl() {
		return url;
	}
}
