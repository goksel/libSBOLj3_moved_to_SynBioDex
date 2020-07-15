package org.sbolstandard.vocabulary;
import java.net.URI;

import org.sbolstandard.util.URINameSpace;
public enum Encoding
{
	 	NucleicAcid(URINameSpace.SBOL.local("iupacNucleicAcid")), 
	    AminoAcid(URINameSpace.SBOL.local("iupacAminoAcid")),
	    SMILES(URI.create("http://www.opensmiles.org/opensmiles.html"));
	 
	    private URI url;
	 
	    Encoding(URI envUrl) {
	        this.url = envUrl;
	    }
	 
	    public URI getUrl() {
	        return url;
	    }
}

