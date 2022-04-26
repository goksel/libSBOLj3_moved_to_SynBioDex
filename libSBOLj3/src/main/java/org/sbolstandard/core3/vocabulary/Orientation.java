package org.sbolstandard.core3.vocabulary;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import org.sbolstandard.core3.util.URINameSpace;

public enum Orientation {
	inline(URINameSpace.SO.local("0001030")), 
	reverseComplement(URINameSpace.SO.local("0001031"));

	private URI uri;

	Orientation(URI uri) {
		this.uri = uri;
	}

	public URI getUri() {
		return uri;
	}
	
	private static final Map<URI, Orientation> lookup = new HashMap<>();
	  
    static
    {
        for(Orientation orientation : Orientation.values())
        {
            lookup.put(orientation.getUri(), orientation);
        }
        lookup.put(URINameSpace.SBOL.local("inline"), inline);
        lookup.put(URINameSpace.SBOL.local("reverseComplement"), reverseComplement);
        
    }
  
    public static Orientation get(URI uri) 
    {
        return lookup.get(uri);
    }

}
