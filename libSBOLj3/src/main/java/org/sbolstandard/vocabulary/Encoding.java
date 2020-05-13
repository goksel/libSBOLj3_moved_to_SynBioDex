package org.sbolstandard.vocabulary;
import java.net.URI;

import org.sbolstandard.util.NameSpace;
public enum Encoding
{
	 	NucleicAcid(NameSpace.SBOL.local("iupacNucleicAcid")), 
	    AminoAcid(NameSpace.SBOL.local("iupacAminoAcid")),
	    SMILES(URI.create("http://www.opensmiles.org/opensmiles.html"));
	 
	    private URI url;
	 
	    Encoding(URI envUrl) {
	        this.url = envUrl;
	    }
	 
	    public URI getUrl() {
	        return url;
	    }
}

