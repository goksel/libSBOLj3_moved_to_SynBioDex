package org.sbolstandard.vocabulary;

import java.net.URI;
import org.sbolstandard.NameSpace;

public enum Orientation {
	inline(NameSpace.SBOL.local("inline")), 
	reverseComplement(NameSpace.SBOL.local("reverseComplement"));

	private URI url;

	Orientation(URI envUrl) {
		this.url = envUrl;
	}

	public URI getUrl() {
		return url;
	}
}
