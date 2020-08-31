package org.sbolstandard.vocabulary;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import org.sbolstandard.util.URINameSpace;

public enum Orientation {
	inline(URINameSpace.SBOL.local("inline")), 
	reverseComplement(URINameSpace.SBOL.local("reverseComplement"));

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
    }
  
    public static Orientation get(URI uri) 
    {
        return lookup.get(uri);
    }

}
