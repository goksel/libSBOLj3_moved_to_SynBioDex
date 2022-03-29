package org.sbolstandard.core3.vocabulary;

import java.net.URI;

import org.sbolstandard.core3.util.URINameSpace;

public enum ActivityType {
	Design(URINameSpace.SBOL.local("design")), 
	Build(URINameSpace.SBOL.local("build")), 
	Test(URINameSpace.SBOL.local("test")), 
	Learn(URINameSpace.SBOL.local("learn")); 
	
	private URI url;

	ActivityType(URI envUrl) {
		this.url = envUrl;
	}

	public URI getUrl() {
		return url;
	}
}
