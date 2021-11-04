package org.sbolstandard.core3.vocabulary;
import java.net.URI;

import org.sbolstandard.core3.util.URINameSpace;
public enum CombinatorialDerivationStrategy
{
	 	Enumerate(URINameSpace.SBOL.local("enumerate")), 
	 	Local(URINameSpace.SBOL.local("local"));
	 	
	    private URI url;
	 
	    CombinatorialDerivationStrategy(URI envUrl) {
	        this.url = envUrl;
	    }
	 
	    public URI getUrl() {
	        return url;
	    }
}


