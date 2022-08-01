package org.sbolstandard.core3.vocabulary;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import org.sbolstandard.core3.util.URINameSpace;
public enum CombinatorialDerivationStrategy
{
	 	Enumerate(URINameSpace.SBOL.local("enumerate")), 
	 	Sample(URINameSpace.SBOL.local("sample"));
	 	
	    private URI url;
	 
	    CombinatorialDerivationStrategy(URI envUrl) {
	        this.url = envUrl;
	    }
	 
	    public URI getUri() {
	        return url;
	    }
	    
	    private static final Map<URI, CombinatorialDerivationStrategy> lookup = new HashMap<>();
		  
	    static
	    {
	        for(CombinatorialDerivationStrategy strategy : CombinatorialDerivationStrategy.values())
	        {
	            lookup.put(strategy.getUri(), strategy);
	        }
	    }
	  
	    public static CombinatorialDerivationStrategy get(URI uri) 
	    {
	        return lookup.get(uri);
	    }
}


