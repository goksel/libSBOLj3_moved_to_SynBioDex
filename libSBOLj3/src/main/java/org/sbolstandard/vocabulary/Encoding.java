package org.sbolstandard.vocabulary;
import java.net.URI;
public enum Encoding
{
	 NucleicAcid(URI.create("http://sbols.org/v3#iupacNucleicAcid")), 
	    AminoAcid(URI.create("http://sbols.org/v3#iupacAminoAcid")),
	    SMILES(URI.create("http://www.opensmiles.org/opensmiles.html"));
	 
	    private URI url;
	 
	    Encoding(URI envUrl) {
	        this.url = envUrl;
	    }
	 
	    public URI getUrl() {
	        return url;
	    }
}

