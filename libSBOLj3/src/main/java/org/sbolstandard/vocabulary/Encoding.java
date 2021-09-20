package org.sbolstandard.vocabulary;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import org.sbolstandard.util.URINameSpace;
public enum Encoding
{
	 	NucleicAcid(URINameSpace.EDAM.local("format_1207")), 
	    AminoAcid(URINameSpace.EDAM.local("format_1208")),
	    SMILES(URINameSpace.EDAM.local("format_1196")),
    	INCHI(URINameSpace.EDAM.local("format_1197"));
	 
	    private URI uri;
	 
	    Encoding(URI uri) {
	        this.uri = uri;
	    }
	 
	    public URI getUri() {
	        return uri;
	    }
	    
	    private static final Map<URI, Encoding> lookup = new HashMap<>();
	    
	    static
	    {
	        for(Encoding encoding: Encoding.values())
	        {
	            lookup.put(encoding.getUri(), encoding);
	        }
	    }

	    public static Encoding get(URI uri) 
	    {
	        return lookup.get(uri);
	    }
	    
	    
}



