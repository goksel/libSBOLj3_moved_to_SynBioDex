package org.sbolstandard.vocabulary;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import org.sbolstandard.util.URINameSpace;
public enum Encoding
{
	 	NucleicAcid(URINameSpace.SBOL.local("iupacNucleicAcid")), 
	    AminoAcid(URINameSpace.SBOL.local("iupacAminoAcid")),
	    SMILES(URI.create("http://www.opensmiles.org/opensmiles.html"));
	 
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



