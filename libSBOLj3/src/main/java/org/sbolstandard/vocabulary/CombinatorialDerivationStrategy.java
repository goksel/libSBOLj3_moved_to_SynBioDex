package org.sbolstandard.vocabulary;
import java.net.URI;

import org.sbolstandard.util.URINameSpace;
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


